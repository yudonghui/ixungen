package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.friends.bean.Contact;
import com.ruihuo.ixungen.utils.PicassoUtils;

import java.util.ArrayList;

/**
 * @author yudonghui
 * @date 2017/9/6
 * @describe May the Buddha bless bug-free!!!
 */
public class FriendFormsAdapter extends RecyclerView.Adapter<FriendFormsAdapter.ContactViewHolder> {
    public static final int SHOW_HEADER_VIEW = 1;   //显示header
    public static final int DISMISS_HEADER_VIEW = 2;//隐藏header
    private ArrayList<Contact> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context mContext;

    public FriendFormsAdapter(ArrayList<Contact> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    public void updateData(ArrayList<Contact> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private boolean isDelete;

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
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

        holder.tvHeader.setText(contact.firstPinYin);
        holder.tvName.setText(contact.name);
        holder.tvName.setGravity(Gravity.BOTTOM);
        holder.tvRid.setText("ID：" + contact.rid);
        PicassoUtils.setImgView(contact.avatar, mContext, R.mipmap.default_header, holder.imageAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getLayoutPosition(), holder.itemView);
            }
        });
        if (isDelete) holder.mIsDelete.setVisibility(View.VISIBLE);
        else holder.mIsDelete.setVisibility(View.GONE);
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

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHeader;
        public TextView tvName;
        public ImageView imageAvatar;
        public TextView tvRid;
        public TextView redPoints;
        public ImageView mIsDelete;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_header);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imageAvatar = (ImageView) itemView.findViewById(R.id.image_header);
            tvRid = (TextView) itemView.findViewById(R.id.text_rid);
            redPoints = (TextView) itemView.findViewById(R.id.red_point);
            mIsDelete = (ImageView) itemView.findViewById(R.id.isSelect);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
