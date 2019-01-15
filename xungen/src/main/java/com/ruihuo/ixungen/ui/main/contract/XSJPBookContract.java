package com.ruihuo.ixungen.ui.main.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.main.fragment.XSJPBookFragment;
import com.ruihuo.ixungen.ui.main.model.XSJPBookModel;

/**
 * @author yudonghui
 * @date 2017/10/26
 * @describe May the Buddha bless bug-free!!!
 */
public interface XSJPBookContract {
    interface View extends BaseView{
        void getSystemSuccess(String result);
        void getSystemError(String error);
        void getMySuccess(String result);
        void getMyError(String error);
    }
    interface Model extends BaseModel{
        void getSystemData(Bundle params, Context mContext, ICallBack callBack);
        void getMyData(Bundle params, Context mContext, ICallBack callBack);
    }
    abstract class Prestener extends BasePresenter<XSJPBookFragment,XSJPBookModel>{
        public abstract void getSystemData(Bundle params, Context mContext);
        public abstract void getMyData(Bundle params, Context mContext);
    }
}
