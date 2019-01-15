package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.view.MyListView;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends BaseNoTitleActivity {
    /*
    * textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线

    textView.getPaint().setAntiAlias(true);//抗锯齿

    textview.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

    setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰

    textView.getPaint().setFlags(0);  // 取消设置的的划线
    *
    * */
    private Context mContext;
    private TitleBar mTitlBar;
    private TextView mName;
    private StarView mStarView;
    private TextView mCommentNum;
    private TextView mCurPrice;
    private TextView mBeforePrice;
    private TextView mBuyNum;
    private TextView mShopsName;
    private TextView mAddress;
    private ImageView mPhone;
    private TextView mGoodsContent;
    private MyListView mListView;
    private LinearLayout mLl_bottom;
    private TextView mPrice;
    private TextView mOrder;
    private ShopsFormBean.DataBean dataBean;
    private String address;
    private String mobile;
    private String shop_name;
    private String region;
    private int displayWidth;
    private String shopId;
    private String logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_food_detail);
        mContext = this;
        Intent intent = getIntent();
        dataBean = (ShopsFormBean.DataBean) intent.getSerializableExtra("dataBean");
        address = intent.getStringExtra("address");
        mobile = intent.getStringExtra("mobile");
        shop_name = intent.getStringExtra("shopName");
        region = intent.getStringExtra("region");
        shopId = intent.getStringExtra("shopId");
        logo = intent.getStringExtra("logo");
        displayWidth = DisplayUtilX.getDisplayWidth();
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("商品详情");
        mName = (TextView) findViewById(R.id.name);
        mStarView = (com.ruihuo.ixungen.activity.merchant.StarView) findViewById(R.id.starView);
        mCommentNum = (TextView) findViewById(R.id.commentNum);
        mCurPrice = (TextView) findViewById(R.id.curPrice);
        mBeforePrice = (TextView) findViewById(R.id.beforePrice);
        mBuyNum = (TextView) findViewById(R.id.buyNum);
        mShopsName = (TextView) findViewById(R.id.shopsName);
        mAddress = (TextView) findViewById(R.id.address);
        mPhone = (ImageView) findViewById(R.id.phone);
        mGoodsContent = (TextView) findViewById(R.id.goodsContent);
        mLl_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mListView = (MyListView) findViewById(R.id.listView);
        mPrice = (TextView) findViewById(R.id.price);
        mOrder = (TextView) findViewById(R.id.order);
        //原来价格中间画个横线
        mBeforePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        mAddress.setText(TextUtils.isEmpty(region) ? "" : region + "  " + (TextUtils.isEmpty(address) ? "" : address));
        mShopsName.setText(TextUtils.isEmpty(shop_name) ? "" : shop_name);

        if (dataBean != null) {
            String info = dataBean.getInfo();
            String img = dataBean.getImg();
            String name = dataBean.getName();
            String original_price = dataBean.getOriginal_price();
            String price = dataBean.getPrice();
            String sales_volume = dataBean.getSales_volume();//已售数量
            mName.setText(TextUtils.isEmpty(name) ? "" : name);
            mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            mCurPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            mBeforePrice.setText("￥" + (TextUtils.isEmpty(original_price) ? "--" : original_price));
            mBuyNum.setText("已售: " + (TextUtils.isEmpty(sales_volume) ? "--" : sales_volume) + "  剩余: " + dataBean.getAmount());
            if (!TextUtils.isEmpty(info)) {
                String goodsContent = "商品内容: " + info;
                SpannableString spannableString = new SpannableString(goodsContent);
                spannableString.setSpan(new ForegroundColorSpan(Color.rgb(0x99, 0x99, 0x99)), 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mGoodsContent.setText(spannableString);
            }
            if (!TextUtils.isEmpty(img)) {
                String[] split = img.split("\\;");
                ImageAdapter imageAdapter = new ImageAdapter(split);
                mListView.setAdapter(imageAdapter);
            }
        }
    }

    private void addData() {

    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPhone.setOnClickListener(TelListener);
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!XunGenApp.isLogin) {
                    IntentSkip intentSkip = new IntentSkip();
                    intentSkip.skipLogin(mContext, 211);
                    return;
                }
                String amount = dataBean.getAmount();
                if (TextUtils.isEmpty(amount) || Float.parseFloat(amount) < 1) {
                    Toast.makeText(mContext, "该菜品已售完", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Time = DateFormatUtils.longToDate(System.currentTimeMillis());
                OrderData orderData = new OrderData();
                orderData.setType(1);
                orderData.setGoodsId(dataBean.getId());
                orderData.setShopId(shopId);
                orderData.setCover(dataBean.getCover());
                orderData.setIsCancel(dataBean.getIs_cancel());
                orderData.setLogo(logo);
                orderData.setShopName(shop_name);
                orderData.setAllAmount(amount);//总数量，不是预订的数量
                orderData.setName(dataBean.getName());
                orderData.setPrice(dataBean.getPrice());
                orderData.setInfo(dataBean.getInfo());
                orderData.setConsumeStartTime(Time);
                orderData.setConsumeEndTime(Time);
                orderData.setService(dataBean.getService());
                Intent intent = new Intent(mContext, OrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderData", orderData);
                bundle.putSerializable("dataBean", dataBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener TelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(mobile)) return;
            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
            hintDialogUtils.setMessage("您将拨打" + mobile);
            hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                @Override
                public void callBack(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            });
        }
    };

    class ImageAdapter extends BaseAdapter {
        String[] imgUrl;

        public ImageAdapter(String[] imgUrl) {
            this.imgUrl = imgUrl;
        }

        @Override
        public int getCount() {
            return imgUrl.length;
        }

        @Override
        public Object getItem(int position) {
            return imgUrl[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_food_img, null);
                mViewHolder = new ViewHolder();
                mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayWidth, displayWidth * 2 / 3);
            mViewHolder.imageView.setLayoutParams(layoutParams);

            String path = imgUrl[position];
            if (!TextUtils.isEmpty(path)) {
                Picasso.with(mContext)
                        .load(path)
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.default_photo)
                        .error(R.mipmap.default_photo)
                        .into(mViewHolder.imageView);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> imgList = new ArrayList<>();
                    for (int i = 0; i < imgUrl.length; i++) {
                        imgList.add(imgUrl[i]);
                    }
                    Intent intent = new Intent(mContext, ShopGalleryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("imgList", (Serializable) imgList);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView imageView;
    }
}
