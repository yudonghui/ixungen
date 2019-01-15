package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.ui.familytree.model.StemmaDetailModel;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public interface StemmaDetailContract {
    interface View extends BaseView {
        void getDetailSuccess(String result);

        void getDetailError(String error);

        void deleteSuccess(String result);

        void deleteError(String error);

        void getBuySuccess(String result);

        void getBuyError(String error);

        void getPrivateSuccess(String result);

        void getPrivateError(String error);

        void getPriceSuccess(String result);

        void getPriceError(String error);
    }

    interface Model extends BaseModel {
        void getDetailData(Bundle parameters, Context mContext, ICallBack callBack);

        void getBuyData(Bundle parameters, Context mContext, ICallBack callBack);

        void getPrivateData(Bundle parameters, Context mContext, ICallBack callBack);

        void getPriceData(Bundle parameters, Context mContext, ICallBack callBack);

        void deleteStemma(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<StemmaDetailActivity, StemmaDetailModel> {
        public abstract void getDetailData(Bundle parameters, Context mContext);

        public abstract void getBuyData(Bundle parameters, Context mContext);

        public abstract void getPrivateData(Bundle parameters, Context mContext);

        public abstract void getPriceData(Bundle parameters, Context mContext);

        public abstract void deleteStemma(Bundle parameters, Context mContext);
    }
}
