package com.ruihuo.ixungen.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/27
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionPhotoFormAdapter extends BaseAdapter {
    List<ActionPhotoFormBean.DataBean> photoList;
    Context mContext;
    int resize;
    boolean isCheck;

    public ActionPhotoFormAdapter(List<ActionPhotoFormBean.DataBean> photoList, Context mContext) {
        this.photoList = photoList;
        this.mContext = mContext;
        int displayHeight = DisplayUtilX.getDisplayHeight();
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int space = DisplayUtilX.dip2px(30);
        resize = (displayWidth - space) / 2;
    }

    public void setFlag(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setData(List<ActionPhotoFormBean.DataBean> photoList) {
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
        convertView = View.inflate(mContext, R.layout.item_photo_form, null);
        ImageView mPhoto_cover = (ImageView) convertView.findViewById(R.id.photo_cover);
       /* int padding = DisplayUtilX.dip2px(5);
        mPhoto_cover.setPadding(padding,padding,padding,padding);*/
        ImageView mCheck = (ImageView) convertView.findViewById(R.id.check);
        TextView mPhoto_name = (TextView) convertView.findViewById(R.id.photo_name);
        TextView mPhoto_desc = (TextView) convertView.findViewById(R.id.photo_desc);
        ActionPhotoFormBean.DataBean dataBean = photoList.get(position);
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
        String create_time = dataBean.getCreate_time();
        String createTime = DateFormatUtils.longToDateM(create_time);
        mPhoto_desc.setText(createTime);
        return convertView;
    }
}
