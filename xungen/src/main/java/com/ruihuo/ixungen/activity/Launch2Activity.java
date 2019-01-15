package com.ruihuo.ixungen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.NewsModel;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.VersionInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Launch2Activity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ImageView mImageView;
    private FrameLayout mRelativeLayout;
    private LinearLayout llpoints;
    private ImageView mSkip;
    private ImageView mStartApp;
    private static final int FIRST_LOGIN = 1;
    private static final int NO_FIRST_LOGIN = 2;
    ArrayList<ImageView> mImageList = new ArrayList<>();
    private SharedPreferences sp;
    private LaunchAdapter mLaunchAdapter;
    private Context mContext;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;
            switch (what) {
                case FIRST_LOGIN:
                    //如果是第一次登录，就访问让viewpager显示，并网络请求获取引导页面。
                    vpData();
                    break;
                case NO_FIRST_LOGIN:
                    //不是第一次登录，直接跳转到主页面
                    NetWorkData netWorkData = new NetWorkData();
                    netWorkData.getRecommend(Launch2Activity.this, 1);
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
        mContext = this;
        sp = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        initView();
        //检查登录状态
        checkLoginStatus();
        addListener();
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
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("versionCode", VersionInfo.getAppVersionCode(mContext));
                edit.commit();
                NetWorkData netWorkData = new NetWorkData();
                netWorkData.getRecommend(Launch2Activity.this, 1);
            }
        });
        //点击开始体验
        mStartApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("versionCode", VersionInfo.getAppVersionCode(mContext));
                edit.commit();
                NetWorkData netWorkData = new NetWorkData();
                netWorkData.getRecommend(Launch2Activity.this, 1);
            }
        });
    }

    private void addData() {
        mRelativeLayout.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        sp = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        int versionCode = sp.getInt("versionCode", 0);
        String token = sp.getString("token", "");
        new NetWorkData().getUserInfo(mContext, token);
        //如果版本号和存起来的版本号不同，那么就有引导页
        if (versionCode != VersionInfo.getAppVersionCode(mContext)) {
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
            }, 1500);
        }
    }

    private int[] resId = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};

    private void vpData() {
        mImageList.clear();
        llpoints.removeAllViews();
        for (int i = 0; i < resId.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(resId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageList.add(imageView);
            View view = new View(mContext);
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
        mLaunchAdapter = new LaunchAdapter(mImageList, mContext);
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
            } else {
                //如果没有截至刷新token
                refreshToken();
            }

        } else {
            addData();
        }
    }

    private void launchImageData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(this);
        Bundle params = new Bundle();
        params.putString("client", "android");
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
                    Picasso.with(Launch2Activity.this)
                            .load(data.get(0).getPic())
                            .resize(width, height)
                            // .placeholder(R.mipmap.launch)
                            .config(Bitmap.Config.RGB_565)
                            .into(mImageView);

                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void refreshToken() {
        HttpInterface mHTTP = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHTTP.get(Url.REFRESH_TOKEN, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
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
                    addData();
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

}
