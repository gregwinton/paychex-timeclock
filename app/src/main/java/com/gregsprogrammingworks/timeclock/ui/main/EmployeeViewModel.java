package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gregsprogrammingworks.timeclock.model.Employee;
import com.gregsprogrammingworks.timeclock.model.EmployeeStore;

import java.util.List;

public class EmployeeViewModel extends ViewModel {

    private static final String TAG = ViewModel.class.getSimpleName();

    private EmployeeStore mEmployeeStore;
    private MutableLiveData<List<Employee>> mEmployeesLiveData;

    public EmployeeViewModel() {
        mEmployeeStore = new EmployeeStore();
    }

    public MutableLiveData<List<Employee>> getEmployees() {
        if (null == mEmployeesLiveData) {
            mEmployeesLiveData = mEmployeeStore.requestEmployees();
        }
        return mEmployeesLiveData;
    }
}