package com.ruihuo.ixungen.adapter;

import android.content.Context;
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
 * @date 2017/4/12
 * @describe May the Buddha bless bug-free!!!
 */
public class FriendFormAdapter extends BaseAdapter {
    private List<FriendBean.DataBean> friendList;
    private String from;
    private boolean isCheck;
    private Context mContext;

    public FriendFormAdapter(List<FriendBean.DataBean> friendList, String from) {
        this.friendList = friendList;
        this.from = from;
    }

    public void isCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        mContext = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_newfriend, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mImage_header = (ImageView) convertView.findViewById(R.id.image_header);
            mViewHolder.mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mViewHolder.mText_rid = (TextView) convertView.findViewById(R.id.text_rid);
            mViewHolder.mFriend_status = (TextView) convertView.findViewById(R.id.friend_status);
            mViewHolder.mIsCheck = (ImageView) convertView.findViewById(R.id.ischeck);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        switch (from) {
            case "agnation":
                //宗亲成员列表
                mViewHolder.mFriend_status.setVisibility(View.GONE);
                if (isCheck) {
                    mViewHolder.mIsCheck.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.mIsCheck.setVisibility(View.GONE);
                }
                break;
            default:
                mViewHolder.mFriend_status.setVisibility(View.GONE);
                break;
        }

        FriendBean.DataBean dataBean = friendList.get(position);
        mViewHolder.mTv_name.setText(dataBean.getNikename());
        mViewHolder.mText_rid.setText("ID：" + dataBean.getRid());
        //头像
        PicassoUtils.setImgView(dataBean.getAvatar(), mContext, R.mipmap.default_header,mViewHolder.mImage_header);
        return convertView;
    }

    class ViewHolder {
        ImageView mImage_header;
        TextView mTv_name;
        TextView mText_rid;
        TextView mFriend_status;
        ImageView mIsCheck;
    }
}
