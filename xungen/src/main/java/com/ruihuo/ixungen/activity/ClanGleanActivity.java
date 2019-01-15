package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

public class ClanGleanActivity extends BaseActivity {
    private Context mContext;
    private TextView mContent;
    private TextView mDetail;
    private TextView mAgree;
    private TextView mRefund;
    private String clanId;
    private String content;
    /**
     * 1,普通用户，2，商户
     * 宗亲会邀请：被邀请时消息flag=0,拒绝时flag=1
     * 家谱邀请：邀请中flag=0，拒绝flag=1，同意flag=2
     */
    private String flag;
    private String smsId;
    private String userRid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_clan_glean);
        mContext = this;
        clanId = getIntent().getStringExtra("clanId");
        content = getIntent().getStringExtra("content");
        flag = getIntent().getStringExtra("flag");
        smsId = getIntent().getStringExtra("smsId");
        userRid = getIntent().getStringExtra("userRid");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("邀请通知");
        mContent = (TextView) findViewById(R.id.content);
        mDetail = (TextView) findViewById(R.id.detail);
        mAgree = (TextView) findViewById(R.id.agree);
        mRefund = (TextView) findViewById(R.id.refund);
    }

    private void addListener() {
        mDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StemmaDetailActivity.class);
                intent.putExtra("familyId", clanId);
                intent.putExtra("flag", false);
                startActivity(intent);
            }
        });
    }

    private void addData() {
        switch (flag) {
            case "0":
                mRefund.setVisibility(View.VISIBLE);
                mAgree.setVisibility(View.VISIBLE);
                mRefund.setOnClickListener(RefundListener);//拒绝
                mAgree.setOnClickListener(AgreeListener);//同意
                break;
            case "1":
                mRefund.setVisibility(View.VISIBLE);
                mAgree.setVisibility(View.GONE);
                mRefund.setText("已拒绝加入");
                break;
            case "2":
                mRefund.setVisibility(View.GONE);
                mAgree.setVisibility(View.VISIBLE);
                mAgree.setText("进入家谱");
                mAgree.setOnClickListener(AgnationListener);
                break;
        }
    }

    View.OnClickListener RefundListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("dealResult", "2");
            params.putString("id", smsId);
            mHttp.post(Url.CLAN_GLEAN_DEAL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    Intent intent = new Intent();
                    setResult(2222, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    View.OnClickListener AgreeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("dealResult", "1");
            params.putString("id", smsId);
            mHttp.post(Url.CLAN_GLEAN_DEAL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    Intent intent = new Intent();
                    setResult(2222, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    View.OnClickListener AgnationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            intent.putExtra("familyId", clanId);
            intent.putExtra("flag", false);
            startActivity(intent);
        }
    };
}
