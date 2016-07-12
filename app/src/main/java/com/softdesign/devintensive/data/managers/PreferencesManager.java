package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
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
        for (String userFieldKey : USER_INFO) {
            userFields.add(mSharedPreferences.getString(userFieldKey, ""));
        }
        return userFields;
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                ""));
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "");
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
            userStatistic.add(mSharedPreferences.getString(userFieldKey, ""));
        }
        return userStatistic;
    }

    public void saveUserAvatar(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserAvatar() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, ""));
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUserName(String firstName, String secondName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_NAME_KEY, secondName + " " + firstName);
        editor.apply();
    }

    public String loadUserName() {
        return mSharedPreferences.getString(ConstantManager.USER_NAME_KEY, "");
    }

    public void saveUserValues(UserModelRes.Data userModel) {
        List<String> userStatistic = new ArrayList<>();
        userStatistic.add(String.valueOf(userModel.getProfileValues().getRating()));
        userStatistic.add(String.valueOf(userModel.getProfileValues().getLinesCode()));
        userStatistic.add(String.valueOf(userModel.getProfileValues().getProjects()));

        List<String> userInfo = new ArrayList<>();
        userInfo.add(userModel.getContacts().getPhone());
        userInfo.add(userModel.getContacts().getEmail());
        userInfo.add(userModel.getContacts().getVk());
        // TODO: 7/10/2016 разобраться с множественными репозиториями
        userInfo.add(userModel.getRepositories().getRepo().get(0).getGit());
        userInfo.add(userModel.getPublicInfo().getBio());

        saveUserStatistic(userStatistic);
        saveUserInfo(userInfo);
        saveUserPhoto(Uri.parse(userModel.getPublicInfo().getPhoto()));
        saveUserAvatar(Uri.parse(userModel.getPublicInfo().getAvatar()));
        saveUserName(userModel.getFirstName(), userModel.getSecondName());
    }
}
