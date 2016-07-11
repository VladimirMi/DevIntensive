package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<ResponseBody> uploadPhoto(MultipartBody.Part userPhotoReq) {
        return mRestService.uploadPhoto(userPhotoReq);
    }

    // endregion

    // region =========== Database ============



    // endregion
}
