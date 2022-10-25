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

class WorkShiftDataStore {

    private static final String TAG = WorkShiftDataStore.class.getSimpleName();

    private static final String kStoreName = "workShiftStore";

    private final SimpleDataStore mDataStore;

    WorkShiftDataStore(Context context) {
        mDataStore = SimpleDataStore.storeNamed(context, kStoreName);
    }

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

    void save(WorkShift shift) {
        String jsonStr = shiftToJsonStr(shift);
        String shiftKey = shift.getUuid().toString();
        mDataStore.put(shiftKey, jsonStr);
    }

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
     */
    private static final String kUuidKey = "uuid";
    private static final String kEmployeeIdKey  = "employeeId";
    private static final String kShiftStartKey  = "shift.start";
    private static final String kShiftEndKey    = "shift.end";
    private static final String kBreakStartKey  = "break.start";
    private static final String kBreakEndKey    = "break.end";
    private static final String kLunchStartKey  = "lunch.start";
    private static final String kLunchEndKey    = "lunch.end";

    private static String shiftToJsonStr(WorkShift shift) {
        Map<String,Object> shiftMap = new HashMap<String, Object>();
        shiftMap.put(kUuidKey, shift.getUuid());
        shiftMap.put(kEmployeeIdKey, shift.getEmployeeId());
        shiftMap.put(kShiftStartKey, shift.getShiftTimeSlice().getStartDate().getTime());
        shiftMap.put(kShiftEndKey, shift.getShiftTimeSlice().getEndDate().getTime());
        shiftMap.put(kLunchStartKey, shift.getLunchTimeSlice().getStartDate().getTime());
        shiftMap.put(kLunchEndKey, shift.getLunchTimeSlice().getEndDate().getTime());
        shiftMap.put(kBreakStartKey, shift.getBreakTimeSlice().getStartDate().getTime());
        shiftMap.put(kBreakEndKey, shift.getBreakTimeSlice().getEndDate().getTime());

        JSONObject shiftJson = new JSONObject(shiftMap);

        String jsonStr = shiftJson.toString();
        return jsonStr;
    }

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

    private static TimeSlice sliceFor(long startMillis, long endMillis) {
        Date startDate = new Date(startMillis);
        Date endDate = new Date(endMillis);
        TimeSlice retval = new TimeSlice(startDate, endDate);
        return retval;
    }
}
