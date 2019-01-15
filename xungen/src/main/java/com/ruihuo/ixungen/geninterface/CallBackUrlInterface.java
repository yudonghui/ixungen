package com.ruihuo.ixungen.geninterface;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/14
 * @describe May the Buddha bless bug-free!!!
 */
public interface CallBackUrlInterface {
    void callBack(List<String> imgUrlList);

    void onError();
}
