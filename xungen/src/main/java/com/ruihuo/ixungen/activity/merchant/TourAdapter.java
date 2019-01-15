package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TourAdapter extends BaseAdapter {
    List<TravelBean.DataBean.TourPortalBean> tourList;
    private Context mContext;
    private int imgWidth;

    public TourAdapter(Context mContext, List<TravelBean.DataBean.TourPortalBean> tourList) {
        this.tourList = tourList;
        this.mContext = mContext;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int i = DisplayUtilX.dip2px(20);
        imgWidth = (displayWidth - i) / 3;
    }

    @Override
    public int getCount() {
        return tourList.size();
    }

    @Override
    public Object getItem(int position) {
        return tourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_tour, null);
        TextView mName = (TextView) convertView.findViewById(R.id.name);
        TextView mRemark = (TextView) convertView.findViewById(R.id.remark);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.imageView);
        TravelBean.DataBean.TourPortalBean tourPortalBean = tourList.get(position);
        String name = tourPortalBean.getName();
        String summary = tourPortalBean.getSummary();
        String img_url = tourPortalBean.getImg_url();
        mName.setText(TextUtils.isEmpty(name) ? "--" : name);
        mRemark.setText(TextUtils.isEmpty(summary) ? "" : summary);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgWidth, imgWidth * 10 / 23);
        mImageView.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(img_url)) {
            Picasso.with(mContext)
                    .load(img_url)
                    .centerCrop()
                    .resize(imgWidth, imgWidth * 10 / 23)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.food)
                    .error(R.mipmap.food)
                    .into(mImageView);
        }

        return convertView;
    }
}
