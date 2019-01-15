package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;

/**
 * @author yudonghui
 * @date 2017/8/17
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderInfoView extends LinearLayout {
    private Context mContext;
    private View inflate;
    private TextView mGoodsName;
    private TextView mConsumeTime;
    private TextView mConsumeUsername;
    private TextView mPhone;
    private ImageView mTelephone;
    private TextView mOrderRemark;

    public OrderInfoView(Context context) {
        super(context);
        mContext = context;
        inflate = View.inflate(context, R.layout.order_info_view, this);
    }

    public OrderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate = View.inflate(context, R.layout.order_info_view, this);
    }

    public OrderInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate = View.inflate(context, R.layout.order_info_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mGoodsName = (TextView) findViewById(R.id.goodsName);
        mConsumeTime = (TextView) findViewById(R.id.consumeTime);
        mConsumeUsername = (TextView) findViewById(R.id.consumeUsername);
        mPhone = (TextView) findViewById(R.id.phone);
        mTelephone = (ImageView) findViewById(R.id.telephone);
        mOrderRemark = (TextView) findViewById(R.id.orderRemark);
        mTelephone.setOnClickListener(new OnClickListener() {
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
    }

    private String phone;

    public void setData(OrderDetailBean.DataBean dataBean) {
        String type = dataBean.getType();
        String goods_name = dataBean.getGoods_name();
        String consume_end_time = dataBean.getConsume_end_time();
        String consume_start_time = dataBean.getConsume_start_time();
        String user_name = dataBean.getUser_name();
        phone = dataBean.getPhone();
        String info = dataBean.getInfo();
        mGoodsName.setText(TextUtils.isEmpty(goods_name) ? "" : goods_name);
        mPhone.setText("联系电话: " + (TextUtils.isEmpty(phone) ? "" : phone));
        if ("2".equals(type)) {//酒店   酒店有入住时间，餐厅没有消费时间
            if (TextUtils.isEmpty(consume_end_time) || TextUtils.isEmpty(consume_start_time))
                return;
            long in = DateFormatUtils.StringToLong(consume_start_time);
            long out = DateFormatUtils.StringToLong(consume_end_time);
            int days = (int) ((out - in) / 86400000);
            String start = DateFormatUtils.longToDate(in);
            String end = DateFormatUtils.longToDate(out);
            mConsumeUsername.setText("入住人: " + (TextUtils.isEmpty(user_name) ? "" : user_name));
            mConsumeTime.setVisibility(View.VISIBLE);
            mConsumeTime.setText("入住: " + start +
                    " 离店: " + end + "  共" + days + "晚");
        } else {
            mConsumeUsername.setText("消费者: " + (TextUtils.isEmpty(user_name) ? "" : user_name));
            mConsumeTime.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(info)) {//订单备注
            mOrderRemark.setVisibility(View.VISIBLE);
            mOrderRemark.setText("订单备注: " + info);
        } else {
            mOrderRemark.setVisibility(View.GONE);
        }
    }

    public void setTelephone() {
        mTelephone.setVisibility(GONE);
    }
}
