/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import budgetbuddy.domain.Item;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
        itemDB = new DatabaseItemDao(databasePath); 
    }
    
    @After
    public void tearDown() {
        boolean result = new File(databasePath).delete();
    }

    @Test
    public void typesCorrect() throws Exception {
        Date date1 = new Date(2018-1900, 8-1, 10);
        Date date2 = new Date(2013-1900, 3-1, 25);
        Item item3 = new Item("Item 3", "item", date1, 395, 10, 3);
        Item item5 = new Item("Item 3", "Item", date2, 595, 5, 4);
        
        itemDB.add(item3);
        itemDB.add(item5);
        
        Set<String> types = itemDB.getTypes();
        assertEquals("[item]", types.toString());
    }
    
    @Test
    public void datesCorrect() throws Exception {
        Date date1 = new Date(2018-1900, 8-1, 10);
        Date date2 = new Date(2013-1900, 3-1, 25);
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
    }
}
