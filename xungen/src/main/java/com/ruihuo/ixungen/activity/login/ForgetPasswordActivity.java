package com.ruihuo.ixungen.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import java.text.SimpleDateFormat;

/**
 * @ author yudonghui
 * @ date 2017/3/30
 * @ describe May the Buddha bless bug-free！！
 */
public class ForgetPasswordActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private EditText mPhone;
    private EditText mCode;
    private TextView mObtainCode;
    private EditText mPassword1;
    private EditText mPassword2;
    private TextView mConfirm;
    private Context mContext;
    private int time = 60;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mContext = this;
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.TitlBar);
        mTitleBar.setTitle("密码");
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mCode = (EditText) findViewById(R.id.edit_code);
        mObtainCode = (TextView) findViewById(R.id.text_obtain_code);
        mPassword1 = (EditText) findViewById(R.id.edit_password);
        mPassword2 = (EditText) findViewById(R.id.edit_password2);
        mConfirm = (TextView) findViewById(R.id.text_confirm);
    }

    private void addData() {

    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取验证码
        mObtainCode.setOnClickListener(ObtainCodeListener);
        //确定
        mConfirm.setOnClickListener(ConfirmListener);
    }

    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mPhone.getText().toString();
            String code = mCode.getText().toString();
            String password1 = mPassword1.getText().toString();
            String password2 = mPassword2.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.toast(mContext, "请输入手机号");
            } else if (!StringUtil.isLegalPhone(phone)) {
                ToastUtils.toast(mContext, "请输入正确的手机号");
            } else if (TextUtils.isEmpty(code)) {
                ToastUtils.toast(mContext, "请输入验证码");
            } else if (TextUtils.isEmpty(password1)) {
                ToastUtils.toast(mContext, "请输入密码");
            } else if (TextUtils.isEmpty(password2)) {
                ToastUtils.toast(mContext, "请确认密码");
            } else if (!password1.equals(password2)) {
                ToastUtils.toast(mContext, "两次输入的密码不相同");
            } else if (!StringUtil.isLegalPassword(password1)) {
                ToastUtils.toast(mContext, "请输入正确的密码");
            } else {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("phone", phone);
                params.putString("code", code);
                params.putString("password", password1);
                mHttp.post(Url.RESETPASSWORD_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        ToastUtils.toast(mContext, "设置密码成功");
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                        hintDialogUtils.setMessage("网络请求错误");
                        hintDialogUtils.setVisibilityCancel();
                    }
                });
            }
        }
    };
    View.OnClickListener ObtainCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.toast(mContext, "请输入手机号");
                return;
            } else if (!StringUtil.isLegalPhone(phone)) {
                ToastUtils.toast(mContext, "请输入正确的手机号");
                return;
            }
            time = 60;
            mObtainCode.setTextColor(getResources().getColor(R.color.again_code_txt));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (time == 0) {
                        mObtainCode.setText("重新获取");
                        mObtainCode.setEnabled(true);
                        return;
                    } else {
                        time--;
                        mObtainCode.setText("重新获取(" + time + ")");
                        mObtainCode.setEnabled(false);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
            //网络请求
            HttpInterface mHttpInterface = HttpUtilsManager.getInstance(mContext);
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
}
