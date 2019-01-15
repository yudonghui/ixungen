package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.adapter.RecommendAgnationAdapter;
import com.ruihuo.ixungen.adapter.RecommendPersonAdapter;
import com.ruihuo.ixungen.adapter.RecommendStemmaAdapter;
import com.ruihuo.ixungen.entity.RecommendData;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.TitleBar;

import java.util.List;

public class RecommendActivity extends BaseNoTitleActivity {
    private Context mContext;
    private TitleBar mTitlBar;
    private ScrollView mScrollView;
    private RecyclerView mRv_stemma;
    private RecyclerView mRv_agnation;
    private MyGridView mRv_person;
    private RecommendStemmaAdapter mStemmaAdapter;
    private RecommendAgnationAdapter mAgnationAdapter;
    private RecommendPersonAdapter mPersonAdapter;
    private HttpInterface mHttp;
    private RecommendData.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        mContext = this;
        dataBean = (RecommendData.DataBean) getIntent().getSerializableExtra("dataBean");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("相关推荐");
        mTitlBar.mImageBack.setVisibility(View.GONE);
        mTitlBar.mShare.setImageResource(R.mipmap.close_icon);
        mTitlBar.mShare.setVisibility(View.VISIBLE);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mRv_stemma = (RecyclerView) findViewById(R.id.rv_stemma);
        mRv_agnation = (RecyclerView) findViewById(R.id.rv_agnation);
        mRv_person = (MyGridView) findViewById(R.id.rv_person);

        mHttp = HttpUtilsManager.getInstance(mContext);

        LinearLayoutManager mManagerStemma = new LinearLayoutManager(mContext);
        mManagerStemma.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv_stemma.setLayoutManager(mManagerStemma);
        LinearLayoutManager mManagerAgnation = new LinearLayoutManager(mContext);
        mManagerAgnation.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv_agnation.setLayoutManager(mManagerAgnation);
    }

    private void addListener() {
        mTitlBar.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addData() {
        if (dataBean == null) return;
        List<RecommendData.DataBean.StemmaBean> stemmaList = dataBean.getStemma();
        List<RecommendData.DataBean.ClanAssociationBean> agnationList = dataBean.getClan_association();
        List<RecommendData.DataBean.FriendBean> friendList = dataBean.getFriend();
        mStemmaAdapter = new RecommendStemmaAdapter(mContext, stemmaList);
        mRv_stemma.setAdapter(mStemmaAdapter);

        mAgnationAdapter = new RecommendAgnationAdapter(mContext, agnationList);
        mRv_agnation.setAdapter(mAgnationAdapter);

        mPersonAdapter = new RecommendPersonAdapter(mContext, friendList);
        mRv_person.setAdapter(mPersonAdapter);
    }
}
