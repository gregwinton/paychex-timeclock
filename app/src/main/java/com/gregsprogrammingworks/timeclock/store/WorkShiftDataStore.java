/*                                                  WorkShiftDataStore.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Employee data store
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
package com.gregsprogrammingworks.timeclock.store;

import android.content.Context;
import android.util.Log;

import com.gregsprogrammingworks.timeclock.common.SimpleDataStore;
import com.gregsprogrammingworks.timeclock.common.TimeSlice;
import com.gregsprogrammingworks.timeclock.model.WorkShift;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Provide storage of
 */
class WorkShiftDataStore {

    /// Tag for logging
    private static final String TAG = WorkShiftDataStore.class.getSimpleName();

    /// Store name
    private static final String kStoreName = "workShiftStore";

    /// Data store implementation
    private final SimpleDataStore mDataStore;

    /**
     * Constructor for class WorkShiftDataStore
     * @param context
     */
    WorkShiftDataStore(Context context) {
        mDataStore = SimpleDataStore.storeNamed(context, kStoreName);
    }

    /**
     * Retreive all work shift objects from the store
     * @return list of all work shift objects in the store
     */
    List<WorkShift> retrieveAll() {
        Set<String> keySet = mDataStore.keySet();
        List<WorkShift> shiftList = new ArrayList<>(keySet.size());
        for (String key : keySet) {
            try {
                UUID uuid = UUID.fromString(key);
                WorkShift workShift = this.retrieve(uuid);
                shiftList.add(workShift);
            }
            catch (IllegalArgumentException ex) {
                // Log the error
                Log.w(TAG, "Exception thrown retrieving workShift " + key);
                // try again
            }
        }
        return shiftList;
    }

    /**
     * Save the workshift - replace existing or add new
     * @param workshift workshift to save
     */
    void save(WorkShift workshift) {
        String jsonStr = shiftToJsonStr(workshift);
        String shiftKey = workshift.getUuid().toString();
        mDataStore.put(shiftKey, jsonStr);
    }

    /**
     *
     * @param shiftUuid
     * @return
     * @throws IllegalArgumentException
     */
    WorkShift retrieve(UUID shiftUuid) throws IllegalArgumentException {

        WorkShift workShift = null;

        String shiftKey = shiftUuid.toString();
        String shiftStr = mDataStore.get(shiftKey);
        if (null != shiftStr) {
            workShift = shiftFromJsonStr(shiftStr);
        }
        return workShift;
    }

    /* "When in doubt, use brute force."
     * -- Ken Thompson
     *
     * Relevant here because we are brute forcing objects to maps to json to strings
     * and back again. (gregw, 2022.10.25):w
     */

    /// Key to worksheet's unique id
    private static final String kUuidKey = "uuid";

    /// Key to employee id (also a uuid, coincidentally)
    private static final String kEmployeeIdKey  = "employeeId";

    /// Key to shift start date/time
    private static final String kShiftStartKey  = "shift.start";

    /// Key to shift end date/time
    private static final String kShiftEndKey    = "shift.end";

    /// Key to break start date/time
    private static final String kBreakStartKey  = "break.start";

    /// Key to shift end date/time
    private static final String kBreakEndKey    = "break.end";

    /// Key to lunch start date/time
    private static final String kLunchStartKey  = "lunch.start";

    /// Key to lunch end date/time
    private static final String kLunchEndKey    = "lunch.end";

    /**
     * Convert workshift object to json string
     * @param shift object to convert
     * @return workshift represented as a json string
     */
    private static String shiftToJsonStr(WorkShift shift) {

        // First, create a map of the object
        Map<String,Object> shiftMap = new HashMap<String, Object>();
        shiftMap.put(kUuidKey, shift.getUuid());
        shiftMap.put(kEmployeeIdKey, shift.getEmployeeId());
        shiftMap.put(kShiftStartKey, shift.getShiftTimeSlice().getStartDate().getTime());
        shiftMap.put(kShiftEndKey, shift.getShiftTimeSlice().getEndDate().getTime());
        shiftMap.put(kLunchStartKey, shift.getLunchTimeSlice().getStartDate().getTime());
        shiftMap.put(kLunchEndKey, shift.getLunchTimeSlice().getEndDate().getTime());
        shiftMap.put(kBreakStartKey, shift.getBreakTimeSlice().getStartDate().getTime());
        shiftMap.put(kBreakEndKey, shift.getBreakTimeSlice().getEndDate().getTime());

        // Create a json object from the dictionary
        JSONObject shiftJson = new JSONObject(shiftMap);

        // Serialize json object to string and return
        String jsonStr = shiftJson.toString();
        return jsonStr;
    }

    /**
     * Convert json string to workshift object
     * @param shiftStr json string to convert
     * @return workshift object
     */
    private static WorkShift shiftFromJsonStr(String shiftStr) {
        WorkShift workShift = null;

        try {
            JSONObject shiftJson = new JSONObject(shiftStr);
            String uuidStr = shiftJson.getString(kUuidKey);
            String employeeId = shiftJson.getString(kEmployeeIdKey);
            long shiftStartMillis = shiftJson.getLong(kShiftStartKey);
            long shiftEndMillis = shiftJson.getLong(kShiftEndKey);
            long breakStartMillis = shiftJson.getLong(kBreakStartKey);
            long breakEndMillis = shiftJson.getLong(kBreakEndKey);
            long lunchStartMillis = shiftJson.getLong(kLunchStartKey);
            long lunchEndMillis = shiftJson.getLong(kLunchEndKey);

            UUID uuid = UUID.fromString(uuidStr);
            workShift = new WorkShift(uuid,
                    employeeId,
                    sliceFor(shiftStartMillis, shiftEndMillis),
                    sliceFor(breakStartMillis, breakEndMillis),
                    sliceFor(lunchStartMillis, lunchEndMillis));
        } catch (JSONException ex) {
            // Rethrow exception
            throw new IllegalArgumentException(TAG + ": Invalid Workshift Json" + shiftStr, ex);
        }
        return workShift;
    }

    /**
     * Create a time slice from start, end times
     * @param startMillis   start time in millis
     * @param endMillis     end time in millis
     * @return timeslice for start, end
     */
    private static TimeSlice sliceFor(long startMillis, long endMillis) {
        Date startDate = new Date(startMillis);
        Date endDate = new Date(endMillis);
        TimeSlice retval = new TimeSlice(startDate, endDate);
        return retval;
    }
}
