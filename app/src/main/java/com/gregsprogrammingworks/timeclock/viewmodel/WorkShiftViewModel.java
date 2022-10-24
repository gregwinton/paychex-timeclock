package com.gregsprogrammingworks.timeclock.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

import java.util.List;

/// @// TODO: 10/22/22 use xml & notations to wire this auto-magically, as it should be
/// @// TODO: 10/22/22 also, use live data objects more judiciously. Much more judiciously.
 public class WorkShiftViewModel extends ViewModel {

     // Cache the work shift store
     private WorkShiftStore mWorkShiftStore = WorkShiftStore.getInstance();

     public MutableLiveData<WorkShift> openWorkShiftFor(String employeeId) {
         MutableLiveData<WorkShift> retval = mWorkShiftStore.openWorkShiftFor(employeeId);
         return retval;
     }

     public MutableLiveData<List<WorkShift>> workShiftsFor(String employeeId) {
         MutableLiveData<List<WorkShift>> retval = mWorkShiftStore.getWorkShiftsFor(employeeId);
         return retval;
     }

     public void addWorkShift(WorkShift workShift) {
         mWorkShiftStore.addCompletedWorkShift(workShift);
     }

     public WorkShiftViewModel() {
         WorkShiftTimer.maybeStartThread();
     }
}