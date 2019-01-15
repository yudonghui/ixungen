package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends BaseActivity {
    private WebView mWebView;
    private String relateTable;
    private String relateTableId;
    private String title;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_web_view);
        mContext = this;
        Intent intent = getIntent();
        relateTable = intent.getStringExtra("relateTable");
        relateTableId = intent.getStringExtra("relateTableId");
        title = intent.getStringExtra("title");
        mTitleBar.setTitle(TextUtils.isEmpty(title) ? "消息内容" : title);
        mWebView = (WebView) findViewById(R.id.webView);
        addData();
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("relateTable", relateTable);
        params.putString("relateTableId", relateTableId);
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.SYSTEM_SMS_DETAIL_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String info = data.getString("info");
                    String html = "<head><style>img{max-width:100%;}</style></head>" + info;
                    mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
