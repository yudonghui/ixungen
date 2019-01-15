package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;

import io.rong.imkit.RongIM;

/**
 * @author yudonghui
 * @date 2017/8/23
 * @describe May the Buddha bless bug-free!!!
 */
public class ContactShops {
    private Context mContext;
    private TextView mCamera;
    private TextView mPhoto;
    private Button mCancel;
    private View inflate;
    private Dialog mDialog;
    private Window window;

    public ContactShops(Context mContext) {
        this.mContext = mContext;
        //有关相机调用
        inflate = View.inflate(mContext, R.layout.photo_popup, null);
        mCamera = (TextView) inflate.findViewById(R.id.buttonCamera);
        mCamera.setText("在线联系");
        mPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        mPhoto.setText("拨打电话");
        mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        addListener();
    }

    public void addListener() {
        //在线联系
        mCamera.setOnClickListener(InternetListener);
        //拨打电话
        mPhoto.setOnClickListener(TelephoneListener);
        //取消
        mCancel.setOnClickListener(CancelListener);
    }

    private String rid;
    private String phone;

    public void showDialog(String rid, String phone) {
        this.rid = rid;
        this.phone = phone;
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
    }

    View.OnClickListener InternetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(rid)) return;
            mDialog.dismiss();
            RongIM.getInstance().startPrivateChat(mContext, rid, "寻根客服");
        }
    };
    View.OnClickListener TelephoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(phone)) return;
            mDialog.dismiss();
            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
            hintDialogUtils.setMessage("您将拨打" + phone);
            hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                @Override
                public void callBack(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mContext.startActivity(intent);
                }
            });
        }
    };
    View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
        }
    };

}
