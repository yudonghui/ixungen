package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

/**
 * @author yudonghui
 * @date 2017/9/6
 * @describe May the Buddha bless bug-free!!!
 */
public class IVRecyclerAdapter extends RecyclerView.Adapter<IVRecyclerAdapter.ViewHolder> {
    private int resize;
    private Context mContext;
    List<String> memberIdList;
    private CallBackPositionInterface mListener;

    public IVRecyclerAdapter(Context mContext, List<String> memberIdList, CallBackPositionInterface mListener) {
        this.memberIdList = memberIdList;
        this.mContext = mContext;
        this.mListener = mListener;
        resize = DisplayUtilX.dip2px(40);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.item_image, null);
        inflate.setOnClickListener(ItemListener);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        String rid = memberIdList.get(position);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize);
        holder.mImageView.setLayoutParams(layoutParams);

        if ("add".equals(rid)) {
            holder.mImageView.setImageResource(R.mipmap.add_member);
        } else if ("delete".equals(rid)) {
            holder.mImageView.setImageResource(R.mipmap.delete_member);
        } else {
            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
            if (userInfo != null) {
                Uri portraitUri = userInfo.getPortraitUri();
                Picasso.with(mContext)
                        .load(portraitUri)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.mipmap.default_header)
                        .placeholder(R.mipmap.default_header)
                        .into(holder.mImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return memberIdList == null ? 0 : memberIdList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
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
