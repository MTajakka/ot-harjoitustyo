/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import budgetbuddy.domain.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author markus
 */
public interface UserDao extends DaoInterface {
    boolean add(User user) throws SQLException;
    
    User get(int id) throws SQLException;
    List<User> getAll() throws SQLException;
    boolean containsName(String user) throws SQLException;
    boolean containsTable(String table) throws SQLException;
    
    boolean update(User user) throws SQLException;    
}
