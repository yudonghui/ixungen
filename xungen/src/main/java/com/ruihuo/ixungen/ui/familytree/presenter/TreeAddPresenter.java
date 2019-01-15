package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.TreeAddActivity;
import com.ruihuo.ixungen.ui.familytree.contract.TreeAddContract;
import com.ruihuo.ixungen.ui.familytree.model.TreeAddModel;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeAddPresenter extends TreeAddContract.Presenter {
    public TreeAddPresenter(TreeAddActivity view) {
        this.mView = view;
        this.mModel = new TreeAddModel();
        setVM(mView, mModel);
    }

    @Override
    public void getCommitData(Bundle parameters, Context mContext) {
        mModel.getCommitData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getCommitSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getCommitError(error);
            }
        });
    }
}
