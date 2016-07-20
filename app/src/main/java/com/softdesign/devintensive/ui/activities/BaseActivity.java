package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.redmadrobot.chronos.ChronosConnector;
import com.redmadrobot.chronos.ChronosOperation;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "BaseActivity";
    protected ProgressDialog mProgressDialog;
    protected DataManager mDataManager;
    protected PreferencesManager mPreferencesManager;
    protected UserDao mUserDao;
    protected RepositoryDao mRepositoryDao;

    protected ChronosConnector mConnector = new ChronosConnector();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector.onCreate(this, savedInstanceState);

        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        mConnector.onPause();
        super.onPause();
    }

    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.layout.progress_splash);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    public void showError(String message, Exception error) {
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public final int runOperation(@NonNull final ChronosOperation operation) {
        return mConnector.runOperation(operation, false);
    }
}
