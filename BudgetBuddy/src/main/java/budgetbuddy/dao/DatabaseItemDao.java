package budgetbuddy.dao;

import budgetbuddy.Helpper;
import budgetbuddy.domain.Item;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;

public class DatabaseItemDao implements ItemDao {
    private boolean connected = false;
    private Connection connection;
    private String connectionAdress;
    private final String addItemSt = "INSERT INTO Items (Name, Type, Date, Price, Amount) " 
                + "VALUES (?, ?, ?, ?, ?)";
    private final String updateItemSt = "UPDATE Items SET Name = ?, Type = ?, Date = ?, Price = ?, Amount = ?   " 
                + "WHERE rowid = ?";

    public DatabaseItemDao(String database) throws SQLException {
        this.connectionAdress = "jdbc:sqlite:" + database;
        if (!hasTable()) {
            createTabel();
        }
    }
    
    private void connect() throws SQLException {
        if (!connected) {
            connection = DriverManager.getConnection(connectionAdress);
            connected = true; 
        }
    }
    
    private void disconnect() throws SQLException {
        if (connected) {
            connection.close();
            connected = false;
        }
    }
    
    private boolean hasTable() throws SQLException {
        connect();
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "Items", null);
        boolean hasTable = tables.next();
        disconnect();
        return hasTable;
    }
    
    
    private void createTabel() throws SQLException {
        connect();
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE Items ("
                + "Name TEXT,"
                + "Type TEXT,"
                + "Date varchar(10),"
                + "Price INT,"
                + "Amount INT   )");
        stmt.execute();
        disconnect();
    }
    
    private boolean idEmpty(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT Name FROM Items WHERE ROWID = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return !rs.next();
    }
    
    
    
    private boolean addItem(Item item) throws SQLException {
        if (connection == null) {
            return false;
        }
        PreparedStatement stmt;
        if (item.getId() == -1 || idEmpty(item.getId())) {
            stmt = connection.prepareStatement(addItemSt);
        } else {
            stmt = connection.prepareStatement(updateItemSt);
            stmt.setInt(6, item.getId());
        }
        stmt.setString(1, item.getName());
        stmt.setString(2, item.getType());
        stmt.setString(3, Helpper.dateToYearMonthDay(item.getDate()));
        stmt.setInt(4, item.getPrice());
        stmt.setInt(5, item.getAmount());
        
        stmt.executeUpdate();
        stmt.close();
        return true;
    }
    
    @Override
    public boolean add(Item item) throws SQLException {
        connect();
        addItem(item);
        disconnect();
        return true;
    }

    @Override
    public boolean add(List<Item> items) throws SQLException {
        connect();
        for (Item item : items) {
            addItem(item);
        }
        disconnect();
        return true;
    }
    
    @Override
    public Set<String> getTypes() throws SQLException {
        Set<String> items = new HashSet<>();
        connect();
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Type FROM Items");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            items.add(rs.getString(1));
        }
        disconnect();
        return items;
    }
    
    @Override
    public Set<Date> getDates() throws Exception {
        Set<Date> dates = new HashSet<>();
        connect();
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Date FROM Items");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String date = rs.getString(1);
            dates.add(Helpper.yearMonthDayToDate(date));
        }
        disconnect();
        return dates;
    }

    @Override
    public Item getItem(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Item> getFromTo(Date from, Date to) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
