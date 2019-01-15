package com.ruihuo.ixungen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.adapter.ArticleAdapter;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/8
 * @describe May the Buddha bless bug-free!!!
 */
public class MessageFragment extends Fragment {
    private View mInflate;
    private Context mContext;
    private MyListView mListView;
    private ArticleAdapter mArticleAdapter;
    private List<HotNewsEntity.DataBean> articleList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_message, null);
        mContext = getContext();
        initView();
        mArticleAdapter = new ArticleAdapter(articleList, mContext, "news");
        mListView.setAdapter(mArticleAdapter);
        addData();
        return mInflate;
    }

    private void initView() {

    }

    private void addData() {

    }

}
