package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.action.FragmentAdapter;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.ConstantNum;

import java.util.ArrayList;
import java.util.List;

public class OrderFormActivity extends BaseNoTitleActivity {
    private Context mContext;

    private RelativeLayout mRl_title;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private ImageView mSearch;
    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private FragmentAdapter mVpAdapter;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private int from;//具体从哪一个过来的。
    private int type;//0-普通消费者查看订单列表；1-店家查看订单列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        from = intent.getIntExtra("from", 0);
        mContext = this;
        initView();
        addFragment();
        mVpAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        //填充适配器
        mViewpager.setAdapter(mVpAdapter);
        //TabLayout加载viewpager
        mTablayout.setupWithViewPager(mViewpager);
        addListener();
        mViewpager.setCurrentItem(from);
    }

    private void initView() {
        mRl_title = (RelativeLayout) findViewById(R.id.rl_title);
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mSearch = (ImageView) findViewById(R.id.search);
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        if (type == 0) {
            mText_title.setText("我的订单");
            //  mRl_title.setBackgroundColor(getResources().getColor(R.color.brown_bg));
            mRl_title.setBackgroundResource(R.color.brown_bg);
        } else {
            mText_title.setText("全部订单");
            mRl_title.setBackgroundResource(R.drawable.shape_shops_title);
        }
    }

    private OrderFormNFragment AllNFragment;
    private OrderFormNFragment WaitPayNFragment;
    private OrderFormNFragment WaitUsedNFragment;
    private OrderFormNFragment WaitRecommendNFragment;
    private OrderFormNFragment QuitNFragment;
    private OrderFormSFragment AllSFragment;
    private OrderFormSFragment WaitPaySFragment;
    private OrderFormSFragment WaitQuitSFragment;
    private OrderFormSFragment WaitReplySFragment;
    private OrderFormSFragment WaitUsedSFragment;
    private OrderFormSFragment FinishedSFragment;

    private void addFragment() {
        if (type == 0) {
            mTitleList.add("全部");
            mTitleList.add("待付款");
            mTitleList.add("待使用");
            mTitleList.add("待评价");
            mTitleList.add("退款/售后");
            //为TabLayout添加tab名称
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(3)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(4)));
            mViewpager.setOffscreenPageLimit(5);
            AllNFragment = new OrderFormNFragment();
            WaitPayNFragment = new OrderFormNFragment();
            WaitUsedNFragment = new OrderFormNFragment();
            WaitRecommendNFragment = new OrderFormNFragment();
            QuitNFragment = new OrderFormNFragment();

            AllNFragment.setData(type, ConstantNum.ORDER_ALLN);
            WaitPayNFragment.setData(type, ConstantNum.ORDER_WAIT_PAYN);
            WaitUsedNFragment.setData(type, ConstantNum.ORDER_WAIT_USEDN);
            WaitRecommendNFragment.setData(type, ConstantNum.ORDER_WAIT_RECOMMENDN);
            QuitNFragment.setData(type, ConstantNum.ORDER_QUITN);


            mFragmentList.add(AllNFragment);
            mFragmentList.add(WaitPayNFragment);
            mFragmentList.add(WaitUsedNFragment);
            mFragmentList.add(WaitRecommendNFragment);
            mFragmentList.add(QuitNFragment);
        } else {
            mTitleList.add("全部");
            mTitleList.add("待付款");
            mTitleList.add("待退款");
            mTitleList.add("待回复");
            mTitleList.add("待使用");
            mTitleList.add("已完成");
            //为TabLayout添加tab名称
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(3)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(4)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(5)));
            mViewpager.setOffscreenPageLimit(6);
            AllSFragment = new OrderFormSFragment();
            WaitPaySFragment = new OrderFormSFragment();
            WaitQuitSFragment = new OrderFormSFragment();
            WaitReplySFragment = new OrderFormSFragment();
            WaitUsedSFragment = new OrderFormSFragment();
            FinishedSFragment = new OrderFormSFragment();

            AllSFragment.setData(type, ConstantNum.ORDER_ALLN);
            WaitPaySFragment.setData(type, ConstantNum.ORDER_WAIT_PAYS);
            WaitQuitSFragment.setData(type, ConstantNum.ORDER_WAIT_QUITS);
            WaitReplySFragment.setData(type, ConstantNum.ORDER_WAIT_REPLYS);
            WaitUsedSFragment.setData(type, ConstantNum.ORDER_WAIT_USEDS);
            FinishedSFragment.setData(type, ConstantNum.ORDER_FINISHEDS);

            mFragmentList.add(AllSFragment);
            mFragmentList.add(WaitPaySFragment);
            mFragmentList.add(WaitQuitSFragment);
            mFragmentList.add(WaitReplySFragment);
            mFragmentList.add(WaitUsedSFragment);
            mFragmentList.add(FinishedSFragment);
        }
    }

    private String selectTab = "全部";

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ////选中了tab的逻辑
                selectTab = tab.getText().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中tab的逻辑
                String s = tab.getText().toString();
                Log.e("onTabUnselected", s);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //再次选中tab的逻辑
                String s = tab.getText().toString();
                Log.e("onTabReselected", s);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (type == 0) {
            switch (selectTab) {
                case "待付款":
                    WaitPayNFragment.notifiData();
                    break;
                case "待使用":
                    WaitUsedNFragment.notifiData();
                    break;
                case "待评价":
                    WaitRecommendNFragment.notifiData();
                    break;
                case "退款/售后":
                    QuitNFragment.notifiData();
                    break;
            }
            AllNFragment.notifiData();//无论如何  全部  这个必须要刷新
        } else {
            switch (selectTab) {
                case "待付款":
                    WaitPaySFragment.notifiData();
                    break;
                case "待退款":
                    WaitQuitSFragment.notifiData();
                    break;
                case "待回复":
                    WaitReplySFragment.notifiData();
                    break;
                case "待使用":
                    WaitUsedSFragment.notifiData();
                    break;
                case "已完成":
                    FinishedSFragment.notifiData();
                    break;
            }
            AllSFragment.notifiData();//无论如何  全部  这个必须要刷新
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
