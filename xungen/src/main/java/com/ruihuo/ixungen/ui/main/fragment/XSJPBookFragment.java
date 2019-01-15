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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.ui.familytree.activity.CreateTreeActivity;
import com.ruihuo.ixungen.ui.familytree.activity.MyTreeActivity;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.ui.main.activity.XSJPActivity;
import com.ruihuo.ixungen.ui.main.adapter.ClanGvAdapter;
import com.ruihuo.ixungen.ui.main.bean.BaseClanFormBean;
import com.ruihuo.ixungen.ui.main.bean.ClanFormBean;
import com.ruihuo.ixungen.ui.main.contract.XSJPBookContract;
import com.ruihuo.ixungen.ui.main.presenter.XSJPBookPresenter;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/26
 * @describe May the Buddha bless bug-free!!!
 */
public class XSJPBookFragment extends Fragment implements XSJPBookContract.View {
    XSJPBookPresenter mPresenter = new XSJPBookPresenter(this);
    private Context mContext;
    private View mInflate;
    private TitleBar mTitlBar;
    private SearchViewY mSearchView;
    private SmartRefreshLayout mRefresh;
    private ScrollView mScrollView;
    private ImageView mCreateStemma;
    private FrameLayout mNoBuild;
    private LinearLayout mLl_data;
    private ImageView mClan_book;
    private TextView mName;
    private TextView mBuilder;
    private TextView mDate;
    private TextView mMore;
    private MyGridView mGridView;
    private ClanGvAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_xsjp_book, null);
        mContext = getContext();
        initView();
        mAdapter = new ClanGvAdapter(mContext, dataList);
        mGridView.setAdapter(mAdapter);
        addListner();
        addData();
        return mInflate;
    }

    private void initView() {
        mTitlBar = (TitleBar) mInflate.findViewById(R.id.titlBar);
        mTitlBar.setTitle("姓氏家谱");
        mTitlBar.mImageBack.setVisibility(View.GONE);
        mTitlBar.mShare.setVisibility(View.VISIBLE);
        mTitlBar.mShare.setImageResource(R.mipmap.family);
        mSearchView = (SearchViewY) mInflate.findViewById(R.id.searchView);
        mRefresh = (SmartRefreshLayout) mInflate.findViewById(R.id.refresh);
        mScrollView = (ScrollView) mInflate.findViewById(R.id.scrollView);
        mCreateStemma = (ImageView) mInflate.findViewById(R.id.creatStemma);
        mNoBuild = (FrameLayout) mInflate.findViewById(R.id.noBuild);
        mLl_data = (LinearLayout) mInflate.findViewById(R.id.ll_data);
        mClan_book = (ImageView) mInflate.findViewById(R.id.clan_book);
        mName = (TextView) mInflate.findViewById(R.id.name);
        mBuilder = (TextView) mInflate.findViewById(R.id.builder);
        mDate = (TextView) mInflate.findViewById(R.id.date);
        mMore = (TextView) mInflate.findViewById(R.id.more);
        mGridView = (MyGridView) mInflate.findViewById(R.id.gridView);
    }

    private void addListner() {
        mTitlBar.mShare.setOnClickListener(FamilyListener);
        mCreateStemma.setOnClickListener(CreateStemmaListener);//创建家谱
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        mSearchView.setSearchViewListener(SearchListener);
        mGridView.setOnItemClickListener(OnItemClickListener);
        mLl_data.setOnClickListener(MyStemmaListener);//我的家谱点击
        mMore.setOnClickListener(MoreListener);//查看更多家谱
    }

    private void addData() {
        myData();//我的家谱的数据
        systemData();//系统家谱的数据
    }

    public void myData() {
        Bundle params = new Bundle();
        params.putString("limit", "1");
        params.putString("page", "1");
        params.putString("type", "1");//type=1,创建家谱，2加入的家谱，3购买的家谱
        params.putString("token", XunGenApp.token);
        mPresenter.getMyData(params, mContext);
    }

    private Bundle params = new Bundle();
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;

    private void systemData() {
        params.clear();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("flagSelf", "true");
        mPresenter.getSystemData(params, mContext);
    }

    AdapterView.OnItemClickListener OnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseClanFormBean baseClanFormBean = dataList.get(position);
            String stemma_id = baseClanFormBean.getStemma_id();
            String id1 = baseClanFormBean.getId();
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            // intent.putExtra("id", id1);
            intent.putExtra("stemmaId", stemma_id);
            intent.putExtra("flag", true);
            startActivity(intent);
        }
    };
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            Intent intent = new Intent(mContext, XSJPActivity.class);
            intent.putExtra("keyword", text);
            startActivity(intent);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onChange(String text) {

        }
    };
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            page = 1;
            dataList.clear();
            addData();
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            if (page < totalPage) {
                page++;
                systemData();
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

    View.OnClickListener MyStemmaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MyClanFormBean == null) return;
            ClanFormBean.DataBean dataBean = MyClanFormBean.getData().get(0);
            String stemmaId = dataBean.getStemma_id();
            String id = dataBean.getId();
            String familyId = dataBean.getFamily_id();
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            //  intent.putExtra("id", id);
            intent.putExtra("stemmaId", stemmaId);
            intent.putExtra("familyId", familyId);
            intent.putExtra("flag", false);
            ((Activity) mContext).startActivityForResult(intent, 6626);
        }
    };
    View.OnClickListener MoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MyTreeActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener FamilyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MyTreeActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener CreateStemmaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CreateTreeActivity.class);
            ((Activity) mContext).startActivityForResult(intent, 1111);
        }
    };
    List<BaseClanFormBean> dataList = new ArrayList<>();

    @Override
    public void getSystemSuccess(String result) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
        Gson gson = GsonUtils.getGson();
        ClanFormBean clanFormBean = gson.fromJson(result, ClanFormBean.class);
        totalPage = clanFormBean.getTotalPage();
        dataList.addAll(clanFormBean.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSystemError(String error) {
        mRefresh.finishRefresh();
        mRefresh.finishLoadMore();
    }

    private ClanFormBean MyClanFormBean;

    @Override
    public void getMySuccess(String result) {
        Gson gson = GsonUtils.getGson();
        MyClanFormBean = gson.fromJson(result, ClanFormBean.class);
        List<ClanFormBean.DataBean> data = MyClanFormBean.getData();
        if (data == null || data.size() == 0) {
            mLl_data.setVisibility(View.GONE);
            mNoBuild.setVisibility(View.VISIBLE);
        } else {
            mLl_data.setVisibility(View.VISIBLE);
            mNoBuild.setVisibility(View.GONE);
            ClanFormBean.DataBean dataBean = data.get(0);
            String name = dataBean.getName();
            String create_time = dataBean.getCreate_time();
            String region = dataBean.getRegion();
            String address = dataBean.getAddress();
            mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            if (TextUtils.isEmpty(region))
                mBuilder.setText("来  源  于: " + (TextUtils.isEmpty(address) ? "--" : address));
            else mBuilder.setText("来  源  于: " + region);
            mDate.setText("创建日期: " + DateFormatUtils.longToDateM(create_time));
        }
    }

    @Override
    public void getMyError(String error) {

    }
}
