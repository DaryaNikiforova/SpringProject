package ru.tsystems.tsproject.sbb.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 28.11.14.
 */
public class ClientHelper {
    /**
     * Formats date to "dd.MM.yyyy в hh:mm" representation.
     * @param date formatted date.
     * @return string with formatted date.
     */
    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy в HH:mm");
        return df.format(date).replace(" ", "&nbsp;");
    }


    /**
     * Return formatted value of ticket price.
     * @param price double value of price.
     * @return string value of price.
     */
    public static String formatPrice(double price) {
        return String.valueOf((long) price) + "&nbsp;р.";
    }

    public static String formatService(String name, Double value) {
        return name + " (" + value + " р.)";
    }
}
