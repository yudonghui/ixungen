package com.ruihuo.ixungen.ui.familytree.model;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.contract.TreeDetailContract;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeDetailModel implements TreeDetailContract.Model {
    @Override
    public void getInfoData(Bundle parameters, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        instance.get(Url.STEMMA_MEMEBER_APP_INFO, parameters, new JsonInterface() {
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

    @Override
    public void getSystemInfoData(Bundle parameters, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        instance.getRSA(Url.STEMMA_MEMEBER_SYSTME_INFO, parameters, new JsonInterface() {
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
