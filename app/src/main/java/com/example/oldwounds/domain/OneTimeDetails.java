package com.example.oldwounds.domain;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.os.Build;

import com.example.oldwounds.utils.DateTransUtils;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

/**
 * Create by Politness Chen on 2019/10/22--17:41
 * desc:   记录打开一次应用的时候，其中的详情，包括这次打开的应用名，使用时长
 */
public class OneTimeDetails {
    private String pkgName;
    private long useTime;
    private ArrayList<UsageEvents.Event> OneTimeDetailEventList;

    public OneTimeDetails(String pkgName, long useTime, ArrayList<UsageEvents.Event> oneTimeDetailEventList) {
        this.pkgName = pkgName;
        this.useTime = useTime;
        OneTimeDetailEventList = oneTimeDetailEventList;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public ArrayList<UsageEvents.Event> getOneTimeDetailEventList() {
        return OneTimeDetailEventList;
    }

    public void setOneTimeDetailEventList(ArrayList<UsageEvents.Event> oneTimeDetailEventList) {
        OneTimeDetailEventList = oneTimeDetailEventList;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getStartTime() {
        String startTime = null;
        if (OneTimeDetailEventList.size() > 0) {
            startTime = DateTransUtils.stampToDate(OneTimeDetailEventList.get(0).getTimeStamp());
        }
        return startTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String getStopTime() {
        String stopTime = null;
        if (OneTimeDetailEventList.size() > 0) {
            stopTime = DateTransUtils.stampToDate(OneTimeDetailEventList.get(OneTimeDetailEventList.size() - 1).getTimeStamp());
        }
        return stopTime;
    }
}
