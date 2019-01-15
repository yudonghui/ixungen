package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.adapter.NewsAdapter;
import com.ruihuo.ixungen.entity.NewsModel;
import com.ruihuo.ixungen.entity.ScrollBaseBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class TestActivity extends AppCompatActivity {
    private Context mContext;
    private SmartRefreshLayout mRefresh;
    private ScrollView mScrollView;
    private ViewPager mViewPager;
    private LinearLayout mLl_points;
    //轮播图数据
    List<ImageView> listImage = new ArrayList<>();
    List<ScrollBaseBean> data = new ArrayList<>();
    private LinearLayout.LayoutParams layoutParams;
    private NewsAdapter mNewsAdapter; //轮播图适配器
    //用于控制是否自动轮播viewpager.true,正在刷新，不主动轮播。
    private boolean refreshing = true;
    private int currentPosition = 1;
    private int prePosition = 0;
    private int msgWhat = 0;
    private boolean flag = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!refreshing) {
                currentPosition++;
                mViewPager.setCurrentItem(currentPosition);
            }
            mHandler.sendEmptyMessageDelayed(msgWhat, 2000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        mContext = this;
        initView();
      /*  mLl = (LinearLayout) findViewById(R.id.linearLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 250);
        XGCardView xgCardView = new XGCardView(this);
        xgCardView.setLayoutParams(layoutParams);
        xgCardView.setX(200);
        xgCardView.setY(200);
        XGCardView xgCardView2 = new XGCardView(this);
        xgCardView2.setLayoutParams(layoutParams);
        xgCardView2.setX(300);
        xgCardView2.setY(400);
        XGCardView xgCardView3 = new XGCardView(this);
        xgCardView3.setLayoutParams(layoutParams);
        xgCardView3.setX(400);
        xgCardView3.setY(600);
        mLl.addView(xgCardView);
        mLl.addView(xgCardView2);
        mLl.addView(xgCardView3);*/
    }

    //防止内存泄露，停止发送消息。当回来的时候再继续
    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(msgWhat, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeMessages(msgWhat);
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mViewPager = (ViewPager) findViewById(R.id.vp_news);
        mLl_points = (LinearLayout) findViewById(R.id.ll_points);
        ImageView iv = new ImageView(mContext);
        iv.setImageResource(R.mipmap.banner);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        listImage.add(iv);
        mNewsAdapter = new NewsAdapter(listImage, data, this);
        mViewPager.setAdapter(mNewsAdapter);
        layoutParams = new LinearLayout.LayoutParams(DisplayUtilX.dip2px(7), DisplayUtilX.dip2px(7));
        layoutParams.leftMargin = 12;
        mViewPager.addOnPageChangeListener(ViewPagerListener);
        mViewPager.setOnTouchListener(ViewPagerTouchListener);
        //上下拉的刷新
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
    }

    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            //数据请求中不让viewpager自动轮播。中间上下滚动的寻根热点停止
            refreshing = true;
            addData();
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            mRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                    mRefresh.finishLoadMore();
                }
            }, 1000);

        }
    };
    ViewPager.OnPageChangeListener ViewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                currentPosition = listImage.size() - 2;
                flag = true;
            } else if (position == listImage.size() - 1) {
                currentPosition = 1;
                flag = true;
            } else {
                currentPosition = position;
            }
            mLl_points.getChildAt(prePosition).setBackgroundResource(R.drawable.chase_normal);
            mLl_points.getChildAt(currentPosition - 1).setBackgroundResource(R.drawable.chase_press);
            prePosition = currentPosition - 1;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE && flag) {
                flag = false;
                mViewPager.setCurrentItem(currentPosition, false);
            }
        }
    };
    View.OnTouchListener ViewPagerTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    refreshing = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    refreshing = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    refreshing = true;
                    break;
            }

            return false;
        }
    };

    private void addData() {
        scrollData();//轮播图数据
    }

    private void scrollData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("activityId", "0");
        mHttp.get(Url.ACTION_SCROLL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                NewsModel newsModel = gson.fromJson(result, NewsModel.class);
                if (newsModel.getCode() == 0) {
                    data.clear();
                    data.addAll(newsModel.getData());
                    //只有请求到后台有数据的时候才执行
                    if (data.size() == 0) {
                        return;
                    }
                    listImage.clear();
                    mLl_points.removeAllViews();
                    //最前面加一张图片
                    ImageView ivFirst = new ImageView(mContext);
                    ivFirst.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(mContext)
                            .load(data.get(data.size() - 1).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivFirst);
                    listImage.add(ivFirst);
                    for (int i = 0; i < data.size(); i++) {
                        Log.e("图片网址", data.get(i).getPic());
                        ImageView iv = new ImageView(mContext);
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Picasso.with(mContext)
                                .load(data.get(i).getPic())
                                .placeholder(R.mipmap.banner)
                                .error(R.mipmap.banner)
                                .config(Bitmap.Config.RGB_565)
                                .into(iv);
                        listImage.add(iv);
                        //加小圆点
                        View view = new View(mContext);

                        view.setBackgroundResource(R.drawable.chase_normal);
                        view.setLayoutParams(layoutParams);
                        mLl_points.addView(view);
                    }
                    //最后加一张图片
                    ImageView ivEnd = new ImageView(mContext);
                    ivEnd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(mContext)
                            .load(data.get(0).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivEnd);
                    listImage.add(ivEnd);
                    mNewsAdapter.setData(data);
                    mNewsAdapter.notifyDataSetChanged();
                    //请求数据完成后才允许自动轮播
                    refreshing = false;
                }
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

}
