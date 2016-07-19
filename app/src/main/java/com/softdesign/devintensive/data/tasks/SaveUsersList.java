package com.softdesign.devintensive.data.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.List;

public class SaveUsersList extends ChronosOperation<Boolean> {
    private final List<User> mUsers;
    private final List<Repository> mRepositories;
    private UserDao mUserDao;
    private RepositoryDao mRepositoryDao;

    public SaveUsersList(List<User> users, List<Repository> repositories) {
        mUsers = users;
        mRepositories = repositories;
        mUserDao = DataManager.getInstance().getDaoSession().getUserDao();
        mRepositoryDao = DataManager.getInstance().getDaoSession().getRepositoryDao();
    }

    @Nullable
    @Override
    public Boolean run() {
        try {
            mUserDao.insertInTx(mUsers);
            mRepositoryDao.insertInTx(mRepositories);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Boolean>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<Boolean> {
        public Result() {}
    }
}
