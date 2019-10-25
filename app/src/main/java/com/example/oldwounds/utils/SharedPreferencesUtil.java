package com.example.oldwounds.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create by Politness Chen on 2019/10/19--11:47
 * desc:   SharedPreferences的一个简单的封装工具类
 */
public class SharedPreferencesUtil {

    //如果通用可以把Name也作为值传入，这里只是用来对登录使用，故没有处理
    public static final String NAME = "login";

    public static void putString(Context context,String key,String value) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context,String key,String value) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key,value);
    }
    public static void putInt(Context context,String key,int value) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context context,String key,int value) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,value);
    }

    public static void putBoolean(Context context,String key,boolean value) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key,boolean value) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,value);
    }

    public static void deletShare(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void deletAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
