package com.ruihuo.ixungen.activity.genxin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.ActionSmsDetailBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.TitleBar;

/**
 * @ author yudonghui
 * @ date 2017/4/25
 * @ describe May the Buddha bless bug-free！！
 */
public class ActionSmsDetailActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private TextView mAction_hint;
    private TextView mAction_name;
    private TextView mAction_time;
    private TextView mAction_address;
    private TextView mAction_joinNum;
    private TextView mQuit_action;
    private LinearLayout mLl_action;
    private Context mContext;
    //活动id
    private String relateTableId;
    private String relateTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_sms_detail);
        mContext = this;
        Intent intent = getIntent();
        relateTableId = intent.getStringExtra("relateTableId");
        relateTable = intent.getStringExtra("relateTable");
        initView();
        addData();
        addListener();
    }


    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.action_titlebar);
        mTitleBar.setTitle("活动确认");
        mAction_hint = (TextView) findViewById(R.id.action_hint);
        mAction_name = (TextView) findViewById(R.id.action_name);
        mAction_time = (TextView) findViewById(R.id.action_time);
        mAction_address = (TextView) findViewById(R.id.action_address);
        mAction_joinNum = (TextView) findViewById(R.id.action_joinNum);
        mQuit_action = (TextView) findViewById(R.id.quit_action);
        mLl_action = (LinearLayout) findViewById(R.id.ll_action);

    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mQuit_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("activityId", relateTableId);
                params.putString("token", XunGenApp.token);
                mHttp.post(Url.ACTION_QUIT_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });
        mLl_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, H5Activity.class);
                intent.putExtra("from", ConstantNum.ACITON_DETAIL);
                startActivity(intent);
            }
        });
    }

    private ActionSmsDetailBean.DataBean dataBean;

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        // params.putString("relateTable",relateTable);
        params.putString("id", relateTableId);
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.ACTION_SMS_DETAIL_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                ActionSmsDetailBean actionSmsDetailBean = gson.fromJson(result, ActionSmsDetailBean.class);
                dataBean = actionSmsDetailBean.getData();
                setView();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setView() {
        //0 参与，1取消
        if ("0".equals(dataBean.getStatus())) {
            if (XunGenApp.rid.equals(dataBean.getRid())) {
                mAction_hint.setText("您发起的活动已确认");
                mQuit_action.setVisibility(View.VISIBLE);
            } else {
                mAction_hint.setText("您的报名已确认");
                mQuit_action.setVisibility(View.VISIBLE);
            }
        } else if ("1".equals(dataBean.getStatus())) {
            if (XunGenApp.rid.equals(dataBean.getRid())) {
                mAction_hint.setText("您已取消活动");
            } else mAction_hint.setText("您的报名已取消");

            mQuit_action.setVisibility(View.GONE);
        } else {
            mAction_hint.setText("未参加本活动");
            mQuit_action.setVisibility(View.GONE);
        }

        mAction_name.setText(TextUtils.isEmpty(dataBean.getName()) ? "" : dataBean.getName());
        String start_time = dataBean.getStart_time();
        String end_time = dataBean.getEnd_time();

        mAction_time.setText(DateFormatUtils.longToDateM(start_time) + "——" + DateFormatUtils.longToDateM(end_time));

        //mAction_address.setText(TextUtils.isEmpty(dataBean.getAddress()) ? "" : dataBean.getAddress());
        mAction_joinNum.setText(TextUtils.isEmpty(dataBean.getCount()) ? "0人" : (dataBean.getCount() + "人"));
        String address = dataBean.getAddress();
        if (!TextUtils.isEmpty(address)) {
            NetWorkData netWorkData = new NetWorkData();
            netWorkData.getAddress(mContext, address, new NetWorkData.AddressInterface() {
                @Override
                public void callBack(String address) {
                    mAction_address.setText(TextUtils.isEmpty(address) ? "" : address);
                }
            });
        }
    }

}
