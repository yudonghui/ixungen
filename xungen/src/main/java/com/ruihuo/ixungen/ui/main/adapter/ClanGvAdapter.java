package com.ruihuo.ixungen.ui.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.main.bean.BaseClanFormBean;
import com.ruihuo.ixungen.utils.DisplayUtilX;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/26
 * @describe May the Buddha bless bug-free!!!
 */
public class ClanGvAdapter extends BaseAdapter {
    List<BaseClanFormBean> dataList;
    Context mContext;
    private int width;

    public ClanGvAdapter(Context mContext, List<BaseClanFormBean> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int hor = DisplayUtilX.dip2px(30);
        width = (displayWidth - hor) / 3;
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
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_clan_gv, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mBook = (ImageView) convertView.findViewById(R.id.book);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mGeneration = (TextView) convertView.findViewById(R.id.generation);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        BaseClanFormBean baseClanFormBean = dataList.get(position);
        String generation = baseClanFormBean.getGeneration();
        String name = baseClanFormBean.getName();
        String stemma_id = baseClanFormBean.getStemma_id();
        mViewHolder.mGeneration.setText("辈分: " + (TextUtils.isEmpty(generation) ? "--" : generation));
        mViewHolder.mName.setText("《" + (TextUtils.isEmpty(name) ? "--" : name) + "》");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        mViewHolder.mBook.setLayoutParams(layoutParams);
        if (TextUtils.isEmpty(stemma_id))
            mViewHolder.mBook.setImageResource(R.mipmap.book_shelf);
        else mViewHolder.mBook.setImageResource(R.mipmap.book_home);
        return convertView;
    }

    class ViewHolder {
        private ImageView mBook;
        private TextView mName;
        private TextView mGeneration;

    }
}
