package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gregsprogrammingworks.timeclock.model.WorkShift;
import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

/// @// TODO: 10/22/22 use xml & notations to wire this auto-magically, as it should be
/// @// TODO: 10/22/22 also, use live data objects more judiciously. Much more judiciously.
 public class WorkShiftViewModel extends ViewModel {

     // Cache the work shift store
     private WorkShiftStore mWorkShiftStore = WorkShiftStore.getInstance();

     public MutableLiveData<WorkShift> openWorkShiftFor(String employeeId) {
         MutableLiveData<WorkShift> retval = mWorkShiftStore.openWorkShiftFor(employeeId);
         maybeStartThread();
         return retval;
     }

     Thread mTimerThread = null;

     private void maybeStartThread() {
         if (null != mTimerThread) {
             return;
         }

         mTimerThread = new Thread(new Runnable() {
             @Override
             public void run() {
                 boolean done = false;
                 while (!done && 0 < mWorkShiftStore.getOpenWorkShiftCount()) {

                    try {
                        mWorkShiftStore.signalOpenWorkShifts();
                        Thread.sleep(500);
                    }
                    catch (InterruptedException ex) {
                        done = true;
                   }
                 }
                 mTimerThread = null;
             }
         });
     }
}