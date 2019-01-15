package com.ruihuo.ixungen.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.adapter.Action2Adapter;
import com.ruihuo.ixungen.adapter.ActionAdapter;
import com.ruihuo.ixungen.adapter.ArticleAdapter;
import com.ruihuo.ixungen.adapter.NewsAdapter;
import com.ruihuo.ixungen.entity.ActionBean;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.entity.NewsModel;
import com.ruihuo.ixungen.entity.ScrollBaseBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.MyListView;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class WonderfulFragment extends Fragment {
    private View view;
    private Context mContext;
    private TitleBar mTitleBar;
    private SmartRefreshLayout mRefresh;
    private ScrollView mScrollView;
    private ViewPager mViewPager;
    private LinearLayout mLl_points;
    private LinearLayout mLl_hot;
    private View mHot_line;
    private View mAction_line;
    private MyGridView mGridView2;
    private MyGridView mGridView;
    private MyListView mListView;
    private ArticleAdapter mArticleAdapter;//相关资讯
    private Action2Adapter mAction2Adapter;//名人，国学诵
    private ActionAdapter mActionAdapter;//相关活动
    private NewsAdapter mNewsAdapter; //轮播图适配器
    private List<HotNewsEntity.DataBean> articleList = new ArrayList<>();
    private List<ActionBean.DataBean> action2List = new ArrayList<>();
    private List<ActionBean.DataBean> actionList = new ArrayList<>();
    //轮播图数据
    List<ImageView> listImage = new ArrayList<>();
    List<ScrollBaseBean> data = new ArrayList<>();
    private LinearLayout.LayoutParams layoutParams;
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wonderful, null);
        mContext = getActivity();
        initView();
        addData();
        addListener();
        return view;
    }

    private void initView() {
        mTitleBar = (TitleBar) view.findViewById(R.id.wonderful_titlebar);
        mTitleBar.setTitle("发现");
        mTitleBar.mImageBack.setVisibility(View.GONE);
        mTitleBar.mShare.setVisibility(View.GONE);
        mTitleBar.mShare.setImageResource(R.mipmap.search);
        mRefresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollView);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_news);
        mLl_points = (LinearLayout) view.findViewById(R.id.ll_points);
        mGridView2 = (MyGridView) view.findViewById(R.id.gridView2);
        mGridView = (MyGridView) view.findViewById(R.id.gridView);
        mLl_hot = (LinearLayout) view.findViewById(R.id.ll_hot);
        mHot_line = view.findViewById(R.id.hot_line);
        mAction_line = view.findViewById(R.id.action_line);
        mListView = (MyListView) view.findViewById(R.id.lv_home);

        mArticleAdapter = new ArticleAdapter(articleList, mContext, "article");
        mListView.setAdapter(mArticleAdapter);

        mActionAdapter = new ActionAdapter(actionList);
        mGridView.setAdapter(mActionAdapter);

        mAction2Adapter = new Action2Adapter(action2List);
        mGridView2.setAdapter(mAction2Adapter);

        ImageView iv = new ImageView(mContext);
        iv.setImageResource(R.mipmap.banner);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        listImage.add(iv);
        mNewsAdapter = new NewsAdapter(listImage, data, getContext());
        mViewPager.setAdapter(mNewsAdapter);
        layoutParams = new LinearLayout.LayoutParams(DisplayUtilX.dip2px(7), DisplayUtilX.dip2px(7));
        layoutParams.leftMargin = 12;
    }

    private void addListener() {
        //搜索
        mTitleBar.mShare.setOnClickListener(SearchListener);
        //上下拉的刷新
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        //
        mListView.setOnItemClickListener(ItemClickListener);
        mViewPager.addOnPageChangeListener(ViewPagerListener);
        mViewPager.setOnTouchListener(ViewPagerTouchListener);
    }

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
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            //数据请求中不让viewpager自动轮播。中间上下滚动的寻根热点停止
            refreshing = true;
            articleList.clear();
            page = 1;
            addData();
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            if (page < totalPage) {
                page++;
                newsData();
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
    };

    AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
    View.OnClickListener SearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void addData() {
        scrollData();//轮播图数据
        action2Data();//名人堂，国学诵。
        newsData();//相关资讯的列表数据
        actionData();//活动的数据
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
                    Picasso.with(getContext())
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
                        Picasso.with(getContext())
                                .load(data.get(i).getPic())
                                .placeholder(R.mipmap.banner)
                                .error(R.mipmap.banner)
                                .config(Bitmap.Config.RGB_565)
                                .into(iv);
                        listImage.add(iv);
                        //加小圆点
                        View view = new View(getContext());

                        view.setBackgroundResource(R.drawable.chase_normal);
                        view.setLayoutParams(layoutParams);
                        mLl_points.addView(view);
                    }
                    //最后加一张图片
                    ImageView ivEnd = new ImageView(mContext);
                    ivEnd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(getContext())
                            .load(data.get(0).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivEnd);
                    listImage.add(ivEnd);
                    //mNewsAdapter.setData(data);
                    // mNewsAdapter.notifyDataSetChanged();
                    mNewsAdapter = new NewsAdapter(listImage, data, getContext());
                    mViewPager.setAdapter(mNewsAdapter);
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

    private void actionData() {
        Bundle params = new Bundle();
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        mHttp.get(Url.ACTION_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                actionList.clear();
                Gson gson = GsonUtils.getGson();
                ActionBean actionBean = gson.fromJson(result, ActionBean.class);
                List<ActionBean.DataBean> data = actionBean.getData();
                for (int i = 0; i < data.size(); i++) {
                    //status 0,不显示 1，显示
                    if ("1".equals(data.get(i).getStatus())) {
                        actionList.add(data.get(i));
                    }
                }
                int num = actionList.size();
                if (num > 0) {
                    mHot_line.setVisibility(View.VISIBLE);
                    mLl_hot.setVisibility(View.VISIBLE);
                    if (num >= 4) {
                        mGridView.setNumColumns(2);
                    } else
                        mGridView.setNumColumns(num);
                    mActionAdapter.notifyDataSetChanged();
                } else {
                    mHot_line.setVisibility(View.GONE);
                    mLl_hot.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                if (actionList.size() > 0) {
                    mHot_line.setVisibility(View.VISIBLE);
                    mLl_hot.setVisibility(View.VISIBLE);
                } else {
                    mHot_line.setVisibility(View.GONE);
                    mLl_hot.setVisibility(View.GONE);
                }

            }
        });
    }

    private void action2Data() {
        Bundle params = new Bundle();
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        params.putString("bundle", "2");
        mHttp.get(Url.ACTION_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                action2List.clear();
                Gson gson = GsonUtils.getGson();
                ActionBean actionBean = gson.fromJson(result, ActionBean.class);
                action2List.addAll(actionBean.getData());
                if (action2List.size() > 0) {
                    mAction_line.setVisibility(View.VISIBLE);
                } else {
                    mAction_line.setVisibility(View.GONE);
                }
                mAction2Adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message) {
                if (action2List.size() > 0) {
                    mAction_line.setVisibility(View.VISIBLE);
                } else {
                    mAction_line.setVisibility(View.GONE);
                }
            }
        });
    }

    private void newsData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("page", page + "");

        mHttp.get(Url.DISCOVER, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    totalPage = jsonObject.getInt("totalPage");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray big_picture_news = jsonObject1.getJSONArray("big_picture_news");
                        JSONArray common_news = jsonObject1.getJSONArray("common_news");
                        if (articleList.size() == 0 && big_picture_news != null && big_picture_news.length() > 0) {
                            HotNewsEntity.DataBean hotNewsEntity = new HotNewsEntity.DataBean();
                            JSONObject jsonObject2 = big_picture_news.getJSONObject(0);
                            hotNewsEntity.setAuthor(jsonObject2.getString("author"));
                            hotNewsEntity.setPicurl(jsonObject2.getString("picurl"));
                            hotNewsEntity.setStatus(jsonObject2.getString("status"));
                            hotNewsEntity.setCity(jsonObject2.getString("city"));
                            hotNewsEntity.setTitle(jsonObject2.getString("title"));
                            hotNewsEntity.setCount(jsonObject2.getString("count"));
                            hotNewsEntity.setCreate_time(jsonObject2.getString("create_time"));
                            hotNewsEntity.setId(jsonObject2.getString("id"));
                            articleList.add(hotNewsEntity);
                        }
                        if (common_news != null && common_news.length() > 0) {
                            for (int i = 0; i < common_news.length(); i++) {
                                JSONObject jsonObject3 = common_news.getJSONObject(i);
                                String status = jsonObject3.getString("status");
                                if (!"0".equals(status) && !"5".equals(status)) {
                                    HotNewsEntity.DataBean hotNewsEntity = new HotNewsEntity.DataBean();
                                    hotNewsEntity.setAuthor(jsonObject3.getString("author"));
                                    hotNewsEntity.setPicurl(jsonObject3.getString("picurl"));
                                    hotNewsEntity.setStatus(jsonObject3.getString("status"));
                                    hotNewsEntity.setCity(jsonObject3.getString("city"));
                                    hotNewsEntity.setTitle(jsonObject3.getString("title"));
                                    hotNewsEntity.setCount(jsonObject3.getString("count"));
                                    hotNewsEntity.setCreate_time(jsonObject3.getString("create_time"));
                                    hotNewsEntity.setId(jsonObject3.getString("id"));
                                    articleList.add(hotNewsEntity);
                                }
                            }
                        }
                        mArticleAdapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });
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
}
