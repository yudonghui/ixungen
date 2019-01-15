package com.ruihuo.ixungen.ui.familytree.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.ui.familytree.activity.CreateTreeActivity;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.ui.familytree.adapter.TreeFormAdapter;
import com.ruihuo.ixungen.ui.familytree.contract.TreeFormContract;
import com.ruihuo.ixungen.ui.familytree.presenter.TreeFormPresenter;
import com.ruihuo.ixungen.ui.main.activity.XSJPActivity;
import com.ruihuo.ixungen.ui.main.bean.BaseClanFormBean;
import com.ruihuo.ixungen.ui.main.bean.ClanFormBean;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeFormFragment extends Fragment implements TreeFormContract.View {
    private TreeFormPresenter mPresenter = new TreeFormPresenter(this);
    private View mInflate;
    private Context mContext;
    List<BaseClanFormBean> dataList = new ArrayList<>();
    private TreeFormAdapter mAdapter;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private LinearLayout mNoData;
    private TextView mNoDataTitle;
    private TextView mNoDataButton;

    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;
    private int type;//type=1,创建家谱，2加入的家谱，3购买的家谱
    private Bundle parameters;

    public void setType(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_tree_form, null);
        mContext = getContext();
        initView();

        mAdapter = new TreeFormAdapter(dataList, mContext);
        mListView.setAdapter(mAdapter);
        parameters = new Bundle();
        addListener();
        addData();
        return mInflate;
    }

    private void addListener() {
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        mListView.setOnItemClickListener(ItemClickListener);
        mNoDataButton.setOnClickListener(NoDataListener);
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) mInflate.findViewById(R.id.refresh);
        mListView = (ListView) mInflate.findViewById(R.id.listView);
        mNoData = mInflate.findViewById(R.id.no_data);
        mNoDataTitle = mInflate.findViewById(R.id.no_data_title);
        mNoDataButton = mInflate.findViewById(R.id.no_data_button);
        switch (type) {
            case 1:
                mNoDataTitle.setText("欢迎创建属于自己的家谱");
                mNoDataButton.setText("创建家谱");
                break;
            case 2:
                mNoDataTitle.setText("欢迎加入已经存在的家谱");
                mNoDataButton.setText("前往加入");
                break;
            case 3:
                mNoDataTitle.setText("找寻与自己相关的族谱");
                mNoDataButton.setText("前往姓氏族谱");
                break;
        }
    }

    private void addData() {
        parameters.clear();
        String url;
        if (type == 3) {
            url = Url.BUY_STEMMA_FORM;
            parameters.putString("flagSelf", "true");
        } else {
            url = Url.GLEAN_FORM;
            parameters.putString("type", type + "");
            parameters.putString("flagSelf", "false");
        }
        parameters.putString("limit", limit + "");
        parameters.putString("page", page + "");
        parameters.putString("token", XunGenApp.token);
        parameters.putString("url", url);
        mPresenter.getData(parameters, mContext);
    }

    AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseClanFormBean baseClanFormBean = dataList.get(position);
            String stemma_id = baseClanFormBean.getStemma_id();
            String id1 = baseClanFormBean.getId();
            String familyId = baseClanFormBean.getFamily_id();
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            // intent.putExtra("id", id1);
            intent.putExtra("flag", false);
            intent.putExtra("stemmaId", stemma_id);
            intent.putExtra("familyId", familyId);
            ((Activity) mContext).startActivityForResult(intent, 6626);
        }
    };
    View.OnClickListener NoDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (type) {
                case 1:
                    Intent intent = new Intent(mContext, CreateTreeActivity.class);
                    ((Activity) mContext).startActivityForResult(intent, 1111);
                    break;
                case 2:
                    Intent intent2 = new Intent(mContext, XSJPActivity.class);
                    intent2.putExtra("keyword", XunGenApp.surname);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(mContext, XSJPActivity.class);
                    intent3.putExtra("keyword", XunGenApp.surname);
                    startActivity(intent3);
                    break;
            }
        }
    };
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            dataList.clear();
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
            }
            {
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

    @Override
    public void getDataSuccess(String result) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        Gson gson = GsonUtils.getGson();
        ClanFormBean clanFormBean = gson.fromJson(result, ClanFormBean.class);
        totalPage = clanFormBean.getTotalPage();
        dataList.addAll(clanFormBean.getData());
        if (dataList.size() == 0) {
            mNoData.setVisibility(View.VISIBLE);
        } else {
            mNoData.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(String error) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        if (dataList.size() == 0) {
            mNoData.setVisibility(View.VISIBLE);
        } else {
            mNoData.setVisibility(View.GONE);
        }
    }

    public void refreshData() {
        dataList.clear();
        page = 1;
        addData();
    }
}
