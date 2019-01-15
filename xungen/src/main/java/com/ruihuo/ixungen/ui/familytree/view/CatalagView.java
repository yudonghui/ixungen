package com.ruihuo.ixungen.ui.familytree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.adapter.CatalagAdapter;
import com.ruihuo.ixungen.ui.familytree.bean.CatalagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public class CatalagView extends LinearLayout {
    private Context mContext;
    private View mInflate;
    private ExpandableListView mListView;
    private CatalagAdapter mAdapter;

    public CatalagView(Context context) {
        super(context);
        mContext = context;
        mInflate = View.inflate(context, R.layout.catalag_view, this);
        initView();
    }

    public CatalagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflate = View.inflate(context, R.layout.catalag_view, this);
        initView();
    }

    private void initView() {
        mListView = (ExpandableListView) mInflate.findViewById(R.id.listView);
        mAdapter = new CatalagAdapter(dataList, mContext, ChildItemListener);
        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Toast.makeText(mContext, groupPosition + "", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    CatalagAdapter.CatalogInterface ChildItemListener = new CatalagAdapter.CatalogInterface() {
        @Override
        public void callBack(int groupPosition, int childPosition) {
            Toast.makeText(mContext, groupPosition + "--" + childPosition, Toast.LENGTH_SHORT).show();
        }
    };
    private List<List<CatalagBean.DataBean>> dataList = new ArrayList<>();

    public void setData(List<List<CatalagBean.DataBean>> dataBeanList) {
        dataList.clear();
        dataList.addAll(dataBeanList);
        mAdapter.notifyDataSetChanged();
        for (int i = 0; i < dataList.size(); i++) {
            mListView.expandGroup(i);
        }
    }

}
