/*                                                   EmployeeViewModel.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *   View model for employee-related information
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

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// project imports
import com.gregsprogrammingworks.timeclock.model.Employee;
import com.gregsprogrammingworks.timeclock.store.EmployeeStore;

/**
 *  View model for employee-related information
 */
public class EmployeeViewModel extends ViewModel {

    /// Tag for logging
    private static final String TAG = ViewModel.class.getSimpleName();

    /// employee data store
    private EmployeeStore mEmployeeStore;

    /// live data list of employees
    private MutableLiveData<List<Employee>> mEmployeesLiveData;

    /**
     * Constructor - initializes employee data store
     */
    public EmployeeViewModel() {
    }

    public void start(Context context) {
        mEmployeeStore = new EmployeeStore(context);
    }

    /**
     * Get a list of all employees
     * @return  Live data list of all employees
     * @// TODO: 10/24/22 Is the MutableLiveData wrapper necessary and helpful?
     */
    public MutableLiveData<List<Employee>> getEmployees() {
        startedOrThrow();
        if (null == mEmployeesLiveData) {
            mEmployeesLiveData = mEmployeeStore.refreshEmployees();
        }
        return mEmployeesLiveData;
    }

    public void saveEmployee(Employee employee) {
        mEmployeeStore.saveEmployee(employee);
        mEmployeesLiveData = null;
    }

    private void startedOrThrow() {
        if (null == mEmployeeStore) {
            throw new IllegalStateException(TAG + ": view model not started");
        }
    }
}