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

public class DatabaseItemDao extends Dao implements ItemDao {

    public DatabaseItemDao(String database, String table) throws SQLException {
        super(database, table);
    }
    
    @Override
    protected void createTable() throws SQLException {
        connect();
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE " + table 
                + " (Id INTEGER PRIMARY KEY,"
                + "Name TEXT,"
                + "Type TEXT,"
                + "Date varchar(10),"
                + "Price INT,"
                + "Amount INT )");
        stmt.execute();
        stmt.close();
        disconnect();
    }
    
    @Override
    public boolean add(Item item) throws SQLException {
        List<Item> items = new ArrayList<>();
        items.add(item);
        add(items);
        return true;
    }

    @Override
    public boolean add(List<Item> items) throws SQLException {
        if (items.isEmpty() || items == null) {
            return false;
        }
        String addItemSt = "INSERT INTO " + table + " (Name, Type, Date, Price, Amount) "
                + "VALUES ";
        for (Item item : items) {
            addItemSt += "('" + item.getName()
                    + "', '" + item.getType()
                    + "', '" + Helpper.dateToYearMonthDay(item.getDate())
                    + "', " + item.getPrice()
                    + ", " + item.getAmount() + "), ";
        }
        addItemSt = addItemSt.substring(0, addItemSt.length() - 2);
        
        connect();
        PreparedStatement addStmt = connection.prepareStatement(addItemSt);
        addStmt.executeUpdate();
        addStmt.close();
        disconnect();
        return true;
    }
    
    @Override
    public boolean update(Item item) throws SQLException {
        if (item == null || item.getId() < 1) {
            return false;
        }
        String updateSt = "UPDATE " + table + " SET"
                + " Name = '" + item.getName()
                + "', Type = '" + item.getType()
                + "', Date = '" + Helpper.dateToYearMonthDay(item.getDate())
                + "', Price = " + item.getPrice()
                + ", Amount = " + item.getAmount()
                + " WHERE Id = " + item.getId();
        connect();
        PreparedStatement stmt = connection.prepareStatement(updateSt);
        stmt.executeUpdate();
        stmt.close();
        disconnect();
        return true;
    }
    
    /*
    *Gets every unique entry on Types column
    */
    @Override
    public Set<String> getTypes() throws SQLException {
        Set<String> items = new HashSet<>();
        connect();
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Type FROM " + table);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            items.add(rs.getString(1));
        }
        stmt.close();
        disconnect();
        return items;
    }
    
    /*
    *Gets every unique date on Dates column
    */
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
    
    /*
    *Method to translate item from resultSet
    */
    private Item itemFromResult(ResultSet rs) throws Exception {
        return new Item(rs.getString("Name"), rs.getString("Type"), Helpper.yearMonthDayToDate(rs.getString("Date")),
            rs.getInt("Price"), rs.getInt("Amount"), rs.getInt("Id"));
    }
    
    @Override
    public Item getItem(int id) throws Exception {
        if (id < 1 || id > getMaxId()) {
            return null;
        }
        Item item;
        String getById = "SELECT * FROM " + table + " WHERE Id = " + id;
        connect();
        PreparedStatement stmt = connection.prepareStatement(getById);
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
    
    /*
    *test to see from and to contain date between them 
    */
    private boolean between(Date date, Date from, Date to) {
        return date.equals(from) || (date.after(from) && date.before(to)) || date.equals(to);
    }

    @Override
    public List<Item> getFromTo(Date from, Date to) throws Exception {
        if (from.after(to)) {
            return null;
        }
        List<Item> items = new ArrayList<>();
        Set<Date> dates = getDates().stream().filter(d -> between(d, from, to)).collect(Collectors.toSet());
        String getByDate = "SELECT * FROM " + table + " WHERE Date IN (";
        for (Date date : dates) {
            getByDate += "'" + Helpper.dateToYearMonthDay(date) + "', ";
        }
        getByDate = getByDate.substring(0, getByDate.length() - 2) + ")";
        connect();
        PreparedStatement stmt = connection.prepareStatement(getByDate);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            items.add(itemFromResult(rs));
        }
        stmt.close();
        disconnect();
        return items;
    }
}
