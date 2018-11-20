package budgetbuddy.domain;

import static budgetbuddy.domain.ItemUtil.itemCombinedPrice;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemTest {
    
    public ItemTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void itemHoldsAllData() {
        Item item = new Item("Item 3", "Item", new Date(2018-1900, 8-1, 10), 395, 10, 3);
        assertEquals("Item 3: 3, 'Item', 2018-08-10, 3.95€, 10", item.toString());
    }
    
    @Test
    public void itemWthoutId() {
        Item item = new Item("Item 3", "Item", new Date(2018-1900, 8-1, 10), 395, 10);
        assertEquals("Item 3: -1, 'Item', 2018-08-10, 3.95€, 10", item.toString());
    }
    
    @Test
    public void itemComaprisonIsOnlyDebendentOnNameAndType() {
        Item item3 = new Item("Item 3", "Item", new Date(2018-1900, 8-1, 10), 395, 10, 3);
        Item item5 = new Item("Item 3", "Item", new Date(2013-1900, 3-1, 25), 595, 5, 4);
        boolean equals = item3.equals(item5);
        assertTrue(equals);
    }
    
    @Test
    public void utilCreatesAnItem() {
        Item item = itemCombinedPrice("Item 3", "Item", new Date(2018-1900, 8-1, 10), 649, 11, 3);
        assertEquals("Item 3: 3, 'Item', 2018-08-10, 0.59€, 11", item.toString());
    }
    
    @Test
    public void utilCreatesAnItemNoId() {
        Item item = itemCombinedPrice("Item 3", "Item", new Date(2018-1900, 8-1, 10), 649, 11);
        assertEquals("Item 3: -1, 'Item', 2018-08-10, 0.59€, 11", item.toString());
    }
    
}
