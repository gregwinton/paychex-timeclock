package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

public class WorkShiftFragment extends Fragment {

    private static final String TAG = WorkShiftFragment.class.getSimpleName();

    private final String mEmployeeId;

    private WorkShiftViewModel mViewModel;
    private MutableLiveData<WorkShift> mWorkShiftLiveData;

    private Button mShiftButton;
    private Button mBreakButton;
    private Button mLunchButton;

    public static WorkShiftFragment newInstance(String employeeId) {
        return new WorkShiftFragment(employeeId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WorkShiftViewModel.class);
        mWorkShiftLiveData = mViewModel.activeWorkShiftFor(mEmployeeId);
        mWorkShiftLiveData.observe(this, mWorkShiftObserver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_shift, container, false);
        mShiftButton = view.findViewById(R.id.ShiftButton);
        mShiftButton.setOnClickListener(mShiftButtonClickListener);
        mBreakButton = view.findViewById(R.id.BreakButton);
        mBreakButton.setOnClickListener(mBreakButtonClickListener);
        mLunchButton = view.findViewById(R.id.LunchButton);
        mLunchButton.setOnClickListener(mLunchButtonClickListener);
        return view;
    }

    private WorkShiftFragment(String employeeId) {
        mEmployeeId = employeeId;
    }

    private View.OnClickListener mShiftButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkShift workShift = mWorkShiftLiveData.getValue();
            if (workShift.canStartShift()) {
                workShift.startShift();
                mWorkShiftLiveData.postValue(workShift);
            }
            else if (workShift.canEndShift()) {
                workShift.endShift();
                mWorkShiftLiveData.postValue(workShift);
            }
        }
    };

    private View.OnClickListener mBreakButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkShift workBreak = mWorkShiftLiveData.getValue();
            if (workBreak.canStartBreak()) {
                workBreak.startBreak();
                mWorkShiftLiveData.postValue(workBreak);
            }
            else if (workBreak.canEndBreak()) {
                workBreak.endBreak();
                mWorkShiftLiveData.postValue(workBreak);
            }
        }
    };

    private View.OnClickListener mLunchButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkShift workLunch = mWorkShiftLiveData.getValue();
            if (workLunch.canStartLunch()) {
                workLunch.startLunch();
                mWorkShiftLiveData.postValue(workLunch);
            }
            else if (workLunch.canEndLunch()) {
                workLunch.endLunch();
                mWorkShiftLiveData.postValue(workLunch);
            }
        }
    };

    private Observer<WorkShift> mWorkShiftObserver = new Observer<WorkShift>() {

        @Override
        public void onChanged(WorkShift workShift) {

            boolean enable = false;
            String label = "Shift";    // todo: move to strings.xml
            if (workShift.canStartShift()) {
                enable = true;
                label = "Start Shift"; // todo: move to strings.xml
            }
            else if (workShift.canEndShift()) {
                enable = true;
                label = "End Shift";   // todo: move to strings.xml
            }
            updateButton(mShiftButton, enable, label);

            enable = false;
            label = "Break";            // todo: move to strings.xml
            if (workShift.canStartBreak()) {
                enable = true;
                label = "Start Break"; // todo: move to strings.xml
            }
            else if (workShift.canEndBreak()) {
                enable = true;
                label = "End Break";   // todo: move to strings.xml
            }
            updateButton(mBreakButton, enable, label);

            enable = false;
            label = "Lunch";    // todo: move to strings.xml
            if (workShift.canStartLunch()) {
                enable = true;
                label = "Start Lunch"; // todo: move to strings.xml
            }
            else if (workShift.canEndLunch()) {
                enable = true;
                label = "End Lunch";   // todo: move to strings.xml
            }
            updateButton(mLunchButton, enable, label);
        }

        private void updateButton(Button button, boolean enable, String label) {
            // Get a handler that can be used to post to the main thread
            Handler mainHandler = new Handler(getContext().getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(enable);
                    button.setText(label);
                }
            };
            mainHandler.post(myRunnable);
        }
    };
}