package com.fbauth.checAuth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by labattula on 08/02/17.
 */

public class UtilsDate {

    public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

    public static String getDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        long longDate = Long.parseLong(str);
        String date = format.format(new Date(longDate));
        return date;
    }
}
