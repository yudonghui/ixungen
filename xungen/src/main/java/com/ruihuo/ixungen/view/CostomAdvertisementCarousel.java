package com.ruihuo.ixungen.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.home.NewsSearchActivity;
import com.ruihuo.ixungen.entity.HotNewsEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class CostomAdvertisementCarousel extends LinearLayout {
    private LinearLayout mBannerll1;
    private LinearLayout mBannerll2;
    private TextView mTv1, mTv2, mTv3, mTv4;
    private Handler handler;
    private boolean isShow;
    private int startY1, endY1, startY2, endY2;
    private Runnable runnable;
    private List<HotNewsEntity.DataBean> list;
    private int position = 0;
    private Context mContext;

    public CostomAdvertisementCarousel(Context context) {
        this(context, null);
        mContext = context;
    }

    public CostomAdvertisementCarousel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public CostomAdvertisementCarousel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_scroll_banner, this);
        mBannerll1 = (LinearLayout) view.findViewById(R.id.tv_banner1);
        mBannerll2 = (LinearLayout) view.findViewById(R.id.tv_banner2);
        mTv1 = (TextView) view.findViewById(R.id.tv1);
        mTv2 = (TextView) view.findViewById(R.id.tv2);
        mTv3 = (TextView) view.findViewById(R.id.tv3);
        mTv4 = (TextView) view.findViewById(R.id.tv4);
        addListener();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isShow = !isShow;

                position = position + 2;
                if (position >= list.size())
                    position = 0;
                // String title1=list.get(position).getTitle();
                String content1 = list.get(position).getTitle();
                //String title2;
                String content2;
                //防止新闻为奇数
                if ((position + 1) == list.size()) {
                    //  title2=list.get(0).getTitle();
                    content2 = list.get(0).getTitle();
                } else {
                    // title2=list.get(position+1).getTitle();
                    content2 = list.get(position + 1).getTitle();
                }
                /*SpannableStringBuilder ss1=new SpannableStringBuilder(title1+" "+content1);
                SpannableStringBuilder ss2=new SpannableStringBuilder(title2+" "+content2);
                ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                ss1.setSpan(span, 0, title1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss2.setSpan(span, 0, title2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (isShow) {
                    mTv1.setText(ss1);
                    mTv2.setText(ss2);
                } else {
                    mTv3.setText(ss1);
                    mTv4.setText(ss2);
                }*/
                if (isShow) {
                    mTv1.setText(content1);
                    mTv2.setText(content2);
                } else {
                    mTv3.setText(content1);
                    mTv4.setText(content2);
                }
                startY2 = isShow ? 0 : getHeight();
                endY2 = isShow ? -getHeight() : 0;
                startY1 = isShow ? getHeight() : 0;
                endY1 = isShow ? 0 : -getHeight();
                ObjectAnimator.ofFloat(mBannerll1, "translationY", startY1, endY1).setDuration(300).start();
                ObjectAnimator.ofFloat(mBannerll2, "translationY", startY2, endY2).setDuration(300).start();
                handler.postDelayed(runnable, 3000);
            }
        };

    }

    private void addListener() {
        mBannerll1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (list!=null&&list.size()>0){
                    Intent intent = new Intent(mContext, H5Activity.class);
                    intent.putExtra("newsId", list.get(position).getId());
                    //intent.putExtra("author", dataBean.getAuthor());
                    intent.putExtra("from", ConstantNum.ARTICLE_DETAIL);
                    mContext.startActivity(intent);
                }*/
                mContext.startActivity(new Intent(mContext, NewsSearchActivity.class));
            }
        });
        mBannerll2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (list!=null&&list.size()>0){
                    Intent intent = new Intent(mContext, H5Activity.class);
                    if (position + 1 >= list.size()) {
                        intent.putExtra("newsId", list.get(0).getId());
                    } else {
                        intent.putExtra("newsId", list.get(position + 1).getId());
                    }
                    intent.putExtra("from", ConstantNum.ARTICLE_DETAIL);
                    mContext.startActivity(intent);
                }*/
                mContext.startActivity(new Intent(mContext, NewsSearchActivity.class));
            }
        });
    }


    public List<HotNewsEntity.DataBean> getList() {
        return list;
    }

    public void setList(List<HotNewsEntity.DataBean> list) {
        this.list = list;
    }

    public void startScroll() {
        handler.post(runnable);
    }

    public void stopScroll() {
        handler.removeCallbacks(runnable);
    }
}
