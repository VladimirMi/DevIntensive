package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UploadImageRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestService {
    @POST("login")
    Call<LoginModelRes> loginUser(@Body UserLoginReq req);

    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<UploadImageRes> uploadPhoto(@Path("userId") String userId, @Part MultipartBody.Part image);

    @GET("user/{userId}")
    Call<UserModelRes> getUser(@Path("userId") String userId);

    @GET("user/list?orderBy=rating")
    Call<UserListRes> getUserList();

    @POST("user/{userId}/like")
    Call<ResponseBody> setLike(@Path("userId") String userId);

    @POST("user/{userId}/unlike")
    Call<ResponseBody> deleteLike(@Path("userId") String userId);
}

