package com.ruihuo.ixungen.action;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;

public class CommentActivity extends BaseActivity {
    private Context mContext;
    private EditText mContent;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_comment);
        pid = getIntent().getStringExtra("pid");
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("评论");
        mTitleBar.mTextRegister.setText("提交");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mContent = (EditText) findViewById(R.id.content);
    }

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(ConfirmListener);
    }

    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = mContent.getText().toString();
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("content", content);
            params.putString("pid", pid);
            mHttp.post(Url.ADD_COMMENT, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
}
