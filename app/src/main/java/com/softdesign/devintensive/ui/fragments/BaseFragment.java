package com.softdesign.devintensive.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosConnector;
import com.redmadrobot.chronos.ChronosOperation;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;

public class BaseFragment extends Fragment {
    protected DataManager mDataManager;
    protected PreferencesManager mPreferencesManager;
    protected Context mContext;

    protected ChronosConnector mConnector = new ChronosConnector();

    public BaseFragment() {
        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();
        mContext = mDataManager.getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector.onCreate(this, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        mConnector.onPause();
        super.onPause();
    }

    /**
     * Создание и запуск SENDTO интента
     *
     * @param uri цель SENDTO
     */
    protected void sendEmail(Uri uri) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent, getString(R.string.chooser_email)));
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

    public final int runOperation(@NonNull final ChronosOperation operation) {
        return mConnector.runOperation(operation, false);
    }
}
