package com.ruihuo.ixungen.activity.merchant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/2
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoAdapter extends BaseAdapter {
    List<String> imgList;
    Context mContext;
    private int resize;
    private boolean isCheck;
    private int mode;

    public PhotoAdapter(List<String> imgList, Context mContext) {
        this.imgList = imgList;
        this.mContext = mContext;
        int displayHeight = DisplayUtilX.getDisplayHeight();
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int space = DisplayUtilX.dip2px(15);
        resize = (displayWidth - space) / 2;
    }


    public void setFlag(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* ViewHolder mViewHolder;
        if (convertView == null) {
            if (position == 0) {
                convertView = View.inflate(mContext, R.layout.item_photo_form_detail_default, null);
                mViewHolder = new ViewHolder();
                mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.add_photo);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize);
                mViewHolder.imageView.setLayoutParams(layoutParams);
            } else {
                convertView = View.inflate(mContext, R.layout.item_photo_form, null);
                mViewHolder = new ViewHolder();
                mViewHolder.mPhoto_cover = (ImageView) convertView.findViewById(R.id.photo_cover);
                mViewHolder.mCheck = (ImageView) convertView.findViewById(R.id.check);
                mViewHolder.mPhoto_name = (TextView) convertView.findViewById(R.id.photo_name);
                mViewHolder.mPhoto_desc = (TextView) convertView.findViewById(R.id.photo_desc);
                mViewHolder.mPhoto_name.setVisibility(View.GONE);
                mViewHolder.mPhoto_desc.setVisibility(View.GONE);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
                mViewHolder.mPhoto_cover.setLayoutParams(layoutParams);
                mViewHolder.mPhoto_cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (position!=0){
            if (isCheck) {
                mViewHolder.mCheck.setVisibility(View.VISIBLE);
            } else mViewHolder.mCheck.setVisibility(View.GONE);

            try {
                String path = imgList.get(position);
                if (path.contains("https://")) {
                    Picasso.with(mContext)
                            .load(path)
                            .placeholder(R.mipmap.default_photo)
                            .error(R.mipmap.default_photo)
                            .into(mViewHolder.mPhoto_cover);
                } else {
                  *//*  Bitmap bitmap = BitmapFactory.decodeFile(path);
                    mPhoto_cover.setImageBitmap(bitmap);
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();  //回收图片所占的内存
                        System.gc(); //提醒系统及时回收
                    }*//*
                    adjustImage(mContext, mViewHolder.mPhoto_cover, path);
                }
            } catch (Exception e) {

            }
        }*/

        if (position == 0) {
            convertView = View.inflate(mContext, R.layout.item_photo_form_detail_default, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.add_photo);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize);
            imageView.setLayoutParams(layoutParams);
        } else {
            convertView = View.inflate(mContext, R.layout.item_photo_form, null);
            ImageView mPhoto_cover = (ImageView) convertView.findViewById(R.id.photo_cover);
            ImageView mCheck = (ImageView) convertView.findViewById(R.id.check);
            TextView mPhoto_name = (TextView) convertView.findViewById(R.id.photo_name);
            TextView mPhoto_desc = (TextView) convertView.findViewById(R.id.photo_desc);
            mPhoto_name.setVisibility(View.GONE);
            mPhoto_desc.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
            mPhoto_cover.setLayoutParams(layoutParams);
            mPhoto_cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (isCheck) {
                mCheck.setVisibility(View.VISIBLE);
            } else mCheck.setVisibility(View.GONE);

            try {
                String path = imgList.get(position);
                if (path.contains("https://")) {
                    Picasso.with(mContext)
                            .load(path)
                            .placeholder(R.mipmap.default_photo)
                            .error(R.mipmap.default_photo)
                            .into(mPhoto_cover);
                } else {
                /*    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    mPhoto_cover.setImageBitmap(bitmap);
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();  //回收图片所占的内存
                        System.gc(); //提醒系统及时回收
                    }*/
                    adjustImage(mContext, mPhoto_cover, path);
                }
            } catch (Exception e) {

            }
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView mPhoto_cover;
        private ImageView mCheck;
        private TextView mPhoto_name;
        private TextView mPhoto_desc;
        private ImageView imageView;

    }

    private void adjustImage(Context mContext, ImageView imageView, String absolutePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
       // Bitmap bm = BitmapFactory.decodeFile(absolutePath, opt);

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 获取屏的宽度和高度
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        Bitmap bm1 = BitmapFactory.decodeFile(absolutePath, opt);

        // 用imageview显示出bitmap
        imageView.setImageBitmap(bm1);
    }
}
