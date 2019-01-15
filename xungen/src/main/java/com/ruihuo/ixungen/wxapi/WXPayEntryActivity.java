package com.ruihuo.ixungen.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.ConstantNum;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;



public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, ConstantNum.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent();
            intent.setAction(ConstantNum.PAYMENT_SUCEESS);
            intent.putExtra("isSuccess",1);
            sendBroadcast(intent);
            finish();
        }else {
            Intent intent = new Intent();
            intent.setAction(ConstantNum.PAYMENT_SUCEESS);
            intent.putExtra("isSuccess",2);
            sendBroadcast(intent);
            finish();
        }
    }
}