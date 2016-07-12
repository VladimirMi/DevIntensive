package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModelRes {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("user")
        @Expose
        private UserModelRes.Data user;
        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }

        public UserModelRes.Data getUser() {
            return user;
        }
    }

}
