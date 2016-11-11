package org.weibeld.example.imageviewscaletypes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper around the single SharedPreferences file of the application.
 */
class Pref {

    private static SharedPreferences prefs = null;
    private static Map<Integer, Integer> map = null;

    private Pref() {}

    // Get a SharedPreferences value by supplying the resource ID of the key
    static String get(Context c, int resIdKey) {
        check(c, resIdKey);
        return prefs.getString(c.getString(resIdKey), c.getString(map.get(resIdKey)));
    }

    // Get the default value of a SharedPreference entry by supplying the resource ID of the key
    static String getDefault(Context c, int resIdKey) {
        check(c, resIdKey);
        return c.getString(map.get(resIdKey));
    }

    // Save a new value to a SharedPreferences entry
    static void put(Context c, int resIdKey, String value) {
        check(c, resIdKey);
        prefs.edit().putString(c.getString(resIdKey), value).apply();
    }

    private static void check(Context c, int resIdKey) {
        if (prefs == null || map == null)
            init(c);
        if (!map.containsKey(resIdKey))
            throw new IllegalArgumentException("Unknown SharedPreferences key: " + c.getString(resIdKey));
    }

    // Get reference to SharedPreferences, and initialise mapping between keys and default values.
    // If a new SharedPreferences entry is added, it must be added to the mapping in this method.
    private static void init(Context c) {
        prefs = c.getSharedPreferences(c.getString(R.string.main_shared_prefs), Context.MODE_PRIVATE);
        map = new HashMap<>();
        map.put(R.string.pref_layout_width_key, R.string.pref_layout_width_default);
        map.put(R.string.pref_layout_height_key, R.string.pref_layout_height_default);
        map.put(R.string.pref_background_key, R.color.colorAccent);
        map.put(R.string.pref_adjustViewBounds_key, R.string.pref_adjustViewBounds_default);
        map.put(R.string.pref_maxWidth_key, R.string.pref_maxWidth_default);
        map.put(R.string.pref_maxHeight_key, R.string.pref_maxHeight_default);
    }
}
