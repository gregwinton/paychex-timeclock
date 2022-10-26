/*                                                           WorkShift.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  WorkShift model (POJO) class, provides sophisticated, granular
 *  interface to various Work Shift properties, some recorded others
 *  calculated.
 *  A work shift has a start and stop time, and may have one break period
 *  and one lunch period.
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
package com.gregsprogrammingworks.timeclock.model;

// project imports
import com.gregsprogrammingworks.timeclock.common.TimeSlice;

import java.util.Objects;
import java.util.UUID;

/**
 * WorkShift model class
 */
public class WorkShift extends BaseModel {

    /// Tag for logging
    private static final String TAG = WorkShift.class.getSimpleName();

    /// Id of employee working shift
    private final String mEmployeeId;

    /// Shift start/stop times, if any
    private final TimeSlice mShiftTimeSlice;

    /// Break start/stop times, if any
    private final TimeSlice mBreakTimeSlice;

    /// Lunch start/stop times, if any
    private final TimeSlice mLunchTimeSlice;

    /**
     * Constructor creates an unstarted work shift for the employee
     * @param employeeId    Employee for whom to create work shift
     */
    public WorkShift(String employeeId) {
        this(UUID.randomUUID(),
                employeeId,
                new TimeSlice(),
                new TimeSlice(),
                new TimeSlice());
    }

    /**
     * Constructor creates a work shift from existing data
     * @param employeeId    Employee for whom to create work shift
     * @param shiftSlice    Overall shift start, stop date/times
     * @param breakSlice    Break start, stop date/times, if any
     * @param lunchSlice    Lunch start, stop date/times, if any
     */
    public WorkShift(UUID uuid,
                     String employeeId,
                     TimeSlice shiftSlice,
                     TimeSlice breakSlice,
                     TimeSlice lunchSlice) {
        super(uuid);
        mEmployeeId = employeeId;
        mShiftTimeSlice = shiftSlice;
        mBreakTimeSlice = breakSlice;
        mLunchTimeSlice = lunchSlice;
    }

    /// Get accessor for employee id
    public String getEmployeeId() {
        return mEmployeeId;
    }

    /**
     * Calculate the "net" elapsed time - shift time less breaks.
     * @return  net elapsed time
     */
    public long onTheClockSeconds() {
        long retval = shiftSeconds();
        retval -= breakSeconds();
        retval -= lunchSeconds();
        return retval;
    }

    /// Get accessor shift elapsed time, in seconds. Does not consider breaks.
    public long shiftSeconds() {
        long retval = mShiftTimeSlice.elapsedSeconds();
        return retval;
    }

    /**
     *  Can the shift can be started
     *  @return true if not started, or false if active or complete
     */
    public boolean canStartShift() {
        // Really only care if it's been started
        boolean retval = !mShiftTimeSlice.isStarted();
        return retval;
    }

    /**
     *  Can the shift can be ended
     *  @return true if started and not complete and neither break nor lunch is active, or false
     */
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

        // Return result
        return retval;
    }

    /**
     * Start the shift
     * @throws IllegalStateException    shift already started
     */
    public void startShift() throws IllegalStateException {
        mShiftTimeSlice.start();
    }

    /**
     *  End the shift
     *  @throws IllegalStateException   shift is not active
     */
    public void endShift() throws IllegalStateException {
        mShiftTimeSlice.end();
    }

    /// Get accessor for shift time slice
    public TimeSlice getShiftTimeSlice() {
        return mShiftTimeSlice;
    }

    /**
     *  Can a break can be started
     *  @return true if shift is active, lunch is not active, and break not started; or false
     */
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

        // Return result
        return retval;
    }

    /**
     *  Can a break can be ended
     *  @return true break is active, or false
     */
    public boolean canEndBreak() {

        boolean onBreak = mBreakTimeSlice.isActive();
        return onBreak;
    }

    /**
     * Start the break
     * @throws IllegalStateException    Shift not active, lunch is active, or break is active
     */
    public void startBreak() throws IllegalStateException {
        mBreakTimeSlice.start();
    }

    /**
     * End the break
     * @throws IllegalStateException    Break is not started, or already complete
     */
    public void endBreak() throws IllegalStateException {
        mBreakTimeSlice.end();
    }

    /// Get accessor for break time slice
    public TimeSlice getBreakTimeSlice() {
        return mBreakTimeSlice;
    }

    /// Get accessor for break elapsed time, in seconds
    public long breakSeconds() {
        long retval = mBreakTimeSlice.elapsedSeconds();
        return retval;
    }

    /**
     *  Can lunch can be started
     *  @return true if shift is active, break is not active, and lunch not started; or false
     */
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

    /**
     *  Can a lunch can be ended
     *  @return true lunch is active, or false
     */
    public boolean canEndLunch() {
        boolean outToLunch = mLunchTimeSlice.isActive();
        return outToLunch;
    }

    /**
     * Start the lunch
     * @throws IllegalStateException    Shift not active, break is active, or lunch is active
     */
    public void startLunch() throws IllegalStateException {
        mLunchTimeSlice.start();
    }

    /**
     * End the lunch
     * @throws IllegalStateException    Lunch is active
     */
    public void endLunch() throws IllegalStateException {
        mLunchTimeSlice.end();
    }

    /// Get accessor for lunch time slice
    public TimeSlice getLunchTimeSlice() {
        return mLunchTimeSlice;
    }

    /// Get accessor for lunch elapsed time, in seconds
    public long lunchSeconds() {
        long retval = mLunchTimeSlice.elapsedSeconds();
        return retval;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getUuid(),
                mEmployeeId, mShiftTimeSlice,
                mBreakTimeSlice, mLunchTimeSlice);

        return hash;
    }
}
