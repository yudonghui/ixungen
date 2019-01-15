package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.EditorStemmaActivity;
import com.ruihuo.ixungen.ui.familytree.contract.EditorStemmaContract;
import com.ruihuo.ixungen.ui.familytree.model.EditorStemmaModel;

/**
 * @author yudonghui
 * @date 2017/11/6
 * @describe May the Buddha bless bug-free!!!
 */
public class EditorStemmaPresenter extends EditorStemmaContract.Presenter {
    public EditorStemmaPresenter(EditorStemmaActivity view) {
        this.mView = view;
        this.mModel = new EditorStemmaModel();
        setVM(mView, mModel);
    }

    @Override
    public void commitEditor(Bundle parameters, Context mContext) {
        mModel.commitEditor(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getEditorSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getEditorError(error);
            }
        });
    }
}
