package com.example.weibo_duzhaoyang;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;

public class MyApplication extends Application {
    private static SharedPreferencesUtil sharedPreferencesUtil;
    public static SharedPreferencesUtil getSp() {
        return sharedPreferencesUtil;
    }
//    private HttpProxyCacheServer proxy;

//    public static HttpProxyCacheServer getProxy(Context context) {
//        MyApplication app = (MyApplication) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }
//
//    private HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer.Builder(this)
//                .maxCacheSize(1024 * 1024 * 1024)
//                .build();
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sharedPreferencesUtil = null;
    }
}
