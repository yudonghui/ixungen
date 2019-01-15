package com.ruihuo.ixungen.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.action.FragmentAdapter;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.ui.familytree.activity.MyTreeActivity;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.ui.main.adapter.ClanFormAdapter;
import com.ruihuo.ixungen.ui.main.bean.BaseClanFormBean;
import com.ruihuo.ixungen.ui.main.bean.ClanFormBean;
import com.ruihuo.ixungen.ui.main.bean.ClanFormBean2;
import com.ruihuo.ixungen.ui.main.contract.XSJPContract;
import com.ruihuo.ixungen.ui.main.fragment.NewsFragment;
import com.ruihuo.ixungen.ui.main.fragment.XSJPFragment;
import com.ruihuo.ixungen.ui.main.fragment.XSMRFragment;
import com.ruihuo.ixungen.ui.main.fragment.ZQHFragment;
import com.ruihuo.ixungen.ui.main.presenter.XSJPPresenter;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class XSJPActivity extends BaseNoTitleActivity implements XSJPContract.View {
    XSJPPresenter presenter = new XSJPPresenter(this);
    private TitleBar mTitleBar;
    private SearchViewY mSearchView;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private LinearLayout mLinearlayout;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private Bundle parameters;
    private String keyword;
    private XSMRFragment mXSMRFragment;
    private XSJPFragment mXSJPFragment;
    private ZQHFragment mZQHFragment;
    private NewsFragment mNewsFragment;
    private FragmentAdapter mVpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsjp);
        initView();
        keyword = getIntent().getStringExtra("keyword");
        if (TextUtils.isEmpty(keyword))
            keyword = XunGenApp.surname;
        mSearchView.setText(keyword);
        addFragment();
        mAdapter = new ClanFormAdapter(dataList, mContext);
        mListView.setAdapter(mAdapter);
        mVpAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        //填充适配器
        mViewPager.setAdapter(mVpAdapter);
        //TabLayout加载viewpager
        mTablayout.setupWithViewPager(mViewPager);
        addListener();
        parameters = new Bundle();
        web();
    }

    private void web() {
        parameters.clear();
        parameters.putString("limit", limit + "");
        parameters.putString("page", page + "");
        parameters.putString("keyword", keyword);
        presenter.getSearchData(parameters, mContext);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.mShare.setOnClickListener(FamilyListener);//右上角点击事件
        //上下拉的刷新
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        //
        mListView.setOnItemClickListener(ItemClickListener);
        //搜索
        mSearchView.setSearchViewListener(SearchListener);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("搜索结果");
        mTitleBar.mShare.setVisibility(View.VISIBLE);
        mTitleBar.mShare.setImageResource(R.mipmap.family);
        mSearchView = (SearchViewY) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mLinearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    private void addFragment() {
        mTitleList.add("姓氏名人");
        mTitleList.add("姓氏家谱");
        mTitleList.add("宗亲会");
        mTitleList.add("相关资讯");
        //为TabLayout添加tab名称
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(3)));
        mViewPager.setOffscreenPageLimit(4);
        mXSMRFragment = new XSMRFragment();
        mXSMRFragment.setStartData(keyword);

        mXSJPFragment = new XSJPFragment();
        mXSJPFragment.setStartData(keyword);

        mZQHFragment = new ZQHFragment();
        mZQHFragment.setStartData(keyword);

        mNewsFragment = new NewsFragment();
        mNewsFragment.setStartData(keyword);

        mFragmentList.add(mXSMRFragment);
        mFragmentList.add(mXSJPFragment);
        mFragmentList.add(mZQHFragment);
        mFragmentList.add(mNewsFragment);
    }

    List<BaseClanFormBean> dataList = new ArrayList<>();
    List<BaseClanFormBean> saveList = new ArrayList<>();
    private ClanFormAdapter mAdapter;

    @Override
    public void getSearchSuccess(String result) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        try {
            JSONObject jsonObject = new JSONObject(result);
            int searchType = jsonObject.getInt("searchType");
            if (searchType == 1) {//1是自己在app建的家谱。2是系统录入的家谱
                Gson gson = GsonUtils.getGson();
                ClanFormBean clanFormBean = gson.fromJson(result, ClanFormBean.class);
                totalPage = clanFormBean.getTotalPage();
                dataList.addAll(clanFormBean.getData());

            } else {
                Gson gson = GsonUtils.getGson();
                ClanFormBean2 clanFormBean = gson.fromJson(result, ClanFormBean2.class);
                totalPage = clanFormBean.getTotalPage();
                ClanFormBean2.DataBean data = clanFormBean.getData();
                List<ClanFormBean2.DataBean.FromAppBean> from_app = data.getFrom_app();
                List<ClanFormBean2.DataBean.FromSystemBean> from_system = data.getFrom_system();
                if (from_system != null)
                    dataList.addAll(from_system);
                if (from_app != null)
                    dataList.addAll(from_app);
            }
            if (dataList.size() > 0) {
                mLinearlayout.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            } else {
                mLinearlayout.setVisibility(View.VISIBLE);
                if (!XunGenApp.surname.equals(keyword)) {
                    mXSMRFragment.setData(keyword);
                    mXSJPFragment.setData(keyword);
                    mZQHFragment.setData(keyword);
                    mNewsFragment.setData(keyword);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSearchError(String error) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
    }

    View.OnClickListener FamilyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MyTreeActivity.class);
            startActivity(intent);
        }
    };
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            keyword = text;
            page = 1;
            totalPage = 1;
            dataList.clear();
            web();
        }

        @Override
        public void onCancel() {
            keyword = "";
            dataList.clear();
            web();
        }

        @Override
        public void onChange(String text) {

        }
    };

    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            dataList.clear();
            page = 1;
            web();
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
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
    };

    AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseClanFormBean baseClanFormBean = dataList.get(position);
            String stemma_id = baseClanFormBean.getStemma_id();
            String id1 = baseClanFormBean.getId();
            String familyId = baseClanFormBean.getFamily_id();
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            intent.putExtra("id", id1);
            intent.putExtra("flag", false);
            intent.putExtra("stemmaId", stemma_id);
            intent.putExtra("familyId", familyId);
            startActivity(intent);
            /*IntentSkip intentSkip = new IntentSkip();
            intentSkip.skipTreeActivity(mContext, stemma_id, id1, gleanId, false);*/
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1111 && resultCode == 2221) {//创建家谱回来
            web();
        }
    }
}
