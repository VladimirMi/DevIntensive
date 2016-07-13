package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UploadImageRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import okhttp3.MultipartBody;
import retrofit2.Call;

public class DataManager {
    private static DataManager ourInstance;
    private PreferencesManager mPreferencesManager;
    private Context mContext;
    private RestService mRestService;

    private DataManager() {
        mPreferencesManager = new PreferencesManager();
        mContext = DevintensiveApplication.getAppContext();
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    // region =========== Network ============

    public Call<LoginModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UploadImageRes> uploadPhoto(String userId, MultipartBody.Part userPhotoReq) {
        return mRestService.uploadPhoto(userId, userPhotoReq);
    }

    public Call<UserModelRes> getUser(String userId) {
        return mRestService.getUser(userId);
    }

    // endregion

    // region =========== Database ============



    // endregion
}
