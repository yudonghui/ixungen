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
import com.ruihuo.ixungen.geninterface.CallBackViewInterface;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/20
 * @describe May the Buddha bless bug-free!!!
 */
public class NewFriendAdapter extends BaseAdapter {
    List<FriendBean.DataBean> friendList;
    private Context mContext;

    public NewFriendAdapter(List<FriendBean.DataBean> friendList, Context mContext) {
        this.friendList = friendList;
        this.mContext = mContext;
    }
    private CallBackViewInterface mCallBack;
    public void setIsAccept(CallBackViewInterface mCallBack){
        this.mCallBack=mCallBack;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_newfriend, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mImage_header = (ImageView) convertView.findViewById(R.id.image_header);
            mViewHolder.mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mViewHolder.mText_rid = (TextView) convertView.findViewById(R.id.text_rid);
            mViewHolder.mFriend_status = (TextView) convertView.findViewById(R.id.friend_status);
            mViewHolder.mFriend_status.setVisibility(View.VISIBLE);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final FriendBean.DataBean dataBean = friendList.get(position);
        String nikename = dataBean.getNikename();
        mViewHolder.mTv_name.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
        String rid = dataBean.getRid();
        mViewHolder.mText_rid.setText("ID：" + (TextUtils.isEmpty(rid) ? "" : rid));
        String status = dataBean.getStatus();
        switch (status) {
            case "0":
                mViewHolder.mFriend_status.setText("已添加");
                mViewHolder.mFriend_status.setTextColor(mContext.getResources().getColor(R.color.gray_txt));
                mViewHolder.mFriend_status.setBackgroundResource(R.drawable.shape_gray_box);
                break;
            case "1":

                break;
            case "2":
                mViewHolder.mFriend_status.setText("接受");
                mViewHolder.mFriend_status.setTextColor(mContext.getResources().getColor(R.color.white));
                mViewHolder.mFriend_status.setBackgroundResource(R.drawable.shape_btn_bg);
                mViewHolder.mFriend_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallBack.callBack(mViewHolder.mFriend_status,position);
                    }
                });
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mIscheck;
        ImageView mImage_header;
        TextView mTv_name;
        TextView mText_rid;
        TextView mFriend_status;
    }
}
