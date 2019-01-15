package com.ruihuo.ixungen.ui.familytree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.CatalogInface;
import com.ruihuo.ixungen.ui.familytree.adapter.CatalagAdapter;
import com.ruihuo.ixungen.ui.familytree.bean.CatalagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class CatalogFragment extends Fragment {
    private View mInflate;
    private Context mContext;
    private ExpandableListView mListView;
    private LinearLayout mLl_xsly;
    private LinearLayout mLl_jgjx;
    private LinearLayout mLl_jzmr;
    private CatalagAdapter mAdapter;
    private CatalogInface mCatalogInface;
    private int vpPage;

    public void setVpPage(int vpPage) {
        this.vpPage = vpPage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.catalag_view, null);
        mContext = getContext();
        initView();

        addListener();
        return mInflate;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            addData();
        }
    }

    private void initView() {
        mListView = (ExpandableListView) mInflate.findViewById(R.id.listView);
        mLl_xsly = (LinearLayout) mInflate.findViewById(R.id.ll_xsly);
        mLl_jgjx = (LinearLayout) mInflate.findViewById(R.id.ll_jgjx);
        mLl_jzmr = (LinearLayout) mInflate.findViewById(R.id.ll_jzmr);
    }

    private void addListener() {
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Toast.makeText(mContext, groupPosition + "", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mLl_xsly.setOnClickListener(LListener);
        mLl_jgjx.setOnClickListener(LListener);
        mLl_jzmr.setOnClickListener(LListener);
    }

    private void addData() {
        if (BookFragment.catalogueMap!=null&&BookFragment.catalogueMap.size()>0){
            dataList.clear();
            dataList.addAll(BookFragment.catalogueMap.get(1));
            mAdapter = new CatalagAdapter(dataList, mContext, ChildItemListener);
            mListView.setAdapter(mAdapter);
            for (int i = 0; i < dataList.size(); i++) {
                mListView.expandGroup(i);
            }
        }
    }

    View.OnClickListener LListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_xsly:
                    mCatalogInface.callBackTop(1);
                    break;
                case R.id.ll_jgjx:
                    mCatalogInface.callBackTop(2);
                    break;
                case R.id.ll_jzmr:
                    mCatalogInface.callBackTop(3);
                    break;
            }
        }
    };
    CatalagAdapter.CatalogInterface ChildItemListener = new CatalagAdapter.CatalogInterface() {
        @Override
        public void callBack(int groupPosition, int childPosition) {
            CatalagBean.DataBean dataBean = dataList.get(groupPosition).get(childPosition);
            if (mCatalogInface != null) mCatalogInface.callBack(dataBean, vpPage);
            // Toast.makeText(mContext, groupPosition + "--" + childPosition, Toast.LENGTH_SHORT).show();
        }
    };
    private List<List<CatalagBean.DataBean>> dataList=new ArrayList<>();

    public void setData(CatalogInface mCatalogInface) {
        this.mCatalogInface = mCatalogInface;
    }
}
