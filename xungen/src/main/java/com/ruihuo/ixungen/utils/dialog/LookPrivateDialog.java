package com.ruihuo.ixungen.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;

/**
 * @author yudonghui
 * @date 2017/11/10
 * @describe May the Buddha bless bug-free!!!
 */
public class LookPrivateDialog {
    private Context mContext;
    private View inflate;
    private TextView mOne;
    private View mOne_lines;
    private TextView mTwo;
    private View mTwo_lines;
    private TextView mThree;
    private View mThree_lines;
    private TextView mFour;
    private Button mCancel;
    private Dialog mDialog;
    private Window window;
    private String topicId;
    private CallBackInterface mCallBack;

    public LookPrivateDialog(Context mContext, String topicId, CallBackInterface mCallBack) {
        this.mContext = mContext;
        this.topicId = topicId;
        this.mCallBack = mCallBack;
        //有关相机调用
        inflate = View.inflate(mContext, R.layout.dialog_private, null);
        mOne = (TextView) inflate.findViewById(R.id.one);
        mOne_lines = (View) inflate.findViewById(R.id.one_lines);
        mTwo = (TextView) inflate.findViewById(R.id.two);
        mTwo_lines = (View) inflate.findViewById(R.id.two_lines);
        mThree = (TextView) inflate.findViewById(R.id.three);
        mThree_lines = (View) inflate.findViewById(R.id.three_lines);
        mFour = (TextView) inflate.findViewById(R.id.four);
        mCancel = (Button) inflate.findViewById(R.id.cancel);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        addListener();
    }

    public void showDialog() {
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
    }

    private void addListener() {
        mOne.setOnClickListener(LookPrivateListener);
        mTwo.setOnClickListener(LookPrivateListener);
        mThree.setOnClickListener(LookPrivateListener);
        mFour.setOnClickListener(LookPrivateListener);
        mCancel.setOnClickListener(LookPrivateListener);
    }

    private int lookPrivate = 0;
    View.OnClickListener LookPrivateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.one:
                    lookPrivate = 2;
                    setPrivate();
                    break;
                case R.id.two:
                    lookPrivate = 1;
                    setPrivate();
                    break;
                case R.id.three:
                    lookPrivate = 3;
                    setPrivate();
                    break;
                case R.id.four:
                    delete();
                    break;
                case R.id.cancel:

                    break;
            }
            mDialog.dismiss();
        }
    };

    private void setPrivate() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("topicId", topicId);
        params.putString("private", lookPrivate + "");
        mHttp.post(Url.UPDATE_PRIVATE, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void delete() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("topicId", topicId);
        mHttp.post(Url.DELETE_POSTS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mCallBack.callBack();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
