package com.ruihuo.ixungen.geninterface;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public interface HttpInterface {
    void jsonByUrl(String url, JsonInterface mJsonInterface);

    void post(String url, Bundle params, JsonInterface mJsonInterface);

    void get(String url, Bundle params, JsonInterface mJsonInterface);

    void getW(String url, Bundle params, JsonInterface mJsonInterface);

    void getRSA(String url, Bundle params, JsonInterface mJsonInterface);

    void updateFile(String url, Context mContext, JsonInterface mJsonInterface);
}
