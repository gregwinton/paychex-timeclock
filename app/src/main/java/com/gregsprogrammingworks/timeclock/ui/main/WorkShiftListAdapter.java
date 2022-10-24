package com.gregsprogrammingworks.timeclock.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.R;
import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

import java.util.List;

public class WorkShiftListAdapter extends ArrayAdapter<WorkShift> {

    private final String mEmployeeId;

    public WorkShiftListAdapter(String employeeId, Context context) {
        super(context, R.layout.row_shift_list_item);
        mEmployeeId = employeeId;
        refresh();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View result;

        WorkShiftListItemRow row;
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_shift_list_item, parent, false);

            row = new WorkShiftListItemRow(convertView);

            result = convertView;

            convertView.setTag(row);
        }
        else {
            row = (WorkShiftListItemRow) convertView.getTag();
            result = convertView;
        }

        WorkShift workShift = getItem(position);
        row.setWorkShift(workShift);

        // Return the completed view to render on screen
        return result;
    }

    public void refresh() {
        MutableLiveData<List<WorkShift>> shiftListLiveData = WorkShiftStore.getInstance().getWorkShiftsFor(mEmployeeId);
        List<WorkShift> shiftList = shiftListLiveData.getValue();
        clear();
        addAll(shiftList);
        notifyDataSetChanged();
    }
}
