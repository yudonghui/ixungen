package com.ruihuo.ixungen.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.editorvideo.VideoEditActivity;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.ForumScrollView;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.io.IOException;

public class ActionUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout mFlTitle;
    private RelativeLayout mLlTitleBar;
    private ImageView mImage_titlebar_back;
    private TextView mTextTitle;
    private TextView mVote;
    private CircleImageView mUser_header;
    private TextView mName;
    private TextView mAward;
    private TextView mIntroduce;
    private TextView mVideo;
    private TextView mPhoto;
    private TextView mDynamic;
    private FrameLayout mFl;
    private LinearLayout mLlJSP;//简介视频照片的导航栏
    private FragmentManager mFragmentManager;
    private IntroduceFragment introduceFragment;
    private VideoFragment videoFragment;
    private ActionPhotoFragment actionPhotoFragment;
    private DynamicFragment mDynamicFragment;
    private SmartRefreshLayout mRefresh;
    private ForumScrollView mScrollView;
    private View mViewAlpha;
    private String title = "简介";
    private Context mContext;
    private String rid;
    /* private LinearLayout mVideoTitle;
     private TextView mTablayout1;
     private TextView mTablayout2;
     private TextView mTablayout3;
     private TextView mTablayout4;
     private TextView mDiv1;
     private TextView mDiv2;
     private TextView mDiv3;
     private TextView mDiv4;*/
    private ImageButton mXuanfu;
    private PopupWindowView popupWindowView;
    private TextView mCancel;
    private TextView mDelete;
    private String actionId;
    private ImageView mIsActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_user_info);
        mContext = this;
        Intent intent = getIntent();
        rid = intent.getStringExtra("rid");
        actionId = intent.getStringExtra("actionId");
        initView();
        addData();
        addListener();
        registerBoradcastReceiver();
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ForumScrollView) findViewById(R.id.pull_scrollView);
        mFlTitle = (FrameLayout) findViewById(R.id.fl_action);
        mLlTitleBar = (RelativeLayout) findViewById(R.id.ll_titlebar);
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mVote = (TextView) findViewById(R.id.vote);
        mUser_header = (CircleImageView) findViewById(R.id.user_header);
        mIsActor = (ImageView) findViewById(R.id.isActor);
        mName = (TextView) findViewById(R.id.name);
        mAward = (TextView) findViewById(R.id.award);
        mAward.setVisibility(View.GONE);
        mIntroduce = (TextView) findViewById(R.id.introduce);
        mVideo = (TextView) findViewById(R.id.video);
        mPhoto = (TextView) findViewById(R.id.photo);
        mDynamic = (TextView) findViewById(R.id.dynamic);
        mFl = (FrameLayout) findViewById(R.id.fl);
        mViewAlpha = findViewById(R.id.viewalpha);
        mLlJSP = (LinearLayout) findViewById(R.id.ll_jsp);
        mXuanfu = (ImageButton) findViewById(R.id.xuanfu);
        popupWindowView = new PopupWindowView(mContext);
        mCancel = (TextView) findViewById(R.id.cancel);
        mDelete = (TextView) findViewById(R.id.delete);
        if (XunGenApp.rid.equals(rid)) {
            mVote.setVisibility(View.GONE);
            mXuanfu.setVisibility(View.VISIBLE);
        } else {
            // mVote.setVisibility(View.VISIBLE);
            mXuanfu.setVisibility(View.GONE);
        }
    }

    private void addData() {
        introduceData();
        introduceFragment = new IntroduceFragment();

        videoFragment = new VideoFragment();
        videoFragment.setData(callBack, rid);

        actionPhotoFragment = new ActionPhotoFragment();
        actionPhotoFragment.setData(rid);

        mDynamicFragment = new DynamicFragment();
        mDynamicFragment.setData(rid);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl, introduceFragment).add(R.id.fl, videoFragment)
                .add(R.id.fl, actionPhotoFragment).add(R.id.fl, mDynamicFragment)
                .show(introduceFragment).hide(videoFragment).hide(actionPhotoFragment).hide(mDynamicFragment);
        fragmentTransaction.commit();
    }

    private void introduceData() {
        final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", rid);
        mHttp.get(Url.INTRODUCE_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                Gson gson = GsonUtils.getGson();
                IntroduceBean introduceBean = gson.fromJson(result, IntroduceBean.class);
                mAward.setText("投票：" + introduceBean.getData().getTotal_vote());
                String nikename = introduceBean.getData().getNikename();
                mName.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
                String avatar = introduceBean.getData().getAvatar();
                if (!TextUtils.isEmpty(avatar)) {
                    Picasso.with(mContext)
                            .load(avatar)
                            .config(Bitmap.Config.RGB_565)
                            .placeholder(R.mipmap.default_header)
                            .error(R.mipmap.default_header)
                            .into(mUser_header);
                }

                introduceFragment.setData(introduceBean);
                String is_performer = introduceBean.getData().getIs_performer();
                if ("1".equals(is_performer)) mIsActor.setVisibility(View.VISIBLE);
                else mIsActor.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
            }
        });
    }


    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIntroduce.setOnClickListener(this);
        mVideo.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
        mDynamic.setOnClickListener(this);
        mVote.setOnClickListener(VoteListener);
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        mScrollView.setOnScrollistener(new ForumScrollView.OnScrollistener() {
            @Override
            public void onScroll(int startY, int endY) {
                dynamicChangeAphla(startY, endY);
            }
        });
      /*  mTablayout1.setOnClickListener(OrderListener);
        mTablayout2.setOnClickListener(OrderListener);
        mTablayout3.setOnClickListener(OrderListener);
        mTablayout4.setOnClickListener(OrderListener);*/
        mXuanfu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置
                popupWindowView.setGon();
                if ("简介".equals(title)) {
                    introduceFragment.setXuanfu(popupWindowView, mXuanfu);
                } else if ("视频".equals(title)) {
                    videoFragment.setXuanfu(popupWindowView, mXuanfu);
                } else if ("照片".equals(title)) {
                    actionPhotoFragment.setXuanfu(popupWindowView, mXuanfu);
                } else {
                    mDynamicFragment.setXuanfu(popupWindowView, mXuanfu);
                }
            }
        });
        //取消删除
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelete.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                if (XunGenApp.rid.equals(rid)) {
                    mVote.setVisibility(View.GONE);
                    mXuanfu.setVisibility(View.VISIBLE);
                } else {
                    // mVote.setVisibility(View.VISIBLE);
                    mXuanfu.setVisibility(View.GONE);
                }
                videoFragment.cancelData();
            }
        });
        //删除
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelete.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                if (XunGenApp.rid.equals(rid)) {
                    mVote.setVisibility(View.GONE);
                    mXuanfu.setVisibility(View.VISIBLE);
                } else {
                    //  mVote.setVisibility(View.VISIBLE);
                    mXuanfu.setVisibility(View.GONE);
                }
                videoFragment.deleteData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mIntroduce.setBackgroundColor(0x19ffffff);
        mVideo.setBackgroundColor(0x19ffffff);
        mPhoto.setBackgroundColor(0x19ffffff);
        mDynamic.setBackgroundColor(0x19ffffff);
        switch (v.getId()) {
            case R.id.introduce:
                title = "简介";
                mRefresh.setEnableLoadMore(false);
                mIntroduce.setBackgroundColor(0x33ffffff);
                fragmentTransaction.show(introduceFragment).hide(videoFragment)
                        .hide(actionPhotoFragment).hide(mDynamicFragment);
                break;
            case R.id.video:
                title = "视频";
                mRefresh.setEnableLoadMore(true);
                mVideo.setBackgroundColor(0x33ffffff);
                fragmentTransaction.hide(introduceFragment).show(videoFragment)
                        .hide(actionPhotoFragment).hide(mDynamicFragment);
                break;
            case R.id.photo:
                title = "照片";
                mRefresh.setEnableLoadMore(true);
                mPhoto.setBackgroundColor(0x33ffffff);
                fragmentTransaction.hide(introduceFragment).hide(videoFragment)
                        .show(actionPhotoFragment).hide(mDynamicFragment);
                break;
            case R.id.dynamic:
                title = "动态";
                mRefresh.setEnableLoadMore(true);
                mDynamic.setBackgroundColor(0x33ffffff);
                fragmentTransaction.hide(introduceFragment).hide(videoFragment)
                        .hide(actionPhotoFragment).show(mDynamicFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    View.OnClickListener VoteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("activityId", actionId);
            params.putString("joinActivityRid", rid);
            mHttp.post(Url.VOTE_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    mVote.setText("已投票");
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };

    private void dynamicChangeAphla(int startY, int endY) {
        //获取到状态栏的高度
        int statusBarHeight = DisplayUtilX.getStatusBarHeight();
        int titleBarHeight = mLlTitleBar.getLayoutParams().height;
        //获取背景高度
        int backHeight = mFlTitle.getLayoutParams().height;
        //导航的高度
        int jspHeight = mLlJSP.getLayoutParams().height;
        int[] loaction = new int[2];
        mFlTitle.getLocationInWindow(loaction);
        int currentY = loaction[1] - statusBarHeight;
        if (currentY < -titleBarHeight - statusBarHeight) {
            mViewAlpha.setAlpha(1);
            if (currentY < -(backHeight - jspHeight - titleBarHeight)) {
                mTextTitle.setText(title);
                mTextTitle.setVisibility(View.VISIBLE);
              /*  if ("视频".equals(title) && currentY < -(backHeight - titleBarHeight)) {
                    //判断目前是显示的哪一栏
                    setVideoTitle();
                    mVideoTitle.setVisibility(View.VISIBLE);
                } else {
                    mVideoTitle.setVisibility(View.GONE);
                    videoFragment.setTitle(type);
                }*/
            } else {
                mTextTitle.setVisibility(View.GONE);
            }
        } else if (currentY >= 0) {
            mViewAlpha.setAlpha(0);
        } else {
            float scale = (float) Math.abs(currentY) / titleBarHeight;
            Log.e("透明比例", scale + "");
            mViewAlpha.setAlpha(scale);
        }
    }

    private int type = 1;
    CallBackPositionInterface callBack = new CallBackPositionInterface() {
        @Override
        public void callBack(int typ) {
            if (typ == 5) {
                mDelete.setVisibility(View.VISIBLE);
                mCancel.setVisibility(View.VISIBLE);
                mVote.setVisibility(View.GONE);
                mXuanfu.setVisibility(View.GONE);
            } else {
                type = typ;
                mDelete.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                if (XunGenApp.rid.equals(rid)) {
                    mVote.setVisibility(View.GONE);
                    mXuanfu.setVisibility(View.VISIBLE);
                } else {
                    //   mVote.setVisibility(View.VISIBLE);
                    mXuanfu.setVisibility(View.GONE);
                }
            }
        }
    };
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            switch (title) {
                case "简介":
                    introduceData();
                    break;
                case "视频":
                    videoFragment.setDownRefresh(mRefresh);
                    break;
                case "照片":
                    actionPhotoFragment.setDownRefresh(mRefresh);
                    break;
                case "动态":
                    mDynamicFragment.setDownRefresh(mRefresh);
                    break;
            }
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            switch (title) {
                case "视频":
                    videoFragment.setUpRefresh(mRefresh);
                    break;
                case "照片":
                    actionPhotoFragment.setUpRefresh(mRefresh);
                    break;
                case "动态":
                    mDynamicFragment.setUpRefresh(mRefresh);
                    break;

            }
        }
    };

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.ACTION_USERINFO);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.ACTION_USERINFO)) {
                String from = intent.getStringExtra("from");
                if ("VideoPreviewActivity".equals(from)) {//上传视频成功
                    videoFragment.refreshData();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 220 && resultCode == 320) {
            introduceData();
        } else if (requestCode == 221 && resultCode == 8811) {
            //发表的动态
            mDynamicFragment.setRefresh();
        } else if (resultCode == 603) {
            videoFragment.refreshData();
        } else if (requestCode == VideoFragment.REQUEST_FILE && resultCode == RESULT_OK && null != data) {//选择视频
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String videoPath = cursor.getString(columnIndex);//选择的视频路径
            Log.e("视频路径", videoPath);

            cursor.close();

            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(videoPath);
                mediaPlayer.prepare();
                int duration = mediaPlayer.getDuration() / 1000;
                Log.e("视频时长", duration + "");
                if (duration > 10) {//选择的视频大于10S
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    hintDialogUtils.setMessage("只能上传10秒内视频，需进行编辑！");
                    hintDialogUtils.setConfirm("编辑", new DialogHintInterface() {
                        @Override
                        public void callBack(View view) {
                            Intent intent = new Intent(mContext, VideoEditActivity.class);
                            intent.putExtra("videoPath", videoPath);
                            startActivityForResult(intent, 220);
                        }
                    });
                } else {
                    Intent intent = new Intent(mContext, VideoPreviewActivity.class);
                    intent.putExtra("path", videoPath);
                    intent.putExtra("from", "VideoEditActivity");
                    startActivityForResult(intent, 220);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
