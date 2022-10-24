/*                                              WorkShiftTimeSliceData.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Data for rows in Work Shift Slice Data list view
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
package com.gregsprogrammingworks.timeclock.ui.main;

// project importx
import com.gregsprogrammingworks.timeclock.common.TimeSlice;

/**
 * Data for rows in Work Shift Slice Data list view
 */
public class WorkShiftTimeSliceData {

    /// Tag for logging
    private static final String TAG = WorkShiftTimeSliceData.class.getSimpleName();

    /// slice entry label ("shift", "break", "lunch")
    private final String mSliceLabel;

    // slice start, end times
    private final TimeSlice mTimeSlice;

    /**
     * Creates data for label and slice
     * @param sliceLabel    label for slice
     * @param timeSlice     start, end times for slice
     * @throws IllegalArgumentException if time label and/or time slice is invalid
     */
    public WorkShiftTimeSliceData(String sliceLabel, TimeSlice timeSlice) throws IllegalArgumentException {
        // Validate params
        ValidParamsOrThrow(sliceLabel, timeSlice);

        // If we're here, we're ok. Save label, slice
        mSliceLabel = sliceLabel;
        mTimeSlice = timeSlice;
    }

    /// Get accessor for slice label
    public String getSliceLabel() {
        return mSliceLabel;
    }

    /// Get accessor for slice start, end time
    TimeSlice getTimeSlice() {
        return mTimeSlice;
    }

    /**
     * Validate the slice data parametners
     * @param sliceLabel    slice label
     * @param timeSlice     slice start, end times
     * @throws IllegalArgumentException Error in slice data
     */
    private static void ValidParamsOrThrow(String sliceLabel, TimeSlice timeSlice)
            throws  IllegalArgumentException {
        if (timeSlice.isActive()) {
            // If its active, we're ok
            return;
        }
        else if (timeSlice.isComplete()) {
            // If it's complete, we're ok
            return;
        }

        // If we're here there's at nothing to show
        throw new IllegalArgumentException(TAG + ": Nothing to show");
    }
}
