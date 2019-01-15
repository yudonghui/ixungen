package com.ruihuo.ixungen.ui.coin.contract;

import com.ruihuo.ixungen.base.BaseModel;
import com.ruihuo.ixungen.base.BasePresenter;
import com.ruihuo.ixungen.base.BaseView;
import com.ruihuo.ixungen.ui.coin.activity.CoinActivity;
import com.ruihuo.ixungen.ui.coin.model.CoinModel;

/**
 * @author yudonghui
 * @date 2017/11/29
 * @describe May the Buddha bless bug-free!!!
 */
public interface CoinConstract {
    interface View extends BaseView {

    }

    interface Model extends BaseModel {

    }

    abstract class Presenter extends BasePresenter<CoinActivity, CoinModel> {

    }
}
