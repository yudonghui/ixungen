package com.ruihuo.ixungen.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.WXBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class PaymentUtils {
    Context mContext;
    Handler mHandler;
    public static final int SDK_PAY_FLAG = 7000;

    public PaymentUtils(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    public void wxpay(String allMoney, String orderNo) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("name", "微信支付");//支付平台名称
        params.putString("type", "APP");
        params.putString("body", "微信支付快捷方便");
        params.putString("totalFee", allMoney + "");
        params.putString("client", "1");
        params.putString("orderNo", orderNo);//订单号
        //  params.putString("timeStamp", System.currentTimeMillis() + "");
        mHttp.post(Url.RECHARGE_WX_URL, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = new Gson();
                WXBean wxBean = gson.fromJson(result, WXBean.class);
               // Toast.makeText(mContext, wxBean.getData().getPrepayid() + wxBean.getMsg(), Toast.LENGTH_SHORT).show();
                if (wxBean.getCode() == 0) {
                    PayReq req = new PayReq();
                    WXBean.DataBean data = wxBean.getData();
                    req.appId = ConstantNum.WX_APP_ID;
                    req.partnerId = data.getPartnerid();
                    req.prepayId = data.getPrepayid();
                    req.packageValue = data.getPackageX();
                    req.nonceStr = data.getNoncestr();
                    req.timeStamp = data.getTimestamp();
                    req.sign = data.getSign();
                    // req.extData = "app data"; // optional
                    // Toast.makeText(RechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先将应用注册到微信
                    XunGenApp.api.sendReq(req);

                } else {
                    Toast.makeText(mContext, wxBean.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    public void alipay(String allMoney, String orderNo, String name) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("name", "支付宝支付");//支付平台名称
        params.putString("type", "APP");
        params.putString("body", "支付宝支付");
        params.putString("totalAmount", allMoney + "");
        params.putString("client", "1");
        params.putString("subject", name);
        params.putString("orderNo", orderNo);//订单号
        mHttp.post(Url.RECHARGE_ALIPAY_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(final String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String data = jsonObject.getString("data");
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask((Activity) mContext);
                            Map<String, String> map = alipay.payV2(data, true);

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = map;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
