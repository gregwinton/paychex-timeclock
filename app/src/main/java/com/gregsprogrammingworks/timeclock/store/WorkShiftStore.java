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

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkShiftStore {

    /// Tag for logging
    private static final String TAG = WorkShiftStore.class.getSimpleName();

    /// Execution/environment context
    private final Context mContext;

    /// Work shift "physical" data store
    private final WorkShiftDataStore mDataStore;

    /// Employee to active time sheets mapping
    private List<MutableLiveData<WorkShift>> mOpenWorkShifts;

    /// Lists of shifts per employee
    private Map<String, MutableLiveData<List<WorkShift>>> mWorkShiftsByEmployee;

    public WorkShiftStore(Context context) {
        mContext = context;
        mDataStore = new WorkShiftDataStore(mContext);
        reloadMaps();
    }

    /**
     * Add or update work shift in the store
     * @param workShift Work Shift to save
     */
    public void saveWorkShift(WorkShift workShift) throws IllegalStateException {
        // Save the workshift
        mDataStore.save(workShift);
        // Refresh
        reloadMaps();
    }

    /**
     * Get an open work shift for the employee, creating one if none are open
     * @param employeeId    employee for whom to create work shift
     * @return  Work shift for employee
     */
    public MutableLiveData<WorkShift> openWorkShiftFor(String employeeId) {

        /* NB:  This is super inefficient - consider adding logic to travers
         *      the map looking for open shifts? That doesn't actually, sound
         *      all that much more efficient. Really, we are getting close to
         *      a need for sql:
         *
         *          select *
         *          from workshifts
         *          where shiftStart != nil && shiftEnd == nil
         *
         *  But for now, this will have to do, big O of N as it is. (gregw, 2022.10.25)
         */

        // Get the work shift from open shift store
        for (MutableLiveData<WorkShift> data : mOpenWorkShifts) {
            // Get workShift - probably not null, but belt & suspenders
            WorkShift workShift = data.getValue();
            if (null != workShift) {
                // Not null - is it the one we want?
                if (employeeId.equals(workShift.getEmployeeId())) {
                    // Yep - return it
                    return data;
                }
            }
        }

        // If we're here, there's not one for the employee
        WorkShift workShift = new WorkShift(employeeId);
        MutableLiveData<WorkShift> shiftData = new MutableLiveData<>(workShift);
        mOpenWorkShifts.add(shiftData);

        return shiftData;
    }

    /**
     * Get all work shifts completed by an employee
     * @param employeeId    unique identifier of employee whose work shifts to get
     * @return  List of work shifts for the employee
     * @// TODO: 10/24/22 Ponder whether the MutableLiveData template is necessary
     */
    public MutableLiveData<List<WorkShift>> getWorkShiftsFor(String employeeId) {

        // Get what's there for the employee, if anything
        MutableLiveData<List<WorkShift>> shiftData = mWorkShiftsByEmployee.get(employeeId);
        if (null == shiftData) {
            List<WorkShift> shiftList = new ArrayList<>();
            shiftData = new MutableLiveData<>(shiftList);
            mWorkShiftsByEmployee.put(employeeId, shiftData);
        }
        return shiftData;
    }

    private void reloadMaps() {
        List<WorkShift> shiftList = mDataStore.retrieveAll();
        mWorkShiftsByEmployee = new HashMap<>();
        mOpenWorkShifts = new ArrayList<>();

        for (WorkShift shift : shiftList) {
            addShiftToMaps(shift);
        }
    }

    private void addShiftToMaps(WorkShift shift) {

        // Cache the employee id
        String employeeId = shift.getEmployeeId();

        // Add it to the employee list
        MutableLiveData<List<WorkShift>> employeeShiftsData = mWorkShiftsByEmployee.get(employeeId);
        if (null == employeeShiftsData) {
            employeeShiftsData = new MutableLiveData<>(new ArrayList<>());
            mWorkShiftsByEmployee.put(employeeId, employeeShiftsData);
        }

        List<WorkShift> shiftList = employeeShiftsData.getValue();
        shiftList.add(shift);
        employeeShiftsData.postValue(shiftList);

        // TODO: Make this a lot prettier... maybe Workshift.IsComplete()
        if (! shift.getShiftTimeSlice().isComplete()) {
            MutableLiveData<WorkShift> shiftData = new MutableLiveData<>(shift);
            mOpenWorkShifts.add(shiftData);
        }
    }

    private WorkShift getWorkShift(UUID uuid) throws IllegalArgumentException {
        WorkShift workShift = mDataStore.retrieve(uuid);
        return workShift;
    }

    private static TimeSlice sliceFor(long startMillis, long endMillis) {
        Date startDate = new Date(startMillis);
        Date endDate = new Date(endMillis);
        TimeSlice retval = new TimeSlice(startDate, endDate);
        return retval;
    }
}