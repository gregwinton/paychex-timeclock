/*                                                WorkShiftListItemRow.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Manages a row in a list of work shifts
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

// project imports
import com.gregsprogrammingworks.timeclock.common.TimeHelper;
import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

/**
 * Manages a row in a list of work shifts
 */
public class WorkShiftListItemRow {

    /// text view for shift date
    private final TextView mShiftDateText;

    /// text view for shift start date/time
    private final TextView mStartDateText;

    /// text view for shift end date/time
    private final TextView mEndDateText;

    /// text view for total shift time, breaks notwithstanding
    private final TextView mTotalTimeText;

    /// text view for net shift time: total time less break and lunch times
    private final TextView mNetTimeText;

    /**
     * Instantiate a work shift list item row instance
     * @param view  View containing associated controls
     */
    public WorkShiftListItemRow(View view) {
        mShiftDateText = view.findViewById(R.id.ShiftDateText);
        mStartDateText = view.findViewById(R.id.ShiftStartDateText);
        mEndDateText = view.findViewById(R.id.ShiftEndDateText);
        mTotalTimeText = view.findViewById(R.id.ShiftTotalTimeText);
        mNetTimeText = view.findViewById(R.id.ShiftNetTimeText);
    }

    /**
     * Set the work shift displayed in the row
     * @param workShift
     */
    public void setWorkShift(WorkShift workShift) {
        setShiftDate(workShift.getShiftTimeSlice().getStartDate());
        setStartDate(workShift.getShiftTimeSlice().getStartDate());
        setEndDate(workShift.getShiftTimeSlice().getEndDate());
        setTotalTime(workShift.shiftSeconds());
        setNetTime(workShift.onTheClockSeconds());
    }

    /**
     * Set the shift date
     * @param date  date of the shift
     */
    void setShiftDate(Date date) {
        String dateStr = TimeHelper.formatDate(date);
        mShiftDateText.setText(dateStr);
    }

    /**
     * Set the shift start time
     * @param date date the shift started
     */
    void setStartDate(Date date) {
        String dateStr = TimeHelper.formatTime(date);
        mStartDateText.setText(dateStr);
    }

    /**
     * Set the shift end time
     * @param date  date the shifted ended
     */
    void setEndDate(Date date) {
        String dateStr = TimeHelper.formatTime(date);
        mEndDateText.setText(dateStr);
    }

    /**
     * Set the total elapsed time of the shift
     * @param seconds   total elapsed time of shift, in seconds
     */
    void setTotalTime(long seconds) {
        String elapsedStr = TimeHelper.formatElapsedSeconds(seconds);
        mTotalTimeText.setText(elapsedStr);
    }

    /**
     * Set the net elapsed time of the shift
     * @param seconds   net elapsed time of shift, (total less break and lunch)
     */
    void setNetTime(long seconds) {
        String elapsedStr = TimeHelper.formatElapsedSeconds(seconds);
        mNetTimeText.setText(elapsedStr);
    }
}
