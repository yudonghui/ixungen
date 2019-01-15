package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.SpouseFromActivity;
import com.ruihuo.ixungen.ui.familytree.contract.SpouseFromContract;
import com.ruihuo.ixungen.ui.familytree.model.SpouseFromModel;

/**
 * @author yudonghui
 * @date 2017/11/6
 * @describe May the Buddha bless bug-free!!!
 */
public class SpouseFromPresenter extends SpouseFromContract.Presenter {
    public SpouseFromPresenter(SpouseFromActivity view) {
        this.mView = view;
        this.mModel = new SpouseFromModel();
        setVM(mView, mModel);
    }

    @Override
    public void getData(Bundle parameters, Context mContext) {
        mModel.getData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getDataSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getDataError(error);
            }
        });
    }
}
