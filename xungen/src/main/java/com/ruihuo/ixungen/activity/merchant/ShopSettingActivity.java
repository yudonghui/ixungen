package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.entity.ShopsDetailBean;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GetPhotoUrl;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.util.HashMap;
import java.util.Map;

public class ShopSettingActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mConfirm;
    private LinearLayout mLl_avator;
    private com.ruihuo.ixungen.view.CircleImageView mAvatar;
    private EditText mShopsName;
    private EditText mShop_time;
    private EditText mIntroduce;
    private LinearLayout mLl_check_in;
    private EditText mCheckin;
    private LinearLayout mLl_check_out;
    private EditText mCheckout;
    /* private ImageView mWIFI;
     private ImageView mStopping_place;
     private ImageView mSofa;
     private ImageView mNon_smoking;
     private ImageView mRoom;
     private ImageView mChildren;
     private ImageView mActor;
     private ImageView mCard;
     private ImageView mBabyq;*/
    private ServiceView mServiceView;
    private PhotoUtils photoUtils;
    private HashMap<String, String> mapFactivity = new HashMap<>();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_setting);
        mContext = this;
        type = getIntent().getIntExtra("type", 0);
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mLl_avator = (LinearLayout) findViewById(R.id.ll_avator);
        mAvatar = (com.ruihuo.ixungen.view.CircleImageView) findViewById(R.id.avatar);
        mShopsName = (EditText) findViewById(R.id.shopsName);
        mShop_time = (EditText) findViewById(R.id.shop_time);
        mIntroduce = (EditText) findViewById(R.id.introduce);
        mLl_check_in = (LinearLayout) findViewById(R.id.ll_check_in);
        mCheckin = (EditText) findViewById(R.id.checkin);
        mLl_check_out = (LinearLayout) findViewById(R.id.ll_check_out);
        mCheckout = (EditText) findViewById(R.id.checkout);
        mServiceView = (ServiceView) findViewById(R.id.serviceView);
        photoUtils = new PhotoUtils(mContext);
        if (type == 2) {
            mLl_check_in.setVisibility(View.VISIBLE);
            mLl_check_out.setVisibility(View.VISIBLE);
        } else {
            mLl_check_in.setVisibility(View.GONE);
            mLl_check_out.setVisibility(View.GONE);
        }
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_avator.setOnClickListener(AvatorListener);
        mServiceView.setListener(ServiceListener);
        mServiceView.setType(type);
        mConfirm.setOnClickListener(ConfirmListener);
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.SHOPS_DETAIL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                ShopsDetailBean shopsDetailBean = gson.fromJson(result, ShopsDetailBean.class);
                ShopsDetailBean.DataBean data = shopsDetailBean.getData();
                logo = data.getLogo();
                String check_in_time = data.getCheck_in_time();
                String check_out_time = data.getCheck_out_time();
                String shop_name = data.getShop_name();
                String shop_time = data.getShop_time();
                String text = data.getText();//店铺简介
                String service = data.getService();//服务
                PicassoUtils.setImgView(logo, R.mipmap.default_header, mContext, mAvatar);
                mShopsName.setText(TextUtils.isEmpty(shop_name) ? "" : shop_name);
                mShop_time.setText(TextUtils.isEmpty(shop_time) ? "" : shop_time);
                mIntroduce.setText(TextUtils.isEmpty(text) ? "" : text);
                mCheckin.setText(TextUtils.isEmpty(check_in_time) ? "" : check_in_time);
                mCheckout.setText(TextUtils.isEmpty(check_out_time) ? "" : check_out_time);
                mServiceView.setService(service);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    View.OnClickListener AvatorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            photoUtils.showDialog();
        }
    };
    ServiceView.SelectorService ServiceListener = new ServiceView.SelectorService() {
        @Override
        public void callBack(HashMap<String, String> map) {
            mapFactivity.clear();
            mapFactivity.putAll(map);
        }
    };

    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(logo)) {
                Toast.makeText(mContext, "请给店铺设置一个头像", Toast.LENGTH_SHORT).show();
                return;
            }
            String shopName = mShopsName.getText().toString();
            String shopTime = mShop_time.getText().toString();
            String checkInTime = mCheckin.getText().toString();
            String checkOutTime = mCheckout.getText().toString();

            String text = mIntroduce.getText().toString();
            StringBuilder service = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, String> entry : mapFactivity.entrySet()) {
                if (i == 0)
                    service.append(entry.getKey());
                else service.append("," + entry.getKey());
                i++;
            }
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("logo", logo);
            params.putString("shopName", shopName);
            params.putString("shopTime", shopTime);
            params.putString("text", text);
            if (!TextUtils.isEmpty(checkOutTime))
                params.putString("checkOutTime", checkOutTime);
            if (!TextUtils.isEmpty(checkInTime))
                params.putString("checkInTime", checkInTime);
            params.putString("service", String.valueOf(service));
            mHttp.post(Url.SHOPS_INFO_UPDATE, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    Intent intent = new Intent();
                    setResult(322, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    private String logo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.REQUEST_CODE_TAKING_PHOTO && resultCode == RESULT_OK) {
            photoUtils.dealTakePhotoThenZoom();// 拍照的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL && resultCode == RESULT_OK) {
            photoUtils.dealChoosePhotoThenZoom(data);//选择图片的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_CUT_PHOTO && resultCode == RESULT_OK) {
            // mPhotoUtils.dealZoomPhoto(mAvator, 1, "");// 剪裁图片的结果
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtils.IMAGE_DETAIL_PATH);
            mAvatar.setImageBitmap(bitmap);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(PhotoUtils.IMAGE_DETAIL_PATH, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    loadingDialogUtils.setDimiss();
                    logo = message;
                }
            });
        }
    }
}
