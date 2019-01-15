package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.entity.EnvirBean;
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

public class EnvironmentFormActivity extends AppCompatActivity {
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TitleBar mTitleBar;
    private Context mContext;
    private EnvironAdapter mAdapter;
    private List<EnvirBean.DataBean> mDataList = new ArrayList<>();
    private LoadingDialogUtils loadingDialogUtils;
    private int limit = 20;
    private int page = 1;
    private int totalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_form);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mContext = this;
        initView();
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        addData();
        mAdapter = new EnvironAdapter();
        mListView.setAdapter(mAdapter);
        addListener();
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.listView);
        mTitleBar = (TitleBar) findViewById(R.id.envirTitleBar);
        mTitleBar.setTitle("景区");
    }

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ENVIRON_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                EnvirBean envirBean = gson.fromJson(result, EnvirBean.class);
                totalPage = envirBean.getTotalPage();
                mDataList.addAll(envirBean.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });
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
                mDataList.clear();
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnvirBean.DataBean dataBean = mDataList.get(position - 1);
                String envirId = dataBean.getId();
                String envirName = dataBean.getName();
                Intent intent = new Intent();
                intent.putExtra("envirId", envirId);
                intent.putExtra("envirName", envirName);
                setResult(322, intent);
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    class EnvironAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHoler;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_city, null);
                mViewHoler = new ViewHolder();
                mViewHoler.mTextView = (TextView) convertView.findViewById(R.id.text_city_name);
                convertView.setTag(mViewHoler);
            } else mViewHoler = (ViewHolder) convertView.getTag();
            EnvirBean.DataBean dataBean = mDataList.get(position);
            String name = dataBean.getName();
            mViewHoler.mTextView.setText(TextUtils.isEmpty(name) ? "--" : name);
            return convertView;
        }
    }

    class ViewHolder {
        TextView mTextView;
    }
}
