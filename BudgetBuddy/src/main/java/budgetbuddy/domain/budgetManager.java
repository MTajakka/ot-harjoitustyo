/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.domain;

import budgetbuddy.dao.UserDao;
import budgetbuddy.dao.ItemDao;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author markus
 */
public class budgetManager {
    private UserDao users;
    private ItemDao items;

    public budgetManager(UserDao users, ItemDao items) {
        this.users = users;
        this.items = items;
    }
    
    public List<User> getUsers() throws SQLException {
        return users.getAll();
    }
}
