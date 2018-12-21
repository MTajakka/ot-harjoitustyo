package budgetbuddy.ui;

import budgetbuddy.dao.DatabaseUserDao;
import budgetbuddy.dao.UserDao;
import budgetbuddy.domain.BudgetManager;
import budgetbuddy.domain.User;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UiInit extends Application {
    private UserDao usersDB;
    private Stage mainStage;
    private final String database = "database/budgetBuddyDatabase.db";
    BudgetManager manager;
    private UserScreen userScreen;
    private LoginScreen loginScreen;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("BudgetBuddy");
        File directory = new File("database");
        if (! directory.exists()){
            directory.mkdir();
        }
        userScreen = new UserScreen(mainStage ,database);
        loginScreen = new LoginScreen(mainStage ,database);
        
        userScreen.setLoginScreen(loginScreen);
        loginScreen.setUserScreen(userScreen);
        
        mainStage.setScene(loginScreen.loginScene());
        mainStage.show();
//        addItemsWindow();
    }
    
    
    
    
}
