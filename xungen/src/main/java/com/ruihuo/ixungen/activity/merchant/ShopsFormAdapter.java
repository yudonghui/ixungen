package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
 * @date 2017/8/3
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopsFormAdapter extends RecyclerView.Adapter<ShopsFormAdapter.ViewHolder> {
    List<GoodsFormBaseBean> dataList;
    private Context mContext;
    private final int verspace;
    private final int left;
    private int resize;
    private int type;

    public ShopsFormAdapter(List<GoodsFormBaseBean> dataList, Context mContext, int type) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.type = type;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth * 2 / 7;
        verspace = DisplayUtilX.dip2px(6);
        left = DisplayUtilX.dip2px(10);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shops_form, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(dataList.get(position));
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mShopsCover;
        private TextView mRoomType;
        private TextView mRemark;
        private TextView mRoomPrice;
        private TextView mDelete;
        private LinearLayout mLl;

        public ViewHolder(View itemView) {
            super(itemView);
            mShopsCover = (ImageView) itemView.findViewById(R.id.shopsCover);
            mRoomType = (TextView) itemView.findViewById(R.id.roomType);
            mRemark = (TextView) itemView.findViewById(R.id.remark);
            mRoomPrice = (TextView) itemView.findViewById(R.id.roomPrice);
            mDelete = (TextView) itemView.findViewById(R.id.delete);
            mLl = (LinearLayout) itemView.findViewById(R.id.ll);
            if (type == 1 || type == 4) mRemark.setVisibility(View.GONE);
            else mRemark.setVisibility(View.VISIBLE);
        }

        public void setData(GoodsFormBaseBean dataBean) {
            String name = dataBean.getName();
            String service = dataBean.getService();//房间的配置
            String price = dataBean.getPrice();//房间价格
            String cover = dataBean.getCover();

            mRoomType.setText(TextUtils.isEmpty(name) ? "--" : name);
            mRemark.setText(TextUtils.isEmpty(service) ? "--" : service);
            mRoomPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 11 / 15);
            layoutParams.topMargin = verspace;
            layoutParams.bottomMargin = verspace;
            layoutParams.leftMargin = left;
            mShopsCover.setLayoutParams(layoutParams);
            if (!TextUtils.isEmpty(cover)) {
                String[] split = cover.split("\\;");
                Picasso.with(mContext)
                        .load(split[0])
                        .centerCrop()
                        .resize(resize, resize * 11 / 15)
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.default_long)
                        .error(R.mipmap.default_long)
                        .into(mShopsCover);
            }
        }
    }
}
