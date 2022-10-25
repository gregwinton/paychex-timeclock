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
import androidx.lifecycle.Observer;
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

    /// observer for Employee live data
    private Observer<List<Employee>> mEmployeeListObserver = new Observer<List<Employee>>() {

        @Override
        public void onChanged(List<Employee> employeeList) {
        }
    };

    /**
     * Constructor - initializes employee data store
     */
    public EmployeeViewModel() {
    }

    /**
     * Start the view model
     * @param context   Context in which view model executes
     */
    public void start(Context context) {
        mEmployeeStore = new EmployeeStore(context);
        mEmployeesLiveData = mEmployeeStore.refreshEmployees();
    }

    /**
     * Get a list of all employees
     * @return  Live data list of all employees
     * @// TODO: 10/24/22 Is the MutableLiveData wrapper necessary and helpful?
     */
    public MutableLiveData<List<Employee>> getEmployees() {
        startedOrThrow();
        return mEmployeesLiveData;
    }

    /**
     * Save a new employee into the system
     * @param employee  employee to save
     */
    public void saveEmployee(Employee employee) {
        mEmployeeStore.saveEmployee(employee);
    }

    /**
     * Verify that the view model has been started, or throw
     * @throws IllegalStateException    view model not started
     */
    private void startedOrThrow() throws IllegalStateException {
        if (null == mEmployeeStore) {
            throw new IllegalStateException(TAG + ": view model not started");
        }
    }
}