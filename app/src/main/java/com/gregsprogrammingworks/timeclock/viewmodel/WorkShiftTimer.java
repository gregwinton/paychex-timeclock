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

import androidx.lifecycle.MutableLiveData;

import com.gregsprogrammingworks.timeclock.store.WorkShiftStore;

/**
 * Timer drives updates of active work shifts
 */
public class WorkShiftTimer {

    /// Singleton instance - only one timer at a time
    private static WorkShiftTimer sInstance;

    private final WorkShiftStore mStore;

    /// Timer thread
    private Thread mTimerThread = null;

    /**
     * Factory method - starts the timer thread if not already started
     */
    static public void maybeStartThread(Context context) {
        if (null == sInstance) {
            sInstance = new WorkShiftTimer(context);
            sInstance.startTimer();
        }
    }

    private WorkShiftTimer(Context context) {
        mStore = new WorkShiftStore(context);
    }

    /**
     * Start the timer running
     */
    private void startTimer() {
        mTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean done = false;
                while (!done) {
                    try {
                        // Signal any open shifts
                        mStore.signalOpenWorkShifts();

                        // Wait a second, then do it again
                        Thread.sleep(1000);
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
