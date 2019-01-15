package com.ruihuo.ixungen.utils;

import android.content.Context;

import com.ruihuo.ixungen.geninterface.HttpInterface;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class HttpUtilsManager {
    public static HttpInterface getInstance(Context mContext){
        return new HttpUtils(mContext);
    }
}
