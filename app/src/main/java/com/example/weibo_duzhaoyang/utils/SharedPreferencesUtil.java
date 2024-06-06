package com.example.weibo_duzhaoyang.utils;

import android.content.Context;
import android.content.SharedPreferences;  
  
public class SharedPreferencesUtil {  
    private static final String PREFERENCE_NAME = "dzy";
  
    // 存储String类型的值  
    public static void saveString(Context context, String key, String value) {  
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sharedPreferences.edit();  
        editor.putString(key, value);  
        editor.apply();  
    }  
  
    // 获取String类型的值  
    public static String getString(Context context, String key, String defaultValue) {  
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);  
        return sharedPreferences.getString(key, defaultValue);  
    }  
  
    // 存储Boolean类型的值  
    public static void saveBoolean(Context context, String key, boolean value) {  
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sharedPreferences.edit();  
        editor.putBoolean(key, value);  
        editor.apply();  
    }  
  
    // 获取Boolean类型的值  
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {  
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);  
        return sharedPreferences.getBoolean(key, defaultValue);  
    }  
  
    // 清除特定key的值  
    public static void removeKey(Context context, String key) {  
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sharedPreferences.edit();  
        editor.remove(key);  
        editor.apply();  
    }  
  
    // 清除所有数据  
    public static void clearAll(Context context) {  
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sharedPreferences.edit();  
        editor.clear();  
        editor.apply();  
    }  
}  