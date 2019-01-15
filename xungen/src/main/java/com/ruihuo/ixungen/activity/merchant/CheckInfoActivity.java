package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.ShopsRequestActivity;
import com.ruihuo.ixungen.entity.ShopsDetailBean;
import com.ruihuo.ixungen.utils.PicassoUtils;

public class CheckInfoActivity extends BaseActivity {
    private ShopsDetailBean shopsDetailBean;
    private LinearLayout mLlFailure;
    private TextView mFailureCause;
    private ImageView mImgState;
    private TextView mTextState;
    private TextView mCause;
    private TextView mRealName;
    private TextView mMobile;
    private TextView mCardNum;
    private TextView mCardEndTime;
    private ImageView mCardZhengImg;
    private ImageView mCardFanImg;
    private TextView mBankType;
    private TextView mBankNum;
    private TextView mBankName;
    private TextView mBankAddress;
    private TextView mShopsName;
    private TextView mShopsType;
    private TextView mShopsTime;
    private TextView mEnvirName;
    private TextView mDetailAddress;
    private ImageView mShopsDoorImg;
    private ImageView mShopsInImg;
    private TextView mIntroduce;
    private ImageView mShopsLicense;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_check_info);
        mContext = this;
        shopsDetailBean = (ShopsDetailBean) getIntent().getSerializableExtra("shopsDetailBean");
        initView();
        setView();
    }

    private void initView() {
        mTitleBar.setTitle("等待审核");
        mLlFailure = (LinearLayout) findViewById(R.id.ll_failure);
        mFailureCause = (TextView) findViewById(R.id.failure_cause);
        mImgState = (ImageView) findViewById(R.id.img_state);
        mTextState = (TextView) findViewById(R.id.text_state);
        mCause = (TextView) findViewById(R.id.cause);
        mRealName = (TextView) findViewById(R.id.realName);
        mMobile = (TextView) findViewById(R.id.mobile);
        mCardNum = (TextView) findViewById(R.id.cardNum);
        mCardEndTime = (TextView) findViewById(R.id.cardEndTime);
        mCardZhengImg = (ImageView) findViewById(R.id.cardZhengImg);
        mCardFanImg = (ImageView) findViewById(R.id.cardFanImg);
        mBankType = (TextView) findViewById(R.id.bankType);
        mBankNum = (TextView) findViewById(R.id.bankNum);
        mBankName = (TextView) findViewById(R.id.bankName);
        mBankAddress = (TextView) findViewById(R.id.bankAddress);
        mShopsName = (TextView) findViewById(R.id.shopsName);
        mShopsType = (TextView) findViewById(R.id.shopsType);
        mShopsTime = (TextView) findViewById(R.id.shopsTime);
        mEnvirName = (TextView) findViewById(R.id.envirName);
        mDetailAddress = (TextView) findViewById(R.id.detailAddress);
        mShopsDoorImg = (ImageView) findViewById(R.id.shopsDoorImg);
        mShopsInImg = (ImageView) findViewById(R.id.shopsInImg);
        mIntroduce = (TextView) findViewById(R.id.introduce);
        mShopsLicense = (ImageView) findViewById(R.id.shopsLicense);
    }

    private void setView() {
        ShopsDetailBean.DataBean data = shopsDetailBean.getData();
        /***************申请入驻**********************/
        final String juridical_name = data.getJuridical_name();//法人姓名
        final String mobile = data.getMobile();//手机号
        final String juridical_idno = data.getJuridical_idno();//身份证号码
        final String juridical_idcard_a = data.getJuridical_idcard_a();//身份证正面
        final String juridical_idcard_b = data.getJuridical_idcard_b();//身份证反面

        mRealName.setText("法人真实姓名: " + (TextUtils.isEmpty(juridical_name) ? "--" : juridical_name));
        mMobile.setText("手机号: " + (TextUtils.isEmpty(mobile) ? "" : mobile));
        mCardNum.setText("身份证号码: " + (TextUtils.isEmpty(juridical_idno) ? "" : juridical_idno));
        PicassoUtils.setImgView(juridical_idcard_a, R.mipmap.card_zheng, mContext, mCardZhengImg);
        PicassoUtils.setImgView(juridical_idcard_b, R.mipmap.card_fan, mContext, mCardFanImg);
        /***************绑定银行信息**********************/
        final String card_type = data.getCard_type();//开户类型
        final String open_bank = data.getOpen_bank();//银行卡开户行
        final String bank_card = data.getBank_card();//银行卡号
        final String branch = data.getBranch();//开户支行
        if (!TextUtils.isEmpty(card_type)) {
            mBankType.setText("银行卡类型: " + ("0".equals(card_type) ? "对私账户" : "对公账户"));
        }
        mBankName.setText("开户行名称: " + (TextUtils.isEmpty(open_bank) ? "" : open_bank));
        mBankAddress.setText("开户支行: " + (TextUtils.isEmpty(branch) ? "--" : branch));
        mBankNum.setText("银行卡号码: " + (TextUtils.isEmpty(bank_card) ? "" : bank_card));
        /***************店铺信息**********************/
        final String shop_name = data.getShop_name();//店铺名字

        final String business_type = data.getBusiness_type();//店铺分类
        final String shop_time = data.getShop_time();//营业时间
        final String scenic_area_id = data.getScenic_area_id();//景区id
        final String name = data.getName();//景区名字
        final String address = data.getAddress();//详细地址
        final String region = data.getRegion();//省市区
        final String text = data.getText();//店铺简介
        final String shop_card_a = data.getShop_card_a();
        final String shop_card_b = data.getShop_card_b();
        final String business_license = data.getBusiness_license();//营业执照照片
        mShopsName.setText("店铺名: " + (TextUtils.isEmpty(shop_name) ? "" : shop_name));
        mShopsType.setText("门店分类: " + (TextUtils.isEmpty(business_type) ? "" : business_type));
        mShopsTime.setText("营业时间: " + (TextUtils.isEmpty(shop_time) ? "" : shop_time));
        mEnvirName.setText("所在景区: " + (TextUtils.isEmpty(name) ? "" : name));
        mDetailAddress.setText("详细地址: " + (TextUtils.isEmpty(address) ? "" : address));
        mIntroduce.setText("商家简介: " + (TextUtils.isEmpty(text) ? "" : text));
        PicassoUtils.setImgView(shop_card_a, R.mipmap.door_default, mContext, mShopsDoorImg);
        PicassoUtils.setImgView(shop_card_b, R.mipmap.in_default, mContext, mShopsInImg);
        PicassoUtils.setImgView(business_license, R.mipmap.request_material, mContext, mShopsLicense);

        /******审核状态******/
        final String state = data.getState();
        if ("0".equals(state)) {//0-审核中；1-审核通过，2-审核不通过
            mImgState.setImageResource(R.mipmap.wait_check);
            mTextState.setText("正在审核中");
            mCause.setText("审核期为1-3个工作日。");
            mLlFailure.setVisibility(View.GONE);
            mTitleBar.mTextRegister.setVisibility(View.GONE);
        } else if ("2".equals(state)) {
            mTitleBar.setTitle("审核结果");
            mImgState.setImageResource(R.mipmap.check_stop);
            mTextState.setText("审核未通过");
            mCause.setText("您的申请资料不够完善");
            String failure_cause = data.getFailure_cause();//失败原因
            if (!TextUtils.isEmpty(failure_cause)) {
                mLlFailure.setVisibility(View.VISIBLE);
                mFailureCause.setText(failure_cause);
            } else mLlFailure.setVisibility(View.GONE);
            mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
            mTitleBar.mTextRegister.setText("重新编辑申请");
            mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = getSharedPreferences("merchantInfo", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("mobile", mobile);
                    edit.putString("shopName", shop_name);
                    edit.putString("juridicalName", juridical_name);
                    edit.putString("juridicalIdNo", juridical_idno);
                    edit.putString("juridicalIdcardA", juridical_idcard_a);
                    edit.putString("juridicalIdcardB", juridical_idcard_b);
                    edit.putString("openBank", open_bank);
                    edit.putString("scenicAreaId", scenic_area_id);
                    edit.putString("name", name);
                    edit.putString("address", address);
                    edit.putString("region", region);
                    edit.putString("shopTime", shop_time);
                    edit.putString("businessType", business_type);
                    edit.putString("text", text);
                    edit.putString("branch", branch);
                    edit.putString("cardType", card_type);
                    edit.putString("bankCard", bank_card);
                    edit.putString("shopCardA", shop_card_a);
                    edit.putString("shopCardB", shop_card_b);
                    edit.putString("businessLicense", business_license);
                    edit.commit();
                    Intent intent = new Intent(mContext, ShopsRequestActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
}
