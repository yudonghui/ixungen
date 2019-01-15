package com.ruihuo.ixungen.ui.main.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.main.contract.XSJPBookContract;
import com.ruihuo.ixungen.ui.main.fragment.XSJPBookFragment;
import com.ruihuo.ixungen.ui.main.model.XSJPBookModel;

/**
 * @author yudonghui
 * @date 2017/10/26
 * @describe May the Buddha bless bug-free!!!
 */
public class XSJPBookPresenter extends XSJPBookContract.Prestener {
    public XSJPBookPresenter(XSJPBookFragment view) {
        this.mView = view;
        this.mModel = new XSJPBookModel();
        setVM(mView, mModel);
    }

    @Override
    public void getSystemData(Bundle params, Context mContext) {
        mModel.getSystemData(params, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getSystemSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getSystemError(error);
            }
        });
    }

    @Override
    public void getMyData(Bundle params, Context mContext) {
        mModel.getMyData(params, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getMySuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getMyError(error);
            }
        });
    }
}
