package com.ruihuo.ixungen.editorvideo;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.action.VideoPreviewActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.utils.SDCardUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import java.io.File;
import java.lang.ref.WeakReference;

public class VideoEditActivity extends AppCompatActivity {
    private static final String TAG = VideoEditActivity.class.getSimpleName();
    private static final long MIN_CUT_DURATION = 3 * 1000L;// 最小剪辑时间3s
    private static final long MAX_CUT_DURATION = 10 * 1000L;//视频最多剪切多长时间
    private static final int MAX_COUNT_RANGE = 10;//seekBar的区域内一共有多少张图片
    private LinearLayout seekBarLayout;
    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private int mMaxWidth;

    private long duration;
    private RangeSeekBar seekBar;
    private VideoView mVideoView;
    private RecyclerView mRecyclerView;
    private TitleBar mTitleBar;
    private ImageView positionIcon;
    private VideoEditAdapter videoEditAdapter;
    private float averageMsPx;//每毫秒所占的px
    private float averagePxMs;//每px所占用的ms毫秒
    private String OutPutFileDirPath;
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    //  private String path;
    private long leftProgress, rightProgress;
    private long scrollPos = 0;
    private int mScaledTouchSlop;
    private int lastScrollX;
    private boolean isSeeking;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        videoPath = getIntent().getStringExtra("videoPath");
        initData();
        initView();
        initEditVideo();
        initPlay();
        addListener();
        registerBoradcastReceiver();
    }

    private void initData() {
        //for video check
        if (!new File(videoPath).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show();
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(videoPath);
        duration = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());


        mMaxWidth = UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70);
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

    }


    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("编辑视频");
        mTitleBar.mTextRegister.setText("完成");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        seekBarLayout = (LinearLayout) findViewById(R.id.id_seekBarLayout);
        mVideoView = (VideoView) findViewById(R.id.uVideoView);
        positionIcon = (ImageView) findViewById(R.id.positionIcon);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_rv_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videoEditAdapter = new VideoEditAdapter(this,
                (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / 10);
        mRecyclerView.setAdapter(videoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }


    private void initEditVideo() {
        //for video edit
        long startPosition = 0;
        long endPosition = duration;
        int thumbnailsCount;//图片个数
        int rangeWidth;
        boolean isOver_60_s;
        if (endPosition <= MAX_CUT_DURATION) {
            isOver_60_s = false;
            thumbnailsCount = MAX_COUNT_RANGE;
            rangeWidth = mMaxWidth;
        } else {
            isOver_60_s = true;
            thumbnailsCount = (int) (endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE);
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        }
        // RecyclerView调用addItemDecoration添加自定义分割线
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(UIUtil.dip2px(this, 35), thumbnailsCount));

        //init seekBar
        if (isOver_60_s) {
            seekBar = new RangeSeekBar(this, 0L, MAX_CUT_DURATION);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(MAX_CUT_DURATION);
        } else {
            seekBar = new RangeSeekBar(this, 0L, endPosition);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(endPosition);
        }
        seekBar.setMin_cut_time(MIN_CUT_DURATION);//设置最小裁剪时间
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        seekBarLayout.addView(seekBar);

        Log.d(TAG, "-------thumbnailsCount--->>>>" + thumbnailsCount);
        averageMsPx = duration * 1.0f / rangeWidth * 1.0f;
        Log.d(TAG, "-------rangeWidth--->>>>" + rangeWidth);
        Log.d(TAG, "-------localMedia.getDuration()--->>>>" + duration);
        Log.d(TAG, "-------averageMsPx--->>>>" + averageMsPx);
        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(this);
        int extractW = (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / MAX_COUNT_RANGE;
        int extractH = UIUtil.dip2px(this, 55);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler, videoPath, OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

        //init pos icon start
        leftProgress = 0;
        if (isOver_60_s) {
            rightProgress = MAX_CUT_DURATION;
        } else {
            rightProgress = endPosition;
        }
        averagePxMs = (mMaxWidth * 1.0f / (rightProgress - leftProgress));
        Log.d(TAG, "------averagePxMs----:>>>>>" + averagePxMs);


    }


    private void initPlay() {
        mVideoView.setVideoPath(videoPath);
        //设置videoview的OnPrepared监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //设置MediaPlayer的OnSeekComplete监听
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        Log.d(TAG, "------ok----real---start-----");
                        Log.d(TAG, "------isSeeking-----" + isSeeking);
                        if (!isSeeking) {
                            videoStart();
                        }
                    }
                });
            }
        });
        //first
        videoStart();
    }

    private String savePath;
    private LoadingDialogUtils loadingDialogUtils;

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleBar.mTextRegister.setEnabled(false);
                TrimVideoUtils trimVideoUtils = TrimVideoUtils.getInstance();
                trimVideoUtils.setTrimCallBack(new TrimVideoUtils.TrimFileCallBack() {
                    @Override
                    public void trimError(int eType) {
                        Message msg = new Message();
                        msg.what = TrimVideoUtils.TRIM_FAIL;
                        switch (eType) {
                            case TrimVideoUtils.FILE_NOT_EXISTS: // 文件不存在
                                msg.obj = "视频文件不存在";
                                break;
                            case TrimVideoUtils.TRIM_STOP: // 手动停止裁剪
                                msg.obj = "停止裁剪";
                                break;
                            case TrimVideoUtils.TRIM_FAIL:
                            default: // 裁剪失败
                                msg.obj = "裁剪失败";
                                break;
                        }
                        cutHandler.sendMessage(msg);
                    }

                    @Override
                    public void trimCallback(boolean isNew, int startS, int endS, int vTotal, File file, File trimFile) {
                        /**
                         * 裁剪回调
                         * @param isNew 是否新剪辑
                         * @param starts 开始时间(秒)
                         * @param ends 结束时间(秒)
                         * @param vTime 视频长度
                         * @param file 需要裁剪的文件路径
                         * @param trimFile 裁剪后保存的文件路径
                         */
                        // ===========
                        System.out.println("isNew : " + isNew);
                        System.out.println("startS : " + startS);
                        System.out.println("endS : " + endS);
                        System.out.println("vTotal : " + vTotal);
                        System.out.println("file : " + file.getAbsolutePath());
                        System.out.println("trimFile : " + trimFile.getAbsolutePath());
                        // --
                        cutHandler.sendEmptyMessage(TrimVideoUtils.TRIM_SUCCESS);
                    }
                });
                // 保存的路径
                savePath = SDCardUtils.getSDCartPath() + File.separator + "cut.mp4";
                // ==
                final File file = new File(videoPath); // 视频文件地址
                final File trimFile = new File(savePath);// 裁剪文件保存地址
                // 进行裁剪
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try { // 开始裁剪
                            loadingDialogUtils = new LoadingDialogUtils(VideoEditActivity.this);
                            TrimVideoUtils.getInstance().startTrim(true, (int) (leftProgress / 1000), (int) (rightProgress / 1000), file, trimFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 设置回调为null
                            TrimVideoUtils.getInstance().setTrimCallBack(null);
                        }
                    }
                }).start();
            }
        });
    }

    private boolean isOverScaledTouchSlop;

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, "-------newState:>>>>>" + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isSeeking = false;
//                videoStart();
            } else {
                isSeeking = true;
                if (isOverScaledTouchSlop && mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSeeking = false;
            int scrollX = getScrollXDistance();
            //达不到滑动的距离
            if (Math.abs(lastScrollX - scrollX) < mScaledTouchSlop) {
                isOverScaledTouchSlop = false;
                return;
            }
            isOverScaledTouchSlop = true;
            Log.d(TAG, "-------scrollX:>>>>>" + scrollX);
            //初始状态,why ? 因为默认的时候有35dp的空白！
            if (scrollX == -UIUtil.dip2px(VideoEditActivity.this, 35)) {
                scrollPos = 0;
            } else {
                // why 在这里处理一下,因为onScrollStateChanged早于onScrolled回调
                if (mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
                isSeeking = true;
                scrollPos = (long) (averageMsPx * (UIUtil.dip2px(VideoEditActivity.this, 35) + scrollX));
                Log.d(TAG, "-------scrollPos:>>>>>" + scrollPos);
                leftProgress = seekBar.getSelectedMinValue() + scrollPos;
                rightProgress = seekBar.getSelectedMaxValue() + scrollPos;
                Log.d(TAG, "-------leftProgress:>>>>>" + leftProgress);
                mVideoView.seekTo((int) leftProgress);
            }
            lastScrollX = scrollX;
        }
    };

    /**
     * 水平滑动了多少px
     *
     * @return int px
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }

    private ValueAnimator animator;

    private void anim() {
        Log.d(TAG, "--anim--onProgressUpdate---->>>>>>>" + mVideoView.getCurrentPosition());
        if (positionIcon.getVisibility() == View.GONE) {
            positionIcon.setVisibility(View.VISIBLE);
        }
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) positionIcon.getLayoutParams();
        int start = (int) (UIUtil.dip2px(this, 35) + (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos) * averagePxMs);
        int end = (int) (UIUtil.dip2px(this, 35) + (rightProgress - scrollPos) * averagePxMs);
        animator = ValueAnimator
                .ofInt(start, end)
                .setDuration((rightProgress - scrollPos) - (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                positionIcon.setLayoutParams(params);
            }
        });
        animator.start();
    }

    private final MainHandler mUIHandler = new MainHandler(this);

    private static class MainHandler extends Handler {
        private final WeakReference<VideoEditActivity> mActivity;

        MainHandler(VideoEditActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoEditActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.videoEditAdapter != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
                        activity.videoEditAdapter.addItemVideoInfo(info);
                    }
                }
            }
        }
    }

    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new RangeSeekBar.OnRangeSeekBarChangeListener() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long maxValue, int action, boolean isMin, RangeSeekBar.Thumb pressedThumb) {
            Log.d(TAG, "-----minValue----->>>>>>" + minValue);
            Log.d(TAG, "-----maxValue----->>>>>>" + maxValue);
            leftProgress = minValue + scrollPos;
            rightProgress = maxValue + scrollPos;
            Log.d(TAG, "-----leftProgress----->>>>>>" + leftProgress);
            Log.d(TAG, "-----rightProgress----->>>>>>" + rightProgress);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "-----ACTION_DOWN---->>>>>>");
                    isSeeking = false;
                    videoPause();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "-----ACTION_MOVE---->>>>>>");
                    isSeeking = true;
                    mVideoView.seekTo((int) (pressedThumb == RangeSeekBar.Thumb.MIN ?
                            leftProgress : rightProgress));
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "-----ACTION_UP--leftProgress--->>>>>>" + leftProgress);
                    isSeeking = false;
                    //从minValue开始播
                    mVideoView.seekTo((int) leftProgress);
//                    videoStart();
                    break;
                default:
                    break;
            }
        }
    };


    private void videoStart() {
        Log.d(TAG, "----videoStart----->>>>>>>");
        mVideoView.start();
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        anim();
        handler.removeCallbacks(run);
        handler.post(run);
    }

    private void videoProgressUpdate() {
        long currentPosition = mVideoView.getCurrentPosition();
        Log.d(TAG, "----onProgressUpdate-cp---->>>>>>>" + currentPosition);
        if (currentPosition >= (rightProgress)) {
            mVideoView.seekTo((int) leftProgress);
            positionIcon.clearAnimation();
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
            anim();
        }
    }

    private void videoPause() {
        isSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(run);
        }
        Log.d(TAG, "----videoPause----->>>>>>>");
        if (positionIcon.getVisibility() == View.VISIBLE) {
            positionIcon.setVisibility(View.GONE);
        }
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.seekTo((int) leftProgress);
//            videoStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            videoPause();
        }
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {
            videoProgressUpdate();
            handler.postDelayed(run, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        if (animator != null) {
            animator.cancel();
        }
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if (mExtractVideoInfoUtil != null) {
            mExtractVideoInfoUtil.release();
        }
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
    }

    /**
     * 裁剪处理 Handler
     */
    private Handler cutHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case TrimVideoUtils.TRIM_FAIL: // 裁剪失败
                    loadingDialogUtils.setDimiss();
                    Toast.makeText(VideoEditActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    // --
                    mTitleBar.mTextRegister.setEnabled(true);
                    break;
                case TrimVideoUtils.TRIM_SUCCESS: // 裁剪成功
                  /*  Toast.makeText(VideoEditActivity.this, "裁剪成功", Toast.LENGTH_SHORT).show();
                    // --*/
                    mTitleBar.mTextRegister.setEnabled(true);

                    loadingDialogUtils.setDimiss();
                    Intent intent = new Intent(VideoEditActivity.this, VideoPreviewActivity.class);
                    intent.putExtra("path", savePath);
                    intent.putExtra("from", "VideoEditActivity");
                    startActivity(intent);
                    finish();
                  /*  compressor.loadBinary(new InitListener() {//视频压缩
                        @Override
                        public void onLoadSuccess() {
                            compressor.execCommand(savePath, new CompressListener() {
                                @Override
                                public void onExecSuccess(String message) {
                                    loadingDialogUtils.setDimiss();
                                    Intent intent = new Intent(VideoEditActivity.this, VideoPreviewActivity.class);
                                    intent.putExtra("path", savePath);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onExecFail(String reason) {
                                    loadingDialogUtils.setDimiss();
                                }

                                @Override
                                public void onExecProgress(String message) {

                                }
                            });
                        }

                        @Override
                        public void onLoadFail(String reason) {
                            loadingDialogUtils.setDimiss();
                        }
                    });*/
                    break;
            }
        }
    };

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.ACTION_USERINFO);
        registerReceiver(br, intentFilter);
    }


    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.ACTION_USERINFO)) {
                String from = intent.getStringExtra("from");
                if ("VideoPreviewActivity".equals(from)) {//上传视频成功
                    finish();
                }
            }
        }
    };
}
