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
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

public class SaveUsersList extends ChronosOperation<String> {
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
    public String run() {
        try {
            if (DataManager.getInstance().getPreferencesManager().isUsersListExists()) {
                mUserDao.updateInTx(mUsers);
                mRepositoryDao.updateInTx(mRepositories);
                return ConstantManager.USER_LIST_UPDATED;
            } else {
                mUserDao.insertOrReplaceInTx(mUsers);
                mRepositoryDao.insertOrReplaceInTx(mRepositories);
                return ConstantManager.USER_LIST_CREATED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ConstantManager.USER_LIST_FAIL;
        }
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<String>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<String> {
        public Result() {}
    }
}
