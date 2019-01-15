package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.TreeDetailActivity;
import com.ruihuo.ixungen.ui.familytree.contract.TreeDetailContract;
import com.ruihuo.ixungen.ui.familytree.model.TreeDetailModel;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeDetailPresenter extends TreeDetailContract.Presenter {
    public TreeDetailPresenter(TreeDetailActivity view) {
        this.mView = view;
        this.mModel = new TreeDetailModel();
        setVM(mView, mModel);
    }

    @Override
    public void getInfoData(Bundle parameters, Context mContext) {
        mModel.getInfoData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getInfoSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getInfoError(error);
            }
        });
    }

    @Override
    public void getSystemInfoData(Bundle parameters, Context mContext) {
        mModel.getSystemInfoData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getSystemInfoSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getSystemInfoError(error);
            }
        });
    }
}
