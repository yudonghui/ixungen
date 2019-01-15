package com.ruihuo.ixungen.activity.useractivity;

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
import com.ruihuo.ixungen.entity.FriendInfoBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.TitleBar;

import io.rong.imkit.RongIM;

/**
 * @ author yudonghui
 * @ date 2017/4/7
 * @ describe May the Buddha bless bug-free！！
 */
public class FriendInfoActivity extends AppCompatActivity {
    private Context mContext;
    // private FriendBean.DataBean contact;
    private CircleImageView mImage_header;
    private TextView mText_name;
    private TextView mText_rid;
    private LinearLayout mLl_sex;
    private TextView mText_sex;
    private LinearLayout mLl_birthday;
    private TextView mText_birthday;
    private LinearLayout mLl_phone;
    private TextView mText_phone;
    private LinearLayout mLlDetrail;
    private LinearLayout mLlImageView;
    //private LinearLayout mLl_twocode;
    private LinearLayout mLl_address;
    private TextView mText_address;
    /*  private String nikename;
      private String rid;
      private String birthday;
      private String phone;*/
    private TextView mSendSms;
    private TextView mDelete;
    private ImageView mImageView;
    private String sex;
    private String phone;
    private String birthday;
    private String rid;
    private String nikeName;
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mContext = this;
        Intent intent = getIntent();
        rid = intent.getStringExtra("rid");
        initView();
        addData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.intitlebar);
        mImage_header = (CircleImageView) findViewById(R.id.image_header);
        mText_name = (TextView) findViewById(R.id.text_name);
        mText_rid = (TextView) findViewById(R.id.text_rid);
        mLl_sex = (LinearLayout) findViewById(R.id.ll_sex);
        mText_sex = (TextView) findViewById(R.id.text_sex);
        mLl_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        mText_birthday = (TextView) findViewById(R.id.text_birthday);
        mLl_phone = (LinearLayout) findViewById(R.id.ll_phone);
        mText_phone = (TextView) findViewById(R.id.text_phone);
        //mLl_twocode = (LinearLayout) findViewById(R.id.ll_twocode);
        mLl_address = (LinearLayout) findViewById(R.id.ll_address);
        mText_address = (TextView) findViewById(R.id.text_address);
        mSendSms = (TextView) findViewById(R.id.send_sms);
        mDelete = (TextView) findViewById(R.id.delete);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mLlDetrail = (LinearLayout) findViewById(R.id.ll_metrail);
        mLlImageView = (LinearLayout) findViewById(R.id.ll_imgview);
        mImage_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlImageView.setVisibility(View.VISIBLE);
            }
        });
        mLlImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlImageView.setVisibility(View.GONE);
            }
        });
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("friendRid", rid);
        mHttp.get(Url.ISFRIEND_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                FriendInfoBean friendInfoBean = gson.fromJson(result, FriendInfoBean.class);
                FriendInfoBean.DataBean data = friendInfoBean.getData();
                PicassoUtils.setImgView(data.getAvatar(), mContext, R.mipmap.default_header, mImage_header);
                PicassoUtils.setImgView(data.getAvatar(), mContext, R.mipmap.default_header, mImageView);
                if (data.getIsFriend() == 0) {
                    //不是好友关系
                    mSendSms.setText("加好友");
                    mSendSms.setVisibility(View.VISIBLE);
                    mSendSms.setOnClickListener(AddFriendListener);
                    mDelete.setVisibility(View.GONE);
                } else if (data.getIsFriend() == 1) {
                    //是好友关系
                    mDelete.setText("删除好友");
                    mDelete.setVisibility(View.VISIBLE);
                    mSendSms.setVisibility(View.VISIBLE);
                    mDelete.setOnClickListener(DeleteFriendListener);
                    mSendSms.setOnClickListener(SendSmsListener);
                } else if (data.getIsFriend() == 2) {
                    //别人申请加好友，待同意
                    mSendSms.setText("通过验证");
                    mSendSms.setVisibility(View.VISIBLE);
                    mSendSms.setOnClickListener(AgreeFriendListener);
                    mDelete.setVisibility(View.GONE);
                }
                //看看是否是在查看自己的信息
                if (XunGenApp.rid.equals(rid)) {
                    mSendSms.setVisibility(View.GONE);
                    mDelete.setVisibility(View.GONE);
                }
                //填充控件
                setView(data);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void setView(FriendInfoBean.DataBean data) {
        nikeName = data.getNikename();
        sex = data.getSex();
        birthday = data.getBirthday();
        phone = data.getPhone();
        String region = data.getRegion();
        mText_address.setText(TextUtils.isEmpty(region) ? "" : region);
        mTitleBar.setTitle(TextUtils.isEmpty(nikeName) ? "好友信息" : nikeName);
        mText_name.setText(TextUtils.isEmpty(nikeName) ? "" : nikeName);
        mText_rid.setText("根号ID：" + (TextUtils.isEmpty(rid) ? "" : rid));
        //性别
        if (!TextUtils.isEmpty(this.sex)) {
            switch (this.sex) {
                case "0":
                    mText_sex.setText("保密");
                    break;
                case "1":
                    mText_sex.setText("男");
                    break;
                case "2":
                    mText_sex.setText("女");
                    break;
            }
        }

        mText_birthday.setText(TextUtils.isEmpty(this.birthday) ? "" : this.birthday);

        mText_phone.setText(TextUtils.isEmpty(this.phone) ? "" : this.phone);

    }


    View.OnClickListener AddFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("toUserRid", rid);
            params.putString("nikename", XunGenApp.nikename);
            // params.putString("",);
            mHttp.post(Url.ADD_FRIEND_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    ToastUtils.toast(mContext, "发送请求成功，等待好友确定");
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    View.OnClickListener AgreeFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("toUserRid", rid);
            params.putString("dealResult", "1");
            mHttp.post(Url.DEAL_FRIEND_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();

                    mDelete.setText("删除好友");
                    mDelete.setVisibility(View.VISIBLE);
                    mSendSms.setText("发送消息");
                    mSendSms.setVisibility(View.VISIBLE);
                    mDelete.setOnClickListener(DeleteFriendListener);
                    mSendSms.setOnClickListener(SendSmsListener);

                    Intent intent = new Intent(ConstantNum.LOGIN_SUCCESS);
                    intent.putExtra("deleteFriend", true);
                    sendBroadcast(intent);
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    View.OnClickListener SendSmsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //启动单聊界面
            RongIM.getInstance().startPrivateChat(mContext, rid, nikeName);
        }
    };
    View.OnClickListener DeleteFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("friendRid", rid);
            mHttp.post(Url.DELETE_FRIEND_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(ConstantNum.LOGIN_SUCCESS);
                    intent.putExtra("deleteFriend", true);
                    sendBroadcast(intent);
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
}
