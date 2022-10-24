package com.gregsprogrammingworks.timeclock.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.R;

import com.gregsprogrammingworks.timeclock.model.WorkShift;

public class WorkShiftTimeSliceAdapter extends ArrayAdapter<WorkShiftTimeSliceData> {

    private final MutableLiveData<WorkShift> mWorkShiftLiveData;

    public WorkShiftTimeSliceAdapter(MutableLiveData<WorkShift> workShiftLiveData, Context context) {
        super(context, R.layout.row_shift_time_slice);
        mWorkShiftLiveData = workShiftLiveData;
        refresh();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View result;

        WorkShiftTimeSliceRow row;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_shift_time_slice, parent, false);

            row = new WorkShiftTimeSliceRow(convertView);

            result = convertView;

            convertView.setTag(row);
        }
        else {
            row = (WorkShiftTimeSliceRow) convertView.getTag();
            result = convertView;
        }

        WorkShiftTimeSliceData data = getItem(position);
        row.setTimeSlice(data.getSliceLabel(), data.getTimeSlice());

        // Return the completed view to render on screen
        return result;
    }

    public void refresh() {
        clear();
        WorkShift workShift = mWorkShiftLiveData.getValue();
        maybeAddSlice("Shift", workShift.getShiftTimeSlice());
        maybeAddSlice("Break", workShift.getBreakTimeSlice());
        maybeAddSlice("Lunch", workShift.getLunchTimeSlice());
    }

    private void maybeAddSlice(String sliceText, TimeSlice slice) {

        try {
            WorkShiftTimeSliceData data = new WorkShiftTimeSliceData(sliceText, slice);
            add(data);
        }
        catch (IllegalArgumentException ex) {

        }
    }
}
