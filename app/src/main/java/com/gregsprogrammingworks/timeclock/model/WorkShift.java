package com.gregsprogrammingworks.timeclock.model;

import com.gregsprogrammingworks.common.TimeSlice;

public class WorkShift {

    private static final String TAG = WorkShift.class.getSimpleName();

    private final String mId;

    private final TimeSlice mTotalTime;
    private final TimeSlice mBreakTime;
    private final TimeSlice mLunchTime;

    public WorkShift(String id) {
        mId = id;
        mTotalTime = new TimeSlice();
        mBreakTime = new TimeSlice();
        mLunchTime = new TimeSlice();
    }

    public long elapsedSeconds() {
        long retval = mTotalTime.elapsedSeconds();
        return retval;
    }

    boolean canStartShift() {
        boolean retval = !mTotalTime.isStarted();
        return retval;
    }

    boolean canEndShift() {
        boolean retval = true;

        // Can't end if we're not active
        if (!mTotalTime.isActive()) {
            retval = false;
        }

        // Can't end if we're on break
        else if (mBreakTime.isActive()) {
            retval = false;
        }

        // Can't end if we're at lunch
        else if (mLunchTime.isActive()) {
            retval = false;
        }
        return retval;
    }

    public void startShift() throws IllegalStateException {
        mTotalTime.start();
    }

    public void endShift() throws IllegalStateException {
        mTotalTime.end();
    }

    public long shiftSeconds() {
        long retval = elapsedSeconds();
        retval -= breakSeconds();
        retval -= lunchSeconds();
        return retval;
    }

    boolean canStartBreak() {
        boolean retval = true;

        // Can't start lunch if we're not active
        if (!mTotalTime.isActive()) {
            retval = false;
        }

        // Can't break if we've already on broke,
        else if (mBreakTime.isStarted()) {
            retval = false;
        }

        // Can't lunch again - not with *that* attitude... wait 'til i get my admin powers...
        else if (mLunchTime.isActive()) {
            retval = false;
        }

        return retval;
    }

    boolean canEndBreak() {

        boolean onBreak = mBreakTime.isActive();
        return onBreak;
    }

    public void startBreak() throws IllegalStateException {
        mTotalTime.start();
    }

    public void endBreak() throws IllegalStateException {
        mTotalTime.end();
    }

    public long breakSeconds() {
        long retval = mBreakTime.elapsedSeconds();
        return retval;
    }

    boolean canStartLunch() {
        boolean retval = true;

        // Can't start lunch if we're not active
        if (!mTotalTime.isActive()) {
            retval = false;
        }

        // Can't lunch if we're on break
        else if (mBreakTime.isActive()) {
            retval = false;
        }

        // Can't lunch again - not with *that* attitude... wait 'til i get my admin powers...
        else if (mLunchTime.isStarted()) {
            retval = false;
        }

        return retval;
    }

    boolean canEndLunch() {
        boolean outToLunch = mLunchTime.isActive();
        return outToLunch;
    }

    public void startLunch() throws IllegalStateException {
        mTotalTime.start();
    }

    public void endLunch() throws IllegalStateException {
        mTotalTime.end();
    }

    public long lunchSeconds() {
        long retval = mBreakTime.elapsedSeconds();
        return retval;
    }
}
