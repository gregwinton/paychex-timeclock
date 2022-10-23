package com.gregsprogrammingworks.timeclock.ui.main;

import com.gregsprogrammingworks.common.TimeSlice;

public class WorkShiftTimeSliceData {
    private static final String TAG = WorkShiftTimeSliceData.class.getSimpleName();
    final String mSliceLabel;
    final TimeSlice mTimeSlice;

    WorkShiftTimeSliceData(String sliceLabel, TimeSlice timeSlice)
            throws  IllegalArgumentException {
        ValidParamsOrThrow(sliceLabel, timeSlice);
        mSliceLabel = sliceLabel;
        mTimeSlice = timeSlice;
    }

    String getSliceLabel() {
        return mSliceLabel;
    }

    TimeSlice getTimeSlice() {
        return mTimeSlice;
    }

    private static void ValidParamsOrThrow(String sliceLabel, TimeSlice timeSlice)
            throws  IllegalArgumentException {
        if (timeSlice.isActive()) {
            return;
        }
        else if (timeSlice.isComplete()) {
            return;
        }

        // If we're here there's at nothing to show
        throw new IllegalArgumentException(TAG + ": Nothing to show");
    }
}
