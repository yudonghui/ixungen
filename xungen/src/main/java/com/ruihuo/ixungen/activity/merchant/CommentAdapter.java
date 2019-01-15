package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.MyGridView;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class CommentAdapter extends BaseAdapter {
    List<CommentBaseBean> commentList;
    private Context mContext;
    private int mode;//1店铺首页显示的时候，这个时候只能最多显示固定条数。并且图片最多显示三张
    //2评价列表  这个时候显示很多条

    public CommentAdapter(List<CommentBaseBean> commentList, Context mContext, int mode) {
        this.commentList = commentList;
        this.mContext = mContext;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        if (mode == 1 && commentList.size() > 3)
            return 3;
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_comment_shops, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mStarView = (StarView) convertView.findViewById(R.id.starView);
            mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
            mViewHolder.mContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.mGridView = (MyGridView) convertView.findViewById(R.id.gridView);
            mViewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
            mViewHolder.mReply = (TextView) convertView.findViewById(R.id.reply);
            convertView.setTag(mViewHolder);
        } else
            mViewHolder = (ViewHolder) convertView.getTag();
        CommentBaseBean commentBean = commentList.get(position);
       final String shop_id = commentBean.getShop_id();
        String comment_avtar = commentBean.getComment_avtar();
        String comment_name = commentBean.getComment_name();
        String comment_time = commentBean.getComment_time();
        String content = commentBean.getContent();
        String score = commentBean.getScore();
        String img = commentBean.getImg();
        String cost = commentBean.getCost();
        String reply_id = commentBean.getReply_id();
        String reply_content = commentBean.getReply_content();

        mViewHolder.mName.setText(TextUtils.isEmpty(comment_name) ? "" : comment_name);
        if (!TextUtils.isEmpty(score)) {
            int i = Integer.parseInt(score);
            mViewHolder.mStarView.setLevel(i * 2 + "");
        }

        mViewHolder.mTime.setText(DateFormatUtils.longToDateM(comment_time));
        mViewHolder.mContent.setText(TextUtils.isEmpty(content) ? "" : content);
        mViewHolder.mPrice.setText("￥" + (TextUtils.isEmpty(cost) ? "" : cost) + "/人");
        if (!TextUtils.isEmpty(reply_id) && !TextUtils.isEmpty(reply_content)) {
            String replyContent = "商家回复: " + reply_content;
            SpannableString spannableString = new SpannableString(replyContent);
            spannableString.setSpan(new ForegroundColorSpan(Color.rgb(0x3b, 0x93, 0xe9)), 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mViewHolder.mReply.setText(spannableString);
        }


        if (!TextUtils.isEmpty(comment_avtar)) {
            PicassoUtils.setImgView(comment_avtar, R.mipmap.default_header, mContext, mViewHolder.mAvatar);
        }
        if (!TextUtils.isEmpty(img)) {
            String[] split = img.split("\\;");
            GridImageAdapter gridImageAdapter = new GridImageAdapter(split, mode, mContext);
            mViewHolder.mGridView.setAdapter(gridImageAdapter);
        }
        if (mode == 1) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopsCommentActivity.class);
                    intent.putExtra("shopId", shop_id);
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private CircleImageView mAvatar;
        private TextView mName;
        private StarView mStarView;
        private TextView mPrice;
        private TextView mContent;
        private MyGridView mGridView;
        private TextView mTime;
        private TextView mReply;
    }
}
