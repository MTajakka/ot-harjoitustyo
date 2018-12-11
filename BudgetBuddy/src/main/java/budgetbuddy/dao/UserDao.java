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
    /**
     * Add an user to its database
     * @param user User to be added
     * @throws SQLException 
     */
    boolean add(User user) throws SQLException;
    
    /**
     * Gets user by id
     * @param id identifier for user
     * @return User from corresponding id
     * @throws SQLException 
     */
    User get(int id) throws SQLException;
    /**
     * Gets every User from database
     * @return List<User> list of every user
     * @throws SQLException 
     */
    List<User> getAll() throws SQLException;
    /**
     * Searchese the databaes for a name
     * @param user name to be searched
     * @return true if name is found, othewise false
     * @throws SQLException 
     */
    boolean containsName(String user) throws SQLException;
    /**
     * Searchese the users databaes for a tablename
     * @param table tablename to be searched
     * @return true if tablename is found
     * @throws SQLException 
     */
    boolean containsTable(String table) throws SQLException;
    
    boolean update(User user) throws SQLException;    
}
