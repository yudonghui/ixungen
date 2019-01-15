package com.ruihuo.ixungen.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.action.ActionUserInfoActivity;
import com.ruihuo.ixungen.activity.AddNewsActivity;
import com.ruihuo.ixungen.adapter.ActionAdapter;
import com.ruihuo.ixungen.adapter.ArticleAdapter;
import com.ruihuo.ixungen.adapter.FriendStateAdapter;
import com.ruihuo.ixungen.adapter.NewsAdapter;
import com.ruihuo.ixungen.adapter.RedGvAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.entity.FriendStateBean;
import com.ruihuo.ixungen.entity.HomeStemmaBean;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.entity.NewsModel;
import com.ruihuo.ixungen.entity.ScrollBaseBean;
import com.ruihuo.ixungen.entity.XWBean;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.activity.MyTreeActivity;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.ui.main.activity.XSJPActivity;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.NetworkUtils;
import com.ruihuo.ixungen.utils.ShareUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.view.CostomAdvertisementCarousel;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.SearchViewY;
import com.squareup.picasso.Picasso;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class HomeFragment extends Fragment {
    private View view;
    private TextView mSurname;
    private List<HotNewsEntity.DataBean> mADList = new ArrayList<>();
    private LinearLayout mLlCac;
    private CostomAdvertisementCarousel costom;
    private ImageView mAdd;
    private ViewPager mViewPager;
    private MyGridView mGridView;
    private LinearLayout llpoints;
    // private PullToRefreshScrollView mPullToRefreshScrollView;
    private SmartRefreshLayout mRefresh;

    private LinearLayout mMoreStemma;
    private LinearLayout mLl_data;
    private ImageView mClan_book;
    private TextView mName;
    private TextView mBuilder;
    private TextView mDate;
    private RelativeLayout mFriend;
    private TextView mFriend_text;
    private View mFriend_block;
    private RelativeLayout mNews;
    private TextView mNews_text;
    private ImageView mCamera;
    private View mNews_block;
    private com.ruihuo.ixungen.view.MyListView mFriend_lv;
    private com.ruihuo.ixungen.view.MyListView mMessage_lv;
    private ImageView mNoData;

    private Context mContext;

    private HttpInterface mHttp;
    //轮播图数据
    List<ImageView> listImage = new ArrayList<>();
    List<ScrollBaseBean> data = new ArrayList<>();
    //轮播图适配器
    private NewsAdapter newsAdapter;
    private ActionAdapter mActionAdapter;
    //用于控制是否自动轮播viewpager.true,正在刷新，不主动轮播。
    private boolean refreshing = true;
    private int currentPosition = 1;
    private int prePosition = 0;
    private boolean flag = false;
    private ArticleAdapter mArticleAdapter;
    private List<HotNewsEntity.DataBean> articleList = new ArrayList<>();
    private FriendStateAdapter mFriendAdapter;
    private List<FriendStateBean.DataBean> friendList = new ArrayList<>();
    private int limitFriend = 15;
    private int pageFriend = 1;
    private int totalPageFriend = 1;
    private int limitNews = 15;
    private int pageNews = 1;
    private int totalPageNews = 1;
    private int msgWhat = 0;
    private TextView mEnvironment;
    private RedGvAdapter mRedGvAdapter;

    private View inflate;
    private PopupWindow mPopupWindow;
    private TextView mPopup_question;
    private View mFirstline;
    private TextView mPopup_smshint;
    private LinearLayout.LayoutParams layoutParams;
    private SearchViewY mSearchView;

    public void updateSurname() {
        mSurname.setText(XunGenApp.surname);
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
    private int identifier;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        mContext = getActivity();
        identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        initView();
        defaultData();
        addData();
        addListener();
        environment();
        return view;
    }

    private void environment() {
        mEnvironment = (TextView) view.findViewById(R.id.environment);
        //mEnvironment.setVisibility(View.VISIBLE);

        mEnvironment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActionUserInfoActivity.class);
                intent.putExtra("rid", XunGenApp.rid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(msgWhat, 2000);
    }

    //防止内存泄露，停止发送消息。当回来的时候再继续
    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeMessages(msgWhat);
    }

    private void defaultData() {
        mArticleAdapter = new ArticleAdapter(articleList, mContext, "news");
        mMessage_lv.setAdapter(mArticleAdapter);

        mFriendAdapter = new FriendStateAdapter(friendList, mContext, 0);
        mFriend_lv.setAdapter(mFriendAdapter);

        mRedGvAdapter = new RedGvAdapter(xwDataList);
        mGridView.setAdapter(mRedGvAdapter);

        ImageView iv = new ImageView(mContext);
        iv.setImageResource(R.mipmap.banner);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        listImage.add(iv);
        newsAdapter = new NewsAdapter(listImage, data, getContext());
        mViewPager.setAdapter(newsAdapter);
        // mViewPager.setCurrentItem(1000);
    }

    private void initView() {
        mSurname = (TextView) view.findViewById(R.id.text_surname);
        mSurname.setFocusable(true);
        mRefresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
       /* mPullToRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.ptrsv_home);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);*/
        mLlCac = (LinearLayout) view.findViewById(R.id.ll_cac);
        costom = (CostomAdvertisementCarousel) view.findViewById(R.id.ad_carousel);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_news);
        mCamera = view.findViewById(R.id.camera);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(DisplayUtilX.dip2px(50), DisplayUtilX.dip2px(50));
        layoutParams.gravity = Gravity.RIGHT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layoutParams.topMargin = DisplayUtilX.dip2px(20);
        } else {
            layoutParams.topMargin = DisplayUtilX.dip2px(1);
        }
        mCamera.setLayoutParams(layoutParams);
        mSearchView = view.findViewById(R.id.searchView);
        mSearchView.setBackGround(R.color.transparent);
        mSearchView.setHint("请输入姓名、姓氏、族谱、宗亲等查询");
        mAdd = (ImageView) view.findViewById(R.id.add);
        mGridView = (MyGridView) view.findViewById(R.id.gridView);
        llpoints = (LinearLayout) view.findViewById(R.id.ll_points);
        mMoreStemma = (LinearLayout) view.findViewById(R.id.moreStemma);
        mLl_data = (LinearLayout) view.findViewById(R.id.ll_data);
        mClan_book = (ImageView) view.findViewById(R.id.clan_book);
        mName = (TextView) view.findViewById(R.id.name);
        mBuilder = (TextView) view.findViewById(R.id.builder);
        mDate = (TextView) view.findViewById(R.id.date);
        mFriend = (RelativeLayout) view.findViewById(R.id.friend);
        mFriend_text = (TextView) view.findViewById(R.id.friend_text);
        mFriend_block = (View) view.findViewById(R.id.friend_block);
        mNews = (RelativeLayout) view.findViewById(R.id.news);
        mNews_text = (TextView) view.findViewById(R.id.news_text);
        mNews_block = (View) view.findViewById(R.id.news_block);
        mFriend_lv = (com.ruihuo.ixungen.view.MyListView) view.findViewById(R.id.friend_lv);
        mMessage_lv = (com.ruihuo.ixungen.view.MyListView) view.findViewById(R.id.message_lv);
        mNoData = (ImageView) view.findViewById(R.id.no_data);
        mHttp = HttpUtilsManager.getInstance(mContext);

        inflate = View.inflate(mContext, R.layout.popup_window, null);

        mPopup_question = (TextView) inflate.findViewById(R.id.popup_question);
        mPopup_question.setVisibility(View.VISIBLE);
        mPopup_question.setText("扫一扫");
        mFirstline = (View) inflate.findViewById(R.id.firstline);
        mFirstline.setVisibility(View.VISIBLE);
        mPopup_smshint = (TextView) inflate.findViewById(R.id.popup_smshint);
        mPopup_smshint.setText("分享寻根");
        mPopup_smshint.setVisibility(View.VISIBLE);

        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(DisplayUtilX.dip2px(150));
        mPopupWindow.setHeight(DisplayUtilX.dip2px(100));
        mPopupWindow.setContentView(inflate);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOnDismissListener(poponDismissListener);
        this.layoutParams = new LinearLayout.LayoutParams(DisplayUtilX.dip2px(7), DisplayUtilX.dip2px(7));
        this.layoutParams.leftMargin = 12;
    }

    PopupWindow.OnDismissListener poponDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 1f;
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mSurname.setText(XunGenApp.surname);
    }

    private void addListener() {
        //加号
        mAdd.setOnClickListener(AddListener);
        //点击照相机发布寻根问祖
        mCamera.setOnClickListener(CameraListener);
        //扫一扫
        mPopup_question.setOnClickListener(SaoSaoListener);
        mSearchView.setSearchViewListener(SearchListener);
        mMoreStemma.setOnClickListener(MoreStemmaListener);//查看更多家谱
        mLl_data.setOnClickListener(StemmaListener);//点击家谱
        //
        mFriend.setOnClickListener(TabListener);
        mNews.setOnClickListener(TabListener);
        //分享
        mPopup_smshint.setOnClickListener(ShareListener);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentSkip intentSkip = new IntentSkip();
                XWBean.DataBean dataBean = xwDataList.get(position);
                String type = dataBean.getType();
                intentSkip.skipType(mContext, type, null);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
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
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //数据请求中不让viewpager自动轮播。中间上下滚动的寻根热点停止
                refreshing = true;
                costom.stopScroll();
                articleList.clear();
                friendList.clear();
                pageFriend = 1;
                pageNews = 1;
                //刷新的过程当中
                addData();
            }
        });
        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mFriend_lv.getVisibility() == View.VISIBLE) {
                    if (pageFriend < totalPageFriend) {
                        pageFriend++;
                        friendStateData();
                    } else {
                        mRefresh.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                                mRefresh.finishLoadMore();
                            }
                        }, 1000);
                    }
                } else {
                    if (pageNews < totalPageNews) {
                        pageNews++;
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
            }
        });
    }

    private void addData() {
        if (NetworkUtils.isNetworkAvailable()) {
            //新闻滚动条
            scrollData();
            //寻根问祖，姓氏百科，等
            xgData();
            //热点新闻上下轮播
            hotNewsData();
            //我的家谱
            myStemmaData();
            //朋友动态
            friendStateData();
            //热门推荐
            newsData();
        } else {
            mRefresh.finishRefresh();
            mRefresh.finishLoadMore();
            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
            hintDialogUtils.setConfirm("重新加载", new DialogHintInterface() {
                @Override
                public void callBack(View view) {
                    addData();
                }
            });
            hintDialogUtils.setMessage("网络连接错误，请重试");
        }
    }

    public void refreshSms() {
        friendList.clear();
        pageFriend = 1;
        //我的家谱
        myStemmaData();
        //朋友动态
        friendStateData();
    }

    //刷新朋友圈动态
    public void refreshFriendState() {
        friendList.clear();
        pageFriend = 1;
        //朋友动态
        friendStateData();
    }

    List<XWBean.DataBean> xwDataList = new ArrayList<>();

    private void xgData() {
        Bundle params = new Bundle();
        mHttp.get(Url.RED_GV_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                xwDataList.clear();
                Gson gson = GsonUtils.getGson();
                XWBean xwBean = gson.fromJson(result, XWBean.class);
                xwDataList.addAll(xwBean.getData());
                mRedGvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void hotNewsData() {
        Bundle params = new Bundle();
        //1 正常，2 热点新闻，3 推荐新闻，4 广告新闻
        params.putString("status", "2");
        params.putString("type", "system_news");
        mHttp.get(Url.NEWS_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                HotNewsEntity hotNewsEntity = gson.fromJson(result, HotNewsEntity.class);
                if (hotNewsEntity.getCode() == 0) {
                    mADList.clear();
                    mADList.addAll(hotNewsEntity.getData());
                    if (mADList != null && mADList.size() != 0) {
                        mLlCac.setVisibility(View.VISIBLE);
                        costom.setList(mADList);
                        costom.startScroll();
                    } else {
                        mLlCac.setVisibility(View.GONE);
                    }
                } else {
                    mLlCac.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                mLlCac.setVisibility(View.GONE);
            }
        });
    }

    //轮播图数据
    private void scrollData() {
        Bundle params = new Bundle();
        params.putString("client", "android");
        params.putString("type", "1");
        mHttp.get(Url.ROLL_URL, params, new JsonInterface() {
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
                    llpoints.removeAllViews();
                    //最前面加一张图片
                    ImageView ivFirst = new ImageView(getActivity());
                    ivFirst.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(getContext())
                            .load(data.get(data.size() - 1).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivFirst);
                    listImage.add(ivFirst);
                    for (int i = 0; i < data.size(); i++) {
                        ImageView iv = new ImageView(getActivity());
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
                        llpoints.addView(view);
                    }
                    //最后加一张图片
                    ImageView ivEnd = new ImageView(getActivity());
                    ivEnd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.with(getContext())
                            .load(data.get(0).getPic())
                            .placeholder(R.mipmap.banner)
                            .error(R.mipmap.banner)
                            .config(Bitmap.Config.RGB_565)
                            .into(ivEnd);
                    listImage.add(ivEnd);
                    newsAdapter.setData(data);
                    newsAdapter.notifyDataSetChanged();
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

    private void newsData() {
        Bundle params = new Bundle();
        //1 正常，2 热点新闻，3 推荐新闻，4 广告新闻
        params.putString("status", "3");
        params.putString("limit", limitNews + "");
        params.putString("page", pageNews + "");
        params.putString("type", "system_news");
        params.putString("sort", "0");
        mHttp.get(Url.NEWS_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                HotNewsEntity hotNewsEntity = gson.fromJson(result, HotNewsEntity.class);
                totalPageNews = hotNewsEntity.getTotalPage();
                articleList.addAll(hotNewsEntity.getData());
                mArticleAdapter.notifyDataSetChanged();
                if (articleList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

    private HomeStemmaBean.DataBean stemmaData;//我的家谱

    public void myStemmaData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.HOME_STEMMA, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                HomeStemmaBean homeStemmaBean = gson.fromJson(result, HomeStemmaBean.class);

                stemmaData = homeStemmaBean.getData();

                String name = stemmaData.getName();
                String generation = stemmaData.getGeneration();
                String create_time = stemmaData.getCreate_time();
                String region = stemmaData.getRegion();
                String address = stemmaData.getAddress();
                mName.setText(TextUtils.isEmpty(name) ? "--" : name);
                mDate.setText(DateFormatUtils.longToDateM(create_time));
                if (TextUtils.isEmpty(region)) {
                    if (TextUtils.isEmpty(address)) {
                        mBuilder.setText("辈分: " + (TextUtils.isEmpty(generation) ? "--" : generation));
                    } else mBuilder.setText("地址: " + address);
                } else
                    mBuilder.setText("地址: " + region);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void friendStateData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("limit", limitFriend + "");
        params.putString("page", pageFriend + "");
        params.putString("type", "3");//0-查看所有动态，1-获取宗亲会动态；2-获取自己发表的动态，3-好友动态 默认0
        mHttp.get(Url.FRIEND_STATUS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                FriendStateBean friendStateBean = gson.fromJson(result, FriendStateBean.class);
                if (friendStateBean.getCode() == 0) {
                    friendList.addAll(friendStateBean.getData());
                    totalPageFriend = friendStateBean.getTotalPage();
                    mFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                mFriendAdapter.notifyDataSetChanged();
            }
        });
    }

    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            Intent intent = new Intent(mContext, XSJPActivity.class);
            intent.putExtra("keyword", text);
            startActivity(intent);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onChange(String text) {

        }
    };
    View.OnClickListener AddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.showAsDropDown(mAdd, -DisplayUtilX.dip2px(130), 0);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);

           /* SignInDialog signInDialog = new SignInDialog(mContext);
            signInDialog.dialogShow();*/
        }
    };
    View.OnClickListener CameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!XunGenApp.isLogin) {
                IntentSkip intentSkip = new IntentSkip();
                intentSkip.skipLogin(mContext, 208);
                return;
            }
            Intent intent = new Intent(mContext, AddNewsActivity.class);
            intent.putExtra("pid", "0");
            ((Activity) mContext).startActivityForResult(intent, 221);
        }
    };
    View.OnClickListener MoreStemmaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!XunGenApp.isLogin) {
                IntentSkip intentSkip = new IntentSkip();
                intentSkip.skipLogin(mContext, 208);
            } else {
                Intent intent = new Intent(mContext, MyTreeActivity.class);
                startActivity(intent);
            }
        }
    };
    View.OnClickListener StemmaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (stemmaData == null) return;
            String familyId = stemmaData.getFamily_id();
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            intent.putExtra("familyId", familyId);
            intent.putExtra("flag", false);
            ((Activity) mContext).startActivityForResult(intent, 6626);
        }
    };
    View.OnClickListener SaoSaoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ConstantNum.PERMISSION_CAMERA);
            } else {
                //调取二维码扫描
                Intent intent = new Intent(mContext, CaptureActivity.class);
                ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
            }
        }
    };
    View.OnClickListener ShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            ShareUtils share = new ShareUtils(mContext);
            share.share(getString(R.string.app_title), getString(R.string.app_introduce), Url.APP_URL);
        }
    };

    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.friend:
                    if (!XunGenApp.isLogin) {
                        IntentSkip intentSkip = new IntentSkip();
                        intentSkip.skipLogin(mContext, 208);
                    } else {
                        mFriend_block.setVisibility(View.VISIBLE);
                        mFriend_text.setTextColor(getResources().getColor(R.color.green_txt));
                        mNews_block.setVisibility(View.INVISIBLE);
                        mNews_text.setTextColor(getResources().getColor(R.color.deep_txt));
                        mFriend_lv.setVisibility(View.VISIBLE);
                        mMessage_lv.setVisibility(View.GONE);
                        if (friendList.size() > 0) mNoData.setVisibility(View.GONE);
                        else mNoData.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.news:
                    mFriend_block.setVisibility(View.INVISIBLE);
                    mFriend_text.setTextColor(getResources().getColor(R.color.deep_txt));
                    mNews_block.setVisibility(View.VISIBLE);
                    mNews_text.setTextColor(getResources().getColor(R.color.green_txt));
                    mFriend_lv.setVisibility(View.GONE);
                    mMessage_lv.setVisibility(View.VISIBLE);
                    if (articleList.size() > 0) mNoData.setVisibility(View.GONE);
                    else mNoData.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        try {
            switch (requestCode) {
                case ConstantNum.PERMISSION_CAMERA:
                    //调取扫描二维码界面
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
                    break;
            }
        } catch (Exception e) {

        }
    }
}
