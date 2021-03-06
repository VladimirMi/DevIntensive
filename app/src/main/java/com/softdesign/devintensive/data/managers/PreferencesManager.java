package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferencesManager {
    private SharedPreferences mSharedPreferences;

    private static final String[] USER_INFO = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_ABOUT_KEY,
    };

    private static final String[] USER_STATISTIC = {
            ConstantManager.USER_RATING_KEY,
            ConstantManager.USER_CODE_LINES_KEY,
            ConstantManager.USER_PROJECTS_KEY
    };

    public PreferencesManager() {
        mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserInfo(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < userFields.size(); i++) {
            editor.putString(USER_INFO[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserInfo() {
        List<String> userFields = new ArrayList<>();
        for (String aUSER_INFO : USER_INFO) {
            if (aUSER_INFO.equals(ConstantManager.USER_GIT_KEY)) {
                Collections.addAll(userFields, mSharedPreferences.getString(aUSER_INFO, "null").split(" "));
            } else {
                userFields.add(mSharedPreferences.getString(aUSER_INFO, "null"));
            }
        }
        return userFields;
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public String loadUserPhoto() {
        return mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "null");
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, ConstantManager.INVALID_TOKEN);
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserStatistic(List<String> userStatistic) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < userStatistic.size(); i++) {
            editor.putString(USER_STATISTIC[i], userStatistic.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserStatistic() {
        List<String> userStatistic = new ArrayList<>();
        for (String userFieldKey : USER_STATISTIC) {
            userStatistic.add(mSharedPreferences.getString(userFieldKey, "null"));
        }
        return userStatistic;
    }

    public void saveUserAvatar(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public String loadUserAvatar() {
        return mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, "null");
    }

    public void saveUserName(String fullName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_NAME_KEY, fullName);
        editor.apply();
    }

    public String loadUserName() {
        return mSharedPreferences.getString(ConstantManager.USER_NAME_KEY, "null");
    }

    public void saveUserData(UserModelRes.Data userData) {
        saveUserId(userData.getId());

        List<String> userStatistic = new ArrayList<>();
        userStatistic.add(String.valueOf(userData.getProfileValues().getRating()));
        userStatistic.add(String.valueOf(userData.getProfileValues().getCodeLines()));
        userStatistic.add(String.valueOf(userData.getProfileValues().getProjects()));
        saveUserStatistic(userStatistic);

        List<String> userInfo = new ArrayList<>();
        userInfo.add(userData.getContacts().getPhone());
        userInfo.add(userData.getContacts().getEmail());
        userInfo.add(userData.getContacts().getVk());

        StringBuilder repositories = new StringBuilder();
        for (UserModelRes.Repo repo : userData.getRepositories().getRepo()) {
            repositories.append(repo.getGit()).append(" ");
        }
        userInfo.add(repositories.toString());

        userInfo.add(userData.getPublicInfo().getBio());
        saveUserInfo(userInfo);

        saveUserPhoto(Uri.parse(userData.getPublicInfo().getPhoto()));

        saveUserAvatar(Uri.parse(userData.getPublicInfo().getAvatar()));

        saveUserName(userData.getFullName());
    }

    public int getRepositoriesSize() {
        return mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "null").split(" ").length;
    }

    public void setUsersListExists(boolean isUsersListExists) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ConstantManager.USERS_LIST_EXISTS_KEY, isUsersListExists);
        editor.apply();
    }

    public boolean isUsersListExists() {
        return mSharedPreferences.getBoolean(ConstantManager.USERS_LIST_EXISTS_KEY, false);
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
