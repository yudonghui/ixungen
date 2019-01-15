package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.action.ActionUserInfoActivity;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.adapter.ArticleAdapter;
import com.ruihuo.ixungen.adapter.MovieGvAdapter;
import com.ruihuo.ixungen.adapter.MovieViewPagerAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.entity.MovieBean;
import com.ruihuo.ixungen.entity.VideoBaseBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.MyListView;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/7/19
 * @ describe May the Buddha bless bug-free！！
 */
public class MovieBaseActivity extends AppCompatActivity {
    private Context mContext;
    private SmartRefreshLayout mRefresh;
    private ScrollView mScrollView;
    private TitleBar mTitleBar;
    private ViewPager mViewpager;
    private LinearLayout mLl_points;
    private LinearLayout mActors;
    private TextView mName1;
    private TextView mRemark1;
    private ImageView mIcon1;
    private TextView mName2;
    private TextView mRemark2;
    private ImageView mIcon2;
    private LinearLayout mActorApply;
    private LinearLayout mNewsMovie;
    // private com.ruihuo.ixungen.view.MovieCardView mPeri;
    //private com.ruihuo.ixungen.view.MovieCardView mChild;
    private MyGridView mGridViewVideo;
    private LinearLayout mWonderful;
    private MyGridView mGridViewTidbit;
    //private com.ruihuo.ixungen.view.MovieCardView mTidbits1;
    // private com.ruihuo.ixungen.view.MovieCardView mTidbits2;
    // private com.ruihuo.ixungen.view.MovieCardView mTidbits3;
    // private com.ruihuo.ixungen.view.MovieCardView mTidbits4;
    private MyListView mListview;
    private List<MovieBean.DataBean.AdsBean> adsList = new ArrayList<>();//轮播图数据
    private List<VideoBaseBean> videoList = new ArrayList<>();//热门影视
    private List<VideoBaseBean> tidbitsList = new ArrayList<>();//精彩花絮
    private List<MovieBean.DataBean.PortalsBean> portalsList = new ArrayList<>();//报名和演员表入口
    private List<ImageView> listImage = new ArrayList<>();//viewpaer的图片
    private MovieViewPagerAdapter mVpAdapter;
    private int msgWhat = 0;
    private LoadingDialogUtils loadingDialogUtils;
    private ArticleAdapter mArticleAdapter;
    private HttpInterface mHttp;
    private String videoId;
    private MovieGvAdapter mGvAdapterVideo;
    private MovieGvAdapter mGvAdapterTidbit;
    private LinearLayout.LayoutParams layoutParams;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_base);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mContext = this;
        initView();
        mHttp = HttpUtilsManager.getInstance(mContext);
        mVpAdapter = new MovieViewPagerAdapter(listImage, adsList, mContext);
        mViewpager.setAdapter(mVpAdapter);
        mArticleAdapter = new ArticleAdapter(articleList, mContext, "news");
        mListview.setAdapter(mArticleAdapter);
        mGvAdapterVideo = new MovieGvAdapter(videoList);
        mGridViewVideo.setAdapter(mGvAdapterVideo);
        mGvAdapterTidbit = new MovieGvAdapter(tidbitsList);
        mGridViewTidbit.setAdapter(mGvAdapterTidbit);
        addData();
        addListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.movieTitleBar);
        mTitleBar.setTitle("炎黄影视");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mLl_points = (LinearLayout) findViewById(R.id.ll_points);
        mActors = (LinearLayout) findViewById(R.id.actors);
        mName1 = (TextView) findViewById(R.id.name1);
        mRemark1 = (TextView) findViewById(R.id.remark1);
        mIcon1 = (ImageView) findViewById(R.id.icon1);
        mActorApply = (LinearLayout) findViewById(R.id.actorApply);
        mName2 = (TextView) findViewById(R.id.name2);
        mRemark2 = (TextView) findViewById(R.id.remark2);
        mIcon2 = (ImageView) findViewById(R.id.icon2);
        mNewsMovie = (LinearLayout) findViewById(R.id.newsMovie);
        // mPeri = (com.ruihuo.ixungen.view.MovieCardView) findViewById(R.id.peri);
        // mChild = (com.ruihuo.ixungen.view.MovieCardView) findViewById(R.id.child);
        mGridViewVideo = (MyGridView) findViewById(R.id.gridViewVideo);
        mWonderful = (LinearLayout) findViewById(R.id.wonderful);
        mGridViewTidbit = (MyGridView) findViewById(R.id.gridViewTidbit);
        mListview = (MyListView) findViewById(R.id.listview);
        mListview.setFocusable(false);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        layoutParams = new LinearLayout.LayoutParams(DisplayUtilX.dip2px(7), DisplayUtilX.dip2px(7));
        layoutParams.leftMargin = 12;
    }

    private void addData() {
        Bundle params = new Bundle();
        if (XunGenApp.isLogin) params.putString("rid", XunGenApp.rid);
        mHttp.get(Url.MOVIES, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                MovieBean movieBean = gson.fromJson(result, MovieBean.class);
                MovieBean.DataBean data = movieBean.getData();
                adsList.clear();
                videoList.clear();
                tidbitsList.clear();
                portalsList.clear();

                adsList.addAll(data.getAds());
                videoList.addAll(data.getVideo());
                tidbitsList.addAll(data.getTidbits());
                portalsList.addAll(data.getPortals());
                setView();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });

        //热门资讯
        recommendNewsData();
    }

    private int limit = 10;
    private int page = 1;
    private int totalPage = 1;
    private List<HotNewsEntity.DataBean> articleList = new ArrayList<>();//新闻资讯

    private void recommendNewsData() {
        Bundle params = new Bundle();
        //1 正常，2 热点新闻，3 推荐新闻，4 广告新闻 5娱乐新闻（炎黄影视）
        params.putString("status", "1");
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("type", "entertainment_news");
        mHttp.get(Url.NEWS_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                HotNewsEntity hotNewsEntity = gson.fromJson(result, HotNewsEntity.class);
                totalPage = hotNewsEntity.getTotalPage();
                articleList.addAll(hotNewsEntity.getData());
                mArticleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

    private int currentPosition = 1;
    private int prePosition = 0;
    private boolean flag = false;

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWonderful.setOnClickListener(WonderfulListener);
        mGridViewVideo.setOnItemClickListener(VideoListener);
        mGridViewTidbit.setOnItemClickListener(TidbitListener);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    mViewpager.setCurrentItem(currentPosition, false);
                }
            }
        });
        mViewpager.setOnTouchListener(new View.OnTouchListener() {
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
                articleList.clear();
                //刷新的过程当中
                addData();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    page++;
                    recommendNewsData();
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
    }

    AdapterView.OnItemClickListener TidbitListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            VideoBaseBean videoBaseBean = tidbitsList.get(position);
            String videoId = videoBaseBean.getId();
            Intent intent = new Intent(mContext, H5Activity.class);
            intent.putExtra("from", ConstantNum.VIDEO_PLAY);
            intent.putExtra("videoId", videoId);
            startActivity(intent);
        }
    };
    AdapterView.OnItemClickListener VideoListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            VideoBaseBean videoBaseBean = videoList.get(position);
            String videoId = videoBaseBean.getId();
            Intent intent = new Intent(mContext, H5Activity.class);
            intent.putExtra("from", ConstantNum.VIDEO_PLAY);
            intent.putExtra("videoId", videoId);
            startActivity(intent);
        }
    };
    View.OnClickListener WonderfulListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, TidbitsFormActivity.class);
            startActivity(intent);
        }
    };

    private void setView() {
        //左右轮播图
        viewpagerData();
        //演员表 和报名入口
        for (int i = 0; i < portalsList.size(); i++) {
            MovieBean.DataBean.PortalsBean portalsBean = portalsList.get(i);
            String name = portalsBean.getName();
            String summary = portalsBean.getSummary();
            String img_url = portalsBean.getImg_url();
            final String type = portalsBean.getType();
            switch (i) {
                case 0:
                    mName1.setText(TextUtils.isEmpty(name) ? "" : name);
                    mRemark1.setText(TextUtils.isEmpty(summary) ? "" : summary);
                    PicassoUtils.setImgView(img_url, R.mipmap.yanyuanku, mContext, mIcon1);
                    mActors.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectSkip(type);
                        }
                    });
                    break;
                case 1:
                    mName2.setText(TextUtils.isEmpty(name) ? "" : name);
                    mRemark2.setText(TextUtils.isEmpty(summary) ? "" : summary);
                    PicassoUtils.setImgView(img_url, R.mipmap.baoming, mContext, mIcon2);
                    mActorApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectSkip(type);
                        }
                    });
                    break;
            }

        }
        mGvAdapterVideo.notifyDataSetChanged();
        mGvAdapterTidbit.notifyDataSetChanged();

    }

    private boolean refreshing = true;

    private void viewpagerData() {
        listImage.clear();
        mLl_points.removeAllViews();
        //最前面加一张图片
        ImageView ivFirst = new ImageView(mContext);
        ivFirst.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext)
                .load(adsList.get(adsList.size() - 1).getPic())
                .placeholder(R.mipmap.banner)
                .error(R.mipmap.banner)
                .config(Bitmap.Config.RGB_565)
                .into(ivFirst);
        listImage.add(ivFirst);
        for (int i = 0; i < adsList.size(); i++) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(mContext)
                    .load(adsList.get(i).getPic())
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
                .load(adsList.get(0).getPic())
                .placeholder(R.mipmap.banner)
                .error(R.mipmap.banner)
                .config(Bitmap.Config.RGB_565)
                .into(ivEnd);
        listImage.add(ivEnd);
        mVpAdapter = new MovieViewPagerAdapter(listImage, adsList, mContext);
        mViewpager.setAdapter(mVpAdapter);
        //请求数据完成后才允许自动轮播
        refreshing = false;
    }

    private void selectSkip(String type) {
        if ("3".equals(type)) {
            Intent intent = new Intent(mContext, MovieActorsActivity.class);
            startActivity(intent);//if ("4".equals(type))
        } else if ("4".equals(type)) {
            if (XunGenApp.isLogin) {
                //在进入报名入口之前先网络请求判断自己是什么状态。1.没有报名，2，审核中，3通过审核，4审核不通过
                getStatus();

            } else {
                IntentSkip intentSkip = new IntentSkip();
                intentSkip.skipLogin(mContext, 211);
            }
        } else if ("6".equals(type)) {
            if (XunGenApp.isLogin) {
                Intent intent = new Intent(mContext, ActionUserInfoActivity.class);
                intent.putExtra("rid", XunGenApp.rid);
                startActivity(intent);

            } else {
                IntentSkip intentSkip = new IntentSkip();
                intentSkip.skipLogin(mContext, 211);
            }
        }
    }

    private void getStatus() {
        /* Intent intent = new Intent(mContext, ActorApplyActivity.class);
                startActivity(intent);*/
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);

        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.ACTOR_APPLY_STATE, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int data = jsonObject.getInt("data");//1.没有报名，2，审核中，3通过审核，4审核不通过
                    Intent intent = new Intent(mContext, ActorApplyActivity.class);
                    intent.putExtra("state", data);
                    startActivityForResult(intent, 223);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!refreshing) {
                currentPosition++;
                mViewpager.setCurrentItem(currentPosition);
            }
            mHandler.sendEmptyMessageDelayed(msgWhat, 2000);

        }
    };

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

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MovieBase Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 223 && resultCode == 323) {

        }
    }
}
