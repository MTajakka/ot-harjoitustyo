/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.ui;

import budgetbuddy.Helpper;
import budgetbuddy.dao.DatabaseItemDao;
import budgetbuddy.dao.ItemDao;
import budgetbuddy.domain.BudgetManager;
import budgetbuddy.domain.Item;
import budgetbuddy.domain.TypePrice;
import budgetbuddy.domain.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author markus
 */
public class UserScreen {
    
    private Stage mainStage;
    private final String database;
    BudgetManager manager;
    private LoginScreen loginScreen;

    public UserScreen(Stage mainStage, String database) {
        this.mainStage = mainStage;
        this.database = database;
    }

    public void setLoginScreen(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
    }
    
    private static GridPane createLargestExpencesGrid(Date from, Date to, BudgetManager manager) {
        GridPane expences = new GridPane();
        expences.setHgap(5);
        List<TypePrice> types;
        try {
            types = manager.expencesByTypeFromTo(from, to);
        } catch (Exception ex) {
            Alerts.sqlAlert(ex.getMessage());
            types = null;
        }
        if (types == null) {
            expences.add(new Label("Dates wrong way around"), 0, 0);
        }
        if (types.isEmpty()) {
            expences.add(new Label("No purchases found"), 0, 0);
            return expences;
        }
        Collections.reverse(types);
        for (int i = 0; i < 3; i++) {
            try {
                TypePrice type = types.get(i);
                expences.add(new Label(type.getType()), 0, i);
                expences.add(new Label(String.format("%.2f€", type.getPrice() / 100.0)), 1, i);
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return expences;
    }
        
    
    private Node dataGrid() {
        GridPane dataGrid = new GridPane();
        dataGrid.setVgap(10);
        dataGrid.setHgap(10);
        Label currentMonth = new Label("Current months expences: ");
        dataGrid.add(currentMonth, 0, 0);
        Label currentMonthvalue = new Label();
        try {
            currentMonthvalue.setText(String.format("%.2f€", manager.currentMonthsExpences()));
        } catch (Exception ex) {
            Alerts.sqlAlert(ex.toString());
        }
        dataGrid.add(currentMonthvalue, 1, 0);
        
        ComboBox<String> timeFrame = new ComboBox<>();
        timeFrame.getItems().addAll("week", "month", "three months", "six months", "year");
        timeFrame.getSelectionModel().select(1);
        timeFrame.setPadding(new Insets(-4, -5, -4, -5));
        timeFrame.setPrefWidth(125);
        Pane expencesGrid = new Pane();
        timeFrame.setOnAction(eh -> {
            expencesGrid.getChildren().clear();
            Date now = new Date();
            Date then = new Date();
            switch (timeFrame.getSelectionModel().getSelectedItem()) {
                case "week":
                    then = Helpper.addDays(now, -7);
                    break;
                case "month":
                    then = Helpper.addMonths(now, -1);
                    break;
                case "three months":
                    then = Helpper.addMonths(now, -3);
                    break;
                case "six months":
                    then = Helpper.addMonths(now, -6);
                    break;
                case "year":
                    then = Helpper.addMonths(now, -12);
                    break;
                default:
                    Alerts.sqlAlert("Selector borke");
            }
            GridPane exGrid = createLargestExpencesGrid(then, now, manager);
            expencesGrid.getChildren().add(exGrid);
        });
        GridPane exGrid = createLargestExpencesGrid(Helpper.addMonths(new Date(), -1), new Date(), manager);
        expencesGrid.getChildren().add(exGrid);
        HBox mostExpences = new HBox();
        mostExpences.getChildren().addAll(new Label("Largest expences in last "), timeFrame);
        dataGrid.add(mostExpences, 0, 1);
        dataGrid.add(expencesGrid, 1, 1);
        
        
        
        dataGrid.setAlignment(Pos.CENTER);
        return dataGrid;
    }
    
    private void logOut() {
        mainStage.setScene(loginScreen.loginScene());
    }
    
    
    public Scene userScreen(User user) {
        ItemDao usersItemDao = null;
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
            logOut();
        });
        loginInfo.getChildren().addAll(new Label("Loged in as: " + user.getName()), logout);
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(20, 20, 20, 20));
        frame.setPrefSize(600, 400);
        frame.setTop(loginInfo);
        frame.setCenter(dataGrid());
        
        HBox newWindows = new HBox();
        newWindows.setSpacing(5);
        
        Button newItems = new Button("Add items");
        newItems.setOnAction(e -> {
            AddItemsWindow.open(manager, mainStage);
            frame.setCenter(dataGrid());
        });
        newWindows.getChildren().add(newItems);
        
        Button viewItem = new Button("View items");
        viewItem.setOnAction(e -> {
            ViewItemsWindow.open(manager, mainStage);
            frame.setCenter(dataGrid());
        });
        newWindows.getChildren().add(viewItem);
        frame.setBottom(newWindows);
        
        return new Scene(frame);
    }
}
