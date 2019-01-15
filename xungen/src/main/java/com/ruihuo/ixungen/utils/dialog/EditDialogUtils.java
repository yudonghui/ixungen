package com.ruihuo.ixungen.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;

/**
 * @author yudonghui
 * @date 2017/4/5
 * @describe May the Buddha bless bug-free!!!
 */
public class EditDialogUtils {
    public Dialog mHintDialog;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView mMessage;
    private TextView mTitle;
    private View viewLine;
    private EditText mEditMessage;
    private EditText mEditMessageNum;

    public EditDialogUtils(Context mContext) {
        mHintDialog = new Dialog(mContext, R.style.HintDialog);
        mHintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_hint, null);
        mTitle = (TextView) view.findViewById(R.id.hint_dialog_title);
        viewLine = view.findViewById(R.id.viewline);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mMessage = (TextView) view.findViewById(R.id.hint_dialog_msm);
        mEditMessage = (EditText) view.findViewById(R.id.hint_dialog_msm_et);
        mEditMessageNum = (EditText) view.findViewById(R.id.hint_dialog_msm_etnumber);
        mMessage.setVisibility(View.GONE);
        mEditMessage.setVisibility(View.VISIBLE);
        mEditMessageNum.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
            }
        });
        mHintDialog.setContentView(view);
        mHintDialog.show();
    }

    public void setConfirm(String confirm, final DialogEditInterface dialogHintInterface) {
        tvConfirm.setText(confirm);
        mEditMessage.setVisibility(View.VISIBLE);
        mEditMessageNum.setVisibility(View.GONE);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEditMessage.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    mHintDialog.dismiss();
                    dialogHintInterface.callBack(s);
                } else dialogHintInterface.callBack("");
                ;
            }
        });
    }

    public void setConfirmNum(String confirm, final DialogEditInterface dialogHintInterface) {
        tvConfirm.setText(confirm);
        mEditMessage.setVisibility(View.GONE);
        mEditMessageNum.setVisibility(View.VISIBLE);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEditMessageNum.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    mHintDialog.dismiss();
                    dialogHintInterface.callBack(s);
                } else dialogHintInterface.callBack("");
                ;
            }
        });
    }


    public void setCancel(String cancel, final DialogEditInterface dialogHintInterface) {
        tvCancel.setText(cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
                dialogHintInterface.callBack("");
            }
        });
    }

    public void setMessage(String message) {
        mMessage.setText(message);
    }

    public void setTvCancel(String cancel) {
        tvCancel.setText(cancel);
    }

    public void setTvConfirm(String confirm) {
        tvConfirm.setText(confirm);
    }

    public void setVisibilityCancel() {
        tvCancel.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
    }

    public void setCancelable(boolean isClick) {
        mHintDialog.setCancelable(isClick);
    }

    public void setTitleVisiable(boolean isVisiable) {
        if (isVisiable) mTitle.setVisibility(View.VISIBLE);
        else mTitle.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
    }
}
