package com.ruihuo.ixungen.utils.dialog;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author yudonghui
 * @date 2017/3/28
 * @describe May the Buddha bless bug-free!!!
 */
public class LoadingDialogUtils {
    public LoadingDialog mLoadingDialog;

    public LoadingDialogUtils(Context mContext) {
        if (mContext == null) return;
        mLoadingDialog = new LoadingDialog(mContext);
        mLoadingDialog.show();
        WindowManager.LayoutParams lp = mLoadingDialog.getWindow().getAttributes();
        lp.alpha = 0.8f;
        Window window = mLoadingDialog.getWindow();
        try {
            window.setAttributes(lp);
        }catch (Exception e){

        }

    }

    public void show() {
        if (mLoadingDialog != null)
            mLoadingDialog.show();
    }

    public void setDimiss() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    //设置加载文字
    public void setLoadingText(String loadingText) {
        if (mLoadingDialog != null)
        mLoadingDialog.setLoadingText(loadingText);
    }

    //带有进程的下载提示
    public void setLoadingProgress(int progress) {

        if (mLoadingDialog != null)
        mLoadingDialog.setLoadingText(progress);
    }

    //判断是否显示
    public boolean isShowing() {
        if (mLoadingDialog == null) {
            return false;
        }
        return mLoadingDialog.isShowing();
    }
}
