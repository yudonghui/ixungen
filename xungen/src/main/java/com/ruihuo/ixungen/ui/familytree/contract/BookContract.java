package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.fragment.BookFragment;
import com.ruihuo.ixungen.ui.familytree.model.BookModel;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public interface BookContract {
    interface View extends BaseView {
        void getCatalogueSuccess(String result);

        void getCatalogueError(String error);

        void getBookDetailSuccess(String result);

        void getBookDetailError(String error);

        void getDataSuccess(String result);

        void getDataError(String error);

    }

    interface Model extends BaseModel {
        void getData(Bundle parameters, Context mContext, ICallBack callBack);

        void getCatalogueData(Bundle parameters, Context mContext, ICallBack callBack);

        void getBookDetailData(Bundle parameters, Context mContext, ICallBack callBack);

    }

    abstract class Presenter extends BasePresenter<BookFragment, BookModel> {
        public abstract void getData(Bundle parameters, Context mContext);

        public abstract void getCatalogueData(Bundle parameters, Context mContext);

        public abstract void getBookDetailData(Bundle parameters, Context mContext);

    }
}
