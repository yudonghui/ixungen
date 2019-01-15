package com.ruihuo.ixungen.ui.main.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.main.model.XSJPFragmentModel;

/**
 * @author yudonghui
 * @date 2017/10/18
 * @describe May the Buddha bless bug-free!!!
 */
public interface XSJPFragmentContract {
    interface View extends BaseView {
        void getSearchSuccess(String result);

        void getSearchError(String error);
    }

    interface Model extends BaseModel {
        //获取搜索的结果
        void getSearchData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<View, XSJPFragmentModel> {
        public abstract void getSearchData(Bundle parameters, Context mContext);
    }
}
