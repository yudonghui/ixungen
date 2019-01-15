package com.ruihuo.ixungen.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.LaunchAdapter;
import com.ruihuo.ixungen.entity.NewsModel;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruihuo.ixungen.utils.PhotoUtils.PERMISSIONS_REQUEST_FILE;

public class LaunchActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ImageView mImageView;
    private FrameLayout mRelativeLayout;
    private LinearLayout llpoints;
    private ImageView mSkip;
    private ImageView mStartApp;
    private static final int FIRST_LOGIN = 1;
    private static final int NO_FIRST_LOGIN = 2;
    ArrayList<ImageView> mImageList = new ArrayList<>();
    private LaunchAdapter mLaunchAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case FIRST_LOGIN:
                    //如果是第一次登录，就访问让viewpager显示，并网络请求获取引导页面。
                    vpData();
                  /* Intent intent1 = new Intent(LaunchActivity.this, XunGenActivity.class);
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(R.anim.launch_in, R.anim.launch_out);*/
                    break;
                case NO_FIRST_LOGIN:
                    //不是第一次登录，直接跳转到主页面
                    Intent intent = new Intent(LaunchActivity.this, XunGenActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.launch_in, R.anim.launch_out);
                    break;
            }

        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        sp = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        initView();
        //检查登录状态
        checkLoginStatus();
        addListener();
        //百度应用自动更新，动态获取权限
        getPression();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_launch);
        llpoints = (LinearLayout) findViewById(R.id.ll_points);
        mRelativeLayout = (FrameLayout) findViewById(R.id.rl_vp);
        mImageView = (ImageView) findViewById(R.id.image_launch);
        mSkip = (ImageView) findViewById(R.id.image_skip);
        mStartApp = (ImageView) findViewById(R.id.start_app);
        mImageList = new ArrayList<>();
    }

    private int prePosition = 0;

    private void addListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mImageList.size() - 1) {
                    Intent intent = new Intent(LaunchActivity.this, XunGenActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    llpoints.setVisibility(View.GONE);
                    mStartApp.setVisibility(View.VISIBLE);
                } else {
                    llpoints.setVisibility(View.VISIBLE);
                    mStartApp.setVisibility(View.GONE);
                }
                llpoints.getChildAt(prePosition).setBackgroundResource(R.drawable.zhongse);
                llpoints.getChildAt(position).setBackgroundResource(R.drawable.lvse);
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //点击跳过引导页
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, XunGenActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.launch_in, R.anim.launch_out);
            }
        });
        //点击开始体验
        mStartApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, XunGenActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.launch_in, R.anim.launch_out);
            }
        });
    }

    private SharedPreferences sp;

    private void addData() {
        sp = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        //false说明是第一次，
        boolean isFirst = sp.getBoolean("isFirst", true);
        mRelativeLayout.setVisibility(View.GONE);
        // mImageView.setVisibility(View.VISIBLE);
        //mImageView.setBackgroundResource(R.drawable.launch);
        if (isFirst) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message().obtain();
                    msg.what = FIRST_LOGIN;
                    handler.sendMessage(msg);
                }
            }, 1000);
        } else {
            //请求启动图片。
            launchImageData();
            //不是第一次登录，不用引导页面
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message().obtain();
                    msg.what = NO_FIRST_LOGIN;
                    handler.sendMessage(msg);
                }
            }, 2000);
        }

    }

    private void launchImageData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(this);
        Bundle params = new Bundle();
        params.putString("client", "ios");
        params.putString("type", "0");
        mHttp.get(Url.ROLL_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                int height = DisplayUtilX.getDisplayHeight();
                int width = DisplayUtilX.getDisplayWidth();
                NewsModel newsModel = gson.fromJson(result, NewsModel.class);
                if (newsModel.getCode() == 0) {
                    List<NewsModel.DataBean> data = newsModel.getData();
                    Picasso.with(LaunchActivity.this)
                            .load(data.get(0).getPic())
                            .resize(width, height)
                            .placeholder(R.mipmap.launch)
                            .config(Bitmap.Config.RGB_565)
                            .into(mImageView);

                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private int[] resId = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};

    private void vpData() {
        mImageList.clear();
        llpoints.removeAllViews();
        for (int i = 0; i < resId.length; i++) {
            ImageView imageView = new ImageView(LaunchActivity.this);
            imageView.setImageResource(resId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageList.add(imageView);
            View view = new View(LaunchActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtilX.dip2px(15), DisplayUtilX.dip2px(15));
            params.leftMargin = 10;
            if (i == 0) {
                view.setBackgroundResource(R.drawable.lvse);
            } else view.setBackgroundResource(R.drawable.zhongse);
            view.setLayoutParams(params);
            llpoints.addView(view);
        }
        mSkip.setVisibility(View.VISIBLE);
        mRelativeLayout.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
        mLaunchAdapter = new LaunchAdapter(mImageList, LaunchActivity.this);
        mViewPager.setAdapter(mLaunchAdapter);
    }

    private String lnglat = "0,0";//当前所在经纬度

    //检查登录状态
    private void checkLoginStatus() {

        boolean islogin = sp.getBoolean("islogin", false);
        //token的有效截至时间
        long timeToken = sp.getLong("timeToken", 0);
        XunGenApp.token = sp.getString("token", "");
        //如果是登录状态的话直接登录
        if (islogin) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - timeToken >= 0) {
                addData();
                XunGenApp.isLogin = false;
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("islogin", false);
                edit.commit();
                //token失效
               /* final String username = sp.getString("username", "");
                final String password = sp.getString("password", "");
                HttpInterface mHttp = HttpUtilsManager.getInstance(LaunchActivity.this);
                Bundle params = new Bundle();
                params.putString("username", username);
                params.putString("password", password);
                params.putString("lnglat", LocationUtils.getLocation(LaunchActivity.this));
                //type  0动态密码登录, 1常规登录
                params.putString("type", "1");
                mHttp.post(Url.LOGIN_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        addData();
                        Gson gson = GsonUtils.getGson();
                        LoginModel loginModel = gson.fromJson(result, LoginModel.class);
                        LoginModel.DataBean data = loginModel.getData();
                        XunGenApp.isLogin = true;
                        XunGenApp.token = data.getToken().getToken();
                        XunGenApp.rid = data.getRid();
                        XunGenApp.ry_token = data.getRong_yun_token();
                        RongConnect.connect(data.getRong_yun_token(), LaunchActivity.this);
                        if (!"0".equals(data.getRid())) {
                            SharedPreferences userinfo = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit = userinfo.edit();
                            edit.putString("username", username);
                            edit.putString("password", password);
                            edit.putString("token",XunGenApp.token);
                            edit.putLong("timeToken", data.getToken().getExp());
                            edit.putBoolean("islogin", true);
                            edit.commit();
                        }
                    }

                    @Override
                    public void onError(String message) {

                        addData();
                    }
                });*/
            } else {
                //如果没有截至刷新token
                refreshToken();
            }

        } else {
            addData();
        }
    }

    private void refreshToken() {
        HttpInterface mHTTP = HttpUtilsManager.getInstance(LaunchActivity.this);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHTTP.get(Url.REFRESH_TOKEN, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                addData();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String token = data.getString("token");
                    int timeToken = data.getInt("exp");
                    XunGenApp.token = token;
                    XunGenApp.isLogin = true;
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("token", token);
                    edit.putLong("timeToken", timeToken);
                    edit.putBoolean("isLogin", true);
                    edit.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                addData();
            }
        });
    }


    public void getPression() {
        if (ContextCompat.checkSelfPermission(LaunchActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LaunchActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showFilePerDialog();
            else
                ActivityCompat.requestPermissions(LaunchActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
        }
    }

    /**
     * 文件权限提示
     */
    public void showFilePerDialog() {
        new AlertDialog.Builder(LaunchActivity.this)
                .setTitle("提示")
                .setMessage("PhotoDemo需要获取存储文件权限，以确保可以正常保存拍摄或选取的图片。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LaunchActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, PERMISSIONS_REQUEST_FILE);
                    }
                }).create().show();
    }

}
