package com.ruihuo.ixungen.activity.merchant;

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
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.ItemClickListener;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/3
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopsPhotoFormAdapter extends BaseAdapter {
    List<ShopsPhotoFormBean.DataBean> imgList;
    Context mContext;
    private int resize;
    private boolean isCheck;
    private int mode;
    private boolean isMerchant;
    private ItemClickListener onItemListener;

    public ShopsPhotoFormAdapter(List<ShopsPhotoFormBean.DataBean> imgList, Context mContext, int mode, boolean isMerchant) {
        this.imgList = imgList;
        this.mContext = mContext;
        this.isMerchant = isMerchant;
        this.mode = mode;
        int displayHeight = DisplayUtilX.getDisplayHeight();
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int space = DisplayUtilX.dip2px(15);
        resize = (displayWidth - space) / 2;
    }

    public void setFlag(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setOnItemListener(ItemClickListener onItemListener) {
        this.onItemListener = onItemListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position == 0 && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.FOOD_ALL && mode != ConstantNum.TOUR_ALL && isMerchant) {
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

            ShopsPhotoFormBean.DataBean dataBean = imgList.get(position);
            String img = dataBean.getImg();
            if (!TextUtils.isEmpty(img)) {
                Picasso.with(mContext)
                        .load(img)
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.default_photo)
                        .error(R.mipmap.default_photo)
                        .into(mPhoto_cover);
            }
        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemListener.onLongClick(v, position);
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onClick(v, position);
            }
        });

        return convertView;
    }
}
