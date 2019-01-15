package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.OrderInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.view.MyGridView;

import java.util.List;

import static com.ruihuo.ixungen.R.id.orderNo;

/**
 * @author yudonghui
 * @date 2017/8/15
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderFormSAdapter extends BaseAdapter {
    List<OrderFormBean.DataBean> dataList;
    private Context mContext;
    private int type;
    private int redTxt;
    private int deepTxt;
    private int resize;
    private OrderInterface mListener;

    public OrderFormSAdapter(List<OrderFormBean.DataBean> dataList, int type, Context mContext, OrderInterface mListener) {
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
            convertView = View.inflate(mContext, R.layout.item_orderform_s, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mOrderNo = (TextView) convertView.findViewById(orderNo);
            mViewHolder.mBuyStatus = (TextView) convertView.findViewById(R.id.buyStatus);
            mViewHolder.mGoodsName = (TextView) convertView.findViewById(R.id.goodsName);
            mViewHolder.mConsumeTime = (TextView) convertView.findViewById(R.id.consumeTime);
            mViewHolder.mConsumeUsername = (TextView) convertView.findViewById(R.id.consumeUsername);
            mViewHolder.mPhone = (TextView) convertView.findViewById(R.id.phone);
            mViewHolder.mTelephone = (ImageView) convertView.findViewById(R.id.telephone);
            mViewHolder.mQuitorrecommend = (TextView) convertView.findViewById(R.id.quitorrecommend);
            mViewHolder.mQuitMoney = (TextView) convertView.findViewById(R.id.quitMoney);
            //  mViewHolder.mQuitRemark = (TextView) convertView.findViewById(R.id.quitRemark);
            mViewHolder.mGridView = (MyGridView) convertView.findViewById(R.id.gridView);
            mViewHolder.mBuyNum = (TextView) convertView.findViewById(R.id.buyNum);
            mViewHolder.mMoney = (TextView) convertView.findViewById(R.id.money);
            mViewHolder.mOrderRemark = (TextView) convertView.findViewById(R.id.orderRemark);
            mViewHolder.mOne = (TextView) convertView.findViewById(R.id.one);
            mViewHolder.mTwo = (TextView) convertView.findViewById(R.id.two);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderFormBean.DataBean dataBean = dataList.get(position);
        final String order_no = dataBean.getOrder_no();
        String type = dataBean.getType();
        //  0-未支付；1-支付完成未消费；2-消费完成；3-退款中；4-退款成功；5-商家拒单；6-商家拒绝退款
        final String status = dataBean.getStatus();
        String goods_name = dataBean.getGoods_name();
        String user_name = dataBean.getUser_name();
        String consume_start_time = dataBean.getConsume_start_time();
        String consume_end_time = dataBean.getConsume_end_time();
        final String phone = dataBean.getPhone();
        String amount = dataBean.getAmount();
        String content = dataBean.getContent();
        String img = dataBean.getImg();
        final String info = dataBean.getInfo();//订单备注
        String cost = dataBean.getCost();
        String refund_reason = dataBean.getRefund_reason();//退款原因

        mViewHolder.mOrderNo.setText("订单号: " + (TextUtils.isEmpty(order_no) ? "" : order_no));
        mViewHolder.mGoodsName.setText(TextUtils.isEmpty(goods_name) ? "" : goods_name);
        if (!TextUtils.isEmpty(amount)) {
            int v = (int) (Float.parseFloat(amount));
            mViewHolder.mBuyNum.setText("数量: " + v);
        }

        if ("0".equals(status)) mViewHolder.mMoney.setTextColor(deepTxt);
        else mViewHolder.mMoney.setTextColor(redTxt);
        mViewHolder.mMoney.setText("￥" + (TextUtils.isEmpty(cost) ? "--" : cost));

        if ("2".equals(type)) {//酒店   酒店有入住时间，餐厅没有消费时间

            if (!TextUtils.isEmpty(consume_end_time) && !TextUtils.isEmpty(consume_start_time)) {
                long in = DateFormatUtils.StringToLong(consume_start_time);
                long out = DateFormatUtils.StringToLong(consume_end_time);
                int days = (int) ((out - in) / 86400000);
                String start = DateFormatUtils.longToDate(in);
                String end = DateFormatUtils.longToDate(out);
                mViewHolder.mConsumeTime.setVisibility(View.VISIBLE);
                mViewHolder.mConsumeTime.setText("入住: " + start +
                        " 离店: " + end + "  共" + days + "晚");
            }
            mViewHolder.mConsumeUsername.setText("入住人: " + (TextUtils.isEmpty(user_name) ? "" : user_name));

        } else {
            mViewHolder.mConsumeUsername.setText("消费者: " + (TextUtils.isEmpty(user_name) ? "" : user_name));
            mViewHolder.mConsumeTime.setVisibility(View.GONE);
        }
        mViewHolder.mPhone.setText("联系电话: " + (TextUtils.isEmpty(phone) ? "" : phone));

        if (!TextUtils.isEmpty(info)) {//订单备注
            mViewHolder.mOrderRemark.setVisibility(View.VISIBLE);
            mViewHolder.mOrderRemark.setText("订单备注: " + info);
        } else {
            mViewHolder.mOrderRemark.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(refund_reason)) {//退款原因
            String quitMoney = "退款金额: ￥" + cost;
            SpannableString spannableString = new SpannableString(quitMoney);
            spannableString.setSpan(new ForegroundColorSpan(Color.rgb(0x00, 0x00, 0x00)), 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mViewHolder.mQuitorrecommend.setVisibility(View.VISIBLE);
            mViewHolder.mQuitMoney.setVisibility(View.VISIBLE);
            mViewHolder.mQuitMoney.setText(spannableString);
            mViewHolder.mQuitorrecommend.setText("退款原因: " + (TextUtils.isEmpty(refund_reason) ? "--" : refund_reason));
        } else {
            mViewHolder.mQuitorrecommend.setVisibility(View.GONE);
            mViewHolder.mQuitMoney.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(img)) {
            mViewHolder.mGridView.setVisibility(View.VISIBLE);
            String[] split = img.split("\\;");
            GridImageAdapter gridImageAdapter = new GridImageAdapter(split, 1, mContext);
            mViewHolder.mGridView.setAdapter(gridImageAdapter);
            mViewHolder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, UserRecommendDetailActivity.class);
                    intent.putExtra("orderNo", order_no);
                    mContext.startActivity(intent);
                }
            });
        } else mViewHolder.mGridView.setVisibility(View.GONE);

       /* if (!TextUtils.isEmpty(content) && "2".equals(status)) {//消费完成并且用户已经评论

        } else {*/
        //  mViewHolder.mQuitorrecommend.setVisibility(View.GONE);
        switch (status) {
            case "0"://0-未支付
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("拒单");

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("修改价钱");

                mViewHolder.mBuyStatus.setText("等待用户付款");

                break;
            case "1"://支付完成未消费
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("拒单");

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("已使用");

                mViewHolder.mBuyStatus.setText("买家已付款");
                break;

            case "2"://消费完成，但是用户没有评论
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");

                mViewHolder.mTwo.setVisibility(View.GONE);

                mViewHolder.mBuyStatus.setText("已完成");
                break;
            case "3"://退款中
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mOne.setTextColor(redTxt);
                mViewHolder.mOne.setText("拒绝退款");

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("同意退款");

                mViewHolder.mBuyStatus.setText("买家申请退款");
                break;
            case "4"://退款完成
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");
                mViewHolder.mTwo.setVisibility(View.GONE);

                mViewHolder.mBuyStatus.setText("已退款");
                break;
            case "5"://商家拒单
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");
                mViewHolder.mTwo.setVisibility(View.GONE);

                mViewHolder.mBuyStatus.setText("已拒单");
                break;
            case "6":
                mViewHolder.mOne.setVisibility(View.GONE);
                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("已使用");

                mViewHolder.mBuyStatus.setText("已拒绝退款");
                break;
            case "7":// 7待评价
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");

                mViewHolder.mTwo.setVisibility(View.GONE);

                mViewHolder.mBuyStatus.setText("待评价");
                break;
            case "8"://8 待回复
                mViewHolder.mOne.setVisibility(View.VISIBLE);
                mViewHolder.mOne.setBackgroundResource(R.drawable.shape_gray_box);
                mViewHolder.mOne.setTextColor(deepTxt);
                mViewHolder.mOne.setText("删除订单");

                mViewHolder.mTwo.setVisibility(View.VISIBLE);
                mViewHolder.mTwo.setBackgroundResource(R.drawable.shape_red_box);
                mViewHolder.mTwo.setTextColor(redTxt);
                mViewHolder.mTwo.setText("回复评论");
                mViewHolder.mBuyStatus.setText("");
                mViewHolder.mQuitorrecommend.setText("用户评论: " + content);
                mViewHolder.mQuitorrecommend.setVisibility(View.VISIBLE);
                break;
            // }
        }

        mViewHolder.mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                String s = textView.getText().toString();
                if (TextUtils.isEmpty(s)) return;
                dataBean.setText(s);
                mListener.callBack(dataBean);

                // Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
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
        mViewHolder.mTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone)) return;
                HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                hintDialogUtils.setMessage("您将拨打" + phone);
                hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mContext.startActivity(intent);
                    }
                });
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UnusedOrderActivity.class);
                intent.putExtra("orderNo", order_no);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView mOrderNo;
        private TextView mBuyStatus;
        private TextView mGoodsName;
        private TextView mConsumeTime;
        private TextView mConsumeUsername;
        private TextView mPhone;
        private ImageView mTelephone;
        private TextView mQuitorrecommend;
        private TextView mQuitMoney;
        // private TextView mQuitRemark;
        private MyGridView mGridView;
        private TextView mBuyNum;
        private TextView mMoney;
        private TextView mOrderRemark;
        private TextView mOne;
        private TextView mTwo;
    }
}
