package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.activity.home.XWFormDetailActivity;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.entity.PostsSmsBean;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import static com.ruihuo.ixungen.R.id.comment_nikename;

/**
 * @author yudonghui
 * @date 2017/4/13
 * @describe May the Buddha bless bug-free!!!
 */
public class PostsSmsAdapter extends BaseAdapter {
    List<PostsSmsBean.DataBean> postsList;
    PostsSmsListener mListener;
    Context mContext;

    public PostsSmsAdapter(List<PostsSmsBean.DataBean> postsList, Context mContext) {
        this.postsList = postsList;
        this.mContext = mContext;
    }

    public void setListener(PostsSmsListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return postsList.size();
    }

    @Override
    public Object getItem(int position) {
        return postsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_posts_sms, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mComment_avatar = (ImageView) convertView.findViewById(R.id.comment_avatar);
            mViewHolder.mComment_nikename = (TextView) convertView.findViewById(comment_nikename);
            mViewHolder.mComment_time = (TextView) convertView.findViewById(R.id.comment_time);
            mViewHolder.mText_reply = (TextView) convertView.findViewById(R.id.text_reply);
            mViewHolder.mContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.mAvatar = (ImageView) convertView.findViewById(R.id.avatar);
            mViewHolder.mNikename = (TextView) convertView.findViewById(R.id.nikename);
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            mViewHolder.mMy_listview = (MyListView) convertView.findViewById(R.id.my_listview);
            mViewHolder.mMiddle_gray = (LinearLayout) convertView.findViewById(R.id.middle_gray);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final PostsSmsBean.DataBean dataBean = postsList.get(position);
        //最上面，也就是最新评论人的信息
        String new_nikename = dataBean.getReply_nikename();
        mViewHolder.mComment_nikename.setText(TextUtils.isEmpty(new_nikename) ? "" : new_nikename);

        String content = dataBean.getReply_content();
        mViewHolder.mContent.setText(TextUtils.isEmpty(content) ? "" : content);

        String comment_time = dataBean.getReply_time();
        String commentTime = DateFormatUtils.longToDateM(comment_time);
        mViewHolder.mComment_time.setText(commentTime);

        String reply_avatar = dataBean.getReply_avatar();
        if (!TextUtils.isEmpty(reply_avatar)) {
            PicassoUtils.setImgView(reply_avatar, mContext, R.mipmap.default_header, mViewHolder.mComment_avatar);
        }
        mViewHolder.mComment_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                //当前点击的这个人的rid
                intent.putExtra("rid", dataBean.getReply_rid());
                mContext.startActivity(intent);
            }
        });

        //帖子作者的昵称，中间带灰色背景部分
        String nikename = dataBean.getNikename();
        mViewHolder.mNikename.setText(TextUtils.isEmpty(nikename) ? "" : nikename);

        final String title = dataBean.getTitle();
        mViewHolder.mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        //点击回复按钮
        mViewHolder.mText_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callBack(position);
            }
        });
        //中间灰条的头像
        String avatar = dataBean.getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mViewHolder.mAvatar);
        }

        //帖子评价回复模块
        List<PostsSmsBean.DataBean.ReplyInfoBean> replyInfoList = new ArrayList<>();
        PostsSmsBean.DataBean.CommentInfoBean comment_info = dataBean.getComment_info();
        final String topic_id = dataBean.getTopic_id();
        final String type = dataBean.getType();
        PostsSmsBean.DataBean.ReplyInfoBean replyInfoBean = new PostsSmsBean.DataBean.ReplyInfoBean();
        replyInfoBean.setAvatar(comment_info.getAvatar());
        replyInfoBean.setNikename(comment_info.getNikename());
        replyInfoBean.setTo_nikename(comment_info.getTo_nikename());
        replyInfoBean.setReply_content(comment_info.getReply_content());
        replyInfoList.add(replyInfoBean);
        replyInfoList.addAll(dataBean.getReply_info());
        CommentAdapter commentAdapter = new CommentAdapter(replyInfoList, mContext);
        mViewHolder.mMy_listview.setAdapter(commentAdapter);
        mViewHolder.mMiddle_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("glean".equals(type)) {//寻根问祖
                    Intent intent = new Intent(mContext, XWFormDetailActivity.class);
                    intent.putExtra("id", topic_id);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, H5Activity.class);
                    intent.putExtra("url", Url.H5_DETAIL + "/id/" + topic_id + "userId" + XunGenApp.rid + "token" + XunGenApp.token);
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView mComment_avatar;
        TextView mComment_nikename;
        TextView mComment_time;
        TextView mText_reply;
        TextView mContent;
        ImageView mAvatar;
        TextView mNikename;
        TextView mTitle;
        MyListView mMy_listview;
        LinearLayout mMiddle_gray;
    }

    public interface PostsSmsListener {
        void callBack(int position);
    }
}
