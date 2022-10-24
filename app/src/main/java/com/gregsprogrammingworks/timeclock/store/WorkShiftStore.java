/*                                                      WorkShiftStore.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Provides storage and retrieval for WorkShift data.
 *  NB: Currently does not persist across sessions.
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
package com.gregsprogrammingworks.timeclock.store;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class WorkShiftStore {

    /// Tag for logging
    private static final String TAG = WorkShiftStore.class.getSimpleName();

    /// Singleton instance
    private static WorkShiftStore sInstance = null;

    /// Employee to active time sheets mapping
    /// @// TODO: 10/24/22 Consider removing this, and add active work shifts to store
    private Dictionary<String, MutableLiveData<WorkShift>> mOpenWorkShifts = new Hashtable<>();

    /**
     * Get the work shift store singleton instance
     * @return work shift store singleton
     */
    public static WorkShiftStore getInstance() {
        if (null == sInstance) {
            sInstance = new WorkShiftStore();
        }
        return sInstance;
    }

    /// Lists of shifts per employee
    private Dictionary<String, MutableLiveData<List<WorkShift>>> mWorkShiftsByEmployee = new Hashtable<>();

    /**
     * Notify active workshifts of a change
     * @// TODO: 10/24/22 ponder moving to WorkShiftViewModel
     */
    public void signalOpenWorkShifts() {
        Enumeration<MutableLiveData<WorkShift>> keyEnum = mOpenWorkShifts.elements();
        while (keyEnum.hasMoreElements()) {
            MutableLiveData<WorkShift> liveData = keyEnum.nextElement();
            WorkShift shift = liveData.getValue();
            liveData.postValue(shift);
        }
    }

    /**
     * Add a completed work shift to the store
     * @param workShift Work Shift to add.
     * @throws IllegalStateException    if the work shift is not complete
     * @// TODO: 10/24/22 Add unique id to WorkShift so it can be easily identified for update
     * @// TODO: 10/24/22 Add support for active worksheets
     */
    public void addCompletedWorkShift(WorkShift workShift) throws IllegalStateException {

        // throw exception if not complete
        if (! workShift.getShiftTimeSlice().isComplete()) {
            throw new IllegalStateException("Attempted to add incomplete work shift");
        }

        // Get the list of shifts for the employee
        String employeeId = workShift.getEmployeeId();
        MutableLiveData<List<WorkShift>> liveData = getWorkShiftsFor(employeeId);

        // Add the shift to the underlying list and post the value back
        List<WorkShift> shiftList = liveData.getValue();
        shiftList.add(workShift);
        liveData.postValue(shiftList);

        // Remove the work shift from the "open" set
        mOpenWorkShifts.remove(workShift.getEmployeeId());
    }

    /**
     * Get an open work shift for the employee, creating one if none are open
     * @param employeeId    employee for whom to create work shift
     * @return  Work shift for employee
     * @// TODO: 10/24/22 Ponder whether the MutableLiveData template is necessary
     */
    public MutableLiveData<WorkShift> openWorkShiftFor(String employeeId) {

        // Get the work shift from open shift store
        MutableLiveData<WorkShift> mutableLiveData = mOpenWorkShifts.get(employeeId);
        if (null == mutableLiveData) {
            // Not there... add it
            WorkShift workShift = new WorkShift(employeeId);
            mutableLiveData = new MutableLiveData<>(workShift);
            mOpenWorkShifts.put(employeeId, mutableLiveData);
        }
        return mutableLiveData;
    }

    /**
     * Get all work shifts completed by an employee
     * @param employeeId    unique identifier of employee whose work shifts to get
     * @return  List of work shifts for the employee
     * @// TODO: 10/24/22 Ponder whether the MutableLiveData template is necessary
     */
    public MutableLiveData<List<WorkShift>> getWorkShiftsFor(String employeeId) {

        // Get what's there for the employee, if anything
        MutableLiveData<List<WorkShift>> liveData = mWorkShiftsByEmployee.get(employeeId);
        if (null == liveData) {

            // Nothing - for now, create it
            // TODO: Retrieve from persistent store
            List<WorkShift> worksheetList = new ArrayList<>();
            liveData = new MutableLiveData<>(worksheetList);

            // Save it in the store
            mWorkShiftsByEmployee.put(employeeId, liveData);
        }
        return liveData;
    }
}