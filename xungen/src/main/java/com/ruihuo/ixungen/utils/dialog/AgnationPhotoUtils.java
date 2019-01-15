package com.ruihuo.ixungen.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.CallBackInterface;

/**
 * @author yudonghui
 * @date 2017/4/11
 * @describe May the Buddha bless bug-free!!!
 */
public class AgnationPhotoUtils {
    private Context mContext;
    private TextView mDeletePhoto;
    private TextView mEditorPhoto;
    private Button mCancel;
    private View inflate;
    private Dialog mDialog;
    private Window window;

    public AgnationPhotoUtils(Context mContext) {
        this.mContext = mContext;
        //有关相机调用
        inflate = View.inflate(mContext, R.layout.photo_popup, null);
        mDeletePhoto = (TextView) inflate.findViewById(R.id.buttonCamera);
        mEditorPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        mDeletePhoto.setText("删除照片");
        mEditorPhoto.setText("编辑相册信息");
        mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void setDeletePhoto(final CallBackInterface mCallBack) {
        mDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                mCallBack.callBack();
            }
        });
    }

    public void setEditorPhoto(final CallBackInterface mCallBack) {
        mEditorPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                mCallBack.callBack();
            }
        });
    }

}
