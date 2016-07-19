package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosOperation;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.tasks.SaveUsersList;
import com.softdesign.devintensive.data.tasks.events.SavingUserDataEvent;
import com.softdesign.devintensive.data.tasks.events.SavingUsersListEvent;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.Helper;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "AuthActivity";
    @BindView(R.id.sign_in_btn) Button mSignIn;
    @BindView(R.id.reminder_txt) TextView mReminderPassword;
    @BindView(R.id.login_email_et) EditText mLogin;
    @BindView(R.id.login_password_et) EditText mPassword;
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;

    private boolean isLoggedIn;
    private boolean isUsersListExists;
    private boolean isUserDataSavingFinished;
    private boolean isUsersListSavingFinished;
    private boolean isUserDataSaved;
    private boolean isUsersListSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        isLoggedIn = !mPreferencesManager.getAuthToken().equals(ConstantManager.INVALID_TOKEN);
        isUsersListExists = mPreferencesManager.isUsersListExists();

        if (isLoggedIn) {
            if (!NetworkStatusChecker.isNetworkAvaliable(this)) {
                startMainActivity();
            }
            showProgress();
            updateUserData();
            updateUsersList();
        }

        if (!isLoggedIn) {
            if (!NetworkStatusChecker.isNetworkAvaliable(this)) {
                showSnackBar(getString(R.string.err_msg_internet));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loginSuccess(LoginModelRes loginModel) {
        EventBus.getDefault().post(new SavingUserDataEvent(true));

        mPreferencesManager.saveAuthToken(loginModel.getData().getToken());
        mPreferencesManager.saveUserData(loginModel.getData().getUser());

        updateUsersList();
    }

    @OnClick(R.id.reminder_txt)
    void remindPassword() {
        Intent remindIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.FORGOT_PASS_URL));
        startActivity(remindIntent);
    }

    @OnClick(R.id.sign_in_btn)
    void signIn() {
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

                        hideProgress();
                        showSnackBar(getString(R.string.err_msg_login));
                        Log.e(TAG, "signIn onResponse: " + response.message());

                    } else {

                        hideProgress();
                        showSnackBar(getString(R.string.err_msg_unknown));
                        Log.e(TAG, "signIn onResponse: " + response.message());

                    }
                }

                @Override
                public void onFailure(Call<LoginModelRes> call, Throwable t) {

                    hideProgress();
                    Log.e(TAG, "SignIn onFailure: " + t.getMessage());
                    showSnackBar(getString(R.string.err_msg_unknown));

                }
            });
        } else {
            hideProgress();
            showSnackBar(getString(R.string.err_msg_internet));
        }
    }

    private void updateUserData() {
        Call<UserModelRes> call = mDataManager.getUserFromNet(mPreferencesManager.getUserId());

        call.enqueue(new Callback<UserModelRes>() {
            @Override
            public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                if (response.isSuccessful()) {

                    EventBus.getDefault().post(new SavingUserDataEvent(true));
                    mPreferencesManager.saveUserData(response.body().getData());

                } else if (response.code() == 401) {

                    showSnackBar(getString(R.string.err_msg_token));
                    Log.e(TAG, "updateUserData onResponse: " + response.message());
                    mPreferencesManager.clearAllData();
                    mPreferencesManager.saveAuthToken(ConstantManager.INVALID_TOKEN);
                    EventBus.getDefault().post(new SavingUserDataEvent(false));

                } else {

                    showSnackBar(getString(R.string.err_msg_unknown));
                    Log.e(TAG, "updateUserData onResponse: " + response.message());
                    EventBus.getDefault().post(new SavingUserDataEvent(false));

                }
            }

            @Override
            public void onFailure(Call<UserModelRes> call, Throwable t) {

                showSnackBar(getString(R.string.err_msg_unknown));
                Log.e(TAG, "updateUserData onFailure: " + t.getMessage());
                EventBus.getDefault().post(new SavingUserDataEvent(false));

            }
        });
    }

    private void updateUsersList() {
        Call<UserListRes> call = DataManager.getInstance().getUserListFromNet();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                if (response.isSuccessful()) {

                    saveUserList(response.body().getData());

                } else {

                    Log.e(TAG, "updateUsersList onResponse: " + response.message());
                    EventBus.getDefault().post(new SavingUsersListEvent(false));

                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {

                Log.e(TAG, "updateUsersList onFailure: " + t.getMessage());
                EventBus.getDefault().post(new SavingUsersListEvent(false));

            }
        });
    }


    private void saveUserList(List<UserListRes.UserData> userDataList) {
        List<User> users = new ArrayList<>();
        List<Repository> repositories = new ArrayList<>();

        for (UserListRes.UserData userData : userDataList) {
            users.add(new User(userData));
            repositories.addAll(Helper.getRepoListFromUserData(userData));
        }

        ChronosOperation<Boolean> task = new SaveUsersList(users, repositories);
        runOperation(task);
    }


    public void onOperationFinished(final SaveUsersList.Result result) {
        if (result.isSuccessful()) {
            EventBus.getDefault().post(new SavingUsersListEvent(true));
        } else {
            EventBus.getDefault().post(new SavingUsersListEvent(false));
            Log.e(TAG, "onSaveUserListFinished: " + result.getErrorMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveUserData(SavingUserDataEvent event) {

        isUserDataSavingFinished = true;
        isUserDataSaved = event.savingStatus;

        if (isUserDataSavingFinished && isUsersListSavingFinished) {
            decideStartMainActivity();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveUsersList(SavingUsersListEvent event) {

        isUsersListSavingFinished = true;
        isUsersListSaved = event.savingStatus;

        if (isUserDataSavingFinished && isUsersListSavingFinished) {
            decideStartMainActivity();
        }

    }

    private void decideStartMainActivity() {
        hideProgress();
        isLoggedIn = !mPreferencesManager.getAuthToken().equals(ConstantManager.INVALID_TOKEN);

        if (isUsersListSaved) {
            isUsersListExists = true;
        }
        mPreferencesManager.setUsersListExists(isUsersListExists);

        if (isLoggedIn) {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}
