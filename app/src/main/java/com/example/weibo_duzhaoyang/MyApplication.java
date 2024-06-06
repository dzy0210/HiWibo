package com.example.weibo_duzhaoyang;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;

public class MyApplication extends Application {
    private static SharedPreferencesUtil sharedPreferencesUtil;
    public static SharedPreferencesUtil getSp() {
        return sharedPreferencesUtil;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(this);
    }
}
