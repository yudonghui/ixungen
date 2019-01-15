package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/5.
 */
public class ToastUtils {
    public static void toast(Context mContext,String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
    public static void toastLong(Context mContext,String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }
}
