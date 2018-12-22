/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author markus
 */
abstract public interface DaoInterface {
    /**
     * Delets an item at corresponding id
     * @param id    id of item to be deleted
     * @return      if deletion was succesful
     * @throws SQLException 
     */
    boolean delete(int id) throws SQLException;
    
    /**
     * Delets multiple items
     * @param ids   List of ids to be deleted
     * @return      If deletion was succesful
     * @throws SQLException 
     */
    boolean delete(List<Integer> ids) throws SQLException;
    
    /**
     * 
     * @return      Largest id of the database
     * @throws SQLException 
     */
    int getMaxId() throws SQLException;
    
    /**
     * Gets the name of its table
     * @return      name of a table
     */
    String getTable();
    /**
     * Delets its table
     * @return      if deletion was succesful
     * @throws SQLException 
     */
    boolean deleteTable() throws SQLException;    
}
