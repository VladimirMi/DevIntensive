package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
        setContentView(R.layout.login);
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
        Intent remindIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(remindIntent);
    }

    private void loginSuccess(UserModelRes userModel) {
        showSnackBar("Вход");
        mPreferencesManager.saveAuthToken(userModel.getData().getToken());
        mPreferencesManager.saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        startMainActivity();
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvaliable(this)) {
            UserLoginReq request = new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString());
            Call<UserModelRes> call = mDataManager.loginUser(request);

            showProgress();
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.isSuccessful()) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("SNAFU");
                    }
                    hideProgress();
                }
                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    showSnackBar("SNAFU");
                    hideProgress();
                }
            });
        } else {
            showSnackBar("Интернет не доступен");
        }
    }

    private void saveUserValues(UserModelRes userModel) {
        UserModelRes.User user = userModel.getData().getUser();
        List<String> userStatistic = new ArrayList<>();
        userStatistic.add(String.valueOf(user.getProfileValues().getRating()));
        userStatistic.add(String.valueOf(user.getProfileValues().getLinesCode()));
        userStatistic.add(String.valueOf(user.getProfileValues().getProjects()));
        mPreferencesManager.saveUserStatistic(userStatistic);

        List<String> userInfo = new ArrayList<>();
        userInfo.add(user.getContacts().getPhone());
        userInfo.add(user.getContacts().getEmail());
        userInfo.add(user.getContacts().getVk());
        // TODO: 7/10/2016 разобраться с множественными репозиториями
        userInfo.add(user.getRepositories().getRepo().get(0).getGit());
        userInfo.add(user.getPublicInfo().getBio());
        mPreferencesManager.saveUserInfo(userInfo);

        mPreferencesManager.saveUserPhoto(Uri.parse(user.getPublicInfo().getPhoto()));
        mPreferencesManager.saveUserAvatar(Uri.parse(user.getPublicInfo().getAvatar()));
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivityIntent);
    }
}
