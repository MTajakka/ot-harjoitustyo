package budgetbuddy.ui;

import budgetbuddy.dao.UserDao;
import budgetbuddy.domain.BudgetManager;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;

public class UiInit extends Application {
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
        if (!directory.exists()) {
            directory.mkdir();
        }
        userScreen = new UserScreen(mainStage, database);
        loginScreen = new LoginScreen(mainStage, database);
        
        userScreen.setLoginScreen(loginScreen);
        loginScreen.setUserScreen(userScreen);
        
        mainStage.setScene(loginScreen.loginScene());
        mainStage.show();
//        addItemsWindow();
    }
    
    
    
    
}
