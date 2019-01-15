package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class VerifyOrderActivity extends BaseNoTitleActivity {
    private Context mContext;
    private ImageView mImgBack;
    private ImageView mCode;
    private EditText mOrderNo;
    private TextView mConfirm;
    private static final int PERMISSION_CODE = 230;
    private static final int REQUEST_CODE = 231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_order);
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mCode = (ImageView) findViewById(R.id.code);
        mOrderNo = (EditText) findViewById(R.id.orderNo);
        mConfirm = (TextView) findViewById(R.id.confirm);
    }

    private void addListener() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE);
                } else {
                    //调取二维码扫描
                    Intent intent = new Intent(getApplication(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String orderNo = mOrderNo.getText().toString();
                if (TextUtils.isEmpty(orderNo)) {
                    Toast.makeText(mContext, "请输入订单号号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("orderNo", orderNo);
                mHttp.post(Url.CONSUME_ORDER, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        Intent intent = new Intent(mContext, UnusedOrderActivity.class);
                        intent.putExtra("orderNo", orderNo);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(mContext, "消费完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });

            }
        });
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        try {
            switch (requestCode) {
                case PERMISSION_CODE:
                    //调取扫描二维码界面
                    Intent intent = new Intent(getApplication(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);//扫描出来的结果
                    NetWorkData netWorkData = new NetWorkData();
                    netWorkData.jieTwoCode(mContext, result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(mContext, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
