package com.ruihuo.ixungen.action;

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
import com.ruihuo.ixungen.geninterface.CallBackBeanInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.MyListView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/6/1
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoCommentAdapter extends BaseAdapter {
    List<VideoDetailBean.DataBean.CommentInfoBean> commentInfoList;
    Context mContext;
    CallBackBeanInterface mListener;

    public VideoCommentAdapter(List<VideoDetailBean.DataBean.CommentInfoBean> commentInfoList, Context mContext) {
        this.commentInfoList = commentInfoList;
        this.mContext = mContext;
    }

    public void setListener(CallBackBeanInterface mListener) {
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
        final VideoDetailBean.DataBean.CommentInfoBean commentInfoBean = commentInfoList.get(position);
        final String comment_name = commentInfoBean.getComment_name();
        String comment_avtar = commentInfoBean.getComment_avtar();
        String comment_time = commentInfoBean.getComment_time();
        final String id = commentInfoBean.getId();
        final String comment_rid = commentInfoBean.getComment_rid();
        final String people_like = TextUtils.isEmpty(commentInfoBean.getPeople_like()) ? "0" : commentInfoBean.getPeople_like();
        String content = commentInfoBean.getContent();
        mViewHolder.mComment_nikename.setText(TextUtils.isEmpty(comment_name) ? "" : comment_name);
        mViewHolder.mComment_time.setText(DateFormatUtils.longToDateM(comment_time));
        mViewHolder.mPraise_times.setText(people_like);
        mViewHolder.mContent.setText(TextUtils.isEmpty(comment_name) ? "" : content);
        if (!TextUtils.isEmpty(comment_avtar)) {
            Picasso.with(mContext)
                    .load(comment_avtar)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_header)
                    .placeholder(R.mipmap.default_header)
                    .into(mViewHolder.mComment_avatar);
        }
        List<VideoDetailBean.DataBean.CommentInfoBean.ReplyInfoBean> reply_info = commentInfoBean.getReply_info();
        VideoReplyAdapter videoReplyAdapter = new VideoReplyAdapter(reply_info, mListener);
        mViewHolder.mMy_listview.setAdapter(videoReplyAdapter);
        mViewHolder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callBack(id, comment_rid, comment_name);
            }
        });
        mViewHolder.mImage_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("id", commentInfoBean.getId());
                mHttp.post(Url.PRAISE_COMMENT, params, new JsonInterface() {
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
