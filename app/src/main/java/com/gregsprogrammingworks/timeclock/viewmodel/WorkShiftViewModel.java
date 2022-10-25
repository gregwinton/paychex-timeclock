/*                                                  WorkShiftViewModel.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *   View model for workshift-related information
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
package com.gregsprogrammingworks.timeclock.viewmodel;

// language, os, platform imports
import android.content.Context;

import java.util.List;

import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * View model for workshift-related information
 * @// TODO: 10/22/22 use xml & notations to wire this auto-magically, as it should be
 * @// TODO: 10/22/22 also, use live data objects more judiciously. Much more judiciously.
 */
public class WorkShiftViewModel extends ViewModel {

    /// tag for logging/exceptions
    private static final String TAG = WorkShiftViewModel.class.getSimpleName();

    /// Work shift store
    private WorkShiftStore mWorkShiftStore;

    /**
     * Constructor for work shift view model.
     */
    public WorkShiftViewModel() {
    }

    public void start(Context context) {
        mWorkShiftStore = new WorkShiftStore(context);
        // Start the work shift update thread
        WorkShiftTimer.maybeStartThread(context);
    }

    /**
     * Get an open work shift for the employee.
     * If the employee does not currently have an open work shift, it will be created
     * @param employeeId    employee whose open work shift to get
     * @return open work shift for specified employee
     * @// TODO: 10/24/22 Ponder combining openWorkShiftFor with workShiftsFor
     */
     public MutableLiveData<WorkShift> openWorkShiftFor(String employeeId) {
         startedOrThrow();

         MutableLiveData<WorkShift> retval = mWorkShiftStore.openWorkShiftFor(employeeId);
         return retval;
     }

    /**
     * Get a list of all completed work shifts for an employee
     * @param employeeId    employee whose completed work shift to get
     * @return open work shift for specified employee
     */
     public MutableLiveData<List<WorkShift>> workShiftsFor(String employeeId) {
         startedOrThrow();

         MutableLiveData<List<WorkShift>> retval = mWorkShiftStore.getWorkShiftsFor(employeeId);
         return retval;
     }

    /**
     * Add a (complete) work shift
     * @param workShift completed work shift
     */
     public void saveWorkShift(WorkShift workShift) {
         startedOrThrow();
         mWorkShiftStore.saveWorkShift(workShift);
     }

     private void startedOrThrow() {
         if (null == mWorkShiftStore) {
             throw new IllegalStateException(TAG + ": view model not started");
         }
     }
}