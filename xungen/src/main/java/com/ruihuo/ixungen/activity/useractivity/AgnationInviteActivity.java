package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.ruihuo.ixungen.R.id.detail;

public class AgnationInviteActivity extends BaseActivity {
    private ImageView mAvatar;
    private TextView mContent;
    private TextView mDetail;
    private TextView mAgree;
    private TextView mRefund;
    private String agnationId;
    private Context mContext;
    private AgnationFormBean.DataBean dataBean;
    private String content;
    private String flag;//被邀请时消息flag=0,拒绝时flag=1
    private String smsId;
    private String userRid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_agnation_invite);
        mContext = this;
        agnationId = getIntent().getStringExtra("agnationId");
        content = getIntent().getStringExtra("content");
        flag = getIntent().getStringExtra("flag");
        smsId = getIntent().getStringExtra("smsId");
        userRid = getIntent().getStringExtra("userRid");
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mTitleBar.setTitle("邀请通知");
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mContent = (TextView) findViewById(R.id.content);
        mDetail = (TextView) findViewById(detail);
        mAgree = (TextView) findViewById(R.id.agree);
        mRefund = (TextView) findViewById(R.id.refund);
    }

    private void addData() {

        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("id", agnationId);
        mHttp.get(Url.AGNATION_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = new Gson();
                AgnationFormBean agnationFormBean = gson.fromJson(result, AgnationFormBean.class);
                List<AgnationFormBean.DataBean> data = agnationFormBean.getData();
                String status = data.get(0).getStatus();//0待审核 1审核通过  2审核不通过
                if ("1".equals(status)) {
                    dataBean = data.get(0);
                    setView();
                } else {
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    hintDialogUtils.setMessage("宗亲会正在审核中，请耐心等待！");
                    hintDialogUtils.setVisibilityCancel();
                    hintDialogUtils.setCancelable(false);
                    hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                        @Override
                        public void callBack(View view) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
        if ("0".equals(flag)) {
            Bundle bundle = new Bundle();
            bundle.putString("token", XunGenApp.token);
            bundle.putString("id", agnationId);
            mHttp.get(Url.AGNATION_ISJOIN, bundle, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String data = jsonObject.getString("data");
                        if (data != null)
                            joinStatus(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {

                }
            });
        } else {
            mRefund.setVisibility(View.VISIBLE);
            mAgree.setVisibility(View.GONE);
            mRefund.setText("已拒绝加入");
        }
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(mContext, AgnationActivity.class);
                Intent intent = new Intent(mContext, AgnationNewActivity.class);
                intent.putExtra("id", agnationId);
                startActivity(intent);
            }
        });
    }

    private void joinStatus(String joinStatus) {
        switch (joinStatus) {//-1：没有加入；0：待宗亲会会长审核；1：审核通过；2：审核不通过
            case "-1":
                mRefund.setVisibility(View.VISIBLE);
                mAgree.setVisibility(View.VISIBLE);
                mRefund.setOnClickListener(RefundListener);//拒绝
                mAgree.setOnClickListener(AgreeListener);//同意
                break;
            case "0":
                mRefund.setVisibility(View.GONE);
                mAgree.setVisibility(View.VISIBLE);
                mAgree.setText("待宗亲会会长审核");
                break;
            case "1":
                mRefund.setVisibility(View.GONE);
                mAgree.setVisibility(View.VISIBLE);
                mAgree.setText("进入宗亲会");
                mAgree.setOnClickListener(AgnationListener);
                break;
            case "2":
                mRefund.setVisibility(View.VISIBLE);
                mAgree.setVisibility(View.GONE);
                mRefund.setText("已拒绝加入");
                break;
        }
    }

    private void setView() {
        if (dataBean == null) {
            return;
        }
        String img_url = dataBean.getImg_url();
        PicassoUtils.setImgView(img_url, mContext, R.mipmap.default_header_clan, mAvatar);
        mContent.setText(TextUtils.isEmpty(content) ? "--" : content);
    }

    View.OnClickListener RefundListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("associationId", agnationId);
            params.putString("userRid", userRid);
            params.putString("id", smsId);
            mHttp.post(Url.REFUSE_JOIN, params, new JsonInterface() {
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
            params.putString("associationId", agnationId);
            params.putString("userRid", XunGenApp.rid);
            mHttp.post(Url.JOIN_AGNATION_URL, params, new JsonInterface() {
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
           // Intent intent = new Intent(mContext, AgnationActivity.class);
            Intent intent = new Intent(mContext, AgnationNewActivity.class);
            intent.putExtra("id", agnationId);
            startActivity(intent);
        }
    };


}
