package com.example.weibo_duzhaoyang.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private String TAG = "dzy RetrofitManger";
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
//        client = new OkHttpClient
//                .Builder()
//                .addInterceptor(new TokenHeaderInterceptor(token))
//                .build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (token != null) {
            builder.addInterceptor(new TokenHeaderInterceptor(token));
        }
        client = builder.build();


        Log.i(TAG, "RetrofitManager: client"+client);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://hotfix-service-prod.g.mi.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.i(TAG, "RetrofitManager: retrofit"+retrofit);
    }
    public ApiService createApi() {
        return retrofit.create(ApiService.class);
    }
    public static RetrofitManager getInstance(String token) {
        return retrofitManager = new RetrofitManager(token);
    }
}
