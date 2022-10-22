package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListFragment extends Fragment {

    private static final String TAG = EmployeeListFragment.class.getSimpleName();

    private EmployeeViewModel mEmployeeViewModel;
    private MutableLiveData<List<Employee>> mEmployeeListLiveData;
    private ListView mEmployeeListView;

    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cache some data
        mEmployeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        mEmployeeListLiveData = mEmployeeViewModel.getEmployees();
    }

    @Nullable
    @Override
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
                // Cache the context. It just makes the code nicer
                Context ctx = EmployeeListFragment.this.getContext();

                // Get the employee that was clicked on
                Employee employee = mEmployeeListLiveData.getValue().get(position);

                // TODO show their timesheet or shift card
                Fragment workShiftFragment = WorkShiftFragment.newInstance(employee.getId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, workShiftFragment, "workShift");
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
        // Get the list of employees from the live data.
        List<Employee> employeeList = mEmployeeListLiveData.getValue();

        // Create an empty array that will be filled with employees and passed to the adapter
        List<String> adapterList = new ArrayList<>();

        // Traverse the employee list, add entry "${name} (${id})" to  the employee rows array
        for (Employee employee : employeeList) {
            String employeeRow = employee.getName() + " (" + employee.getId() + ")";
            adapterList.add(employeeRow);
        }

        // Instantiate a simple list array adapter
        ArrayAdapter<String> employeeListAdapter = new ArrayAdapter<String>(
                EmployeeListFragment.this.getContext(),
                android.R.layout.simple_list_item_1,
                adapterList);

        // Return result
        return employeeListAdapter;
    }
}