/*                                                   WorkShiftFragment.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Fragment presenting a list of work shifts for an employee
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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.Employee;
import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.viewmodel.EmployeeViewModel;
import com.gregsprogrammingworks.timeclock.viewmodel.WorkShiftViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkShiftListFragment extends Fragment {

    private static final String TAG = WorkShiftListFragment.class.getSimpleName();

    private final String mEmployeeId;

    private WorkShiftViewModel mShiftViewModel;
    private EmployeeViewModel mEmployeeViewModel;

    private ListView mShiftListView;
    private FloatingActionButton mAddShiftButton;

    public static WorkShiftListFragment newInstance(String employeeId) {
        return new WorkShiftListFragment(employeeId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftViewModel = new ViewModelProvider(this).get(WorkShiftViewModel.class);
        mShiftViewModel.start(getContext());

        mEmployeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_shift_list, container, false);
        setupViews(view);
        return view;
    }

    /**
     * Set up our bits of the fragment's view
     * for now, just the employee list view
     * @param view  Fragment "root" view
     */
    void setupViews(View view) {

        // Find the employee list view
        mShiftListView = view.findViewById(R.id.WorkShiftListView);

        // Set the employee list view's adapter from the live data
        ListAdapter listAdapter = makeWorkShiftListAdapter();
        mShiftListView.setAdapter(listAdapter);

        // Set the employee list view's item click listener
        AdapterView.OnItemClickListener shiftListViewOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the employee that was clicked
                WorkShiftListAdapter adapter = (WorkShiftListAdapter) mShiftListView.getAdapter();
                WorkShift workShift = adapter.getItem(position);

                // TODO show their timesheet or shift card
                Fragment fragment = WorkShiftFragment.newInstance(workShift);
                gotoShiftFragment(fragment);
            }
        };
        mShiftListView.setOnItemClickListener(shiftListViewOnItemClickListener);

        View.OnClickListener addButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = WorkShiftFragment.newInstance(mEmployeeId);
                gotoShiftFragment(fragment);
            }
        };
        mAddShiftButton = view.findViewById(R.id.WorkShiftAddButton);
        mAddShiftButton.setOnClickListener(addButtonOnClickListener);
    }

    private void gotoShiftFragment(Fragment workShiftFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, workShiftFragment, "workShift");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private WorkShiftListFragment(String employeeId) {
        mEmployeeId = employeeId;
    }

    /**
     * Create a list adapter proxy to shift list live data
     * @return  list adapter
     * @// TODO: 10/23/22 Ponder moving this to ViewModel/Res
     */
    private ListAdapter makeWorkShiftListAdapter() {

        // Instantiate a simple list array adapter
        Context ctx = getContext();
        ListAdapter adapter = new WorkShiftListAdapter(mEmployeeId, ctx);

        return adapter;
    }
}
