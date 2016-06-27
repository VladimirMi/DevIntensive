package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.DevintensiveApplication;

public class PreferencesManager {
    private SharedPreferences mSharedPreferences;

    public PreferencesManager() {
        mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }
}
