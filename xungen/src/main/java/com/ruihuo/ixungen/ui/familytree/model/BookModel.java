package com.ruihuo.ixungen.ui.familytree.model;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.contract.BookContract;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public class BookModel implements BookContract.Model {
    @Override
    public void getData(Bundle parameters, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        instance.get(Url.TREE_ADD, parameters, new JsonInterface() {
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
    public void getCatalogueData(Bundle parameters, Context mContext, final ICallBack callBack) {
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        String url = parameters.getString("url");
        parameters.remove("url");
        instance.getRSA(url, parameters, new JsonInterface() {
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

    @Override
    public void getBookDetailData(Bundle parameters, Context mContext, final ICallBack callBack) {
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        instance.get(Url.STEMMA_MEMEBER_APP_INFO, parameters, new JsonInterface() {
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
