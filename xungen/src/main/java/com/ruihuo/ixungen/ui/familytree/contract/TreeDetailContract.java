package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.TreeDetailActivity;
import com.ruihuo.ixungen.ui.familytree.model.TreeDetailModel;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public interface TreeDetailContract {
    interface View extends BaseView {
        void getSystemInfoSuccess(String result);

        void getSystemInfoError(String error);

        void getInfoSuccess(String result);

        void getInfoError(String error);
    }

    interface Model extends BaseModel {
        void getInfoData(Bundle parameters, Context mContext, ICallBack callBack);

        void getSystemInfoData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<TreeDetailActivity, TreeDetailModel> {
        public abstract void getInfoData(Bundle parameters, Context mContext);

        public abstract void getSystemInfoData(Bundle parameters, Context mContext);
    }
}
