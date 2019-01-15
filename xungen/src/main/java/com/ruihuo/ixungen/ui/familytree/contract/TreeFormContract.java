package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.fragment.TreeFormFragment;
import com.ruihuo.ixungen.ui.familytree.model.TreeFormModel;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public interface TreeFormContract {
    interface View extends BaseView {
        void getDataSuccess(String result);

        void getDataError(String error);
    }

    interface Model extends BaseModel {
        void getData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<TreeFormFragment, TreeFormModel> {
        public abstract void getData(Bundle parameters, Context mContext);

    }
}
