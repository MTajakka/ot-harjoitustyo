
package budgetbuddy;

import budgetbuddy.ui.UiInit;
import java.util.Date;
import budgetbuddy.Helpper;
import budgetbuddy.dao.ItemDao;
import budgetbuddy.dao.DatabaseItemDao;
import budgetbuddy.dao.DatabaseUserDao;
import budgetbuddy.dao.UserDao;
import budgetbuddy.domain.Item;
import budgetbuddy.domain.ItemUtil;
import budgetbuddy.domain.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.application.Application;

public class Init {
    
    public static void main(String[] args) throws Exception {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-DDD");
//        ItemDao items = new DatabaseItemDao("test.db", "Test");
//        List<Integer> deletable = new ArrayList<>();
//        for (int i = 1; i <= items.getMaxId(); i++) {
//            deletable.add(i);
//        }
//        items.delete(deletable);
//        System.out.println("deleted");
//        List<Item> itemList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Date date = format.parse("2018-" + (100 + i));
//            Item item = new Item("Item" + i, "type", date, 100 * i, i);
//            itemList.add(item);
//        }
//        items.add(itemList);
//        Date date = format.parse("2018-" + (100 + 5));
//        Item item = new Item("Item" + 5, "type", date, 100 * 5 + 1, 5 , 5);
//        items.update(item);
//        items.delete(5);
//        items.add(item);
//        items.deleteTable();
//        User user1 = new User("User1", "test.db", "table1");
//        UserDao users = new DatabaseUserDao("test.db", "userTest");
//        users.add(user1);
//        System.out.println(users.get(1));
//        System.out.println(users.containsName("User1"));

        Application.launch(UiInit.class);
    }
}
    