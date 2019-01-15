package com.ruihuo.ixungen.common.friends.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class ScreenUtils {

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpCeilInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    /**
     * <p>通过getWindowManager获取屏幕高度</p><br/>
     *
     * @param activity
     * @return
     * @author lvyadong
     * @since 1.0
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * <p>通过Resources获取屏幕高度</p><br/>
     *
     * @param context
     * @return
     * @author lvyadong
     * @since 1.0
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * <p>通过getWindowManager获取屏幕宽度</p><br/>
     *
     * @param activity
     * @return
     * @author lvyadong
     * @since 1.0
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * <p>通过Resources获取屏幕宽度</p><br/>
     *
     * @param context
     * @return
     * @author lvyadong
     * @since 1.0
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取view在父容器中的坐标位置
     *
     * @param view 待测量的view
     * @return
     */
    public static int[] getLocationInWindow(View view) {
        int[] point = new int[2];
        view.getLocationInWindow(point);
        return point;
    }

    /**
     * 获取view在屏幕中的坐标位置(包括了通知栏)
     *
     * @param view 待测量的view
     * @return
     */
    public static int[] getLocationOnScreen(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return point;
    }
}
