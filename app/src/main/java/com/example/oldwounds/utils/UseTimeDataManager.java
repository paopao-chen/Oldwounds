package com.example.oldwounds.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.oldwounds.domain.OneTimeDetails;
import com.example.oldwounds.domain.UsedInfo;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.RequiresApi;

/**
 * Create by Politness Chen on 2019/10/22--13:36
 * desc:   用来处理主页的数据
 */
public class UseTimeDataManager {

    private static final String TAG = "UseTimeDataManager";

    private int mDayNum;
    private long mStartTime;
    private long mEndTime;

    //记录从系统中读取的数据
    private ArrayList<UsageEvents.Event> mEventList;
    private ArrayList<UsageEvents.Event> mEventListChecked;
    private ArrayList<UsageStats> mStatsList;
    //主界面数据  时间，次数，应用
    private ArrayList<UsedInfo> mUsedInfoList = new ArrayList<>();
    //记录打开一次应用，使用的activity详情
    private ArrayList<OneTimeDetails> mOneTimeDetailList = new ArrayList<>();
    //记录某一次打开应用的使用情况（查询某一次使用情况的时候，用于界面显示）
    private OneTimeDetails mOneTimeDetails;

    private static UseTimeDataManager useTimeDataManager;
    private Context mContext;
    public UseTimeDataManager(Context context) {
        this.mContext = context;
    }

    public static UseTimeDataManager getInstance(Context context){   //单例,懒汉式
        if (useTimeDataManager == null)
            useTimeDataManager = new UseTimeDataManager(context);
        return useTimeDataManager;
    }

    /**
     * 主要的数据获取函数
     *
     * @param dayNumber   查询若干天前的数据
     * @return int        0 : event usage 均查询到了
     *                    1 : event 未查询到 usage 查询到了
     *                    2 : event usage 均未查询到
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int refreshData(int dayNumber){
        mDayNum = dayNumber;
        mEventList = getEventList(dayNumber);
        mStatsList = getUsageList(dayNumber);

        if(mEventList == null  || mEventList.size() == 0){
//            MsgEventBus.getInstance().post(new MessageEvent("未查到events"));
            LogUtil.e(TAG," UseTimeDataManager-refreshData()   未查到events" );

            if(mStatsList == null  || mStatsList.size() == 0){
//                MsgEventBus.getInstance().post(new MessageEvent("未查到stats"));
                LogUtil.e(TAG," UseTimeDataManager-refreshData()   未查到stats" );
                return 2;
            }
            return 1;
        }

        //获取数据之后，进行数据的处理
        mEventListChecked = getEventListChecked();
        refreshOneTimeDetailList(0);
        refreshUsedInfoList();

//        sendEventBus();
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void refreshUsedInfoList() {
        mUsedInfoList.clear();
        for (int i = 0; i < mStatsList.size(); i++) {
            UsedInfo info = new UsedInfo(0,calculateUseTime(mStatsList.get(i).getPackageName()),mStatsList.get(i).getPackageName());
            mUsedInfoList.add(info);
        }

        for (int i = 0; i < mUsedInfoList.size(); i++) {
            String pkg = mUsedInfoList.get(i).getmPackageName();
            for (int j = 0; j < mOneTimeDetailList.size(); j++) {
                if (pkg.equals(mOneTimeDetailList.get(j).getPkgName())) {
                    mUsedInfoList.get(i).addCount();
                }
            }
        }
    }

    /**
     * 从 startIndex 开始分类event  直至将event分完
     * @param startIndex
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void refreshOneTimeDetailList(int startIndex) {
        if (startIndex == 0) {
            if (mOneTimeDetailList != null) {
                mOneTimeDetailList.clear();  //最初应从0开始，将原本的 mOneTimeDetailList 清除一次,然后开始分类
            }
        }

        long totalTime = 0;
        int usedIndex = 0;
        String pkg = null;
        ArrayList<UsageEvents.Event> list = new ArrayList<>();
        for (int i = startIndex; i < mEventListChecked.size(); i++) {
            if (i == startIndex) { // 查看每次打开第一个Activity的类型值是多少
                if (mEventListChecked.get(i).getEventType() == 2) {   //看看是否是从2开始的（其实是从1开始的）
                    LogUtil.e(TAG,"  refreshOneTimeDetailList()     warning : 每次打开一个app  第一个activity的类型是 2     ");
                }
                pkg = mEventListChecked.get(i).getPackageName();
                list.add(mEventListChecked.get(i));
            } else {
                if (pkg != null) {    //event是根据包名进行排好的，所以只需遍历到最后即可
                    if (pkg.equals(mEventListChecked.get(i).getPackageName())) {
                        list.add(mEventListChecked.get(i));
                        if (i == mEventListChecked.size() - 1) {
                            usedIndex = i;
                        }
                    } else {
                        usedIndex = i;
                        break;
                    }
                }
            }
        }
        checkEventList(list);  // 检查数据是否异常
        for(int i = 1 ; i < list.size() ; i += 2){
            if (list.get(i).getEventType() == 2 && list.get( i - 1).getEventType() == 1) {
                //event记录的时间戳
                totalTime += (list.get(i).getTimeStamp() - list.get( i - 1).getTimeStamp());
            }
        }

        OneTimeDetails oneTimeDetails = new OneTimeDetails(pkg,totalTime,list);
        mOneTimeDetailList.add(oneTimeDetails);

        if(usedIndex < mEventListChecked.size() - 1){
            refreshOneTimeDetailList(usedIndex);    // 递归调用函数，进行分类
        }else {
            LogUtil.e(TAG,"  refreshOneTimeDetailList()     已经将  mEventListChecked 分类完毕   ");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkEventList(ArrayList<UsageEvents.Event> list) {
        boolean isCheckAgain = false;
        for (int i = 0; i < list.size() - 1 ; i++) {
            if (list.get(i).getClassName().equals(list.get(i+1).getClassName())) {
                if (list.get(i).getEventType() != 1) {
                    Log.i(UseTimeDataManager.TAG,"   EventList 出错  ： "+list.get(i).getPackageName() +"  "+ DateUtils.formatSameDayTime(list.get(i).getTimeStamp(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM).toString());
                    list.remove(i);
                    isCheckAgain = true;
                    break;
                }
                if (list.get(i+1).getEventType() != 2) {
                    Log.i(UseTimeDataManager.TAG,"   EventList 出错  ： "+list.get(i).getPackageName() +"  "+ DateUtils.formatSameDayTime(list.get(i).getTimeStamp(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM).toString());
                    list.remove(i);
                    isCheckAgain = true;
                    break;
                }
            } else {
                //i和i+1的className对不上，则删除第i个数据，重新检查
                list.remove(i);
                isCheckAgain = true;
                break;
            }
        }
        if (isCheckAgain) {
            checkEventList(list);
        }
    }

    /**
     * 得到该程序每次的详情（主页点击）
     * @param pkg
     * @return
     */
    public ArrayList<OneTimeDetails> getPkgOneTimeDetailList(String pkg) {
        if("all".equals(pkg)){
            return mOneTimeDetailList;
        }

        ArrayList<OneTimeDetails> list = new ArrayList<>();
        if (mOneTimeDetailList != null && mOneTimeDetailList.size() > 0) {
            for (int i = 0; i < mOneTimeDetailList.size() ; i++) {
                if (mOneTimeDetailList.get(i).getPkgName().equals(pkg)) {
                    list.add(mOneTimeDetailList.get(i));
                }
            }
        }
        return list;
    }

    /**
     * 仅保留 event 中 type 为 1或者2 （正确的）
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageEvents.Event> getEventListChecked() {
        ArrayList<UsageEvents.Event> list = new ArrayList<>();
        for (int i = 0; i < mEventList.size(); i++) {
            if (mEventList.get(i).getEventType() == 1 || mEventList.get(i).getEventType() == 2) {
                list.add(mEventList.get(i));
            }
        }
        return list;
    }

    /**
     * 根据使用时间的长短进行排序（需要改成快排）
     * @return
     */
    public ArrayList<UsedInfo> getmPackageInfoListOrderByTime() {
        LogUtil.e(TAG,"  getmPackageInfoListOrderByTime : "+mUsedInfoList.size());
        for (int n = 0; n < mUsedInfoList.size() ; n++) {
            for (int m = n+1; m < mUsedInfoList.size(); m++) {   //选择排序（不稳定且复杂度高）
                if(mUsedInfoList.get(n).getmUsedTime() < mUsedInfoList.get(m).getmUsedTime()){
                    UsedInfo temp = mUsedInfoList.get(n);
                    mUsedInfoList.set(n,mUsedInfoList.get(m));
                    mUsedInfoList.set(m,temp);
                }
            }
        }
//        quickSort(mUsedInfoList,0,mUsedInfoList.size());
        return mUsedInfoList;
    }

    /**
     * 根据选择的时间获取数据
     * @param dayNumber
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageEvents.Event>  getEventList(int dayNumber) {
        long endTime = 0,startTime = 0;
        if(dayNumber == 0 ){
            endTime = System.currentTimeMillis();
            startTime = DateTransUtils.getZeroClockTimeStamp(endTime);
        }else {
            //00:00:00 ------> 23:59:59
            endTime = DateTransUtils.getZeroClockTimeStamp(System.currentTimeMillis() - (dayNumber-1) * DateTransUtils.DAY_IN_MILLIS) - 1;
            startTime = endTime - DateTransUtils.DAY_IN_MILLIS + 1;
        }
        LogUtil.e(TAG,"getEventList");
        return getEventList(mContext,startTime,endTime);
    }

    /**
     * 根据选择的时间获取数据
     * @param dayNumber
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageStats> getUsageList(int dayNumber) {
        long endTime = 0,startTime = 0;
        if(dayNumber == 0 ){
            endTime = System.currentTimeMillis();
            startTime = DateTransUtils.getZeroClockTimeStamp(endTime);
        }else {
            endTime = DateTransUtils.getZeroClockTimeStamp(System.currentTimeMillis() - (dayNumber-1) * DateTransUtils.DAY_IN_MILLIS )-1;
            startTime = endTime - DateTransUtils.DAY_IN_MILLIS + 1;
        }
        LogUtil.e(TAG,"getUsageList");
        return getUsageList(mContext,startTime,endTime);
    }

    //获取系统记录的详细的各个activity的使用情况   Event
    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<UsageEvents.Event> getEventList(Context context, long startTime, long endTime) {
        ArrayList<UsageEvents.Event> mEventList = new ArrayList<>();

        UsageStatsManager mUsmManger = (UsageStatsManager) context.getSystemService("usagestats");
        UsageEvents events = mUsmManger.queryEvents(startTime,endTime);

        while (events.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            events.getNextEvent(e);
            if (e != null && (e.getEventType() == 1 || e.getEventType() == 2)) {
                mEventList.add(e);
            }
        }
        return mEventList;
    }

    //获取系统统计信息   系统中每一个应用的使用情况
    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<UsageStats> getUsageList(Context context, long startTime, long endTime) {

        ArrayList<UsageStats> list = new ArrayList<>();

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        Map<String, UsageStats> map = mUsmManager.queryAndAggregateUsageStats(startTime, endTime);
        for (Map.Entry<String, UsageStats> entry : map.entrySet()) {
            UsageStats stats = entry.getValue();
            if(stats.getTotalTimeInForeground() > 0){
                list.add(stats);
            }
        }

        return list;
    }


    public ArrayList<UsedInfo> getPkgInfoListFromEventList(){
        return mUsedInfoList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<UsedInfo> getPkgInfoListFromUsageList() throws IllegalAccessException {
        ArrayList<UsedInfo> result = new ArrayList<>();

        if (mStatsList != null && mStatsList.size() > 0) {
            for (int i = 0 ; i < mStatsList.size() ; i++) {
                result.add(new UsedInfo(getLaunchCount(mStatsList.get(i)),mStatsList.get(i).getTotalTimeInForeground(),mStatsList.get(i).getPackageName()));
            }
        }
        return result;
    }

    /**
     * 没有提供api直接获取，只能利用反射，获取UsageStats中统计的应用使用次数
     * @param usageStats
     * @return
     * @throws IllegalAccessException
     */
    private int getLaunchCount(UsageStats usageStats) throws IllegalAccessException {
        Field field = null;
        try {
            //对于一个应用，被用户使用的次数
            field = usageStats.getClass().getDeclaredField("mLaunchCount");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return (int) field.get(usageStats);
    }

    /**
     * 根据event计算使用时间
     * @param pkg
     * @return
     */
    public long calculateUseTime(String pkg) {
        long useTime = 0;
        for (int i = 0 ; i < mOneTimeDetailList.size() ; i++) {
            if (mOneTimeDetailList.get(i).getPkgName().equals(pkg)) {
                useTime += mOneTimeDetailList.get(i).getUseTime();
//                useTime = mOneTimeDetailList.get(i).getUseTime();
            }
        }
        LogUtil.e(TAG,"  calculateUseTime : " + useTime);
        return useTime;
    }

    /**
     * 得到UsageStats并通过它获得使用时长
     * @param pkg
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UsageStats getUsageStats(String pkg) {
        for (int i = 0; i < mStatsList.size(); i++) {
            if (mStatsList.get(i).getPackageName().equals(pkg)) {
                return mStatsList.get(i);
            }
        }
        return null;
    }

    public int getmDayNum() {
        return mDayNum;
    }

    public void setmDayNum(int mDayNum) {
        this.mDayNum = mDayNum;
    }

    public ArrayList<OneTimeDetails> getmOneTimeDetailList() {
        return mOneTimeDetailList;
    }

    public OneTimeDetails getmOneTimeDetails() {
        return mOneTimeDetails;
    }

    public void setmOneTimeDetails(OneTimeDetails mOneTimeDetails) {
        this.mOneTimeDetails = mOneTimeDetails;
    }
}
