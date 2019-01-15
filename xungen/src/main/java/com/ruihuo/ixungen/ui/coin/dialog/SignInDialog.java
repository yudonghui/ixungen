package com.ruihuo.ixungen.ui.coin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/11/30
 * @describe May the Buddha bless bug-free!!!
 */
public class SignInDialog {
    private Context mContext;
    private Dialog mDialog;
    private ImageView mClose;
    private ImageView mImageView;

    public SignInDialog(Context mContext) {
        this.mContext = mContext;
        View inflate = View.inflate(mContext, R.layout.dialog_sign_in, null);
        mImageView = (ImageView) inflate.findViewById(R.id.get_award_img);
        mClose = (ImageView) inflate.findViewById(R.id.close);
        mDialog = new Dialog(mContext, R.style.CustomDialog);
        mDialog.setContentView(inflate);
        // mDialog.show();
        Window window = mDialog.getWindow();
        window.setWindowAnimations(R.style.anim_in_out);
        addListener();
    }

    public void dialogShow() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    private void addListener() {
        //点击叉叉
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
