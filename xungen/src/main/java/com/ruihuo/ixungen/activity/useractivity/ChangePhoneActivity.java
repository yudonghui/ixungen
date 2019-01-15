package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.ToastUtils;

import java.text.SimpleDateFormat;

public class ChangePhoneActivity extends BaseActivity {
    private Context mContext;
    private TextView mText_obtain_code;
    private EditText mEdit_code;
    private TextView mText_login;
    private EditText mEdit_phone;
    private int time = 60;
    private Handler mHandler = new Handler();
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_chang_phone);
        from = getIntent().getStringExtra("from");
        mContext = this;
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("更换手机号");
        mEdit_phone = (EditText) findViewById(R.id.edit_phone);
        mText_obtain_code = (TextView) findViewById(R.id.text_obtain_code);
        mEdit_code = (EditText) findViewById(R.id.edit_code);
        mText_login = (TextView) findViewById(R.id.text_login);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mText_obtain_code.setOnClickListener(ObtainCodeListener);

        if ("ok".equals(from)) {
            mEdit_phone.setEnabled(true);
            mEdit_phone.setHint("请输入新手机号！");
            mText_login.setOnClickListener(ConfirmListener);
        } else {
            mEdit_phone.setEnabled(false);
            mEdit_phone.setText(XunGenApp.phone);
            mText_login.setOnClickListener(NextListener);
        }
    }

    View.OnClickListener ObtainCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mEdit_phone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.toast(mContext, "请输入手机号");
                return;
            } else if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            time = 60;
            mText_obtain_code.setTextColor(getResources().getColor(R.color.again_code_txt));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (time == 0) {
                        mText_obtain_code.setText("重新获取");
                        mText_obtain_code.setEnabled(true);
                        return;
                    } else {
                        time--;
                        mText_obtain_code.setText("重新获取(" + time + ")");
                        mText_obtain_code.setEnabled(false);
                    }
                     mHandler.postDelayed(this, 1000);
                }
            });
            //网络请求
            HttpInterface mHttpInterface = HttpUtilsManager.getInstance(ChangePhoneActivity.this);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            //format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            long l = System.currentTimeMillis();
            String date1 = format.format(l);
            String md5 = StringUtil.getMD5(phone + "0");
            String addDate = md5 + date1;
            String sign = StringUtil.getMD5(addDate);
            //String sign=StringUtil.toUpperCase(md51);
            Bundle params = new Bundle();
            params.putString("phone", phone);
            params.putString("msg", "0");
            params.putString("sign", sign);
            mHttpInterface.get(Url.SMS_CODE_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    View.OnClickListener NextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mEdit_phone.getText().toString().trim();
            String code = mEdit_code.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(code)) {
                Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }

            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("phone", phone);
            params.putString("code", code);
            mHttp.post(Url.CHECK_SMS_CODE_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(mContext, ChangePhoneActivity.class);
                    intent.putExtra("from", "ok");
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mEdit_phone.getText().toString().trim();
            String code = mEdit_code.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(code)) {
                Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }

            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("phone", phone);
            params.putString("code", code);
            params.putString("token", XunGenApp.token);
            mHttp.post(Url.CHANGE_PHONE_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(mContext, "修改手机号成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };

    private void addData() {

    }
}
