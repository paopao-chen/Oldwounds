package com.example.oldwounds.Application;

import android.app.Application;

import com.example.oldwounds.utils.StaticData;

import cn.bmob.v3.Bmob;

/**
 * Create by Politness Chen on 2019/12/7--14:08
 * desc:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bmob
        Bmob.initialize(this, StaticData.BMOB_APP_ID);
    }
}
