package com.ruihuo.ixungen.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.CityModel;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class CityAdapter extends BaseAdapter {
    List<CityModel.DataBean> data;
    public CityAdapter (List<CityModel.DataBean> data){
       this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if(convertView==null){
            convertView=View.inflate(parent.getContext(), R.layout.item_city,null);
            mViewHolder=new ViewHolder();
            mViewHolder.mTextView= (TextView) convertView.findViewById(R.id.text_city_name);
            convertView.setTag(mViewHolder);
        }else {
           mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTextView.setText(data.get(position).getName());
        return convertView;
    }
    class ViewHolder{
       TextView mTextView;
    }
}
