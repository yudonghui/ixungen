package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.action.FragmentAdapter;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.TablayoutTabView;
import com.ruihuo.ixungen.ui.familytree.fragment.TreeFormFragment;
import com.ruihuo.ixungen.ui.main.activity.XSJPActivity;
import com.ruihuo.ixungen.view.SearchViewY;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class MyTreeActivity extends BaseNoTitleActivity {
    private ImageView mImage_titlebar_back;
    private SearchViewY mSearchView;
    private TextView mText_build;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private FragmentAdapter mVpAdapter;
    private TreeFormFragment mTreeFormFragmentCreate;
    private TreeFormFragment mTreeFormFragmentJoin;
    private TreeFormFragment mTreeFormFragmentBuy;
    private int type;//决定优先显示三个条目的哪一个 1创建的家谱，2加入的家谱，3购买的家谱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tree);
        type = getIntent().getIntExtra("type", 0);
        initView();
        addFragment();
        mVpAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        //填充适配器
        mViewPager.setAdapter(mVpAdapter);
        //TabLayout加载viewpager
        mTablayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(type);
        addListener();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setHint("搜索");
        mText_build = (TextView) findViewById(R.id.text_build);
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void addFragment() {
        mTitleList.add("创建的家谱");
        mTitleList.add("加入的家谱");
        mTitleList.add("购买的家谱");
        //为TabLayout添加tab名称
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
        mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));
        mViewPager.setOffscreenPageLimit(3);

        mTreeFormFragmentCreate = new TreeFormFragment();
        mTreeFormFragmentCreate.setType(1);
        mTreeFormFragmentJoin = new TreeFormFragment();
        mTreeFormFragmentJoin.setType(2);
        mTreeFormFragmentBuy = new TreeFormFragment();
        mTreeFormFragmentBuy.setType(3);

        mFragmentList.add(mTreeFormFragmentCreate);
        mFragmentList.add(mTreeFormFragmentJoin);
        mFragmentList.add(mTreeFormFragmentBuy);

        mTablayout.post(new Runnable() {
            @Override
            public void run() {
                new TablayoutTabView().setIndicator(mTablayout, DisplayUtil.dip2px(mContext, 15), DisplayUtil.dip2px(mContext, 15));
            }
        });
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchView.setSearchViewListener(SearchListener);//搜索家谱
        mText_build.setOnClickListener(CreatListener);//创建家谱
    }

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
    View.OnClickListener CreatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CreateTreeActivity.class);
            startActivityForResult(intent, 1111);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1111 && resultCode == 2221) {
            mTreeFormFragmentCreate.refreshData();
        } else if (requestCode == 6626 && resultCode == 6626) {
            mTreeFormFragmentCreate.refreshData();
        }
    }
}
