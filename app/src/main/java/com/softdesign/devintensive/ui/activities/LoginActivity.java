package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.sign_in_btn) Button mSignIn;
    @BindView(R.id.reminder_txt) TextView mReminderPassword;
    @BindView(R.id.login_email_et) EditText mLogin;
    @BindView(R.id.login_password_et) EditText mPassword;
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager = DataManager.getInstance();
    private PreferencesManager mPreferencesManager = mDataManager.getPreferencesManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mSignIn.setOnClickListener(this);
        mReminderPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_btn:
                signIn();
                break;
            case R.id.reminder_txt:
                remindPassword();
                break;
        }

    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void remindPassword() {
        Intent remindIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.FORGOT_PASS_URL));
        startActivity(remindIntent);
    }

    private void loginSuccess(LoginModelRes loginModel) {
        mPreferencesManager.saveAuthToken(loginModel.getData().getToken());
        mPreferencesManager.saveUserId(loginModel.getData().getUser().getId());
        mPreferencesManager.saveUserValues(loginModel.getData().getUser());
        startMainActivity();
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvaliable(this)) {
            UserLoginReq request = new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString());
            Call<LoginModelRes> call = mDataManager.loginUser(request);

            showProgress();
            call.enqueue(new Callback<LoginModelRes>() {
                @Override
                public void onResponse(Call<LoginModelRes> call, Response<LoginModelRes> response) {
                    if (response.isSuccessful()) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackBar(getString(R.string.err_msg_login));
                    } else {
                        showSnackBar(getString(R.string.err_msg_unknown));
                    }
                    hideProgress();
                }
                @Override
                public void onFailure(Call<LoginModelRes> call, Throwable t) {
                    showSnackBar(getString(R.string.err_msg_unknown));
                    hideProgress();
                }
            });
        } else {
            showSnackBar(getString(R.string.err_msg_internet));
        }
    }


    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}
