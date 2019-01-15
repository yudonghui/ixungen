package com.ruihuo.ixungen.ui.familytree.presenter;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.contract.TreeFormContract;
import com.ruihuo.ixungen.ui.familytree.fragment.TreeFormFragment;
import com.ruihuo.ixungen.ui.familytree.model.TreeFormModel;
import com.ruihuo.ixungen.utils.ReaderFileUtils;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeFormPresenter extends TreeFormContract.Presenter {
    public TreeFormPresenter(TreeFormFragment view) {
        this.mView = view;
        this.mModel = new TreeFormModel();
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
