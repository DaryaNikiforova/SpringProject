package ru.tsystems.tsproject.sbb.helpers;

import java.nio.charset.Charset;

/**
 * Created by apple on 23.10.2014.
 */
public class StringHelper {
    public static String encode(String string) {
        return new String(string.getBytes(), Charset.forName("UTF-8"));
    }
}
