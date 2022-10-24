package com.gregsprogrammingworks.timeclock.store;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class WorkShiftStore {

    private static final String TAG = WorkShiftStore.class.getSimpleName();

    private Dictionary<String,MutableLiveData<WorkShift>> mOpenWorkShifts = new Hashtable<>();
    private Dictionary<String,MutableLiveData<List<WorkShift>>> mWorkShiftsByEmployee = new Hashtable<>();

    private static WorkShiftStore sInstance = null;

    public static WorkShiftStore getInstance() {
        if (null == sInstance) {
            sInstance = new WorkShiftStore();
        }
        return sInstance;
    }

    public int getOpenWorkShiftCount() {
        int retval = mOpenWorkShifts.size();
        return retval;
    }

    public void signalOpenWorkShifts() {
        Enumeration<MutableLiveData<WorkShift>> keyEnum = mOpenWorkShifts.elements();
        while (keyEnum.hasMoreElements()) {
            MutableLiveData<WorkShift> liveData = keyEnum.nextElement();
            WorkShift shift = liveData.getValue();
            liveData.postValue(shift);
        }
    }

    public void addWorkShift(WorkShift workShift) {
        String employeeId = workShift.getEmployeeId();
        MutableLiveData<List<WorkShift>> liveData = shiftListLiveDataFor(employeeId);
        List<WorkShift> shiftList = liveData.getValue();
        shiftList.add(workShift);
        liveData.postValue(shiftList);
        mOpenWorkShifts.remove(workShift.getEmployeeId());
    }

    public MutableLiveData<WorkShift> openWorkShiftFor(String employeeId) {
        MutableLiveData<WorkShift> mutableLiveData = mOpenWorkShifts.get(employeeId);
        if (null == mutableLiveData) {
            WorkShift workShift = new WorkShift(employeeId);
            mutableLiveData = new MutableLiveData<>(workShift);
            mOpenWorkShifts.put(employeeId, mutableLiveData);
        }
        return mutableLiveData;
    }

    public MutableLiveData<List<WorkShift>> getWorkShiftsFor(String employeeId) {
        MutableLiveData<List<WorkShift>> retval = shiftListLiveDataFor(employeeId);
        return retval;
    }

    private MutableLiveData<List<WorkShift>> shiftListLiveDataFor(String employeeId) {
        MutableLiveData<List<WorkShift>> liveData = mWorkShiftsByEmployee.get(employeeId);
        if (null == liveData) {
            liveData = requestWorkShifts(employeeId);
            mWorkShiftsByEmployee.put(employeeId, liveData);
        }
        return liveData;
    }

    private MutableLiveData<List<WorkShift>> requestWorkShifts(String employeeId) {

        List<WorkShift> worksheetList = new ArrayList<>();

//        maybeAddWorkShift(worksheetList, employeeId, 5, 30, 45);
//        maybeAddWorkShift(worksheetList, employeeId, 4, 0, 60);
//        maybeAddWorkShift(worksheetList, employeeId, 3, 45, 0);
//        maybeAddWorkShift(worksheetList, employeeId, 2, 40, 60);
//        maybeAddWorkShift(worksheetList, employeeId, 1, 20, 90);

        final MutableLiveData<List<WorkShift>>
                worksheetListData = new MutableLiveData<>(worksheetList);

        return worksheetListData;
    }

    private void maybeAddWorkShift(List<WorkShift> workShiftList,
                                  String employeeId,
                                  int daysAgo,
                                  int breakMinutes,
                                  int lunchMinutes) {
        TimeSlice shiftSlice, breakSlice, lunchSlice;

        final long delta = daysAgo * 24 /*hours*/ * 60 /*minutes*/ * 60 /*seconds*/ * 1000 /*millis*/;
        final Date now = new Date();
        long millis = now.getTime() - delta;

        final Date shiftStart = new Date(millis);

        // Skip two hours
        millis += 2 * 60/*minutes*/ * 60/*seconds*/ * 1000/*millis*/;

        // Check for lunch
        if (0 < breakMinutes) {
            final long breakMillis = breakMinutes * 60/*seconds*/ * 1000/*millis*/;
            final Date breakStart = new Date(millis);
            millis += breakMillis;
            final Date breakEnd = new Date(millis);
            breakSlice = new TimeSlice(breakStart, breakEnd);
        }
        else {
            breakSlice = new TimeSlice();
        }

        // Skip two hours
        millis += 2 * 60/*minutes*/ * 60/*seconds*/ * 1000/*millis*/;

        // Check for lunch
        if (0 < lunchMinutes) {
            final long lunchMillis = lunchMinutes * 60/*seconds*/ * 1000/*millis*/;
            final Date lunchStart = new Date(millis);
            millis += lunchMillis;
            final Date lunchEnd = new Date(millis);
            lunchSlice = new TimeSlice(lunchStart, lunchEnd);
        }
        else {
            lunchSlice = new TimeSlice();
        }

        // Skip four hours
        millis += 2 * 60/*minutes*/ * 60/*seconds*/ * 1000/*millis*/;
        final Date shiftEnd = new Date(millis);
        shiftSlice = new TimeSlice(shiftStart, shiftEnd);
        final UUID uuid = UUID.randomUUID();
        final WorkShift workShift = new WorkShift(uuid, employeeId, shiftSlice, breakSlice, lunchSlice);
        workShiftList.add(workShift);
    }
}
