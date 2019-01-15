package com.ruihuo.ixungen.ui.main.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.main.activity.XSJPActivity;
import com.ruihuo.ixungen.ui.main.contract.XSJPContract;
import com.ruihuo.ixungen.ui.main.model.XSJPModel;

/**
 * @author yudonghui
 * @date 2017/10/18
 * @describe May the Buddha bless bug-free!!!
 */
public class XSJPPresenter extends XSJPContract.Presenter {
    public XSJPPresenter(XSJPActivity view) {
        this.mView = view;
        this.mModel = new XSJPModel();
        setVM(mView, mModel);
    }

    @Override
    public void getSearchData(Bundle parameters, Context mContext) {
        mModel.getSearchData(parameters,mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getSearchSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getSearchError(error);
            }
        });
    }
}
