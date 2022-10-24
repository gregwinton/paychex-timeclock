package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
    private MutableLiveData<List<WorkShift>> mShiftListData;

    private ListView mShiftListView;
    private FloatingActionButton mAddShiftButton;

    public static WorkShiftListFragment newInstance(String employeeId) {
        return new WorkShiftListFragment(employeeId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftViewModel = new ViewModelProvider(this).get(WorkShiftViewModel.class);
        mShiftListData = mShiftViewModel.workShiftsFor(mEmployeeId);

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
        makeWorkShiftListAdapter();

        // Set the employee list view's item click listener
        AdapterView.OnItemClickListener shiftListViewOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the employee that was clicked on
                WorkShift workShift = mShiftListData.getValue().get(position);

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
    private void makeWorkShiftListAdapter() {

        // Get the list of employees from the live data.
        List<WorkShift> workShiftList = mShiftListData.getValue();

        // Instantiate a simple list array adapter
        ArrayAdapter<WorkShift> listAdapter = new ArrayAdapter<String>(
                WorkShiftListFragment.this.getContext(),
                R.layout.row_shift_list_item,
                workShiftList);

        // See if there's already an adapter...
        ListAdapter adapter = mShiftListView.getAdapter();
        if (null == adapter) {
            // TODO: Add sort options - by id, by date, by total?
            adapter = new WorkShiftListAdapter(mEmployeeId, getContext());
            mShiftListView.setAdapter(adapter);
        }
    }
}
