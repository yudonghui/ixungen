package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/23
 * @describe May the Buddha bless bug-free!!!
 */
public class ScenicFormAdapter extends RecyclerView.Adapter<ScenicFormAdapter.ViewHolder> {
    // List<GoodsFormBaseBean> dataList;
    private Context mContext;
    private final int verspace;
    private final int left;
    private int resize;
    List<TravelBean.DataBean.PopularScenicSpotBean> middleList;

    public ScenicFormAdapter(Context mContext, List<TravelBean.DataBean.PopularScenicSpotBean> middleList) {
        this.middleList = middleList;
        this.mContext = mContext;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth * 1 / 3;
        verspace = DisplayUtilX.dip2px(5);
        left = DisplayUtilX.dip2px(8);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.item_scenic_recyclerview, null);
        inflate.setOnClickListener(ItemListener);
        ViewHolder viewHolder = new ViewHolder(inflate);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        TravelBean.DataBean.PopularScenicSpotBean dataBean = middleList.get(position);
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return middleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCover;
        private TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mCover = (ImageView) itemView.findViewById(R.id.cover);
            mName = (TextView) itemView.findViewById(R.id.name);
        }

        public void setData(TravelBean.DataBean.PopularScenicSpotBean dataBean) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 2 / 3);
            layoutParams.topMargin = verspace;
            layoutParams.bottomMargin = verspace;
            layoutParams.leftMargin = left;
            mCover.setLayoutParams(layoutParams);
            String shop_name = dataBean.getShop_name();
            String cover = dataBean.getShop_card_a();
            mName.setText(TextUtils.isEmpty(shop_name) ? "" : shop_name);
            if (!TextUtils.isEmpty(cover)) {
                Picasso.with(mContext)
                        .load(cover)
                        .centerCrop()
                        .resize(resize, resize * 2 / 3)
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.default_long)
                        .error(R.mipmap.default_long)
                        .into(mCover);
            }
        }
    }

    View.OnClickListener ItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TravelBean.DataBean.PopularScenicSpotBean goodsFormBaseBean = middleList.get(position);
            String shopId = goodsFormBaseBean.getId();
            String type = goodsFormBaseBean.getType();
            if (TextUtils.isEmpty(shopId) || TextUtils.isEmpty(type)) return;
            Intent intent = new Intent(mContext, HotelActivity.class);
            intent.putExtra("shopId", shopId);
            intent.putExtra("type", Integer.parseInt(type));
            mContext.startActivity(intent);
        }
    };
}
