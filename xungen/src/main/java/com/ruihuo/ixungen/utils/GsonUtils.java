package com.ruihuo.ixungen.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class GsonUtils {
    public static Gson gson;
    public static Gson getGson(){
        if(gson==null){
            gson=new Gson();
        }
        return gson;
    }
}
