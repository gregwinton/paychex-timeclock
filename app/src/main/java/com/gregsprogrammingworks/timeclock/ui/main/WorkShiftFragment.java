package com.gregsprogrammingworks.timeclock.ui.main;

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

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

public class WorkShiftFragment extends Fragment {

    private static final String TAG = WorkShiftFragment.class.getSimpleName();

    private final String mEmployeeId;

    private WorkShiftViewModel mViewModel;
    private MutableLiveData<WorkShift> mWorkShift;

    public static WorkShiftFragment newInstance(String employeeId) {
        return new WorkShiftFragment(employeeId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WorkShiftViewModel.class);
        mWorkShift = mViewModel.activeWorkShiftFor(mEmployeeId);
        mWorkShift.observe(this, mWorkShiftObserver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_shift, container, false);
    }

    private WorkShiftFragment(String employeeId) {
        mEmployeeId = employeeId;
    }

    private Observer<WorkShift> mWorkShiftObserver = new Observer<WorkShift>() {
        @Override
        public void onChanged(WorkShift workShift) {
            Log.d(TAG, "WorkShift for " + mEmployeeId + " has changed");
        }
    };
}