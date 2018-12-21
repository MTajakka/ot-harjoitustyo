/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.ui;

import budgetbuddy.dao.DatabaseUserDao;
import budgetbuddy.dao.UserDao;
import budgetbuddy.domain.BudgetManager;
import budgetbuddy.domain.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author markus
 */
public class LoginScreen {
    
    private UserDao usersDB;
    private Stage mainStage;
    private final String database;
    BudgetManager manager;
    private UserScreen userScreen;

    public LoginScreen(Stage mainStage, String database) {
        this.mainStage = mainStage;
        this.database = database;
        try {
            usersDB = new DatabaseUserDao(database, "Users");
        } catch (SQLException ex) {
            Logger.getLogger(UiInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUserScreen(UserScreen userScreen) {
        this.userScreen = userScreen;
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
        TextField nameInput = new TextField(user.getName());
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
    
    private void logIn(User user) {
        mainStage.setScene(userScreen.userScreen(user));
    }
    
    
    private Node userNode(User user) {
        String nameSt = user.getName();
        Label name = new Label(nameSt);
        Button login = new Button("Login");
        Button delete = new Button("Delete");
        Button edit = new Button("Edit");
        edit.setPadding(new Insets(2, 5, 2, 5));
        
        login.setOnAction(e -> {
            logIn(user);
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
            mainStage.setScene(loginScene());
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
    
    public Scene loginScene() {        
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
            mainStage.setScene(loginScene());
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
}
