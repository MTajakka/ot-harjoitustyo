package budgetbuddy.ui;

import budgetbuddy.Helpper;
import budgetbuddy.dao.DatabaseItemDao;
import budgetbuddy.dao.DatabaseUserDao;
import budgetbuddy.dao.ItemDao;
import budgetbuddy.dao.UserDao;
import budgetbuddy.domain.BudgetManager;
import budgetbuddy.domain.Item;
import budgetbuddy.domain.User;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BudgetBudyUi extends Application {
    private UserDao usersDB;
    private Stage mainStage;
    private final String database = "database/budgetBuddyDatabase.db";
    private ItemDao usersItemDao;
    BudgetManager manager;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("BudgetBuddy");
        File directory = new File("database");
        if (! directory.exists()){
            directory.mkdir();
        }
        try {
            usersDB = new DatabaseUserDao(database, "Users");
        } catch (SQLException ex) {
            Logger.getLogger(BudgetBudyUi.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainStage.setScene(loginScene());
        mainStage.show();
//        addItemsWindow();
    }
    
    private void addUserWindow() {
        Stage newUserWindow = new Stage();
        newUserWindow.setTitle("New User");
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(10, 10, 10, 30));
        
        HBox information = new HBox();
        information.setSpacing(5);
        information.setPadding(new Insets(10, 0, 10, 0));
        Label title = new Label("Name:");
        TextField nameInput = new TextField();
        information.getChildren().addAll(title, nameInput);
        frame.setCenter(information);
        
        Button createNew = new Button("Create user");
        frame.setBottom(createNew);
        
        frame.setTop(new Label(" "));
        
        createNew.setOnAction(e -> {
            String name = nameInput.getText().trim();
            if (name.isEmpty()) {
                frame.setTop(new Label("Please give a name"));  
            } else if (name.contains(" ")) {
                frame.setTop(new Label("Name should not have spaces"));
            } else if (name.length() > 12) {
                frame.setTop(new Label("Name too long. Try using less\nor equal than 12 characters"));
            } else {
                try {
                    String table = name + "sTable";
                    if (usersDB.containsTable(table)) {
                        int i = 1;
                        while (usersDB.containsTable(table + i)) {
                            i += 1;
                        }
                        table += i;
                    }
                    if (usersDB.containsName(name)) {
                        frame.setTop(new Label("Name already used"));
                    } else {
                        User user = new User(name, database, table);
                        usersDB.add(user);
                        mainStage.setScene(loginScene());
                        newUserWindow.close();
                    }
                } catch (SQLException ex) {
                    frame.setTop(new Label(ex.getMessage() + " occured add other name"));
                }
            }
        });
        
        Scene scene = new Scene(frame);
        newUserWindow.setScene(scene);
        newUserWindow.setResizable(false);
        newUserWindow.setX(mainStage.getX() + mainStage.getWidth() / 2 - 130);
        newUserWindow.setY(mainStage.getY() + mainStage.getHeight() / 2 - 50);
        newUserWindow.showAndWait();
    }
    
    private void editUserWindow(User user) {
        Stage newUserWindow = new Stage();
        newUserWindow.setTitle("Eidt User");
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(10, 10, 10, 30));
        
        HBox information = new HBox();
        information.setSpacing(5);
        information.setPadding(new Insets(10, 0, 10, 0));
        Label title = new Label("Name:");
        TextField nameInput = new TextField();
        information.getChildren().addAll(title, nameInput);
        frame.setCenter(information);
        
        Button rename = new Button("Rename user");
        frame.setBottom(rename);
        
        frame.setTop(new Label(" "));
        
        rename.setOnAction(e -> {
            String name = nameInput.getText().trim();
            if (name.isEmpty()) {
                frame.setTop(new Label("Please give a name"));  
            } else if (name.contains(" ")) {
                frame.setTop(new Label("Name should not have spaces"));
            } else if (name.length() > 12) {
                frame.setTop(new Label("Name too long. Try using less\nor equal than 12 characters"));
            } else {
                try {
                    if (usersDB.containsName(name)) {
                        frame.setTop(new Label("Name already used"));
                    } else {
                        user.setName(name);
                        usersDB.update(user);
                        mainStage.setScene(loginScene());
                        newUserWindow.close();
                    }
                } catch (SQLException ex) {
                    frame.setTop(new Label(ex.getMessage() + " occured add other name"));
                }
            }
        });
        
        Scene scene = new Scene(frame);
        newUserWindow.setScene(scene);
        newUserWindow.setResizable(false);
        newUserWindow.setX(mainStage.getX() + mainStage.getWidth() / 2 - 130);
        newUserWindow.setY(mainStage.getY() + mainStage.getHeight() / 2 - 50);
        newUserWindow.showAndWait();
    }
    
    private Node userNode(User user) {
        String nameSt = user.getName();
        Label name = new Label(nameSt);
        Button login = new Button("Login");
        Button delete = new Button("Delete");
        Button edit = new Button("Edit");
        edit.setPadding(new Insets(2, 5, 2, 5));
        
        login.setOnAction(e -> {
            mainStage.setScene(userScreen(user));
        });
        
        delete.setOnAction(e -> {
            if (Alerts.confirmationAlert("You are about to delete " + nameSt
                    + "'s account and everything related to it")) {
                try {
                    usersDB.delete(user.getId());
                    mainStage.setScene(loginScene());                
                } catch (SQLException ex) {
                    Alerts.sqlAlert(ex.getMessage());
                }
            }
        });
        
        edit.setOnAction(e -> {
            editUserWindow(user);
        });
        
        HBox nameplate = new HBox();
        nameplate.setSpacing(5);
        nameplate.getChildren().addAll(name, edit);
        
        HBox controlls = new HBox();
        controlls.setSpacing(5);
        controlls.getChildren().addAll(delete, login);
        
        VBox frame = new VBox();
        frame.setSpacing(5);
        frame.setPadding(new Insets(10));
        frame.setMinWidth(140);
        frame.getChildren().addAll(nameplate, controlls);
        
        return frame;
    }
    
    private Scene loginScene() {        
        // userers grid
        GridPane userGrid = new GridPane();
        userGrid.setPadding(new Insets(10, 10, 10, 10));
        userGrid.setAlignment(Pos.CENTER);
        
        // utility controlls
        Button addUser = new Button("Add User");
        HBox hbox = new HBox();
        hbox.getChildren().add(addUser);
        
        addUser.setOnAction(e -> {
            addUserWindow();
        });
        
        // main layout
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(20, 20, 20, 20));
        frame.setPrefSize(600, 400);
        frame.setCenter(userGrid);
        frame.setBottom(hbox);
        
        // show avilable users
        List<User> users;
        try {
            users = usersDB.getAll();
        } catch (SQLException ex) {
            users = new ArrayList<>();
            Alerts.sqlAlert(ex.getMessage());
        }
        if (users.isEmpty()) {
            frame.setCenter(new Label("No users found."));
        } else {
            double scale = 5.0 / 3.0;
            int columns = (int) Math.floor(Math.sqrt(users.size() * scale));
            for (int i = 0; i < users.size(); i++) {
                int x = Math.floorMod(i, columns);
                int y = Math.floorDiv(i, columns);
                userGrid.add(userNode(users.get(i)), x, y);
            }
        }
        
        
        return new Scene(frame);
    }
    
    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if (rowIndex != null) {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }
    
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
    
    
    
    private void addItemsWindow() {
        List<Item> items = new ArrayList<>();
        
        Stage newItemWindow = new Stage();
        newItemWindow.setTitle("Add items");
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(10, 10, 10, 30));
        
        HBox dateInput = new HBox();
        DatePicker date = new DatePicker();
        dateInput.getChildren().addAll(new Label("Date: "), date);  
        
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
            double combinedValue = 0;
            int amountValue = 0;
            double induvidualValue = 0;
            String nameValue = name.getText().trim();
            String typeValue = type.getText().trim().toLowerCase();
            try {
                combinedValue = Double.parseDouble(combined.getText().trim().replace(",", "."));
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
    
    
    private Node dataGrid() {
        GridPane dataGrid = new GridPane();
        Label currentMonth = new Label("Current months expences: ");
        dataGrid.add(currentMonth, 0, 0);
        Label currentMonthvalue = new Label();
        try {
            currentMonthvalue.setText(String.format("%.2f€", manager.currentMonthsExpences()));
        } catch (Exception ex) {
            Alerts.sqlAlert(ex.toString());
        }
        dataGrid.add(currentMonthvalue, 1, 0);
        dataGrid.setAlignment(Pos.CENTER);
        return dataGrid;
    }
    
    private Scene userScreen(User user) {
        try {
            usersItemDao = new DatabaseItemDao(database, user.getTable());
        } catch (SQLException ex) {
            Alerts.sqlAlert("Database creation error " + ex.getMessage());
        }
        manager = new BudgetManager(user, usersItemDao);        
        
        HBox loginInfo = new HBox();
        Button logout = new Button("Logout");
        loginInfo.setSpacing(5);
        logout.setOnAction(e -> {
            mainStage.setScene(loginScene());
        });
        loginInfo.getChildren().addAll(new Label("Loged in as: " + user.getName()), logout);
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(20, 20, 20, 20));
        frame.setPrefSize(600, 400);
        frame.setTop(loginInfo);
        frame.setCenter(dataGrid());
        
        Button newItems = new Button("Add items");
        newItems.setOnAction(e -> {
            addItemsWindow();
            frame.setCenter(dataGrid());
        });
        frame.setBottom(newItems);
        
        return new Scene(frame);
    }
}
