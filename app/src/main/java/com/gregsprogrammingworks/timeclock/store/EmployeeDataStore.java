/*                                                   EmployeeDataStore.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Employee data store
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
import android.util.Log;

import com.gregsprogrammingworks.timeclock.common.SimpleDataStore;
import com.gregsprogrammingworks.timeclock.model.Employee;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Employee data store
 */
class EmployeeDataStore {

    /// Tag for logging
    private static final String TAG = EmployeeDataStore.class.getSimpleName();

    /// Data store name
    private static final String kStoreName = "employeeStore";

    /// Reference to (physical) data store
    private final SimpleDataStore mDataStore;

    /**
     * Constructor for class EmployeeDataStore
     * @param context
     */
    EmployeeDataStore(Context context) {
        mDataStore = SimpleDataStore.storeNamed(context, kStoreName);
    }

    /**
     * Retrieve all stored employees
     * @return all stored employees
     */
    List<Employee> retrieveAll() {
        Set<String> keySet = mDataStore.keySet();
        List<Employee> employeeList = new ArrayList<>(keySet.size());
        for (String key : keySet) {
            try {
                UUID uuid = UUID.fromString(key);
                Employee employee = this.retrieve(uuid);
                employeeList.add(employee);
            }
            catch (IllegalArgumentException ex) {
                // Log the error
                Log.w(TAG, "Exception thrown retrieving employee " + key);
                // try again
            }
        }
        return employeeList;
    }

    /**
     * Save an employee
     * @param employee employee to save
     */
    void save(Employee employee) {
        String json = employeeToJsonStr(employee);
        String key = employee.getUuid().toString();
        mDataStore.put(key, json);
    }

    /**
     * Retrieve employee from storage
     * @param employeeUuid  unique of employee to retrieve
     * @return retrieved employee
     * @throws IllegalArgumentException
     */
    Employee retrieve(UUID employeeUuid) throws IllegalArgumentException {

        Employee employee = null;

        String employeeKey = employeeUuid.toString();
        String employeeStr = mDataStore.get(employeeKey);
        if (null != employeeStr) {
            employee = employeeFromJsonStr(employeeStr);
        }
        return employee;
    }

    /// Key to employee uuid
    private static final String kUuidKey = "uuid";

    /// Key to employee name
    private static final String kEmployeeNameKey  = "employee.name";

    /**
     * Convert employee object to json string
     * @param employee employee to convert
     * @return employee represented as json string
     */
    private static String employeeToJsonStr(Employee employee) {

        // First, create a dictionary, er, map of the object
        Map<String,Object> employeeMap = new HashMap<String, Object>();
        employeeMap.put(kUuidKey, employee.getUuid());
        employeeMap.put(kEmployeeNameKey, employee.getName());

        // Create a json object from the dictionary
        JSONObject employeeJson = new JSONObject(employeeMap);

        // Serialize json object to string and return
        String jsonStr = employeeJson.toString();
        return jsonStr;
    }

    /**
     * Convert json string to employee object
     * @param employeeStr json string to convert
     * @return employee object
     */
    private static Employee employeeFromJsonStr(String employeeStr) {
        Employee employee = null;

        try {
            JSONObject employeeJson = new JSONObject(employeeStr);
            String uuidStr = employeeJson.getString(kUuidKey);
            UUID uuid = UUID.fromString(uuidStr);
            String name = employeeJson.getString(kEmployeeNameKey);
            employee = new Employee(uuid, name);
        }
        catch (JSONException ex) {
            // Rethrow exception
            throw new IllegalArgumentException(TAG + ": Invalid Employee Json" + employeeStr, ex);
        }
        return employee;
    }
}
