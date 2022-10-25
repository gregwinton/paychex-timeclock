package com.gregsprogrammingworks.timeclock.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

import java.util.Map;
import java.util.Set;

/**
 * Abstracts interface to data store from implementation.
 * @note Shared preferences handling adopted from:
 *       <a href="https://www.geeksforgeeks.org/shared-preferences-in-android-with-examples/" />
 */
public class SimpleDataStore {

    /// Tag for logging
    private static final String TAG = SimpleDataStore.class.getSimpleName();

    private static final String kStoreNameSfx = ".prefs";
    private final Context mContext;
    private final String mStoreName;

    public static SimpleDataStore defaultStore(Context context) {
        String storeName = defaultStoreName(context);
        SimpleDataStore store = storeNamed(context, storeName);
        return store;
    }

    public static SimpleDataStore storeNamed(Context context, String storeName)
    {
        SimpleDataStore store = new SimpleDataStore(context, storeName);
        return store;
    }

    public Set<String> keySet() {
        SharedPreferences prefs = sharedPreferences();
        Map<String,?> all = prefs.getAll();
        Set<String> keySet = all.keySet();
        return keySet;
    }

    public String get(String key) {
        SharedPreferences prefs = sharedPreferences();
        String retval = prefs.getString(key, "");
        return retval;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private SimpleDataStore(Context context, String storeName) {
        mContext = context;
        mStoreName = storeName;
    }

    private SharedPreferences sharedPreferences() {
        return mContext.getSharedPreferences(mStoreName, Context.MODE_PRIVATE);
    }

    /**
     * Use application name + suffix to calc. name
     * @param context   Execution context
     * @return  store name
     * @note Adopted from  <a href="https://stackoverflow.com/questions/11229219/android-how-to-get-application-name-not-package-name"/>
     */
    public static String defaultStoreName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        StringBuilder storeNameBldr = new StringBuilder();
        if (0 == applicationInfo.labelRes) {
            storeNameBldr.append(applicationInfo.nonLocalizedLabel);
        }
        else {
            storeNameBldr.append(context.getString(applicationInfo.labelRes));
        }
        storeNameBldr.append(kStoreNameSfx);

        String retval = storeNameBldr.toString();
        return retval;
    }
}
