package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListFragment extends Fragment {

    private static final String TAG = EmployeeListFragment.class.getSimpleName();

    private EmployeeViewModel mEmployeeViewModel;
    private MutableLiveData<List<Employee>> mEmployeeLiveData;

    private ViewHolder mViewHolder;

    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEmployeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        mEmployeeLiveData = mEmployeeViewModel.getEmployees();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);

        mViewHolder = new ViewHolder(view);

        return view;
    }

    private class ViewHolder {
        final ListView employeeListView;

        ViewHolder(View view) {
            employeeListView = view.findViewById(R.id.employeeListView);

            ListAdapter employeeListAdapter = getEmployeeListAdapter();
            employeeListView.setAdapter(employeeListAdapter);
            employeeListView.setOnItemClickListener(mEmployeeListOnItemClickListener);
        }

        ListAdapter getEmployeeListAdapter() {
            List<String> employeeRows = new ArrayList<>();

            List<Employee> employeeList = mEmployeeLiveData.getValue();
            for (Employee employee : employeeList) {
                String employeeRow = employee.getName() + " (" + employee.getId() + ")";
                employeeRows.add(employeeRow);
            }

            ArrayAdapter<String> employeeListAdapter = new ArrayAdapter<String>(
                    EmployeeListFragment.this.getContext(),
                    android.R.layout.simple_list_item_1,
                    employeeRows);

            return employeeListAdapter;
        }

        private AdapterView.OnItemClickListener mEmployeeListOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Context ctx = EmployeeListFragment.this.getContext();
                Employee employee = mEmployeeLiveData.getValue().get(position);
                String toastStr = "Hello " + employee.getName() + "!";
                Toast toast = Toast.makeText(ctx, toastStr, Toast.LENGTH_LONG);
                toast.show();
            }
        };
    }
}