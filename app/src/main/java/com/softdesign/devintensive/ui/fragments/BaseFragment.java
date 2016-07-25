package com.softdesign.devintensive.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;

public class BaseFragment extends Fragment {
    protected DataManager mDataManager;
    protected PreferencesManager mPreferencesManager;
    protected Context mContext;

    public BaseFragment() {
        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();
        mContext = mDataManager.getContext();
    }

    /**
     * Создание и запуск ACTION_VIEW интента
     *
     * @param uri цель ACTION_VIEW
     */
    protected void makeActionView(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
