package com.ruihuo.ixungen.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.RegisterBean;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.ClickUtils;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.LocationUtils;
import com.ruihuo.ixungen.utils.NetworkUtils;
import com.ruihuo.ixungen.utils.RongConnect;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.wheelview.ChooseCityInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseCityUtil;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateUtil;
import com.ruihuo.ixungen.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

public class UserInfoActivity extends AppCompatActivity {

    private TitleBar mTitleBar;
    //姓
    private EditText mEtSurname;
    //昵称
    private TextView mNickname;
    //籍贯地
    private TextView mRootAddress;
    //现居住地
    private TextView mCurrAddress;
    //下一步
    private TextView mNext;
    String currAddress;
    String rootAddress;
    private Context mContext;
    private String phone;
    // private String loginLat;
    // private String loginLng;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //第一个是经度，第二个维度
        location = LocationUtils.getLocation(this);
        phone = getIntent().getStringExtra("phone");
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.titlBar);
        mTitleBar.setTitle("个人信息");
        mEtSurname = (EditText) findViewById(R.id.et_surname);
        mNickname = (TextView) findViewById(R.id.et_nickname);
        mRootAddress = (TextView) findViewById(R.id.text_user_rootaddress);
        mCurrAddress = (TextView) findViewById(R.id.text_user_address);
        mNext = (TextView) findViewById(R.id.text_next);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击选择祖籍
        mRootAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        //点击选取所在城市
        mCurrAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chooseCityDialog();
                Intent intent = new Intent(UserInfoActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, 101);
            }
        });
        //点击完成提交
        mNext.setOnClickListener(CommitListener);
    }

    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //防止连续点击全部玩法的时候跳出来多个框
            if (ClickUtils.isFastClickLong()) {
                return;
            }
            final String surname = mEtSurname.getText().toString().trim();
            final String nickname = mNickname.getText().toString().trim();
            if (TextUtils.isEmpty(surname)) {
                ToastUtils.toast(mContext, "姓氏不能为空");
                return;
            }
            if (TextUtils.isEmpty(nickname)) {
                ToastUtils.toast(mContext, "昵称不能为空");
                return;
            }
            if (TextUtils.isEmpty(rootAddress)) {
                ToastUtils.toast(mContext, "祖籍不能为空");
                return;
            }
            if (TextUtils.isEmpty(currAddress)) {
                ToastUtils.toast(mContext, "所在地不能为空");
                return;
            }
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(UserInfoActivity.this);
            Bundle params = new Bundle();
            params.putString("surname", surname);
            params.putString("region", rootAddress);
            params.putString("city", currAddress);
            params.putString("nikename", nickname);
            params.putString("phone", phone);
            params.putString("lnglat", location);
            String loginIp = NetworkUtils.getIPAddress();
            if (!TextUtils.isEmpty(loginIp)) {
                params.putString("loginIp", loginIp);
            }
            mHttp.post(Url.USER_INFO_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    Gson gson = GsonUtils.getGson();
                    RegisterBean registerBean = gson.fromJson(result, RegisterBean.class);
                    MobclickAgent.onProfileSignIn(registerBean.getRid());//友盟统计---账号的统计
                    XunGenApp.rid = registerBean.getRid();
                    XunGenApp.token = registerBean.getToken().getToken();
                    XunGenApp.ry_token = registerBean.getRong_yun_token();
                    XunGenApp.isLogin = true;
                    //  XunGenApp.surname = registerBean.getData().getName();
                    RongConnect.connect(XunGenApp.ry_token, mContext);
                    NetWorkData netWorkData = new NetWorkData();
                    netWorkData.getByName(mContext);
                    //存token.token截至日期
                    SharedPreferences userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = userinfo.edit();
                    edit.putString("token", XunGenApp.token);
                    edit.putBoolean("islogin", true);
                    edit.putLong("timeToken", registerBean.getToken().getExp());
                    edit.commit();
                    //-1 姓氏不存在，0 姓氏存在，1 姓氏正在审核，2 姓氏非法（审核不通过）
                    String data = registerBean.getData();
                    if ("0".equals(data)) {
                        Intent intent = new Intent(UserInfoActivity.this, SetPasswordActivity.class);
                        intent.putExtra("region", rootAddress);
                        intent.putExtra("city", currAddress);
                        intent.putExtra("phone", phone);
                        intent.putExtra("nickname", nickname);
                        startActivity(intent);
                        finish();
                    } else {
                        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                        switch (data) {
                            case "-1":
                                hintDialogUtils.setMessage("您的姓氏不存在，部分功能待审核完成后才能使用！");
                                break;
                            case "1":
                                hintDialogUtils.setMessage("您的姓氏正在审核，部分功能待审核完成后才能使用！");
                                break;
                            case "2":
                                hintDialogUtils.setMessage("您的姓氏未通过审核，无法使用跟姓氏相关的功能！");
                                break;
                        }
                        hintDialogUtils.setVisibilityCancel();
                        hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                Intent intent = new Intent(UserInfoActivity.this, SetPasswordActivity.class);
                                intent.putExtra("region", rootAddress);
                                intent.putExtra("city", currAddress);
                                intent.putExtra("phone", phone);
                                intent.putExtra("nickname", nickname);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };

    //Choose 出生日期
    public void chooseDateDialog() {
        final ChooseDateUtil dateUtil = new ChooseDateUtil();
        int[] oldDateArray = {2017, 01, 01};
        dateUtil.createDialog(this, oldDateArray, new ChooseDateInterface() {
            @Override
            public void sure(int[] newDateArray) {
                // mEtUserBirth.setText(newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2]);
            }
        });
    }

    //Choose city
    public void chooseCityDialog() {
        final ChooseCityUtil cityUtil = new ChooseCityUtil();
        String[] oldCityArray = {"上海", "上海", "浦东新区"};
        cityUtil.createDialog(this, oldCityArray, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                //mEtUserAddress.setText(newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            currAddress = data.getStringExtra("cityName");
            mCurrAddress.setText(currAddress);
        } else if (requestCode == 100 && resultCode == 323) {
            //点击籍贯所在地，返回的值
            rootAddress = data.getStringExtra("cityName");
            mRootAddress.setText(rootAddress);
        }
    }
}
