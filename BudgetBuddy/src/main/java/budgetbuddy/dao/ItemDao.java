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
public interface ItemDao extends DaoInterface {    
    /**
     * Adds item to the database
     * @param item  Item to be added
     * @return      If item was added succesfully
     * @throws SQLException 
     */
    boolean add(Item item) throws SQLException;
    /**
     * Adds multiple items
     * @param items List of Items to be added
     * @return      If items were added succesfully
     * @throws SQLException 
     */
    boolean add(List<Item> items) throws SQLException;
    
    /**
     * Item holds an id at whitch it to be updated
     * @param item  Item to be updated
     * @return      If update was succesful
     * @throws SQLException 
     */
    boolean update(Item item) throws SQLException;
    
    /**
     * Gets Item by id form database
     * @param id    Id of item to be returned
     * @return      Item at id
     * @throws Exception 
     */
    Item getItem(int id) throws Exception;
    /**
     * Gets List of Items between from and to
     * @param from  Earlier date
     * @param to    Later date
     * @return      List of items between from and to
     * @throws Exception 
     */
    List<Item> getFromTo(Date from, Date to) throws Exception;
    
    /**
     * Returns Set of all the used types
     * @return Set of types
     * @throws SQLException 
     */
    Set<String> getTypes() throws SQLException;
    /**
     * Returns all dates which have an item
     * @return Set of Dates which have an item
     * @throws Exception 
     */
    Set<Date> getDates() throws Exception;
}
