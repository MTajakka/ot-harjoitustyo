/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import budgetbuddy.Helpper;
import budgetbuddy.domain.Item;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markus
 */
public class DatabaseItemDaoTest {
    private String databasePath;
    private ItemDao itemDB;
    
    
    public DatabaseItemDaoTest() {
    }
    
    @Before
    public void setUp() throws SQLException {
        databasePath = "target/test.db";
        itemDB = new DatabaseItemDao(databasePath, "itemTestTable"); 
    }
    
    @After
    public void tearDown() {
        boolean result = new File(databasePath).delete();
    }
    
    
    
    private void clearTable() throws SQLException {
        List<Integer> deletable = new ArrayList<>();
        for (int i = 1; i <= itemDB.getMaxId(); i++) {
            deletable.add(i);
        }
        itemDB.delete(deletable);
    }
    
    /*
    *Testing using Create Read Update Delete method in which items are created, 
    *read, updated, and then deleted.
    */
    @Test
    public void CRUDTest() throws Exception {
        ItemDao itemDB = new DatabaseItemDao(databasePath, "CRUDTest"); 
        Date date1 = Helpper.yearMonthDayToDate("2018-08-10");
        Date date2 = Helpper.yearMonthDayToDate("2013-03-25");
        Item item1 = new Item("Item 1", "item", date1, 395, 10, 3);
        Item item2 = new Item("Item 2", "Item", date2, 595, 5, 4);
        
        List<Item> items = new ArrayList<>();
        items.add(item2);
        
        itemDB.add(item1);
        itemDB.add(items);
        
        items = itemDB.getFromTo(date2, Helpper.addDays(date1, 10));
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 2", items.get(1).getName());
        
        Item tempItem = itemDB.getItem(2);
        tempItem.setPrice(600);
        
        itemDB.update(tempItem);
        
        items = itemDB.getFromTo(date2, Helpper.addDays(date1, 10));
        assertEquals("Item 1", items.get(0).getName());
        assertEquals(395, items.get(0).getPrice());
        assertEquals("Item 2", items.get(1).getName());
        assertEquals(600, items.get(1).getPrice());
        
        List<Integer> deletable = new ArrayList<>();
        deletable.add(2);
        
        itemDB.delete(deletable);
        itemDB.delete(1);
        
        assertEquals(0, itemDB.getMaxId());
        
        assertTrue(itemDB.deleteTable());
    }

    @Test
    public void typesCorrect() throws Exception {
        Date date1 = Helpper.yearMonthDayToDate("2018-08-10");
        Date date2 = Helpper.yearMonthDayToDate("2013-03-25");
        Item item3 = new Item("Item 3", "item", date1, 395, 10, 3);
        Item item5 = new Item("Item 3", "Item", date2, 595, 5, 4);
        
        itemDB.add(item3);
        itemDB.add(item5);
        
        Set<String> types = itemDB.getTypes();
        assertEquals("[item]", types.toString());
        clearTable();
    }
    
    @Test
    public void datesCorrect() throws Exception {
        Date date1 = Helpper.yearMonthDayToDate("2018-08-10");
        Date date2 = Helpper.yearMonthDayToDate("2013-03-25");
        Item item3 = new Item("Item 3", "item", date1, 395, 10, 3);
        Item item5 = new Item("Item 3", "Item", date2, 595, 5, 4);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item3);
        items.add(item5);
        
        itemDB.add(items);
        
        Set<Date> datesDB = itemDB.getDates();
        Set<Date> dates = new HashSet<>();
        dates.add(date1);
        dates.add(date2);
        
        assertEquals(dates, datesDB);
        clearTable();
    }
    
    @Test
    public void getFromToTest() throws Exception {
        Date date1 = Helpper.yearMonthDayToDate("2018-07-01");
        Date date2 = Helpper.yearMonthDayToDate("2018-08-25");
        
        Item item1 = new Item("Item 1", "item", date1, 395, 10);
        Item item2 = new Item("Item 2", "Item", date2, 595, 5);
        
        itemDB.add(item1);
        itemDB.add(item2);
        
        List<Item> items = itemDB.getFromTo(Helpper.yearMonthDayToDate("2018-08-01"), date2);
        
        assertFalse(items.contains(item1));
        assertTrue(items.contains(item2));
    }
}
