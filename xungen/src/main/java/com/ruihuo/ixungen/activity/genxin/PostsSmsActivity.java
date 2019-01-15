package com.ruihuo.ixungen.activity.genxin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.PostsSmsAdapter;
import com.ruihuo.ixungen.entity.PostsSmsBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/4/13
 * @ describe May the Buddha bless bug-free！！
 */
public class PostsSmsActivity extends AppCompatActivity {
    private LinearLayout mLlActivity;
    private TitleBar mTitleBar;
    private ListView mListView;
    private LinearLayout mLlEdit;
    private EditText mEtReplyContent;
    private TextView mSendSms;
    private ImageView mNoData;
    private PostsSmsAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_sms);
        mContext = this;
        initView();
        mAdapter = new PostsSmsAdapter(postsList, mContext);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mLlActivity = (LinearLayout) findViewById(R.id.activity_posts_sms);
        mTitleBar = (TitleBar) findViewById(R.id.postsms_titlebar);
        mTitleBar.setTitle("帖子消息");
        mListView = (ListView) findViewById(R.id.listview);
        mLlEdit = (LinearLayout) findViewById(R.id.ll_editor);
        mEtReplyContent = (EditText) findViewById(R.id.edit_sms);
        mSendSms = (TextView) findViewById(R.id.send_sms);
        mNoData = (ImageView) findViewById(R.id.no_data);
    }

    private String commentId;
    private String replyRid;
    private PostsSmsBean.DataBean dataBean;

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter.setListener(new PostsSmsAdapter.PostsSmsListener() {
            @Override
            public void callBack(int position) {
                dataBean = postsList.get(position);
                commentId = dataBean.getId();
                replyRid = dataBean.getReply_rid();
                mLlEdit.setVisibility(View.VISIBLE);
                mEtReplyContent.setFocusable(true);
                mEtReplyContent.setFocusableInTouchMode(true);
                mEtReplyContent.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mLlActivity.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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
        });
        mEtReplyContent.addTextChangedListener(new TextWatcher() {
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
        });
        mSendSms.setOnClickListener(SendSmsListener);
    }

    View.OnClickListener SendSmsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String replyContent = mEtReplyContent.getText().toString();
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            final Bundle params = new Bundle();
            params.putString("commentId", commentId);
            params.putString("token", XunGenApp.token);
            params.putString("replyContent", replyContent);
            params.putString("toRid", replyRid);
            mHttp.post(Url.POSTS_REPLY_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    mLlEdit.setVisibility(View.GONE);
                    mEtReplyContent.setFocusable(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtReplyContent.getWindowToken(), 0);
                    postsList.clear();
                    page = 1;
                    addData();

                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    List<PostsSmsBean.DataBean> postsList = new ArrayList<>();
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        final Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.POSTS_SMS_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                PostsSmsBean postsSmsBean = gson.fromJson(result, PostsSmsBean.class);
                totalPage = postsSmsBean.getTotalPage();
                postsList.addAll(postsSmsBean.getData());
                mAdapter.notifyDataSetChanged();
                if (postsList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                if (postsList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }
        });
    }
}
