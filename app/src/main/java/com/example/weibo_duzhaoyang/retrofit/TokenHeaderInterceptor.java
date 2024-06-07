package com.example.weibo_duzhaoyang.retrofit;

import com.example.weibo_duzhaoyang.MyApplication;
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
            Request originalRequest = chain.request();
            Request updateRequest = originalRequest.newBuilder()
                    .header("Authorization","Bearer "+ token)
                    .header("content-type", "application/json")
                    .build();
            return chain.proceed(updateRequest);
//        }
    }
}