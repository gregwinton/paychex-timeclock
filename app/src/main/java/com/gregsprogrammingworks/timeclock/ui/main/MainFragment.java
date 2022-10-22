package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.Employee;

import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private EmployeesViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(EmployeesViewModel.class);
        MutableLiveData<List<Employee>> employeeLiveData = mViewModel.getEmployees();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}