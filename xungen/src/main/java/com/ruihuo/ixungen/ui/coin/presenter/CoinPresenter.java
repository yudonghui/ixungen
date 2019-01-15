package com.ruihuo.ixungen.ui.coin.presenter;

import com.ruihuo.ixungen.ui.coin.activity.CoinActivity;
import com.ruihuo.ixungen.ui.coin.contract.CoinConstract;
import com.ruihuo.ixungen.ui.coin.model.CoinModel;

/**
 * @author yudonghui
 * @date 2017/11/29
 * @describe May the Buddha bless bug-free!!!
 */
public class CoinPresenter extends CoinConstract.Presenter {
    public CoinPresenter(CoinActivity view) {
        this.mView = view;
        this.mModel = new CoinModel();
        this.setVM(mView, mModel);
    }

}
