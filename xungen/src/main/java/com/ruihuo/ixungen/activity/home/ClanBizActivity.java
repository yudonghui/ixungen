package com.ruihuo.ixungen.activity.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.view.TitleBar;

public class ClanBizActivity extends AppCompatActivity {
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_biz);
        initView();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.clanbiz_titlebar);
        mTitleBar.setTitle("宗亲商业");
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
