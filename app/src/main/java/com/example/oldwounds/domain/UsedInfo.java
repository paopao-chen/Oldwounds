package com.example.oldwounds.domain;

import androidx.annotation.Nullable;

/**
 * Create by Politness Chen on 2019/10/22--11:59
 * desc:   (主页面)统计数据---记录每个应用的包名，使用时长和使用次数
 */
public class UsedInfo {

    private int mUsedCount;  //使用的次数
    private long mUsedTime;  //使用的时间
    public String mPackageName;  //包名

    public UsedInfo(int mUsedCount, long mUsedTime, String mPackageName) {
        this.mUsedCount = mUsedCount;
        this.mUsedTime = mUsedTime;
        this.mPackageName = mPackageName;
    }

    public void addCount() {
        mUsedCount ++;
    }

    public int getmUsedCount() {
        return mUsedCount;
    }

    public void setmUsedCount(int mUsedCount) {
        this.mUsedCount = mUsedCount;
    }

    public long getmUsedTime() {
        return mUsedTime;
    }

    public void setmUsedTime(long mUsedTime) {
        this.mUsedTime = mUsedTime;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    @Override
    public int hashCode() {
//        return super.hashCode();
        return (mPackageName + mUsedTime).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
//        return super.equals(o);
        if(o == null) return false;
        if(this == o) return true;
        UsedInfo standardDetail = (UsedInfo)o;
        if( standardDetail.getmPackageName().equals(this.mPackageName) ){
            return true;
        }else {
            return false;
        }
    }
}
