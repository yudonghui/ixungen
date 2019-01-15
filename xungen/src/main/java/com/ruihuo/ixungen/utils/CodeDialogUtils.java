package com.ruihuo.ixungen.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;

import java.text.SimpleDateFormat;

/**
 * @author yudonghui
 * @date 2017/7/12
 * @describe May the Buddha bless bug-free!!!
 */
public class CodeDialogUtils {
    public Dialog mHintDialog;
    private TextView mHint_dialog_title;
    private TextView mHint_dialog_msm;
    private EditText mEdit_code;
    private TextView mSend_sms;
    private TextView mTv_cancel;
    private TextView mTv_confirm;
    private Context mContext;
    private String phone;

    public CodeDialogUtils(Context mContext, String phone) {
        this.mContext = mContext;
        this.phone = phone;
        mHintDialog = new Dialog(mContext, R.style.HintDialog);
        mHintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_code, null);
        mHint_dialog_title = (TextView) view.findViewById(R.id.hint_dialog_title);
        mHint_dialog_msm = (TextView) view.findViewById(R.id.hint_dialog_msm);
        mEdit_code = (EditText) view.findViewById(R.id.edit_code);
        mSend_sms = (TextView) view.findViewById(R.id.send_sms);
        mTv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        mTv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        mTv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
            }
        });
        mTv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
            }
        });
        mHintDialog.setContentView(view);
        mHintDialog.show();
        setView();
        mSend_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView();
            }
        });
    }

    private void obtainCode() {
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

    private int time;
    private Handler mHandler = new Handler();

    private void setView() {
        time = 60;
        mSend_sms.setBackgroundResource(R.drawable.shape_gray_slide);
        mSend_sms.setTextColor(mContext.getResources().getColor(R.color.again_code_txt));
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    mSend_sms.setText("重新获取");
                    mSend_sms.setEnabled(true);
                    return;
                } else {
                    time--;
                    mSend_sms.setText("重新获取(" + time + ")");
                    mSend_sms.setEnabled(false);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
        obtainCode();
    }

    public void setConfirm(String confirm, final CodeDialogInterface dialogHintInterface) {
        mTv_confirm.setText(confirm);
        mTv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEdit_code.getText().toString().trim();
                if (!TextUtils.isEmpty(code)) {
                    mHintDialog.dismiss();
                    dialogHintInterface.callBack(code);
                } else Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setCancel(String cancel, final CodeDialogInterface dialogHintInterface) {
        mTv_cancel.setText(cancel);
        mTv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
                dialogHintInterface.callBack("");
            }
        });
    }

    public void setMessage(String message) {
        mHint_dialog_msm.setText(message);
    }

    public void setTitle(String title) {
        mHint_dialog_title.setText(title);
    }

    public void setTvCancel(String cancel) {
        mTv_cancel.setText(cancel);
    }

    public void setTvConfirm(String confirm) {
        mTv_confirm.setText(confirm);
    }

   public interface CodeDialogInterface {
        void callBack(String code);
    }
}
