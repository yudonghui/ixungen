package com.ruihuo.ixungen.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yudonghui
 * @date 2017/3/28
 * @describe May the Buddha bless bug-free!!!
 */
public class DateFormatUtils {
    public static String longToDate(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String longToDateS(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String longToDateM(String date) {
        if (TextUtils.isEmpty(date)) return "";
        long date1 = Long.parseLong(date + "000");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date1);
    }

    public static long StringToLong(String date) {
        if (TextUtils.isEmpty(date)) return 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = simpleDateFormat.parse(date);
            long time = parse.getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String stringToDateMim(String date) {
        if (TextUtils.isEmpty(date)) return "";
        long date1 = Long.parseLong(date + "000");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date1);
    }
}
