package com.gregsprogrammingworks.timeclock.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.common.TimeSlice;
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

        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_shift_time_slice, parent, false);

            viewHolder = new ViewHolder(convertView);
            viewHolder.mSliceLabel = (TextView) convertView.findViewById(R.id.SliceLabel);
            viewHolder.mSliceStart = (TextView) convertView.findViewById(R.id.StartTime);
            viewHolder.mSliceEnd = (TextView) convertView.findViewById(R.id.EndTime);
            viewHolder.mSliceElapsed = (TextView) convertView.findViewById(R.id.ElapsedTime);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        WorkShiftTimeSliceData data = getItem(position);
        viewHolder.mSliceLabel.setText(data.sliceText);
        viewHolder.mSliceStart.setText(data.startText);
        viewHolder.mSliceEnd.setText(data.endText);
        viewHolder.mSliceElapsed.setText(data.elapsedText);

        // Return the completed view to render on screen
        return result;
    }

    public void refresh() {
        clear();
        maybeAddSliceData("", "Stsrt", "End", "Elapsed");

        WorkShift workShift = mWorkShiftLiveData.getValue();
        maybeAddSlice("Shift", workShift.shiftTimeSlice());
        maybeAddSlice("Break", workShift.getBreakTimeSlice());
        maybeAddSlice("Lunch", workShift.getLunchTimeSlice());
    }

    private void maybeAddSlice(String sliceText, TimeSlice slice) {

        String startText = "", endText = "", elapsedText = "";
        if (slice.isActive()) {
            startText = slice.getStart().toString();
            endText = "--";
            elapsedText = formatElapsedTime(slice);
        }
        else if (slice.isComplete()) {
            startText = slice.getStart().toString();
            endText = slice.getEnd().toString();
            elapsedText = formatElapsedTime(slice);
        }
        maybeAddSliceData(sliceText, startText, endText, elapsedText);
    }

    private void maybeAddSliceData(String sliceText, String startText, String endText, String elapsedText) {
        try {
            WorkShiftTimeSliceData sliceData = new WorkShiftTimeSliceData(sliceText, startText,
                    endText, elapsedText);
            add(sliceData);
        }
        catch (Exception ex) {

        }
    }

    private String formatElapsedTime(TimeSlice slice) {
        long elapsedSeconds = slice.elapsedSeconds();
        long elapsedMinutes = elapsedSeconds / 60;
        long elapsedHours = elapsedMinutes / 60;

        elapsedMinutes %= 60;

        String retval = String.format("%02d:%02d", (int) elapsedHours, (int) elapsedMinutes);
        return retval;
    }

    private static class ViewHolder {
        TextView mSliceLabel;
        TextView mSliceStart;
        TextView mSliceEnd;
        TextView mSliceElapsed;

        ViewHolder(View view) {
            mSliceLabel = view.findViewById(R.id.SliceLabel);
            mSliceStart = view.findViewById(R.id.StartTime);
            mSliceEnd = view.findViewById(R.id.EndTime);
            mSliceElapsed = view.findViewById(R.id.ElapsedTime);
        }
    }
}
