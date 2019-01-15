package com.ruihuo.ixungen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ruihuo.ixungen.R;

public class PictureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_picture);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView mWebView= (WebView) findViewById(R.id.webview_picture);
        if(!TextUtils.isEmpty(url)){
            mWebView.loadUrl(url);
            mWebView.getSettings().setJavaScriptEnabled(true);
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }
}
