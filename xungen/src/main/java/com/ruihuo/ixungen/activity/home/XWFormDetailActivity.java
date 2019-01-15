package com.ruihuo.ixungen.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.XWCommentAdapter;
import com.ruihuo.ixungen.entity.CommentBaseBeand;
import com.ruihuo.ixungen.entity.XWFormDetailBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.geninterface.XWInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ShareUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.MyListView;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class XWFormDetailActivity extends AppCompatActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private TextView mTitle;
    private TextView mCreattime;
    private TextView mAuthor;
    private TextView mReadTimes;
    private SmartRefreshLayout mRefresh;

    private ScrollView mScrollView;
    private WebView mWebView;
    private LinearLayout mLl_comment;
    private MyListView mListView;
    private LinearLayout mLlEdit;
    private EditText mEtReplyContent;
    private TextView mSendSms;
    private HttpInterface mHttp;
    private String id;
    private List<CommentBaseBeand> commentInfoList = new ArrayList<>();
    private XWCommentAdapter mAdapter;
    private int totalPage;
    private int limit = 10;
    private int page = 1;
    private boolean isComment = true;//是评论文章还是回复评论人.true是评论文章
    private String commentId;
    private String toRid;
    private LoadingDialogUtils loadingDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwform_detail);
        mContext = this;
        id = getIntent().getStringExtra("id");
        initView();
        mAdapter = new XWCommentAdapter(commentInfoList, mContext, 1);
        mListView.setAdapter(mAdapter);
        addData(true);
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.detail_titlebar);
        mTitleBar.setTitle("详情");
        mTitleBar.mShare.setVisibility(View.VISIBLE);
        mTitleBar.mShare.setImageResource(R.mipmap.share);
        mTitle = (TextView) findViewById(R.id.title);
        mCreattime = (TextView) findViewById(R.id.creattime);
        mAuthor = (TextView) findViewById(R.id.author);
        mReadTimes = (TextView) findViewById(R.id.readTimes);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mLl_comment = (LinearLayout) findViewById(R.id.ll_comment);
        mListView = (MyListView) findViewById(R.id.listView);
        mLlEdit = (LinearLayout) findViewById(R.id.ll_editor);
        mEtReplyContent = (EditText) findViewById(R.id.edit_sms);
        mSendSms = (TextView) findViewById(R.id.send_sms);

        mHttp = HttpUtilsManager.getInstance(mContext);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_comment.setOnClickListener(CommentListener);//点击评论
        mEtReplyContent.addTextChangedListener(TextChangeListener);//评论的编辑内容
        mAdapter.setListener(new XWInterface() {
            @Override
            public void callBack(String commentI, String toRi, String nikenam) {
                //弹出键盘
                softControll();
                isComment = false;
                commentId = commentI;
                toRid = toRi;
                mEtReplyContent.setHint("回复" + nikenam);
            }

        });
        mSendSms.setOnClickListener(SendSmsListener);//回复帖子，评论
        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    page++;
                    addData(false);
                }
                {
                    mRefresh.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            mRefresh.finishLoadMore();
                        }
                    }, 1000);
                }
            }

        });
        mTitleBar.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Url.H5_HTTP + "/findroots/questions/question/id/" + id + "/shareId/1";
                ShareUtils shareXGWZ = new ShareUtils(mContext);
                shareXGWZ.share(TextUtils.isEmpty(data.getTitle()) ? "" : data.getTitle(),
                        TextUtils.isEmpty(data.getSummary()) ? "" : data.getSummary(),
                        url);
            }
        });
    }

    private XWFormDetailBean.DataBean data;

    private void addData(final boolean isFirst) {
        Bundle params = new Bundle();
        params.putString("id", id);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.TIEZI_DETAIL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                XWFormDetailBean xwFormDetailBean = gson.fromJson(result, XWFormDetailBean.class);
                data = xwFormDetailBean.getData();
                if (isFirst) {
                    String title = data.getTitle();
                    String create_time = data.getCreate_time();
                    String author = data.getAuthor();
                    String count = data.getCount();
                    String content = data.getContent();
                    mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
                    mAuthor.setText(TextUtils.isEmpty(author) ? "" : author);
                    mReadTimes.setText("阅读 " + (TextUtils.isEmpty(count) ? "" : count));
                    mCreattime.setText("发表于 " + DateFormatUtils.longToDateM(create_time));
                    String html = "<head><style>img{max-width:100%;}</style></head>" + content;
                    mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                }
                commentInfoList.addAll(data.getComment_info());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });
    }

    View.OnClickListener CommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //弹出键盘
            softControll();
            isComment = true;
            mEtReplyContent.setHint("请输入评论内容");
        }

    };

    private void softControll() {
        mLlEdit.setVisibility(View.VISIBLE);
        mEtReplyContent.setFocusable(true);
        mEtReplyContent.setFocusableInTouchMode(true);
        mEtReplyContent.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > DisplayUtilX.getDisplayHeight() / 3)) {
                    mLlEdit.setVisibility(View.VISIBLE);
                    //Toast.makeText(getContext(), "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > DisplayUtilX.getDisplayHeight() / 3)) {
                    mLlEdit.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    TextWatcher TextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                mSendSms.setEnabled(false);
                mSendSms.setTextColor(getResources().getColor(R.color.gray_txt));
                mSendSms.setBackgroundResource(R.drawable.shape_gray_box);
            } else {
                mSendSms.setEnabled(true);
                mSendSms.setTextColor(getResources().getColor(R.color.white));
                mSendSms.setBackgroundResource(R.drawable.shape_btn_bg);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    View.OnClickListener SendSmsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = mEtReplyContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.toast(mContext, "请输入评论内容");
                return;
            }
            if (isComment) {//对帖子直接评论
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("topicId", id);
                params.putString("content", content);
                mHttp.post(Url.SMS_COMMENT, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        commentInfoList.clear();
                        //page=1;
                        addData(false);
                        isComment = true;//恢复到直接评论视频的状态
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mLlEdit.getWindowToken(), 0);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            } else {//回复别人的评论
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("commentId", commentId);
                params.putString("replyContent", content);
                params.putString("toRid", toRid);
                mHttp.post(Url.SMS_REPLY, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        commentInfoList.clear();
                        //page=1;
                        addData(false);
                        isComment = true;//恢复到直接评论视频的状态
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mLlEdit.getWindowToken(), 0);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        }
    };
}
