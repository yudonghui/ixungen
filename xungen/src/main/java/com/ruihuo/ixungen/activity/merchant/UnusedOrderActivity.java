package com.ruihuo.ixungen.activity.merchant;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.CircleImageView;

public class UnusedOrderActivity extends BaseNoTitleActivity {
    private Context mContext;
    private String orderNo;
    private ImageView mImage_titlebar_back;
    private CircleImageView mAvatar;
    private TextView mNikename;
    private TextView mBuyStatus;
    private CardShopsView mCardShopsView;
    private OrderInfoView mOrderInfoView;
    private TextView mQuitorrecommend;
    private TextView mBuyNum;
    private TextView mMoney;
    private TextView mOrderNo;
    private TextView mOrderTime;
    private TextView mCopy;
    private TextView mOne;
    private TextView mTwo;
    private OrderDetailBean.DataBean dataBean;
    private int redTxt;
    private int deepTxt;
    private BottomSkip bottomSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_order);
        mContext = this;
        orderNo = getIntent().getStringExtra("orderNo");
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mAvatar = (CircleImageView) findViewById(R.id.avatar);
        mNikename = (TextView) findViewById(R.id.nikename);
        mBuyStatus = (TextView) findViewById(R.id.buyStatus);
        mCardShopsView = (CardShopsView) findViewById(R.id.cardShopsView);
        mOrderInfoView = (OrderInfoView) findViewById(R.id.orderInfoView);
        mBuyNum = (TextView) findViewById(R.id.buyNum);
        mMoney = (TextView) findViewById(R.id.money);
        mQuitorrecommend = (TextView) findViewById(R.id.quitorrecommend);
        mOrderNo = (TextView) findViewById(R.id.orderNo);
        mOrderTime = (TextView) findViewById(R.id.orderTime);
        mOne = (TextView) findViewById(R.id.one);
        mTwo = (TextView) findViewById(R.id.two);
        mCopy = (TextView) findViewById(R.id.copy);
        redTxt = mContext.getResources().getColor(R.color.red_txt);
        deepTxt = mContext.getResources().getColor(R.color.deep_txt);
        bottomSkip = new BottomSkip(mContext, new DialogEditInterface() {
            @Override
            public void callBack(String message) {
                if ("删除订单".equals(message) || "同意退款".equals(message) || "已使用".equals(message)) {
                    finish();
                }
            }
        });
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCopy.setOnClickListener(CopyListener);
        mOne.setOnClickListener(BottomListener);
        mTwo.setOnClickListener(BottomListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addData();
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
        mCardShopsView.setData(dataBean);
        mOrderInfoView.setData(dataBean);
        String amount = dataBean.getAmount();
        String user_name = dataBean.getUser_name();
        String content = dataBean.getContent();
        String cost = dataBean.getCost();
        String status = dataBean.getStatus();
        String order_no = dataBean.getOrder_no();
        String create_time = dataBean.getCreate_time();
        String avatar = dataBean.getAvatar();
        if (!TextUtils.isEmpty(amount)) {
            int num = (int) Float.parseFloat(amount);
            mBuyNum.setText("" + num);
        }
        mNikename.setText(TextUtils.isEmpty(user_name) ? "" : user_name);
        mMoney.setText("￥" + (TextUtils.isEmpty(cost) ? "--" : cost));
        mOrderNo.setText("订单编号: " + (TextUtils.isEmpty(order_no) ? "--" : order_no));
        mOrderTime.setText("下单时间: " + DateFormatUtils.stringToDateMim(create_time));
        PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mAvatar);
        switch (status) {
            case "0"://0-未支付
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("拒单");

                mTwo.setVisibility(View.VISIBLE);
                mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mTwo.setTextColor(redTxt);
                mTwo.setText("修改价钱");

                mBuyStatus.setText("等待用户付款");

                break;
            case "1"://支付完成未消费
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("拒单");

                mTwo.setVisibility(View.VISIBLE);
                mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mTwo.setTextColor(redTxt);
                mTwo.setText("已使用");

                mBuyStatus.setText("买家已付款");
                break;

            case "2"://消费完成，但是用户没有评论
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("删除订单");

                mTwo.setVisibility(View.GONE);

                mBuyStatus.setText("已完成");
                break;
            case "3"://退款中
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_red_box);
                mOne.setTextColor(redTxt);
                mOne.setText("拒绝退款");

                mTwo.setVisibility(View.VISIBLE);
                mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mTwo.setTextColor(redTxt);
                mTwo.setText("同意退款");

                mBuyStatus.setText("买家申请退款");
                break;
            case "4"://退款完成
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("删除订单");
                mTwo.setVisibility(View.GONE);

                mBuyStatus.setText("已退款");
                break;
            case "5"://商家拒单
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("删除订单");
                mTwo.setVisibility(View.GONE);

                mBuyStatus.setText("已拒单");
                break;
            case "6":
                mOne.setVisibility(View.GONE);
                mTwo.setVisibility(View.VISIBLE);
                mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mTwo.setTextColor(redTxt);
                mTwo.setText("已使用");

                mBuyStatus.setText("已拒绝退款");
                break;
            case "7":// 7待评价
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("删除订单");

                mTwo.setVisibility(View.GONE);

                mBuyStatus.setText("待评价");
                break;
            case "8"://8 待回复
                mOne.setVisibility(View.VISIBLE);
                mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mOne.setTextColor(deepTxt);
                mOne.setText("删除订单");

                mTwo.setVisibility(View.VISIBLE);
                mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mTwo.setTextColor(redTxt);
                mTwo.setText("回复评论");
                mBuyStatus.setText("");
                mQuitorrecommend.setText("用户评论: " + content);
                mQuitorrecommend.setVisibility(View.VISIBLE);
                break;
        }


    }

    View.OnClickListener CopyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) return;
            String order_no = dataBean.getOrder_no();
            ClipboardManager systemService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            systemService.setText(order_no);
            Toast.makeText(mContext, "复制成功！", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener BottomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) return;
            TextView textView = (TextView) v;
            String text = textView.getText().toString();
            dataBean.setText(text);
            dataBean.setMode("1");//商家还是用户
            bottomSkip.skip(dataBean);
        }
    };
}
