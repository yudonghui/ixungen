package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @author yudonghui
 * @date 2017/8/17
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderDetailWeb {
    private Context mContext;

    public OrderDetailWeb(Context mContext) {
        this.mContext = mContext;
    }

    public void orderDetail(String orderNo, String type, final CallBackInterface mListener) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("type", type);//0-普通消费者查看订单详情；1-店家查看订单详情
        params.putString("orderNo", orderNo);
        mHttp.get(Url.ORDER_FORM_DETAIL, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                OrderDetailBean orderDetailBean = gson.fromJson(result, OrderDetailBean.class);
                OrderDetailBean.DataBean dataBean = orderDetailBean.getData();
                mListener.callBack(dataBean);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    public interface CallBackInterface {
        void callBack(OrderDetailBean.DataBean data);
    }
}
