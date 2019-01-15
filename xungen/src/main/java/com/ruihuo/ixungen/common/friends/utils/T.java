package com.ruihuo.ixungen.common.friends.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @Author: duke
 * @DateTime: 2016-08-12 17:34
 * @Description:
 */
public class T {
    //单例模式
    private static Toast toast;

    public static void show(Context context, String msg) {
        show(context, msg, Gravity.BOTTOM);
    }

    public static void show(Context context, String msg, int gravity) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }
}