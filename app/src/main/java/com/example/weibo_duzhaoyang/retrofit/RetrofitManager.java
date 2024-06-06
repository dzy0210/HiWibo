package com.example.weibo_duzhaoyang.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static volatile RetrofitManager retrofitManager;
    private final Retrofit retrofit;

    private OkHttpClient client;

//    private RetrofitManager() {
//        client = new OkHttpClient
//                .Builder()
//                .build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://hotfix-service-prod.g.mi.com")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
    private RetrofitManager(String token) {
        if (token.equals("")) {
            client = new OkHttpClient.Builder().build();
        }else {
            client = new OkHttpClient
                    .Builder()
                    .addInterceptor(new TokenHeaderInterceptor(token))
                    .build();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("https://hotfix-service-prod.g.mi.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public ApiService createApi() {
        return retrofit.create(ApiService.class);
    }
    public static RetrofitManager getInstance(String token) {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (retrofitManager == null) {
                    retrofitManager = new RetrofitManager(token);
                }
            }
        }
        return retrofitManager;
    }
}
