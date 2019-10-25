package com.example.oldwounds.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Create by Politness Chen on 2019/10/22--14:22
 * desc:    用于时间的格式转化
 */
public class DateTransUtils {

    private String TAG = this.getClass().getName();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    //将时间戳转换为时间
    public static String stampToDate (String stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(stamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDate(long stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDay(long stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Calendar stampToCalendar(long stamp) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(stamp);
        return calendar;
    }

    //获取今日某时间的时间戳
    public long getTodayStartStamp(int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,second);
        long todayStamp = cal.getTimeInMillis();
        LogUtil.e(TAG, "getTodayStartStamp: " + hour + ":" + minute + ":" + second);
        return todayStamp;
    }

    //获取当日00:00:00的时间戳，东八区则为早上8点
    public static long getZeroClockTimeStamp(long time) {
        long currentStamp = time;
        currentStamp -= currentStamp % DAY_IN_MILLIS;
        return currentStamp;
    }

    //获取最近7天的日期，用于查询这7天的系统数据
    public static ArrayList<String> getSearchDays() {
        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dayList.add(getDateString(i));
        }
        return dayList;
    }

    //获取dayNumber天前，当天的日期字符串
    public static String getDateString(int dayNumber) {
        long time = System.currentTimeMillis() - dayNumber * DAY_IN_MILLIS;
        return dateFormat.format(time);
    }
}
