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
    boolean delete(int id) throws SQLException;
    boolean delete(List<Integer> ids) throws SQLException;
    
    int getMaxId() throws SQLException;
    
    String getTable();
    boolean deleteTable() throws SQLException;    
}
