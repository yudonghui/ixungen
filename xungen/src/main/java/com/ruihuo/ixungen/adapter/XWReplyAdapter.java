package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.entity.XWFormDetailBean;
import com.ruihuo.ixungen.geninterface.XWInterface;

import java.util.List;


/**
 * @author yudonghui
 * @date 2017/7/31
 * @describe May the Buddha bless bug-free!!!
 */
public class XWReplyAdapter extends BaseAdapter {
    List<XWFormDetailBean.DataBean.CommentInfoBean.ReplyInfoBean> reply_info;
    XWInterface mListener;
    Context mContext;

    public XWReplyAdapter(List<XWFormDetailBean.DataBean.CommentInfoBean.ReplyInfoBean> reply_info,
                          XWInterface mListener, Context mContext) {
        this.reply_info = reply_info;
        this.mListener = mListener;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return reply_info.size();
    }

    @Override
    public Object getItem(int position) {
        return reply_info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_xw_reply, null);
        TextView mReplyName = (TextView) convertView.findViewById(R.id.reply_name);
        TextView mToName = (TextView) convertView.findViewById(R.id.to_name);
        final TextView mReplyContent = (TextView) convertView.findViewById(R.id.reply_content);
        final TextView mReplyContent2 = (TextView) convertView.findViewById(R.id.reply_content2);
        final XWFormDetailBean.DataBean.CommentInfoBean.ReplyInfoBean replyInfoBean = reply_info.get(position);
        String reply_content = replyInfoBean.getReply_content();
        String reply_name = replyInfoBean.getNikename();
        String to_name = replyInfoBean.getTo_nikename();
        final String comment_id = replyInfoBean.getComment_id();
        final String reply_rid = replyInfoBean.getReply_rid();
        final String nikename = replyInfoBean.getNikename();
        mReplyName.setText(TextUtils.isEmpty(reply_name) ? "" : reply_name);
        mToName.setText(TextUtils.isEmpty(to_name) ? "" : to_name);
        if (!TextUtils.isEmpty(reply_content)) {
            if (reply_content.length() > 14) {
                mReplyContent.setVisibility(View.VISIBLE);
                mReplyContent2.setVisibility(View.VISIBLE);
                String substring = reply_content.substring(0, 14);
                mReplyContent.setText(substring);
                mReplyContent2.setText(reply_content.substring(14, reply_content.length()));
            } else{
                mReplyContent2.setVisibility(View.GONE);
                mReplyContent.setVisibility(View.VISIBLE);
                mReplyContent.setText(reply_content);
            }
        }else {
            mReplyContent.setVisibility(View.GONE);
            mReplyContent2.setVisibility(View.GONE);
        }
        mReplyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callBack(comment_id, reply_rid, nikename);
            }
        });
        mReplyContent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callBack(comment_id, reply_rid, nikename);
            }
        });
        mReplyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                //当前点击的这个人的rid
                intent.putExtra("rid", replyInfoBean.getReply_rid());
                mContext.startActivity(intent);
            }
        });
        mToName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                //当前点击的这个人的rid
                intent.putExtra("rid", replyInfoBean.getTo_rid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
