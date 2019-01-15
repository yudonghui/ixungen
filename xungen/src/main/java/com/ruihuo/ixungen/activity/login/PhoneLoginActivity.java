package com.ruihuo.ixungen.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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
import com.ruihuo.ixungen.observer.SmsObserver;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.LocationUtils;
import com.ruihuo.ixungen.utils.LogUtils;
import com.ruihuo.ixungen.utils.RongConnect;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import io.rong.imkit.RongIM;

public class PhoneLoginActivity extends AppCompatActivity {
    private SmsObserver mObserver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final int MSG_RECEIVED_CODE = 1;
    private TextView mObtainCode;
    private EditText mCode;
    private TitleBar mTitleBar;
    private TextView mPasswordLogin;
    private EditText mPhone;
    private TextView mLogin;
    private TextView mRegister;
    private Context mContext;
    private SharedPreferences userinfo;
    // private boolean isRegister;
    private int time = 60;
    private String lnglat = "0,0";//当前所在经纬度
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECEIVED_CODE:
                    String code = (String) msg.obj;
                    mCode.setText(code);
                    break;

            }
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        mContext = this;
        // isRegister=getIntent().getBooleanExtra("isRegister",false);
        //获取当前的地理位置信息
        lnglat = LocationUtils.getLocation(this);
        initView();
        mObserver = new SmsObserver(this, mHandler);
        //获取读取权限的方法。目前没有做好
        // testCall();
        addListener();
        //动态设置获取设备号的权限。
        permissionDevice();

    }

    public void testCall() {
        /*
         * 检查是否有读取短信的权限，如果授权了直接注册一个观察者，用于监控短信，
         * 否则申请获取权限。在onRequestPermissionsResult回调
         * */

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    1);
        } else {
            Uri uri = Uri.parse("content://sms");
            getContentResolver().registerContentObserver(uri, true, mObserver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.parse("content://sms");
                getContentResolver().registerContentObserver(uri, true, mObserver);
            } else {
                // Permission Denied
                // Toast.makeText(PhoneLoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode == ConstantNum.REQUEST_DEVICEID) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限请求成功的操作
                //获取设备权限
                deviceIdToken();
            } else {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.TitlBar);
        mTitleBar.setTitle("登录");
        mObtainCode = (TextView) findViewById(R.id.text_obtain_code);
        mCode = (EditText) findViewById(R.id.edit_code);
        mPasswordLogin = (TextView) findViewById(R.id.password_login);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mLogin = (TextView) findViewById(R.id.text_login);
        mRegister = (TextView) findViewById(R.id.text_register);
        mPhone.setText(userinfo.getString("phone", ""));
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
        //获取验证码
        mObtainCode.setOnClickListener(ObtainCodeListener);
        //密码登录
        mPasswordLogin.setOnClickListener(PasswordListener);
        //点击登录按钮
        mLogin.setOnClickListener(LoginListener);
        /*//点击新用户注册
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });*/
    }

    View.OnClickListener PasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PhoneLoginActivity.this, PasswordLoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener LoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String phone = mPhone.getText().toString().trim();
            final String code = mCode.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(PhoneLoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(PhoneLoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(code)) {
                Toast.makeText(PhoneLoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
           /* Intent intent = new Intent(PhoneLoginActivity.this, UserInfoActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
*/

            HttpInterface mHttp = HttpUtilsManager.getInstance(PhoneLoginActivity.this);
            Bundle params = new Bundle();
            params.putString("username", phone);
            params.putString("password", code);
            params.putString("lnglat", lnglat);
            //type  0动态密码登录, 1常规登录
            params.putString("type", "0");
            params.putString("timeStamp", System.currentTimeMillis() / 1000 + "");
           /* params.putString("username", "wayking");
            params.putString("password", "wayling");
            params.putString("lnglat", "116.403875,39.915168");
            //type  0动态密码登录, 1常规登录
            params.putString("type", "1");
            params.putString("timeStamp", "1999999999");*/
            mHttp.post(Url.LOGIN_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject data1 = jsonObject.getJSONObject("data");
                        int rid = data1.getInt("rid");
                        SharedPreferences.Editor edit = userinfo.edit();
                        edit.putString("phone", phone);
                        //根号为0说明是第一次登录。
                        if (rid == 0) {

                            Intent intent = new Intent(PhoneLoginActivity.this, UserInfoActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);

                        } else {
                            Gson gson = GsonUtils.getGson();
                            LoginModel loginModel = gson.fromJson(result, LoginModel.class);
                            LoginModel.DataBean data = loginModel.getData();
                            XunGenApp.isLogin = true;
                            XunGenApp.token = data.getToken().getToken();
                            //存token.以及本次登陆的时间
                            edit.putString("token", XunGenApp.token);
                            edit.putBoolean("islogin", true);
                            edit.putLong("timeToken", data.getToken().getExp());
                            new NetWorkData().getUserInfo(mContext, XunGenApp.token);//获取用户信息
                            MobclickAgent.onProfileSignIn(data.getRid());//友盟统计---账号的统计
                            XunGenApp.rid = data.getRid();
                            XunGenApp.ry_token = data.getRong_yun_token();
                            RongConnect.connect(data.getRong_yun_token(), mContext);
                            Intent intent = new Intent();
                            intent.setAction(ConstantNum.LOGIN_SUCCESS);
                            sendBroadcast(intent);
                        }
                        finish();
                        edit.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
                    }
                        /*SharedPreferences userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor edit = userinfo.edit();
                        edit.putString("username",phone);
                        edit.putString("password",password);
                        edit.putBoolean("islogin",true);
                        edit.commit();*/
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    View.OnClickListener ObtainCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.toast(mContext, "请输入手机号");
                return;
            } else if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(PhoneLoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            time = 60;
            mObtainCode.setTextColor(getResources().getColor(R.color.again_code_txt));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (time == 0) {
                        mObtainCode.setText("重新获取");
                        mObtainCode.setEnabled(true);
                        return;
                    } else {
                        time--;
                        mObtainCode.setText("重新获取(" + time + ")");
                        mObtainCode.setEnabled(false);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
            //网络请求
            HttpInterface mHttpInterface = HttpUtilsManager.getInstance(PhoneLoginActivity.this);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            //format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            long l = System.currentTimeMillis();
            String date1 = format.format(l);
            String md5 = StringUtil.getMD5(phone + "0");
            String addDate = md5 + date1;
            String sign = StringUtil.getMD5(addDate);
            //String sign=StringUtil.toUpperCase(md51);
            Bundle params = new Bundle();
            params.putString("phone", phone);
            params.putString("msg", "0");
            params.putString("sign", sign);
            mHttpInterface.get(Url.SMS_CODE_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };

    private void deviceIdToken() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String deviceId = TelephonyMgr.getDeviceId();
        LogUtils.e("设备id", deviceId);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", deviceId);
        mHttp.get(Url.YUNTOKEN_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    RongConnect.connect(data, mContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void permissionDevice() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_PHONE_STATE))
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, ConstantNum.REQUEST_DEVICEID);
            else
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, ConstantNum.REQUEST_DEVICEID);
        } else
            deviceIdToken();
    }

}
