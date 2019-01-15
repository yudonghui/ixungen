package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * @author yudonghui
 * @date 2017/4/21
 * @describe May the Buddha bless bug-free!!!
 */
public class PicassoUtils {
    public static void setImgView(String url, Context mContext, int defaultResId, ImageView mImageView) {

        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext)
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(defaultResId)
                    .error(defaultResId)
                    .into(mImageView);
        }
    }

    public static void setImgView(String url, int defaultResId, Context mContext, ImageView mImageView) {

        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext)
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(defaultResId)
                    .error(defaultResId)
                    .into(mImageView);
        }
    }
}
