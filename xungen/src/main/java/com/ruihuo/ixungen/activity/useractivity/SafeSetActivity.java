package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.login.ForgetPasswordActivity;

public class SafeSetActivity extends BaseActivity {
    private LinearLayout mChangPassword;
    private LinearLayout mChangPhone;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_safe_set);
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("安全设置");
        mChangPassword = (LinearLayout) findViewById(R.id.ll_chang_password);
        mChangPhone= (LinearLayout) findViewById(R.id.ll_chang_phone);
    }

    private void addListener() {
        mChangPassword.setOnClickListener(ChangePasswordListener);
        mChangPhone.setOnClickListener(ChangePhoneListener);
    }

    View.OnClickListener ChangePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener ChangePhoneListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext,ChangePhoneActivity.class);
            startActivity(intent);
        }
    };
}
