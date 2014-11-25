package ru.tsystems.tsproject.sbb.services.helpers;

/**
 * Created by Rin on 25.10.2014.
 */
public class NumberHelper {
    private NumberHelper(){};

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }
}
