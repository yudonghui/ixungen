package com.ruihuo.ixungen.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.ruihuo.ixungen.XunGenApp;

import java.io.File;
import java.lang.reflect.Field;

public class DisplayUtilX {
    public static int dip2px(double dipValue) {
        float m = XunGenApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    public static int px2dip(double pxValue) {
        float m = XunGenApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / m + 0.5f);
    }

    //sp转px
    public static int Sp2Px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, XunGenApp.getInstance().getResources().getDisplayMetrics());
    }

    //px转sp
    public static int Px2Sp(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, XunGenApp.getInstance().getResources().getDisplayMetrics());
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 沉浸式状态栏
     */
    public static void setWindowStatusBarStatus(Activity activity, boolean flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            Window window = activity.getWindow();
            if (flag) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            }
            //透明导航栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
    }

    /**
     * 长的为宽度
     * <功能详细描述>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int screenWidthPx() {
        int widthPx = XunGenApp.getInstance().getResources().getDisplayMetrics().widthPixels;
        int heightPx = XunGenApp.getInstance().getResources().getDisplayMetrics().heightPixels;
        return widthPx > heightPx ? widthPx : heightPx;
    }

    /**
     * 小的为高度
     *
     * @return
     */
    public static int screenHeightPx() {
        int widthPx = XunGenApp.getInstance().getResources().getDisplayMetrics().widthPixels;
        int heightPx = XunGenApp.getInstance().getResources().getDisplayMetrics().heightPixels;
        return widthPx > heightPx ? heightPx : widthPx;
    }

    public static int getDisplayHeight() {
        return XunGenApp.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getDisplayWidth() {
        return XunGenApp.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取通知栏高度
     *
     * @return 通知栏高度
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = XunGenApp.getInstance().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获得可用存储空间
     *
     * @return 可用存储空间（单位b）
     */
    public static long getFreeSpace() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;//区块的大小
        long totalBlocks;//区块总数
        long availableBlocks;//可用区块的数量
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
            availableBlocks = stat.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }
}
