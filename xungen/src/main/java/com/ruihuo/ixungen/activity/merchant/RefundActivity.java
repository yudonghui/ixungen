package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import static com.ruihuo.ixungen.R.id.avatar;

public class RefundActivity extends BaseNoTitleActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private LinearLayout mLl_title;
    private com.ruihuo.ixungen.view.CircleImageView mAvatar;
    private TextView mNikename;
    private TextView mBuyStatus;
    private com.ruihuo.ixungen.activity.merchant.OrderInfoView mOrderInfoView;
    private TextView mBuyNum;
    private TextView mMoney;
    private TextView mOrderNo;
    private TextView mOrderTime;
    private com.ruihuo.ixungen.activity.merchant.CardShopsView mCardShopsView;
    private LinearLayout mRefund_resean;
    private TextView mRefund;
    private LinearLayout mLl_one;
    private ImageView mOne;
    private TextView mOnetext;
    private LinearLayout mLl_two;
    private ImageView mTwo;
    private TextView mTwotext;
    private LinearLayout mLl_three;
    private ImageView mThree;
    private TextView mThreetext;
    private LinearLayout mLl_changprice;
    private EditText mChangPrice;
    private TextView mConfirm;
    // private OrderFormBean.DataBean dataBean;
    private String text;
    private HttpInterface mHttp;
    private LoadingDialogUtils loadingDialogUtils;
    private String orderNo;
    private OrderDetailBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        mContext = this;
        text = getIntent().getStringExtra("text");
        orderNo = getIntent().getStringExtra("orderNo");
        initView();
        addData();
        addListener();
    }

    private String oneText;
    private String twoText;
    private String threeText;
    private String refuseReason;

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mLl_title = (LinearLayout) findViewById(R.id.ll_title);
        mAvatar = (com.ruihuo.ixungen.view.CircleImageView) findViewById(avatar);
        mNikename = (TextView) findViewById(R.id.nikename);
        mBuyStatus = (TextView) findViewById(R.id.buyStatus);
        mOrderInfoView = (OrderInfoView) findViewById(R.id.orderInfoView);
        mBuyNum = (TextView) findViewById(R.id.buyNum);
        mMoney = (TextView) findViewById(R.id.money);
        mOrderNo = (TextView) findViewById(R.id.orderNo);
        mOrderTime = (TextView) findViewById(R.id.orderTime);
        mCardShopsView = (CardShopsView) findViewById(R.id.cardShopsView);
        mRefund_resean = (LinearLayout) findViewById(R.id.refund_resean);
        mRefund = (TextView) findViewById(R.id.refund);
        mLl_one = (LinearLayout) findViewById(R.id.ll_one);
        mOne = (ImageView) findViewById(R.id.one);
        mOnetext = (TextView) findViewById(R.id.onetext);
        mLl_two = (LinearLayout) findViewById(R.id.ll_two);
        mTwo = (ImageView) findViewById(R.id.two);
        mTwotext = (TextView) findViewById(R.id.twotext);
        mLl_three = (LinearLayout) findViewById(R.id.ll_three);
        mThree = (ImageView) findViewById(R.id.three);
        mThreetext = (TextView) findViewById(R.id.threetext);
        mLl_changprice = (LinearLayout) findViewById(R.id.ll_changprice);
        mChangPrice = (EditText) findViewById(R.id.changPrice);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mHttp = HttpUtilsManager.getInstance(mContext);
        mConfirm.setOnClickListener(ConfirmListener);

    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_one.setOnClickListener(RefundReseanListener);
        mLl_two.setOnClickListener(RefundReseanListener);
        mLl_three.setOnClickListener(RefundReseanListener);
        mCardShopsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra("shopId", dataBean.getShop_id());
                intent.putExtra("type", Integer.parseInt(dataBean.getType()));
                startActivity(intent);
            }
        });
    }

    private void addData() {
        OrderDetailWeb orderDetailWeb = new OrderDetailWeb(mContext);
        orderDetailWeb.orderDetail(orderNo, "1", new OrderDetailWeb.CallBackInterface() {
            @Override
            public void callBack(OrderDetailBean.DataBean data) {
                dataBean = data;
                setView();
            }

        });
    }

    private void setView() {
        if (dataBean == null) return;
        String status = dataBean.getStatus();
        if ("3".equals(status)) {//退款中。这个时候本界面是拒绝退款
            oneText = "用户正在消费，所以取消退款申请";
            twoText = "该商品一经预定，不可取消";
            threeText = "已经不在退款时间！";
            mText_title.setText("拒绝退款");
            mConfirm.setText("拒绝退款");
            mRefund.setText("拒绝退款申请的原因");

            refuseReason = "用户正在消费，所以取消退款申请";
        } else if ("修改价钱".equals(text)) {
            mLl_changprice.setVisibility(View.VISIBLE);
            mRefund_resean.setVisibility(View.GONE);
            mConfirm.setText("确定修改，并提醒买家付款");
            mText_title.setText("修改价钱");
        } else {
            oneText = "商品已售完，请您谅解";
            twoText = "店家休息中";
            threeText = "系统问题，请电话联系！";
            mText_title.setText("拒绝订单");
            mRefund.setText("拒绝用户订单的原因");
            if ("0".equals(status)) {
                mConfirm.setText("确认拒绝");
                mBuyStatus.setText("用户未付款");
            } else {
                mConfirm.setText("确认拒绝，并退款");
            }
            refuseReason = "商品已售完，请您谅解";
        }
        mOnetext.setText(oneText);
        mTwotext.setText(twoText);
        mThreetext.setText(threeText);
        mCardShopsView.setData(dataBean);
        mOrderInfoView.setData(dataBean);
        String amount = dataBean.getAmount();
        String user_name = dataBean.getUser_name();
        String cost = dataBean.getCost();
        String order_no = dataBean.getOrder_no();
        String create_time = dataBean.getCreate_time();
        String avatar = dataBean.getAvatar();

        if (!TextUtils.isEmpty(amount)) {
            int num = (int) Float.parseFloat(amount);
            mBuyNum.setText("数量: " + num);
        }
        mNikename.setText(TextUtils.isEmpty(user_name) ? "" : user_name);
        mMoney.setText("￥" + (TextUtils.isEmpty(cost) ? "--" : cost));
        mOrderNo.setText("订单编号: " + (TextUtils.isEmpty(order_no) ? "--" : order_no));
        mOrderTime.setText("下单时间: " + DateFormatUtils.stringToDateMim(create_time));
        PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mAvatar);
    }

    View.OnClickListener RefundReseanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOne.setImageResource(R.mipmap.gray);
            mTwo.setImageResource(R.mipmap.gray);
            mThree.setImageResource(R.mipmap.gray);
            switch (v.getId()) {
                case R.id.ll_one:
                    mOne.setImageResource(R.mipmap.green);
                    refuseReason = oneText;
                    break;
                case R.id.ll_two:
                    mTwo.setImageResource(R.mipmap.green);
                    refuseReason = twoText;
                    break;
                case R.id.ll_three:
                    mThree.setImageResource(R.mipmap.green);
                    refuseReason = threeText;
                    break;
            }
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            String s = textView.getText().toString();
            switch (s) {
                case "拒绝退款":
                    dealRefund(dataBean);
                    break;
                case "确定修改，并提醒买家付款":
                    updateOrderPrice(dataBean);
                    break;
                case "确认拒绝":
                    refuseOrder(dataBean, 0);
                    break;
                case "确认拒绝，并退款":
                    refuseOrder(dataBean, 1);
                    break;
            }
        }
    };

    private void dealRefund(final OrderBaseBean dataBean) {
        String order_no = dataBean.getOrder_no();
        String cost = dataBean.getCost();
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        params.putString("refuseReason", refuseReason); //拒绝退款原因
        params.putString("amount", cost);
        params.putString("dealResult", "2"); //1-同意退款；2-拒绝退款
        mHttp.post(Url.DEAL_REFUND, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                finish();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void refuseOrder(final OrderBaseBean dataBean, int mode) {
        String order_no = dataBean.getOrder_no();
        String cost = dataBean.getCost();
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        params.putString("refuseReason", refuseReason); //拒单原因
        params.putString("amount", cost);
        mHttp.post(Url.REFUSE_ORDER, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                finish();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void updateOrderPrice(OrderBaseBean dataBean) {
        String cost = mChangPrice.getText().toString();
        if (TextUtils.isEmpty(cost)) {
            Toast.makeText(mContext, "请输入修改后的金额", Toast.LENGTH_SHORT).show();
            return;
        }
        String order_no = dataBean.getOrder_no();
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        params.putString("cost", cost); //修改后的金额
        mHttp.post(Url.UPDATE_ORDER_PRICE, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                finish();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
