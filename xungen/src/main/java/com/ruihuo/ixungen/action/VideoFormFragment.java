package com.ruihuo.ixungen.action;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/26
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoFormFragment extends Fragment {
    private View v;
    private MyListView mPtrlRecord;
    private ImageView mNoData;
    private int pn = 1;
    private int totalPage = 1;
    private Context mContext;
    private int type;
    List<HotNewsEntity.DataBean> mList;
    private VideoFormAdapter mAdapter;

    public void setData(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_video_form, null);
        mContext = getActivity();
        initView();
        addListener();
        addData();
        mAdapter = new VideoFormAdapter(mList, mContext);
        mPtrlRecord.setAdapter(mAdapter);
        return v;
    }

    private void initView() {
        mPtrlRecord = (MyListView) v.findViewById(R.id.ptrl_record);
        //mPtrlRecord.setMode(PullToRefreshBase.Mode.BOTH);
        mNoData = (ImageView) v.findViewById(R.id.no_data);
    }

    private void addData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HotNewsEntity.DataBean dataBean = new HotNewsEntity.DataBean();
            dataBean.setTitle("zhongguo");
            dataBean.setAuthor("欲动火");
            dataBean.setCount("100");
            mList.add(dataBean);
        }
    }

    private void addListener() {

    }
}
