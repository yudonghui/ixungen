package com.ruihuo.ixungen.adapter;

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
import com.ruihuo.ixungen.entity.TidbitBean;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/20
 * @describe May the Buddha bless bug-free!!!
 */
public class TidbitAdapter extends BaseAdapter {
    List<TidbitBean.DataBean> articleList;
    Context mContext;
    int resize;

    public TidbitAdapter(List<TidbitBean.DataBean> articleList, Context mContext) {
        this.mContext = mContext;
        this.articleList = articleList;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth / 4;
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
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
            mViewHolder.mLl_normal = (LinearLayout) convertView.findViewById(R.id.ll_normal);
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            mViewHolder.mAuthor = (TextView) convertView.findViewById(R.id.author);
            mViewHolder.mReads = (TextView) convertView.findViewById(R.id.readed_num);
            mViewHolder.mCreatTime = (TextView) convertView.findViewById(R.id.creattime);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mLl_normal.setVisibility(View.VISIBLE);
        mViewHolder.mAuthor.setVisibility(View.GONE);
        TidbitBean.DataBean dataBean = articleList.get(position);
        String title = dataBean.getTitle();
        String create_time = dataBean.getCreate_time();
        String total_play = dataBean.getTotal_play();
        String img = dataBean.getImg();//图片
        String url = dataBean.getUrl();//视频播放地址
        mViewHolder.mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        if (!TextUtils.isEmpty(create_time))
            mViewHolder.mCreatTime.setText(DateFormatUtils.longToDate(Long.parseLong(create_time + "000")));
        mViewHolder.mReads.setText(getPlays(total_play));

        mViewHolder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 3 / 4);
        layoutParams.setMargins(0, 10, 0, 10);
        mViewHolder.mImageView.setLayoutParams(layoutParams);
        if (TextUtils.isEmpty(img)) {
            mViewHolder.mImageView.setVisibility(View.GONE);
        } else {
            mViewHolder.mImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(img)
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
        LinearLayout mLl_normal;
        TextView mTitle;
        ImageView mImageView;
        TextView mAuthor;
        TextView mReads;
        TextView mCreatTime;
    }

    private String getPlays(String plays) {
        long play = Long.parseLong(plays);
        if (play >= 10000 && play < 100000000) {
            double a = play / 10000;
            BigDecimal bigDecimal = new BigDecimal(a).setScale(2, RoundingMode.UP);
            return bigDecimal.doubleValue() + "万次播放";
        } else if (play >= 100000000) {
            double a = play / 100000000;
            BigDecimal bigDecimal = new BigDecimal(a).setScale(2, RoundingMode.UP);
            return bigDecimal.doubleValue() + "亿次播放";
        } else {
            return plays + "次播放";
        }

    }
}
