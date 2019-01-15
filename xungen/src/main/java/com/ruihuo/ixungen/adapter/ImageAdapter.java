package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.merchant.ShopGalleryActivity;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author yudonghui
 * @date 2017/8/1
 * @describe May the Buddha bless bug-free!!!
 */
public class ImageAdapter extends BaseAdapter {
    ArrayList<String> urlList;
    Context mContext;
    int width;

    public ImageAdapter(ArrayList<String> urlList, Context mContext) {
        this.urlList = urlList;
        this.mContext=mContext;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int space = DisplayUtilX.dip2px(24);
        width = (displayWidth - space) / 3;
    }

    @Override
    public int getCount() {
        if (urlList != null)
            return urlList.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return urlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {

        }
        convertView = View.inflate(parent.getContext(), R.layout.item_img_gv, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        imageView.setLayoutParams(layoutParams);
        setImgView(urlList.get(position), R.mipmap.default_photo, parent.getContext(), imageView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShopGalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgList", (Serializable) urlList);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
    }

    private void setImgView(String url, int defaultResId, Context mContext, ImageView mImageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(defaultResId)
                    .error(defaultResId)
                    .resize(width, width)
                    .centerCrop()
                    .into(mImageView);
        } else {
            mImageView.setImageResource(defaultResId);
        }
    }
}
