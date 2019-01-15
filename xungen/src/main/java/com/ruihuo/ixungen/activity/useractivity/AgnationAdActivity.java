package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.entity.AdBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @ author yudonghui
 * @ date 2017/4/6
 * @ describe May the Buddha bless bug-free！！
 */
public class AgnationAdActivity extends BaseActivity {
    //是否是会长，true是 false不是。
    private boolean isPresident;
    private Context mContext;
    private String associationId;
    private TextView mNoData;
    private TextView mText_title;
    private TextView mText_info;
    private TextView mAd_author;
    private TextView mAd_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_agnation_ad);
        mContext = this;
        Intent intent = getIntent();
        isPresident = intent.getBooleanExtra("isPresident", false);
        associationId = intent.getStringExtra("associationId");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("宗亲公告");
        if (isPresident) {
            mTitleBar.mTextRegister.setText("发布");
            mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        } else {
            mTitleBar.mTextRegister.setVisibility(View.GONE);
        }
        mNoData = (TextView) findViewById(R.id.no_data);
        mText_title = (TextView) findViewById(R.id.text_title);
        mText_info = (TextView) findViewById(R.id.text_info);
        mAd_author = (TextView) findViewById(R.id.ad_author);
        mAd_date = (TextView) findViewById(R.id.ad_date);
    }

    // private boolean isEditor=true;
    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAgnationAdActivity.class);
                if (data != null) {
                    String title = data.getTitle();
                    String info = data.getInfo();
                    intent.putExtra("info", info);
                    intent.putExtra("title", title);
                }
                intent.putExtra("associationId", associationId);
                startActivityForResult(intent, 201);
            }
        });
    }

    private AdBean.DataBean data;

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("associationId", associationId);
        mHttp.get(Url.AGNATION_AD_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                AdBean adBean = gson.fromJson(result, AdBean.class);
                data = adBean.getData();
                if (data != null) {
                    mNoData.setVisibility(View.GONE);
                    mText_title.setVisibility(View.VISIBLE);
                    mText_title.setText(TextUtils.isEmpty(data.getTitle()) ? "" : data.getTitle());
                    mText_info.setText(TextUtils.isEmpty(data.getInfo()) ? "" : data.getInfo());
                    mAd_author.setText(TextUtils.isEmpty(data.getTitle()) ? "" : data.getName());
                    mAd_date.setText(TextUtils.isEmpty(data.getCreate_time()) ? "" : data.getCreate_time());
                    mTitleBar.mTextRegister.setText("编辑");
                } else {
                    mText_title.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                    mTitleBar.mTextRegister.setText("发布");
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 201 && resultCode == 301) {
            addData();
        }

    }
}
