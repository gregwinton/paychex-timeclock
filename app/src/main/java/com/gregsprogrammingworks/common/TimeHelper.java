package com.gregsprogrammingworks.common;

import android.text.format.DateFormat;

import java.util.Date;

public class TimeHelper {

    private static final Date kNilDate = new Date(0);

    public static Date nilDate() { return kNilDate; }

    public static boolean isNilDate(Date date) { return kNilDate.equals(date); }

    public static String formatDateTime(Date date) {
        String retval = "";
        if (isNilDate(date)) {
            retval = "--:--";
        }
        else {
            DateFormat dateFormat = new DateFormat();
            retval = (String) dateFormat.format("MM-dd HH:mm", date);
        }
        return retval;
    }

    public static String formatDate(Date date) {
        String retval = "";
        if (isNilDate(date)) {
            retval = "--/--";
        }
        else {
            DateFormat dateFormat = new DateFormat();
            retval = (String) dateFormat.format("MMM dd", date);
        }
        return retval;
    }

    public static String formatTime(Date date) {
        String retval = "";
        if (isNilDate(date)) {
            retval = "--:--";
        }
        else {
            DateFormat dateFormat = new DateFormat();
            retval = (String) dateFormat.format("HH:mm", date);
        }
        return retval;
    }

    public static String formatElapsedSeconds(long elapsedSeconds) {
        long elapsedMinutes = elapsedSeconds / 60;
        long elapsedHours = elapsedMinutes / 60;

        elapsedMinutes %= 60;
        elapsedSeconds %= 60;

        String retval = String.format("%02d:%02d:%02d ",
                (int) elapsedHours,
                (int) elapsedMinutes,
                (int) elapsedSeconds);
        return retval;
    }
}
