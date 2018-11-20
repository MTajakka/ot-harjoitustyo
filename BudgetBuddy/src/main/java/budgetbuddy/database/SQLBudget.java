package budgetbuddy.database;

import budgetbuddy.domain.Item;
import java.sql.*;
import java.util.List;

public class SQLBudget {
    private boolean connected=false;
    private Connection connection;
    private String connectionAdress;

    public SQLBudget(String database) {
        this.connectionAdress = "jdbc:sqlite:"+database;
    }
    private void connect() {
        if (!connected) {
            try {
                connection = DriverManager.getConnection(connectionAdress);
            } catch (SQLException ex) {
            }
            connected=true;
        }
    }
    
    private void disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
        }
        connected = false;
    }
    
        
    public void addToDatabase(Item item) {
        boolean alreadyConnected = connected;
        connect();
        
        
        
        if (!alreadyConnected) 
            disconnect();
    }
    
    public void addToDatabase(List<Item> items) {
        connect();
        for (Item item : items) {
            addToDatabase(item);
        }
        disconnect();
    }
    
    
}
