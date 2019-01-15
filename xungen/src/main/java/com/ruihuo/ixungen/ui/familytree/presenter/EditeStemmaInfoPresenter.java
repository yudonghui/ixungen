package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.EditeStemmaInfoActivity;
import com.ruihuo.ixungen.ui.familytree.contract.EditeStemmaInfoContract;
import com.ruihuo.ixungen.ui.familytree.model.EditeStemmaInfoModel;

/**
 * @author yudonghui
 * @date 2017/11/3
 * @describe May the Buddha bless bug-free!!!
 */
public class EditeStemmaInfoPresenter extends EditeStemmaInfoContract.Presenter {
    public EditeStemmaInfoPresenter(EditeStemmaInfoActivity view) {
        this.mView = view;
        this.mModel = new EditeStemmaInfoModel();
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
