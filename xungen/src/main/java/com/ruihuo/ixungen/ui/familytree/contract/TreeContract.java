package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.geninterface.ICallBackTree;
import com.ruihuo.ixungen.ui.familytree.fragment.TreeFragment;
import com.ruihuo.ixungen.ui.familytree.model.TreeModel;

/**
 * @author yudonghui
 * @date 2017/10/24
 * @describe May the Buddha bless bug-free!!!
 */
public interface TreeContract {
    interface View extends BaseView {
        void getTreeSuccess(String result);

        void getTreeError(String error);

        void getExpandSuccess(Bundle bundle);

        void getExpandError(String error);

        void getDeleteSuccess(String result);

        void getDeleteError(String error);
    }

    interface Model extends BaseModel {
        void getTreeData(Bundle parameters, Context mContext, ICallBack callBack);

        void getExpandData(Bundle parameters, Context mContext, ICallBackTree callBack);

        void getDeleteData(Bundle parameters, Context mContext, ICallBack callBack);
    }

    abstract class Presenter extends BasePresenter<TreeFragment, TreeModel> {
        public abstract void getTreeData(Bundle parameters, Context mContext);

        public abstract void getExpandData(Bundle parameters, Context mContext);

        public abstract void getDeleteData(Bundle parameters, Context mContext);
    }
}
