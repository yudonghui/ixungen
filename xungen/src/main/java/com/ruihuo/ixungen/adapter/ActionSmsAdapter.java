package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/13
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionSmsAdapter extends BaseAdapter {
    private Context mContext;
    private int displayWidth;
    List<UserMsgBean.DataBean> data;

    public ActionSmsAdapter(Context mContext, List<UserMsgBean.DataBean> data) {
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
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displayWidth / 4, displayWidth / 4);
        mViewHolder.mSystemPhoto.setLayoutParams(params);
        UserMsgBean.DataBean dataBean = data.get(position);
        mViewHolder.mSystemTitle.setText(TextUtils.isEmpty(dataBean.getTitle()) ? "" : dataBean.getTitle());
        mViewHolder.mSystemContent.setText(TextUtils.isEmpty(dataBean.getContent()) ? "" : dataBean.getContent());
        String create_time = dataBean.getCreate_time();
        mViewHolder.mSystemTime.setText(DateFormatUtils.longToDateM(create_time));
        if (!TextUtils.isEmpty(dataBean.getAvatar())) {
            Picasso.with(mContext)
                    .load(dataBean.getAvatar())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_photo)
                    .placeholder(R.mipmap.default_photo)
                    .resize(displayWidth / 4, displayWidth / 4)
                    .centerCrop()
                    .into(mViewHolder.mSystemPhoto);

        }

        return convertView;
    }

    class ViewHolder {
        ImageView mSystemPhoto;
        TextView mSystemTitle;
        TextView mSystemContent;
        TextView mSystemTime;
    }
}
