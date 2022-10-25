/*                                                       EmployeeStore.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Provides storage and retrieval for Employee data.
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

// language, os imports
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

// project imports
import com.gregsprogrammingworks.timeclock.model.Employee;

/**
 * Provides storage and retrieval for Employee data.
 * NB: Currently does not persist across sessions.
 */
public class EmployeeStore {

    /// Tag for logging
    private static final String TAG = EmployeeStore.class.getSimpleName();

    private EmployeeDataStore mDataStore;
    private MutableLiveData<List<Employee>> mEmployeeListData;

    public EmployeeStore(Context context) {
        super();
        mDataStore = new EmployeeDataStore(context);
        mEmployeeListData = new MutableLiveData<>();
    }

    /**
     * Request employees from store
     * @return  employees from store
     * @// TODO: 10/24/22 consider removing MutableLiveData template from return value
     */
    public MutableLiveData<List<Employee>> refreshEmployees() {

        // Get the employees
        List<Employee> employeeList = mDataStore.retrieveAll();

        // Create a mutable live data around the list and return.
        mEmployeeListData.setValue(employeeList);

        return mEmployeeListData;
    }

    /**
     * Add or update employee in the store
     * @param employee Employee to save
     */
    public void saveEmployee(Employee employee) {
        mDataStore.save(employee);
        refreshEmployees();
    }
}
