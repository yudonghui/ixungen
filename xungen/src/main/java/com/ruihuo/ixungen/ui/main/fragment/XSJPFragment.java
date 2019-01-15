package com.ruihuo.ixungen.ui.main.fragment;

import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.ui.familytree.activity.CreateTreeActivity;
import com.ruihuo.ixungen.ui.main.bean.SurnameClanBean;
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
public class XSJPFragment extends Fragment implements XSJPFragmentContract.View {
    private Context mContext;
    private View mInflate;
    private ImageView mNoData;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TextView mContent;
    private TextView mCreate;
    XSJPFragmentPresenter mPresenter = new XSJPFragmentPresenter(this);
    private int type = 2;//搜索类型：1-名人；2-家谱；3-宗亲会；4-相关资讯，默认值为1
    private int limit = 15;
    private int page = 1;
    private String keyword = XunGenApp.surname;
    private List<SurnameClanBean.DataBean> dataList = new ArrayList<>();
    private XSJPAdapter mAdapter;
    private Bundle parameters;
    private int totalPage = 1;
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
        mAdapter = new XSJPAdapter();
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
    }

    private void addListner() {
        //创建家谱
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateTreeActivity.class);
                ((Activity) mContext).startActivityForResult(intent, 1111);
            }
        });
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

    @Override
    public void getSearchSuccess(String result) {
        loadingDialogUtils.setDimiss();
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        Gson gson = GsonUtils.getGson();
        SurnameClanBean surnameClanBean = gson.fromJson(result, SurnameClanBean.class);
        totalPage = surnameClanBean.getTotalPage();
        dataList.addAll(surnameClanBean.getData());
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

    class XSJPAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_clan_form, null);
                mViewHolder = new ViewHolder();
                mViewHolder.mCover = (ImageView) convertView.findViewById(R.id.cover);
                mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
                mViewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
                mViewHolder.mAddress = (TextView) convertView.findViewById(R.id.address);
                convertView.setTag(mViewHolder);
            } else mViewHolder = (ViewHolder) convertView.getTag();
            SurnameClanBean.DataBean dataBean = dataList.get(position);
            String region = dataBean.getRegion();
            String name = dataBean.getName();
            String generation = dataBean.getGeneration();
            if (!TextUtils.isEmpty(generation)) {
                generation.replace(" ", "");
            }
            mViewHolder.mTime.setText("字辈: " + (TextUtils.isEmpty(generation) ? "--" : generation));
            mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            mViewHolder.mAddress.setText("来源于: " + (TextUtils.isEmpty(region) ? "--" : region));
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView mCover;
        private TextView mName;
        private TextView mTime;
        private TextView mAddress;
    }
}
