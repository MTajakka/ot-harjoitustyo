package budgetbuddy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Helpper {
    /**
     * Parses a java.util.Date from 'yyyy-MM-dd' string format
     * @param date string from date is to be parsed
     * @return Date
     * @throws Exception 
     */
    public static Date yearMonthDayToDate(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(date);
    }
    /**
     * Translates java.util.Date to 'yyyy-MM-dd' string format
     * @param date Date to be translated
     * @return String in form of 'yyyy-MM-dd' string format
     */
    public static String dateToYearMonthDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    /**
    *Method to add days to a date
    * 
    * @param date java.util.Date from which days are calculated
    * @param days how many days are to be added
    * 
    * @return returns a Date days vairable many days added to given date
    */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    
    /**
    *Method to add months to a date
    *
    *@param date java.util.Date from which months are calculated
    *@param months how manty months are to be added
    *
    *@return returns a Date months variable many monhts added given date
    */
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
     /**
      * Finds the first day of the mont
      * @param date java.util.Date from which month is taken
      * @return Date first day of the month
      * @throws Exception 
      */
    public static Date firstDayOfMonth(Date date) throws Exception {
        String now = dateToYearMonthDay(date);
        String firstDay = now.substring(0, 8) + "01";
        return yearMonthDayToDate(firstDay);
    }
    
    /**
     * Translates java.time.LocalDate to java.util.Date
     * @param date LocalDate to be translated
     * @return translated Date 
     */
    public static Date loaclDateToDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    
    
}
