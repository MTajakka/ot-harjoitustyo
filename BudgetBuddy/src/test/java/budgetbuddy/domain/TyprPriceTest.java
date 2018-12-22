/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.domain;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markus
 */
public class TyprPriceTest {
    
    public TyprPriceTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void addition() {
        TypePrice type = new TypePrice("type", 200);
        type.addPrice(35);
        assertEquals(235,type.getPrice());
    }
    
    @Test
    public void order() {
        TypePrice type1 = new TypePrice("type1", 200);
        TypePrice type2 = new TypePrice("type2", 201);
        
        ArrayList<TypePrice> types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
        Collections.sort(types);
        
        assertEquals(201, types.get(1).getPrice());
    }
}
