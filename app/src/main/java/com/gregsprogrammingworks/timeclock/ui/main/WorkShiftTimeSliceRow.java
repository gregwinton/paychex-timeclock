package com.gregsprogrammingworks.timeclock.ui.main;

import android.view.View;
import android.widget.TextView;

import com.gregsprogrammingworks.common.TimeHelper;
import com.gregsprogrammingworks.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.R;

import java.util.Date;

public class WorkShiftTimeSliceRow {
    private final TextView mLabelText;
    private final TextView mStartText;
    private final TextView mEndText;
    private final TextView mElapsedText;

    WorkShiftTimeSliceRow(View view) {
        mLabelText = view.findViewById(R.id.SliceLabel);
        mStartText = view.findViewById(R.id.StartTimeText);
        mEndText = view.findViewById(R.id.EndTimeText);
        mElapsedText = view.findViewById(R.id.ElapsedTime);
    }

    void setTimeSlice(String label, TimeSlice timeSlice) {
        setLabelText(label);
        setStartDate(timeSlice.getStart());
        setEndDate(timeSlice.getEnd());
        setElapsedSeconds(timeSlice.elapsedSeconds());
    }

    void setLabelText(String text) {
        mLabelText.setText(text);
    }

    void setStartText(String text) {
        mStartText.setText(text);
    }

    void setEndText(String text) {
        mEndText.setText(text);
    }

    void setElapsedText(String text) {
        mElapsedText.setText(text);
    }

    private void setStartDate(Date date) {
        String dateStr = TimeHelper.formatDateTime(date);
        setStartText(dateStr);
    }

    private void setEndDate(Date date) {
        String dateStr = TimeHelper.formatDateTime(date);
        setEndText(dateStr);
    }

    private void setElapsedSeconds(long elapsedSeconds) {
        String elapsedStr = TimeHelper.formatElapsedSeconds(elapsedSeconds);
        setElapsedText(elapsedStr);
    }
}
