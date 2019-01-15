package com.ruihuo.ixungen.utils.dialog;

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
 * @date 2017/4/5
 * @describe May the Buddha bless bug-free!!!
 */
public class SexDialogUtils {
    private static LinearLayout mLlWoman;
    private static LinearLayout mLlMan;
    private static ImageView mImageMan;
    private static ImageView mImageWoman;
    private static LinearLayout mLlSecret;
    private static ImageView mImageSecret;
    private static Dialog mDialog;
    private static DialogEditInterface mDialogInterface;

    public static void setDialog(Context mContext, String sex, DialogEditInterface mDialogInterfac) {
        mDialogInterface = mDialogInterfac;
        mDialog = new Dialog(mContext, R.style.HintDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.sex_select, null);
        mLlMan = (LinearLayout) view.findViewById(R.id.ll_man);
        mLlWoman = (LinearLayout) view.findViewById(R.id.ll_woman);
        mImageMan = (ImageView) view.findViewById(R.id.image_man);
        mImageWoman = (ImageView) view.findViewById(R.id.image_woman);
        mLlSecret= (LinearLayout) view.findViewById(R.id.ll_secret);
        mImageSecret= (ImageView) view.findViewById(R.id.image_secret);
        if ("1".equals(sex)) {
            mImageMan.setImageResource(R.drawable.chase_press);
            mImageWoman.setImageResource(R.drawable.chase_normal);
            mImageSecret.setImageResource(R.drawable.chase_normal);
        } else if("2".equals(sex)){
            mImageMan.setImageResource(R.drawable.chase_normal);
            mImageWoman.setImageResource(R.drawable.chase_press);
            mImageSecret.setImageResource(R.drawable.chase_normal);
        }else {
            mImageMan.setImageResource(R.drawable.chase_normal);
            mImageWoman.setImageResource(R.drawable.chase_normal);
            mImageSecret.setImageResource(R.drawable.chase_press);
        }
        addListener();
        mDialog.setContentView(view);
        mDialog.show();
    }

    private static void addListener() {
        mLlMan.setOnClickListener(WomanOrManListener);
        mLlWoman.setOnClickListener(WomanOrManListener);
        mLlSecret.setOnClickListener(WomanOrManListener);
    }

    static View.OnClickListener WomanOrManListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_man:
                    mImageMan.setImageResource(R.drawable.chase_press);
                    mDialogInterface.callBack("1");
                    break;
                case R.id.ll_woman:
                    mImageWoman.setImageResource(R.drawable.chase_press);
                    mDialogInterface.callBack("2");
                    break;
                case R.id.ll_secret:
                    mImageSecret.setImageResource(R.drawable.chase_press);
                    mDialogInterface.callBack("0");
                    break;
            }
            mDialog.dismiss();
        }
    };

}
