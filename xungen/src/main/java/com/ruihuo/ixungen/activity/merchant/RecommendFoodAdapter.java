package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.view.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class RecommendFoodAdapter extends BaseAdapter {

    List<ShopsFormBean.DataBean> dataList;
    private Context mContext;
    private final int verspace;
    private final int left;
    private int resize;

    public RecommendFoodAdapter(List<ShopsFormBean.DataBean> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_recommend_food, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mImageView = (XCRoundRectImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mGoods_name = (TextView) convertView.findViewById(R.id.goods_name);
            mViewHolder.mCurPrice = (TextView) convertView.findViewById(R.id.curPrice);
            mViewHolder.mBeforePrice = (TextView) convertView.findViewById(R.id.beforePrice);
            mViewHolder.mBuyNum = (TextView) convertView.findViewById(R.id.buyNum);
            mViewHolder.mBeforePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        ShopsFormBean.DataBean dataBean = dataList.get(position);
        String name = dataBean.getName();
        String cover = dataBean.getCover();
        String price = dataBean.getPrice();
        String sales_volume = dataBean.getSales_volume();//已售
        String amount = dataBean.getAmount();//剩余
        String original_price = dataBean.getOriginal_price();
        mViewHolder.mGoods_name.setText(TextUtils.isEmpty(name) ? "--" : name);
        mViewHolder.mBeforePrice.setText("￥" + (TextUtils.isEmpty(original_price) ? "--" : original_price));
        mViewHolder.mCurPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
        mViewHolder.mBeforePrice.setText("￥" + (TextUtils.isEmpty(original_price) ? "--" : original_price));
        mViewHolder.mBuyNum.setText("已售" + sales_volume + " 剩余" + amount);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 11 / 15);
        layoutParams.topMargin = verspace;
        layoutParams.bottomMargin = verspace;
        layoutParams.leftMargin = left;
        mViewHolder.mImageView.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(cover)) {
            String[] split = cover.split("\\;");
            Picasso.with(mContext)
                    .load(split[0])
                    .centerCrop()
                    .resize(resize, resize * 11 / 15)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.default_long)
                    .error(R.mipmap.default_long)
                    .into(mViewHolder.mImageView);
        }
        return convertView;
    }

    class ViewHolder {
        private XCRoundRectImageView mImageView;
        private TextView mGoods_name;
        private TextView mCurPrice;
        private TextView mBeforePrice;
        private TextView mBuyNum;
    }
}
