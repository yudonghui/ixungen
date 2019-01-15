package com.ruihuo.ixungen.action;

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
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/26
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoFormAdapter extends BaseAdapter {
    List<HotNewsEntity.DataBean> mArticleList;
    //int[] resId={R.drawable.test1,R.drawable.test2,R.drawable.test3};
    Context mContext;
    int resize;

    public VideoFormAdapter(List<HotNewsEntity.DataBean> mArticleList, Context mContext) {
        this.mArticleList = mArticleList;
        this.mContext = mContext;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth / 4;
    }

    @Override
    public int getCount() {
        return mArticleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArticleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_article, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            mViewHolder.mAuthor = (TextView) convertView.findViewById(R.id.author);
            mViewHolder.mReads = (TextView) convertView.findViewById(R.id.readed_num);
            mViewHolder.mCreatTime = (TextView) convertView.findViewById(R.id.creattime);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final HotNewsEntity.DataBean dataBean = mArticleList.get(position);
        mViewHolder.mTitle.setText(dataBean.getTitle());
        mViewHolder.mAuthor.setText(TextUtils.isEmpty(dataBean.getAuthor()) ? "寻根网" : dataBean.getAuthor());
        mViewHolder.mReads.setText("阅读：" + dataBean.getCount());
        //这个长度单位是秒
        String create_time = dataBean.getCreate_time();
        mViewHolder.mCreatTime.setText(DateFormatUtils.longToDateM(create_time));
        String picurl = dataBean.getPicurl();
        /**
         *如果图片地址不为空的话，显示imageview控件并取出第一个图片连接，显示图片
         * 如果为空，那么直接隐藏imageview
         */
        if (TextUtils.isEmpty(picurl)) {
            mViewHolder.mImageView.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 3 / 4);
            mViewHolder.mImageView.setLayoutParams(layoutParams);
            String[] split = picurl.split("\\,");
            mViewHolder.mImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(split[0])
                    .config(Bitmap.Config.RGB_565)
                    .resize(resize, resize * 3 / 4)
                    .centerCrop()
                    .placeholder(R.mipmap.default_photo)
                    .error(R.mipmap.default_photo)
                    .into(mViewHolder.mImageView);
        }
        return convertView;
    }

    class ViewHolder {
        TextView mTitle;
        ImageView mImageView;
        TextView mAuthor;
        TextView mReads;
        TextView mCreatTime;
    }
}
