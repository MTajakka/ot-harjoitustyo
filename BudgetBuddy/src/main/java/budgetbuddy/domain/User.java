/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.domain;

import budgetbuddy.dao.DatabaseItemDao;
import budgetbuddy.dao.ItemDao;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author markus
 */
public class User {
    private String name;
    private int id;
    private String database;
    private String table;
    private ItemDao items;
    private boolean databaseActive = false;

    public User(String name, String database, String table, int id) throws SQLException {
        this.name = name;
        this.database = database;
        this.table = table;
        this.id = id;
        try {
            activateDatabase();
        } catch (SQLException exception) {
            databaseActive = false;
        }
    }
    public User(String name, String database, String table) throws SQLException {
        this(name, database, table, -1);
    }
    
    public void activateDatabase() throws SQLException {
        if (!databaseActive) {
            items = new DatabaseItemDao(database, table);
            databaseActive = true;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
    
    public String getTable() {
        return items.getTable();
    }
    
    public boolean deleteTable() throws SQLException {
        return items.deleteTable();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
}
