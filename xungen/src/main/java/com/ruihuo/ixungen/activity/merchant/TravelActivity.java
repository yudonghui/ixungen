package com.ruihuo.ixungen.activity.merchant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.adapter.NewsAdapter;
import com.ruihuo.ixungen.entity.NewsModel;
import com.ruihuo.ixungen.entity.ScrollBaseBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class TravelActivity extends BaseNoTitleActivity {

    private TitleBar mTitlBar;

    private SmartRefreshLayout mRefresh;
    private ScrollView mScrollView;
    private SearchViewY mSearchView;
    private LinearLayout mLl;//控制是否显示
    private ViewPager mViewPager;
    private LinearLayout llpoints;
    private MyGridView mGridView;
    /* private LinearLayout mLl_travel;
     private TextView mTravel;
     private TextView mTravel_remark;
     private ImageView mTravel_img;
     private LinearLayout mLl_hotel;
     private TextView mHotel;
     private TextView mHotel_remark;
     private ImageView mHotel_img;
     private LinearLayout mLl_food;
     private TextView mFood;
     private TextView mFood_remark;
     private ImageView mFood_img;*/
    private android.support.v7.widget.RecyclerView mRecyclerView;
    private com.ruihuo.ixungen.view.MyListView mListView;

    //用于控制是否自动轮播viewpager.true,正在刷新，不主动轮播。
    private boolean refreshing = true;
    private int currentPosition = 1;
    private int prePosition = 0;
    private int msgWhat = 0;
    private boolean flag = false;
    //轮播图数据
    List<ImageView> listImage = new ArrayList<>();
    private NewsAdapter newsAdapter;
    List<NewsModel.DataBean> data = new ArrayList<>();
    private HttpInterface mHttp;
    private ScenicFormAdapter middleAdapter;
    private NearbyShopAdatper mShopFormAdatper;
    private TourAdapter tourAdapter;
    private LoadingDialogUtils loadingDialogUtils;
    private LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        initView();
        defaultData();
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("寻根旅游");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        llpoints = (LinearLayout) findViewById(R.id.ll_points);
        mSearchView = (com.ruihuo.ixungen.view.SearchViewY) findViewById(R.id.searchView);
        mLl = (LinearLayout) findViewById(R.id.ll);
        mGridView = (MyGridView) findViewById(R.id.gridView);
   /*     mLl_travel = (LinearLayout) findViewById(R.id.ll_travel);
        mTravel = (TextView) findViewById(R.id.travel);
        mTravel_img = (ImageView) findViewById(R.id.travel_img);
        mLl_hotel = (LinearLayout) findViewById(R.id.ll_hotel);
        mHotel = (TextView) findViewById(R.id.hotel);
        mHotel_remark = (TextView) findViewById(R.id.hotel_remark);
        mHotel_img = (ImageView) findViewById(R.id.hotel_img);
        mLl_food = (LinearLayout) findViewById(R.id.ll_food);
        mFood = (TextView) findViewById(R.id.food);
        mFood_remark = (TextView) findViewById(R.id.food_remark);
        mFood_img = (ImageView) findViewById(R.id.food_img);*/
        mRecyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerView);
        mListView = (com.ruihuo.ixungen.view.MyListView) findViewById(R.id.listView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        loadingDialogUtils = new LoadingDialogUtils(mContext);

        layoutParams = new LinearLayout.LayoutParams(DisplayUtilX.dip2px(7), DisplayUtilX.dip2px(7));
        layoutParams.leftMargin = 12;
     /*   int displayWidth = DisplayUtilX.getDisplayWidth();
        int i = DisplayUtilX.dip2px(14);
        int imgWidth = (displayWidth - i) / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgWidth, imgWidth * 10 / 23);
        mHotel_img.setLayoutParams(layoutParams);
        mFood_img.setLayoutParams(layoutParams);
        mTravel_img.setLayoutParams(layoutParams);*/
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.addOnPageChangeListener(ViewPagerListener);
        mViewPager.setOnTouchListener(ViewPagerOnTouchListener);
        //搜索
        mSearchView.setSearchViewListener(SearchListener);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                formList.clear();
                page = 1;
                if (TextUtils.isEmpty(title)) {
                    saveList.clear();
                }
                if (searchFlag) shopsData();
                else
                    addData();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    page++;
                    if (searchFlag) shopsData();
                    else
                        addData();
                } else {
                    mRefresh.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            mRefresh.finishLoadMore();
                        }
                    }, 1000);
                }
            }

        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyShopBaseBean nearbyShopBaseBean = formList.get(position);
                String shopId = nearbyShopBaseBean.getId();
                int type = Integer.parseInt(nearbyShopBaseBean.getType());
                if (TextUtils.isEmpty(shopId)) return;
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TravelBean.DataBean.TourPortalBean tourPortalBean = tourList.get(position);
                String type = tourPortalBean.getType();
                if (TextUtils.isEmpty(type)) return;
                int mode = 0;
                switch (type) {
                    case "7"://麻城旅游
                        mode = 4;
                        break;
                    case "8"://麻城酒店
                        mode = 2;
                        break;
                    case "9"://麻城美食
                        mode = 1;
                        break;
                }
                Intent intent = new Intent(mContext, NearByShopActivity.class);
                intent.putExtra("type", mode);
                startActivity(intent);
            }
        });
    }

    private void defaultData() {
        ImageView iv = new ImageView(mContext);
        iv.setImageResource(R.mipmap.banner);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        listImage.add(iv);
        newsAdapter = new NewsAdapter(listImage, adList, mContext);
        mViewPager.setAdapter(newsAdapter);

        tourAdapter = new TourAdapter(mContext, tourList);
        mGridView.setAdapter(tourAdapter);

        middleAdapter = new ScenicFormAdapter(mContext, middleList);
        mRecyclerView.setAdapter(middleAdapter);

        mShopFormAdatper = new NearbyShopAdatper(formList, mContext, 2);
        mListView.setAdapter(mShopFormAdatper);
    }

    private List<ScrollBaseBean> adList = new ArrayList<>();//上面轮播图
    private List<TravelBean.DataBean.TourPortalBean> tourList = new ArrayList<>();//选择旅游 酒店 住宿
    private List<TravelBean.DataBean.PopularScenicSpotBean> middleList = new ArrayList<>();//中间数据
    private List<NearbyShopBaseBean> formList = new ArrayList<>();//下面列表
    private List<NearbyShopBaseBean> saveList = new ArrayList<>();
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;

    private void addData() {

        mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");

        mHttp.get(Url.TRAVEL_HOME, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                TravelBean travelBean = gson.fromJson(result, TravelBean.class);
                totalPage = travelBean.getTotalPage();
                TravelBean.DataBean data = travelBean.getData();

                tourList.clear();
                tourList.addAll(data.getTour_portal());
                tourAdapter.notifyDataSetChanged();

                middleList.clear();
                middleList.addAll(data.getPopular_scenic_spot());//中间左右滑动
                middleAdapter.notifyDataSetChanged();

                formList.addAll(data.getRecommend_scenic_spot());//最下面列表
                mShopFormAdatper.notifyDataSetChanged();
                saveList.addAll(data.getRecommend_scenic_spot());

                adList.clear();
                adList.addAll(data.getAd());//轮播图
                if (adList.size() > 0) {
                    listImage.clear();
                    llpoints.removeAllViews();
                    //最前面加一张图片
                    ImageView ivFirst = new ImageView(mContext);
                    ivFirst.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(mContext)
                            .load(adList.get(adList.size() - 1).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivFirst);
                    listImage.add(ivFirst);
                    for (int i = 0; i < adList.size(); i++) {
                        ImageView iv = new ImageView(mContext);
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Picasso.with(mContext)
                                .load(adList.get(i).getPic())
                                .placeholder(R.mipmap.banner)
                                .error(R.mipmap.banner)
                                .config(Bitmap.Config.RGB_565)
                                .into(iv);
                        listImage.add(iv);
                        //加小圆点
                        View view = new View(mContext);
                        view.setBackgroundResource(R.drawable.chase_normal);
                        view.setLayoutParams(layoutParams);
                        llpoints.addView(view);
                    }
                    //最后加一张图片
                    ImageView ivEnd = new ImageView(mContext);
                    ivEnd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(mContext)
                            .load(adList.get(0).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivEnd);
                    listImage.add(ivEnd);
                    newsAdapter = new NewsAdapter(listImage, adList, mContext);
                    mViewPager.setAdapter(newsAdapter);
                    //请求数据完成后才允许自动轮播
                    refreshing = false;
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });


    }

    private boolean searchFlag = false;

    private void shopsData() {
        Bundle params = new Bundle();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(mContext, "请输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }
        params.putString("key", title);
        params.putString("page", page + "");
        params.putString("limit", limit + "");
        mHttp.get(Url.SHOP_NEARBY_SHOP, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                searchFlag = true;
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                NearbyShopBean nearbyShopBean = gson.fromJson(result, NearbyShopBean.class);
                totalPage = nearbyShopBean.getTotalPage();
                formList.clear();
                formList.addAll(nearbyShopBean.getData());
                mShopFormAdatper.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

    private String title;
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {
            Log.e("onDelete", "ondelete");
            title = "";
            formList.clear();
            formList.addAll(saveList);
            searchFlag = false;
            //saveList.clear();
            mShopFormAdatper.notifyDataSetChanged();
            mLl.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSearch(String text) {
            title = text;
            page = 1;
            totalPage = 1;
            shopsData();
            mLl.setVisibility(View.GONE);
        }

        @Override
        public void onCancel() {
            title = "";
            formList.clear();
            formList.addAll(saveList);
            searchFlag = false;
            //saveList.clear();
            mShopFormAdatper.notifyDataSetChanged();
            mLl.setVisibility(View.VISIBLE);
        }

        @Override
        public void onChange(String text) {

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
            llpoints.getChildAt(prePosition).setBackgroundResource(R.drawable.chase_normal);
            llpoints.getChildAt(currentPosition - 1).setBackgroundResource(R.drawable.chase_press);
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
    View.OnTouchListener ViewPagerOnTouchListener = new View.OnTouchListener() {
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

    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(msgWhat, 2000);
    }

    //防止内存泄露，停止发送消息。当回来的时候再继续
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeMessages(msgWhat);
    }

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
}
