package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.contract.BookContract;
import com.ruihuo.ixungen.ui.familytree.fragment.BookFragment;
import com.ruihuo.ixungen.ui.familytree.model.BookModel;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public class BookPresenter extends BookContract.Presenter {
    public BookPresenter(BookFragment view) {
        this.mView = view;
        this.mModel = new BookModel();
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

    @Override
    public void getCatalogueData(Bundle parameters, Context mContext) {
        mModel.getCatalogueData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getCatalogueSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getCatalogueError(error);
            }
        });
    }

    @Override
    public void getBookDetailData(Bundle parameters, Context mContext) {
        mModel.getBookDetailData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getBookDetailSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getBookDetailError(error);
            }
        });
    }

}
