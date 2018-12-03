package budgetbuddy.dao;

import budgetbuddy.Helpper;
import budgetbuddy.domain.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.stream.Collectors;

public class DatabaseItemDao implements ItemDao {
    private boolean connected = false;
    private Connection connection;
    private String connectionAdress;
    private String table;
    
    public DatabaseItemDao(String database, String table) throws SQLException {
        this.connectionAdress = "jdbc:sqlite:" + database;
        this.table = table;
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
        ResultSet tables = dbm.getTables(null, null, table, null);
        boolean hasTable = tables.next();
        disconnect();
        return hasTable;
    }
    
    
    private void createTabel() throws SQLException {
        connect();
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE " + table 
                + " (Name TEXT,"
                + "Type TEXT,"
                + "Date varchar(10),"
                + "Price INT,"
                + "Amount INT )");
        stmt.execute();
        stmt.close();
        disconnect();
    }
    
    private boolean idEmpty(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT Name FROM " + table + " WHERE ROWID = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return !rs.next();
    }
    
    
    
    private boolean addItem(Item item) throws SQLException {
        String addItemSt = "INSERT INTO " + table + " (Name, Type, Date, Price, Amount) " 
                + "VALUES (?, ?, ?, ?, ?)";
        String updateItemSt = "UPDATE " + table + " SET Name = ?, Type = ?, Date = ?, Price = ?, Amount = ?   " 
                + "WHERE rowid = ?";
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
        stmt.close();
        disconnect();
        return items;
    }
    
    @Override
    public Set<Date> getDates() throws Exception {
        Set<Date> dates = new HashSet<>();
        connect();
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Date FROM " + table);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String date = rs.getString(1);
            dates.add(Helpper.yearMonthDayToDate(date));
        }
        stmt.close();
        disconnect();
        return dates;
    }
    
    @Override
    public int getMaxId() throws SQLException {
        int max;
        String getMaxId = "SELECT MAX(ROWID) FROM Items";
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
    
    private Item itemFromResult(ResultSet rs) throws Exception {
        return new Item(rs.getString("Name"), rs.getString("Type"), Helpper.yearMonthDayToDate(rs.getString("Date")),
            rs.getInt("Price"), rs.getInt("Amount"), rs.getRow());
    }
    
    @Override
    public Item getItem(int id) throws Exception {
        if (id < 1 || id > getMaxId()) {
            return null;
        }
        Item item;
        String getById = "SELECT * FROM " + table + " WHERE ROWID = ?";
        connect();
        PreparedStatement stmt = connection.prepareStatement(getById);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            item = itemFromResult(rs);
        } else {
            item = null;
        }
        stmt.close();
        disconnect();
        return item;
    }
    
    private boolean between(Date date, Date from, Date to) {
        return date.equals(from) || ( date.after(from) && date.before(to) ) || date.equals(to);
    }

    @Override
    public List<Item> getFromTo(Date from, Date to) throws Exception {
        if (from.after(to)) {
            return null;
        }
        List<Item> items = new ArrayList<>();
        Set<Date> dates = getDates().stream().filter(d -> between(d, from, to)).collect(Collectors.toSet());
        String getByDate = "SELECT * FROM " + table + " WHERE Date = ?";
        connect();
        PreparedStatement stmt = connection.prepareStatement(getByDate);
        for (Date date : dates) {
            stmt.setString(1, Helpper.dateToYearMonthDay(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(itemFromResult(rs));
            }
        }
        stmt.close();
        disconnect();
        return items;
    }

}
