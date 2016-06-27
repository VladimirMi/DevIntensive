package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "BaseActivity";
    protected ProgressDialog mProgressDialog;

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
        runWithDelay();
    }

    public void hideProgress() {
        mProgressDialog.hide();
    }

    public void showError(String message, Exception error) {
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void runWithDelay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 5000);
    }
}