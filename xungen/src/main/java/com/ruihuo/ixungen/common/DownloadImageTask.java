package com.ruihuo.ixungen.common;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * @author yudonghui
 * @date 2017/9/8
 * @describe May the Buddha bless bug-free!!!
 */
public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
    InterfaceDrawable mCallBack;

    public DownloadImageTask(InterfaceDrawable mCallBack) {
        this.mCallBack = mCallBack;
    }

    protected Drawable doInBackground(String... urls) {
        return loadImageFromNetwork(urls[0]);
    }

    protected void onPostExecute(Drawable result) {
        mCallBack.callBack(result);
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {

        }
        if (drawable == null) {

        } else {

        }

        return drawable;
    }

    public interface InterfaceDrawable {
        void callBack(Drawable drawable);
    }
}
