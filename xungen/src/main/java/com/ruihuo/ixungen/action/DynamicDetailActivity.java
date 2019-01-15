package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.activity.merchant.GridImageAdapter;
import com.ruihuo.ixungen.adapter.XWCommentAdapter;
import com.ruihuo.ixungen.entity.CommentBaseBeand;
import com.ruihuo.ixungen.entity.DynamicCommentBean;
import com.ruihuo.ixungen.entity.FriendStateBean;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.geninterface.XWInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LookPrivateDialog;
import com.ruihuo.ixungen.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class DynamicDetailActivity extends BaseNoTitleActivity {
    private Context mContext;
    private FriendStateBean.DataBean dataBean;
    private ScrollView mScrollView;
    private com.ruihuo.ixungen.view.TitleBar mTitlBar;
    private com.ruihuo.ixungen.view.CircleImageView mAvatar;
    private TextView mName;
    private TextView mDate;
    private TextView mContent;
    private com.ruihuo.ixungen.view.MyGridView mGridView;
    private TextView mLookNum;
    private LinearLayout mLl_comment;
    private ImageView mComment_img;
    private TextView mComment_text;
    private LinearLayout mLl_praise;
    private ImageView mPraise_img;
    private TextView mPraise_text;
    private ImageView mXiaLa;
    private MyListView mListView;
    private LinearLayout mLl_editor;
    private EditText mEdit_sms;
    private TextView mSend_sms;

    private String pid;
    private int type;//0-查看所有动态，1-获取宗亲会动态；2-获取自己发表的动态，
    private XWCommentAdapter mCommentAdapter;
    private HttpInterface mHttp;
    private boolean isComment = true;//是评论文章还是回复评论人.true是评论文章
    private String commentId;
    private String toRid;

    private String is_like;
    private String total_like;
    private String total_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        mContext = this;
        dataBean = (FriendStateBean.DataBean) getIntent().getSerializableExtra("dataBean");
        type = getIntent().getIntExtra("type", 0);
        initView();
        mCommentAdapter = new XWCommentAdapter(commentList, mContext, 2);
        mListView.setAdapter(mCommentAdapter);
        mHttp = HttpUtilsManager.getInstance(mContext);
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("详情");
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mAvatar = (com.ruihuo.ixungen.view.CircleImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mDate = (TextView) findViewById(R.id.date);
        mContent = (TextView) findViewById(R.id.content);
        mGridView = (com.ruihuo.ixungen.view.MyGridView) findViewById(R.id.gridView);
        mLookNum = (TextView) findViewById(R.id.lookNum);
        mLl_comment = (LinearLayout) findViewById(R.id.ll_comment);
        mComment_img = (ImageView) findViewById(R.id.comment_img);
        mComment_text = (TextView) findViewById(R.id.comment_text);
        mLl_praise = (LinearLayout) findViewById(R.id.ll_praise);
        mPraise_img = (ImageView) findViewById(R.id.praise_img);
        mPraise_text = (TextView) findViewById(R.id.praise_text);
        mXiaLa = (ImageView) findViewById(R.id.xiala);
        mListView = (MyListView) findViewById(R.id.listView);
        mLl_editor = (LinearLayout) findViewById(R.id.ll_editor);
        mEdit_sms = (EditText) findViewById(R.id.edit_sms);
        mSend_sms = (TextView) findViewById(R.id.send_sms);
        if (dataBean != null) {
            String avatar = dataBean.getAvatar();
            String nikename = dataBean.getNikename();
            pid = dataBean.getId();
            String create_time = dataBean.getCreate_time();
            String content = dataBean.getContent();
            String count = dataBean.getCount();
            String img = dataBean.getImg();
            total_reply = dataBean.getTotal_reply();//回复数
            total_like = dataBean.getTotal_like();//点赞数
            is_like = dataBean.getIs_like();

            mName.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
            mContent.setText(TextUtils.isEmpty(content) ? "" : content);
            PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mAvatar);
            mDate.setText(DateFormatUtils.longToDateM(create_time));
            mLookNum.setText(TextUtils.isEmpty(count) ? "0" : count);
            if (type == 2) mXiaLa.setVisibility(View.VISIBLE);
            else mXiaLa.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(img)) {
                String[] urlArray = img.split("\\;");
                GridImageAdapter gridImageAdapter = new GridImageAdapter(urlArray, 3, mContext);
                mGridView.setAdapter(gridImageAdapter);
            }
            setView();
        }
    }

    private void setView() {
        if (TextUtils.isEmpty(total_reply) || "0".equals(total_reply)) {
            mComment_text.setText("评论");
        } else mComment_text.setText(total_reply);
        if (TextUtils.isEmpty(total_like) || "0".equals(total_like)) {
            mPraise_text.setText("点赞");
        } else mPraise_text.setText(total_like);

        if ("1".equals(is_like)) {
            mPraise_img.setImageResource(R.mipmap.praise);
            mPraise_text.setTextColor(mContext.getResources().getColor(R.color.green_txt));
        } else {
            mPraise_img.setImageResource(R.mipmap.praise_icon);
            mPraise_text.setTextColor(mContext.getResources().getColor(R.color.gray_txt));
        }
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("pid", pid);
                mContext.startActivity(intent);*/
                //弹出键盘
                softControll();
                isComment = true;
                mEdit_sms.setHint("请输入评论内容");
            }
        });
        mSend_sms.setOnClickListener(SendSmsListener);//回复帖子，评论
        mXiaLa.setOnClickListener(XiaLaListener);
        mLl_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("topicId", pid);
                mHttp.post(Url.PRAISE, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        if ("1".equals(is_like)) {
                            is_like = "0";
                            if (!TextUtils.isEmpty(total_like))//取消。则点赞数减少1
                                total_like = (Integer.parseInt(total_like) - 1) + "";
                        } else {
                            is_like = "1";
                            if (!TextUtils.isEmpty(total_like))//取消。则点赞数减少1
                                total_like = (Integer.parseInt(total_like) + 1) + "";
                        }
                        setView();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });
        mEdit_sms.addTextChangedListener(TextChangeListener);//评论的编辑内容
        mCommentAdapter.setListener(new XWInterface() {
            @Override
            public void callBack(String commentI, String toRi, String nikenam) {
                //弹出键盘
                softControll();
                isComment = false;
                commentId = commentI;
                toRid = toRi;
                mEdit_sms.setHint("回复" + nikenam);
            }

        });
    }

    private List<CommentBaseBeand> commentList = new ArrayList<>();

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("topicId", pid);
        mHttp.get(Url.COMMENT_DETAIL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                DynamicCommentBean dynamicCommentBean = gson.fromJson(result, DynamicCommentBean.class);
                commentList.addAll(dynamicCommentBean.getData());
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void softControll() {
        mLl_editor.setVisibility(View.VISIBLE);
        mEdit_sms.setFocusable(true);
        mEdit_sms.setFocusableInTouchMode(true);
        mEdit_sms.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > DisplayUtilX.getDisplayHeight() / 3)) {
                    mLl_editor.setVisibility(View.VISIBLE);
                    //Toast.makeText(getContext(), "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > DisplayUtilX.getDisplayHeight() / 3)) {
                    mLl_editor.setVisibility(View.GONE);
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
                mSend_sms.setEnabled(false);
                mSend_sms.setTextColor(getResources().getColor(R.color.gray_txt));
                mSend_sms.setBackgroundResource(R.drawable.shape_gray_box);
            } else {
                mSend_sms.setEnabled(true);
                mSend_sms.setTextColor(getResources().getColor(R.color.white));
                mSend_sms.setBackgroundResource(R.drawable.shape_btn_bg);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    View.OnClickListener SendSmsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = mEdit_sms.getText().toString();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.toast(mContext, "请输入评论内容");
                return;
            }
            if (isComment) {//对帖子直接评论
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("pid", pid);
                params.putString("content", content);
                mHttp.post(Url.ADD_COMMENT, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        commentList.clear();
                        //page=1;
                        addData();
                        isComment = true;//恢复到直接评论视频的状态
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mLl_editor.getWindowToken(), 0);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            } else {//回复别人的评论
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("topicId", commentId);
                params.putString("content", content);
                params.putString("toRid", toRid);
                mHttp.post(Url.REPLY_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        commentList.clear();
                        //page=1;
                        addData();
                        isComment = true;//恢复到直接评论的状态
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mLl_editor.getWindowToken(), 0);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        }
    };
    View.OnClickListener XiaLaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new LookPrivateDialog(mContext, pid, new CallBackInterface() {
                @Override
                public void callBack() {
                    Intent intent = new Intent();
                    setResult(8811, intent);
                    finish();
                }
            }).showDialog();
        }
    };
}
