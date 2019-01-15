package com.ruihuo.ixungen.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;

/**
 * @author yudonghui
 * @date 2017/4/10
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoDialogUtils {
    public Dialog mHintDialog;
    private TextView tvCancel;
    private TextView tvConfirm;
    //  private static TextView mMessage;
    // private static TextView mTitle;
    //private static View viewLine;
    private EditText mPhotoName;
    private EditText mPhotoRemark;
    private Context mContext;

    public PhotoDialogUtils(Context mContext) {
        this.mContext = mContext;
        mHintDialog = new Dialog(mContext, R.style.HintDialog);
        mHintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_add_photo, null);
        mPhotoName = (EditText) view.findViewById(R.id.photo_name);
        mPhotoRemark = (EditText) view.findViewById(R.id.photo_remark);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
            }
        });
        mHintDialog.setContentView(view);
        mHintDialog.show();
    }

    public void setConfirm(final String associationId, final DialogEditInterface dialogHintInterface) {
        //tvConfirm.setText(confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoName = mPhotoName.getText().toString();
                String photoRemark = mPhotoRemark.getText().toString();
                if (TextUtils.isEmpty(photoName)) {
                    ToastUtils.toast(mContext, "相册名字不能为空");
                } else {
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("albumName", photoName);
                    params.putString("associationId", associationId);
                    if (!TextUtils.isEmpty(photoRemark)) {
                        params.putString("remark", photoRemark);
                    }
                    mHttp.post(Url.ADD_AGNATION_PHOTO_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            mHintDialog.dismiss();
                            dialogHintInterface.callBack("");
                        }

                        @Override
                        public void onError(String message) {
                            mHintDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
    public void setConfirm( final DialogEditInterface dialogHintInterface) {
        //tvConfirm.setText(confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoName = mPhotoName.getText().toString();
                String photoRemark = mPhotoRemark.getText().toString();
                if (TextUtils.isEmpty(photoName)) {
                    ToastUtils.toast(mContext, "相册名字不能为空");
                } else {
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("albumName", photoName);
                    if (!TextUtils.isEmpty(photoRemark)) {
                        params.putString("address", photoRemark);
                    }
                    mHttp.post(Url.ACTION_ADD_PHOTO, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            mHintDialog.dismiss();
                            dialogHintInterface.callBack("");
                        }

                        @Override
                        public void onError(String message) {
                            mHintDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}
