/*                                                           TimeSlice.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Represents a slice of time, from start to end.
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
import java.util.Objects;

/**
 * A slice of time, from start to end.
 */
public class TimeSlice {

    /// Tag for logging
    private static final String TAG = TimeSlice.class.getSimpleName();

    /// Start date/time of slice - nil if not started
    private Date mStartDate;

    /// End date/time of slice - nil if not started or not ended
    private Date mEndDate;

    /**
     * Default constructor for time slice
     */
    public TimeSlice() {
        // Initialize start and finish to nil
        mStartDate = TimeHelper.nilDate();
        mEndDate = TimeHelper.nilDate();
    }

    /**
     * Parameterized constructor. Allows caller to set specific start, end
     * times. Useful for reconstituting persisted data into object.
     * @param startDate
     * @param endDate
     */
    public TimeSlice(Date startDate, Date endDate) {

        // Save the start, end dates
        mStartDate = startDate;
        mEndDate = endDate;
    }

    /**
     * "Copy" constructor.
     * @param that Time Slice instance to copy
     */
    protected TimeSlice(TimeSlice that) {
        this(that.getStartDate(), that.getEndDate());
    }

    /**
     * How many seconds in the time slice
     * @returns if finished, time in seconds elapsed from start to end.
     * @returns if not finished, time in seconds from start to "now"
     */
    public long elapsedSeconds() {
        // Initialize return value
        long elapsedSeconds = 0;    // If we're not started, no time has elapsed
        if (isStarted())
        {
            // Figure out which end date to use
            Date endDate = TimeHelper.isNilDate(mEndDate)   // Is slice end date nil?
                    ?   new Date()                          // Yes! Use "now"
                    :   mEndDate;                           // No! use slice end date

            // Get start, end date in millis
            long startMillis = mStartDate.getTime();
            long endMillis = endDate.getTime();

            // Get diff in millis, convert to seconds
            long elapsedMillis = endMillis - startMillis;
            elapsedSeconds = elapsedMillis / 1000;
        }

        // Return result
        return elapsedSeconds;
    }

    /**
     * Is the slice "active" - ie, started but not ended
     * @returns true if started and not ended
     * @returns false if not started
     * @returns false if started and ended
     */
    public boolean isActive() {
        // Assume not active
        boolean retval = false;
        if (isStarted()) {
            // started
            if (! isComplete()) {
                // not complete
                retval = true;  // Ergo active
            }
        }

        // Return result
        return retval;
    }

    /**
     * Is the slice started, whether or not ended
     * Slice is started if start date is not nil
     * @return  true if started, or false
     */
    public boolean isStarted() {
        // Check if the start date is *not* nil
        Date start = getStartDate();
        boolean isStarted = !TimeHelper.isNilDate(start);
        return isStarted;
    }

    /**
     * Is the slice completed
     * Slice is completed if end date is not nil
     * @return  true if completed, or false
     */
    public boolean isComplete() {
        // Check if the end date is *not* nil
        Date end = getEndDate();
        boolean isEnded = !TimeHelper.isNilDate(end);
        return isEnded;
    }

    /**
     * Start the time slice.
     * @throws IllegalStateException if slice is already started
     */
    public void start() throws IllegalStateException {
        // Thrown an exception if we're already started
        if (isStarted()) {
            throw new IllegalStateException(TAG + ": time slice already started");
        }

        // Set the start date, and we're off
        mStartDate = new Date();
    }

    /**
     * End the time slice.
     * @throws IllegalStateException if slice is not started or already ended
     */
    public void end() throws IllegalStateException {
        // Throw an exception if we're not active (ie, not started, or already ended)
        if (! isActive()) {
            throw new IllegalStateException(TAG + ": time slice already started");
        }

        // Set the end date, and we're done
        mEndDate = new Date();
    }

    /**
     * Get the start date and time
     * @return  start date/time
     */
    public Date getStartDate() {
        return mStartDate;
    }

    /**
     * Get the end date and time
     * @return end date/time
     */
    public Date getEndDate() {
        return mEndDate;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(mStartDate, mEndDate);
        return hash;
    }
}
