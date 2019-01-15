package com.ruihuo.ixungen.ui.coin.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.ui.coin.contract.CoinConstract;
import com.ruihuo.ixungen.ui.coin.fragment.CoinDetailFragment;
import com.ruihuo.ixungen.ui.coin.fragment.CoinRechargeFragment;
import com.ruihuo.ixungen.ui.coin.presenter.CoinPresenter;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

public class CoinActivity extends BaseNoTitleActivity implements CoinConstract.View {
    CoinPresenter mPresenter = new CoinPresenter(this);
    private TitleBar mTitlBar;
    private ScrollView mScrollView;
    private SmartRefreshLayout mRefresh;
    private TextView mCoinNum;
    private RelativeLayout mRecharge;
    private TextView mRecharge_text;
    private View mRecharge_block;
    private RelativeLayout mDetail;
    private TextView mDetail_text;
    private View mDetail_block;
    private FrameLayout mFl;
    private CoinRechargeFragment mRechargeFragment;
    private CoinDetailFragment mDetailFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        initView();
        initFragment();
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("根币");
        mTitlBar.mTextRegister.setText("说明");
        mTitlBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitlBar.mImageBack.setFocusable(true);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mCoinNum = (TextView) findViewById(R.id.coinNum);
        mRecharge = (RelativeLayout) findViewById(R.id.recharge);
        mRecharge_text = (TextView) findViewById(R.id.recharge_text);
        mRecharge_block = (View) findViewById(R.id.recharge_block);
        mDetail = (RelativeLayout) findViewById(R.id.detail);
        mDetail_text = (TextView) findViewById(R.id.detail_text);
        mDetail_block = (View) findViewById(R.id.detail_block);
        mFl = (FrameLayout) findViewById(R.id.fl);
    }

    private void initFragment() {
        mRechargeFragment = new CoinRechargeFragment();
        mDetailFragment = new CoinDetailFragment();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl, mRechargeFragment).add(R.id.fl, mDetailFragment)
                .show(mRechargeFragment).hide(mDetailFragment);
        fragmentTransaction.commit();
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitlBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRecharge.setOnClickListener(TabListener);
        mDetail.setOnClickListener(TabListener);
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
    }

    private void addData() {

    }

    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            switch (v.getId()) {
                case R.id.recharge:
                    mRecharge_block.setVisibility(View.VISIBLE);
                    mRecharge_text.setTextColor(getResources().getColor(R.color.green_txt));
                    mDetail_block.setVisibility(View.INVISIBLE);
                    mDetail_text.setTextColor(getResources().getColor(R.color.deep_txt));
                    fragmentTransaction.show(mRechargeFragment).hide(mDetailFragment);
                    mRefresh.setEnableLoadMore(false);
                    break;
                case R.id.detail:
                    mRecharge_block.setVisibility(View.INVISIBLE);
                    mRecharge_text.setTextColor(getResources().getColor(R.color.deep_txt));
                    mDetail_block.setVisibility(View.VISIBLE);
                    mDetail_text.setTextColor(getResources().getColor(R.color.green_txt));
                    fragmentTransaction.hide(mRechargeFragment).show(mDetailFragment);
                    mRefresh.setEnableLoadMore(true);
                    break;
            }
            fragmentTransaction.commit();
        }
    };

    com.ydh.refresh_layout.listener.OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            mRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                    mRefresh.finishRefresh();
                }
            }, 1000);
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            mRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                    mRefresh.finishLoadMore();
                }
            }, 1000);

        }
    };
}
