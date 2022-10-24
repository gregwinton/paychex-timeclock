package com.gregsprogrammingworks.timeclock.ui.main;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.List;

public class WorkShiftListAdapter extends ArrayAdapter<WorkShift> {

    private final MutableLiveData<List<WorkShift>> mShiftListLiveData;

    public WorkShiftListAdapter(MutableLiveData<List<WorkShift>> shiftListData, Context context) {
        super(context, R.layout.row_shift_time_slice);
        mShiftListLiveData = shiftListData;
        refresh();
    }

    private void refresh() {
    }
}
