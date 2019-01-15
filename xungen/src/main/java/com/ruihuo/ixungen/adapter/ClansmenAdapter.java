package com.ruihuo.ixungen.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.ClansmenBean;

import java.util.List;

public class ClansmenAdapter extends BaseAdapter {
    List<ClansmenBean> mList;

    public ClansmenAdapter(List<ClansmenBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_clansmen, null);
        TextView mTitle = convertView.findViewById(R.id.title);
        ImageView mImageView = convertView.findViewById(R.id.imageView);
        ClansmenBean clansmenBean = mList.get(position);
        mImageView.setImageResource(clansmenBean.getResId());
        mTitle.setText(clansmenBean.getTitle());
        return convertView;
    }
}
