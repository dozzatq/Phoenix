package com.github.dozzatq.phoenix.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.github.dozzatq.phoenix.Phoenix;

/**
 * Created by Rodion Bartoshik on 12.10.2016.
 */
public class PhoenixPrefsScheme {

    private String prefsName;
    private int prefsMode;

    private Context appContext;

    PhoenixPrefsScheme(String prefsName, int prefsMode) {
        this.prefsName = prefsName;
        this.prefsMode = prefsMode;
        appContext = Phoenix.getInstance().getContext();
        if (appContext==null)
            throw new IllegalStateException("Phoenix must be inited");
    }

    public void applyDefaults(int resId, boolean readAgain )
    {
        if (prefsMode == -1 && prefsName == null)
            PreferenceManager.setDefaultValues(appContext, resId, readAgain);
        else PreferenceManager.setDefaultValues(appContext, prefsName, prefsMode, resId, readAgain);
    }

    public int incrementInt(@NonNull String key, int defaultValue)
    {
        int result = getInt(key, defaultValue);
        putInt(key, result+1);
        return result;
    }

    public int decrementInt(@NonNull String key, int defaultValue)
    {
        int result = getInt(key, defaultValue);
        if (result-1>0)
            putInt(key, result-1);
        else putInt(key, 0);
        return result;
    }

    public void clearPrefs()
    {
        getPrefs().edit().clear().apply();
    }

    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
    public void clearPrefsFuture()
    {
        getPrefs().edit().clear().commit();
    }

    public void putString(@NonNull String key, String value)
    {
        getPrefs().edit().putString(key, value).apply();
    }

    public boolean putStringFuture(@NonNull String key, String value)
    {
        return getPrefs().edit().putString(key, value).commit();
    }

    public String getString(@NonNull String key)
    {
        return getPrefs().getString(key, null);
    }

    public void putBoolean(@NonNull String key, boolean value)
    {
        getPrefs().edit().putBoolean(key, value).apply();
    }

    public boolean putBooleanFuture(@NonNull String key, boolean value)
    {
        return getPrefs().edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(@NonNull String key, boolean default_bool)
    {
        return getPrefs().getBoolean(key, default_bool);
    }

    public void removeValue(@NonNull String key)
    {
        getPrefs().edit().remove(key).apply();
    }

    public void putLong(@NonNull String key, Long value)
    {
        getPrefs().edit().putLong(key, value).apply();
    }

    public boolean putLongFuture(@NonNull String key, Long value)
    {
        return getPrefs().edit().putLong(key, value).commit();
    }

    public long getLong(@NonNull String key, long defValue) {
        return getPrefs().getLong(key, defValue);
    }

    public void putFloat(@NonNull String key, Float value)
    {
        getPrefs().edit().putFloat(key, value).apply();
    }

    public boolean putFloatFuture(@NonNull String key, Float value)
    {
        return getPrefs().edit().putFloat(key, value).commit();
    }

    public float getFloat(@NonNull String key, float defValue) {
        return getPrefs().getFloat(key, defValue);
    }

    public void putInt(@NonNull String key, Integer value)
    {
        getPrefs().edit().putInt(key, value).apply();
    }

    public boolean putIntFuture(@NonNull String key, Integer value)
    {
        return getPrefs().edit().putInt(key, value).commit();
    }

    public int getInt(@NonNull String key, int defValue) {
        return getPrefs().getInt(key, defValue);
    }

    public String getString(@NonNull String key, String defValue) {
        return getPrefs().getString(key, defValue);
    }

    private SharedPreferences getPrefs() {
        if (prefsMode == -1 && prefsName == null)
            return PreferenceManager.getDefaultSharedPreferences(appContext);
        else return appContext.getSharedPreferences(prefsName, prefsMode);
    }

    private boolean save(@NonNull String key, long value) {
        return getPrefs()
                .edit()
                .putLong(key, value)
                .commit();
    }

    private boolean save(@NonNull String key, float value) {
        return getPrefs()
                .edit()
                .putFloat(key, value)
                .commit();
    }

    private boolean save(@NonNull String key, int value) {
        return getPrefs()
                .edit()
                .putInt(key, value)
                .commit();
    }

    private boolean save(@NonNull String key, String value) {
        return getPrefs()
                .edit()
                .putString(key, value)
                .commit();
    }
}
