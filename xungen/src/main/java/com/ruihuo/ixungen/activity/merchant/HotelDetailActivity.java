package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotelDetailActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mEditor;
    private TextView mText_title;
    private ImageView mPhoto;
    private TextView mPhotos;
    private TextView mClassify;
    private TextView mBath;
    private TextView mBreakfast;
    private TextView mInternet;
    private TextView mAir;
    private TextView mIs_reserve;
    private TextView mRoomPrice;
    private TextView mInfo;
    private TextView mIs_cancel;
    private TextView mCancel_rule;
    private LinearLayout mLl_hotel;
    private LinearLayout mLl_hotel2;
    private TextView mTextInfo;
    private GoodsFormBaseBean dataBean;
    private int type;
    private String shopId;
    private int mode;//1是管理商品跳转过来。可以编辑。2是不可编辑的。从推荐菜跳转过来

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        mContext = this;
        dataBean = (GoodsFormBaseBean) getIntent().getSerializableExtra("dataBean");
        type = getIntent().getIntExtra("type", 0);
        shopId = getIntent().getStringExtra("shopId");
        mode = getIntent().getIntExtra("mode", 0);
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mLl_hotel = (LinearLayout) findViewById(R.id.ll_hotel);
        mLl_hotel2 = (LinearLayout) findViewById(R.id.ll_hotel2);
        mTextInfo = (TextView) findViewById(R.id.text_info);

        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mEditor = (TextView) findViewById(R.id.edit);
        mText_title = (TextView) findViewById(R.id.text_title);
        mPhoto = (ImageView) findViewById(R.id.photo);
        mPhotos = (TextView) findViewById(R.id.photos);
        mClassify = (TextView) findViewById(R.id.classify);
        mBath = (TextView) findViewById(R.id.bath);
        mBreakfast = (TextView) findViewById(R.id.breakfast);
        mInternet = (TextView) findViewById(R.id.internet);
        mAir = (TextView) findViewById(R.id.air);
        mIs_reserve = (TextView) findViewById(R.id.is_reserve);
        mRoomPrice = (TextView) findViewById(R.id.roomPrice);
        mInfo = (TextView) findViewById(R.id.info);
        mIs_cancel = (TextView) findViewById(R.id.is_cancel);
        mCancel_rule = (TextView) findViewById(R.id.cancel_rule);
        if (mode == 1) {
            mEditor.setVisibility(View.VISIBLE);
            mText_title.setText("管理商品");
        } else {
            mEditor.setVisibility(View.GONE);
            mText_title.setText("商品详情");
        }
        if (type == 1 || type == 4) {//商品类型，1-餐饮；2-酒店
            mLl_hotel.setVisibility(View.GONE);
            mLl_hotel2.setVisibility(View.GONE);
            mTextInfo.setText("商品简介");
        } else {
            mLl_hotel.setVisibility(View.VISIBLE);
            mLl_hotel2.setVisibility(View.VISIBLE);
            mTextInfo.setText("房间说明");
        }

        int displayWidth = DisplayUtilX.getDisplayWidth();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(displayWidth, displayWidth / 2);
        mPhoto.setLayoutParams(layoutParams);
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditor.setOnClickListener(EditorListener);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cover = dataBean.getCover();
                String img = dataBean.getImg();
                if (TextUtils.isEmpty(cover)) return;
                if (TextUtils.isEmpty(img)) return;
                List<String> imgList = new ArrayList<>();
                if (type == 1) {
                    String[] split = img.split("\\;");
                    for (int i = 0; i < split.length; i++) {
                        imgList.add(split[i]);
                    }
                } else {
                    String[] split = cover.split("\\;");
                    for (int i = 0; i < split.length; i++) {
                        imgList.add(split[i]);
                    }
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

    private void addData() {
        if (dataBean == null) return;
        String cover = dataBean.getCover();
        String img = dataBean.getImg();
        String price = dataBean.getPrice();
        String service = dataBean.getService();
        String classify = dataBean.getClassify();
        String is_cancel = dataBean.getIs_cancel();
        String is_reserve = dataBean.getIs_reserve();
        String cancel_rule = dataBean.getCancel_rule();
        String info = dataBean.getInfo();

        mClassify.setText(TextUtils.isEmpty(classify) ? "--" : classify);
        mIs_reserve.setText("1".equals(is_reserve) ? "可预定" : "不可预定");
        if (type == 2)
            mRoomPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price) + "/晚");
        else mRoomPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
        mInfo.setText(TextUtils.isEmpty(info) ? "" : info);
        if ("1".equals(is_cancel)) {
            mIs_cancel.setText("该房间可取消");
            mCancel_rule.setText(TextUtils.isEmpty(cancel_rule) ? "" : cancel_rule);
        } else {
            mIs_cancel.setText("该房间不可取消");
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

            if (type == 1) {
                if (!TextUtils.isEmpty(img)) {
                    String[] split = img.split("\\;");
                    mPhotos.setText(split.length + "");
                }
            } else mPhotos.setText(split1.length + "");
            Picasso.with(mContext)
                    .load(split1[0])
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_long)
                    .placeholder(R.mipmap.default_long)
                    .into(mPhoto);
        }
    }

    View.OnClickListener EditorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (type == 1 || type == 4) {
                Intent intent = new Intent(mContext, AddGoodsFoodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", dataBean);
                bundle.putString("shopId", shopId);
                bundle.putInt("type", type);
                intent.putExtras(bundle);
                startActivityForResult(intent, 661);
            } else {
                Intent intent = new Intent(mContext, AddShopsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", dataBean);
                bundle.putString("shopId", shopId);
                bundle.putInt("type", type);
                intent.putExtras(bundle);
                startActivityForResult(intent,661);
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 661 && resultCode == 666) {
            Intent intent = new Intent();
            setResult(666, intent);
            finish();
        }
    }
}
