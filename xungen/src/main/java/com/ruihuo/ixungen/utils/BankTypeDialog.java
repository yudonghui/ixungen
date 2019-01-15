package com.ruihuo.ixungen.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;

/**
 * @author yudonghui
 * @date 2017/7/17
 * @describe May the Buddha bless bug-free!!!
 */
public class BankTypeDialog {
    private LinearLayout mLlWoman;
    private LinearLayout mLlMan;
    private ImageView mImageMan;
    private ImageView mImageWoman;
    private Dialog mDialog;
    private DialogEditInterface mDialogInterface;

    public void setDialog(Context mContext, String sex, DialogEditInterface mDialogInterfac) {
        mDialogInterface = mDialogInterfac;
        mDialog = new Dialog(mContext, R.style.HintDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.bank_type, null);
        mLlMan = (LinearLayout) view.findViewById(R.id.ll_man);
        mLlWoman = (LinearLayout) view.findViewById(R.id.ll_woman);
        mImageMan = (ImageView) view.findViewById(R.id.image_man);
        mImageWoman = (ImageView) view.findViewById(R.id.image_woman);
        if ("0".equals(sex)) {
            mImageMan.setImageResource(R.drawable.chase_press);
            mImageWoman.setImageResource(R.drawable.chase_normal);
        } else if ("1".equals(sex)) {
            mImageMan.setImageResource(R.drawable.chase_normal);
            mImageWoman.setImageResource(R.drawable.chase_press);
        }
        addListener();
        mDialog.setContentView(view);
        mDialog.show();
    }

    private void addListener() {
        mLlMan.setOnClickListener(WomanOrManListener);
        mLlWoman.setOnClickListener(WomanOrManListener);
    }

    View.OnClickListener WomanOrManListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_man:
                    mImageMan.setImageResource(R.drawable.chase_press);
                    mDialogInterface.callBack("0");
                    break;
                case R.id.ll_woman:
                    mImageWoman.setImageResource(R.drawable.chase_press);
                    mDialogInterface.callBack("1");
                    break;
            }
            mDialog.dismiss();
        }
    };

}
