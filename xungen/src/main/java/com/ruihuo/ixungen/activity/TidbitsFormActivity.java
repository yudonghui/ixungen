package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.adapter.TidbitAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.TidbitBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class TidbitsFormActivity extends AppCompatActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private SearchViewY mSearchView;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TidbitAdapter mAdapter;
    private int limit = 20;
    private int page = 1;
    private int totalPage;
    private List<TidbitBean.DataBean> articleList = new ArrayList<>();
    private List<TidbitBean.DataBean> saveList = new ArrayList<>();
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tidbits_form);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mContext = this;
        initView();
        mAdapter = new TidbitAdapter(articleList, mContext);
        mListView.setAdapter(mAdapter);
        addListener();
        addData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.tidTitlbar);
        mTitleBar.setTitle("精彩花絮");
        // mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mSearchView = (SearchViewY) findViewById(R.id.searchview);
        mListView = (ListView) findViewById(R.id.listView);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);

    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索
        mSearchView.setSearchViewListener(SearchListener);
        //上下拉的刷新
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        //
        mListView.setOnItemClickListener(ItemClickListener);
    }

    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            articleList.clear();
            if (TextUtils.isEmpty(title)) {
                saveList.clear();
            }
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
    AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TidbitBean.DataBean dataBean = articleList.get(position - 1);
            String videoId = dataBean.getId();
            Intent intent = new Intent(mContext, H5Activity.class);
            intent.putExtra("from", ConstantNum.VIDEO_PLAY);
            intent.putExtra("videoId", videoId);
            startActivity(intent);
        }
    };
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

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
            title = "";
            articleList.clear();
            articleList.addAll(saveList);
            //saveList.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChange(String text) {

        }
    };

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("type", "1");//视频类型：0-普通，1-花絮，2-预告，默认0
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        if (!TextUtils.isEmpty(title))
            params.putString("title", title);
        mHttp.get(Url.MOVIE_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                TidbitBean tidbitBean = gson.fromJson(result, TidbitBean.class);
                if (tidbitBean.getCode() == 0) {
                    List<TidbitBean.DataBean> data = tidbitBean.getData();
                    articleList.addAll(data);
                    //当搜索框里面搜索的有内容的话，这个时候请求的数据不保存
                    if (TextUtils.isEmpty(title)) {
                        saveList.addAll(data);
                    }
                    totalPage = tidbitBean.getTotalPage();
                    mAdapter.notifyDataSetChanged();
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
}
