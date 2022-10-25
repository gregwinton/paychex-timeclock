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

class EmployeeDataStore {

    private static final String TAG = EmployeeDataStore.class.getSimpleName();

    private static final String kStoreName = "employeeStore";

    private final SimpleDataStore mDataStore;

    EmployeeDataStore(Context context) {
        mDataStore = SimpleDataStore.storeNamed(context, kStoreName);
    }

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

    void save(Employee employee) {
        String json = employeeToJsonStr(employee);
        String key = employee.getUuid().toString();
        mDataStore.put(key, json);
    }

    Employee retrieve(UUID employeeUuid) throws IllegalArgumentException {

        Employee employee = null;

        String employeeKey = employeeUuid.toString();
        String employeeStr = mDataStore.get(employeeKey);
        if (null != employeeStr) {
            employee = employeeFromJsonStr(employeeStr);
        }
        return employee;
    }

    /* "When in doubt, use brute force."
     * -- Ken Thompson
     */
    private static final String kUuidKey = "uuid";
    private static final String kEmployeeNameKey  = "employee.name";

    private static String employeeToJsonStr(Employee employee) {
        Map<String,Object> employeeMap = new HashMap<String, Object>();
        employeeMap.put(kUuidKey, employee.getUuid());
        employeeMap.put(kEmployeeNameKey, employee.getName());

        JSONObject employeeJson = new JSONObject(employeeMap);

        String jsonStr = employeeJson.toString();
        return jsonStr;
    }

    private static Employee employeeFromJsonStr(String employeeStr) {
        Employee employee = null;

        try {
            JSONObject employeeJson = new JSONObject(employeeStr);
            String uuidStr = employeeJson.getString(kUuidKey);
            UUID uuid = UUID.fromString(uuidStr);
            String name = employeeJson.getString(kEmployeeNameKey);
            employee = new Employee(uuid, name);
        } catch (JSONException ex) {
            // Rethrow exception
            throw new IllegalArgumentException(TAG + ": Invalid Employee Json" + employeeStr, ex);
        }
        return employee;
    }
}
