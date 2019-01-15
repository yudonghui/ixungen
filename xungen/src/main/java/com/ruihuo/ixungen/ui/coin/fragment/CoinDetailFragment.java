package com.ruihuo.ixungen.ui.coin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/29
 * @describe May the Buddha bless bug-free!!!
 */
public class CoinDetailFragment extends Fragment {
    private View mInflate;
    private Context mContext;
    private MyListView mListView;
    private DetailAdapter mDetailAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_coin_detail, null);
        this.mContext = getContext();
        initView();
        addData();
        return mInflate;
    }

    private void initView() {
        mListView = (MyListView) mInflate.findViewById(R.id.listView);
    }

    List<String> dataList = new ArrayList<>();

    private void addData() {
        for (int i = 0; i < 20; i++) {
            dataList.add(i + "");
        }
        mDetailAdapter = new DetailAdapter();
        mListView.setAdapter(mDetailAdapter);
    }

    class DetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_coin_detail, null);
                mViewHolder = new ViewHolder();
                mViewHolder.mRemark = (TextView) convertView.findViewById(R.id.remark);
                mViewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
                mViewHolder.mRight = (TextView) convertView.findViewById(R.id.right);
                convertView.setTag(mViewHolder);
            } else mViewHolder = (ViewHolder) convertView.getTag();
            return convertView;
        }
    }

    class ViewHolder {
        private TextView mRemark;
        private TextView mTime;
        private TextView mRight;
    }
}
