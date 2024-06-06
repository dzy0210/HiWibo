package com.example.weibo_duzhaoyang.retrofit;

import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//在请求头里添加token的拦截器处理
public class TokenHeaderInterceptor implements Interceptor {
    String token;
    TokenHeaderInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        if (token.isEmpty()) {
            Request originalRequest = chain.request();
            return chain.proceed(originalRequest);
        }else {
            Request originalRequest = chain.request();
            //key的话以后台给的为准，我这边是叫token
            Request updateRequest = originalRequest.newBuilder().header("Authorization","Bearer "+ token).build();
            return chain.proceed(updateRequest);
        }
    }
}