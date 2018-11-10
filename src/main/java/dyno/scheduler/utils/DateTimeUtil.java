/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Prabash
 */
public class DateTimeUtil
{

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_TIME_FORMAT_JSON = "yyyy-MM-dd HH:mm:ss";
    
    public static DateTimeFormatter getDateFormat()
    {
        return DateTimeFormat.forPattern(DATE_FORMAT);
    }
    
    public static DateTimeFormatter getTimeFormat()
    {
        return DateTimeFormat.forPattern(TIME_FORMAT);
    }
    
    public static DateTimeFormatter getDateTimeFormat()
    {
        return DateTimeFormat.forPattern(DATE_TIME_FORMAT);
    }

    public static DateTimeFormatter getDateTimeFormatJson()
    {
        return DateTimeFormat.forPattern(DATE_TIME_FORMAT_JSON);
    }
    
    public static DateTime concatenateDateTime(String date, String time)
    {
        String concatStringDate = date + " " + time;
        return getDateTimeFormat().parseDateTime(concatStringDate);
    }
    
    public static DateTime concatenateDateTime(DateTime date, String time)
    {
        String concatStringDate = date.toString(getDateFormat()) + " " + time;
        return getDateTimeFormat().parseDateTime(concatStringDate);
    }
    
    public static DateTime concatenateDateTime(String date, DateTime time)
    {
        String concatStringDate = date + " " + time.toString(getTimeFormat());
        return getDateTimeFormat().parseDateTime(concatStringDate);
    }
    
    public static DateTime concatenateDateTime(DateTime date, DateTime time)
    {
        String concatStringDate = date.toString(getDateFormat()) + " " + time.toString(getTimeFormat());
        return getDateTimeFormat().parseDateTime(concatStringDate);
    }
    
    public static DateTime concatenateDateTime(LocalDate date, LocalTime time)
    {
        String concatStringDate = date.toString(getDateFormat()) + " " + time.toString(getTimeFormat());
        return getDateTimeFormat().parseDateTime(concatStringDate);
    }
}
