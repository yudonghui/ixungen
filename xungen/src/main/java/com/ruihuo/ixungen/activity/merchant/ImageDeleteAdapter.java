package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/14
 * @describe May the Buddha bless bug-free!!!
 */
public class ImageDeleteAdapter extends BaseAdapter {
    List<String> imgUrlList;
    Context mContext;
    private CallBackPositionInterface mListener;
    private int margin;
    private int resize;

    public ImageDeleteAdapter(List<String> imgUrlList, Context mContext, CallBackPositionInterface mListener) {
        this.imgUrlList = imgUrlList;
        this.mContext = mContext;
        this.mListener = mListener;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        margin = DisplayUtilX.dip2px(5);
        int verspace = DisplayUtilX.dip2px(41);
        resize = (displayWidth - verspace) / 4;
    }

    @Override
    public int getCount() {
        return imgUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_img_delete, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mDelete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        String s = imgUrlList.get(position);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
        mViewHolder.mImageView.setLayoutParams(layoutParams);
        layoutParams.rightMargin = margin;
        layoutParams.topMargin = margin;
      /*  layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;
        layoutParams.topMargin = margin;
        layoutParams.bottomMargin = margin;*/
        if (!TextUtils.isEmpty(s)) {
            mViewHolder.mDelete.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(s)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.add_photo_comment)
                    .error(R.mipmap.add_photo_comment)
                    .into(mViewHolder.mImageView);

        } else {
            mViewHolder.mDelete.setVisibility(View.GONE);
            mViewHolder.mImageView.setImageResource(R.mipmap.add_photo_comment);
        }
        mViewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callBack(position);
            }
        });
        if (position == imgUrlList.size() - 1) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.callBack(position);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView mImageView;
        private ImageView mDelete;
    }
}
