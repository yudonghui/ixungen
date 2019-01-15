package com.ruihuo.ixungen.utils;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class ClickUtils {
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isFastClickLong() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
