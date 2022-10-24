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
import android.widget.ListView;

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.viewmodel.WorkShiftViewModel;

///@// TODO: 10/22/22 Move some of this function to WorkShiftViewModel
public class WorkShiftFragment extends Fragment {

    private static final String TAG = WorkShiftFragment.class.getSimpleName();

    private final String mEmployeeId;

    private WorkShiftViewModel mWorkShiftViewModel;
    private MutableLiveData<WorkShift> mWorkShiftLiveData;

    private ButtonAssistant mShiftButtonAsst;
    private ButtonAssistant mBreakButtonAsst;
    private ButtonAssistant mLunchButtonAsst;
    private ListView mTimeSliceListView;

    public static WorkShiftFragment newInstance(String employeeId) {
        return new WorkShiftFragment(employeeId);
    }

    public static WorkShiftFragment newInstance(WorkShift shift) {
        return new WorkShiftFragment(shift);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWorkShiftViewModel = new ViewModelProvider(this).get(WorkShiftViewModel.class);
        if (null == mWorkShiftLiveData) {
            mWorkShiftLiveData = mWorkShiftViewModel.openWorkShiftFor(mEmployeeId);
            mWorkShiftLiveData.observe(this, mWorkShiftObserver);
        }
        else {
            // WorkShift passed to constructor - therefore already complete.
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_shift, container, false);

        mShiftButtonAsst = new ButtonAssistant(view, R.id.ShiftButton, mShiftButtonClickListener);
        mBreakButtonAsst = new ButtonAssistant(view, R.id.BreakButton, mBreakButtonClickListener);
        mLunchButtonAsst = new ButtonAssistant(view, R.id.LunchButton, mLunchButtonClickListener);
        mTimeSliceListView = view.findViewById(R.id.TimeSliceListView);

        WorkShiftTimeSliceAdapter adapter = new WorkShiftTimeSliceAdapter(mWorkShiftLiveData, getContext());
        mTimeSliceListView.setAdapter(adapter);

        return view;
    }

    private WorkShiftFragment(String employeeId) {
        mEmployeeId = employeeId;
    }

    private WorkShiftFragment(WorkShift workShift) {
        mEmployeeId = workShift.getEmployeeId();
        mWorkShiftLiveData = new MutableLiveData<>(workShift);
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
                mWorkShiftViewModel.addWorkShift(workShift);
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
            maybeUpdateShiftButton(workShift);
            maybeUpdateBreakButton(workShift);
            maybeUpdateLunchButton(workShift);
            maybeUpdateTimeSliceListView();
        }

        private void maybeUpdateShiftButton(WorkShift workShift) {
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
            mShiftButtonAsst.maybeUpdate(label, enable);
        }

        private void maybeUpdateBreakButton(WorkShift workShift) {
            boolean enable = false;
            String label = "Break";            // todo: move to strings.xml
            if (workShift.canStartBreak()) {
                enable = true;
                label = "Start Break"; // todo: move to strings.xml
            }
            else if (workShift.canEndBreak()) {
                enable = true;
                label = "End Break";   // todo: move to strings.xml
            }
            mBreakButtonAsst.maybeUpdate(label, enable);
        }

        private void maybeUpdateLunchButton(WorkShift workShift) {
            boolean enable = false;
            String label = "Lunch";    // todo: move to strings.xml
            if (workShift.canStartLunch()) {
                enable = true;
                label = "Start Lunch"; // todo: move to strings.xml
            }
            else if (workShift.canEndLunch()) {
                enable = true;
                label = "End Lunch";   // todo: move to strings.xml
            }
            mLunchButtonAsst.maybeUpdate(label, enable);
        }

        private void maybeUpdateTimeSliceListView() {
            WorkShiftTimeSliceAdapter adapter =
                    (WorkShiftTimeSliceAdapter) mTimeSliceListView.getAdapter();

            adapter.refresh();
        }
    };

    private class ButtonAssistant {
        private final Button mButton;

        public ButtonAssistant(View view, int resId, View.OnClickListener onClickListener) {
            mButton = view.findViewById(resId);
            mButton.setOnClickListener(onClickListener);
        }

        public void maybeUpdate(String label, boolean enable) {
            String btnLabel = mButton.toString();
            boolean btnEnabled = mButton.isEnabled();
            if (! btnLabel.equals(label) || (btnEnabled == enable)) {
                updateButton(label, enable);
            }
        }

        private void updateButton(String label, boolean enable) {
            // Get a handler that can be used to post to the main thread
            Handler mainHandler = new Handler(getContext().getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    mButton.setEnabled(enable);
                    mButton.setText(label);
                }
            };
            mainHandler.post(myRunnable);
        }
    }
}