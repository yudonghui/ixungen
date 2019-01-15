package com.ruihuo.ixungen.activity.home;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.view.CommonVideoView;

public class RootMediaActivity extends AppCompatActivity {
    private CommonVideoView mVideoView;
    private long curPosition;
    private String path = "http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com%20/D046015255134077DDB3ACA0D7E68D45.flv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_media);
        mVideoView = (CommonVideoView) findViewById(R.id.common_videoView);
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        curPosition = sp.getLong(path, 0);
        Log.e("当前位置", curPosition + "");
        mVideoView.start(path,curPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVideoView.setFullScreen();
        } else {
            mVideoView.setNormalScreen();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //如果是横屏就变成竖屏，否则就退出当前页面
            mVideoView.screen();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        long currentPosition = mVideoView.getCurrentPosition();
        editor.putLong(path, currentPosition);
        editor.commit();
    }
}
