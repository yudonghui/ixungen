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
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class NearbyShopAdatper extends BaseAdapter {
    List<NearbyShopBaseBean> nearbyShopList;
    private Context mContext;
    private int mode;
    private final int verspace;
    private final int left;
    private int resize;

    public NearbyShopAdatper(List<NearbyShopBaseBean> nearbyShopList, Context mContext, int mode) {
        this.nearbyShopList = nearbyShopList;
        this.mContext = mContext;
        this.mode = mode;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth * 2 / 7;
        verspace = DisplayUtilX.dip2px(6);
        left = DisplayUtilX.dip2px(10);

    }

    @Override
    public int getCount() {
        if (mode == 1 && nearbyShopList.size() > 3) return 3;
        return nearbyShopList.size();
    }

    @Override
    public Object getItem(int position) {
        return nearbyShopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_nearby_shop, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mShopsCover = (ImageView) convertView.findViewById(R.id.shopsCover);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mStarView = (StarView) convertView.findViewById(R.id.starView);
            mViewHolder.mCommentNum = (TextView) convertView.findViewById(R.id.commentNum);
            mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
            mViewHolder.mQi = (TextView) convertView.findViewById(R.id.qi);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 11 / 15);
        layoutParams.topMargin = verspace;
        layoutParams.bottomMargin = verspace;
        layoutParams.leftMargin = left;
        mViewHolder.mShopsCover.setLayoutParams(layoutParams);

        NearbyShopBaseBean nearbyShopBaseBean = nearbyShopList.get(position);
        String consumption_per_person = nearbyShopBaseBean.getConsumption_per_person();
        String level = nearbyShopBaseBean.getLevel();
        String shop_card_a = nearbyShopBaseBean.getShop_card_a();
        String shop_name = nearbyShopBaseBean.getShop_name();
        String total_comment = nearbyShopBaseBean.getTotal_comment();
        mViewHolder.mName.setText(TextUtils.isEmpty(shop_name) ? "--" : shop_name);
        mViewHolder.mPrice.setText("￥" +
                (TextUtils.isEmpty(consumption_per_person) ? "--" : consumption_per_person));
        mViewHolder.mQi.setText("起");
        mViewHolder.mStarView.setLevel(level);
        mViewHolder.mCommentNum.setText(TextUtils.isEmpty(total_comment) ? "0" : total_comment + "条评价");
        if (!TextUtils.isEmpty(shop_card_a)) {
            Picasso.with(mContext)
                    .load(shop_card_a)
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
        private TextView mName;
        private StarView mStarView;
        private TextView mCommentNum;
        private TextView mPrice;
        private TextView mQi;
    }
}
