package com.ruihuo.ixungen.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.XWBean;
import com.ruihuo.ixungen.utils.PicassoUtils;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/9/18
 * @describe May the Buddha bless bug-free!!!
 */
public class RedGvAdapter extends BaseAdapter {
    List<XWBean.DataBean> xwDataList;

    public RedGvAdapter(List<XWBean.DataBean> xwDataList) {
        this.xwDataList = xwDataList;
    }

    @Override
    public int getCount() {
        return xwDataList == null ? 0 : xwDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return xwDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_home_gv, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mImagView = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        XWBean.DataBean dataBean = xwDataList.get(position);
        String name = dataBean.getName();
        String img_url = dataBean.getImg_url();
        mViewHolder.mTextView.setText(TextUtils.isEmpty(name) ? "" : name);
        PicassoUtils.setImgView(img_url, R.mipmap.xgwz, parent.getContext(), mViewHolder.mImagView);
        return convertView;
    }

    class ViewHolder {
        ImageView mImagView;
        TextView mTextView;
    }
}
