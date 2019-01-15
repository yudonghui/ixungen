package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.PicassoUtils;

public class UserRecommendDetailActivity extends AppCompatActivity {
    private Context mContext;
    private FrameLayout mActivity_user_recommend_detail;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private com.ruihuo.ixungen.view.CircleImageView mAvatar;
    private TextView mNikename;
    private TextView mContent;
    private com.ruihuo.ixungen.view.MyGridView mGridView;
    private com.ruihuo.ixungen.activity.merchant.CardShopsView mCardShopsView;
    private TextView mReply;
    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recommend_detail);
        orderNo = getIntent().getStringExtra("orderNo");
        mContext = this;
        initView();
        addData();
        addListener();

    }

    private void initView() {
        mActivity_user_recommend_detail = (FrameLayout) findViewById(R.id.activity_user_recommend_detail);
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mAvatar = (com.ruihuo.ixungen.view.CircleImageView) findViewById(R.id.avatar);
        mNikename = (TextView) findViewById(R.id.nikename);
        mContent = (TextView) findViewById(R.id.content);
        mGridView = (com.ruihuo.ixungen.view.MyGridView) findViewById(R.id.gridView);
        mCardShopsView = (com.ruihuo.ixungen.activity.merchant.CardShopsView) findViewById(R.id.cardShopsView);
        mReply = (TextView) findViewById(R.id.reply);
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBean == null) return;
                dataBean.setText("回复评论");
                new BottomSkip(mContext).skip(dataBean);
            }
        });
    }

    private OrderDetailBean.DataBean dataBean;

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
        String avatar = dataBean.getAvatar();
        String user_name = dataBean.getUser_name();
        String content = dataBean.getContent();
        String img = dataBean.getImg();
        mContent.setText(TextUtils.isEmpty(content) ? "" : content);
        String[] split = img.split("\\;");
        GridImageAdapter gridImageAdapter = new GridImageAdapter(split, 2, mContext);
        mGridView.setAdapter(gridImageAdapter);
        PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mAvatar);
        mNikename.setText(TextUtils.isEmpty(user_name) ? "" : user_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 233 && resultCode == 332) {//从申请退款 界面回来
            finish();
        }
    }
}
