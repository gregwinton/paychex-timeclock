package com.gregsprogrammingworks.timeclock.store;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeStore {

    private static final String TAG = EmployeeStore.class.getSimpleName();

    public MutableLiveData<List<Employee>> requestEmployees() {

        List<Employee> employees = new ArrayList<Employee>();
        maybeAddEmployee(employees, "ABC1234", "    ");
        maybeAddEmployee(employees, "DEF 678", "Banana");
        maybeAddEmployee(employees, "WINT00007", "Greg Winton");
        maybeAddEmployee(employees, "QUIN00003", "Alejandro Quintana");
        maybeAddEmployee(employees, "PANZ00004", "Sancho Panzes");

        final MutableLiveData<List<Employee>> employeesData = new MutableLiveData<>(employees);
        return employeesData;
    }

    private void maybeAddEmployee(List<Employee> list, String id, String name) {
        try {
            Employee employee = new Employee(id, name);
            list.add(employee);
        }
        catch (IllegalArgumentException ex) {
            Log.e(TAG, "Exception thrown creating Employee: " + ex.getLocalizedMessage(), ex);
        }
    }
}
