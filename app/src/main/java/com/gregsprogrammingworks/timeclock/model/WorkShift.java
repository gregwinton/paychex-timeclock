package com.gregsprogrammingworks.timeclock.model;

import com.gregsprogrammingworks.common.TimeSlice;

public class WorkShift {

    private static final String TAG = WorkShift.class.getSimpleName();

    private final String mId;

    private final TimeSlice mShiftTimeSlice;
    private final TimeSlice mBreakTimeSlice;
    private final TimeSlice mLunchTimeSlice;

    public WorkShift(String id) {
        mId = id;
        mShiftTimeSlice = new TimeSlice();
        mBreakTimeSlice = new TimeSlice();
        mLunchTimeSlice = new TimeSlice();
    }

    public long onTheClockSeconds() {
        long retval = shiftSeconds();
        retval -= breakSeconds();
        retval -= lunchSeconds();
        return retval;
    }

    public long shiftSeconds() {
        long retval = mShiftTimeSlice.elapsedSeconds();
        return retval;
    }

    public boolean canStartShift() {
        boolean retval = !mShiftTimeSlice.isStarted();
        return retval;
    }

    public boolean canEndShift() {
        boolean retval = true;

        // Can't end if we're not active
        if (!mShiftTimeSlice.isActive()) {
            retval = false;
        }

        // Can't end if we're on break
        else if (mBreakTimeSlice.isActive()) {
            retval = false;
        }

        // Can't end if we're at lunch
        else if (mLunchTimeSlice.isActive()) {
            retval = false;
        }
        return retval;
    }

    public void startShift() throws IllegalStateException {
        mShiftTimeSlice.start();
    }

    public void endShift() throws IllegalStateException {
        mShiftTimeSlice.end();
    }

    public TimeSlice shiftTimeSlice() {
        return mShiftTimeSlice;
    }

    public boolean canStartBreak() {
        boolean retval = true;

        // Can't start lunch if we're not active
        if (!mShiftTimeSlice.isActive()) {
            retval = false;
        }

        // Can't break if we've already on broke,
        else if (mBreakTimeSlice.isStarted()) {
            retval = false;
        }

        // Can't lunch again - not with *that* attitude... wait 'til i get my admin powers...
        else if (mLunchTimeSlice.isActive()) {
            retval = false;
        }

        return retval;
    }

    public boolean canEndBreak() {

        boolean onBreak = mBreakTimeSlice.isActive();
        return onBreak;
    }

    public void startBreak() throws IllegalStateException {
        mBreakTimeSlice.start();
    }

    public void endBreak() throws IllegalStateException {
        mBreakTimeSlice.end();
    }

    public TimeSlice getBreakTimeSlice() {
        return mBreakTimeSlice;
    }

    public long breakSeconds() {
        long retval = mBreakTimeSlice.elapsedSeconds();
        return retval;
    }

    public boolean canStartLunch() {
        boolean retval = true;

        // Can't start lunch if we're not active
        if (!mShiftTimeSlice.isActive()) {
            retval = false;
        }

        // Can't lunch if we're on break
        else if (mBreakTimeSlice.isActive()) {
            retval = false;
        }

        // Can't lunch again - not with *that* attitude... wait 'til i get my admin powers...
        else if (mLunchTimeSlice.isStarted()) {
            retval = false;
        }

        return retval;
    }

    public boolean canEndLunch() {
        boolean outToLunch = mLunchTimeSlice.isActive();
        return outToLunch;
    }

    public void startLunch() throws IllegalStateException {
        mLunchTimeSlice.start();
    }

    public void endLunch() throws IllegalStateException {
        mLunchTimeSlice.end();
    }

    public TimeSlice getLunchTimeSlice() {
        return mLunchTimeSlice;
    }

    public long lunchSeconds() {
        long retval = mLunchTimeSlice.elapsedSeconds();
        return retval;
    }
}
