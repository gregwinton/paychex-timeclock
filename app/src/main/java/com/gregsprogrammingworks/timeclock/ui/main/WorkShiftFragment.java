/*                                                   WorkShiftFragment.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Fragment presenting a single work shift
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

// language, os, platform imports
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// project imports
import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.viewmodel.WorkShiftViewModel;

/**
 * presents a single work shift
 * @// TODO: 10/22/22 Move some of this function to WorkShiftViewModel
 */
///
public class WorkShiftFragment extends Fragment {

    /// Tag for logging
    private static final String TAG = WorkShiftFragment.class.getSimpleName();

    /**
     * Factory method creates fragment with a new work shift
     * @param employeeId    Employee for whom to open the work shift
     * @return  fragment for new employee work shift
     */
    public static WorkShiftFragment newInstance(String employeeId) {
        return new WorkShiftFragment(employeeId);
    }

    /**
     * Factory method creates fragment with an existing work work shift
     * @param workShift     existing work work shift to present
     * @return  fragment for existing work work shift
     */
    public static WorkShiftFragment newInstance(WorkShift workShift) {
        return new WorkShiftFragment(workShift);
    }

    /// id of employee whose work shift to present
    private final String mEmployeeId;

    /// view model for work shift information
    private WorkShiftViewModel mWorkShiftViewModel;

    /// work shift live data
    /// @// TODO: 10/24/22 Ponder whether LiveData wrapper is necessary
    private MutableLiveData<WorkShift> mWorkShiftLiveData;

    /// View containing shift, break, lunch buttons
    private View mShiftButtonsView;

    /// Button helper for shift button
    private ButtonAssistant mShiftButtonAsst;

    /// Button helper for break button
    private ButtonAssistant mBreakButtonAsst;

    /// Button helper for lunch button
    private ButtonAssistant mLunchButtonAsst;

    /// List of time slices in the work shift - shift, break, lunch
    private ListView mTimeSliceListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantisate the view model
        mWorkShiftViewModel = new ViewModelProvider(this).get(WorkShiftViewModel.class);
        mWorkShiftViewModel.start(getContext());
        if (null == mWorkShiftLiveData) {
            // That worked - get the data we'll want
            mWorkShiftLiveData = mWorkShiftViewModel.openWorkShiftFor(mEmployeeId);
        }

        // We'll want to observe the data
        // for now...
        mWorkShiftLiveData.observe(this, mWorkShiftObserver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the view
        View view = inflater.inflate(R.layout.fragment_work_shift, container, false);

        // Hook up the controls
        mShiftButtonsView = view.findViewById(R.id.WorkShiftButtonsView);
        mShiftButtonAsst = new ButtonAssistant(view, R.id.ShiftButton, mShiftButtonClickListener);
        mBreakButtonAsst = new ButtonAssistant(view, R.id.BreakButton, mBreakButtonClickListener);
        mLunchButtonAsst = new ButtonAssistant(view, R.id.LunchButton, mLunchButtonClickListener);
        mTimeSliceListView = view.findViewById(R.id.TimeSliceListView);

        // Set the adapter
        WorkShiftTimeSliceAdapter adapter = new WorkShiftTimeSliceAdapter(mWorkShiftLiveData, getContext());
        mTimeSliceListView.setAdapter(adapter);

        // Get the work shift and refresh the view with it
        WorkShift workShift = mWorkShiftLiveData.getValue();
        refresh(workShift);

        // Return result
        return view;
    }

    /**
     * Constructor creates fragment with a new work shift
     * @param employeeId    Employee for whom to open the work shift
     */
    private WorkShiftFragment(String employeeId) {
        mEmployeeId = employeeId;
    }

    /**
     * Constructor creates fragment with an existing work shift
     * @param workShift     existing work shift to present
     * @return  fragment for existing work shift
     */
    private WorkShiftFragment(WorkShift workShift) {
        mEmployeeId = workShift.getEmployeeId();
        mWorkShiftLiveData = new MutableLiveData<>(workShift);
    }

    /// on click listener for shift button
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
            mWorkShiftViewModel.saveWorkShift(workShift);
        }
    };

    /// on click listener for break button
    private View.OnClickListener mBreakButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkShift workShift = mWorkShiftLiveData.getValue();
            if (workShift.canStartBreak()) {
                workShift.startBreak();
                mWorkShiftLiveData.postValue(workShift);
            }
            else if (workShift.canEndBreak()) {
                workShift.endBreak();
                mWorkShiftLiveData.postValue(workShift);
            }
            mWorkShiftViewModel.saveWorkShift(workShift);
        }
    };

    /// on click listener for lunch button
    private View.OnClickListener mLunchButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkShift workShift = mWorkShiftLiveData.getValue();
            if (workShift.canStartLunch()) {
                workShift.startLunch();
                mWorkShiftLiveData.postValue(workShift);
            }
            else if (workShift.canEndLunch()) {
                workShift.endLunch();
                mWorkShiftLiveData.postValue(workShift);
            }
            mWorkShiftViewModel.saveWorkShift(workShift);
        }
    };

    /// observer for WorkShift live data
    /// @// TODO: 10/24/22 Is LiveData wrapper necessary and/or helpful
    private Observer<WorkShift> mWorkShiftObserver = new Observer<WorkShift>() {

        @Override
        public void onChanged(WorkShift workShift) {
            refresh(workShift);
        }
    };

    /**
     * Refresh the fragment's views from a work shift
     * @param workShift work shift to update in fragment
     */
    private void refresh(WorkShift workShift) {
        maybeUpdateShiftButton(workShift);
        maybeUpdateBreakButton(workShift);
        maybeUpdateLunchButton(workShift);
        maybeUpdateTimeSliceListView();
    }

    /**
     * Update the shift button label and/or enable flag based on work shift
     * @param workShift source of shift data for update
     */
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

    /**
     * Update the break button label and/or enable flag based on work shift
     * @param workShift source of break data for update
     */
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

    /**
     * Update the lunch button label and/or enable flag based on work shift
     * @param workShift source of break data for update
     */
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

    /**
     * Update the list of time slice entries: shift, break, lunch
     */
    private void maybeUpdateTimeSliceListView() {
        WorkShiftTimeSliceAdapter adapter =
                (WorkShiftTimeSliceAdapter) mTimeSliceListView.getAdapter();

        adapter.refresh();
    }

    /**
     * Button helper class, adds project-specific semantics around shift, break, lunch
     * buttons
     */
    private class ButtonAssistant {

        /// Button being assisted
        private final Button mButton;

        /**
         * Parameterized constructor
         * @param view  view containing button
         * @param resId identifier of button in view
         * @param onClickListener   on click listener object
         */
        public ButtonAssistant(View view, int resId, View.OnClickListener onClickListener) {
            mButton = view.findViewById(resId);
            mButton.setOnClickListener(onClickListener);
        }

        /**
         * Update the button if label or enable flag has changed
         * @param label     new label
         * @param enable    new enable flag
         */
        public void maybeUpdate(String label, boolean enable) {
            // Get the current values
            String btnLabel = mButton.toString();
            boolean btnEnabled = mButton.isEnabled();

            // See if anything has changed
            if (! btnLabel.equals(label) || (btnEnabled == enable)) {
                updateButton(label, enable);
            }
        }

        /**
         * Update button label, enable flag - on ui thread
         * @param label     new value for button label text
         * @param enable    new value for button enable flag
         */
        private void updateButton(String label, boolean enable) {
            // Get a handler that can be used to post to the main thread
            Handler mainHandler = new Handler(getContext().getMainLooper());

            // Define the runnable to set label, enable
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
