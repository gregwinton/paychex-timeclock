package com.gregsprogrammingworks.timeclock.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gregsprogrammingworks.timeclock.model.WorkShift;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/// @// TODO: 10/22/22 use xml & notations to wire this auto-magically, as it should be
/// @// TODO: 10/22/22 also, use live data objects more judiciously. Much more judiciously.
 public class WorkShiftViewModel extends ViewModel {

     private Dictionary<String,MutableLiveData<WorkShift>> mActiveWorkshifts = new Hashtable<>();

     public MutableLiveData<WorkShift> activeWorkShiftFor(String employeeId) {

         MutableLiveData<WorkShift> mutableLiveData = mActiveWorkshifts.get(employeeId);
         if (null == mutableLiveData) {
             WorkShift workShift = new WorkShift(employeeId);
             mutableLiveData = new MutableLiveData<>(workShift);
             mActiveWorkshifts.put(employeeId, mutableLiveData);
             maybeStartThread();
         }
         return mutableLiveData;
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
                 while (!done && !mActiveWorkshifts.isEmpty()) {
                    Enumeration<MutableLiveData<WorkShift>> keyEnum = mActiveWorkshifts.elements();
                    while (keyEnum.hasMoreElements()) {
                        MutableLiveData<WorkShift> liveData = keyEnum.nextElement();
                        if (liveData.hasActiveObservers()) {
                            liveData.notifyAll();
                        }
                    }
                    try {
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