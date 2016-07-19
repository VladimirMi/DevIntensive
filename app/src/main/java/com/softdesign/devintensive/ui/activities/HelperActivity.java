//package com.softdesign.devintensive.ui.activities;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//
//import com.softdesign.devintensive.data.managers.DataManager;
//import com.softdesign.devintensive.data.managers.PreferencesManager;
//import com.softdesign.devintensive.data.network.res.UserModelRes;
//import com.softdesign.devintensive.data.storage.models.UserProfile;
//import com.softdesign.devintensive.utils.NetworkStatusChecker;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class HelperActivity extends BaseActivity {
//    private PreferencesManager mPreferencesManager = DataManager.getInstance().getPreferencesManager();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (mPreferencesManager.getAuthToken().isEmpty() || mPreferencesManager.getUserId().isEmpty()) {
//            Intent authActivityIntent = new Intent(this, AuthActivity.class);
//            startActivity(authActivityIntent);
//        } else {
//            showProgress();
//            updateProfileInfo();
//        }
//    }
//
//    private void updateProfileInfo() {
//        if (NetworkStatusChecker.isNetworkAvaliable(this)) {
//            Call<UserModelRes> call = mDataManager.getUserFromNet(mPreferencesManager.getUserId());
//
//            call.enqueue(new Callback<UserModelRes>() {
//                @Override
//                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
//                    SaveUserProfile task = new SaveUserProfile();
//                    task.execute(response.body().getData());
//                }
//
//                @Override
//                public void onFailure(Call<UserModelRes> call, Throwable t) {
//
//                }
//            });
//        } else {
//            hideProgress();
//            startMainActivity();
//        }
//    }
//
//    private void startMainActivity() {
//        Intent mainActivityIntent = new Intent(this, MainActivity.class);
//        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(mainActivityIntent);
//    }
//
//    private void startLoginActivity() {
//        Intent mainActivityIntent = new Intent(this, AuthActivity.class);
//        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(mainActivityIntent);
//    }
//
//    public class SaveUserProfile extends AsyncTask<UserModelRes.Data, Void, Void> {
//
//        @Override
//        protected Void doInBackground(UserModelRes.Data... params) {
//            mUserProfileDao.insertOrReplace(new UserProfile(params[0]));
//            mRepositoryDao.insertOrReplaceInTx(getRepoListFromUserData(params[0]));
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            hideProgress();
//            startMainActivity();
//        }
//    }
//
//}