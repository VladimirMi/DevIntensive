package com.softdesign.devintensive.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Like;
import com.softdesign.devintensive.data.storage.models.Repository;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Helper {

    public static File createImageFile() throws IOException {
        Context context = DataManager.getInstance().getContext();
        String timeStamp = new SimpleDateFormat(AppConfig.TIMESTAMP_FORMAT,
                context.getResources().getConfiguration().locale).format(new Date());
        String imageFileName = AppConfig.PHOTO_FILE_PREFIX + timeStamp;
        File imageFile;
        if (ExStorageState.isWritable()) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            imageFile = File.createTempFile(imageFileName, ConstantManager.EXTENSION_JPEG, storageDir);
        } else {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, ConstantManager.MIME_TYPE_JPEG);
        values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return imageFile;
    }

    public static List<Repository> getRepoListFromUserData(UserModelRes.Data userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repo : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repo, userId));
        }
        return repositories;
    }

    public static List<Repository> getRepoListFromUserData(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repo : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repo, userId));
        }
        return repositories;
    }

    public static List<Like> getLikeListFromUserData(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Like> likes = new ArrayList<>();
        for (String likeBy : userData.getProfileValues().getLikesBy()) {
            likes.add(new Like(userId, likeBy));
        }
        return likes;
    }
}
