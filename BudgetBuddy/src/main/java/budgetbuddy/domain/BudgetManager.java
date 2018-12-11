/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.domain;

import budgetbuddy.Helpper;
import budgetbuddy.dao.UserDao;
import budgetbuddy.dao.ItemDao;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author markus
 */
public class BudgetManager {
    private User user;
    private ItemDao itemDB;

    public BudgetManager(User user, ItemDao items) {
        this.user = user;
        this.itemDB = items;
    }
    
    public double currentMonthsExpences() throws Exception {
        Date now = Helpper.addDays(new Date(), 1);
        Date firstDate = Helpper.firstDayOfMonth(now);
        List<Item> itemList = itemDB.getFromTo(firstDate, now);
        double expences = 0;
        for (Item item : itemList) {
            expences += item.getPrice() * item.getAmount();
        }
        expences /= 100.0;
        return expences;
    }
    
    public void add(Item item) throws SQLException {
        itemDB.add(item);
    }
    
    public void add(List<Item> items) throws SQLException {
        itemDB.add(items);
    }
}
