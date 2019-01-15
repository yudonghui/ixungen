package com.ruihuo.ixungen.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/5/18
 * @describe May the Buddha bless bug-free!!!
 */
public class PopMediaAdapter extends BaseAdapter {
    int[] ints = new int[30];

    public PopMediaAdapter(int[] ints) {
        this.ints = ints;
    }

    @Override
    public int getCount() {
        return ints.length;
    }

    @Override
    public Object getItem(int position) {
        return ints[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_pop_media, null);
        TextView mTextView = (TextView) convertView.findViewById(R.id.textnum);
        mTextView.setText(ints[position]+"");
        return convertView;
    }
}
