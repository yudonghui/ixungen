package com.ruihuo.ixungen.ui.main.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.main.contract.XSJPFragmentContract;
import com.ruihuo.ixungen.ui.main.model.XSJPFragmentModel;

/**
 * @author yudonghui
 * @date 2017/10/18
 * @describe May the Buddha bless bug-free!!!
 */
public class XSJPFragmentPresenter  extends XSJPFragmentContract.Presenter {
    public XSJPFragmentPresenter(XSJPFragmentContract.View view) {
        this.mView = view;
        this.mModel = new XSJPFragmentModel();
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

