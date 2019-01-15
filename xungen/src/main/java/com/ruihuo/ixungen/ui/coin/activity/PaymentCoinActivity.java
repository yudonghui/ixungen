package com.ruihuo.ixungen.ui.coin.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.PayResult;
import com.ruihuo.ixungen.utils.PaymentUtils;

import java.util.Map;

public class PaymentCoinActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout mLl_alipay;
    private ImageView mAlipay;
    private LinearLayout mLl_wxpay;
    private ImageView mWxpay;
    private TextView mConfirm;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PaymentUtils.SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //   Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        isSuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private PaymentUtils paymentUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_payment_coin);
        mContext = this;
        initView();
        addListener();
        addData();
        registerBoradcastReceiver();
    }

    private void initView() {
        mLl_alipay = (LinearLayout) findViewById(R.id.ll_alipay);
        mAlipay = (ImageView) findViewById(R.id.alipay);
        mLl_wxpay = (LinearLayout) findViewById(R.id.ll_wxpay);
        mWxpay = (ImageView) findViewById(R.id.wxpay);
        mConfirm = (TextView) findViewById(R.id.confirm);
        paymentUtils = new PaymentUtils(mContext, mHandler);
    }

    private void addListener() {
        mLl_alipay.setOnClickListener(SelectorPayListener);
        mLl_wxpay.setOnClickListener(SelectorPayListener);
        mConfirm.setOnClickListener(ConfirmListener);
    }

    private void addData() {

    }

    private int payMethod = 1;//1是微信支付。2是支付宝支付
    View.OnClickListener SelectorPayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_alipay:
                    mAlipay.setImageResource(R.mipmap.selected);
                    mWxpay.setImageResource(R.mipmap.unselected);
                    payMethod = 2;
                    break;
                case R.id.ll_wxpay:
                    mAlipay.setImageResource(R.mipmap.unselected);
                    mWxpay.setImageResource(R.mipmap.selected);
                    payMethod = 1;
                    break;
            }
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (payMethod == 1) {
                paymentUtils.wxpay("" + "", "");
            } else if (payMethod == 2) {
                paymentUtils.alipay("" + "", "", "");
            }
        }
    };

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.PAYMENT_SUCEESS);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.PAYMENT_SUCEESS)) {
                //微信支付成功后的回调
                int isSuccess = intent.getIntExtra("isSuccess", 0);//1成功，2没有成功
                //微信支付成功后的回调
                if (isSuccess == 1)
                    isSuccess();
                else Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

            }
        }
    };


    private void isSuccess() {//支付成功，请求自己后台看是否真的成功了。

    }

}
