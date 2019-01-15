package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.TitleBar;

public class ApplyQuitOrderActivity extends AppCompatActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private CardShopsView mCardShopsView;
    private LinearLayout mLl_one;
    private ImageView mOne;
    private TextView mOnetext;
    private LinearLayout mLl_two;
    private ImageView mTwo;
    private TextView mTwotext;
    private LinearLayout mLl_three;
    private ImageView mThree;
    private TextView mThreetext;
    private TextView mMoney;
    private EditText mEdit_info;
    private TextView mConfirm;
    private OrderBaseBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_quit_order);
        mContext = this;
        dataBean = (OrderBaseBean) getIntent().getSerializableExtra("dataBean");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mCardShopsView = (com.ruihuo.ixungen.activity.merchant.CardShopsView) findViewById(R.id.cardShopsView);
        mLl_one = (LinearLayout) findViewById(R.id.ll_one);
        mOne = (ImageView) findViewById(R.id.one);
        mOnetext = (TextView) findViewById(R.id.onetext);
        mLl_two = (LinearLayout) findViewById(R.id.ll_two);
        mTwo = (ImageView) findViewById(R.id.two);
        mTwotext = (TextView) findViewById(R.id.twotext);
        mLl_three = (LinearLayout) findViewById(R.id.ll_three);
        mThree = (ImageView) findViewById(R.id.three);
        mThreetext = (TextView) findViewById(R.id.threetext);
        mMoney = (TextView) findViewById(R.id.money);
        mEdit_info = (EditText) findViewById(R.id.edit_info);
        mConfirm = (TextView) findViewById(R.id.confirm);
    }

    private void addListener() {
        mLl_one.setOnClickListener(QuitMoneyListener);
        mLl_two.setOnClickListener(QuitMoneyListener);
        mLl_three.setOnClickListener(QuitMoneyListener);
        mConfirm.setOnClickListener(ConfirmListener);
    }

    private void addData() {
        if (dataBean == null) return;
        mCardShopsView.setData(dataBean);
        String cost = dataBean.getCost();
        mMoney.setText("￥" + (TextUtils.isEmpty(cost) ? "" : cost));

    }

    private String resean = "已经预定，拍多了。";
    View.OnClickListener QuitMoneyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOne.setImageResource(R.mipmap.gray);
            mTwo.setImageResource(R.mipmap.gray);
            mThree.setImageResource(R.mipmap.gray);
            switch (v.getId()) {
                case R.id.ll_one:
                    mOne.setImageResource(R.mipmap.green);
                    resean = "已经预定，拍多了。";
                    break;
                case R.id.ll_two:
                    mTwo.setImageResource(R.mipmap.green);
                    resean = "拍错了，重新购买。";
                    break;
                case R.id.ll_three:
                    mThree.setImageResource(R.mipmap.green);
                    resean = "不想买了";
                    break;
            }
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String remark = mEdit_info.getText().toString();
            //退款
            String order_no = dataBean.getOrder_no();
            String cost = dataBean.getCost();
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("orderNo", order_no);
            params.putString("amount", cost);
            params.putString("refundReason", resean);
            params.putString("client", "1");
            mHttp.post(Url.CANCEL_ORDER, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent();
                    setResult(332, intent);
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
}
