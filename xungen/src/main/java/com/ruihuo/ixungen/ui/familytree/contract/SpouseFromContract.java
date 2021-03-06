package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.SpouseFromActivity;
import com.ruihuo.ixungen.ui.familytree.model.SpouseFromModel;

/**
 * @author yudonghui
 * @date 2017/11/6
 * @describe May the Buddha bless bug-free!!!
 */
public interface SpouseFromContract  {
    interface View extends BaseView {
        void getDataSuccess(String result);

        void getDataError(String error);
    }

    interface Model extends BaseModel {
        void getData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<SpouseFromActivity, SpouseFromModel> {
        public abstract void getData(Bundle parameters, Context mContext);
    }
}
