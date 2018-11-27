package budgetbuddy.domain;

import java.util.Date;
    
public class ItemUtil {
    
    public static Item itemCombinedPrice(String name, String type, Date date, int price, int amount, int id) throws Exception {
        if (amount == 0) {
            throw new Exception("divison by 0");
        }
        int induvidualPrice = price / amount;
        Item item = new Item(name, type, date, induvidualPrice, amount, id);
        return item;
    }
    
    public static Item itemCombinedPrice(String name, String type, Date date, int price, int amount) throws Exception {
        return ItemUtil.itemCombinedPrice(name, type, date, price, amount, -1);
    }
    
    
}
