package com.ruihuo.ixungen.activity.useractivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.AddNewsActivity;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.adapter.FriendStateAdapter;
import com.ruihuo.ixungen.entity.FriendStateBean;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class AgnationStatusActivity extends BaseNoTitleActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private ImageView mNoData;
    private FriendStateAdapter mAdapter;
    private List<FriendStateBean.DataBean> dataList = new ArrayList<>();
    private int page = 1;
    private int totalPage = 1;
    private int limit = 10;
    private LoadingDialogUtils loadingDialogUtils;
    private String associationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnation_status);
        mContext = this;
        associationId = getIntent().getStringExtra("associationId");
        initView();
        mAdapter = new FriendStateAdapter(dataList, mContext, 1);
        mAdapter.setDelete(DeleteListener);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("宗亲动态");
        mTitleBar.mShare.setImageResource(R.mipmap.icon_edit);
        mTitleBar.mShare.setVisibility(View.VISIBLE);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.listView);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        mNoData = (ImageView) findViewById(R.id.no_data);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddNewsActivity.class);
                intent.putExtra("pid", "0");
                ((Activity) mContext).startActivityForResult(intent, 221);
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dataList.clear();
                page = 1;
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
    }

    CallBackInterface DeleteListener = new CallBackInterface() {
        @Override
        public void callBack() {
            dataList.clear();
            page = 1;
            addData();
        }
    };

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("type", "1");//0-查看所有动态，1-获取宗亲会动态；2-获取自己发表的动态，默认0
        params.putString("associationId", associationId);
        mHttp.get(Url.FRIEND_STATUS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                FriendStateBean friendStateBean = gson.fromJson(result, FriendStateBean.class);
                if (friendStateBean.getCode() == 0) {
                    dataList.addAll(friendStateBean.getData());
                    totalPage = friendStateBean.getTotalPage();
                    mAdapter.notifyDataSetChanged();
                }
                if (dataList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                if (dataList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 221 && resultCode == 8811) {
            dataList.clear();
            page = 1;
            addData();
        }
    }
}
