package com.ruihuo.ixungen.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.useractivity.AgnationNewActivity;
import com.ruihuo.ixungen.adapter.AgnationFormAdapter;
import com.ruihuo.ixungen.common.IsJoinAgantion;
import com.ruihuo.ixungen.entity.AgnationFormBean;
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
public class ZQHFragment extends Fragment implements XSJPFragmentContract.View {
    private Context mContext;
    private View mInflate;
    private ImageView mNoData;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TextView mContent;
    private TextView mCreate;
    XSJPFragmentPresenter mPresenter = new XSJPFragmentPresenter(this);
    private List<AgnationFormBean.DataBean> dataList = new ArrayList<>();//列表展示的数据
    private AgnationFormAdapter mAdapter;
    private Bundle parameters;
    private int totalPage = 1;
    private int type = 3;//搜索类型：1-名人；2-家谱；3-宗亲会；4-相关资讯，默认值为1
    private int limit = 15;
    private int page = 1;
    private String keyword = XunGenApp.surname;
    private LoadingDialogUtils loadingDialogUtils;

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
        mInflate = inflater.inflate(R.layout.fragment_xsjp, null);
        mContext = getContext();
        initView();
        mAdapter = new AgnationFormAdapter(dataList);
        mListView.setAdapter(mAdapter);
        addListner();
        parameters = new Bundle();
        web();
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

    private void initView() {
        mNoData = (ImageView) mInflate.findViewById(R.id.no_data);
        mRefresh = (SmartRefreshLayout) mInflate.findViewById(R.id.refresh);
        mListView = (ListView) mInflate.findViewById(R.id.listView);
        mContent = (TextView) mInflate.findViewById(R.id.textView);
        mCreate = (TextView) mInflate.findViewById(R.id.creat);
        if (type == 2) mCreate.setText("创建姓氏家谱");
        else if (type == 1) mCreate.setText("创建姓氏名人");
        else if (type == 3) mCreate.setText("创建宗亲会");

        if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {//没有宗亲会
            mContent.setVisibility(View.VISIBLE);
            mCreate.setVisibility(View.VISIBLE);
        } else {
            mContent.setVisibility(View.GONE);
            mCreate.setVisibility(View.GONE);
        }
    }

    private void addListner() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Intent intent = new Intent(mContext, AgnationActivity.class);
                Intent intent = new Intent(mContext, AgnationNewActivity.class);
                intent.putExtra("id", dataList.get(position - 1).getId());
                startActivity(intent);
            }
        });
        mCreate.setOnClickListener(CreateListener);
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

    View.OnClickListener CreateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IsJoinAgantion isJoinAgantion = new IsJoinAgantion(mContext);
            isJoinAgantion.isJoin();
        }
    };

    @Override
    public void getSearchSuccess(String result) {
        loadingDialogUtils.setDimiss();
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        Gson gson = GsonUtils.getGson();
        AgnationFormBean agnationFormBean = gson.fromJson(result, AgnationFormBean.class);
        totalPage = agnationFormBean.getTotalPage();
        dataList.addAll(agnationFormBean.getData());
        mAdapter.notifyDataSetChanged();
        if (dataList.size() <= 0) mNoData.setVisibility(View.VISIBLE);
        else mNoData.setVisibility(View.GONE);
    }

    @Override
    public void getSearchError(String error) {
        loadingDialogUtils.setDimiss();
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        if (dataList.size() <= 0) mNoData.setVisibility(View.VISIBLE);
        else mNoData.setVisibility(View.GONE);
    }

}
