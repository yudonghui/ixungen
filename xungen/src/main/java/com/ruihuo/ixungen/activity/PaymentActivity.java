package com.ruihuo.ixungen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.merchant.OrderData;
import com.ruihuo.ixungen.activity.merchant.OrderFormActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.PayResult;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PaymentUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PaymentActivity extends BaseActivity {
    private Context mContext;
    private com.ruihuo.ixungen.view.CircleImageView mAvatar;
    private TextView mShopsName;
    /*  private TextView mUser_name;
      private TextView mPhone;
      private TextView mTime;*/
    private TextView mGoods_name;
    private TextView mService;
    private TextView mPrice;
    private ImageView mCover;
    private TextView mNeedmoney;//付款金额
    private LinearLayout mLl_alipay;
    private ImageView mAlipay;
    private LinearLayout mLl_wxpay;
    private ImageView mWxpay;
    private TextView mConfirm;

    private String orderNo = "";//订单号
    private OrderData orderData;

    //private String sss = "alipay_sdk=alipay-sdk-php-20161101&app_id=2017072807930335&biz_content=%7B%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22subject%22%3A+%22App%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95%22%2C%22out_trade_no%22%3A+%2220170125test01%22%2C%22timeout_express%22%3A+%2230m%22%2C%22total_amount%22%3A+%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=%E5%95%86%E6%88%B7%E5%A4%96%E7%BD%91%E5%8F%AF%E4%BB%A5%E8%AE%BF%E9%97%AE%E7%9A%84%E5%BC%82%E6%AD%A5%E5%9C%B0%E5%9D%80&sign_type=RSA2×tamp=2017-07-28+14%3A53%3A15&version=1.0&sign=kcwBWa7bGb%2FXUQPCt2AC83RJYv1eo2pQZJvr4tkHpcrseK%2BU4aoOn0XsVoLWWdrlfaWNUSL5%2BG1tsVtmY6v5fXdLp7DvTTtH%2BAAYAjaSXdFNiL%2F2zF74tiBKgfQifAOgLNs%2F9nNt3faz7PP0oSvB7O6oOtIENcknut2syCLhCMpFHKarQINMsiuEid8qLcWuj4IttpJ%2BsaOrY%2BZVRHMCCHmO1lq5kylpXzA6npx3UarEr7uF4Pa8MKs55odgCTpKwob2hnHbY%2F0TCjWybg9njr7CUGkgGRHTAxB%2BLwlRLNOZu9Ffln1Uu41BdabnBq09CP%2BkG3HzLFHyrxjNrLJyHA%3D%3D";
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
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_payment);
        mContext = this;
        orderNo = getIntent().getStringExtra("orderNo");
        orderData = (OrderData) getIntent().getSerializableExtra("orderData");

        initView();
        addListener();
        //注册一个动态广播
        registerBoradcastReceiver();
    }

    private float allMoney;

    private void initView() {
        mTitleBar.setTitle("订单支付");
        mAvatar = (com.ruihuo.ixungen.view.CircleImageView) findViewById(R.id.avatar);
        mShopsName = (TextView) findViewById(R.id.shopsName);
       /* mUser_name = (TextView) findViewById(R.id.user_name);
        mPhone = (TextView) findViewById(R.id.phone);
        mTime = (TextView) findViewById(R.id.time);*/
        mGoods_name = (TextView) findViewById(R.id.goods_name);
        mService = (TextView) findViewById(R.id.service);
        mPrice = (TextView) findViewById(R.id.price);
        mNeedmoney = (TextView) findViewById(R.id.needmoney);
        mCover = (ImageView) findViewById(R.id.cover);
        mLl_alipay = (LinearLayout) findViewById(R.id.ll_alipay);
        mAlipay = (ImageView) findViewById(R.id.alipay);
        mLl_wxpay = (LinearLayout) findViewById(R.id.ll_wxpay);
        mWxpay = (ImageView) findViewById(R.id.wxpay);
        mConfirm = (TextView) findViewById(R.id.confirm);
        paymentUtils = new PaymentUtils(mContext, mHandler);
        if (orderData != null) {
            type = orderData.getType();
            String shopName = orderData.getShopName();
            String logo = orderData.getLogo();
            allMoney = orderData.getAllMoney();
            String name = orderData.getName();
            String price = orderData.getPrice();
            int days = orderData.getDays();
            String amount = orderData.getAmount();
            String consumeStartTime = orderData.getConsumeStartTime();
            String consumeEndTime = orderData.getConsumeEndTime();

            String cover = orderData.getCover();

            PicassoUtils.setImgView(logo, R.mipmap.default_header, mContext, mAvatar);
            mShopsName.setText(TextUtils.isEmpty(shopName) ? "" : shopName);

            mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "-" : price));
            mGoods_name.setText(TextUtils.isEmpty(name) ? "-" : name);
            if (orderData.getType() == 2) {
                mService.setText(consumeStartTime + "至" + consumeEndTime + " 共" + days + "晚 " + amount + "间");
            } else {
                mService.setText("数量: " + amount);
            }
            mNeedmoney.setText("￥" + allMoney);

            int displayWidth = DisplayUtilX.getDisplayWidth();
            int resize = displayWidth / 3;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 4 / 5);
            mCover.setLayoutParams(layoutParams);
            if (!TextUtils.isEmpty(cover)) {
                String[] split = cover.split("\\;");
                Picasso.with(mContext)
                        .load(split[0])
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.default_long)
                        .error(R.mipmap.default_long)
                        .into(mCover);
            }

        } else {

        }
    }

    private void addListener() {
        mLl_alipay.setOnClickListener(SelectorPayListener);
        mLl_wxpay.setOnClickListener(SelectorPayListener);
        mConfirm.setOnClickListener(ConfirmListener);
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
                paymentUtils.wxpay(allMoney + "", orderNo);
            } else if (payMethod == 2) {
                paymentUtils.alipay(allMoney + "", orderNo, TextUtils.isEmpty(orderData.getName()) ? "" : orderData.getName());
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

    private void isSuccess() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", orderNo);
        mHttp.get(Url.ORDER_IS_SUCCESS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int data = jsonObject.getInt("data");
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    if (data == 1) {
                        hintDialogUtils.setMessage("恭喜您！您已支付成功！");
                        hintDialogUtils.setConfirm("查看订单", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                Intent intent = new Intent(mContext, OrderFormActivity.class);
                                intent.putExtra("type", 0);
                                startActivity(intent);
                                finish();
                            }
                        });
                        hintDialogUtils.setCancel("继续预定", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                finish();
                            }
                        });
                    } else {
                        hintDialogUtils.setMessage("您是否已经支付?");
                        hintDialogUtils.setCancel("未支付", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {

                            }
                        });
                        hintDialogUtils.setConfirm("已支付", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                Intent intent = new Intent(mContext, OrderFormActivity.class);
                                intent.putExtra("type", 0);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
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
