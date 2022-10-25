/*                                                     SimpleDataStore.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Provides a simple key-value store for persisting string values
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
package com.gregsprogrammingworks.timeclock.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Provides a simple key-value store for persisting string values
 * @note Shared preferences handling adopted from:
 *       <a href="https://www.geeksforgeeks.org/shared-preferences-in-android-with-examples/" />
 */
public class SimpleDataStore {

    /// Tag for logging
    private static final String TAG = SimpleDataStore.class.getSimpleName();

    /// Execution context
    private final Context mContext;

    /// Store name
    private final String mStoreName;

    /**
     * Class factory method creates a data store in an execution context with a name
     * @param context   execution context
     * @param storeName name of store
     * @return created store
     */
    public static SimpleDataStore storeNamed(Context context, String storeName)
    {
        // Just create the store and return it
        SimpleDataStore store = new SimpleDataStore(context, storeName);
        return store;
    }

    /**
     * Get a list of keys to all values in the store
     * @return  list of keys
     */
    public Set<String> keySet() {

        // Get the shared prefs for the store
        SharedPreferences prefs = preferencesStore();

        // Get all entries in the store
        Map<String,?> all = prefs.getAll();

        // We just want the keys
        Set<String> keySet = all.keySet();

        // Return result
        return keySet;
    }

    /**
     * Get the string value associated with a key
     * @param key   key to look up
     * @return value associated with key, or null if key not found
     */
    public String get(String key) {
        SharedPreferences prefs = preferencesStore();
        String retval = prefs.getString(key, "");
        return retval;
    }

    /**
     * Update the value of a key in the store
     * @param key   key whose value is to be updated
     * @param value new value
     */
    public void put(String key, String value) {
        SharedPreferences.Editor editor = preferencesStore().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Constructor for data store
     * @param context   execution context
     * @param storeName name of store
     */
    private SimpleDataStore(Context context, String storeName) {
        mContext = context;
        mStoreName = storeName;
    }

    /**
     * Get the shared preferences to store data in
     * @return  shared preference store
     */
    private SharedPreferences preferencesStore() {
        return mContext.getSharedPreferences(mStoreName, Context.MODE_PRIVATE);
    }
}
