/*                                           WorkShiftTimeSliceAdapter.java
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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

// project imports
import com.gregsprogrammingworks.timeclock.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import com.gregsprogrammingworks.timeclock.R;

/**
 * Adapter presents shift, break, or lunch time slice for a work shift
 */
class WorkShiftTimeSliceAdapter extends ArrayAdapter<WorkShiftTimeSliceData> {

    /// Tag for logging
    private static final String TAG = WorkShiftTimeSliceAdapter.class.getSimpleName();

    private static final String kShiftLabel = "Shift";
    private static final String kBreakLabel = "Break";
    private static final String kLunchLabel = "Lunch";
    private static final String kTotalLabel = "Total";

    /// WorkShift live data
    private final MutableLiveData<WorkShift> mWorkShiftLiveData;

    /**
     * Constructor creates adapter for work shift live data, context
     * @param workShiftLiveData work shift live data
     * @param context   execution context
     */
    public WorkShiftTimeSliceAdapter(MutableLiveData<WorkShift> workShiftLiveData, Context context) {
        super(context, R.layout.row_shift_time_slice);
        mWorkShiftLiveData = workShiftLiveData;
        refresh();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View rowView;

        // Create a row instance
        WorkShiftTimeSliceRow row;
        if (convertView == null) {

            // It's a new view - inflate the view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.row_shift_time_slice, parent, false);

            // Create the row instance and store it in the view's tag
            row = new WorkShiftTimeSliceRow(rowView);
            rowView.setTag(row);
        }
        else {
            // No need for a new view
            rowView = convertView;

            // Get the row from the view
            row = (WorkShiftTimeSliceRow) rowView.getTag();
        }

        // Get the data for the row
        WorkShiftTimeSliceData data = getItem(position);

        // Set the info in the row.
        String label = data.getSliceLabel();
        row.setBold(false);
        row.setTimeSlice(label, data.getTimeSlice());
        if (kShiftLabel.equals(label)) {
            WorkShift workShift = mWorkShiftLiveData.getValue();
            row.setElapsedSeconds(workShift.onTheClockSeconds());
        }
        else if (kTotalLabel.equals(label)) {
            row.setBold(true);
        }
        // Return the completed view to render on screen
        return rowView;
    }

    /**
     * Refresh the list of slices
     */
    public void refresh() {
        clear();
        WorkShift workShift = mWorkShiftLiveData.getValue();
        maybeAddSlice(kShiftLabel, workShift.getShiftTimeSlice());
        maybeAddSlice(kBreakLabel, workShift.getBreakTimeSlice());
        maybeAddSlice(kLunchLabel, workShift.getLunchTimeSlice());
        if (1 < super.getCount()) {
            maybeAddSlice(kTotalLabel, workShift.getShiftTimeSlice());
        }
    }

    /**
     * Add a slice to the list, if appropriate
     * @param sliceText label for slice
     * @param slice slice start, end times
     */
    private void maybeAddSlice(String sliceText, TimeSlice slice) {
        try {
            // Throws an exception if sliceText, slice not valid
            WorkShiftTimeSliceData data = new WorkShiftTimeSliceData(sliceText, slice);

            // If we're here, we're good. Add it.
            add(data);
        }
        catch (IllegalArgumentException ex) {
        }
    }
}
