package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.InviteActivity;
import com.ruihuo.ixungen.ui.familytree.model.InviteModel;

/**
 * @author yudonghui
 * @date 2017/10/28
 * @describe May the Buddha bless bug-free!!!
 */
public interface InviteContract  {
    interface View extends BaseView {
        void getInvitePhoneSuccess(String result);

        void getInvitePhoneError(String error);
    }

    interface Model extends BaseModel {
        void getInvitePhoneData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<InviteActivity, InviteModel> {
        public abstract void getInvitePhoneData(Bundle parameters, Context mContext);
    }
}
