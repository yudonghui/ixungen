package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConsalTransformation;
import com.ruihuo.ixungen.entity.PhotoFormBean;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/10
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoFormAdapter extends BaseAdapter {
    List<PhotoFormBean.DataBean> photoList;
    Context mContext;
    int resize;
    boolean isCheck;
    String presidentId;

    public PhotoFormAdapter(List<PhotoFormBean.DataBean> photoList, Context mContext, String presidentId) {
        this.photoList = photoList;
        this.mContext = mContext;
        this.presidentId = presidentId;
        int displayHeight = DisplayUtilX.getDisplayHeight();
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int space = DisplayUtilX.dip2px(30);
        resize = (displayWidth - space) / 2;
    }

    public void setFlag(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setData(List<PhotoFormBean.DataBean> photoList) {
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ((XunGenApp.rid).equals(presidentId)) {
            if (position == 0) {
                convertView = View.inflate(mContext, R.layout.item_photo_form_default, null);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.add_photo);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize);
                imageView.setLayoutParams(layoutParams);
            } else {
                convertView = View.inflate(mContext, R.layout.item_photo_form, null);
                ImageView mPhoto_cover = (ImageView) convertView.findViewById(R.id.photo_cover);
                ImageView mCheck = (ImageView) convertView.findViewById(R.id.check);
                TextView mPhoto_name = (TextView) convertView.findViewById(R.id.photo_name);
                TextView mPhoto_desc = (TextView) convertView.findViewById(R.id.photo_desc);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
                mPhoto_cover.setLayoutParams(layoutParams);
                PhotoFormBean.DataBean dataBean = photoList.get(position);
                if (isCheck) {
                    mCheck.setVisibility(View.VISIBLE);
                } else mCheck.setVisibility(View.GONE);
                String img_url = dataBean.getImg_url();
                if (!TextUtils.isEmpty(img_url)) {
                    Picasso.with(mContext)
                            .load(img_url)
                            .config(Bitmap.Config.RGB_565)
                            .transform(new ConsalTransformation(mContext, resize, resize))
                            .placeholder(R.mipmap.default_photo)
                            .error(R.mipmap.default_photo)
                            .into(mPhoto_cover);
                }
                mPhoto_name.setText(TextUtils.isEmpty(dataBean.getAlbum_name()) ? "" : dataBean.getAlbum_name());
                String count = TextUtils.isEmpty(dataBean.getCount()) ? "" : dataBean.getCount();
                mPhoto_desc.setText(count + " · " + (TextUtils.isEmpty(dataBean.getRemark()) ? "相册" : dataBean.getRemark()));
            }
        } else {
            convertView = View.inflate(mContext, R.layout.item_photo_form, null);
            ImageView mPhoto_cover = (ImageView) convertView.findViewById(R.id.photo_cover);
            ImageView mCheck = (ImageView) convertView.findViewById(R.id.check);
            TextView mPhoto_name = (TextView) convertView.findViewById(R.id.photo_name);
            TextView mPhoto_desc = (TextView) convertView.findViewById(R.id.photo_desc);
            PhotoFormBean.DataBean dataBean = photoList.get(position);
            if (isCheck) {
                mCheck.setVisibility(View.VISIBLE);
            } else mCheck.setVisibility(View.GONE);
            String img_url = dataBean.getImg_url();
            if (!TextUtils.isEmpty(img_url)) {
                Picasso.with(mContext)
                        .load(img_url)
                        .config(Bitmap.Config.RGB_565)
                        .resize(resize, resize)
                        .centerCrop()
                        .placeholder(R.mipmap.default_photo)
                        .error(R.mipmap.default_photo)
                        .into(mPhoto_cover);
            } else {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
                mPhoto_cover.setLayoutParams(layoutParams);
            }
            mPhoto_name.setText(TextUtils.isEmpty(dataBean.getAlbum_name()) ? "" : dataBean.getAlbum_name());
            mPhoto_desc.setText(dataBean.getCount() + " · " + (TextUtils.isEmpty(dataBean.getRemark()) ? "" : dataBean.getRemark()));
        }

        return convertView;
    }

}