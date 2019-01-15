package com.ruihuo.ixungen.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.LoginModel;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.RongConnect;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.rong.imkit.RongIM;

public class PasswordLoginActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private TextView mPhoneLogin;
    private EditText mPhone;
    private EditText mPassword;
    private TextView mForgetPassword;
    //private TextView mRegister;
    private TextView mLogin;
    private String lnglat = "0,0";//当前所在经纬度
    private LocationManager locationManager;
    private Context mContext;
    private SharedPreferences userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        mContext = this;
        //获取当前所在的位置
        getLocation();
        initView();
        addListener();
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.setAction(ConstantNum.LOGIN_SUCCESS);
            sendBroadcast(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.TitlBar);
        mTitleBar.setTitle("登录");
        mPhoneLogin = (TextView) findViewById(R.id.phone_login);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mPassword = (EditText) findViewById(R.id.edit_password);
        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        mLogin = (TextView) findViewById(R.id.text_login);
        mPhone.setText(userinfo.getString("userName", ""));
        TextView mKefu = (TextView) findViewById(R.id.kefu);
        mKefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取设备id，并获取融云token
                //启动单聊界面
                RongIM.getInstance().startPrivateChat(mContext, ConstantNum.SERVICE, "寻根客服");
            }
        });
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ConstantNum.LOGIN_SUCCESS);
                sendBroadcast(intent);
                finish();
            }
        });
        //采用手机号码登录
        mPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordLoginActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //点击登录按钮
        mLogin.setOnClickListener(LoginListener);
        //忘记密码
        mForgetPassword.setOnClickListener(ForgetPasswordListener);
    }

    View.OnClickListener ForgetPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener LoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String phone = mPhone.getText().toString().trim();
            final String password = mPassword.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(PasswordLoginActivity.this, "请输入手机号/根号", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(PasswordLoginActivity.this, "请输入登录密码", Toast.LENGTH_SHORT).show();
                return;
            }
            //
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(PasswordLoginActivity.this);
            Bundle params = new Bundle();
            params.putString("username", phone);
            params.putString("password", password);
            params.putString("lnglat", lnglat);
            //type  0动态密码登录, 1常规登录
            params.putString("type", "1");
            mHttp.post(Url.LOGIN_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject data1 = jsonObject.getJSONObject("data");
                        int rid = data1.getInt("rid");
                        MobclickAgent.onProfileSignIn(rid + "");//友盟统计---账号的统计
                        SharedPreferences.Editor edit = userinfo.edit();
                        edit.putString("userName", phone);
                        //根号为0说明是第一次登录。
                        if (rid == 0) {
                            Intent intent = new Intent(mContext, UserInfoActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        } else {
                            Gson gson = GsonUtils.getGson();
                            LoginModel loginModel = gson.fromJson(result, LoginModel.class);
                            LoginModel.DataBean data = loginModel.getData();
                            XunGenApp.isLogin = true;
                            XunGenApp.token = data.getToken().getToken();
                            XunGenApp.rid = data.getRid();
                            XunGenApp.ry_token = data.getRong_yun_token();
                            RongConnect.connect(data.getRong_yun_token(), mContext);
                            //存token.以及本次登陆的时间
                            edit.putString("token", XunGenApp.token);
                            edit.putLong("timeToken", data.getToken().getExp());
                            edit.putBoolean("islogin", true);
                            edit.putString("loginMethod", "password");
                            new NetWorkData().getUserInfo(mContext, XunGenApp.token);//获取用户信息
                            Intent intent = new Intent();
                            intent.setAction(ConstantNum.LOGIN_SUCCESS);
                            sendBroadcast(intent);
                        }
                        finish();
                        edit.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };

    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获取到位置服务
        //判断是否有可用的内容提供器
        String provider = judgeProvider(locationManager);
        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                //得到当前经纬度
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                lnglat = latitude + "," + longitude;
            }
        } else {//不存在位置提供器的情况
            // Toast.makeText(this, "无法获取当前位置", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否有可用的内容提供器
     *
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            return null;
        }

    }
}
