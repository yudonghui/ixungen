package com.ruihuo.ixungen.ui.familytree;

import com.ruihuo.ixungen.ui.familytree.bean.CatalagBean;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public interface CatalogInface {
    void callBack(CatalagBean.DataBean dataBean, int vpPage);

    void callBackTop(int top);
}
