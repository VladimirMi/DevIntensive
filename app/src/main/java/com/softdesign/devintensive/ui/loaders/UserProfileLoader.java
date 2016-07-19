package com.softdesign.devintensive.ui.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.UserProfile;

public class UserProfileLoader extends AsyncTaskLoader<UserProfile> {


    public UserProfileLoader(Context context, Bundle args) {
        super(context);
    }

    @Override
    public UserProfile loadInBackground() {
        return DataManager.getInstance().getUserProfileFromDB();
    }

}
