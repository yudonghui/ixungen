package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * @author yudonghui
 * @date 2017/3/28
 * @describe May the Buddha bless bug-free!!!
 */
public class ArticleAdapter extends BaseAdapter {
    List<HotNewsEntity.DataBean> mArticleList;
    //int[] resId={R.drawable.test1,R.drawable.test2,R.drawable.test3};
    Context mContext;
    String from;
    int resize;
    int displayWidth;
    private int dp5;

    public ArticleAdapter(List<HotNewsEntity.DataBean> mArticleList, Context mContext, String from) {
        this.mArticleList = mArticleList;
        this.mContext = mContext;
        this.from = from;
        displayWidth = DisplayUtilX.getDisplayWidth();
        dp5 = DisplayUtilX.dip2px(5);
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
            mViewHolder.mLl_normal = (LinearLayout) convertView.findViewById(R.id.ll_normal);
            mViewHolder.mFrameLayout = (FrameLayout) convertView.findViewById(R.id.frameLayout);
            mViewHolder.mNews_bg = (ImageView) convertView.findViewById(R.id.news_bg);
            mViewHolder.mNews_text = (TextView) convertView.findViewById(R.id.news_text);
            mViewHolder.mNews_text.setSelected(true);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final HotNewsEntity.DataBean dataBean = mArticleList.get(position);
        String title = dataBean.getTitle();
        String picurl = dataBean.getPicurl();
        String status = dataBean.getStatus();
        if ("5".equals(status)) {
            mViewHolder.mFrameLayout.setVisibility(View.VISIBLE);
            mViewHolder.mLl_normal.setVisibility(View.GONE);
            mViewHolder.mNews_text.setText(TextUtils.isEmpty(title) ? "" : title);
            mViewHolder.mNews_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(displayWidth - (2 * dp5), (int) ((displayWidth - 2 * dp5) * 0.5));
            layoutParams.setMargins(dp5, 10, dp5, 0);
            mViewHolder.mFrameLayout.setLayoutParams(layoutParams);
            if (!TextUtils.isEmpty(picurl)) {
                String[] split = picurl.split("\\,");
                Picasso.with(mContext)
                        .load(split[0])
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.banner)
                        .error(R.mipmap.banner)
                        .into(mViewHolder.mNews_bg);
            }
        } else {
            mViewHolder.mFrameLayout.setVisibility(View.GONE);
            mViewHolder.mLl_normal.setVisibility(View.VISIBLE);

            mViewHolder.mTitle.setText(title);
            mViewHolder.mAuthor.setText(TextUtils.isEmpty(dataBean.getAuthor()) ? "寻根网" : dataBean.getAuthor());
            mViewHolder.mReads.setText("阅读：" + dataBean.getCount());
            //这个长度单位是秒
            String create_time = dataBean.getCreate_time();
            mViewHolder.mCreatTime.setText(DateFormatUtils.longToDate(Long.parseLong(create_time + "000")));

            /**
             *如果图片地址不为空的话，显示imageview控件并取出第一个图片连接，显示图片
             * 如果为空，那么直接隐藏imageview
             */
            mViewHolder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 3 / 4);
            layoutParams.setMargins(0, 10, 0, 10);
            mViewHolder.mImageView.setLayoutParams(layoutParams);
            if (TextUtils.isEmpty(picurl)) {
                mViewHolder.mImageView.setVisibility(View.GONE);
            } else {
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
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, H5Activity.class);
                intent.putExtra("newsId", dataBean.getId());
                //intent.putExtra("author", dataBean.getAuthor());
                intent.putExtra("from", ConstantNum.ARTICLE_DETAIL);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout mLl_normal;
        FrameLayout mFrameLayout;
        private ImageView mNews_bg;
        private TextView mNews_text;
        TextView mTitle;
        ImageView mImageView;
        TextView mAuthor;
        TextView mReads;
        TextView mCreatTime;

    }

   /* class GridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resId.length;
        }

        @Override
        public Object getItem(int position) {
            return resId[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(parent.getContext(), R.layout.item_gridview, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.item_image);
            imageView.setImageResource(resId[position]);
            return convertView;
        }
    }*/
}
