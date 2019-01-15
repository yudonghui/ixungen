package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.ui.familytree.contract.StemmaDetailContract;
import com.ruihuo.ixungen.ui.familytree.model.StemmaDetailModel;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public class StemmaDetailPresenter extends StemmaDetailContract.Presenter {
    public StemmaDetailPresenter(StemmaDetailActivity view) {
        this.mView = view;
        this.mModel = new StemmaDetailModel();
        setVM(mView, mModel);

    }

    @Override
    public void getDetailData(Bundle parameters, Context mContext) {
        mModel.getDetailData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getDetailSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getDetailError(error);
            }
        });
    }

    @Override
    public void getBuyData(Bundle parameters, Context mContext) {
        mModel.getBuyData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getBuySuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getBuyError(error);
            }
        });
    }

    @Override
    public void getPrivateData(Bundle parameters, Context mContext) {
        mModel.getPrivateData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getPrivateSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getPrivateError(error);
            }
        });
    }

    @Override
    public void getPriceData(Bundle parameters, Context mContext) {
        mModel.getPriceData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getPriceSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getPriceError(error);
            }
        });
    }

    @Override
    public void deleteStemma(Bundle parameters, Context mContext) {
        mModel.deleteStemma(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.deleteSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.deleteError(error);
            }
        });
    }
}
