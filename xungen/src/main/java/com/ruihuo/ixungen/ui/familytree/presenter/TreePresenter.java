package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.ICallBackTree;
import com.ruihuo.ixungen.ui.familytree.contract.TreeContract;
import com.ruihuo.ixungen.ui.familytree.fragment.TreeFragment;
import com.ruihuo.ixungen.ui.familytree.model.TreeModel;

/**
 * @author yudonghui
 * @date 2017/10/24
 * @describe May the Buddha bless bug-free!!!
 */
public class TreePresenter extends TreeContract.Presenter {
    public TreePresenter(TreeFragment view) {
        this.mView = view;
        this.mModel = new TreeModel();
        setVM(mView, mModel);
    }

    @Override
    public void getTreeData(Bundle parameters, Context mContext) {
        mModel.getTreeData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getTreeSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getTreeError(error);
            }
        });
    }

    @Override
    public void getExpandData(Bundle parameters, Context mContext) {
        mModel.getExpandData(parameters, mContext, new ICallBackTree() {
            @Override
            public void onSuccess(Bundle bundle) {
                mView.getExpandSuccess(bundle);
            }

            @Override
            public void onError(String error) {
                mView.getExpandError(error);
            }
        });
    }

    @Override
    public void getDeleteData(Bundle parameters, Context mContext) {
        mModel.getDeleteData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getDeleteSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getDeleteError(error);
            }
        });
    }


}
