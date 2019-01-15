package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.AgnationFormAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
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

import io.rong.imkit.RongIM;

/**
 * @ author yudonghui
 * @ date 2017/4/6
 * @ describe May the Buddha bless bug-free！！
 */
public class AgnationFormActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private ImageView mNoData;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private Context mContext;
    private int page = 1;
    private int limit = 20;
    private int totalPage = 1;
    List<AgnationFormBean.DataBean> data = new ArrayList<>();
    List<AgnationFormBean.DataBean> saveList = new ArrayList<>();
    private AgnationFormAdapter mAdapter;
    private String from;
    private SearchViewY mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnation_form);
        mContext = this;
        from = getIntent().getStringExtra("from");
        initView();
        mAdapter = new AgnationFormAdapter(data);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.agnation_titlebar);
        mTitleBar.setTitle("查找宗亲");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.ptlv);
        mNoData = (ImageView) findViewById(R.id.no_data);
        mSearchView = (SearchViewY) findViewById(R.id.searchview);
        mSearchView.setHint("请输入宗亲会名称");
    }

    private String name;

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        String url;
        if ("self".equals(from) || "jgjx".equals(from)
                || "zzhz".equals(from) || "zzhd".equals(from)
                || "groups".equals(from)) {
            mSearchView.setVisibility(View.GONE);
            //展示自己的宗亲列表
            params.putString("token", XunGenApp.token);
            url = Url.AGNATION_FORM_BYID_URL;
        } else {
            mSearchView.setVisibility(View.VISIBLE);
            params.putString("page", page + "");
            params.putString("limit", limit + "");
            params.putString("surnameId", XunGenApp.surnameId);
            //当根据宗亲会名称搜索的时候
            if (!TextUtils.isEmpty(name)) {
                params.putString("name", name);
            }
            // params.putString("surnameId", XunGenApp.surname);
            url = Url.AGNATION_FORM_URL;
        }

        // params.putString("surnameId", XunGenApp.surname);
        mHttp.get(url, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = new Gson();
                AgnationFormBean agnationFormBean = gson.fromJson(result, AgnationFormBean.class);
                totalPage = agnationFormBean.getTotalPage();
                data.addAll(agnationFormBean.getData());
                /**
                 *在带搜索框的前提下，判断搜索框里面有没有内容。
                 * 如果有证明是在搜索，这个时候请求的数据没有必要去保留。
                 * 否则需要把数据保存下来。当点击搜索的取消按钮时候把数据恢复
                 */
                if (!"self".equals(from)
                        && !"jgjx".equals(from)
                        && !"zzhz".equals(from)
                        && TextUtils.isEmpty(name)) {
                    saveList.addAll(agnationFormBean.getData());
                }
                if (data.size() > 0) {
                    mNoData.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                } else {
                    mNoData.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                mNoData.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("jgjx".equals(from)) {

                    IntentSkip intentSkip = new IntentSkip();
                    Bundle bundle = new Bundle();
                    bundle.putString("associationId", data.get(position).getId());
                    intentSkip.skipType(mContext, ConstantNum.JGJX, bundle);
                } else if ("zzhz".equals(from)) {
                    IntentSkip intentSkip = new IntentSkip();
                    Bundle bundle = new Bundle();
                    bundle.putString("associationId", data.get(position).getId());
                    intentSkip.skipType(mContext, ConstantNum.ZQHZ, bundle);
                } else if ("zzhd".equals(from)) {
                    IntentSkip intentSkip = new IntentSkip();
                    Bundle bundle = new Bundle();
                    bundle.putString("associationId", data.get(position).getId());
                    intentSkip.skipType(mContext, ConstantNum.ZQHD, bundle);
                } else if ("groups".equals(from)) {
                   /* Intent intent = new Intent(mContext, AgnationMemeberActivity.class);
                    intent.putExtra("associationId", data.get(position).getId());
                    intent.putExtra("from","AgationActivity");
                    startActivity(intent);*/
                    RongIM.getInstance().startGroupChat(mContext, data.get(position).getId(), data.get(position).getName());
                } else {
                    //Intent intent = new Intent(mContext, AgnationActivity.class);
                    Intent intent = new Intent(mContext, AgnationNewActivity.class);
                    intent.putExtra("id", data.get(position).getId());
                    startActivity(intent);
                }
                finish();
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                data.clear();
                if (TextUtils.isEmpty(name)) {
                    saveList.clear();
                }
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
                } else{
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
        mSearchView.setSearchViewListener(new SearchViewY.SearchViewListener() {
            @Override
            public void onDelete() {

            }

            @Override
            public void onSearch(String text) {
                name = text;
                page = 1;
                totalPage = 1;
                data.clear();
                addData();
            }

            @Override
            public void onCancel() {
                name = "";
                data.clear();
                data.addAll(saveList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChange(String text) {

            }
        });
       /* mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                data.clear();
                page = 1;
                name = query;
                addData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 当搜索内容改变时触发该方法
                return false;
            }
        });*/
    }

}
