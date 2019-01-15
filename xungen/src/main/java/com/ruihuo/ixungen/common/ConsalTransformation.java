package com.ruihuo.ixungen.common;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author yudonghui
 * @date 2017/4/13
 * @describe May the Buddha bless bug-free!!!
 */
public class ConsalTransformation implements com.squareup.picasso.Transformation {
    private Context mContext;
    private int targetWidth;
    private int targetHeight;

    public ConsalTransformation(Context mContext, int targetWidth, int targetHeight) {
        this.mContext = mContext;
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source.getWidth() == 0 || source.getHeight() == 0) {
            return source;
        }
        float targetRatio = targetWidth / targetHeight;
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        float sourceRatio = sourceWidth / sourceHeight;
        int x = 0;
        int y = 0;
        if (sourceRatio >= targetRatio) {
            x = (int) ((sourceWidth - sourceHeight * targetRatio) / 2);
            sourceWidth = (int) (sourceHeight * targetRatio);
        } else {
            y = (int) ((sourceHeight - sourceWidth / targetRatio) / 2);
            sourceHeight = (int) (sourceWidth / targetRatio);

        }
        /*LogUtils.e("y+sourceheight", y + sourceHeight + "");
        LogUtils.e("x+sourceWidth", x + sourceWidth + "");
        LogUtils.e("sourceheight", source.getHeight() + "");
        LogUtils.e("sourcewidth", source.getWidth() + "");*/
        Bitmap bitmap = Bitmap.createBitmap(source, x, y, sourceWidth, sourceHeight);
        if (bitmap != source) {
            source.recycle();
        }
        return bitmap;
    }

    @Override
    public String key() {
        return "desiredWidth" + " desiredHeight";
    }
}
