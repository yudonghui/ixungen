package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.entity.CommentBaseBeand;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.geninterface.XWInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.MyListView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/31
 * @describe May the Buddha bless bug-free!!!
 */
public class XWCommentAdapter extends BaseAdapter {
    private List<CommentBaseBeand> commentInfoList;
    Context mContext;
    XWInterface mListener;
    int mode;//1,寻根问祖的帖子评论；2，好友动态的评论。

    public XWCommentAdapter(List<CommentBaseBeand> commentInfoList, Context mContext, int mode) {
        this.commentInfoList = commentInfoList;
        this.mContext = mContext;
        this.mode = mode;
    }

    public void setListener(XWInterface mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return commentInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_video_comment, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mComment_avatar = (CircleImageView) convertView.findViewById(R.id.comment_avatar);
            mViewHolder.mComment_nikename = (TextView) convertView.findViewById(R.id.comment_nikename);
            mViewHolder.mComment_time = (TextView) convertView.findViewById(R.id.comment_time);
            mViewHolder.mImage_praise = (ImageView) convertView.findViewById(R.id.image_praise);
            mViewHolder.mPraise_times = (TextView) convertView.findViewById(R.id.praise_times);
            mViewHolder.mContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.mMy_listview = (MyListView) convertView.findViewById(R.id.my_listview);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final CommentBaseBeand commentBaseBeand = commentInfoList.get(position);
        final String nikename = commentBaseBeand.getNikename();
        final String id = commentBaseBeand.getId();
        final String comment_rid = commentBaseBeand.getComment_rid();
        final String rid = commentBaseBeand.getRid();
        String comment_avtar = commentBaseBeand.getAvatar();
        String comment_time = commentBaseBeand.getComment_time();
        final String people_like = TextUtils.isEmpty(commentBaseBeand.getPeople_like()) ? "0" : commentBaseBeand.getPeople_like();
        String content = commentBaseBeand.getContent();
        mViewHolder.mComment_nikename.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
        mViewHolder.mComment_time.setText(DateFormatUtils.longToDateM(comment_time));
        mViewHolder.mPraise_times.setText(people_like);
        mViewHolder.mContent.setText(TextUtils.isEmpty(content) ? "" : content);
        if (!TextUtils.isEmpty(comment_avtar)) {
            Picasso.with(mContext)
                    .load(comment_avtar)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_header)
                    .placeholder(R.mipmap.default_header)
                    .into(mViewHolder.mComment_avatar);
        }
        List<CommentBaseBeand.ReplyInfoBean> reply_info = commentBaseBeand.getReply_info();
        XWReplyAdapter videoReplyAdapter = new XWReplyAdapter(reply_info, mListener, mContext);
        mViewHolder.mMy_listview.setAdapter(videoReplyAdapter);
        mViewHolder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 2)
                    mListener.callBack(id, rid, nikename);
                else
                    mListener.callBack(id, comment_rid, nikename);
            }
        });
        if (mode == 2) mViewHolder.mImage_praise.setVisibility(View.GONE);
        else mViewHolder.mImage_praise.setVisibility(View.VISIBLE);
        mViewHolder.mImage_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("id", commentBaseBeand.getId());
                mHttp.post(Url.SMS_PRAISE, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        mViewHolder.mImage_praise.setImageResource(R.mipmap.praise);
                        int i = Integer.parseInt(people_like) + 1;
                        mViewHolder.mPraise_times.setText(i + "");
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });
        return convertView;
    }

    class ViewHolder {
        CircleImageView mComment_avatar;
        TextView mComment_nikename;
        TextView mComment_time;
        ImageView mImage_praise;
        TextView mPraise_times;
        TextView mContent;
        MyListView mMy_listview;
    }
}
