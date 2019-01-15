package com.ruihuo.ixungen.ui.main.model;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.main.contract.XSJPFragmentContract;
import com.ruihuo.ixungen.utils.HttpUtilsManager;

/**
 * @author yudonghui
 * @date 2017/10/18
 * @describe May the Buddha bless bug-free!!!
 */
public class XSJPFragmentModel implements XSJPFragmentContract.Model {
    @Override
    public void getSearchData(Bundle parameters, Context mContext, final ICallBack callBack) {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        parameters.putString("flagSelf", "true");
        mHttp.getRSA(Url.SURNAME_CLAN, parameters, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.onError(message);
            }
        });
    }
}
