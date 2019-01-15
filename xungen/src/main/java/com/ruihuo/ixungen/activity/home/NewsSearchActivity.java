package com.ruihuo.ixungen.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.adapter.ArticleAdapter;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class NewsSearchActivity extends AppCompatActivity {
    private Context mContext;
    private com.ruihuo.ixungen.view.TitleBar mTitleBar;
    private com.ruihuo.ixungen.view.SearchViewY mSearchView;
    private com.ydh.refresh_layout.SmartRefreshLayout mRefresh;
    private ListView mListView;
    private int page = 1;
    private int totalPage = 1;
    private String title;
    private List<HotNewsEntity.DataBean> articleList = new ArrayList<>();
    private ArticleAdapter mArticleAdapter;//资讯

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mContext = this;
        title = getIntent().getStringExtra("title");
        initView();
        mArticleAdapter = new ArticleAdapter(articleList, mContext, "article");
        mListView.setAdapter(mArticleAdapter);
        addListener();
        addData();
    }

    private void initView() {
        mTitleBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mSearchView = (com.ruihuo.ixungen.view.SearchViewY) findViewById(R.id.searchView);
        mRefresh = (com.ydh.refresh_layout.SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.searchListView);
        if (!StringUtil.isNullOrEmpty(title)) {
            mSearchView.setText(title);
        }

    }

    private void addListener() {
        //上下拉的刷新
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        mSearchView.setSearchViewListener(SearchListener);
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("page", page + "");
        if (!TextUtils.isEmpty(title))
            params.putString("title", title);
        mHttp.get(Url.NEWS_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                HotNewsEntity hotNewsEntity = gson.fromJson(result, HotNewsEntity.class);
                if (hotNewsEntity.getCode() == 0) {
                    totalPage = hotNewsEntity.getTotalPage();
                    List<HotNewsEntity.DataBean> data = hotNewsEntity.getData();
                    articleList.addAll(data);
                }
                mArticleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });
    }

    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            //数据请求中不让viewpager自动轮播。中间上下滚动的寻根热点停止
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
    };
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {
            title = "";
            page = 1;
            totalPage = 1;
            articleList.clear();
            addData();
        }

        @Override
        public void onSearch(String text) {
            title = text;
            page = 1;
            totalPage = 1;
            articleList.clear();
            addData();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onChange(String text) {

        }
    };

}
