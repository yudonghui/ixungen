package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.OrderInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/16
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderFormNAdapter extends BaseAdapter {
    List<OrderFormBean.DataBean> dataList;
    private Context mContext;
    private int type;
    private int redTxt;
    private int deepTxt;
    private int resize;
    private OrderInterface mListener;

    public OrderFormNAdapter(List<OrderFormBean.DataBean> dataList, int type, Context mContext, OrderInterface mListener) {
        this.dataList = dataList;
        this.type = type;
        this.mContext = mContext;
        this.mListener = mListener;
        redTxt = mContext.getResources().getColor(R.color.red_txt);
        deepTxt = mContext.getResources().getColor(R.color.deep_txt);
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = (int) (displayWidth / 3.5);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_orderform_n, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mLl_title = (LinearLayout) convertView.findViewById(R.id.ll_title);
            mViewHolder.mLogo = (CircleImageView) convertView.findViewById(R.id.logo);
            mViewHolder.mShopsName = (TextView) convertView.findViewById(R.id.shopsName);
            mViewHolder.mBuyStatus = (TextView) convertView.findViewById(R.id.buyStatus);
            mViewHolder.mCover = (ImageView) convertView.findViewById(R.id.cover);
            mViewHolder.mGoodsName = (TextView) convertView.findViewById(R.id.goodsName);
            mViewHolder.mService = (TextView) convertView.findViewById(R.id.service);
            mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
            mViewHolder.mBuyNum = (TextView) convertView.findViewById(R.id.buyNum);
            mViewHolder.mMoney = (TextView) convertView.findViewById(R.id.money);
            mViewHolder.mOne = (TextView) convertView.findViewById(R.id.one);
            mViewHolder.mTwo = (TextView) convertView.findViewById(R.id.two);
            mViewHolder.mLl_detail = (LinearLayout) convertView.findViewById(R.id.ll_detail);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderFormBean.DataBean dataBean = dataList.get(position);
        final String status = dataBean.getStatus();//0-未支付；1-支付完成未消费；2-消费完成；3-退款中；4退款完成
        switch (status) {
            case "0"://0-未支付
                String deadline = dataBean.getDeadline();
                if (TextUtils.isEmpty(deadline)) break;
                long endTime = Long.parseLong(deadline);
                long curTime = System.currentTimeMillis() / 1000;
                if (endTime > curTime) {//结束时间大，说明还可以支付。没有失效
                    mViewHolder.mOne.setVisibility(View.VISIBLE);
                    mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                    mViewHolder.mOne.setTextColor(deepTxt);
                    mViewHolder.mOne.setText("取消订单");

                    mViewHolder.mTwo.setVisibility(View.VISIBLE);
                    mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                    mViewHolder.mTwo.setTextColor(redTxt);
                    mViewHolder.mTwo.setText("立即支付");

                    mViewHolder.mBuyStatus.setText("等待付款");
                } else {
                    mViewHolder.mOne.setVisibility(View.VISIBLE);
                    mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                    mViewHolder.mOne.setTextColor(deepTxt);
                    mViewHolder.mOne.setText("取消订单");
                    mViewHolder.mTwo.setVisibility(View.GONE);
                    mViewHolder.mBuyStatus.setText("订单已失效");
                }

                break;
            case "1"://支付完成未消费
                mViewHolder.mOne.setVisibility(View.GONE);

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("立即使用");

                mViewHolder.mBuyStatus.setText("未使用");
                break;
            case "2"://消费完成
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");
                mViewHolder.mTwo.setVisibility(View.GONE);
                mViewHolder.mBuyStatus.setText("消费完成");
                break;

            case "3"://退款中
                mViewHolder.mOne.setVisibility(View.GONE);


                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("取消退款");

                mViewHolder.mBuyStatus.setText("等待商家退款");
                break;
            case "4"://退款完成
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");
                mViewHolder.mTwo.setVisibility(View.GONE);

                mViewHolder.mBuyStatus.setText("商家已退款");
                break;
            case "5"://商家拒单
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("投诉建议");

                mViewHolder.mBuyStatus.setText("商家已拒单");
                break;
            case "6"://商家拒绝退款
                mViewHolder.mOne.setVisibility(View.GONE);

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("立即使用");

                mViewHolder.mBuyStatus.setText("商家已拒绝退款");
                break;

            case "8"://评价完成，等待商家回复
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");
                mViewHolder.mTwo.setVisibility(View.GONE);
                mViewHolder.mBuyStatus.setText("等待商家回复");
                break;
            case "7"://待评价
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("立即评价");

                mViewHolder.mBuyStatus.setText("消费完成");
                break;
        }
        final String type = dataBean.getType();
        final String shopId = dataBean.getShop_id();
        String logo = dataBean.getLogo();
        String shop_name = dataBean.getShop_name();
        String cover = dataBean.getCover();
        String goods_name = dataBean.getGoods_name();
        String is_reserve = dataBean.getIs_reserve();//1-可以预定；2-不可以预定
        String is_cancel = dataBean.getIs_cancel();
        String consume_start_time = dataBean.getConsume_start_time();
        String consume_end_time = dataBean.getConsume_end_time();
        String price = dataBean.getPrice();//单价
        String amount = dataBean.getAmount();//数量
        String cost = dataBean.getCost();//商品总价
        final String order_no = dataBean.getOrder_no();//订单号

        mViewHolder.mShopsName.setText(TextUtils.isEmpty(shop_name) ? "--" : shop_name);
        mViewHolder.mGoodsName.setText(TextUtils.isEmpty(goods_name) ? "--" : goods_name);
        mViewHolder.mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
        if (!TextUtils.isEmpty(amount)) {
            int v = (int) (Float.parseFloat(amount));
            mViewHolder.mBuyNum.setText("数量: " + v);
        }

        if ("0".equals(status)) mViewHolder.mMoney.setTextColor(deepTxt);
        else mViewHolder.mMoney.setTextColor(redTxt);
        mViewHolder.mMoney.setText("￥" + (TextUtils.isEmpty(cost) ? "--" : cost));
        String remark = "--";
        if ("1".equals(type)) {//餐厅
            remark = "1".equals(is_cancel) ? "可取消" : "不可取消";
        } else {
            if (!TextUtils.isEmpty(consume_end_time) && !TextUtils.isEmpty(consume_start_time)) {
                long in = DateFormatUtils.StringToLong(consume_start_time);
                long out = DateFormatUtils.StringToLong(consume_end_time);
                int days = (int) ((out - in) / 86400000);
                String start = DateFormatUtils.longToDate(in);
                String end = DateFormatUtils.longToDate(out);
                remark = start + "至" + end + "  共" + days + "天";
            }
        }
        mViewHolder.mService.setText(remark);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 3 / 4);
        mViewHolder.mCover.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(cover)) {
            String[] split = cover.split("\\;");
            Picasso.with(mContext)
                    .load(split[0])
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.default_long)
                    .error(R.mipmap.default_long)
                    .into(mViewHolder.mCover);
        }
        if (!TextUtils.isEmpty(logo)) {
            Picasso.with(mContext)
                    .load(logo)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.default_header)
                    .error(R.mipmap.default_header)
                    .into(mViewHolder.mLogo);
        }
        mViewHolder.mLl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("type", Integer.parseInt(type));
                mContext.startActivity(intent);
            }
        });
        mViewHolder.mLl_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderNo", order_no);
                mContext.startActivity(intent);
            }
        });
        mViewHolder.mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                String s = textView.getText().toString();
                if (TextUtils.isEmpty(s)) return;
                dataBean.setText(s);
                mListener.callBack(dataBean);
                //Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        });
        mViewHolder.mTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                String s = textView.getText().toString();
                if (TextUtils.isEmpty(s)) return;
                dataBean.setText(s);
                mListener.callBack(dataBean);

                //  Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    class ViewHolder {
        private LinearLayout mLl_title;
        private CircleImageView mLogo;
        private TextView mShopsName;
        private TextView mBuyStatus;
        private ImageView mCover;
        private TextView mGoodsName;
        private TextView mService;
        private TextView mPrice;
        private TextView mBuyNum;
        private TextView mMoney;
        private TextView mOne;
        private TextView mTwo;
        private LinearLayout mLl_detail;
    }
}
