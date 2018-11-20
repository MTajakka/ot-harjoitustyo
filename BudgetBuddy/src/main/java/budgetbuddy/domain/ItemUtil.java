package budgetbuddy.domain;

import java.util.Date;
    
public class ItemUtil {
    
    public static Item itemCombinedPrice(String name, String type, Date date, int price, int amount, int id) {
        int InduvidualPrice = price/amount;
        Item item = new Item(name, type, date, InduvidualPrice, amount, id);
        return item;
    }
    
    public static Item itemCombinedPrice(String name, String type, Date date, int price, int amount) {
        return ItemUtil.itemCombinedPrice(name, type, date, price, amount, -1);
    }
    
    
}
