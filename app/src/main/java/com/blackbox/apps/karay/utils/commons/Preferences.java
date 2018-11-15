package com.blackbox.apps.karay.utils.commons;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Set;

/**
 * SharedPreferences Utilities
 */
public final class Preferences {


    private static Preferences mInstance = null;
    private SharedPreferences sharedPreferences;

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences("sp_kapray", Activity.MODE_PRIVATE);
    }

    public static void init(Context context) {
        mInstance = new Preferences(context);

    }

    public static Preferences getInstance() {
        if (mInstance == null) {
            throw new RuntimeException(
                    "Must run init(Application application) before an instance can be obtained");
        }
        return mInstance;
    }


    public boolean save(String key, String value) {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }


    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }


    public boolean save(HashMap<String, String> valuesMap) {
        Editor editor = sharedPreferences.edit();
        String value = "";
        for (String key : valuesMap.keySet()) {
            value = valuesMap.get(key);
            editor.putString(key, value);
        }
        return editor.commit();
    }

    public boolean save(String key, boolean value) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void clear(Context context) {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void remove(String key) {
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


    public boolean save(String key, int value) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean save(String key, float value) {
        Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }


    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public boolean save(String key,  Set<String> value) {
        if (value == null) return false;
        Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }


    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sharedPreferences.getStringSet(key, defaultValue);
    }

}
