package com.ruihuo.ixungen.ui.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.main.bean.BaseClanFormBean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/18
 * @describe May the Buddha bless bug-free!!!
 */
public class ClanFormAdapter extends BaseAdapter {
    List<BaseClanFormBean> dataList;
    Context mContext;

    public ClanFormAdapter(List<BaseClanFormBean> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

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
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_clan_form, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mCover = (ImageView) convertView.findViewById(R.id.cover);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
            mViewHolder.mAddress = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        BaseClanFormBean dataBean = dataList.get(position);
        String stemma_generation = dataBean.getStemma_generation();//族谱的辈分
        String region_name = dataBean.getRegion_name();//族谱的地址
        String stemma_name = dataBean.getStemma_name();//家谱名字
        String generation = dataBean.getGeneration();//家谱的辈分。
        String stemma_id = dataBean.getStemma_id();
        String region = dataBean.getRegion();
        String name = dataBean.getName();
        String surname = dataBean.getSurname();
        if (!TextUtils.isEmpty(stemma_id)) {//只有族谱才有这个字段
            mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            mViewHolder.mTime.setText("字    辈: " + (TextUtils.isEmpty(stemma_generation) ? "--" : stemma_generation));
            mViewHolder.mAddress.setText("来源于:" + (TextUtils.isEmpty(stemma_name) ? "--" : stemma_name));
            mViewHolder.mCover.setImageResource(R.mipmap.system_book);
        } else {
            mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            mViewHolder.mTime.setText("字    辈: " + (TextUtils.isEmpty(generation) ? "--" : generation));
            if (TextUtils.isEmpty(region_name)) {
                mViewHolder.mAddress.setText("来源于: " + (TextUtils.isEmpty(region) ? "--" : region));
            } else mViewHolder.mAddress.setText("来源于: " + region_name);
            mViewHolder.mCover.setImageResource(R.mipmap.family_book);
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView mCover;
        private TextView mName;
        private TextView mTime;
        private TextView mAddress;
    }
}
