package com.ruihuo.ixungen.activity.useractivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DataCleanManager;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.VersionInfo;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;

import io.rong.imkit.RongIM;

/**
 * @ author yudonghui
 * @ date 2017/3/31
 * @ describe May the Buddha bless bug-free！！
 */
public class SettingActivity extends BaseActivity {
    private LinearLayout mLl_safe_set;
    private LinearLayout mLl_service_phone;
    private LinearLayout mLl_version;
    private LinearLayout mLl_about_us;
    private TextView mText_quit;
    private Context mContext;
    private TextView mText_version;
    private LinearLayout mLl_cache;
    private TextView mText_cache;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_setting);
        mContext = this;
        initView();
        addListener();
        try {
            mText_cache.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void initView() {
        mTitleBar.setTitle("设置");
        mLl_safe_set = (LinearLayout) findViewById(R.id.ll_safe_set);
        mLl_service_phone = (LinearLayout) findViewById(R.id.ll_service_phone);
        mLl_version = (LinearLayout) findViewById(R.id.ll_version);
        mText_version = (TextView) findViewById(R.id.text_version);
        mLl_cache = (LinearLayout) findViewById(R.id.ll_cache);
        mText_cache = (TextView) findViewById(R.id.text_cache);
        mLl_about_us = (LinearLayout) findViewById(R.id.ll_about_us);
        mText_quit = (TextView) findViewById(R.id.text_quit);
        //版本号
        String appVersionName = VersionInfo.getAppVersionName(mContext);
        int appVersionCode = VersionInfo.getAppVersionCode(mContext);
        mText_version.setText(appVersionName + "-" + appVersionCode);
    }

    private void addListener() {
        mLl_safe_set.setOnClickListener(SetListener);
        mLl_service_phone.setOnClickListener(SetListener);
        mLl_version.setOnClickListener(SetListener);
        mLl_about_us.setOnClickListener(SetListener);
        mText_quit.setOnClickListener(SetListener);
        mLl_cache.setOnClickListener(SetListener);
    }

    View.OnClickListener SetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.ll_safe_set:
                    intent.setClass(mContext, SafeSetActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_service_phone:
                    //服务电话
                    tel();
                    break;
                case R.id.ll_version:

                    break;
                case R.id.ll_about_us:
                    intent.setClass(mContext, AboutUsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.text_quit:
                    //退出登录
                    quit();
                    break;
                case R.id.ll_cache:
                    //清理缓存
                    clearCache();
                    break;
            }
        }
    };

    private void clearCache() {
        DataCleanManager.clearAllCache(mContext);
        try {
            mText_cache.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tel() {
        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
        hintDialogUtils.setMessage("您将拨打客服电话0713-2790279");
        hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
            @Override
            public void callBack(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0713-2790279"));
                if (ActivityCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    private void quit() {
        SharedPreferences sp = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        /*edit.putString("username", "");
        edit.putString("password", "");*/
        edit.putBoolean("islogin", false);
        edit.putString("token","");
        edit.commit();
        XunGenApp.isLogin = false;
        XunGenApp.token = "";
        XunGenApp.rid = "";
        XunGenApp.ry_token = "";
        //融云退出登录
        RongIM.getInstance().logout();
        // LotteryApp.buyPrivilege = 2;
        // LotteryApp.recommendPrivilege = 2;
        //告诉h5页面清理数据
        clanH5Data();
        Intent intent = new Intent();
        setResult(30, intent);
        finish();
    }

    private void clanH5Data() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        mHttp.jsonByUrl(Url.H5_clan, new JsonInterface() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String message) {

            }
        });

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Setting Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
