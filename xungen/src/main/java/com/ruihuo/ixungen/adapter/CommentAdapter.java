package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.entity.PostsSmsBean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/17
 * @describe May the Buddha bless bug-free!!!
 */
public class CommentAdapter extends BaseAdapter {
    List<PostsSmsBean.DataBean.ReplyInfoBean> replyInfoList;
    Context mContext;

    public CommentAdapter(List<PostsSmsBean.DataBean.ReplyInfoBean> replyInfoList, Context mContext) {
        this.replyInfoList = replyInfoList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return replyInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_comment, null);
        TextView mReplyContent = (TextView) convertView.findViewById(R.id.reply_content);
        TextView mReplyNikename = (TextView) convertView.findViewById(R.id.reply_nikename);
        TextView mToNikename = (TextView) convertView.findViewById(R.id.to_nikename);
        final PostsSmsBean.DataBean.ReplyInfoBean replyInfoBean = replyInfoList.get(position);
        mReplyNikename.setText(replyInfoBean.getNikename());
        mToNikename.setText(replyInfoBean.getTo_nikename());
        mReplyContent.setText(": " + replyInfoBean.getReply_content());
        mReplyNikename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                //当前点击的这个人的rid
                intent.putExtra("rid",replyInfoBean.getReply_rid());
                mContext.startActivity(intent);
            }
        });
        mToNikename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                //当前点击的这个人的rid
                intent.putExtra("rid",replyInfoBean.getTo_rid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
