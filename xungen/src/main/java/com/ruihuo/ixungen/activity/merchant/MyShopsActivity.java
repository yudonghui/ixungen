package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

public class MyShopsActivity extends AppCompatActivity {
    private ImageView mImgBack;
    private TextView mTitle;
    private ImageView mAvatar;
    private ImageView mIsActor;
    private TextView mShopsName;
    private TextView mShopsNum;
    private TextView mBrowses;
    private TextView mAllmoney;
    private LinearLayout mLl_photo;
    private LinearLayout mLl_fabu;
    private LinearLayout mLl_recommend;
    private LinearLayout mLl_manager;
    private LinearLayout mLl_youhui;
    private LinearLayout mLl_setting;
    private LinearLayout mLl_allorder;
    private LinearLayout mWait_pay;//待付款
    private LinearLayout mWait_quit;//待退款
    private LinearLayout mWait_reply;//待回复
    private LinearLayout mUnused;//未使用
    private LinearLayout mFinished;//已完成
    private TextView mVerifyOrder;//验证订单
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shops);
        mContext = this;
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTitle = (TextView) findViewById(R.id.title);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mIsActor = (ImageView) findViewById(R.id.isActor);
        mShopsName = (TextView) findViewById(R.id.shopsName);
        mShopsNum = (TextView) findViewById(R.id.shopsNum);
        mBrowses = (TextView) findViewById(R.id.browses);
        mAllmoney = (TextView) findViewById(R.id.allmoney);
        mLl_photo = (LinearLayout) findViewById(R.id.ll_photo);
        mLl_fabu = (LinearLayout) findViewById(R.id.ll_fabu);
        mLl_recommend = (LinearLayout) findViewById(R.id.ll_recommend);
        mLl_manager = (LinearLayout) findViewById(R.id.ll_manager);
        mLl_youhui = (LinearLayout) findViewById(R.id.ll_youhui);
        mLl_setting = (LinearLayout) findViewById(R.id.ll_setting);
        mLl_allorder = (LinearLayout) findViewById(R.id.ll_allorder);
        mWait_pay = (LinearLayout) findViewById(R.id.wait_pay);
        mWait_quit = (LinearLayout) findViewById(R.id.wait_quit);
        mWait_reply = (LinearLayout) findViewById(R.id.wait_reply);
        mUnused = (LinearLayout) findViewById(R.id.unused);
        mFinished = (LinearLayout) findViewById(R.id.finished);
        mVerifyOrder = (TextView) findViewById(R.id.verify_order);
        mLl_fabu.setVisibility(View.VISIBLE);
    }

    private void addListener() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_photo.setOnClickListener(PhotoListener);//店铺相册
        mLl_fabu.setOnClickListener(FabuListener);//发布商品
        mLl_recommend.setOnClickListener(RecommendListener);//推荐商品
        mLl_manager.setOnClickListener(ManagerListener);//管理商品
        mLl_youhui.setOnClickListener(YouhuiListener);//优惠活动
        mLl_setting.setOnClickListener(SettingListener);//店铺设置
        mAvatar.setOnClickListener(AvatarListener);
        mLl_allorder.setOnClickListener(OrderFormListener);
        mWait_pay.setOnClickListener(OrderFormListener);
        mWait_quit.setOnClickListener(OrderFormListener);
        mWait_reply.setOnClickListener(OrderFormListener);
        mUnused.setOnClickListener(OrderFormListener);
        mFinished.setOnClickListener(OrderFormListener);
        mVerifyOrder.setOnClickListener(VerifyListener);//验证订单
    }

    private String shopId;
    private int type;

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.SHOP_INFO, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    Gson gson = GsonUtils.getGson();
                    ShopInfoBean shopInfoBean = gson.fromJson(result, ShopInfoBean.class);
                    ShopInfoBean.DataBean data1 = shopInfoBean.getData();
                    String shop_name = data1.getShop_name();
                    String total_sale = data1.getTotal_sale();
                    String total_visit = data1.getTotal_visit();
                    String logo = data1.getLogo();
                    if (!TextUtils.isEmpty(data1.getType()))
                        type = Integer.parseInt(data1.getType());

                    shopId = data1.getId();
                    mBrowses.setText(TextUtils.isEmpty(total_visit) ? "" : total_visit);
                    mAllmoney.setText(TextUtils.isEmpty(total_sale) ? "" : total_sale);
                    mShopsName.setText(TextUtils.isEmpty(shop_name) ? "" : shop_name);
                    mShopsNum.setText("店铺号: " + shopId);
                    PicassoUtils.setImgView(logo, R.mipmap.default_header, mContext, mAvatar);
                } catch (Exception e) {
                    finish();
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    View.OnClickListener VerifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, VerifyOrderActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener OrderFormListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, OrderFormActivity.class);
            switch (v.getId()) {
                case R.id.ll_allorder:
                    intent.putExtra("from", ConstantNum.ORDER_ALLS);
                    break;
                case R.id.wait_pay:
                    intent.putExtra("from", ConstantNum.ORDER_WAIT_PAYS);
                    break;
                case R.id.wait_quit:
                    intent.putExtra("from", ConstantNum.ORDER_WAIT_QUITS);
                    break;
                case R.id.wait_reply:
                    intent.putExtra("from", ConstantNum.ORDER_WAIT_REPLYS);
                    break;
                case R.id.unused:
                    intent.putExtra("from", ConstantNum.ORDER_WAIT_USEDS);
                    break;
                case R.id.finished:
                    intent.putExtra("from", ConstantNum.ORDER_FINISHEDS);
                    break;
            }
            intent.putExtra("type", 1);//0-普通消费者查看订单列表；1-店家查看订单列表
            startActivity(intent);
        }
    };
    View.OnClickListener AvatarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, HotelActivity.class);
            intent.putExtra("shopId", shopId);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    };
    View.OnClickListener PhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopsPhotoActivity.class);
            intent.putExtra("shopId", shopId);
            intent.putExtra("type", type);//1-餐饮；2-酒店; 4-旅游
            intent.putExtra("mode", true);
            startActivity(intent);
        }
    };
    View.OnClickListener FabuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (type == 1) {
                Intent intent = new Intent(mContext, AddGoodsFoodActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("type", type);
                startActivity(intent);
            } else if (type==2){
                Intent intent = new Intent(mContext, AddShopsActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("type", type);
                startActivity(intent);
            }else {//旅游
                Intent intent = new Intent(mContext, AddGoodsFoodActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("type", type);
                startActivity(intent);
            }

        }
    };
    View.OnClickListener RecommendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener ManagerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopsManagerActivity.class);
            intent.putExtra("shopId", shopId);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    };
    View.OnClickListener YouhuiListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener SettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopSettingActivity.class);
            intent.putExtra("type", type);
            startActivityForResult(intent, 222);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 222 && resultCode == 322) {
            addData();
        }
    }
}
