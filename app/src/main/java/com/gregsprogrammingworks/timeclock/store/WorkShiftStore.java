package com.gregsprogrammingworks.timeclock.store;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class WorkShiftStore {

    private Dictionary<String,MutableLiveData<WorkShift>> mOpenWorkShifts = new Hashtable<>();

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
            if (liveData.hasActiveObservers()) {
                liveData.notifyAll();
            }
        }
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
        // TODO: Implement this method
        return null;
    }
}
