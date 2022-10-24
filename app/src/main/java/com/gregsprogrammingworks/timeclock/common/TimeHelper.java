/*                                                          TimeHelper.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Helpful time-related functions for the sample PayChex Time Clock app.
 * ------------------------------------------------------------------------
 *
 * COPYRIGHT:
 * ---------
 *  Copyright (C) 2022 Greg Winton
 * ------------------------------------------------------------------------
 *
 * LICENSE:
 * -------
 *  This program is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.
 *
 *  If not, see http://www.gnu.org/licenses/.
 * ------------------------------------------------------------------------ */
package com.gregsprogrammingworks.timeclock.common;

// language, os imports
import java.util.Date;
import android.text.format.DateFormat;

/**
 * Provides time-related helper methods
 */
public class TimeHelper {

    /// Dawn of the Epoch, define as commonly-accepted nil
    private static final Date kNilDate = new Date(0);

    /// Return the commonly-accepted nil date
    public static Date nilDate() { return kNilDate; }

    /// Helper method tests whether a date is "nil" - i.e., equal to kNilDate
    public static boolean isNilDate(Date date) { return (null == date) || date.equals(kNilDate); }

    /**
     * Format date and time, according to "secret" rules.
     * @// TODO: 10/24/22 Move format strings (i.e., secret rules) to strings.xml
     * @param date  DateTime whose date and time to format
     * @return  Date and time formatted as string, or "nil date-time" indicater
     */
    public static String formatDateTime(Date date) {

        // Init return value - in case date is invalid
        String retval = "-----";   // TODO: Move to strings.xml

        if (! isNilDate(date)) {

            // date is not nil - format as date and time
            DateFormat dateFormat = new DateFormat();
            // --> note 24 hour clock in format
            retval = (String) dateFormat.format("MM-dd HH:mm", date);   // TODO: Move to strings.xml
        }
        
        // Return result
        return retval;
    }

    /**
     * Format date, according to "secret" rules.
     * @// TODO: 10/24/22 Move format strings (i.e., secret rules) to strings.xml
     * @param date  DateTime whose date to format
     * @return  Date formatted as string, or "nil date" indicater
     */
    public static String formatDate(Date date) {

        // Init return value - in case date is invalid
        String retval = "--/--";    // TODO: Move to strings.xml
        if (! isNilDate(date)) {
            // date is not nil - format as date only
            DateFormat dateFormat = new DateFormat();
            // --> note "medium" month name ("JAN", "FEB", etc.)
            retval = (String) dateFormat.format("MMM dd", date);
        }

        // return result
        return retval;
    }

    /**
     * Format time, according to "secret" rules.
     * @// TODO: 10/24/22 Move format strings (i.e., secret rules) to strings.xml
     * @param date  DateTime whose time to format
     * @return  Date formatted as string, or "nil date" indicater
     */
    public static String formatTime(Date date) {
        // Init return value - in case date is invalid
        String retval = "--:--";
        if (! isNilDate(date)) {
            // date is not nil - format as date only
            DateFormat dateFormat = new DateFormat();
            retval = (String) dateFormat.format("HH:mm", date);
        }

        // Return result
        return retval;
    }

    /**
     * Format elapsed seconds as hours, minutes, and seconds
     * @param elapsedSeconds    Elapsed seconds to format
     * @return  Elapsed seconds formatted as 00:00:00 (hours, minutes, seconds)
     */
    public static String formatElapsedSeconds(long elapsedSeconds) {

        // Calculate completed minutes and hours
        long elapsedMinutes = elapsedSeconds / 60;
        long elapsedHours = elapsedMinutes / 60;

        // Calculate remainders for hours, minutes, seconds
        int hoursRemaining = (int) elapsedHours;    // Assume less than 2^31, or even 2^15
        int secondsRemaining = (int)(elapsedSeconds % 60);
        int minutesRemaining = (int)(elapsedMinutes % 60);

        // Format as "00:00:00"
        String retval = String.format("%02d:%02d:%02d ",
                hoursRemaining, minutesRemaining,
                secondsRemaining);

        // return result
        return retval;
    }
}
