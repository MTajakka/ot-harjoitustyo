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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author markus
 */
public class AddItemsWindow {
    
    private static GridPane updateInputGrid(GridPane inputGrid, List<Item> items) {
        Set<Node> removable = new HashSet<>();
        for (Node child : inputGrid.getChildren()) {
            if (GridPane.getRowIndex(child) > 1) {
                removable.add(child);   
            }
        }
        inputGrid.getChildren().removeAll(removable);
        for (int i = 0; i < items.size(); i++) {
            int j = i + 2;
            inputGrid.add(new Label(items.get(i).getName()), 0, j);
            inputGrid.add(new Label(items.get(i).getType()), 1, j);
            inputGrid.add(new Label(String.format("%.2f", items.get(i).getAmount() * (double) items.get(i).getPrice() / 100)), 2, j);
            inputGrid.add(new Label(String.format("%s", items.get(i).getAmount())), 3, j);
            inputGrid.add(new Label(String.format("%.2f", (double) items.get(i).getPrice() / 100)), 4, j);
            Button delete = new Button("Delete");
            delete.setPadding(new Insets(2, 5, 2, 5));
            delete.setOnAction(eh -> {
                items.remove(j - 2);
                updateInputGrid(inputGrid, items);
            });
            inputGrid.add(delete, 5, j);
        }
        return inputGrid;
    }
    
    public static void open(BudgetManager manager, Stage mainStage) {
        List<Item> items = new ArrayList<>();
        
        Stage newItemWindow = new Stage();
        newItemWindow.setTitle("Add items");
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(10, 10, 10, 30));
        
        HBox dateInput = new HBox();
        DatePicker date = new DatePicker(LocalDate.now());
        dateInput.getChildren().addAll(new Label("Date: "), date);  
        
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
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };   
        date.setConverter(converter);
        
        HBox top = new HBox();
        Label warnig = new Label(" ");
        top.setSpacing(105);
        top.getChildren().addAll(dateInput, warnig);
        frame.setTop(top);
        
        GridPane inputGrid = new GridPane();
        inputGrid.setPrefSize(500, 400);
        inputGrid.setAlignment(Pos.TOP_CENTER);
        inputGrid.add(new Label("Name"), 0, 0);
        inputGrid.add(new Label("type"), 1, 0);
        inputGrid.add(new Label("Combined €"), 2, 0);
        inputGrid.add(new Label("Amount"), 3, 0);
        inputGrid.add(new Label("Induvidual €"), 4, 0);
        
        TextField name = new TextField();
        name.setPrefWidth(100);
        inputGrid.add(name, 0, 1);
        
        TextField type = new TextField();
        type.setPrefWidth(100);
        inputGrid.add(type, 1, 1);
        
        TextField combined = new TextField();
        combined.setPrefWidth(100);
        inputGrid.add(combined, 2, 1);
        
        TextField amount = new TextField();
        amount.setPrefWidth(60);
        inputGrid.add(amount, 3, 1);
        
        TextField induvidual = new TextField();
        induvidual.setPrefWidth(100);
        inputGrid.add(induvidual, 4, 1);
        
        combined.setOnKeyReleased(eh -> {
            try {
                double combinedValue = Double.parseDouble(combined.getText().trim().replace(",", "."));
                warnig.setText(" ");
                double induvidualValue;
                try {
                    int amountValue = Integer.parseInt(amount.getText().trim());
                    induvidualValue = combinedValue / amountValue;
                } catch (Exception e) {
                    induvidualValue = combinedValue;
                }
                induvidual.setText(String.format("%.2f", induvidualValue));
            } catch (Exception e) {
                warnig.setText("Combindevalue incorrect");
            }
        });
        
        amount.setOnKeyReleased(eh -> {
            try {
                double amountValue = Integer.parseInt(amount.getText().trim());
                warnig.setText(" ");
                double induvidualValue;
                try {
                    double combinedValue = Double.parseDouble(combined.getText().trim().replace(",", "."));
                    induvidualValue = combinedValue / amountValue;
                    warnig.setText(" ");
                    if (amountValue == 0) {
                        throw new IllegalArgumentException("Division by zero");
                    }
                } catch (Exception e) {
                    induvidualValue = 0;
                    warnig.setText("Combindevalue incorrect");
                }
                induvidual.setText(String.format("%.2f", induvidualValue));
            } catch (Exception e) {
                warnig.setText("Amount incorrect");
            }
        });
        
        induvidual.setOnKeyReleased(eh -> {
            try {
                double induvidualValue = Double.parseDouble(induvidual.getText().trim().replace(",", "."));
                warnig.setText(" ");
                double combinedValeu;
                try {
                    int amountValue = Integer.parseInt(amount.getText().trim());
                    combinedValeu = induvidualValue * amountValue;
                } catch (Exception e) {
                    combinedValeu = induvidualValue;
                }
                combined.setText(String.format("%.2f", combinedValeu));
            } catch (Exception e) {
                warnig.setText("Combindevalue incorrect");
            }
        });
                
        Button add = new Button("Add");
        add.setOnAction(eh -> {
            int amountValue = 0;
            double induvidualValue = 0;
            String nameValue = name.getText().trim();
            String typeValue = type.getText().trim().toLowerCase();
            try {
                amountValue = Integer.parseInt(amount.getText().trim());
                induvidualValue = Double.parseDouble(induvidual.getText().trim().replace(",", "."));
                if (amountValue > 0 && !nameValue.isEmpty() && !typeValue.isEmpty()) {
                    Item item = new Item(nameValue, typeValue, new Date(), (int) (induvidualValue * 100), amountValue);
                    items.add(item);
                    name.clear();
                    type.clear();
                    combined.clear();
                    amount.clear();
                    induvidual.clear();
                    updateInputGrid(inputGrid, items);
                    
                }
            } catch (Exception e) {
                warnig.setText("could not parse values");
            }
        });
        inputGrid.add(add, 5, 1);
        
        frame.setCenter(inputGrid);
        Button finish = new Button("Finish");
        finish.setOnAction(eh -> {
            if (!items.isEmpty()) {
                if (date.getValue() == null) {
                    warnig.setText("Date not Specified");
                } else {
                    for (Item item : items) {
                        item.setDate(Helpper.loaclDateToDate(date.getValue()));
                    }
                    try {
                        manager.add(items);
                        newItemWindow.close();
                    } catch (SQLException ex) {
                        Alerts.sqlAlert(ex.getMessage());
                    }
                }
            } else {
                warnig.setText("No items selected");
            }
        });
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction(eh -> {
            newItemWindow.close();
        });
        
        HBox bottom = new HBox();
        bottom.getChildren().addAll(finish, cancel);
        frame.setBottom(bottom);
        
        
        Scene scene = new Scene(frame);
        newItemWindow.setScene(scene);
        newItemWindow.setResizable(false);
        newItemWindow.setX(mainStage.getX() + mainStage.getWidth() / 2 - 130);
        newItemWindow.setY(mainStage.getY() + mainStage.getHeight() / 2 - 50);
        newItemWindow.showAndWait();
    }
    
}
