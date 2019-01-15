package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class GridImageAdapter extends BaseAdapter {
    String[] imgUrl;
    int mode;//1店铺首页显示的时候，这个时候只能最多显示固定条数。并且图片最多显示三张
    //2评价列表  这个时候显示很多条.3朋友动态的图片，最多是九张。
    Context mContext;
    private int resize;

    public GridImageAdapter(String[] imgUrl, int mode, Context mContext) {
        this.mode = mode;
        this.imgUrl = imgUrl;
        this.mContext = mContext;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int i;
        if (mode == 3)
            i = DisplayUtilX.dip2px(74);
        else
            i = DisplayUtilX.dip2px(30);
        resize = (displayWidth - i) / 3;
    }

    @Override
    public int getCount() {
        if (mode == 1 && imgUrl.length > 3) return 3;//只能显示三张图片
        if (mode == 3 && imgUrl.length > 9) return 9;//只能显示九张图片
        return imgUrl.length;
    }

    @Override
    public Object getItem(int position) {
        return imgUrl[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_grid_image, null);
            mViewHolder = new ViewHolder();
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mNum = (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        String url = imgUrl[position];
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
        mViewHolder.imageView.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext)
                    .load(url)
                    .centerCrop()
                    .resize(resize, resize)
                    .placeholder(R.mipmap.default_photo)
                    .error(R.mipmap.default_photo)
                    .into(mViewHolder.imageView);
            // PicassoUtils.setImgView(url, R.mipmap.default_photo, mContext, mViewHolder.imageView);
        }

        if (mode == 1 && position == 2 && imgUrl.length > 3) {
            mViewHolder.mNum.setVisibility(View.VISIBLE);
            mViewHolder.mNum.setText(imgUrl.length + "");
        } else if (mode == 3 && position == 8 && imgUrl.length > 9) {
            mViewHolder.mNum.setVisibility(View.VISIBLE);
            mViewHolder.mNum.setText(imgUrl.length + "");
        } else mViewHolder.mNum.setVisibility(View.GONE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> imgList = new ArrayList<>();
                for (int i = 0; i < imgUrl.length; i++) {
                    imgList.add(imgUrl[i]);
                }
                Intent intent = new Intent(mContext, ShopGalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgList", (Serializable) imgList);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView mNum;
    }
}
