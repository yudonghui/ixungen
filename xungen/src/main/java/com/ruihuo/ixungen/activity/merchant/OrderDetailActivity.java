package com.ruihuo.ixungen.activity.merchant;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DateFormtUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.view.TimeTextView;
import com.ruihuo.ixungen.view.TitleBar;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class OrderDetailActivity extends BaseNoTitleActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private TimeTextView mTime;
    private ImageView mTubiao;
    private TextView mBuyStatus;
    private LinearLayout mLl_twocode;
    private TextView mRefuseResean;
    private ImageView mTwocode;
    private TextView mUserCode;
    private ImageView mLogo;
    private TextView mShopsName;
    private LinearLayout mTelephone;
    private com.ruihuo.ixungen.activity.merchant.CardShopsView mCardShopsView;
    private TextView mAmount;
    private TextView mOrderNo;
    private TextView mMoney;
    private com.ruihuo.ixungen.activity.merchant.OrderInfoView mOrderInfoView;
    private TextView mCopy;
    private TextView mOrderTime;
    private TextView mOne;
    private TextView mTwo;
    private TextView mThree;
    private String orderNo;
    private OrderDetailBean.DataBean dataBean;
    private String mobile;//商家电话
    // private int isUsed;//1,使用的时候显示的界面，带二维码
    private BottomSkip bottomSkip;
    private ContactShops contactShops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mContext = this;
        orderNo = getIntent().getStringExtra("orderNo");
        // isUsed = getIntent().getIntExtra("isUsed", 0);
        initView();
        addData();
        addListener();
        //注册一个动态广播
        registerBoradcastReceiver();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("订单详情");
        mTime = (TimeTextView) findViewById(R.id.time);
        mTubiao = (ImageView) findViewById(R.id.tubiao);
        mBuyStatus = (TextView) findViewById(R.id.buyStatus);
        mLl_twocode = (LinearLayout) findViewById(R.id.ll_twocode);
        mTwocode = (ImageView) findViewById(R.id.twocode);
        mUserCode = (TextView) findViewById(R.id.userCode);
        mLogo = (ImageView) findViewById(R.id.logo);
        mShopsName = (TextView) findViewById(R.id.shopsName);
        mTelephone = (LinearLayout) findViewById(R.id.telephone);
        mCardShopsView = (com.ruihuo.ixungen.activity.merchant.CardShopsView) findViewById(R.id.cardShopsView);
        mAmount = (TextView) findViewById(R.id.amount);
        mMoney = (TextView) findViewById(R.id.money);
        mOrderInfoView = (com.ruihuo.ixungen.activity.merchant.OrderInfoView) findViewById(R.id.orderInfoView);
        mCopy = (TextView) findViewById(R.id.copy);
        mRefuseResean = (TextView) findViewById(R.id.refuse_resean);
        mOrderNo = (TextView) findViewById(R.id.orderNo);
        mOrderTime = (TextView) findViewById(R.id.orderTime);
        mOne = (TextView) findViewById(R.id.one);
        mTwo = (TextView) findViewById(R.id.two);
        mThree = (TextView) findViewById(R.id.three);

        contactShops = new ContactShops(mContext);


        bottomSkip = new BottomSkip(mContext, new DialogEditInterface() {
            @Override
            public void callBack(String message) {
                if ("取消订单".equals(message) || "删除订单".equals(message)) {
                    finish();
                }
            }
        });
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOne.setOnClickListener(BottomListener);
        mTwo.setOnClickListener(BottomListener);
        mThree.setOnClickListener(BottomListener);
        mTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = dataBean.getMobile();//商家电话
                String rid = dataBean.getRid();//商家根号
                contactShops.showDialog(rid, mobile);
            }
        });
        mCardShopsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBean == null) return;
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra("shopId", dataBean.getShop_id());
                intent.putExtra("type", Integer.parseInt(dataBean.getType()));
                mContext.startActivity(intent);
            }
        });
        mCopy.setOnClickListener(CopyListener);
    }

    private void addData() {
        OrderDetailWeb orderDetailWeb = new OrderDetailWeb(mContext);
        orderDetailWeb.orderDetail(orderNo, "0", new OrderDetailWeb.CallBackInterface() {
            @Override
            public void callBack(OrderDetailBean.DataBean data) {
                dataBean = data;
                setView();
            }

        });
    }

    private void setView() {
        if (dataBean == null) return;
        mCardShopsView.setData(dataBean);
        mOrderInfoView.setData(dataBean);
        mOrderInfoView.setTelephone();
        String logo = dataBean.getLogo();
        String shop_name = dataBean.getShop_name();
        mobile = dataBean.getMobile();//商家电话
        String order_no = dataBean.getOrder_no();
        String status = dataBean.getStatus();
        String cost = dataBean.getCost();
        String amount = dataBean.getAmount();
        String create_time = dataBean.getCreate_time();
        String refuse_reson = dataBean.getRefuse_reason();

        if (!TextUtils.isEmpty(amount)) {
            int num = (int) Float.parseFloat(amount);
            mAmount.setText(num + "");
        }
        mMoney.setText("￥" + (TextUtils.isEmpty(cost) ? "--" : cost));
        mOrderNo.setText("订单编号: " + (TextUtils.isEmpty(order_no) ? "--" : order_no));
        mOrderTime.setText("下单时间: " + DateFormatUtils.stringToDateMim(create_time));
        mShopsName.setText(TextUtils.isEmpty(shop_name) ? "" : shop_name);
        PicassoUtils.setImgView(logo, R.mipmap.default_header, mContext, mLogo);
        if ("0".equals(status)) {
            String deadline = dataBean.getDeadline();
            if (!TextUtils.isEmpty(deadline)) {
                long endTime = Long.parseLong(deadline);
                long curTime = System.currentTimeMillis();
                long time = endTime * 1000 - curTime;
                if (time > 0) {
                    int[] times = DateFormtUtils.obtainTime(time);
                    mTime.setTimes(times);
                    mTime.setVisibility(View.VISIBLE);
                    mTime.setListener(new TimeTextView.endTimeListener() {
                        @Override
                        public void callback() {
                            //倒计时完成之后的回调
                            mTime.setText("订单已失效");
                            mOne.setText("订单已失效");
                            mOne.setVisibility(View.VISIBLE);
                            mTwo.setVisibility(View.GONE);
                            mBuyStatus.setText("订单已失效");
                            mTime.setVisibility(View.GONE);
                        }
                    });
                    if (!mTime.isRun()) {
                        mTime.run();
                    }
                    mOne.setText("取消订单");
                    mOne.setVisibility(View.VISIBLE);
                    mTwo.setText("立即支付");
                    mTwo.setVisibility(View.VISIBLE);
                    mBuyStatus.setText("等待付款");

                } else {
                    mOne.setText("取消订单");
                    mOne.setVisibility(View.VISIBLE);
                    mBuyStatus.setText("订单已失效");
                }
            }

        } else if ("1".equals(status)) {//支付完成，未消费。和商家拒绝退款
            mLl_twocode.setVisibility(View.VISIBLE);
            if ("1".equals(dataBean.getIs_cancel()))
                mOne.setText("申请退款");
            else mOne.setText("不可取消");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setVisibility(View.GONE);
            mTubiao.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.GONE);
            mTubiao.setImageResource(R.mipmap.unuser_order);
            mBuyStatus.setText("待使用");

            final int resize = DisplayUtilX.dip2px(150);
            NetWorkData netWorkData = new NetWorkData();
            netWorkData.jiaTwoCode(mContext, orderNo, "1", new NetWorkData.AddressInterface() {
                @Override
                public void callBack(String result) {
                    Bitmap mBitmap = CodeUtils.createImage(result, resize, resize, BitmapFactory.decodeResource(getResources(), R.drawable.notification));
                    mTwocode.setImageBitmap(mBitmap);
                }
            });

            mUserCode.setText("使用码: " + orderNo);
        } else if ("2".equals(status)) {
            mLl_twocode.setVisibility(View.GONE);
            mOne.setText("删除订单");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setText("再来一单");
            mTwo.setVisibility(View.VISIBLE);
            mThree.setText("投诉建议");
            mThree.setVisibility(View.VISIBLE);

            mTubiao.setVisibility(View.VISIBLE);
            mTubiao.setImageResource(R.mipmap.order_finish);
            mBuyStatus.setText("交易完成！");
        } else if ("3".equals(status)) {
            mLl_twocode.setVisibility(View.GONE);
            mOne.setVisibility(View.GONE);
            mTwo.setText("取消退款");
            mTwo.setVisibility(View.VISIBLE);
            mThree.setVisibility(View.GONE);
            mTubiao.setVisibility(View.GONE);
            mTime.setText("-￥" + cost);
            mTime.setVisibility(View.VISIBLE);

            mBuyStatus.setText("退款中");
        } else if ("4".equals(status)) {//退款成功
            mLl_twocode.setVisibility(View.GONE);
            mOne.setText("删除订单");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setVisibility(View.GONE);
            mThree.setVisibility(View.GONE);
            mTubiao.setVisibility(View.GONE);
            mTime.setText("+￥" + cost);
            mTime.setVisibility(View.VISIBLE);
            mBuyStatus.setText("退款成功");
        } else if ("5".equals(status)) {//商家拒单
            mLl_twocode.setVisibility(View.GONE);
            mOne.setText("删除订单");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setText("投诉建议");
            mTwo.setVisibility(View.VISIBLE);
            mThree.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
            mTubiao.setVisibility(View.VISIBLE);
            mRefuseResean.setVisibility(View.VISIBLE);
            mRefuseResean.setText("拒单原因: " + (TextUtils.isEmpty(refuse_reson) ? "" : refuse_reson));
            mTubiao.setImageResource(R.mipmap.refund_order);
            mBuyStatus.setText("商家拒单");

        } else if ("6".equals(status)) {//商家拒绝了退款
            mLl_twocode.setVisibility(View.VISIBLE);
            mOne.setText("投诉建议");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setVisibility(View.GONE);
            mThree.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
            mTubiao.setVisibility(View.VISIBLE);
            mTubiao.setImageResource(R.mipmap.refund_order);
            mRefuseResean.setVisibility(View.VISIBLE);
            mRefuseResean.setText("拒绝退款原因: " + (TextUtils.isEmpty(refuse_reson) ? "" : refuse_reson));
            final int resize = DisplayUtilX.dip2px(150);
            NetWorkData netWorkData = new NetWorkData();
            netWorkData.jiaTwoCode(mContext, orderNo, ConstantNum.ORDER_TYPE, new NetWorkData.AddressInterface() {
                @Override
                public void callBack(String result) {
                    Bitmap mBitmap = CodeUtils.createImage(result, resize, resize, BitmapFactory.decodeResource(getResources(), R.drawable.notification));
                    mTwocode.setImageBitmap(mBitmap);
                }
            });
            mUserCode.setText("使用码: " + orderNo);
            mBuyStatus.setText("商家拒绝退款");
        } else if ("7".equals(status)) {
            mLl_twocode.setVisibility(View.GONE);
            mOne.setText("删除订单");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setText("立即评价");
            mTwo.setVisibility(View.VISIBLE);
            mThree.setVisibility(View.GONE);
            mTubiao.setImageResource(R.mipmap.order_finish);
            mTubiao.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.GONE);
            mBuyStatus.setText("待评价");
        } else if ("8".equals(status)) {
            mLl_twocode.setVisibility(View.GONE);
            mOne.setText("删除订单");
            mOne.setVisibility(View.VISIBLE);
            mTwo.setVisibility(View.GONE);
            mThree.setText("投诉建议");
            mThree.setVisibility(View.VISIBLE);

            mTubiao.setImageResource(R.mipmap.order_finish);
            mTubiao.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.GONE);
            mBuyStatus.setText("等待商家回复");
        }

    }

    View.OnClickListener BottomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            String text = textView.getText().toString();
            dataBean.setText(text);
            dataBean.setMode("0");//商家还是用户
            bottomSkip.skip(dataBean);
        }
    };
    View.OnClickListener CopyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String order_no = dataBean.getOrder_no();
            ClipboardManager systemService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            systemService.setText(order_no);
            Toast.makeText(mContext, "复制成功！", Toast.LENGTH_SHORT).show();
        }
    };


    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.CONSUME_SUCEESS);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.CONSUME_SUCEESS)) {
                addData();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 233 && resultCode == 332) {//从申请退款 界面回来
            finish();
        }
    }

}
