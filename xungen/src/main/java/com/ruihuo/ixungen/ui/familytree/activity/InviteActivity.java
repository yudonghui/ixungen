package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.ui.familytree.contract.InviteContract;
import com.ruihuo.ixungen.ui.familytree.presenter.InvitePresenter;
import com.ruihuo.ixungen.utils.ShareUtils;

public class InviteActivity extends BaseActivity implements InviteContract.View {
    InvitePresenter mPresenter = new InvitePresenter(this);
    private Context mContext;
    private EditText mPhone;
    private TextView mConfirm;
    private LinearLayout mLl_wx;
    private LinearLayout mLl_qq;
    private LinearLayout mLl_weibo;
    private TextView mInvite_introduce;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_invite);
        mContext = this;
        id = getIntent().getStringExtra("id");
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mTitleBar.setTitle("家谱邀请");
        mPhone = (EditText) findViewById(R.id.phone);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mLl_wx = (LinearLayout) findViewById(R.id.ll_wx);
        mLl_qq = (LinearLayout) findViewById(R.id.ll_qq);
        mLl_weibo = (LinearLayout) findViewById(R.id.ll_weibo);
        mInvite_introduce = (TextView) findViewById(R.id.invite_introduce);
    }

    private void addListener() {
        mConfirm.setOnClickListener(ConfirmListener);
        mLl_wx.setOnClickListener(ShareListener);
        mLl_qq.setOnClickListener(ShareListener);
        mLl_weibo.setOnClickListener(ShareListener);
    }

    private void addData() {

    }

    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mPhone.getText().toString();
            Bundle parameters = new Bundle();
            parameters.putString("token", XunGenApp.token);
            parameters.putString("id", id);
            parameters.putString("phone", phone);
            mPresenter.getInvitePhoneData(parameters, mContext);
        }
    };
    View.OnClickListener ShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils shareUtils = new ShareUtils(mContext);//0-新浪，1-微信，4-QQ
            switch (v.getId()) {
                case R.id.ll_wx:
                    shareUtils.shareSelf(Url.APP_URL, getString(R.string.app_title), getString(R.string.app_introduce), "", "1");
                    break;
                case R.id.ll_qq:
                    shareUtils.shareSelf(Url.APP_URL, getString(R.string.app_title), getString(R.string.app_introduce), "", "4");
                    break;
                case R.id.ll_weibo:
                    shareUtils.shareSelf(Url.APP_URL, getString(R.string.app_title), getString(R.string.app_introduce), "", "0");
                    break;
            }
        }
    };

    @Override
    public void getInvitePhoneSuccess(String result) {
        Toast.makeText(mContext, "邀请已经发送成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void getInvitePhoneError(String error) {

    }
}
