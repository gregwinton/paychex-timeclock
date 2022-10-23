package com.gregsprogrammingworks.common;

import java.util.Date;

public class TimeSlice {

    private static final String TAG = TimeSlice.class.getSimpleName();

    private static final Date kNilDate = new Date(0);

    Date mStartDate = kNilDate;
    Date mEndDate = kNilDate;

    public static boolean isNilDate(Date date) {
        return kNilDate.equals(date);
    }

    public TimeSlice() {
    }

    protected TimeSlice(TimeSlice that) {
        mStartDate = that.mStartDate;
        mEndDate = that.mEndDate;
    }

    public long elapsedSeconds() {
        long elapsedSeconds = 0;
        if (isStarted())
        {
            Date endDate = ! kNilDate.equals(mEndDate) ? mEndDate : new Date();
            long startMillis = mStartDate.getTime();
            long endMillis = endDate.getTime();
            long elapsedMillis = endMillis - startMillis;
            elapsedSeconds = elapsedMillis / 1000;
        }
        return elapsedSeconds;
    }

    public boolean isActive() {
        boolean retval = false;
        if (isStarted()) {
            if (! isComplete()) {
                retval = true;
            }
        }
        return retval;
    }

    public boolean isStarted() {
        Date start = getStart();
        boolean isStarted = !isNilDate(start);
        return isStarted;
    }

    public boolean isComplete() {
        Date end = getEnd();
        boolean isEnded = !isNilDate(end);
        return isEnded;
    }

    public void start() throws IllegalStateException {

        if (isStarted()) {
            throw new IllegalStateException(TAG + ": time slice already started");
        }

        mStartDate = new Date();
    }

    public void end() throws IllegalStateException {
        if (! isActive()) {
            throw new IllegalStateException(TAG + ": time slice already started");
        }

        mEndDate = new Date();
    }

    public Date getStart() {
        return mStartDate;
    }

    public Date getEnd() {
        return mEndDate;
    }
}
