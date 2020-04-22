package com.example.oldwounds.application;

import android.app.Application;
import android.content.Context;

import com.example.oldwounds.utils.StaticData;

import cn.bmob.v3.Bmob;

/**
 * Create by Politness Chen on 2019/12/7--14:08
 * desc:
 */
public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //初始化Bmob
        Bmob.initialize(mContext, StaticData.BMOB_APP_ID);
    }

    public static Context getContext() {
        return mContext;
    }
}
