package com.gregsprogrammingworks.timeclock.ui.main;

import android.view.View;
import android.widget.TextView;

import com.gregsprogrammingworks.common.TimeHelper;
import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.Date;

public class WorkShiftListItemRow {
    private final TextView mShiftDateText;
    private final TextView mStartDateText;
    private final TextView mEndDateText;
    private final TextView mTotalTimeText;
    private final TextView mNetTimeText;

    public WorkShiftListItemRow(View view) {
        mShiftDateText = view.findViewById(R.id.ShiftDateText);
        mStartDateText = view.findViewById(R.id.ShiftStartDateText);
        mEndDateText = view.findViewById(R.id.ShiftEndDateText);
        mTotalTimeText = view.findViewById(R.id.ShiftTotalTimeText);
        mNetTimeText = view.findViewById(R.id.ShiftNetTimeText);
    }

    public void setWorkShift(WorkShift workShift) {
        setShiftDate(workShift.getShiftTimeSlice().getStart());
        setStartDate(workShift.getShiftTimeSlice().getStart());
        setEndDate(workShift.getShiftTimeSlice().getEnd());
        setTotalTime(workShift.shiftSeconds());
        setNetTime(workShift.onTheClockSeconds());
    }

    void setShiftDate(Date date) {
        String dateStr = TimeHelper.formatDate(date);
        mShiftDateText.setText(dateStr);
    }

    void setStartDate(Date date) {
        String dateStr = TimeHelper.formatTime(date);
        mStartDateText.setText(dateStr);
    }

    void setEndDate(Date date) {
        String dateStr = TimeHelper.formatTime(date);
        mEndDateText.setText(dateStr);
    }

    void setTotalTime(long seconds) {
        String elapsedStr = TimeHelper.formatElapsedSeconds(seconds);
        mTotalTimeText.setText(elapsedStr);
    }

    void setNetTime(long seconds) {
        String elapsedStr = TimeHelper.formatElapsedSeconds(seconds);
        mNetTimeText.setText(elapsedStr);
    }

}
