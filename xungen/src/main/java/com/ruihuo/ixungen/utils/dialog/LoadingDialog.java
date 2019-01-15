package com.ruihuo.ixungen.utils.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.view.GifView;

/**
 * @author yudonghui
 * @date 2017/3/28
 * @describe May the Buddha bless bug-free!!!
 */
public class LoadingDialog extends AlertDialog {
    private TextView loadingTextView;
    private GifView mGifView;
    private Context mContext;

    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    //一个自定义view和一个textview组成。加载提示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_refresh);
        loadingTextView = (TextView) findViewById(R.id.loading_text);
        mGifView = (GifView) findViewById(R.id.loading_gifview);
        mGifView.setMovieResource(R.raw.loading);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = DisplayUtilX.dip2px(100);
        lp.height = DisplayUtilX.dip2px(100);
        getWindow().setAttributes(lp);

    }

    public void setLoadingText(String text) {
        loadingTextView.setText(text);
    }

    public void setLoadingText(int progress) {
        if (loadingTextView != null) {
            loadingTextView.setText("努力加载中:" + progress + "%");
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
