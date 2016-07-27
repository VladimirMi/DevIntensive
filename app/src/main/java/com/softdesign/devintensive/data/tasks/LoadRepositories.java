package com.softdesign.devintensive.data.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadRepositories extends ChronosOperation<HashMap<String, List<Repository>>> {

    private String mUserId;
    private DataManager mDatamanager;
    private RepositoryDao mRepositoryDao;
    private List<Repository> mRepoList;

    public LoadRepositories() {
        mRepositoryDao = DataManager.getInstance().getDaoSession().getRepositoryDao();
        mDatamanager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public HashMap<String, List<Repository>> run() {
        try {
            mRepoList = mDatamanager.getAllRepositoriesFromDB();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        HashMap<String, List<Repository>> result = new HashMap<>();

        for (Repository repository : mRepoList) {
            List<Repository> repositories = new ArrayList<>();
            if (result.containsKey(repository.getUserRemoteId())) {
                result.get(repository.getUserRemoteId()).add(repository);
            } else {
                repositories.add(repository);
                result.put(repository.getUserRemoteId(), repositories);
            }
        }
        return result;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<HashMap<String, List<Repository>>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<HashMap<String, List<Repository>>> {
        public Result() {}
    }
}
