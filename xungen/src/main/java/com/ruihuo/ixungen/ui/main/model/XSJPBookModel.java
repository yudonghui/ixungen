package com.ruihuo.ixungen.ui.main.model;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.main.contract.XSJPBookContract;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @author yudonghui
 * @date 2017/10/26
 * @describe May the Buddha bless bug-free!!!
 */
public class XSJPBookModel implements XSJPBookContract.Model {
    @Override
    public void getSystemData(Bundle params, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        //网络请求
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        mHttp.getRSA(Url.CLANSTEMMA_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                callBack.onSuccess(result);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                callBack.onSuccess(message);
            }
        });
    }

    @Override
    public void getMyData(Bundle params, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        mHttp.get(Url.GLEAN_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                callBack.onSuccess(result);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                callBack.onError(message);
            }
        });
    }
}
