package com.ruihuo.ixungen.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.NetworkUtils;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

public class SetPasswordActivity extends AppCompatActivity {
    // Content View Elements

    private EditText mEdit_password;
    private EditText mEdit_password2;
    private TextView mRootId;
    private TextView mText_next;
    private Context mContext;
    private TextView mTextSkip;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mEdit_password = (EditText) findViewById(R.id.edit_password);
        mEdit_password2 = (EditText) findViewById(R.id.edit_password2);
        mRootId = (TextView) findViewById(R.id.text_rootid);
        mRootId.setText(XunGenApp.rid);
        mText_next = (TextView) findViewById(R.id.text_next);
        mTextSkip = (TextView) findViewById(R.id.text_skip);
    }

    private void addListener() {
        mText_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password1 = mEdit_password.getText().toString();
                String password2 = mEdit_password2.getText().toString();
                if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
                    ToastUtils.toast(mContext, "请输入密码");
                } else if (!StringUtil.isLegalPassword(password1) && !StringUtil.isLegalPassword(password2)) {
                    ToastUtils.toast(mContext, "请输入规范的密码");
                } else if (!password1.equals(password2)) {
                    ToastUtils.toast(mContext, "两次输入的密码不一致");
                } else {
                    final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("password", password1);
                    String loginIp = NetworkUtils.getIPAddress();
                    if (!TextUtils.isEmpty(loginIp)) {
                        params.putString("loginIp", loginIp);
                    }
                    mHttp.post(Url.SETPASSWORD_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                            XunGenApp.isLogin = true;
                           /* SharedPreferences userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit = userinfo.edit();
                            edit.putString("username", phone);
                            edit.putString("password", password1);
                            edit.putBoolean("islogin", true);
                            edit.commit();*/
                            SharedPreferences userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit = userinfo.edit();
                            edit.putString("loginMethod", "password");
                            edit.commit();
                            Intent intent = new Intent();
                            intent.setAction(ConstantNum.LOGIN_SUCCESS);
                            sendBroadcast(intent);
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                }
            }
        });
        //跳过
        mTextSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                String loginIp = NetworkUtils.getIPAddress();
                if (!TextUtils.isEmpty(loginIp)) {
                    params.putString("loginIp", loginIp);
                }
                mHttp.post(Url.SETPASSWORD_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();

                        Intent intent = new Intent();
                        intent.setAction(ConstantNum.LOGIN_SUCCESS);
                        sendBroadcast(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        });
    }
}
