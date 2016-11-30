package org.weibeld.example.imageviewscaletypes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper around the single SharedPreferences file of the application.
 */
class Pref {

    // The unique application-wide SharedPreferences file
    private static SharedPreferences mPrefFile = null;
    // Mapping between keys and default values
    private static Map<Integer, Integer> mDefaultVals = null;

    private Pref() {}

    // Get a SharedPreferences value by supplying the resource ID of the key
    static String get(Context c, int resIdKey) {
        check(c, resIdKey);
        return mPrefFile.getString(c.getString(resIdKey), c.getString(mDefaultVals.get(resIdKey)));
    }

    // Get the default value of a SharedPreference entry by supplying the resource ID of the key
    static String getDefault(Context c, int resIdKey) {
        check(c, resIdKey);
        return c.getString(mDefaultVals.get(resIdKey));
    }

    // Save a new value to a SharedPreferences entry
    static void put(Context c, int resIdKey, String value) {
        check(c, resIdKey);
        mPrefFile.edit().putString(c.getString(resIdKey), value).apply();
    }

    // Check if singleton instances have been instantiated, and if the provided key exists
    private static void check(Context c, int resIdKey) {
        if (mPrefFile == null || mDefaultVals == null)
            init(c);
        if (!mDefaultVals.containsKey(resIdKey))
            throw new IllegalArgumentException("Unknown SharedPreferences key: " + c.getString(resIdKey));
    }

    // Instantiate SharedPreferences ref, and initialise mapping between keys and default values
    private static void init(Context c) {
        mPrefFile = c.getSharedPreferences(c.getString(R.string.main_shared_prefs), Context.MODE_PRIVATE);
        mDefaultVals = new HashMap<>();
        mDefaultVals.put(R.string.pref_layout_width_key, R.string.pref_layout_width_default);
        mDefaultVals.put(R.string.pref_layout_height_key, R.string.pref_layout_height_default);
        mDefaultVals.put(R.string.pref_background_key, R.color.colorAccent);
        mDefaultVals.put(R.string.pref_adjustViewBounds_key, R.string.pref_adjustViewBounds_default);
        mDefaultVals.put(R.string.pref_maxWidth_key, R.string.pref_maxWidth_default);
        mDefaultVals.put(R.string.pref_maxHeight_key, R.string.pref_maxHeight_default);
        mDefaultVals.put(R.string.pref_image_key, R.string.pref_image_default);
    }
}
