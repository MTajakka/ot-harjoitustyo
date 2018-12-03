
package budgetbuddy;

import budgetbuddy.ui.BudgetBudyUi;
import java.util.Date;
import budgetbuddy.Helpper;
import budgetbuddy.dao.ItemDao;
import budgetbuddy.dao.DatabaseItemDao;
import budgetbuddy.domain.Item;
import budgetbuddy.domain.ItemUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Init {
    
    public static void main(String[] args) throws Exception {
//        ArrayList<Item> itemlist = new ArrayList<>();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-DDD");
//        String[] types = {"type1", "Type1", "type2", " type2  ", "tyPe2", 
//            "type3", "type4", "type5"};
//        for (int i = 0; i < 10; i++) {
//            int randomNum = 100 + (int)(Math.random() * 1000);
//            int randomDate = (int)(Math.random() * 365);
//            Date date = format.parse("2018-" + randomDate);
//            itemlist.add(ItemUtil.itemCombinedPrice("name", types[i%types.length], date, randomNum, 10, i));
//        }
        ItemDao items = new DatabaseItemDao("test.db", "Test");
//        items.add(itemlist);
        List<Item> itemList = items.getFromTo(Helpper.yearMonthDayToDate("2018-04-01"), Helpper.yearMonthDayToDate("2018-04-30"));
        System.out.println("Got:");
        for (Item item : itemList) {
            System.out.println(Helpper.dateToYearMonthDay(item.getDate()));
        }
        //BudgetBudyUi.launch(BudgetBudyUi.class);
    }
}
    