package com.ruihuo.ixungen.utils;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class DateFormtUtils {
    public static  int[] obtainTime(long time){
        int[] times = new int[4];
        int i1 = (int) (time / 1000);
        int i2=i1/60;
        int i3=i2/60;
        int i4=i3/24;
        int second=i1%60;
        int minute=i2%60;
        int hour=i3%24;
        int day=i4;
        times[0]=day;
        times[1] = hour;
        times[2] = minute;
        times[3] = second;
        return times;
    }
}
