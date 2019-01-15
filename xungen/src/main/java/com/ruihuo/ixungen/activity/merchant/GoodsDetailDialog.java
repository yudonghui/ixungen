package com.ruihuo.ixungen.activity.merchant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ruihuo.ixungen.R.id.ll_bottom;

/**
 * @author yudonghui
 * @date 2017/8/9
 * @describe May the Buddha bless bug-free!!!
 */
public class GoodsDetailDialog {
    private final Window window;
    private Context mContext;
    private View inflate;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private TextView mEdit;
    private ImageView mPhoto;
    private TextView mPhotos;
    private LinearLayout mLl_hotel;
    private TextView mClassify;
    private TextView mBath;
    private TextView mBreakfast;
    private TextView mInternet;
    private TextView mAir;
    private TextView mIs_reserve;
    private TextView mRoomPrice;
    private TextView mText_info;
    private TextView mInfo;
    private LinearLayout mLl_hotel2;
    private TextView mIs_cancel;
    private TextView mCancel_rule;
    private LinearLayout mLl_bottom;
    private TextView mPrice;
    private TextView mOrder;
    private LinearLayout mLl_title;
    private TextView mName;
    private TextView mCancel;
    private RelativeLayout mRl_title;
    private Dialog mDialog;
    private GoodsFormBaseBean dataBean;
    private OrderData orderData;


    public GoodsDetailDialog(Context mContext) {
        this.mContext = mContext;
        //有关相机调用
        inflate = View.inflate(mContext, R.layout.activity_hotel_detail, null);
        mRl_title = (RelativeLayout) inflate.findViewById(R.id.rl_title);
        mImage_titlebar_back = (ImageView) inflate.findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) inflate.findViewById(R.id.text_title);
        mEdit = (TextView) inflate.findViewById(R.id.edit);
        mPhoto = (ImageView) inflate.findViewById(R.id.photo);
        mPhotos = (TextView) inflate.findViewById(R.id.photos);
        mLl_hotel = (LinearLayout) inflate.findViewById(R.id.ll_hotel);
        mClassify = (TextView) inflate.findViewById(R.id.classify);
        mBath = (TextView) inflate.findViewById(R.id.bath);
        mBreakfast = (TextView) inflate.findViewById(R.id.breakfast);
        mInternet = (TextView) inflate.findViewById(R.id.internet);
        mAir = (TextView) inflate.findViewById(R.id.air);
        mIs_reserve = (TextView) inflate.findViewById(R.id.is_reserve);
        mRoomPrice = (TextView) inflate.findViewById(R.id.roomPrice);
        mText_info = (TextView) inflate.findViewById(R.id.text_info);
        mInfo = (TextView) inflate.findViewById(R.id.info);
        mLl_hotel2 = (LinearLayout) inflate.findViewById(R.id.ll_hotel2);
        mIs_cancel = (TextView) inflate.findViewById(R.id.is_cancel);
        mCancel_rule = (TextView) inflate.findViewById(R.id.cancel_rule);
        mLl_bottom = (LinearLayout) inflate.findViewById(ll_bottom);
        mPrice = (TextView) inflate.findViewById(R.id.price);
        mOrder = (TextView) inflate.findViewById(R.id.order);
        mLl_title = (LinearLayout) inflate.findViewById(R.id.ll_title);
        mName = (TextView) inflate.findViewById(R.id.name);
        mCancel = (TextView) inflate.findViewById(R.id.cancel);
        mRl_title.setVisibility(View.GONE);
        mLl_title.setVisibility(View.VISIBLE);

        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        addListener();
    }

    private void addListener() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        //立即预定
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XunGenApp.isLogin) {
                    Intent intent = new Intent(mContext, OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderData", orderData);
                    bundle.putSerializable("dataBean", dataBean);
                    intent.putExtras(bundle);
                    ((Activity) mContext).startActivity(intent);
                    mDialog.dismiss();
                } else {
                    IntentSkip intentSkip = new IntentSkip();
                    intentSkip.skipLogin(mContext, 211);
                }

            }
        });
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cover = dataBean.getCover();
                if (TextUtils.isEmpty(cover)) return;
                String[] split = cover.split("\\;");
                List<String> imgList = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    imgList.add(split[i]);
                }
                Intent intent = new Intent(mContext, ShopGalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgList", (Serializable) imgList);
                bundle.putInt("position", 0);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    public void showDialog(GoodsFormBaseBean dataBean, OrderData orderData, int mode) {
        if (mode == 1) {
            mLl_bottom.setVisibility(View.VISIBLE);
        } else {
            mLl_bottom.setVisibility(View.GONE);
        }
        if (dataBean == null || orderData == null) return;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        FrameLayout.LayoutParams photoLayout = new FrameLayout.LayoutParams(displayWidth, displayWidth / 2);
        mPhoto.setLayoutParams(photoLayout);
        this.dataBean = dataBean;
        this.orderData = orderData;
        int type = orderData.getType();
        String shopId = orderData.getShopId();

        String name = dataBean.getName();
        String cover = dataBean.getCover();
        String price = dataBean.getPrice();
        String service = dataBean.getService();
        String classify = dataBean.getClassify();
        String is_cancel = dataBean.getIs_cancel();
        String is_reserve = dataBean.getIs_reserve();
        String cancel_rule = dataBean.getCancel_rule();
        String info = dataBean.getInfo();
        if (type == 1) {//商品类型，1-餐饮；2-酒店
            mLl_hotel.setVisibility(View.GONE);
            mLl_hotel2.setVisibility(View.GONE);
            mText_info.setText("商品简介");
            mRoomPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
        } else if (type == 2) {
            mLl_hotel.setVisibility(View.VISIBLE);
            mLl_hotel2.setVisibility(View.VISIBLE);
            mText_info.setText("房间说明");
            mRoomPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price) + "/晚");
            mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price) + "/晚");
        } else {
            mLl_hotel.setVisibility(View.GONE);
            mLl_hotel2.setVisibility(View.GONE);
            mText_info.setText("商品简介");
            mRoomPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
        }

        mName.setText(TextUtils.isEmpty(name) ? "" : name);
        mClassify.setText(TextUtils.isEmpty(classify) ? "--" : classify);
        mIs_reserve.setText("1".equals(is_reserve) ? "可预定" : "不可预定");

        mInfo.setText(TextUtils.isEmpty(info) ? "" : info);
        if ("1".equals(is_cancel)) {
            mIs_cancel.setText("可取消订购");
            mCancel_rule.setText(TextUtils.isEmpty(cancel_rule) ? "" : cancel_rule);
        } else {
            mIs_cancel.setText("不可取消订购");
            mCancel_rule.setText("该房间一旦购买则不可取消，我们将认定您及时入住，该房间将整晚为您保留");
        }
        if (!TextUtils.isEmpty(service)) {
            String[] split = service.split("\\,");
            for (int i = 0; i < split.length; i++) {
                switch (split[i]) {
                    case "WIFI":
                        mInternet.setText("WIFI");
                        break;
                    case "早餐":
                        mBreakfast.setText("提供早餐");
                        break;
                    case "独立卫浴":
                        mBath.setText("独立卫浴");
                        break;
                    case "空调":
                        mAir.setText("有");
                        break;
                    case "电脑宽带":
                        mInternet.setText("电脑宽带");
                        break;
                }
            }
        }

        if (!TextUtils.isEmpty(cover)) {
            String[] split1 = cover.split("\\;");
            mPhotos.setText(split1.length + "");
            Picasso.with(mContext)
                    .load(split1[0])
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_long)
                    .placeholder(R.mipmap.default_long)
                    .into(mPhoto);
        }


        mDialog.show();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //layoutParams.width = window.getWindowManager().getDefaultDisplay().getWidth();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayUtilX.getDisplayHeight() * 3 / 4;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
    }
}
