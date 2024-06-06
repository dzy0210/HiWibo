package com.example.weibo_duzhaoyang.retrofit;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class OkhttpInstance {
    private static volatile OkhttpInstance instance = null;
    private OkHttpClient client;

    private final String TAG = "dzy okhttp";

    private final Gson gson;
    private OkhttpInstance() {
        gson = new Gson();
    }
    public static OkhttpInstance getInstance() {
        if (instance == null) {
            synchronized (OkhttpInstance.class) {
                if (instance == null) {
                    instance = new OkhttpInstance();
                }
            }
        }
        return instance;
    }
}
