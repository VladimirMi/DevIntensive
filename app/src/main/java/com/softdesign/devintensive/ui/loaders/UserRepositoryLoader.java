package com.softdesign.devintensive.ui.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

public class UserRepositoryLoader extends AsyncTaskLoader<List<Repository>> {
    private String userId;

    public UserRepositoryLoader(Context context, Bundle args) {
        super(context);

        if (args != null) {
            userId = args.getString(ConstantManager.USER_ID_KEY);
        }
    }

    @Override
    public List<Repository> loadInBackground() {
        return DataManager.getInstance().getRepositoriesFromDB(userId);
    }
}
