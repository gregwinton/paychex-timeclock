package com.gregsprogrammingworks.timeclock.ui.main;

import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

public class WorkShiftTimer {

    // Singleton
    private static WorkShiftTimer sInstance;

    // Cache the work shift store
    private WorkShiftStore mWorkShiftStore = WorkShiftStore.getInstance();
    private Thread mTimerThread = null;

    static public void maybeStartThread() {
        if (null == sInstance) {
            sInstance = new WorkShiftTimer();
            sInstance.startTimer();
        }
    }

    private WorkShiftTimer() {
    }

    private void startTimer() {
        mTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean done = false;
//                while (!done && 0 < mWorkShiftStore.getOpenWorkShiftCount()) {
                while (!done) {
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
        mTimerThread.start();
    }
}
