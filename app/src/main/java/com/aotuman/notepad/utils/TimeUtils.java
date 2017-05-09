package com.aotuman.notepad.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aotuman on 2017/5/9.
 */

public class TimeUtils {
    public static String timeStampToDate(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String timeStampToHour(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static int getYearByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String year = date.substring(0, 4);
        return Integer.parseInt(year);
    }

    public static int getMonthByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    public static int getDayByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }

    public static int getHourByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String hour = date.substring(11, 13);
        return Integer.parseInt(hour);
    }

    public static int getMinuteByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String minute = date.substring(14, 16);
        return Integer.parseInt(minute);
    }

    public static int getSecondByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String second = date.substring(17, 19);
        return Integer.parseInt(second);
    }

    //判断两个时间戳是否为同一天
    public static boolean isTwoTimeStampDayEqual(long firstTimeStamp, long secondTimeStamp){
        if(getYearByTimeStamp(firstTimeStamp) == getYearByTimeStamp(secondTimeStamp) &&
                getMonthByTimeStamp(firstTimeStamp) == getMonthByTimeStamp(secondTimeStamp)
                && getDayByTimeStamp(firstTimeStamp) == getDayByTimeStamp(secondTimeStamp)){
            return true;
        }
        return false;
    }
}
