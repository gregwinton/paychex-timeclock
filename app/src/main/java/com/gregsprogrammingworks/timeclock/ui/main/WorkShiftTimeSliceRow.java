package com.gregsprogrammingworks.timeclock.ui.main;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.gregsprogrammingworks.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.R;

import java.util.Date;

public class WorkShiftTimeSliceRow {
    private final Context mContext;

    private final TextView mLabelText;
    private final TextView mStartText;
    private final TextView mEndText;
    private final TextView mElapsedText;

    WorkShiftTimeSliceRow(View view) {
        mContext = view.getContext();
        mLabelText = view.findViewById(R.id.SliceLabel);
        mStartText = view.findViewById(R.id.StartTime);
        mEndText = view.findViewById(R.id.EndTime);
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
        String dateStr = formatDate(date);
        setStartText(dateStr);
    }

    private void setEndDate(Date date) {
        String dateStr = formatDate(date);
        setEndText(dateStr);
    }

    private void setElapsedSeconds(long elapsedSeconds) {
        String dateStr = formatElapsed(elapsedSeconds);
        setElapsedText(dateStr);
    }

    private String formatDate(Date date) {
        String retval = "";
        if (TimeSlice.isNilDate(date)) {
            retval = "--:--";
        }
        else {
            DateFormat dateFormat = new DateFormat();
            retval = (String) dateFormat.format("MM-dd hh:mm", date);
        }
        return retval;
    }

    private String formatElapsed(long elapsedSeconds) {
        long elapsedMinutes = elapsedSeconds / 60;
        long elapsedHours = elapsedMinutes / 60;

        elapsedMinutes %= 60;
        elapsedSeconds %= 60;

        String retval = String.format("%02d:%02d:%02d ",
                (int) elapsedHours,
                (int) elapsedMinutes,
                (int) elapsedSeconds);
        return retval;
    }
}
