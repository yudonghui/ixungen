package com.ruihuo.ixungen.ui.familytree.contract;

import android.content.Context;
import android.os.Bundle;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.geninterface.ICallBack;
import com.ruihuo.ixungen.ui.familytree.activity.EditorStemmaActivity;
import com.ruihuo.ixungen.ui.familytree.model.EditorStemmaModel;

/**
 * @author yudonghui
 * @date 2017/11/6
 * @describe May the Buddha bless bug-free!!!
 */
public interface EditorStemmaContract {
    interface View extends BaseView {
        void getEditorSuccess(String result);

        void getEditorError(String error);
    }

    interface Model extends BaseModel {
        void commitEditor(Bundle parameters, Context mContext, ICallBack callBack);

    }

    abstract class Presenter extends BasePresenter<EditorStemmaActivity, EditorStemmaModel> {
        public abstract void commitEditor(Bundle parameters, Context mContext);
    }
}
