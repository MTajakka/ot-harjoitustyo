/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.dao;

import budgetbuddy.Helpper;
import budgetbuddy.domain.User;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markus
 */
public class DatabaseUserDaoTest {
    private String databasePath;
    private ItemDao userDB;
    
    public DatabaseUserDaoTest() {
    }
    
    @Before
    public void setUp() throws SQLException {
        databasePath = "target/test.db";
        userDB = new DatabaseItemDao(databasePath, "userTestTable"); 
    }
    
    @After
    public void tearDown() {
        boolean result = new File(databasePath).delete();
    }

    @Test
    public void CRUDTest() throws SQLException {
        UserDao userDB = new DatabaseUserDao(databasePath, "CRUDTest"); 
        User user = new User("User1", databasePath, "Table1");
        User user2 = new User("User2", databasePath, "Table2");
        
        userDB.add(user);
        userDB.add(user2);
        
        assertEquals("User1", userDB.get(1).getName());
        assertEquals("User2", userDB.getAll().get(1).getName());
        
        User tempUser = userDB.get(2);
        tempUser.setName("User3");
        userDB.update(tempUser);
        
        assertEquals("User3", userDB.get(2).getName());
        
        List<Integer> deletable = new ArrayList<>();
        deletable.add(2);
        
        userDB.delete(deletable);
        userDB.delete(1);
        
        assertEquals(0, userDB.getMaxId());
        
        assertTrue(userDB.deleteTable());
    }
}
