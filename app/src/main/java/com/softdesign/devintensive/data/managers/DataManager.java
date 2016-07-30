package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UploadImageRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.Like;
import com.softdesign.devintensive.data.storage.models.LikeDao;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DataManager {
    private static DataManager ourInstance;
    private PreferencesManager mPreferencesManager;
    private Context mContext;
    private RestService mRestService;

    private DaoSession mDaoSession;

    private DataManager() {
        mPreferencesManager = new PreferencesManager();
        mContext = DevintensiveApplication.getAppContext();
        mRestService = ServiceGenerator.createService(RestService.class);
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

    public Call<ResponseBody> setLike(String userId) {
        return mRestService.setLike(userId);
    }

    public Call<ResponseBody> deleteLike(String userId) {
        return mRestService.deleteLike(userId);
    }
    // endregion

    // region =========== Database ===========

    public List<User> getUsersListFromDB() {
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .where(UserDao.Properties.Deleted.eq(false))
                    .orderAsc(UserDao.Properties.Order)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<Like> getLikesForUser(String userId) {
        List<Like> likes = new ArrayList<>();

        try {
            likes = mDaoSession.queryBuilder(Like.class)
                    .where(LikeDao.Properties.ObjectRemoteId.eq(userId))
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return likes;
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

    public List<Repository> getAllRepositoriesFromDB() {
        List<Repository> repositories = new ArrayList<>();
        try {
            repositories = mDaoSession.queryBuilder(Repository.class)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repositories;
    }

    public List<User> searchUsers(String search) {
        List<User> users = new ArrayList<>();
        try {
            users = mDaoSession.queryBuilder(User.class)
                    .orderAsc(UserDao.Properties.Order)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getSearchName().contains(search)) {
                result.add(user);
            }
        }

        return result;
    }

    // endregion
}
