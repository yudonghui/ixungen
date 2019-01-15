package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.PaymentActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ author yudonghui
 * @ date 2017/8/9
 * @ describe May the Buddha bless bug-free！！
 */
public class OrderActivity extends AppCompatActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private TextView mName;
    private TextView mTime;
    private TextView mPriceOne;
    private TextView mClassify;
    private TextView mRoomDetail;
    private EditText mAmount;
    private LinearLayout mLl_username;
    private EditText mTruename;
    private LinearLayout mLl_allmoney;
    private TextView mAllmoney;
    private EditText mMobile;
    private LinearLayout mLl_remark;
    private EditText mRemark;
    private TextView mInfo;
    private LinearLayout mLl_bottom;
    private TextView mPrice;
    private TextView mOrder;
    private GoodsFormBaseBean dataBean;
    private OrderData orderData;
    private String allAmount;//房间的总数量
    private String amount = "1";//购买额数量
    private GoodsDetailDialog mGoodsDetailDialog;
    private int days;//天数
    private float price;//单价
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mContext = this;
        orderData = (OrderData) getIntent().getSerializableExtra("orderData");
        dataBean = (GoodsFormBaseBean) getIntent().getSerializableExtra("dataBean");
        if (orderData != null)
            type = orderData.getType();

        initView();
        addData();
        addListener();
    }


    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mTitleBar.setTitle("填写订单");
        mName = (TextView) findViewById(R.id.name);
        mTime = (TextView) findViewById(R.id.time);
        mPriceOne = (TextView) findViewById(R.id.priceOne);
        mClassify = (TextView) findViewById(R.id.classify);
        mRoomDetail = (TextView) findViewById(R.id.roomDetail);
        mAmount = (EditText) findViewById(R.id.amount);
        mLl_username = (LinearLayout) findViewById(R.id.ll_username);
        mTruename = (EditText) findViewById(R.id.truename);
        mLl_allmoney = (LinearLayout) findViewById(R.id.ll_allmoney);
        mAllmoney = (TextView) findViewById(R.id.allmoney);
        mMobile = (EditText) findViewById(R.id.mobile);
        mLl_remark = (LinearLayout) findViewById(R.id.ll_remark);
        mRemark = (EditText) findViewById(R.id.remark);
        mInfo = (TextView) findViewById(R.id.info);
        mLl_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mPrice = (TextView) findViewById(R.id.price);
        mOrder = (TextView) findViewById(R.id.order);
        mMobile.setText(XunGenApp.phone);
        if (orderData == null) return;
        String name = orderData.getName();
        String consumeStartTime = orderData.getConsumeStartTime();
        String consumeEndTime = orderData.getConsumeEndTime();
        String classify = orderData.getClassify();
        String service = orderData.getService();
        price = Float.parseFloat(TextUtils.isEmpty(orderData.getPrice()) ? "0" : orderData.getPrice());
        price = (float) StringUtil.getDecimal2(price);//取两位小数，四舍五入
        days = orderData.getDays();
        allAmount = orderData.getAllAmount();
        mName.setText(TextUtils.isEmpty(name) ? "" : name);
        mInfo.setText("商品说明: " + (TextUtils.isEmpty(orderData.getInfo()) ? "" : orderData.getInfo()));
        if (type == 1) {
            mClassify.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
            mRoomDetail.setVisibility(View.GONE);
            mLl_username.setVisibility(View.GONE);
            mPriceOne.setVisibility(View.VISIBLE);
            mLl_allmoney.setVisibility(View.VISIBLE);
            mLl_remark.setVisibility(View.VISIBLE);
            mPrice.setText("￥" + price);
            mPriceOne.setText("￥" + price);
            mAllmoney.setText("￥" + price * 1);
        } else if (type == 2) {
            mClassify.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.VISIBLE);
            if (dataBean == null) mRoomDetail.setVisibility(View.GONE);
            else
                mRoomDetail.setVisibility(View.VISIBLE);
            mLl_username.setVisibility(View.VISIBLE);
            mPriceOne.setVisibility(View.GONE);
            mLl_allmoney.setVisibility(View.GONE);
            mLl_remark.setVisibility(View.VISIBLE);
            mTime.setText("入住: " + consumeStartTime + "  离店: " + consumeEndTime + "  共" + days + "晚");
            mClassify.setText(TextUtils.isEmpty(classify) ? "" : classify + "  " + (TextUtils.isEmpty(service) ? "" : service));
            mPrice.setText("￥" + (float) StringUtil.getDecimal2(price * days));
        } else {
            mClassify.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
            mRoomDetail.setVisibility(View.GONE);
            mLl_username.setVisibility(View.GONE);
            mPriceOne.setVisibility(View.VISIBLE);
            mLl_allmoney.setVisibility(View.VISIBLE);
            mLl_remark.setVisibility(View.VISIBLE);
            mPrice.setText("￥" + price);
            mPriceOne.setText("￥" + price);
            mAllmoney.setText("￥" + price * 1);
        }


    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOrder.setOnClickListener(OrderListener);
        mAmount.addTextChangedListener(TextChangListener);
        mRoomDetail.setOnClickListener(RoomDetailListener);
        mGoodsDetailDialog = new GoodsDetailDialog(mContext);
    }

    private void addData() {

    }

    View.OnClickListener RoomDetailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGoodsDetailDialog.showDialog(dataBean, orderData, 2);
        }
    };
    TextWatcher TextChangListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String s1 = mAmount.getText().toString();
            if (!TextUtils.isEmpty(s1)) {
                int i = Integer.parseInt(s1);
                int allAmount1 = (int) Float.parseFloat(allAmount);
                if (i > allAmount1) {
                    mAmount.setText(allAmount1 + "");
                    Toast.makeText(mContext, "商品仅剩数目为" + allAmount1, Toast.LENGTH_SHORT).show();
                    i = allAmount1;
                }
                double decimal2 = StringUtil.getDecimal2(price * i);//取两位小说，四舍五入
                double decimal21 = StringUtil.getDecimal2(price * i * days);
                if (type == 1) {
                    mPrice.setText("￥" + decimal2);
                    mAllmoney.setText("￥" + decimal2);
                } else {
                    mPrice.setText("￥" + decimal21);
                    //  mAllmoney.setText("￥" + decimal21);
                }

            }

        }
    };
    View.OnClickListener OrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            amount = mAmount.getText().toString();
            final String truename = mTruename.getText().toString();
            final String phone = mMobile.getText().toString();
            String remark = mRemark.getText().toString();
            if (TextUtils.isEmpty(amount) || Integer.parseInt(amount) == 0) {
                Toast.makeText(mContext, "您预订商品数量为0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (type == 2) {
                if (TextUtils.isEmpty(truename)) {
                    Toast.makeText(mContext, "请输入入住人姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            final int type = orderData.getType();
            String goodsId = orderData.getGoodsId();
            String shopId = orderData.getShopId();
            String isCancel = orderData.getIsCancel();
            String consumeStartTime = orderData.getConsumeStartTime();
            String consumeEndTime = orderData.getConsumeEndTime();
            String name = orderData.getName();
            //String info = orderData.getInfo();
            String classify = orderData.getClassify();
            String service = orderData.getService();
            final int i = Integer.parseInt(amount);

            final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("type", type + "");
            params.putString("goodsId", goodsId);
            params.putString("shopId", shopId);
            params.putString("isCancel", TextUtils.isEmpty(isCancel) ? "2" : isCancel);//默认提交的时候是不可取消
            if (type == 1) {
                params.putString("cost", price * i + "");
            } else {
                params.putString("cost", StringUtil.getDecimal2(price * i * days) + "");
            }
            params.putString("consumeStartTime", consumeStartTime);
            params.putString("consumeEndTime", consumeEndTime);
            params.putString("client", "1");
            params.putString("name", name);
            params.putString("price", price + "");//单价
            params.putString("amount", amount);//预定的商品数
            params.putString("info", TextUtils.isEmpty(remark) ? "" : remark);
            params.putString("phone", phone);
            if (type == 2) {
                params.putString("classify", classify);
                params.putString("service", service);
                params.putString("userName", truename);
            } else params.putString("userName", XunGenApp.nikename);
            mHttp.post(Url.SHOP_PLACE_ORDER, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String orderNo = jsonObject.getString("data");//订单号
                        Log.e("订单号：", orderNo);
                        orderData.setPhone(phone);
                        orderData.setAmount(amount);
                        orderData.setUserName(truename);
                        if (type == 1)
                            orderData.setAllMoney((float) StringUtil.getDecimal2(price * i));
                        else
                            orderData.setAllMoney((float) StringUtil.getDecimal2(price * i * days));
                        Intent intent = new Intent(mContext, PaymentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderData", orderData);
                        bundle.putString("orderNo", orderNo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
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
    };

}
