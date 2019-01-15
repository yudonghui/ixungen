package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.ui.familytree.bean.SpouesBean;
import com.ruihuo.ixungen.ui.familytree.contract.SpouseFromContract;
import com.ruihuo.ixungen.ui.familytree.presenter.SpouseFromPresenter;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class SpouseFromActivity extends BaseNoTitleActivity implements SpouseFromContract.View {
    private SpouseFromPresenter mPresenter = new SpouseFromPresenter(this);
    private Context mContext;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TitleBar mTitleBar;
    private String id;
    private String name;
    private boolean flag;
    private SpouseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spouse_from);
        mContext = this;
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        flag = getIntent().getBooleanExtra("flag", false);
        initView();
        mAdapter = new SpouseAdapter();
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private Bundle parameters;

    private void addData() {
        parameters.clear();
        parameters.putString("id", id);
        mPresenter.getData(parameters, mContext);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle(TextUtils.isEmpty(name) ? "配偶" : (name + "的配偶"));
        mListView = (ListView) findViewById(R.id.listView);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);

        parameters = new Bundle();
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                addData();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpouesBean.DataBean dataBean = dataList.get(position - 1);
                String id1 = dataBean.getId();
                Intent intent = new Intent(mContext, TreeDetailActivity.class);
                intent.putExtra("id", id1);
                intent.putExtra("flag", flag);
                startActivity(intent);
            }
        });
    }

    private List<SpouesBean.DataBean> dataList = new ArrayList<>();

    @Override
    public void getDataSuccess(String result) {
        mRefresh.finishRefresh();
        dataList.clear();
        Gson gson = GsonUtils.getGson();
        SpouesBean spouesBean = gson.fromJson(result, SpouesBean.class);
        dataList.addAll(spouesBean.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(String error) {
        mRefresh.finishRefresh();
    }

    class SpouseAdapter extends BaseAdapter {

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
                convertView = View.inflate(mContext, R.layout.item_spouse_form, null);
                mViewHolder = new ViewHolder();
                mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(mViewHolder);
            } else mViewHolder = (ViewHolder) convertView.getTag();
            SpouesBean.DataBean dataBean = dataList.get(position);
            String name = dataBean.getName();
            mViewHolder.mTextView.setText(TextUtils.isEmpty(name) ? "--" : name);
            return convertView;
        }
    }

    class ViewHolder {
        private TextView mTextView;
    }
}
