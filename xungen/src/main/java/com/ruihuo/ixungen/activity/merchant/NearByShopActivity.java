package com.ruihuo.ixungen.activity.merchant;

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

public class NearByShopActivity extends AppCompatActivity {
    private Context mContext;
    private String region;
    private HttpInterface mHttp;
    private NearbyShopAdatper mAdapter;
    private TitleBar mTitlBar;
    private SearchViewY mSearch;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private LoadingDialogUtils loadingDialogUtils;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_shop);
        mContext = this;
        region = getIntent().getStringExtra("region");
        type = getIntent().getIntExtra("type", 0);
        initView();
        mAdapter = new NearbyShopAdatper(nearbyShopList, mContext, 2);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (TitleBar) findViewById(R.id.titlBar);
        if (type == 1) {
            mTitlBar.setTitle("麻城美食");
        } else if (type == 2) {
            mTitlBar.setTitle("麻城酒店");
        } else if (type == 4) {
            mTitlBar.setTitle("麻城旅游");
        }
        mListView = (ListView) findViewById(R.id.listView);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mSearch = (SearchViewY) findViewById(R.id.search);
        mHttp = HttpUtilsManager.getInstance(mContext);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearch.setSearchViewListener(SearchListener);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                nearbyShopList.clear();
                if (TextUtils.isEmpty(title)) {
                    saveList.clear();
                }
                addData();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
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

        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyShopBaseBean nearbyShopBaseBean = nearbyShopList.get(position - 1);
                String shopId = nearbyShopBaseBean.getId();
                int type = Integer.parseInt(nearbyShopBaseBean.getType());
                if (TextUtils.isEmpty(shopId)) return;
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    private List<NearbyShopBaseBean> nearbyShopList = new ArrayList<>();
    private List<NearbyShopBaseBean> saveList = new ArrayList<>();
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;

    private void addData() {
        Bundle params = new Bundle();
        if (!TextUtils.isEmpty(title)) {
            params.putString("key", title);
        } else {
            if (!TextUtils.isEmpty(region))
                params.putString("key", region);
        }
        params.putString("type", type + "");
        params.putString("page", page + "");
        params.putString("limit", limit + "");
        mHttp.get(Url.SHOP_NEARBY_SHOP, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                NearbyShopBean nearbyShopBean = gson.fromJson(result, NearbyShopBean.class);
                totalPage = nearbyShopBean.getTotalPage();
                nearbyShopList.addAll(nearbyShopBean.getData());
                if (TextUtils.isEmpty(title)) {
                    saveList.addAll(nearbyShopBean.getData());
                }
                mAdapter.notifyDataSetChanged();
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

        }

        @Override
        public void onSearch(String text) {
            title = text;
            page = 1;
            totalPage = 1;
            nearbyShopList.clear();
            addData();
        }

        @Override
        public void onCancel() {
            title = "";
            nearbyShopList.clear();
            nearbyShopList.addAll(saveList);
            //saveList.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChange(String text) {

        }
    };
}
