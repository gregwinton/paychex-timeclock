/*                                               WorkShiftTimeSliceRow.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Adapter presents shift, break, or lunch time slice for a work shift
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

// language, os, platform imports
import java.util.Date;

import android.view.View;
import android.widget.TextView;

// Project imports
import com.gregsprogrammingworks.timeclock.common.TimeHelper;
import com.gregsprogrammingworks.timeclock.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.R;

/**
 * Manages a row in a list of named time slices
 */
public class WorkShiftTimeSliceRow {

    /// Slice label text view
    private final TextView mLabelText;

    /// Slice start date/time text view
    private final TextView mStartText;

    /// Slice end date/time text view
    private final TextView mEndText;

    /// Slice elapsed time (in seconds) text view
    private final TextView mElapsedText;

    /**
     * Connect to a time slice row
     * @param view  view of row with which to connect
     */
    WorkShiftTimeSliceRow(View view) {

        // Get the views
        mLabelText = view.findViewById(R.id.SliceLabel);
        mStartText = view.findViewById(R.id.StartTimeText);
        mEndText = view.findViewById(R.id.EndTimeText);
        mElapsedText = view.findViewById(R.id.ElapsedTime);
    }

    /**
     * Set the view values from a label and time slice
     * @param label label to set
     * @param timeSlice time slice to set
     */
    void setTimeSlice(String label, TimeSlice timeSlice) {
        setLabelText(label);
        setStartDate(timeSlice.getStartDate());
        setEndDate(timeSlice.getEndDate());
        setElapsedSeconds(timeSlice.elapsedSeconds());
    }

    /**
     * Set the slice label text
     * @param text  new text for slice label
     */
    void setLabelText(String text) {
        mLabelText.setText(text);
    }

    /**
     * Set the slice start date/time text
     * @param text  slice start date/time text
     */
    void setStartText(String text) {
        mStartText.setText(text);
    }

    /**
     * Set the slice end date/time text
     * @param text  slice end date/time text
     */
    void setEndText(String text) {
        mEndText.setText(text);
    }

    /**
     * Set the slice elapsed time in seconds text
     * @param text  slice elapsed time in seconds text
     */
    void setElapsedText(String text) {
        mElapsedText.setText(text);
    }

    /**
     * Set the start date text with a date
     * @param date  date to set as start date text
     */
    void setStartDate(Date date) {
        String dateStr = TimeHelper.formatDateTime(date);
        setStartText(dateStr);
    }

    /**
     * Set the end date text with a date
     * @param date  date to set as end date text
     */
    void setEndDate(Date date) {
        String dateStr = TimeHelper.formatDateTime(date);
        setEndText(dateStr);
    }

    /**
     * Set the elapsed time text with the number of elapsed seconds
     * @param elapsedSeconds    number of elapsed seconds
     */
    void setElapsedSeconds(long elapsedSeconds) {
        String elapsedStr = TimeHelper.formatElapsedSeconds(elapsedSeconds);
        setElapsedText(elapsedStr);
    }
}
