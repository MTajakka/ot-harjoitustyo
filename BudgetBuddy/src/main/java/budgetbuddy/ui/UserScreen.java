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
import budgetbuddy.domain.User;
import java.sql.SQLException;
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
