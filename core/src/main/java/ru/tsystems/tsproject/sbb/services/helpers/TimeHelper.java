package ru.tsystems.tsproject.sbb.services.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implements set of methods to help fill transfer objects fields. Methods need to calculate
 * metrics needed for client data representation.
 * @author Daria Nikiforova
 */
public class TimeHelper {
    private TimeHelper() {}

    /**
     * Calculates new date by subtracting hours and minutes.
     * @param date being modified.
     * @param hour to subtract.
     * @param minute to subtract.
     * @return modified date.
     */
    public static Date getHourBack(Date date, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, - hour);
        cal.add(Calendar.MINUTE, - minute);
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    /**
     * Calculates new date by adding hours and minutes.
     * @param date being modified.
     * @param hour to add.
     * @param minute to add.
     * @return modified date.
     */
    public static Date addHours (Date date, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * Returns the date part of date6 without time.
     * @param date formatted date.
     * @return only date part.
     */
    public static String getDatePart(Date date) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date);
    }
}
