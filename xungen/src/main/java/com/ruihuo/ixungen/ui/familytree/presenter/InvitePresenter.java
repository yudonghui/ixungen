package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.InviteActivity;
import com.ruihuo.ixungen.ui.familytree.contract.InviteContract;
import com.ruihuo.ixungen.ui.familytree.model.InviteModel;

/**
 * @author yudonghui
 * @date 2017/10/28
 * @describe May the Buddha bless bug-free!!!
 */
public class InvitePresenter extends InviteContract.Presenter {
    public InvitePresenter(InviteActivity view) {
        this.mView = view;
        this.mModel = new InviteModel();
        setVM(mView, mModel);
    }

    @Override
    public void getInvitePhoneData(Bundle parameters, Context mContext) {
        mModel.getInvitePhoneData(parameters, mContext, new ICallBack() {
            @Override
            public void onSuccess(String result) {
                mView.getInvitePhoneSuccess(result);
            }

            @Override
            public void onError(String error) {
                mView.getInvitePhoneError(error);
            }
        });
    }
}
