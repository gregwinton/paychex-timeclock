/*                                                      WorkShiftTimer.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *   Timer drives updates of active work shifts
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
package com.gregsprogrammingworks.timeclock.viewmodel;

// project imports
import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

import java.util.Date;

/**
 * Timer drives updates of active work shifts
 */
public class WorkShiftTimer {

    /// Singleton instance - only one timer at a time
    private static WorkShiftTimer sInstance;

    private final Context mContext;

    /// Timer thread
    private Thread mTimerThread = null;

    private MutableLiveData<Long> mTimeLiveData;

    /**
     * Factory method - starts the timer thread if not already started
     */
    static public MutableLiveData<Long> maybeStartThread(Context context) {
        if (null == sInstance) {
            sInstance = new WorkShiftTimer(context);
            sInstance.startTimer();
        }

        MutableLiveData<Long> liveData = sInstance.timeLiveData();
        return liveData;
    }

    private WorkShiftTimer(Context context) {
        mContext = context;
    }

    private MutableLiveData<Long> timeLiveData() {
        if (null == mTimeLiveData) {
            Long timeInSeconds = new Long(0);
            mTimeLiveData = new MutableLiveData<>(timeInSeconds);
        }

        return mTimeLiveData;
    }

    /**
     * Start the timer running
     */
    private void startTimer() {
        mTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {

                        // Signal any open shifts
                        long timeInSeconds = new Date().getTime() / 1000;
                        updateLiveData(timeInSeconds);

                        // Wait a second, then do it again
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ex) {
                        break;  // Exit while loop
                    }
                }
                mTimerThread = null;
            }
        });
        mTimerThread.start();
    }

    /**
     * Update the live data and notify observers
     * @param timeInSeconds new value for live data
     */
    private void updateLiveData(long timeInSeconds) {
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(mContext.getMainLooper());

        // Define the runnable to set label, enable
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                // Signal any open shifts
                mTimeLiveData.setValue(timeInSeconds);
            }
        };
        mainHandler.post(myRunnable);
    }
}
