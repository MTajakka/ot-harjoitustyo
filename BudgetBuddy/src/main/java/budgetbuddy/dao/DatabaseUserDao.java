/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import budgetbuddy.Helpper;
import budgetbuddy.domain.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author markus
 */
public class DatabaseUserDao extends Dao implements UserDao {

    public DatabaseUserDao(String database, String table) throws SQLException {
        super(database, table);
    }

    @Override
    protected void createTable() throws SQLException {
        connect();
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE " + table + " ("
                + "Id INTEGER PRIMARY KEY,"
                + "Name TEXT, "
                + "TableNames TEXT)");
        stmt.execute();
        stmt.close();
        disconnect();
    }
    
    

    @Override
    public boolean add(User user) throws SQLException {
        if (user == null) {
            return false;
        }
        String addUserSt = "INSERT INTO " + table + " (Name, TableNames) "
                + "VALUES ('"
                + user.getName()
                + "', '" + user.getTable() + "')";
        connect();
        PreparedStatement stmt = connection.prepareStatement(addUserSt);
        stmt.executeUpdate();
        stmt.close();
        disconnect();
        return true;
    }
    
    private User userFromResult(ResultSet rs) throws SQLException {
        return new User(rs.getString("Name"), database,  rs.getString("TableNames"), rs.getInt("Id"));
    }

    @Override
    public User get(int id) throws SQLException {
        if (id < 1 || id > getMaxId()) {
            return null;
        }
        User user;
        String addUserSt = "SELECT * FROM " + table + " WHERE Id = " + id;
        connect();
        PreparedStatement stmt = connection.prepareStatement(addUserSt);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            user = userFromResult(rs);
        } else {
            user = null;
        }
        stmt.close();
        disconnect();
        user.activateDatabase();
        return user;
    }
    
    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String addUserSt = "SELECT * FROM " + table;
        connect();
        PreparedStatement stmt = connection.prepareStatement(addUserSt);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            users.add(userFromResult(rs));
        }
        stmt.close();
        disconnect();
        for (User user : users) {
            user.activateDatabase();
        }
        return users;
    }

    @Override
    public boolean update(User user) throws SQLException {
        if (user == null || user.getId() < 1) {
            return false;
        }
        String updateSt = "UPDATE " + table + " SET"
                + " Name = '" + user.getName()
                + "', TableNames = '" + user.getTable()
                + "' WHERE Id = " + user.getId();
        connect();
        PreparedStatement stmt = connection.prepareStatement(updateSt);
        stmt.executeUpdate();
        stmt.close();
        disconnect();
        return true;
    }

}
