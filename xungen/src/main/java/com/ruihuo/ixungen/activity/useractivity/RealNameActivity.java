package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.IDCardValidate;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.text.SimpleDateFormat;

import static com.ruihuo.ixungen.XunGenApp.trueName;

/**
 * @ author yudonghui
 * @ date 2017/4/18
 * @ describe May the Buddha bless bug-free！！
 */
public class RealNameActivity extends BaseActivity {
    private LinearLayout mActivity_true_name;
    private EditText mEt_name;
    private EditText mEt_surname;
    private EditText mEt_idcard;
    private LinearLayout mLl_code;
    private ImageView mImage_code_logo;
    private TextView mText_obtain_code;
    private EditText mEdit_code;
    private TextView mCard_comit;
    private String truename;
    private String phone;
    private String idCard;
    private Handler mHandler = new Handler();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_true_name);
        mContext = this;
        truename = getIntent().getStringExtra("truename");
        phone = getIntent().getStringExtra("phone");
        idCard = getIntent().getStringExtra("idCard");
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("身份证绑定");
        mActivity_true_name = (LinearLayout) findViewById(R.id.activity_true_name);
        mEt_name = (EditText) findViewById(R.id.et_name);
        mEt_surname = (EditText) findViewById(R.id.et_surname);
        mEt_idcard = (EditText) findViewById(R.id.et_idcard);
        mLl_code = (LinearLayout) findViewById(R.id.ll_code);
        mImage_code_logo = (ImageView) findViewById(R.id.image_code_logo);
        mText_obtain_code = (TextView) findViewById(R.id.text_obtain_code);
        mEdit_code = (EditText) findViewById(R.id.edit_code);
        mCard_comit = (TextView) findViewById(R.id.card_comit);
        if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(idCard)) {
            //已经设置了真实姓名
            mLl_code.setVisibility(View.GONE);
            mCard_comit.setVisibility(View.GONE);
            mEt_idcard.setFocusable(false);
            mEt_name.setFocusable(false);
            mEt_surname.setFocusable(false);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(truename.substring(0, 1));
            for (int i = 0; i < truename.length() - 1; i++) {
                stringBuilder.append("*");
            }
            mEt_surname.setText(XunGenApp.surname);
            mEt_name.setText(stringBuilder);
            mEt_idcard.setText(idCard.substring(0, 3) + "***" + idCard.substring(idCard.length() - 3, idCard.length()));
        }
    }

    private int time;

    private void addListener() {
        mText_obtain_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 60;
                mText_obtain_code.setTextColor(getResources().getColor(R.color.gray_txt));
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
        });
        mCard_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String trueSurname = mEt_surname.getText().toString();
                final String trueName = mEt_name.getText().toString();
                idCard = mEt_idcard.getText().toString();
                final String code = mEdit_code.getText().toString();
                if (TextUtils.isEmpty(trueSurname)) {
                    ToastUtils.toast(mContext, "真实姓不能为空");
                } else if (TextUtils.isEmpty(trueName)) {
                    ToastUtils.toast(mContext, "真实名不能为空");
                } else if (TextUtils.isEmpty(idCard)) {
                    ToastUtils.toast(mContext, "身份证号不能为空");
                } else if (!idCard.equals(IDCardValidate.validate_effective(idCard))) {
                    Toast.makeText(mContext, IDCardValidate.validate_effective(idCard), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(code)) {
                    ToastUtils.toast(mContext, "验证码不能为空");
                } else {
                    //如果姓氏和当初注册的时候姓氏不同，那么就提示是否修改
                    if (!trueSurname.equals(XunGenApp.surname)) {
                        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                        hintDialogUtils.setMessage("姓氏变更，是否确认修改姓氏");
                        hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                setRealName(code, trueSurname, trueName);
                            }
                        });
                    } else {
                        setRealName(code, trueSurname, trueName);
                    }
                }
            }
        });
    }

    private void setRealName(final String code, final String trueSurname, final String truename) {
        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
        hintDialogUtils.setTitleVisiable(true);
        hintDialogUtils.setTitle("信息确认");
        hintDialogUtils.setVersionMessage("姓:  " + trueSurname + "\n" +
                "名:  " + truename + "\n" +
                "姓名:  " + trueSurname + truename + "\n" +
                "身份证号:  " + idCard + "\n");
        hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
            @Override
            public void callBack(View view) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("phone", phone);
                params.putString("code", code);
                params.putString("surname", trueSurname);
                params.putString("truename", truename);
                params.putString("idcard", idCard);
                params.putString("token", XunGenApp.token);
                mHttp.post(Url.IDCARD_CHANG_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        XunGenApp.surname = trueSurname;
                        trueName = truename;
                        XunGenApp.idCard = idCard;
                        Intent intent = new Intent();
                        intent.putExtra("truename", truename);
                        intent.putExtra("trueSurname", trueSurname);
                        intent.putExtra("idcard", idCard);
                        setResult(306, intent);
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
