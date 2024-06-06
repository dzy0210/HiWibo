package com.example.weibo_duzhaoyang;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {
    private static MyApplication application;
    private static SharedPreferences sp;
    public SharedPreferences getSp() {
        return sp;
    }
    public static MyApplication getInstance() {
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
