/*                                                EmployeeListFragment.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Fragment presenting a list of employees
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
package com.gregsprogrammingworks.timeclock.ui.main;

// language, os, platform headers
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// project imports
import com.gregsprogrammingworks.timeclock.viewmodel.EmployeeViewModel;
import com.gregsprogrammingworks.timeclock.model.Employee;
import com.gregsprogrammingworks.timeclock.R;

/**
 *  Fragment presenting a list of employees
 */
public class EmployeeListFragment extends Fragment {

    /// TAG for logging
    private static final String TAG = EmployeeListFragment.class.getSimpleName();

    /// Employee view model
    private EmployeeViewModel mEmployeeViewModel;

    /// Live Data list of employees
    /// @// TODO: 10/24/22 Ponder whether MutableLiveData template is necessary
    private MutableLiveData<List<Employee>> mEmployeeListLiveData;

    /// ListView presenting list of employees
    private ListView mEmployeeListView;

    /**
     * Factory method creates a new EmployeeListFragment instance
     * @return  new EmployeeListFragment instance
     */
    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cache some data
        mEmployeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        mEmployeeViewModel.start(getContext());
    }

    @Override @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the resource into a view
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);

        // Initialize our bits
        setupViews(view);

        // Return result
        return view;
    }

    /**
     * Set up our bits of the fragment's view
     * for now, just the employee list view
     * @param view  Fragment "root" view
     */
    void setupViews(View view) {

        // Find the employee list view
        mEmployeeListView = view.findViewById(R.id.employeeListView);

        // Set the employee list view's adapter from the live data
        ListAdapter employeeListAdapter = makeEmployeeListAdapter();
        mEmployeeListView.setAdapter(employeeListAdapter);

        // Set the employee list view's item click listener
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the employee that was clicked on
                Employee employee = mEmployeeListLiveData.getValue().get(position);

                // Want to show (employee) work shift list fragment
                Fragment employeeFragment = WorkShiftListFragment.newInstance(employee.getEmployeeId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, employeeFragment, "workShiftList");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        mEmployeeListView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * Create a list adapter proxy to employee list live data
     * @return  list adapter
     * @// TODO: 10/22/22 Ponder moving this to ViewModel/Res
     */
    ListAdapter makeEmployeeListAdapter() {

        // TODO: Add sort options - by name, by id, by total?

        /* NB:  This may well be a case of premature optimization - i'm not sure how clever Java
         *      compiler optimizers are these days - but I hate repeating the same function call
         *      in a loop unless I know *for sure* it will be inlined (as in C++). That said, i
         *      haven't followed the latest news on java compilers, i just expect them to do their
         *      best. (gregw, 2022.10.22)
         */
        if (null == mEmployeeListLiveData) {
            mEmployeeListLiveData = mEmployeeViewModel.getEmployees();
        }

        // Get the list of employees from the live data.
        List<Employee> employeeList = mEmployeeListLiveData.getValue();

        // Create an empty array that will be filled with employees and passed to the adapter
        List<String> adapterList = new ArrayList<>();

        // Traverse the employee list, add entry "${name} (${id})" to  the employee rows array
        for (Employee employee : employeeList) {
            String employeeRow = employee.getName();
            adapterList.add(employeeRow);
        }

        // Instantiate a simple list array adapter
        ArrayAdapter<String> employeeListAdapter = new ArrayAdapter<>(
                EmployeeListFragment.this.getContext(),
                android.R.layout.simple_list_item_1,
                adapterList);

        // Return result
        return employeeListAdapter;
    }
}