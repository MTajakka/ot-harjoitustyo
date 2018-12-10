
package budgetbuddy.ui;

import budgetbuddy.dao.DatabaseUserDao;
import budgetbuddy.dao.ItemDao;
import budgetbuddy.dao.UserDao;
import budgetbuddy.domain.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BudgetBudyUi extends Application {
    private UserDao usersDB;
    private Stage mainStage;
    private final String database = "budgetBuddyDatabase.db";

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("BudgetBuddy");
        try {
            usersDB = new DatabaseUserDao(database, "Users");
        } catch (SQLException ex) {
            Logger.getLogger(BudgetBudyUi.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainStage.setScene(loginScene());
        mainStage.show();
    }
    
    private void sqlAlert(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("SQL error");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
    
    private boolean confirmationAlert(String text) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirm");
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    
    
    private void addUser() {
        Stage newUserWindow = new Stage();
        newUserWindow.setTitle("New User");
        
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(10, 10, 10, 30));
        
        HBox information = new HBox();
        information.setSpacing(5);
        information.setPadding(new Insets(10,0,10,0));
        Label title = new Label("Name:");
        TextField nameInput = new TextField();
        information.getChildren().addAll(title, nameInput);
        frame.setCenter(information);
        
        Button createNew = new Button("Create user");
        frame.setBottom(createNew);
        
        frame.setTop(new Label(" "));
        
        createNew.setOnAction(e->{
            String name = nameInput.getText().trim();
            if (name.isEmpty()) {
                frame.setTop(new Label("Please give a name"));  
            } else if (name.contains(" ")) {
                frame.setTop(new Label("Name should not have spaces"));
            } else {
                try {
                    String table = name + "sTable";
                    if (usersDB.containsName(name)) {
                        frame.setTop(new Label("Name already used"));
                    } else if(usersDB.containsTable(table)) {
                        int i = 1;
                        while (usersDB.containsTable(table + i)) {
                            i += 1;
                        }
                        table += i;
                    } else if (name.length()>14) {
                        frame.setTop(new Label("Name too long. Try using less\nthan 14 characters"));
                        
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
        newUserWindow.setX(mainStage.getX() + mainStage.getWidth()/2- 130);
        newUserWindow.setY(mainStage.getY() + mainStage.getHeight()/2 - 50);
        newUserWindow.showAndWait();
    }
    
    private Node userNode(User user) {
        String nameSt = user.getName();
        Label name = new Label(nameSt);
        Button login = new Button("Login");
        Button delete = new Button("Delete");
        
        delete.setOnAction(e->{
            if (confirmationAlert("You are about to delete " + nameSt
                    + "'s account and everything related to it")) {
                try {
                    usersDB.delete(user.getId());
                    mainStage.setScene(loginScene());                
                } catch (SQLException ex) {
                    sqlAlert(ex.getMessage());
                }
            }
        });
        
        HBox controlls = new HBox();
        controlls.setSpacing(5);
        controlls.getChildren().addAll(delete, login);
        
        VBox frame = new VBox();
        frame.setSpacing(5);
        frame.setPadding(new Insets(10));
        frame.setMinWidth(140);
        frame.getChildren().addAll(name, controlls);
        
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
        
        addUser.setOnAction(e->{
            addUser();
        });
        
        // main layout
        BorderPane frame = new BorderPane();
        frame.setPadding(new Insets(20, 20, 20, 20));
        frame.setPrefSize(500, 400);
        frame.setCenter(userGrid);
        frame.setBottom(hbox);
        
        // show avilable users
        List<User> users;
        try {
            users = usersDB.getAll();
        } catch (SQLException ex) {
            users = new ArrayList<>();
            frame.setCenter(new Label("SQL error occured. Please restart porgram"));
        }
        if (users.isEmpty()) {
            frame.setCenter(new Label("No users found."));
        } else {
            double scale = 5.0/3.0;
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
