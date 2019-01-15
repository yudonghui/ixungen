package com.ruihuo.ixungen.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class LogUtils {
    public static final int VERBOSE=1;
    public static final int DEBUG =2 ;
    public static final int INFO=3;
    public static final int WARN=4;
    public static final int ERROR=5;
    public static final int NOTHING=6;
    public static final int LEVEL=VERBOSE;
    public static  boolean isLog=true;
    public static void init(boolean log){
        isLog=log;
    }
    public static void v(String tag,String msg){
      if(tag!=null&&msg!=null&&LEVEL<=VERBOSE&&isLog){
          Log.v(tag,msg);
      }
    }
    public static void d(String tag,String msg){
        if(tag!=null&&msg!=null&&LEVEL<=DEBUG&&isLog){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(tag!=null&&msg!=null&&LEVEL<=INFO&&isLog){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if(tag!=null&&msg!=null&&LEVEL<=WARN&&isLog){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if(tag!=null&&msg!=null&&isLog){
            if(LEVEL<=ERROR){
                Log.e(tag,msg);
            }
        }

    }
}
