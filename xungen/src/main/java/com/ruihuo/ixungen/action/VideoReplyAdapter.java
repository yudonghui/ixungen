package com.ruihuo.ixungen.action;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.CallBackBeanInterface;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/6/1
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoReplyAdapter extends BaseAdapter {
    List<VideoDetailBean.DataBean.CommentInfoBean.ReplyInfoBean> reply_info;
    CallBackBeanInterface mListener;

    public VideoReplyAdapter(List<VideoDetailBean.DataBean.CommentInfoBean.ReplyInfoBean> reply_info,
                             CallBackBeanInterface mListener) {
        this.reply_info = reply_info;
        this.mListener = mListener;
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
        convertView = View.inflate(parent.getContext(), R.layout.item_reply, null);
        TextView mReplyName = (TextView) convertView.findViewById(R.id.reply_name);
        TextView mReplyContent = (TextView) convertView.findViewById(R.id.reply_content);
        final VideoDetailBean.DataBean.CommentInfoBean.ReplyInfoBean replyInfoBean = reply_info.get(position);
        String reply_content = replyInfoBean.getReply_content();
        final String reply_name = replyInfoBean.getReply_name();
        String to_name = replyInfoBean.getTo_name();
        final String reply_rid = replyInfoBean.getReply_rid();
        final String comment_id = replyInfoBean.getComment_id();
        mReplyName.setText(reply_name + " 回复 " + to_name + ":");
        mReplyContent.setText(TextUtils.isEmpty(reply_content) ? "" : reply_content);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callBack(comment_id, reply_rid, reply_name);
            }
        });
        return convertView;
    }
}
