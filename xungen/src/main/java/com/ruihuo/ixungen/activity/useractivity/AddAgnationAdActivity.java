package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @ author yudonghui
 * @ date 2017/4/10
 * @ describe May the Buddha bless bug-free！！
 */
public class AddAgnationAdActivity extends BaseActivity {
    private EditText mTitle;
    private EditText mInfo;
    private Context mContext;
    private String associationId;
    private String title;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_add_agnation_ad);
        mContext = this;
        Intent intent = getIntent();
        associationId = intent.getStringExtra("associationId");
        title = intent.getStringExtra("title");
        info = intent.getStringExtra("info");
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("发布公告");
        mTitleBar.mTextRegister.setText("保存");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitle = (EditText) findViewById(R.id.et_title);
        mInfo = (EditText) findViewById(R.id.et_info);
        boolean empty = TextUtils.isEmpty(title);
        if (!TextUtils.isEmpty(title)) mTitle.setText(title);
        if (!TextUtils.isEmpty(info)) mInfo.setText(info);
    }

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mTitle.getText().toString();
                info = mInfo.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    ToastUtils.toast(mContext, "标题不能为空");
                } else if (TextUtils.isEmpty(info)) {
                    ToastUtils.toast(mContext, "公告内容不能为空");
                } else {
                    final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("title", title);
                    params.putString("info", info);
                    params.putString("associationId", associationId);
                    params.putString("token", XunGenApp.token);
                    mHttp.post(Url.AGNATION_AD_ADD_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                            Intent intent = new Intent();
                            setResult(301, intent);
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                }
            }
        });
    }

}
