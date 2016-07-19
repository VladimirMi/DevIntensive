package com.softdesign.devintensive.data.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.List;

public class LoadUsersList extends ChronosOperation<List<User>> {
    private DataManager mDatamanager;
    private UserDao mUserDao;
    private RepositoryDao mRepositoryDao;
    private List<User> mUsersList;

    public LoadUsersList() {
        mUserDao = DataManager.getInstance().getDaoSession().getUserDao();
        mDatamanager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public List<User> run() {
        try {
            mUsersList = mDatamanager.getUsersListFromDB();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mUsersList;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<User>> {
        public Result() {}
    }
}
