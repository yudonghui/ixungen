package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.utils.PicassoUtils;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/15
 * @describe May the Buddha bless bug-free!!!
 */
public class SearchFriendAdapter extends BaseAdapter {
    List<FriendBean.DataBean> searchList;
    Context mContext;

    public SearchFriendAdapter(Context mContext, List<FriendBean.DataBean> searchList) {
        this.searchList = searchList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_friend_search, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            mViewHolder.imageAvatar = (ImageView) convertView.findViewById(R.id.image_header);
            mViewHolder.tvRid = (TextView) convertView.findViewById(R.id.text_rid);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        FriendBean.DataBean dataBean = searchList.get(position);
        String nikename = dataBean.getNikename();
        String rid = dataBean.getRid();
        String avatar = dataBean.getAvatar();
        mViewHolder.tvName.setText(TextUtils.isEmpty(nikename) ? "--" : nikename);
        mViewHolder.tvRid.setText("ID: " + (TextUtils.isEmpty(rid) ? "--" : rid));
        PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mViewHolder.imageAvatar);
        return convertView;
    }

    class ViewHolder {
        private TextView tvName;
        private ImageView imageAvatar;
        private TextView tvRid;
    }
}
