package com.ruihuo.ixungen.activity.chatactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.view.TitleBar;

public class ConversationListActivity extends FragmentActivity {
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.conversationlist_titlebar);

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
