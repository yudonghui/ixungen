package com.ruihuo.ixungen.base;

/**
 * @author yudonghui
 * @date 2017/9/9
 * @describe May the Buddha bless bug-free!!!
 */
public class BasePresenter<V extends BaseView, M extends BaseModel> {
    public V mView;
    public M mModel;

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
    }
}
