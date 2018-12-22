/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.ui;

import budgetbuddy.Helpper;
import budgetbuddy.domain.BudgetManager;
import budgetbuddy.domain.Item;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author markus
 */
public class ViewItemsWindow {
    
    private static ReWrite namePlate(Item item, BudgetManager manager) {
        ReWrite type = new ReWrite(item.getName());
        type.setOnFocusLost(eh -> {
            item.setName(type.getText());
            try {
                manager.update(item);
            } catch (SQLException ex) {
                Alerts.sqlAlert(ex.getMessage());
            }
        });
        return type;
    }
    
    private static ReWrite typePlate(Item item, BudgetManager manager) {
        ReWrite type = new ReWrite(item.getType());
        type.setOnFocusLost(eh -> {
            item.setType(type.getText());
            try {
                manager.update(item);
            } catch (SQLException ex) {
                Alerts.sqlAlert(ex.getMessage());
            }
        });
        return type;
    }
    
    private static ReWrite combinedPlate(Item item) {
        double price = (double) item.getPrice() * item.getAmount() / 100.0;
        ReWrite combined = new ReWrite(String.format("%.2f", price));
        return combined;
    }
    
    private static ReWrite amountPlate(Item item) {
        return new ReWrite(String.format("%s", item.getAmount()));
    }
    
    private static ReWrite induvidualPlate(Item item) {
        double price = (double) item.getPrice() / 100.0;
        return new ReWrite(String.format("%.2f", price));
    }
    
    private static DatePicker createDatePicker(Set<Date> dates) {
        Set<LocalDate> localDates = dates.stream().map(date -> Helpper.dateToLocalDate(date)).collect(Collectors.toSet());
        DatePicker date = new DatePicker(Collections.max(localDates));
        
        date.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                setDisable(empty || !localDates.contains(localDate));
            }
        });
        
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                      DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    LocalDate date = LocalDate.parse(string, dateFormatter);
                    if (!localDates.contains(date)) {
                        throw new IllegalStateException("Date does not have any purchases");
                    }
                    return date;
                } else {
                    return null;
                }
            }
        };   
        date.setConverter(converter);
        return date;
    }
    
    private static GridPane updateItemGrid(GridPane itemGrid, Date date, BudgetManager manager) {
        Set<Node> removable = new HashSet<>();
        for (Node child : itemGrid.getChildren()) {
            removable.add(child);   
        }
        itemGrid.getChildren().removeAll(removable);
        
        Set<Integer> deletableIds = new HashSet<>();
        List<Item> items;
        try {
            items = manager.getFromTo(date, date);
        } catch (Exception ex) {
            Alerts.sqlAlert(ex.getMessage());
            items = null;
        }
        itemGrid.setHgap(10);
        itemGrid.setVgap(3);
        itemGrid.add(new Label("Name"), 0, 0);
        itemGrid.add(new Label("type"), 1, 0);
        itemGrid.add(new Label("Combined €"), 2, 0);
        itemGrid.add(new Label("Amount"), 3, 0);
        itemGrid.add(new Label("Induvidual €"), 4, 0);
        
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(eh -> {
            try {
                manager.delete(deletableIds);
            } catch (SQLException ex) {
                Alerts.sqlAlert(ex.getMessage());
            }
            updateItemGrid(itemGrid, date, manager);
        });
        itemGrid.add(deleteButton, 5, 0);
        
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            itemGrid.add(namePlate(item, manager), 0, i + 1);
            itemGrid.add(typePlate(item, manager), 1, i + 1);
            
            ReWrite combined = combinedPlate(item);
            itemGrid.add(combined, 2, i + 1);
            ReWrite amount = amountPlate(item);
            itemGrid.add(amount, 3, i + 1);
            ReWrite induvidual = induvidualPlate(item);
            itemGrid.add(induvidual, 4, i + 1);
            
            combined.setOnFocusLost(eh -> {
                try {
                    double combinedValue = Double.parseDouble(combined.getText().trim().replace(",", "."));
//                    warnig.setText(" ");
                    double induvidualValue;
                    try {
                        int amountValue = Integer.parseInt(amount.getText().trim());
                        induvidualValue = combinedValue / amountValue;
                    } catch (Exception e) {
                        induvidualValue = combinedValue;
                    }
                    induvidual.setText(String.format("%.2f", induvidualValue));
                    item.setPrice((int) (induvidualValue * 100));
                    manager.update(item);
                    combined.setText(String.format("%.2f", combinedValue));
                } catch (Exception e) {
//                    warnig.setText("Combindevalue incorrect");
                    double combinendValue = (item.getPrice() * item.getAmount() / 100.0);
                    combined.setText(String.format("%.2f", combinendValue));
                }
            });
            
            amount.setOnFocusLost(eh -> {
                try {
                    double amountValue = Integer.parseInt(amount.getText().trim());
//                    warnig.setText(" ");
                    double induvidualValue;
                    try {
                        double combinedValue = Double.parseDouble(combined.getText().trim().replace(",", "."));
                        induvidualValue = combinedValue / amountValue;
//                        warnig.setText(" ");
                        if (amountValue == 0) {
                            throw new IllegalArgumentException("Division by zero");
                        }
                    } catch (Exception e) {
                        induvidualValue = 0;
//                        warnig.setText("Combindevalue incorrect");
                    }
                    induvidual.setText(String.format("%.2f", induvidualValue));
                    item.setAmount((int) amountValue);
                    manager.update(item);
                    amount.setText(String.format("%s", amountValue));
                } catch (Exception e) {
//                    warnig.setText("Amount incorrect");
                    amount.setText(String.format("%s", item.getAmount()));
                }
            });
            
            induvidual.setOnFocusLost(eh -> {
                try {
                    double induvidualValue = Double.parseDouble(induvidual.getText().trim().replace(",", "."));
//                    warnig.setText(" ");
                    double combinedValeu;
                    try {
                        int amountValue = Integer.parseInt(amount.getText().trim());
                        combinedValeu = induvidualValue * amountValue;
                    } catch (Exception e) {
                        combinedValeu = induvidualValue;
                    }
                    combined.setText(String.format("%.2f", combinedValeu));
                    item.setPrice((int) (induvidualValue * 100));
                    manager.update(item);
                    induvidual.setText(String.format("%.2f", induvidualValue));
                } catch (Exception e) {
//                    warnig.setText("Combindevalue incorrect");
                    double induvidualValue = (item.getPrice() / 100.0);
                    combined.setText(String.format("%.2f", induvidualValue));
                }
            });
            
            CheckBox delete = new CheckBox();
            delete.setOnAction(eh -> {
                if (delete.isSelected()) {
                    deletableIds.add(item.getId());
                } else {
                    deletableIds.remove(item.getId());
                }
            });
            itemGrid.add(delete, 5, i + 1);
        }
        
        
        return itemGrid;
    }
    
    
    public static void open(BudgetManager manager, Stage mainStage) {
        Date date1 = null;
        try {
            date1 = Helpper.yearMonthDayToDate("2018-08-10");
        } catch (Exception ex) {
            Logger.getLogger(ViewItemsWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Item item1 = new Item("Item 1", "item", date1, 395, 10, 3);
        
        Stage newItemWindow = new Stage();
        newItemWindow.setTitle("View items");
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(10, 10, 10, 20));
        frame.setPrefSize(550, 400);
        GridPane itemGrid = new GridPane();
        
        try {
            DatePicker datePicker = createDatePicker(manager.getDates());
            datePicker.setOnAction((ActionEvent eh) -> {
                Date date = Helpper.loaclDateToDate(datePicker.getValue());
                updateItemGrid(itemGrid, date, manager);
                frame.setCenter(itemGrid);
            });
            
            HBox dateInput = new HBox();
            dateInput.getChildren().addAll(new Label("Date: "), datePicker);  
            frame.setTop(dateInput);
            
            updateItemGrid(itemGrid, Helpper.loaclDateToDate(datePicker.getValue()), manager);
            frame.setCenter(itemGrid);
        } catch (Exception ex) {
            Alerts.sqlAlert(ex.getMessage());
        }
        
        Button close = new Button("Close");
        frame.setBottom(close);

//        GridPane itemGrid = new GridPane();
//        itemGrid.add(namePlate(item1), 0, 0);
//        itemGrid.add(typePlate(item1), 1, 0);
        Scene scene = new Scene(frame);
        newItemWindow.setScene(scene);
        
        close.setOnAction(eh -> {
            newItemWindow.close();
        });
        
//        newItemWindow.setResizable(false);
        newItemWindow.setX(mainStage.getX() + mainStage.getWidth() / 2 - 130);
        newItemWindow.setY(mainStage.getY() + mainStage.getHeight() / 2 - 50);
        newItemWindow.showAndWait();
    }
    
    
}
