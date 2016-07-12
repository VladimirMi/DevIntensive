package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;

public class HelperActivity extends BaseActivity {
    private PreferencesManager mPreferencesManager = DataManager.getInstance().getPreferencesManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgress();
        if (mPreferencesManager.getAuthToken().isEmpty() && mPreferencesManager.getUserId().isEmpty()) {
            startLoginActivity();
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mainActivityIntent);
        finish();
    }

    private void startLoginActivity() {
        Intent mainActivityIntent = new Intent(this, LoginActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mainActivityIntent);
        finish();
    }
}
