package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

/**
 * @author yudonghui
 * @date 2017/9/7
 * @describe May the Buddha bless bug-free!!!
 */
public class DiscussionMemberAdapter extends RecyclerView.Adapter<DiscussionMemberAdapter.ViewHolder> {
    private int resize;
    private Context mContext;
    List<String> memberIdList;
    private CallBackPositionInterface mListener;

    public DiscussionMemberAdapter(Context mContext, List<String> memberIdList, CallBackPositionInterface mListener) {
        this.memberIdList = memberIdList;
        this.mContext = mContext;
        this.mListener = mListener;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int ver = DisplayUtilX.dip2px(70);
        resize = (displayWidth - ver) / 5;
    }

    private boolean isDelete;

    public void isDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(mContext, R.layout.item_discussion_member, null);
        inflate.setOnClickListener(ItemListener);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        String rid = memberIdList.get(position);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resize, resize);
        holder.mAvatar.setLayoutParams(layoutParams);
        if (isDelete) holder.mDelete.setVisibility(View.VISIBLE);
        else holder.mDelete.setVisibility(View.GONE);
        if ("add".equals(rid)) {
            holder.mAvatar.setImageResource(R.mipmap.add_member);
            holder.mDelete.setVisibility(View.GONE);
            holder.mNikeName.setVisibility(View.GONE);
        } else if ("delete".equals(rid)) {
            holder.mAvatar.setImageResource(R.mipmap.delete_member);
            holder.mDelete.setVisibility(View.GONE);
            holder.mNikeName.setVisibility(View.GONE);
        } else {
            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
            if (userInfo != null) {
                Uri portraitUri = userInfo.getPortraitUri();
                String name = userInfo.getName();
                holder.mNikeName.setText(TextUtils.isEmpty(name) ? "" : name);
                Picasso.with(mContext)
                        .load(portraitUri)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.mipmap.default_header)
                        .placeholder(R.mipmap.default_header)
                        .into(holder.mAvatar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return memberIdList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAvatar;
        private ImageView mDelete;
        private TextView mNikeName;

        public ViewHolder(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mDelete = (ImageView) itemView.findViewById(R.id.delete);
            mNikeName = (TextView) itemView.findViewById(R.id.nikename);

        }
    }

    View.OnClickListener ItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            mListener.callBack(position);
        }
    };
}
