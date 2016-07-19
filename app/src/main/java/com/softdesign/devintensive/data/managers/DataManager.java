package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UploadImageRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserProfile;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;

public class DataManager {
    private static DataManager ourInstance;
    private PreferencesManager mPreferencesManager;
    private Context mContext;
    private RestService mRestService;
    private Picasso mPicasso;

    private DaoSession mDaoSession;

    private DataManager() {
        mPreferencesManager = new PreferencesManager();
        mContext = DevintensiveApplication.getAppContext();
        mRestService = ServiceGenerator.createService(RestService.class);
        mPicasso = new PicassoCache(mContext).getPicassoInstance();
        mDaoSession = DevintensiveApplication.getDaoSession();
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

    public Picasso getPicasso() {
        return mPicasso;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    // region =========== Network ============

    public Call<LoginModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UploadImageRes> uploadPhoto(String userId, MultipartBody.Part userPhotoReq) {
        return mRestService.uploadPhoto(userId, userPhotoReq);
    }

    public Call<UserModelRes> getUserFromNet(String userId) {
        return mRestService.getUser(userId);
    }

    public Call<UserListRes> getUserListFromNet() {
        return mRestService.getUserList();
    }
    // endregion

    // region =========== Database ============

    public UserProfile getUserProfileFromDB() {
        UserProfile userProfile = null;
        try {
            userProfile = mDaoSession.queryBuilder(UserProfile.class)
                    .build()
                    .unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userProfile;
    }

    public List<User> getUserListFromDB() {
        List<User> temp = new ArrayList<>();

        return temp;
    }

    public List<Repository> getRepositoriesFromDB(String userId) {
        List<Repository> repositories = new ArrayList<>();
        try {
            repositories = mDaoSession.queryBuilder(Repository.class)
                    .where(RepositoryDao.Properties.UserRemoteId.eq(userId))
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repositories;
    }


    // endregion
}
