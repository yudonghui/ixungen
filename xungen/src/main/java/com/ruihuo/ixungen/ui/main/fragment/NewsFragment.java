package com.ruihuo.ixungen.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.ArticleAdapter;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.ui.main.contract.XSJPFragmentContract;
import com.ruihuo.ixungen.ui.main.presenter.XSJPFragmentPresenter;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yudonghui
 * @date 2017/10/18
 * @describe May the Buddha bless bug-free!!!
 */
public class NewsFragment extends Fragment implements XSJPFragmentContract.View {
    private Context mContext;
    private View mInflate;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private ImageView mNoData;
    private Bundle parameters;
    XSJPFragmentPresenter mPresenter = new XSJPFragmentPresenter(this);
    private LoadingDialogUtils loadingDialogUtils;
    private int type = 4;//搜索类型：1-名人；2-家谱；3-宗亲会；4-相关资讯，默认值为1
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;
    private ArticleAdapter mArticleAdapter;
    private String keyword = XunGenApp.surname;

    public void setData(String keyword) {
        this.keyword = keyword;
        page = 1;
        dataList.clear();
        web();
    }

    public void setStartData(String keyword) {
        this.keyword = keyword;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_order_form, null);
        mContext = getContext();
        initView();
        parameters = new Bundle();
        mArticleAdapter = new ArticleAdapter(dataList, mContext, "news");
        mListView.setAdapter(mArticleAdapter);
        parameters = new Bundle();
        web();
        addListener();
        return mInflate;
    }

    private void web() {
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        parameters.clear();
        parameters.putString("type", type + "");
        parameters.putString("limit", limit + "");
        parameters.putString("page", page + "");
        parameters.putString("keyword", keyword);
        mPresenter.getSearchData(parameters, mContext);
    }

    List<HotNewsEntity.DataBean> dataList = new ArrayList<>();

    private void addListener() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dataList.clear();
                page = 1;
                web();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    page++;
                    web();
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

    private void initView() {
        mRefresh = (SmartRefreshLayout) mInflate.findViewById(R.id.refresh);
        mListView = (ListView) mInflate.findViewById(R.id.listView);
        mNoData = (ImageView) mInflate.findViewById(R.id.no_data);
    }

    @Override
    public void getSearchSuccess(String result) {
        loadingDialogUtils.setDimiss();
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        Gson gson = GsonUtils.getGson();
        HotNewsEntity hotNewsEntity = gson.fromJson(result, HotNewsEntity.class);
        dataList.addAll(hotNewsEntity.getData());
        mArticleAdapter.notifyDataSetChanged();
        if (dataList.size() <= 0) mNoData.setVisibility(View.VISIBLE);
        else mNoData.setVisibility(View.GONE);
    }

    @Override
    public void getSearchError(String error) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        loadingDialogUtils.setDimiss();
        if (dataList.size() <= 0) mNoData.setVisibility(View.VISIBLE);
        else mNoData.setVisibility(View.GONE);
    }
}
