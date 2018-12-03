/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import budgetbuddy.domain.Item;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author markus
 */
public interface ItemDao {    
    boolean add(Item item) throws SQLException;
    boolean add(List<Item> items) throws SQLException;
    
    Item getItem(int id) throws Exception;
    List<Item> getFromTo(Date from, Date to) throws Exception;
    
    Set<String> getTypes() throws SQLException;
    Set<Date> getDates() throws Exception;
    int getMaxId() throws SQLException;
}
