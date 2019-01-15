package com.ruihuo.ixungen.common.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.friends.bean.Contact;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.view.CircleImageView;

import java.util.ArrayList;

/**
 * @Author: duke
 * @DateTime: 2016-08-12 15:34
 * @Description:
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    public static final int SHOW_HEADER_VIEW = 1;   //显示header
    public static final int DISMISS_HEADER_VIEW = 2;//隐藏header
    private ArrayList<Contact> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private boolean isFriendForm;
    private String redPointsStatus;
    private Context mContext;

    public ContactAdapter(ArrayList<Contact> list, boolean isFriendForm, Context mContext) {
        this.list = list;
        this.isFriendForm = isFriendForm;
        this.mContext = mContext;
    }

    public void updateData(ArrayList<Contact> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setRedPointsStatus(String redPointsStatus) {
        this.redPointsStatus = redPointsStatus;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        if (list == null || list.size() <= 0)
            return;
        final Contact contact = list.get(position);
        if (isFriendForm && position == 0) {
            if ("1".equals(redPointsStatus)) {
                holder.redPoints.setVisibility(View.VISIBLE);
            } else holder.redPoints.setVisibility(View.GONE);
            holder.imageAvatar.setImageResource(R.mipmap.new_friend);
            holder.tvRid.setVisibility(View.GONE);
            holder.tvName.setGravity(Gravity.CENTER_VERTICAL);
            holder.tvName.setText("新的朋友");
            // Log.e("位置",position+"新的朋友");
        } else if (isFriendForm && position == 1) {
            // Log.e("位置",position+"宗亲会");
            holder.imageAvatar.setImageResource(R.mipmap.agnation_family);
            holder.tvRid.setVisibility(View.GONE);
            holder.tvName.setGravity(Gravity.CENTER_VERTICAL);
            holder.tvName.setText("宗亲会");
        } else if (isFriendForm && position == 2) {
            // Log.e("位置",position+"讨论组");
            holder.imageAvatar.setImageResource(R.mipmap.discussion_logo);
            holder.tvRid.setVisibility(View.GONE);
            holder.tvName.setGravity(Gravity.CENTER_VERTICAL);
            holder.tvName.setText("讨论组");
        } else {
            //  Log.e("位置",position+"其他");
            holder.tvHeader.setText(contact.firstPinYin);
            holder.tvName.setText(contact.name);
            holder.tvName.setGravity(Gravity.BOTTOM);
            holder.tvRid.setText("ID：" + contact.rid);
            PicassoUtils.setImgView(contact.avatar, mContext, R.mipmap.default_header, holder.imageAvatar);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getLayoutPosition(), holder.itemView);
            }
        });
        if (position == 0) {
            holder.tvHeader.setText(contact.firstPinYin);
            holder.tvHeader.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.equals(contact.firstPinYin, list.get(position - 1).firstPinYin)) {
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(contact.firstPinYin);
                holder.itemView.setTag(SHOW_HEADER_VIEW);
            } else {
                holder.tvHeader.setVisibility(View.GONE);
                holder.itemView.setTag(DISMISS_HEADER_VIEW);
            }
        }
        holder.itemView.setContentDescription(contact.firstPinYin);
    }

    @Override
    public int getItemCount() {
        return (list == null || list.size() <= 0) ? 0 : list.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHeader;
        public TextView tvName;
        public CircleImageView imageAvatar;
        public TextView tvRid;
        public TextView redPoints;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_header);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imageAvatar = (CircleImageView) itemView.findViewById(R.id.image_header);
            tvRid = (TextView) itemView.findViewById(R.id.text_rid);
            redPoints = (TextView) itemView.findViewById(R.id.red_point);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}