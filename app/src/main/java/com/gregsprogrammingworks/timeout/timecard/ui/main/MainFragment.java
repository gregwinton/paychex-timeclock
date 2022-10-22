package com.gregsprogrammingworks.timeout.timecard.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregsprogrammingworks.timeout.timecard.R;
import com.gregsprogrammingworks.timeout.timecard.model.Employee;

public class MainFragment extends Fragment {

    private static final String TAG = Fragment.class.getSimpleName();

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testEmployees();
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void testEmployees() {
        Employee employee;
        employee = maybeMakeEmployee("ABC1234", "    ");
        employee = maybeMakeEmployee("DEF 678", "Banana");
        employee = maybeMakeEmployee("WINT00007", "Greg Winton");
        employee = maybeMakeEmployee("QUIN00003", "Alejandro Quintana");
        employee = maybeMakeEmployee("PANZ00004", "Sancho Panzes");
    }

    private Employee maybeMakeEmployee(String id, String name) {
        Employee employee = null;
        try {
            employee = new Employee(id, name);
        }
        catch (IllegalArgumentException ex) {
            Log.e(TAG, "Exception thrown creating Employee: " + ex.getLocalizedMessage(), ex);
        }
        return employee;
    }
}