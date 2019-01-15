package com.ruihuo.ixungen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.view.TitleBar;
public class BaseActivity extends AppCompatActivity {
    public TitleBar mTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // PushAgent.getInstance(this).onAppStart();
    }

    public void initContentView(int layoutResID) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_content);
        mTitleBar = (TitleBar) findViewById(R.id.titlebar_base);
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View inflate = View.inflate(this, layoutResID, null);
        linearLayout.addView(inflate);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, R.anim.slide_out_right);
    }*/
}
