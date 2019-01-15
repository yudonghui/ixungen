package com.ruihuo.ixungen.ui.familytree.model;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.ICallBackTree;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.bean.Tree;
import com.ruihuo.ixungen.ui.familytree.contract.TreeContract;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @author yudonghui
 * @date 2017/10/24
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeModel implements TreeContract.Model {
    @Override
    public void getTreeData(Bundle parameters, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        String url = parameters.getString("url");
        parameters.remove("url");
        mHttp.getRSA(url, parameters, new JsonInterface() {
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
    public void getExpandData(final Bundle parameters, Context mContext, final ICallBackTree callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);

        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        String url = parameters.getString("url");
        final Tree tree = (Tree) parameters.getSerializable("tree");
        parameters.remove("tree");
        parameters.remove("url");
        mHttp.getRSA(url, parameters, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Bundle bundle = new Bundle();
                bundle.putSerializable("tree", tree);
                bundle.putString("type", parameters.getString("type"));
                bundle.putString("result", result);
                callBack.onSuccess(bundle);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                callBack.onError(message);
            }
        });
    }

    @Override
    public void getDeleteData(Bundle parameters, Context mContext, final ICallBack callBack) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        mHttp.post(Url.TREE_DELETE, parameters, new JsonInterface() {
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
