/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author markus
 */
abstract public class Dao implements DaoInterface {
    protected boolean connected = false;
    protected Connection connection;
    protected String connectionAdress;
    protected String table;
    protected String database;
    
    public Dao(String database, String table) throws SQLException {
        this.database = database;
        this.connectionAdress = "jdbc:sqlite:" + database;
        this.table = table;
        if (!hasTable()) {
            createTable();
        }
    }
    protected void connect() throws SQLException {
        if (!connected) {
            connection = DriverManager.getConnection(connectionAdress);
            connected = true; 
        }
    }
    
    protected void disconnect() throws SQLException {
        if (connected) {
            connection.close();
            connected = false;
        }
    }
    protected boolean hasTable() throws SQLException {
        connect();
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, table, null);
        boolean hasTable = tables.next();
        disconnect();
        return hasTable;
    }
    
    protected void createTable() throws SQLException {
        connect();
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE " + table 
                + " (Id INTEGER PRIMARY KEY)");
        stmt.execute();
        stmt.close();
        disconnect();
    }
    
    protected boolean idEmpty(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT Name FROM " + table + " WHERE Id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return !rs.next();
    }
    
    public int getMaxId() throws SQLException {
        int max;
        String getMaxId = "SELECT MAX(Id) FROM " + table;
        connect();
        PreparedStatement stmt = connection.prepareStatement(getMaxId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            max = rs.getInt(1);
        } else {
            max = -1;
        }
        stmt.close();
        disconnect();
        return max;
    }
    
    /**
    *Delets single item using deleteItem method
    */
    public boolean delete(int id) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        return delete(ids);
    }

    /**
    *Delets multiple items using deleteItem method
    */
    public boolean delete(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        connect();
        String deleteStmt = "DELETE FROM " + table + " WHERE Id IN (";
        for (int i = 0; i < ids.size(); i++) {
            if (i == 0) {
                deleteStmt += ids.get(i);
            } else {
                deleteStmt += ", " + ids.get(i);
            }
        }
        deleteStmt += ")";
        PreparedStatement stmt = connection.prepareStatement(deleteStmt);
        stmt.executeUpdate();
        stmt.close();
        disconnect();
        return true;
    }
    
    public String getTable() {
        return table;
    }
    
    public boolean deleteTable() throws SQLException {
        String dropTable = "DROP TABLE " + table;
        if (!hasTable()) {
            disconnect();
            return false;
        }
        connect();
        System.out.println(connection.isClosed());
        PreparedStatement stmt = connection.prepareStatement(dropTable);
        stmt.executeUpdate();
        stmt.close();
        disconnect();
        return !hasTable();
    }
}
