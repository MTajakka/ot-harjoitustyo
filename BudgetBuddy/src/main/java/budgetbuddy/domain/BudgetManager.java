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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    /**
     * Returns sum of all items from first day of month to now
     * @return Sum of all items from first day of month to now
     * @throws Exception 
     */
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
    
    /**
     * Returns sum of prices between diffrent types between dates from and to
     * @param from  Earlier Date
     * @param to    Later Date
     * @return List of TypePrices which are types whith corresponding sum of expences
     * @throws Exception 
     */
    public List<TypePrice> expencesByTypeFromTo(Date from, Date to) throws Exception {
        List<Item> items = getFromTo(from, to);
        HashMap<String, Integer> prices = new HashMap<>();
        for (Item item : items) {
            int old = prices.getOrDefault(item.getType(), 0);
            prices.put(item.getType(), old + item.getAmount() * item.getPrice());
        }
        List<TypePrice> combinedPrices = new ArrayList<>();
        for (String type : prices.keySet()) {
            combinedPrices.add(new TypePrice(type, prices.get(type)));
        }
        return combinedPrices.stream().sorted().collect(Collectors.toList());
    }
    
    public void add(Item item) throws SQLException {
        itemDB.add(item);
    }
    
    public void add(List<Item> items) throws SQLException {
        itemDB.add(items);
    }
    
    public List<Item> getFromTo(Date from, Date to) throws Exception {
        return itemDB.getFromTo(from, to);
    }
    
    public boolean update(Item item) throws SQLException {
        return itemDB.update(item);
    }
    public Set<Date> getDates() throws Exception {
        return itemDB.getDates();
    }
    
    public Set<String> getTypes() throws SQLException {
        return itemDB.getTypes();
    }
    
    public void delete(Set<Integer> deletable) throws SQLException {
        itemDB.delete(deletable.stream().sorted().collect(Collectors.toList()));
    }
}
