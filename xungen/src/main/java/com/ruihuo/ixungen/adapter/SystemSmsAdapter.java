package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.UserMsgBean;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/19
 * @describe May the Buddha bless bug-free!!!
 */
public class SystemSmsAdapter extends BaseAdapter {
    List<UserMsgBean.DataBean> data;
    private Context mContext;
    private int displayWidth;

    public SystemSmsAdapter(Context mContext, List<UserMsgBean.DataBean> data) {
        this.mContext = mContext;
        this.data = data;
        displayWidth = DisplayUtilX.getDisplayWidth();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_system_sms, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mSystemPhoto = (ImageView) convertView.findViewById(R.id.system_photo);
            mViewHolder.mSystemTitle = (TextView) convertView.findViewById(R.id.system_title);
            mViewHolder.mSystemContent = (TextView) convertView.findViewById(R.id.system_content);
            mViewHolder.mSystemTime = (TextView) convertView.findViewById(R.id.system_time);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (SystemSmsAdapter.ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displayWidth / 4, displayWidth / 4);
        mViewHolder.mSystemPhoto.setLayoutParams(params);
        UserMsgBean.DataBean dataBean = data.get(position);
        String relate_table = dataBean.getRelate_table();
        switch (relate_table) {
            case "tour_order"://订单消息
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.order_sms);
                break;
            case "tour_comment"://订单评论
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.order_sms);
                break;
            case "tour_reply"://订单回复
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.order_sms);
                break;
            case "user_friend"://好友消息
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.friend_sms);
                break;
            case "cms_activity"://活动消息
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.action_sms1);
                break;
            case "user_association"://邀请加入宗亲会消息
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.agnation_sms);
                break;
            case "clan_glean":
                mViewHolder.mSystemPhoto.setImageResource(R.mipmap.clan_glean);
                break;
        }
        mViewHolder.mSystemTitle.setText(TextUtils.isEmpty(dataBean.getTitle()) ? "" : dataBean.getTitle());
        mViewHolder.mSystemContent.setText(TextUtils.isEmpty(dataBean.getContent()) ? "" : dataBean.getContent());
        String create_time = dataBean.getCreate_time();
        mViewHolder.mSystemTime.setText(DateFormatUtils.longToDateM(create_time));
       /* if (!TextUtils.isEmpty(dataBean.getAvatar())) {
            Picasso.with(mContext)
                    .load(dataBean.getAvatar())
                    .resize(displayWidth / 4, displayWidth / 4)
                    .centerCrop()
                    .placeholder(R.mipmap.default_photo)
                    .error(R.mipmap.default_photo)
                    .into(mViewHolder.mSystemPhoto);
        }*/

        return convertView;
    }

    class ViewHolder {
        ImageView mSystemPhoto;
        TextView mSystemTitle;
        TextView mSystemContent;
        TextView mSystemTime;
    }
}
