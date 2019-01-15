package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.TreeAddActivity;
import com.ruihuo.ixungen.ui.familytree.model.TreeAddModel;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public interface TreeAddContract {
    interface View extends BaseView {
        void getCommitSuccess(String result);

        void getCommitError(String error);
    }

    interface Model extends BaseModel {
        void getCommitData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<TreeAddActivity, TreeAddModel> {
        public abstract void getCommitData(Bundle parameters, Context mContext);
    }
}
