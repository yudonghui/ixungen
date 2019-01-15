package com.ruihuo.ixungen.ui.familytree.model;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.contract.TreeAddContract;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeAddModel implements TreeAddContract.Model {
    @Override
    public void getCommitData(Bundle parameters, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        instance.post(Url.TREE_ADD, parameters, new JsonInterface() {
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
