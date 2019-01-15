package com.ruihuo.ixungen.activity.chatactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.view.TitleBar;

public class SubConversationListActivtiy extends AppCompatActivity {
    private TitleBar mTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_conversation_list_activtiy);
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.subconversationlist_titlebar);

    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
