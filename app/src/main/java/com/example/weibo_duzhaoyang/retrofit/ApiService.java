package com.example.weibo_duzhaoyang.retrofit;

import com.example.weibo_duzhaoyang.bean.BaseBean;
import com.example.weibo_duzhaoyang.bean.LoginBean;
import com.example.weibo_duzhaoyang.bean.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/weibo/api/auth/sendCode")
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    Call<BaseBean<Boolean>> getSms(@Body String phone);


    @POST("/weibo/api/auth/login")
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    Call<BaseBean<String>> login(@Body LoginBean bean);

    @GET("/weibo/api/user/info")
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    Call<BaseBean<UserInfo>> getUserInfo();

}
