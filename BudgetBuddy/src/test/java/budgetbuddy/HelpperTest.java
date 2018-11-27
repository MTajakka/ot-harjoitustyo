/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import budgetbuddy.Helpper;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author markus
 */
public class HelpperTest {
    
    public HelpperTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void rightDateFormatParse() throws Exception{
        String rightDate = "2018-08-10";
        Date date = Helpper.yearMonthDayToDate(rightDate);

    }

    @Test
    public void wrongDateFormatParse() throws Exception{
        String wrongDate = "20180810";
        try {
            Date date = Helpper.yearMonthDayToDate(wrongDate);
            throw new Exception("Should have thrown");
        } catch (ParseException e) {
        }
    }
    
    @Test
    public void rightISOFormat() {
        Date date = new Date(2018-1900, 8-1, 10);
        String ISODate = Helpper.dateToYearMonthDay(date);
        assertEquals("2018-08-10", ISODate);
    }
    
    @Test
    public void daysAddedCorrecly() {
        Date date = new Date(2018-1900, 8-1, 10);
        Date target = new Date(2018-1900, 9-1, 9);
        
        date = Helpper.addDays(date, 30);
        assertEquals(target, date);
    }
    
    @Test
    public void monthsAddedCorrectly() {
        Date date = new Date(2018-1900, 8-1, 10);
        Date target = new Date(2019-1900, 2-1, 10);
        
        date = Helpper.addMonths(date, 6);
        assertEquals(target, date);
    }

}
