package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.utils.BitmapUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class TwoCodeActivity extends BaseNoTitleActivity {
    private ImageView mTwoCode;
    private Bitmap mBitmap = null;
    private String city;
    private String userName;
    private String avatar;
    private ImageView mImage_header;
    private TextView mUser_name;
    private TextView mRemark;
    private TextView mCity;
    private Context mContext;
    private TitleBar mTitleBar;
    private TextView mSaveTwoCode;
    private String code;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_code);
        mContext = this;
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        userName = intent.getStringExtra("userName");
        avatar = intent.getStringExtra("avatar");
        code = intent.getStringExtra("code");
        type = intent.getStringExtra("type");
        initView();
        setView();
        addListener();
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSaveTwoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtils.saveImageToGallery(mContext, mBitmap);
            }
        });
    }


    private void setView() {
        mCity.setText(TextUtils.isEmpty(city) ? "" : city);
        mUser_name.setText(TextUtils.isEmpty(userName) ? "" : userName);
        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(mContext)
                    .load( avatar)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            mImage_header.setImageBitmap(bitmap);
                            //
                            final int resize = DisplayUtilX.dip2px(220);
                            NetWorkData netWorkData = new NetWorkData();
                            netWorkData.jiaTwoCode(mContext, code, type, new NetWorkData.AddressInterface() {
                                @Override
                                public void callBack(String result) {
                                    mBitmap = CodeUtils.createImage(result, resize, resize, bitmap);
                                    mTwoCode.setImageBitmap(mBitmap);
                                }
                            });
                            /*mBitmap = CodeUtils.createImage(code, 400, 400, bitmap);
                            mTwoCode.setImageBitmap(mBitmap);*/
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            mImage_header.setImageResource(R.mipmap.default_header);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            mImage_header.setImageResource(R.mipmap.default_header);
                        }
                    });
        } else {
            final int resize = DisplayUtilX.dip2px(220);
            NetWorkData netWorkData = new NetWorkData();
            netWorkData.jiaTwoCode(mContext, code, type, new NetWorkData.AddressInterface() {
                @Override
                public void callBack(String result) {
                    mBitmap = CodeUtils.createImage(result, resize, resize, BitmapFactory.decodeResource(getResources(), R.drawable.notification));
                    mTwoCode.setImageBitmap(mBitmap);
                }
            });
        }
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("二维码名片");
        mTwoCode = (ImageView) findViewById(R.id.twocode);
        mImage_header = (ImageView) findViewById(R.id.image_header);
        mUser_name = (TextView) findViewById(R.id.user_name);
        mCity = (TextView) findViewById(R.id.city);
        mSaveTwoCode = (TextView) findViewById(R.id.save);
        mRemark = (TextView) findViewById(R.id.remark);
        if (type == ConstantNum.DISCUSSION_TYPE) {
            mCity.setVisibility(View.GONE);
            mRemark.setText("扫我加入我们的讨论组");
        }
    }
}
