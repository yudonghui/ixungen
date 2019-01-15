package com.ruihuo.ixungen.activity.merchant;

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
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/8
 * @describe May the Buddha bless bug-free!!!
 */
public class GoodsFormAdapter extends BaseAdapter {
    List<ShopsFormBean.DataBean> dataList;
    private Context mContext;
    private final int verspace;
    private final int left;
    private int resize;
    private CallBackPositionInterface mListener;

    public GoodsFormAdapter(List<ShopsFormBean.DataBean> dataList, Context mContext, CallBackPositionInterface mListener) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.mListener = mListener;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth * 2 / 7;
        verspace = DisplayUtilX.dip2px(6);
        left = DisplayUtilX.dip2px(10);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.shops_goods_form, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mShopsCover = (ImageView) convertView.findViewById(R.id.shopsCover);
            mViewHolder.mClassify = (TextView) convertView.findViewById(R.id.classify);
            mViewHolder.mService = (TextView) convertView.findViewById(R.id.service);
            mViewHolder.mIsCancel = (TextView) convertView.findViewById(R.id.isCancel);
            mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
            mViewHolder.mReserve = (TextView) convertView.findViewById(R.id.reserve);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        ShopsFormBean.DataBean dataBean = dataList.get(position);
        String type = dataBean.getType();
        String name = dataBean.getName();
        String service = dataBean.getService();
        String is_reserve = dataBean.getIs_reserve();
        String is_cancel = dataBean.getIs_cancel();
        String cover = dataBean.getCover();
        String price = dataBean.getPrice();
        String amount = dataBean.getAmount();
        String sales_volume = dataBean.getSales_volume();
        mViewHolder.mClassify.setText(TextUtils.isEmpty(name) ? "" : name);
        if ("2".equals(type)) {//酒店
            mViewHolder.mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price) + "/晚");
            mViewHolder.mService.setText("剩余" + amount + "  " + (TextUtils.isEmpty(service) ? "" : service));
            if ("1".equals(is_cancel)) mViewHolder.mIsCancel.setText("可取消");
            else mViewHolder.mIsCancel.setText("不可取消");
            mViewHolder.mIsCancel.setVisibility(View.VISIBLE);
            if ("1".equals(is_reserve)) {
                mViewHolder.mReserve.setText("立即预定");
                mViewHolder.mReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.callBack(position);
                    }
                });
            } else {
                mViewHolder.mReserve.setText("不可预定");
            }
        } else {//旅游
            mViewHolder.mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            mViewHolder.mService.setText("已售" + sales_volume + "  " + (TextUtils.isEmpty(service) ? "" : service));
            mViewHolder.mIsCancel.setVisibility(View.GONE);
            mViewHolder.mReserve.setText("立即预定");
            mViewHolder.mReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.callBack(position);
                }
            });
        }


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 11 / 15);
        layoutParams.topMargin = verspace;
        layoutParams.bottomMargin = verspace;
        layoutParams.leftMargin = left;
        mViewHolder.mShopsCover.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(cover)) {
            String[] split = cover.split("\\;");
            Picasso.with(mContext)
                    .load(split[0])
                    .centerCrop()
                    .resize(resize, resize * 11 / 15)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.default_long)
                    .error(R.mipmap.default_long)
                    .into(mViewHolder.mShopsCover);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView mShopsCover;
        private TextView mClassify;
        private TextView mService;
        private TextView mIsCancel;
        private TextView mPrice;
        private TextView mReserve;
    }
}
