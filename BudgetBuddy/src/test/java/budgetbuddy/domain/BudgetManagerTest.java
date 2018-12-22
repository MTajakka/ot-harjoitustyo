/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.domain;

import budgetbuddy.Helpper;
import budgetbuddy.dao.DatabaseItemDao;
import budgetbuddy.dao.ItemDao;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markus
 */
public class BudgetManagerTest {
    private String databasePath;
    private ItemDao itemDB;
    private User user;
    private BudgetManager manager;
    
    public BudgetManagerTest() {
    }
    
    @Before
    public void setUp() throws SQLException {
        databasePath = "target/test.db";
        itemDB = new DatabaseItemDao(databasePath, "itemTestTable"); 
        user = new User("TestUser", databasePath, "Table1");
        manager = new BudgetManager(user, itemDB);
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

    @Test
    public void currentMonthsExspences() throws Exception {
        Date date1 = new Date();
        Date date2 = Helpper.firstDayOfMonth(date1);
        
        Item item1 = new Item("Item 1", "item", date1, 395, 10);
        Item item2 = new Item("Item 2", "Item", Helpper.addDays(date2, -20), 595, 5);
        
        manager.add(item1);
        manager.add(item2);
        
        double expences = manager.currentMonthsExpences();
        assertEquals(395.0*10/100.0, expences, 0.001);
        clearTable();
    }
    
    @Test
    public void expencesByTypeFromToTest() throws Exception {
        Date date1 = new Date();
        
        Item item1 = new Item("Item 1", "item", date1, 395, 10);
        Item item2 = new Item("Item 2", "Item", Helpper.addDays(date1, -20), 595, 5);
        
        manager.add(item1);
        manager.add(item2);
        
        List<TypePrice> types = manager.expencesByTypeFromTo(Helpper.addDays(date1, -30), date1);
        assertEquals(595 * 5 + 395 * 10, types.get(0).getPrice());
        clearTable();
    }
}
