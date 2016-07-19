package com.softdesign.devintensive.utils;

import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;

import java.util.ArrayList;
import java.util.List;

public class Helper {

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
}
