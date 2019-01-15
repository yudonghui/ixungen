package com.ruihuo.ixungen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.entity.ArticleDetailBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @ author yudonghui
 * @ date 2017/3/28
 * @ describe May the Buddha bless bug-free！！
 */
public class ArticleDetailActivity extends BaseActivity {
    private String from;
    private String id;
    private String author;
    private WebView mWebView;
    private TextView mAuthor;
    private TextView mReadedNum;
    private TextView mCreatTime;
    //新闻不显示，帖子显示
    private LinearLayout mLlComment;
    private TextView mOrder;
    private LinearLayout mLlWriteComment;
    private ListView mListView;
    // private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_article_detail);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        author = intent.getStringExtra("author");
        from = intent.getStringExtra("from");
        initView();
        // mContent = (TextView) findViewById(R.id.content);
        addData();
    }

    private void initView() {
        mTitleBar.setTitle("帖子正文");
        mWebView = (WebView) findViewById(R.id.wv_content);
        mAuthor = (TextView) findViewById(R.id.author);
        mCreatTime = (TextView) findViewById(R.id.creattime);
        mReadedNum = (TextView) findViewById(R.id.readed_num);

        mLlComment = (LinearLayout) findViewById(R.id.ll_comment);
        mOrder = (TextView) findViewById(R.id.order);
        mLlWriteComment = (LinearLayout) findViewById(R.id.write_comment);
        mListView = (ListView) findViewById(R.id.lv_comment);
        if ("news".equals(from)) {
            //从新闻界面跳转过来
            mLlComment.setVisibility(View.GONE);
        } else {
            mLlComment.setVisibility(View.VISIBLE);
        }
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(this);
        HttpInterface mHttp = HttpUtilsManager.getInstance(this);
        Bundle params = new Bundle();
        params.putString("id", id);
        mHttp.get(Url.NEWS_DETAIL_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                ArticleDetailBean articleDetailBean = gson.fromJson(result, ArticleDetailBean.class);
                if (articleDetailBean.getCode() == 0) {
                    ArticleDetailBean.DataBean dataBean = articleDetailBean.getData();
                    mAuthor.setText(dataBean.getAuthor());
                    mReadedNum.setText("阅读：" + dataBean.getCount());
                    String create_time = dataBean.getCreate_time();
                    mCreatTime.setText(DateFormatUtils.longToDate(Long.parseLong(create_time + "000")));
                    String content = dataBean.getContent();
                    String html = "<head><style>img{max-width:100%;}</style></head>" + content;
                    mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                } else {

                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
