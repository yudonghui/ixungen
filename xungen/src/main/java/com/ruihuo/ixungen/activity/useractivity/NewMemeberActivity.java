package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.adapter.NewMemeberAdapter;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.geninterface.OnClickInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class NewMemeberActivity extends BaseNoTitleActivity {
    private TitleBar mTitlebar;
    private SearchViewY mSearchview;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private Context mContext;
    private String from;
    private String presidentId;
    private String associationId;//宗亲会id
    private List<FriendBean.DataBean> memeberList = new ArrayList<>();//列表展示的数据
    private List<FriendBean.DataBean> saveList = new ArrayList<>();
    private AgnationFormBean.DataBean dataBean;
    private NewMemeberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memeber);
        mContext = this;
        Intent intent = getIntent();
        associationId = intent.getStringExtra("associationId");
        from = intent.getStringExtra("from");
        presidentId = intent.getStringExtra("presidentId");
        dataBean = (AgnationFormBean.DataBean) intent.getSerializableExtra("dataBean");
        initView();
        mAdapter = new NewMemeberAdapter(memeberList, OnClickListener);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitlebar = (TitleBar) findViewById(R.id.titlbar);
        mTitlebar.setTitle("新的成员申请");
        mSearchview = (SearchViewY) findViewById(R.id.searchview);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = findViewById(R.id.listView);
        mSearchview.setHint("请输入名称/根号");
    }

    private void addListener() {
        mRefresh.setOnRefreshListener(RefreshListener);
        mSearchview.setSearchViewListener(SearchListener);
    }

    private int limit = 10000;

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("associationId", associationId);
        params.putString("limit", limit + "");
        mHttp.get(Url.AGNATION_MEMEBER_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = new Gson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                memeberList.addAll(friendBean.getData());
                saveList.addAll(friendBean.getData());
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

    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            memeberList.clear();
            saveList.clear();
            addData();
        }
    };

    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            if (!TextUtils.isEmpty(text)) {
                memeberList.clear();
                for (int i = 0; i < saveList.size(); i++) {
                    FriendBean.DataBean dataBean = saveList.get(i);
                    String nikename = dataBean.getNikename();
                    String rid = dataBean.getRid();
                    if ((!TextUtils.isEmpty(nikename) && nikename.contains(text)) || (!TextUtils.isEmpty(rid) && rid.contains(text))) {
                        memeberList.add(dataBean);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancel() {
            memeberList.clear();
            memeberList.addAll(saveList);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChange(String text) {

        }
    };
    private List<FriendBean.DataBean> mList = new ArrayList<>();
    OnClickInterface OnClickListener = new OnClickInterface() {
        @Override
        public void onClick(TextView view, final int position) {
            FriendBean.DataBean dataBean = memeberList.get(position);
            String userRid = dataBean.getRid();
            String text = view.getText().toString();
            final String status;
            if ("拒绝".equals(text)) {
                status = "2";
            } else {
                status = "1";
            }
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("status", status);
            params.putString("associationId", associationId);
            params.putString("userRid", userRid);
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            mHttp.post(Url.AGNATION_UPDATE, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    mList.clear();
                    mList.addAll(memeberList);
                    memeberList.clear();
                    for (int i = 0; i < mList.size(); i++) {
                        FriendBean.DataBean dataBean = mList.get(i);
                        if (i == position) {
                            dataBean.setStatus(status);
                        }
                        memeberList.add(dataBean);
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
}
