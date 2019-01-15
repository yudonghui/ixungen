package com.ruihuo.ixungen.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseActivity;

public class ClanSkillDetailActivity extends BaseActivity {

    private String skillName;
    private String info;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_clan_skill_detail);
        Intent intent = getIntent();
        skillName = intent.getStringExtra("skillName");
        info = intent.getStringExtra("info");
        mTitleBar.setTitle(skillName + "");
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        info = info.replace("\r\n", "<br />");
        info = info.replace("/../", "/");
        String html = "<head><style>img{max-width:100%;}</style></head>" + info;
        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }


}
