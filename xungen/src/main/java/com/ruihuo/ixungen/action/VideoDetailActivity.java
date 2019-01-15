package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.CallBackBeanInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ShareUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.CommonVideoView;
import com.ruihuo.ixungen.view.MyListView;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class VideoDetailActivity extends AppCompatActivity {
    private Context mContext;
    private FrameLayout mFlVideo;
    private SmartRefreshLayout mRefresh;
    private ScrollView mScrollView;
    private RelativeLayout mRLTitlerbar;//
    private LinearLayout mLl_Info;//发表视频的人的个人信息
    private RelativeLayout mRl_fenzhan;//分享点赞
    private View mLines;//
    private LinearLayout mLl_pinlun;//下面的评论模块
    private ImageView mImageBack;
    private CircleImageView mImage_header;
    private TextView mUser_name;
    private TextView mCreatTime;
    private TextView mPlayTimes;
    private TextView mIntroduce;
    private CommonVideoView mVideoView;
    private ImageView mImage_share;
    private TextView mShare_times;
    private ImageView mImage_praise;
    private TextView mPraise_times;
    private MyListView mListview;
    private FrameLayout mLl_videoview;
    private VideoView mVideoViewSelf;
    private ProgressBar mProgressBar;
    private LinearLayout mLl_editor;
    private EditText mEdit_sms;
    private TextView mSend_sms;
    private ImageView mShare_Bottom;
    private ImageView mPraise_Bottom;
    private ImageView mVideoBg;
    private ImageView mImagePlay;
    private int page = 1;
    private int limit = 20;
    private String videoId;
    List<VideoDetailBean.DataBean.CommentInfoBean> commentInfoList = new ArrayList<>();
    private VideoCommentAdapter mAdapter;
    private VideoDetailBean.DataBean.CommentInfoBean commentInfoBean;
    private boolean isCommentVideo = true;//决定是评论视频还是回复别人的评论。true是评论视频
    private String nikename;
    private String avatar;
    private String remark;
    private String total_like;
    private String imgBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        nikename = intent.getStringExtra("nikename");
        avatar = intent.getStringExtra("avatar");
        imgBg = intent.getStringExtra("imgBg");
        mContext = this;
        initView();
        addData(true);
        mAdapter = new VideoCommentAdapter(commentInfoList, mContext);
        mListview.setAdapter(mAdapter);
        addListener();
    }

    private void initView() {
        mFlVideo = (FrameLayout) findViewById(R.id.fl_video);
        mImageBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ScrollView) findViewById(R.id.pull_scrollview);
        mImage_header = (CircleImageView) findViewById(R.id.image_header);
        mUser_name = (TextView) findViewById(R.id.user_name);
        mCreatTime = (TextView) findViewById(R.id.time_times);
        mPlayTimes = (TextView) findViewById(R.id.play_times);
        mIntroduce = (TextView) findViewById(R.id.introduce);
        mVideoView = (CommonVideoView) findViewById(R.id.videoView);
        mVideoBg = (ImageView) findViewById(R.id.video_bg);
        mImagePlay = (ImageView) findViewById(R.id.img_play);
        mLl_videoview = (FrameLayout) findViewById(R.id.ll_videoview);
        mVideoViewSelf = (VideoView) findViewById(R.id.videoview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mImage_share = (ImageView) findViewById(R.id.share_video);
        mShare_times = (TextView) findViewById(R.id.share_times);
        mImage_praise = (ImageView) findViewById(R.id.image_praise);
        mPraise_times = (TextView) findViewById(R.id.praise_times);
        mListview = (MyListView) findViewById(R.id.listview);
        mRLTitlerbar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        mLl_Info = (LinearLayout) findViewById(R.id.ll_info);
        mRl_fenzhan = (RelativeLayout) findViewById(R.id.rl_fenzan);
        mLines = findViewById(R.id.graylines);
        mLl_pinlun = (LinearLayout) findViewById(R.id.ll_pinlun);
        //底部输入框
        mLl_editor = (LinearLayout) findViewById(R.id.ll_editor);
        mEdit_sms = (EditText) findViewById(R.id.edit_sms);
        mSend_sms = (TextView) findViewById(R.id.send_sms);
        mShare_Bottom = (ImageView) findViewById(R.id.share_bottom);
        mPraise_Bottom = (ImageView) findViewById(R.id.praise_bottom);
        if (!TextUtils.isEmpty(imgBg)) {
            Picasso.with(mContext)
                    .load(imgBg)
                    // .load("https://res.ixungen.cn/img/9,024533026b1b")
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.default_photo)
                    .error(R.mipmap.default_photo)
                    .into(mVideoBg);
        }
    }

    private void addData(final boolean isFirst) {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("id", videoId);
        params.putString("page", page + "");
        params.putString("limit", limit + "");
        mHttp.get(Url.VIDEO_DETAIL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                VideoDetailBean videoDetailBean = gson.fromJson(result, VideoDetailBean.class);
                VideoDetailBean.DataBean data = videoDetailBean.getData();
                if (isFirst)
                    setView(data);
                commentInfoList.addAll(data.getComment_info());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private String commentId;
    private String toRid;//作为被回复者的根号。实际是目前评论者的根号

    private void addListener() {
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(603, intent);
                finish();
            }
        });
        //播放视频
        mImagePlay.setOnClickListener(PlayVideoListener);
        //点击播放视频的页面
        mLl_videoview.setOnClickListener(PauseVideoListener);
        //视频播放完成后
        mVideoViewSelf.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("执行了", "onCompletion");
                mVideoViewSelf.seekTo(0);
                mp.start();
            }
        });
        mVideoViewSelf.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e("执行了", "onPrepared");
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mSend_sms.setOnClickListener(SendSmsListener);
        //分享视频
        mImage_share.setOnClickListener(ShareVideoListener);
        mShare_Bottom.setOnClickListener(ShareVideoListener);
        //点赞视频
        // mImage_praise.setOnClickListener(PraiseVideoListener);//设计师说这个按钮只是展示左右。不可点击。
        mPraise_Bottom.setOnClickListener(PraiseVideoListener);

        mAdapter.setListener(new CallBackBeanInterface() {

            @Override
            public void callBack(String commentid, String torid, String replayname) {
                isCommentVideo = false;
                commentId = commentid;
                toRid = torid;
                mEdit_sms.setFocusable(true);
                mEdit_sms.setFocusableInTouchMode(true);
                mEdit_sms.requestFocus();
                mEdit_sms.setHint("回复" + replayname);
            }
        });
    }

    View.OnClickListener ShareVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils shareUtils = new ShareUtils(mContext);
            shareUtils.share(TextUtils.isEmpty(nikename) ? "" : nikename + "的视频", TextUtils.isEmpty(remark) ? "" : remark, pathShare);
        }
    };
    View.OnClickListener PraiseVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("id", videoId);
            mHttp.post(Url.PRAISE_VIDEO, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    mImage_praise.setImageResource(R.mipmap.praise);
                    mPraise_Bottom.setImageResource(R.mipmap.praise);
                    int i = Integer.parseInt(total_like) + 1;
                    mPraise_times.setText(i + "");
                }

                @Override
                public void onError(String message) {

                }
            });
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
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            String url;
            if (isCommentVideo) {
                url = Url.COMMENT_VIDEO;
                params.putString("token", XunGenApp.token);
                params.putString("videoId", videoId);
                params.putString("content", content);

            } else {
                url = Url.REPLAY_COMMENT_VIDEO;
                params.putString("token", XunGenApp.token);
                params.putString("commentId", commentId);
                params.putString("replyContent", content);
                params.putString("toRid", toRid);
            }

            mHttp.post(url, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    commentInfoList.clear();
                    //page=1;
                    addData(false);
                    mEdit_sms.setText("");
                    mEdit_sms.setHint("请输入评论内容");
                    isCommentVideo = true;//恢复到直接评论视频的状态
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mLl_editor.getWindowToken(), 0);
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    private long curPosition;
    private String path;
    private String pathShare;
    private String url;

    private void setView(VideoDetailBean.DataBean data) {
        String url = data.getUrl();//视频地址
        String id = data.getId();
        path = Url.ROOT_VIDEO + url;
        pathShare = Url.H5_HTTP + "/index/Zone/video?id=" + id;
        remark = data.getRemark();
        String total_play = data.getTotal_play();//播放次数
        String create_time = DateFormatUtils.longToDateM(data.getCreate_time()); //上传时间
        total_like = TextUtils.isEmpty(data.getTotal_like()) ? "0" : data.getTotal_like();

        mPraise_times.setText(total_like);
        mCreatTime.setText(create_time);//上传时间和播放次数
        mPlayTimes.setText(total_play + "次播放");
        mUser_name.setText(TextUtils.isEmpty(nikename) ? "" : nikename);//视频拥有者的昵称
        mIntroduce.setText(TextUtils.isEmpty(remark) ? "" : remark);
        if (TextUtils.isEmpty(remark)) {
            mIntroduce.setVisibility(View.GONE);
        } else {
            mIntroduce.setVisibility(View.VISIBLE);
            mIntroduce.setText(remark);
        }
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        curPosition = sp.getLong(path, 0);//本视频上次播放的位置
        //  mVideoView.start(path, curPosition);

        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(mContext)
                    .load(avatar)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_header)
                    .placeholder(R.mipmap.default_header)
                    .into(mImage_header);
        }
    }

    View.OnClickListener PlayVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(path)) {
                mLl_videoview.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);//点击播放加载条要处于显示状态
                mVideoViewSelf.setVideoPath(path);
                mVideoViewSelf.start();
            }
        }
    };
    View.OnClickListener PauseVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLl_videoview.setVisibility(View.GONE);
            mVideoViewSelf.pause();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLl_editor.setVisibility(View.GONE);
            mLl_pinlun.setVisibility(View.GONE);
            mRl_fenzhan.setVisibility(View.GONE);
            mRLTitlerbar.setVisibility(View.GONE);
            mLl_Info.setVisibility(View.GONE);
            mIntroduce.setVisibility(View.GONE);
            mLines.setVisibility(View.GONE);
            mListview.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mScrollView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            mScrollView.setLayoutParams(layoutParams);
            mRefresh.setEnableLoadMore(false);
            mRefresh.setEnableRefresh(false);
            // mFlVideo.setSystemUiVisibility(View.INVISIBLE);//z状态栏控制隐藏
            mVideoView.setFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            mLl_editor.setVisibility(View.VISIBLE);
            mLl_pinlun.setVisibility(View.VISIBLE);
            mRl_fenzhan.setVisibility(View.VISIBLE);
            mRLTitlerbar.setVisibility(View.VISIBLE);
            mLl_Info.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(remark)) {
                mIntroduce.setVisibility(View.GONE);
            } else {
                mIntroduce.setVisibility(View.VISIBLE);
                mIntroduce.setText(remark);
            }
            mLines.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.VISIBLE);
            float dimension = getResources().getDimension(R.dimen.dp_50);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mScrollView.getLayoutParams();
            layoutParams.setMargins(0, (int) dimension, 0, 0);
            mScrollView.setLayoutParams(layoutParams);
            mRefresh.setEnableLoadMore(true);
            mRefresh.setEnableRefresh(true);
            //  mFlVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//显示状态栏
            mVideoView.setNormalScreen();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //如果是横屏就变成竖屏，否则就退出当前页面
            //mVideoView.screen();
            int visibility = mLl_videoview.getVisibility();
            if (visibility == View.VISIBLE) {
                mLl_videoview.setVisibility(View.GONE);
                mVideoViewSelf.pause();
            } else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        long currentPosition = mVideoView.getCurrentPosition();
        editor.putLong(path, currentPosition);
        editor.commit();
    }
}
