package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.login.SelectCityActivity;
import com.ruihuo.ixungen.common.PrivateUtils;
import com.ruihuo.ixungen.ui.familytree.bean.StemmaDetailBean;
import com.ruihuo.ixungen.ui.familytree.contract.EditeStemmaInfoContract;
import com.ruihuo.ixungen.ui.familytree.presenter.EditeStemmaInfoPresenter;
import com.ruihuo.ixungen.utils.SelectorDialog;

import java.util.ArrayList;
import java.util.List;

public class EditeStemmaInfoActivity extends BaseActivity implements EditeStemmaInfoContract.View {
    EditeStemmaInfoPresenter mPresenter = new EditeStemmaInfoPresenter(this);
    private Context mContext;
    private EditText mStemmaName;
    private EditText mPhone;
    private TextView mRegion;
    private EditText mGeneration;
    private EditText mSummary;
    private ImageView mIspublic;
    private TextView mCommit;
    private SelectorDialog selectorDialog;
    private List<String> dialogData;
    private String lookPrivate = "9";
    private StemmaDetailBean.DataBean dataBean;
    private String id;
    private PrivateUtils mPrivateUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_edite_stemma_info);
        mContext = this;
        dataBean = (StemmaDetailBean.DataBean) getIntent().getSerializableExtra("dataBean");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("修改家谱简介");
        mStemmaName = (EditText) findViewById(R.id.stemmaName);
        mPhone = (EditText) findViewById(R.id.phone);
        mRegion = (TextView) findViewById(R.id.region);
        mGeneration = (EditText) findViewById(R.id.generation);
        mSummary = (EditText) findViewById(R.id.summary);
        mIspublic = (ImageView) findViewById(R.id.ispublic);
        mCommit = (TextView) findViewById(R.id.commit);

        selectorDialog = new SelectorDialog(mContext);
        dialogData = new ArrayList<>();

        mPrivateUtils = new PrivateUtils();
        if (dataBean != null) {
            id = dataBean.getId();
            String name = dataBean.getName();
            String summary = dataBean.getSummary();
            String phone = dataBean.getPhone();
            region = dataBean.getRegion();
            String generation = dataBean.getGeneration();
            lookPrivate = dataBean.getPrivateX();
            mStemmaName.setText(TextUtils.isEmpty(name) ? "" : name);
            mPhone.setText(TextUtils.isEmpty(phone) ? "" : phone);
            mRegion.setText(TextUtils.isEmpty(region) ? "" : region);
            mGeneration.setText(TextUtils.isEmpty(generation) ? "" : generation);
            mSummary.setText(TextUtils.isEmpty(summary) ? "" : summary);
            mIspublic.setImageResource("9".equals(lookPrivate) ? R.drawable.btn_on : R.drawable.btn_off);
        }
    }


    private void addListener() {
        mRegion.setOnClickListener(AddressListener);
        mIspublic.setOnClickListener(LookPrivateListener);
        mCommit.setOnClickListener(CommitListener);
    }

    View.OnClickListener AddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 101);
        }
    };
    View.OnClickListener LookPrivateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("9".equals(lookPrivate)) {
                lookPrivate = "1";
                mIspublic.setImageResource(R.drawable.btn_off);
            } else {
                lookPrivate = "9";
                mIspublic.setImageResource(R.drawable.btn_on);
            }
        }
    };
    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String stemmaName = mStemmaName.getText().toString();
            String phone = mPhone.getText().toString();
            String generation = mGeneration.getText().toString();
            String summary = mSummary.getText().toString();

            Bundle parameters = new Bundle();
            parameters.putString("token", XunGenApp.token);
            parameters.putString("id", id);
            if (!TextUtils.isEmpty(stemmaName))
                parameters.putString("name", stemmaName);
            if (!TextUtils.isEmpty(phone))
                parameters.putString("phone", phone);
            if (!TextUtils.isEmpty(region))
                parameters.putString("region", region);
            if (!TextUtils.isEmpty(generation))
                parameters.putString("generation", generation);
            if (!TextUtils.isEmpty(summary))
                parameters.putString("summary", summary);
            if (!TextUtils.isEmpty(lookPrivate))
                parameters.putString("private", lookPrivate);
            mPresenter.getData(parameters, mContext);
        }
    };


    private void addData() {

    }

    private String region;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            region = data.getStringExtra("cityName");
            mRegion.setText(TextUtils.isEmpty(data.getStringExtra("cityName")) ? "--" : data.getStringExtra("cityName"));
        }
    }

    @Override
    public void getDataSuccess(String result) {
        Intent intent = new Intent();
        setResult(6667, intent);
        finish();
    }

    @Override
    public void getDataError(String error) {

    }
}
