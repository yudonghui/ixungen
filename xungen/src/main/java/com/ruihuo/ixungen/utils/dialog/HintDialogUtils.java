package com.ruihuo.ixungen.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;

/**
 * Created by Administrator on 2017/1/4.
 */
public class HintDialogUtils {
    public Dialog mHintDialog;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView mMessage;
    private TextView mTitle;
    private View viewLine;
    private TextView mVersionMessage;

    public HintDialogUtils(Context mContext) {
        mHintDialog = new Dialog(mContext, R.style.HintDialog);
        mHintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_hint, null);
        mTitle = (TextView) view.findViewById(R.id.hint_dialog_title);
        viewLine = view.findViewById(R.id.viewline);
        mVersionMessage = (TextView) view.findViewById(R.id.version_content);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mMessage = (TextView) view.findViewById(R.id.hint_dialog_msm);
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

    public void setConfirm(String confirm, final DialogHintInterface dialogHintInterface) {
        tvConfirm.setText(confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
                dialogHintInterface.callBack(v);
            }
        });
    }

    public void setCancel(String cancel, final DialogHintInterface dialogHintInterface) {
        tvCancel.setText(cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
                dialogHintInterface.callBack(v);
            }
        });
    }

    public void setMessage(String message) {
        mMessage.setText(message);
    }

    public void setVersionMessage(String message) {
        mMessage.setVisibility(View.GONE);
        mVersionMessage.setVisibility(View.VISIBLE);
        mVersionMessage.setText(message);
    }

    public void setTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
    }

    public void setTvCancel(String cancel) {
        tvCancel.setText(cancel);
    }

    public void setTvConfirm(String confirm) {
        tvConfirm.setText(confirm);
    }

    //只显示确定按钮
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
}
