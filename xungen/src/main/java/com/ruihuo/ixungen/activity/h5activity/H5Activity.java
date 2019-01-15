package com.ruihuo.ixungen.activity.h5activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.action.ActionUserInfoActivity;
import com.ruihuo.ixungen.activity.genxin.JoinActionFormActivity;
import com.ruihuo.ixungen.activity.genxin.PostsSmsActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.entity.PayResult;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.LogUtils;
import com.ruihuo.ixungen.utils.PaymentUtils;
import com.ruihuo.ixungen.utils.ShareUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.VersionInfo;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateUtil;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruihuo.ixungen.R.id.webView;
import static com.ruihuo.ixungen.XunGenApp.rid;

public class H5Activity extends AppCompatActivity {
    private FrameLayout mRlTitleBar;
    private ImageView mTitleBack;
    private TextView mTextTitle;
    private ImageView mShare;
    private ImageView mMore;
    private ImageView mSearch;
    private ImageView mFamily;
    private TextView mTextEdit;
    private ImageView mImageEdit;
    private TextView mTextBuild;
    private TextView mTextCommit;

    private View inflate;
    private PopupWindow mPopupWindow;
    private TextView mPopup_question;
    private View mFirstline;
    private TextView mPopup_smshint;
    private TextView mPopup_share;
    private View mSecondline;
    private TextView mPopup_look;

    private ImageView mNoData;
    private WebView mWebView;
    private int from;
    private Context mContext;
    LoadingDialogUtils loadingDialogUtils;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    private Uri imageUri;
    private String url = "";
    //宗亲id
    private String associationId;
    private LoadingDialogUtils loading;
    private boolean isToken = false;//是否调用了请求token的方法
    private boolean isRid = false;//是否调用了请求rid的方法。
    private IntentSkip intentSkip;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mWebView.loadUrl("javascript:goback()");
                    break;
                case 2:
                    String obj = (String) msg.obj;
                    if ("dismiss".equals(obj)) {
                        finish();
                    } else if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else finish();
                    break;
                case 3:
                    //跳转帖子消息界面
                    Intent intent = new Intent(mContext, PostsSmsActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    //寻根问祖的分享
                    ShareUtils shareXGWZ = new ShareUtils(mContext);
                    MessageBean messageBean = (MessageBean) msg.obj;
                    shareXGWZ.share(messageBean.getTitle(), "寻根问祖--寻根溯源,传承中华文化,发现姓氏来源!", messageBean.getUrl());
                    break;
                case 5:
                    //宗亲活动
                    ShareUtils shareZZHD = new ShareUtils(mContext);
                    MessageBean messageZZHD = (MessageBean) msg.obj;
                    shareZZHD.share(messageZZHD.getTitle(), "宗亲活动--寻根溯源,传承中华文化,发现姓氏来源!", messageZZHD.getUrl());
                    break;
                case 6:
                    //分享家谱链接
                    ShareUtils shareTree = new ShareUtils(mContext);
                    shareTree.share("我的家谱", "快来寻根网创建属于自己的家谱吧", (String) msg.obj);
                    break;
                case 7:
                    //分享宗亲互助详情链接
                    ShareUtils shareZZHZ = new ShareUtils(mContext);
                    MessageBean messageZZHZ = (MessageBean) msg.obj;
                    shareZZHZ.share(messageZZHZ.getTitle(), "宗亲互助--寻根溯源,传承中华文化,发现姓氏来源!", messageZZHZ.getUrl());
                    break;
                case 8:
                    //分享新闻详情
                    ShareUtils shareNews = new ShareUtils(mContext);
                    MessageBean messageNews = (MessageBean) msg.obj;
                    shareNews.share(messageNews.getTitle(), "寻根溯源,传承中华文化,发现姓氏来源!", messageNews.getUrl());
                    break;
                case 9:
                    //调取日期
                    MessageBean messageDate = (MessageBean) msg.obj;
                    chooseDateDialog(messageDate.getUrl(), messageDate.getTitle());
                    break;
                case 10:
                    //分享视频
                    ShareUtils shareVideo = new ShareUtils(mContext);
                    MessageBean messageVideo = (MessageBean) msg.obj;
                    shareVideo.share(TextUtils.isEmpty(messageVideo.getTitle()) ? "" : messageVideo.getTitle(),
                            TextUtils.isEmpty(messageVideo.getContent()) ? "" : messageVideo.getContent(), messageVideo.getUrl());
                    break;
                case 11:
                    //是否显示导航栏
                    int arg1 = msg.arg1;
                    if (arg1 == 1) mRlTitleBar.setVisibility(View.VISIBLE);
                    else mRlTitleBar.setVisibility(View.GONE);
                    break;
                case 12:
                    //左边导航栏是否显示
                    mTitleBack.setVisibility(View.VISIBLE);
                    break;
                case 13:
                    //导航栏中间显示的文字
                    String title = (String) msg.obj;
                    mTextTitle.setText(TextUtils.isEmpty(title) ? "" : title);
                    break;
                case 14:
                    //跳转个人中心
                    MessageBean messageInfo = (MessageBean) msg.obj;
                    Intent intentInfo = new Intent(mContext, ActionUserInfoActivity.class);
                    intentInfo.putExtra("rid", messageInfo.getUrl());
                    intentInfo.putExtra("actionId", messageInfo.getTitle());
                    startActivity(intentInfo);
                    break;
                case 15:
                    String rotate = (String) msg.obj;
                    switch (rotate) {
                        case "1":
                            //正常拿着竖屏
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "2":
                            //旋转180度
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "3":
                            //右转横屏
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            break;
                        case "4":
                            //左转横屏
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }
                    break;
                case 16:
                    String[] strings = (String[]) msg.obj;
                    for (int i = 0; i < strings.length; i++) {
                        switch (strings[i]) {
                            case "1":
                                mShare.setVisibility(View.VISIBLE);
                                break;
                            case "2":
                                mSearch.setVisibility(View.VISIBLE);
                                break;
                            case "3":
                                mImageEdit.setVisibility(View.VISIBLE);
                                break;
                            case "4":
                                mMore.setVisibility(View.VISIBLE);
                                break;
                            case "5":
                                mTextCommit.setVisibility(View.VISIBLE);
                                break;
                            case "6":
                                mTextBuild.setVisibility(View.VISIBLE);
                                break;
                            case "7":
                                mTextEdit.setVisibility(View.VISIBLE);
                                break;
                            case "8":
                                mFamily.setVisibility(View.VISIBLE);
                                break;
                            case "":
                                break;
                        }
                    }
                    break;
                case 17:
                    if (XunGenApp.isLogin) {
                        mWebView.loadUrl("javascript:getTokenFormApp('" + XunGenApp.token + "')");
                    } else {
                        if (!isRid && !isToken) {
                            intentSkip.skipLogin(mContext, 1111);
                            isToken = true;
                        }
                    }
                    break;
                case 18:
                    if (XunGenApp.isLogin) {
                        mWebView.loadUrl("javascript:getRidFormApp('" + XunGenApp.rid + "')");
                    } else {
                        if (!isRid && !isToken) {
                            intentSkip.skipLogin(mContext, 2222);
                            isRid = true;
                        }
                    }
                    break;
                case 19:
                    String string = (String) msg.obj;
                    if ("1".equals(string)) {
                        //我的提问，消息提示
                        mPopup_question.setVisibility(View.VISIBLE);
                        mPopup_smshint.setVisibility(View.VISIBLE);
                    } else if ("2".equals(string)) {
                        //分享现有家谱，查看现有家谱
                        mPopup_share.setVisibility(View.VISIBLE);
                        mPopup_look.setVisibility(View.VISIBLE);
                    }
                    mPopupWindow.showAsDropDown(mMore, -DisplayUtilX.dip2px(130), 0);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    getWindow().setAttributes(lp);
                    break;
                case 20:
                    //暂时不用
                    int isRotate = msg.arg1;
                    if (isRotate == 1) {
                        //支持横竖屏

                    } else {
                        //不支持横竖屏

                    }
                    break;
                case 21:
                    String isShow = (String) msg.obj;
                    if ("1".equals(isShow)) {
                        if (loading == null)
                            loading = new LoadingDialogUtils(mContext);
                        if (!loading.isShowing()) loading.show();
                    } else loading.setDimiss();
                    break;
                case 22:
                    String appVersionName = VersionInfo.getAppVersionName(mContext);
                    if (!TextUtils.isEmpty(appVersionName)) {
                        mWebView.loadUrl("javascript:getVersionFormApp('" + appVersionName + "')");
                    } else ToastUtils.toast(mContext, "获取版本号失败");
                    break;
                case 23:
                    MessageBean messageBean1 = (MessageBean) msg.obj;
                    String url = messageBean1.getUrl();
                    String title1 = messageBean1.getTitle();
                    String content = messageBean1.getContent();
                    String imgUrl = messageBean1.getImgUrl();
                    ShareUtils share = new ShareUtils(mContext);
                    if (!TextUtils.isEmpty(url)) {
                        share.share(url, TextUtils.isEmpty(title1) ? "" : title1, TextUtils.isEmpty(content) ? "" : content);
                    }
                    break;
                case 24:

                    MessageBean payMessage = (MessageBean) msg.obj;
                    String goodsName = payMessage.getUrl();
                    String orderNo = payMessage.getTitle();
                    String allMoney = payMessage.getContent();
                    String payMothed = payMessage.getImgUrl();
                    if ("0".equals(payMothed)) {//0微信，1，支付宝
                        paymentUtils.wxpay(allMoney, orderNo);
                    } else {
                        paymentUtils.alipay(allMoney, orderNo, goodsName);
                    }
                    break;
                case 25:
                    ShareUtils shareUtils = new ShareUtils(mContext);
                    MessageBean message = (MessageBean) msg.obj;
                    shareUtils.shareSelf(message.getUrl(), message.getTitle(), message.getContent(), message.getImgUrl(), message.getType());
                    break;
                case PaymentUtils.SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();

                        mWebView.loadUrl("javascript:finishPay('" + 1 + "')");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        mWebView.loadUrl("javascript:finishPay('" + 2 + "')");
                    }
                    break;
            }
        }
    };
    private String newsId;
    private String videoId;
    private PaymentUtils paymentUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        mContext = this;
        intentSkip = new IntentSkip();
        Intent intent = getIntent();
        from = intent.getIntExtra("from", 0);
        associationId = intent.getStringExtra("associationId");
        newsId = intent.getStringExtra("newsId");
        url = intent.getStringExtra("url");
        videoId = intent.getStringExtra("videoId");
        initView();
        addData();
        addListener();
        registerBoradcastReceiver();
    }

    private void addListener() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }

            //<3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            //>3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            //>4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        });
        //点击导航栏上面的按钮
        mTitleBack.setOnClickListener(TitleListener);
        mShare.setOnClickListener(TitleListener);
        mMore.setOnClickListener(TitleListener);
        mSearch.setOnClickListener(TitleListener);
        mFamily.setOnClickListener(TitleListener);
        mImageEdit.setOnClickListener(TitleListener);
        mTextEdit.setOnClickListener(TitleListener);
        mTextBuild.setOnClickListener(TitleListener);
        mTextCommit.setOnClickListener(TitleListener);
        //下拉弹框
        mPopup_question.setOnClickListener(TitleListener);
        mPopup_smshint.setOnClickListener(TitleListener);
        mPopup_look.setOnClickListener(TitleListener);
        mPopup_share.setOnClickListener(TitleListener);
    }

    private void initView() {
        mRlTitleBar = (FrameLayout) findViewById(R.id.rl_title);
        mTitleBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mShare = (ImageView) findViewById(R.id.share);
        mMore = (ImageView) findViewById(R.id.more);
        mSearch = (ImageView) findViewById(R.id.search);
        mFamily = (ImageView) findViewById(R.id.family);
        mTextEdit = (TextView) findViewById(R.id.text_edit);
        mImageEdit = (ImageView) findViewById(R.id.image_edit);
        mTextBuild = (TextView) findViewById(R.id.text_build);
        mTextCommit = (TextView) findViewById(R.id.text_commit);

        mNoData = (ImageView) findViewById(R.id.no_data);
        mWebView = (WebView) findViewById(webView);
        WebSettings settings = mWebView.getSettings();
        String ua = settings.getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + " xungenandroid/" + VersionInfo.getAppVersionName(mContext));//便于WEB端统计分析

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);

        settings.setTextSize(WebSettings.TextSize.NORMAL);//webview字体大小设置
        JavaScriptInterface app = new JavaScriptInterface(this);
        mWebView.addJavascriptInterface(app, "app"); // 设置js接口  第一个参数事件接口实例，第二个是实例在js中的别名，这个在js中会用到

        inflate = View.inflate(mContext, R.layout.popup_window, null);

        mPopup_question = (TextView) inflate.findViewById(R.id.popup_question);
        mFirstline = (View) inflate.findViewById(R.id.firstline);
        mPopup_smshint = (TextView) inflate.findViewById(R.id.popup_smshint);
        mPopup_share = (TextView) inflate.findViewById(R.id.popup_share);
        mSecondline = (View) inflate.findViewById(R.id.secondline);
        mPopup_look = (TextView) inflate.findViewById(R.id.popup_look);

        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(DisplayUtilX.dip2px(150));
        mPopupWindow.setHeight(DisplayUtilX.dip2px(100));
        mPopupWindow.setContentView(inflate);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOnDismissListener(poponDismissListener);
        paymentUtils = new PaymentUtils(mContext, handler);
    }

    private void addData() {

        switch (from) {
            case ConstantNum.MYQUESTION:
                //我的提问
                url = Url.H5_MYQUESTION_URL + rid + "/token/" + XunGenApp.token;
                break;
            case ConstantNum.ARTICLE_DETAIL:
                //新闻详情
                url = Url.H5_ARTICLE_DETAIL_URL + newsId + "/token/" + XunGenApp.token;
                break;
            case ConstantNum.VIDEO_PLAY:
                //视频播放
                url = Url.H5_VIDEO + videoId;
                break;
            default:
                break;
        }
        Map extraHeaders = new HashMap();
        extraHeaders.put("Referer", "https://api.ixungen.cn");
        mWebView.loadUrl(url, extraHeaders);
        LogUtils.e("第一次进来的网址", url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                LogUtils.e("h5中的网址", url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器

                PayTask payTask = new PayTask((Activity) mContext);
                boolean isAplipay = payTask.payInterceptorWithUrl(url, true, new H5PayCallback() {
                    @Override
                    public void onPayResult(H5PayResultModel h5PayResultModel) {
                            /*
                            * 返回码，标识支付状态，含义如下：
                            * 9000——订单支付成功
                            * 8000——正在处理中
                            * 4000——订单支付失败
                            * 5000——重复请求
                            * 6001——用户中途取消
                            * 6002——网络连接出错
                            * */
                        final String resultCode = h5PayResultModel.getResultCode();
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (resultCode) {
                                    case "9000":
                                        Toast.makeText(mContext, "订单支付成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "8000":
                                        Toast.makeText(mContext, "正在处理中", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "4000":
                                        Toast.makeText(mContext, "订单支付失败", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "5000":
                                        Toast.makeText(mContext, "重复请求", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "6001":
                                        Toast.makeText(mContext, "用户中途取消", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "6002":
                                        Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }
                });
                if (!isAplipay) {
                    Map extraHeaders = new HashMap();
                    extraHeaders.put("Referer", "https://api.ixungen.cn");
                    if (url.startsWith("weixin://")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } else view.loadUrl(url, extraHeaders);


                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.e("onPageStarted--url", url);
                if (loadingDialogUtils == null)
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                if (!loadingDialogUtils.isShowing()) {
                    loadingDialogUtils.show();
                }
                // mWebView.loadUrl("javascript:xungeninit()");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.e("onPageFinished--url", url);
                if (loadingDialogUtils.isShowing()) {
                    loadingDialogUtils.setDimiss();
                }
                mFamily.setVisibility(View.GONE);
                mImageEdit.setVisibility(View.GONE);
                mTextEdit.setVisibility(View.GONE);
                mTextCommit.setVisibility(View.GONE);
                mTextBuild.setVisibility(View.GONE);
                mShare.setVisibility(View.GONE);
                mSearch.setVisibility(View.GONE);
                mMore.setVisibility(View.GONE);
                mTitleBack.setVisibility(View.VISIBLE);
                mWebView.loadUrl("javascript:xungeninit()");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.GONE);
                mNoData.setVisibility(View.VISIBLE);
                loadingDialogUtils.setDimiss();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                loadingDialogUtils.setDimiss();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                loadingDialogUtils.setDimiss();
            }
        });
    }

    PopupWindow.OnDismissListener poponDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            handler.sendEmptyMessage(1);
        }
        return super.onKeyDown(keyCode, event);
    }

    class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void goback(String dismiss) {
            LogUtils.e("H5调用APP", "goback(String dismiss)");
            Message message = Message.obtain();
            message.what = 2;
            message.obj = dismiss;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void goback() {
            LogUtils.e("H5调用APP", "goback()");
            Message message = Message.obtain();
            message.what = 2;
            message.obj = "";
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void message() {
            //跳转APP帖子消息
            LogUtils.e("H5调用APP", "message");
            handler.sendEmptyMessage(3);
        }

        @JavascriptInterface
        public void sharefindroot(String url, String title) {
            LogUtils.e("H5调用APP", "sharefindroot");
            //寻根问祖的分享
            Message message = Message.obtain();
            message.what = 4;
            MessageBean messageBean = new MessageBean(Url.H5_HTTP + url, title);
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void shareactivity(String url, String title) {
            LogUtils.e("H5调用APP", "shareactivity");
            //宗亲活动分享
            Message message = Message.obtain();
            message.what = 5;
            MessageBean messageBean = new MessageBean(Url.H5_HTTP + url, title);
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void sharegenealogy(String url) {
            LogUtils.e("H5调用APP", "sharegenealogy");
            //我的家谱的分享
            Message message = Message.obtain();
            message.what = 6;
            message.obj = Url.H5_HTTP + url;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void shareclanhelp(String url, String title) {
            LogUtils.e("H5调用APP", "shareclanhelp");
            //宗亲互助的分享
            Message message = Message.obtain();
            message.what = 7;
            MessageBean messageBean = new MessageBean(Url.H5_HTTP + url, title);
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void Participantslist(String activityId) {
            LogUtils.e("H5调用APP", "Participantslist");
            //查看活动参与人列表
            Intent intent = new Intent(mContext, JoinActionFormActivity.class);
            intent.putExtra("activityId", activityId);
            startActivity(intent);
            //handler.sendEmptyMessage(8);
        }

        @JavascriptInterface
        public void sharenews(String url, String title) {
            LogUtils.e("H5调用APP", "sharenews");
            //分享新闻详情
            Message message = Message.obtain();
            message.what = 8;
            MessageBean messageBean = new MessageBean(Url.H5_HTTP + url, title);
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void showDatePicker(String date, String second) {
            LogUtils.e("H5调用APP", "showDatePicker");
            //调用日期控件
            Message message = Message.obtain();
            message.what = 9;
            MessageBean messageBean = new MessageBean(date, second);
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void shareVideo(String url, String title, String content) {
            LogUtils.e("H5调用APP", "shareVideo");
            MessageBean messageBean = new MessageBean(url, title, content);
            Message message = Message.obtain();
            message.what = 10;
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void showNaviBar(boolean isShowTitler) {
            LogUtils.e("H5调用APP", "showNaviBar");
            Message message = Message.obtain();
            message.what = 11;
            if (isShowTitler) message.arg1 = 1;
            else message.arg1 = 0;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void showLeftItem() {
            LogUtils.e("H5调用APP", "showLeftItem");
            handler.sendEmptyMessage(12);
        }

        @JavascriptInterface
        public void naviItemTitle(String title) {
            LogUtils.e("H5调用APP", "naviItemTitle");
            Message message = Message.obtain();
            message.what = 13;
            message.obj = title;
            handler.sendMessage(message);

        }

        @JavascriptInterface
        public void goUserSpace(String rid, String actionId) {
            LogUtils.e("H5调用APP", "goUserSpace");
            MessageBean messageBean = new MessageBean(rid, actionId);
            Message message = Message.obtain();
            message.what = 14;
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void deviceRotating(String rotate) {
            LogUtils.e("H5调用APP", "deviceRotating");
            Message message = Message.obtain();
            message.obj = rotate;
            message.what = 15;
            handler.sendMessage(message);

        }

        @JavascriptInterface
        public void showRightItem(String[] strings) {
            LogUtils.e("H5调用APP", "showRightItem");
            Message message = Message.obtain();
            message.what = 16;
            message.obj = strings;
            handler.sendMessage(message);

        }

        @JavascriptInterface
        public void getToken() {
            LogUtils.e("H5调用APP", "getToken");
            handler.sendEmptyMessage(17);
        }

        @JavascriptInterface
        public void getRid() {
            LogUtils.e("H5调用APP", "getRid");
            handler.sendEmptyMessage(18);
        }

        @JavascriptInterface
        public void showChoose(String string) {
            LogUtils.e("H5调用APP", "showChoose");
            Message message = Message.obtain();
            message.what = 19;
            message.obj = string;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void canRotating(boolean isRotate) {
            LogUtils.e("H5调用APP", "canRotating");
            int isRotat;
            if (isRotate) isRotat = 1;
            else isRotat = 0;

            Message message = Message.obtain();
            message.what = 20;
            message.arg1 = isRotat;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void showLoading(String isShow) {
            LogUtils.e("H5调用APP", "showLoading");
            Message message = Message.obtain();
            message.what = 21;
            message.obj = isShow;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void getVersion() {
            LogUtils.e("H5调用APP", "getVersion");
            handler.sendEmptyMessage(22);
        }

        @JavascriptInterface
        public void shareInfo(String url, String title, String content, String imgUrl) {
            LogUtils.e("H5调用APP", "shareInfo");
            MessageBean messageBean = new MessageBean(url, title, content, imgUrl);
            Message message = Message.obtain();
            message.what = 23;
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void orderPay(String name, String orderNo, String allMoney, int payMethod) {
            LogUtils.e("H5调用APP", "orderPay");
            MessageBean messageBean = new MessageBean(name, orderNo, allMoney, payMethod + "");
            Message message = Message.obtain();
            message.what = 24;
            message.obj = messageBean;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void shareInfoWithType(String linkUrl, String title, String content, String imgUrl, String type) {
            MessageBean messageBean = new MessageBean();
            messageBean.setUrl(linkUrl);
            messageBean.setTitle(title);
            messageBean.setContent(content);
            messageBean.setImgUrl(imgUrl);
            messageBean.setType(type);
            Message message = Message.obtain();
            message.what = 25;
            message.obj = messageBean;
            handler.sendMessage(message);
        }
    }

    public void chooseDateDialog(String birthday, final String second) {
        final ChooseDateUtil dateUtil = new ChooseDateUtil();
        if (TextUtils.isEmpty(birthday)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birthday = dateFormat.format(new Date());
        }
        String[] split = birthday.split("\\-");
        int[] oldDateArray = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        dateUtil.createDialog(this, oldDateArray, new ChooseDateInterface() {
            @Override
            public void sure(int[] newDateArray) {
                String birthday = newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2];
                mWebView.loadUrl("javascript:setDate('" + birthday + "','" + second + "')");
            }
        });
    }

    class MessageBean implements Serializable {
        private String title;
        private String content;
        private String url;
        private String imgUrl;
        private String type;

        public MessageBean() {
        }

        public MessageBean(String url, String title) {
            this.title = title;
            this.url = url;
        }

        public MessageBean(String url, String title, String content) {
            this.title = title;
            this.url = url;
            this.content = content;
        }

        public MessageBean(String url, String title, String content, String imgUrl) {
            this.url = url;
            this.title = title;
            this.content = content;
            this.imgUrl = imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                Log.e("result", result + "");
                if (result == null) {
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                    Log.e("imageUri", imageUri + "");
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }
    }

    @SuppressWarnings("null")
    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    View.OnClickListener TitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_titlebar_back:
                    mWebView.loadUrl("javascript:goback()");
                    break;
                case R.id.share:
                    mWebView.loadUrl("javascript:shareforxungen()");
                    break;
                case R.id.more:
                    mWebView.loadUrl("javascript:formore()");
                    break;
                case R.id.search:
                    mWebView.loadUrl("javascript:searchforxungen()");
                    break;
                case R.id.image_edit:
                    mWebView.loadUrl("javascript:jumpforxungen()");
                    break;
                case R.id.text_edit:
                    mWebView.loadUrl("javascript:jumpforxungen()");
                    break;
                case R.id.text_build:
                    mWebView.loadUrl("javascript:jumpforxungen()");
                    break;
                case R.id.family:
                    if (XunGenApp.isLogin) {
                        mWebView.loadUrl("javascript:jumpforxungen()");
                    } else {
                        IntentSkip intentSkip = new IntentSkip();
                        intentSkip.skipLogin(mContext, 0);
                    }
                    break;
                case R.id.text_commit://提交
                    mWebView.loadUrl("javascript:submit()");
                    break;
                case R.id.popup_question:
                    mWebView.loadUrl("javascript:jumpforxungen()");
                    mPopupWindow.dismiss();
                    break;
                case R.id.popup_smshint:
                    mWebView.loadUrl("javascript:message()");
                    mPopupWindow.dismiss();
                    break;
                case R.id.popup_share:
                    mWebView.loadUrl("javascript:shareforxungen()");
                    mPopupWindow.dismiss();
                    break;
                case R.id.popup_look:
                    mWebView.loadUrl("javascript:jumpforxungen()");
                    mPopupWindow.dismiss();
                    break;
            }

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "onPause");
       /* try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView,(Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "onStop");
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }*/
    }

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.PAYMENT_SUCEESS);
        registerReceiver(br, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(ConstantNum.LOGIN_SUCCESS);
        registerReceiver(br2, intentFilter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("H5Activity", "onDestroy");
        unregisterReceiver(br);
        unregisterReceiver(br2);
        if (loading != null && loading.isShowing()) loading.setDimiss();
        if (loadingDialogUtils != null && loadingDialogUtils.isShowing())
            loadingDialogUtils.setDimiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConstantNum.PAYMENT_SUCEESS)) {
                int isSuccess = intent.getIntExtra("isSuccess", 0);//1成功，2没有成功
                //微信支付成功后的回调
                mWebView.loadUrl("javascript:finishPay('" + isSuccess + "')");
                if (isSuccess == 1) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    private BroadcastReceiver br2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConstantNum.LOGIN_SUCCESS)) {
                mWebView.loadUrl("javascript:getTokenFormApp('" + XunGenApp.token + "')");
                mWebView.loadUrl("javascript:getRidFormApp('" + XunGenApp.rid + "')");
            }
        }
    };
}
