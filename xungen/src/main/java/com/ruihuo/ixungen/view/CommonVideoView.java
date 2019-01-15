package com.ruihuo.ixungen.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.adapter.PopMediaAdapter;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.Timer;
import java.util.TimerTask;



/**
 * qiangyu on 1/26/16 15:33
 * csdn博客:http://blog.csdn.net/yissan
 */
public class CommonVideoView extends FrameLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, View.OnTouchListener, View.OnClickListener, Animator.AnimatorListener, SeekBar.OnSeekBarChangeListener {

    private final int UPDATE_VIDEO_SEEKBAR = 1000;
    private final int MEDIA_CONTROLLER_SHOW = 2000;
    private Context context;
    private FrameLayout viewBox;
    private VideoView videoView;
    private LinearLayout videoPauseBtn;
    private LinearLayout screenSwitchBtn;
    private LinearLayout touchStatusView;
    private LinearLayout videoControllerLayout;
    private ImageView touchStatusImg;
    private ImageView videoPlayImg;
    private ImageView videoPauseImg;
    private TextView touchStatusTime;
    private TextView videoCurTimeText;
    private TextView videoTotalTimeText;
    private SeekBar videoSeekBar;
    private LinearLayout mTitlebar_media;
    private ImageView mClanbiz_back;
    private TextView mMedia_title;
    private ImageView mShare;
    private TextView mSelect_ji;
    private TextView mQuality;
    private ImageView mNext;
    private ImageView mLoadCurrent;
    private ProgressBar progressBar;
    private String url;
    private int duration;
    private String formatTotalTime;
    private PopupWindow popupWindow;
    private Timer timer = new Timer();
    private float touchLastX;
    //定义用seekBar当前的位置，触摸快进的时候显示时间
    // private int position;
    private int touchStep = 1000;//快进的时间，1秒
    // private int touchPosition = -1;
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private PopMediaAdapter popMediaAdapter;
    //最大声音
    private int mMaxVolume;
    // 当前声音
    private int mVolume = -1;
    //当前亮度
    private float mBrightness = -1f;
    //进程
    private int progress;
    private boolean videoControllerShow = true;//底部状态栏的显示状态
    private boolean animation = false;
    int[] select1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    int[] select2 = {31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60};
    int[] select3 = {61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
    int[] select4 = {91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 1010, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120};
    int[] select5 = {121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 160};

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
        }
    };

    private Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIDEO_SEEKBAR:
                    if (videoView.isPlaying()) {
                        videoSeekBar.setProgress(videoView.getCurrentPosition());
                    }
                    break;
                case MEDIA_CONTROLLER_SHOW:
                    float curY = videoControllerLayout.getY();
                    float curTitleY = mTitlebar_media.getY();
                    if (!animation && videoControllerShow) {
                        animation = true;
                        ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y",
                                curY, curY + videoControllerLayout.getHeight());
                        animator.setDuration(200);
                        animator.start();
                        animator.addListener(CommonVideoView.this);
                        ObjectAnimator animatorTitle = ObjectAnimator.ofFloat(mTitlebar_media, "y",
                                curTitleY, curTitleY - mTitlebar_media.getHeight());
                        animatorTitle.setDuration(200);
                        animatorTitle.start();
                    }
                    break;
            }
        }
    };

    public CommonVideoView(Context context) {
        this(context, null);
    }

    public CommonVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private long curPosition;

    public void start(String url, long curPosition) {
        this.url = url;
        this.curPosition = curPosition;
        videoPauseBtn.setEnabled(false);
        videoSeekBar.setEnabled(false);
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();
    }

    public void setFullScreen() {
        mClanbiz_back.setVisibility(VISIBLE);
        touchStatusImg.setImageResource(R.mipmap.iconfont_exit);
        this.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtilX.getDisplayWidth(), DisplayUtilX.getDisplayHeight() - DisplayUtilX.getStatusBarHeight()));
        videoView.requestLayout();
    }

    public void setFullScreenChang() {
        touchStatusImg.setImageResource(R.mipmap.iconfont_exit);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.requestLayout();
        mSelect_ji.setVisibility(VISIBLE);
        mQuality.setVisibility(VISIBLE);
        mNext.setVisibility(VISIBLE);
        mLoadCurrent.setVisibility(VISIBLE);
    }

    public void setNormalScreen() {
        mClanbiz_back.setVisibility(GONE);
        touchStatusImg.setImageResource(R.mipmap.iconfont_enter_32);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 200)));
        videoView.requestLayout();
        mSelect_ji.setVisibility(GONE);
        mQuality.setVisibility(GONE);
        mNext.setVisibility(GONE);
        mLoadCurrent.setVisibility(GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.common_video_view, null);
        viewBox = (FrameLayout) view.findViewById(R.id.viewBox);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        videoPauseBtn = (LinearLayout) view.findViewById(R.id.videoPauseBtn);
        screenSwitchBtn = (LinearLayout) view.findViewById(R.id.screen_status_btn);
        videoControllerLayout = (LinearLayout) view.findViewById(R.id.videoControllerLayout);
        mTitlebar_media = (LinearLayout) view.findViewById(R.id.titlebar_media);
        mClanbiz_back = (ImageView) view.findViewById(R.id.clanbiz_back);
        mMedia_title = (TextView) view.findViewById(R.id.media_title);
        mShare = (ImageView) view.findViewById(R.id.share);
        mSelect_ji = (TextView) view.findViewById(R.id.select_ji);
        mNext = (ImageView) view.findViewById(R.id.next);
        mQuality = (TextView) view.findViewById(R.id.quality);
        mLoadCurrent = (ImageView) view.findViewById(R.id.loadcurrent);
        touchStatusView = (LinearLayout) view.findViewById(R.id.touch_view);
        touchStatusImg = (ImageView) view.findViewById(R.id.touchStatusImg);
        touchStatusTime = (TextView) view.findViewById(R.id.touch_time);
        videoCurTimeText = (TextView) view.findViewById(R.id.videoCurTime);
        videoTotalTimeText = (TextView) view.findViewById(R.id.videoTotalTime);
        videoSeekBar = (SeekBar) view.findViewById(R.id.videoSeekBar);
        videoPlayImg = (ImageView) view.findViewById(R.id.videoPlayImg);
        videoPlayImg.setVisibility(GONE);
        videoPauseImg = (ImageView) view.findViewById(R.id.videoPauseImg);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        //暂停和播放
        videoPauseBtn.setOnClickListener(this);
        videoSeekBar.setOnSeekBarChangeListener(this);
        videoPauseBtn.setOnClickListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        //横竖屏转换
        screenSwitchBtn.setOnClickListener(this);
        //中间的播放按钮
        //  videoPlayImg.setOnClickListener(this);
        //注册在设置或播放过程中发生错误时调用的回调函数。如果未指定回调函数，或回调函数返回false，VideoView 会通知用户发生了错误。
        videoView.setOnErrorListener(this);
        //整个页面的布局
        viewBox.setOnTouchListener(this);
        viewBox.setOnClickListener(this);
        //下一集
        mNext.setOnClickListener(this);
        //下载
        mLoadCurrent.setOnClickListener(this);
        //清晰度
        mQuality.setOnClickListener(this);
        //选集
        mSelect_ji.setOnClickListener(this);
        //返回按钮
        mClanbiz_back.setOnClickListener(this);
        //添加手势
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        addView(view);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        duration = videoView.getDuration();
        int[] time = getMinuteAndSecond(duration);
        videoTotalTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
        formatTotalTime = String.format("%02d:%02d", time[0], time[1]);
        videoSeekBar.setMax(duration);
        progressBar.setVisibility(View.GONE);

        mp.start();
        videoPauseBtn.setEnabled(true);
        videoSeekBar.setEnabled(true);
        videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
        timer.schedule(timerTask, 0, 1000);
        mp.seekTo((int) curPosition);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        videoView.seekTo(0);
        videoSeekBar.setProgress(0);
        videoPauseImg.setImageResource(R.mipmap.icon_video_play);
        // videoPlayImg.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return mGestureDetector.onTouchEvent(event);
            case MotionEvent.ACTION_UP:
                if (touchStatusView.getVisibility() == View.VISIBLE) {
                    touchStatusView.setVisibility(View.GONE);
                }
                return mGestureDetector.onTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                return mGestureDetector.onTouchEvent(event);
        }
        return false;
    }

    private int[] getMinuteAndSecond(int mils) {
        mils /= 1000;
        int[] time = new int[2];
        time[0] = mils / 60;
        time[1] = mils % 60;
        return time;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clanbiz_back:
                screen();
                break;
           /* case R.id.videoPlayImg:
                videoView.start();
                videoPlayImg.setVisibility(View.INVISIBLE);
                videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
                break;*/
            case R.id.videoPauseBtn:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    videoPauseImg.setImageResource(R.mipmap.icon_video_play);
                    //videoPlayImg.setVisibility(View.VISIBLE);
                } else {
                    videoView.start();
                    videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
                    // videoPlayImg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.viewBox:

                break;
            case R.id.screen_status_btn:
                int i = getResources().getConfiguration().orientation;
                if (i == Configuration.ORIENTATION_PORTRAIT) {
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (i == Configuration.ORIENTATION_LANDSCAPE) {
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.share:

                break;
            case R.id.select_ji:
                selectJi();
                break;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        this.animation = false;
        this.videoControllerShow = !this.videoControllerShow;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int[] time = getMinuteAndSecond(progress);
        videoCurTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        videoView.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoView.seekTo(videoSeekBar.getProgress());
        videoView.start();
        // videoPlayImg.setVisibility(View.INVISIBLE);
        videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
    }

    public void screen() {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                Intent intent = new Intent();
                ((Activity) context).setResult(603,intent);
                ((Activity) context).finish();
                break;
        }
    }

    private void selectJi() {
        View popupWindowView = View.inflate(context, R.layout.pop_media, null);
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        ColorDrawable colorDrawable = new ColorDrawable(0x99000000);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.showAtLocation(View.inflate(context, R.layout.common_video_view, null), Gravity.RIGHT, 0, 500);
        backgroundAlpha(0.3f);
        popupWindow.setOnDismissListener(new popupDismissListener());
        TextView mSelect1 = (TextView) popupWindowView.findViewById(R.id.select_region1);
        TextView mSelect2 = (TextView) popupWindowView.findViewById(R.id.select_region2);
        TextView mSelect3 = (TextView) popupWindowView.findViewById(R.id.select_region3);
        TextView mSelect4 = (TextView) popupWindowView.findViewById(R.id.select_region4);
        GridView mGridView = (GridView) popupWindowView.findViewById(R.id.gridview);
        popMediaAdapter = new PopMediaAdapter(select1);
        mGridView.setAdapter(popMediaAdapter);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("onSingleTapUp", "onSingleTapUp");
            return false;
        }

        /**
         * 因为使用的是自定义的mediaController 当显示后，mediaController会铺满屏幕，
         * 所以VideoView的点击事件会被拦截，所以重写控制器的手势事件，
         * 将全部的操作全部写在控制器中，
         * 因为点击事件被控制器拦截，无法传递到下层的VideoView，
         * 所以 原来的单机隐藏会失效，作为代替，
         * 在手势监听中onSingleTapConfirmed（）添加自定义的隐藏/显示，
         *
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            Log.e("onSingleTapConfirmed", "onSingleTapConfirmed");
            //当手势结束，并且是单击结束时，控制器隐藏/显示
            float curY = videoControllerLayout.getY();
            float curTitleY = mTitlebar_media.getY();
            if (!animation && videoControllerShow) {
                animation = true;
                ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y",
                        curY, curY + videoControllerLayout.getHeight());
                animator.setDuration(200);
                animator.start();
                animator.addListener(CommonVideoView.this);
                ObjectAnimator animatorTitle = ObjectAnimator.ofFloat(mTitlebar_media, "y",
                        curTitleY, curTitleY - mTitlebar_media.getHeight());
                animatorTitle.setDuration(200);
                animatorTitle.start();
            } else if (!animation) {
                animation = true;
                ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y",
                        curY, curY - videoControllerLayout.getHeight());
                animator.setDuration(200);
                animator.start();
                animator.addListener(CommonVideoView.this);
                ObjectAnimator animatorTitle = ObjectAnimator.ofFloat(mTitlebar_media, "y",
                        curTitleY, curTitleY + mTitlebar_media.getHeight());
                animatorTitle.setDuration(200);
                animatorTitle.start();
                videoHandler.sendEmptyMessageDelayed(MEDIA_CONTROLLER_SHOW, 5000);
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.e("onDown", "onDown");
            progress = videoSeekBar.getProgress();
            return true;
        }

        //滑动事件监听
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            int x = (int) e2.getRawX();
            Display disp = ((Activity) context).getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            if (Math.abs(x - mOldX) < Math.abs(y - mOldY)) {
                if (mOldX > windowWidth * 3.0 / 4.0) {// 右边滑动上下滑动 屏幕 3/4
                    float cy = (mOldY - y) / 15;
                    Log.e("百分比", cy / windowHeight + "");
                    onVolumeSlide(cy / windowHeight);
                } else if (mOldX < windowWidth * 1.0 / 4.0) {// 左边滑动 屏幕 1/4
                    onBrightnessSlide((mOldY - y) / windowHeight);
                }
            } else {
                //左右滑动
                onSeekTo(x - mOldX);
            }


            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("onDoubleTap", "onDoubleTap");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }

    private void onSeekTo(float deltaX) {
        if (!videoView.isPlaying()) {
            return;
        }
       /* float currentX = event.getRawX();
        float deltaX = currentX - touchLastX;*/
        //除去5是为了降低灵敏度
        float deltaXAbs = Math.abs(deltaX / 10);
        if (deltaXAbs > 1) {
            //控制中间快进快退的显示与否
            if (touchStatusView.getVisibility() != View.VISIBLE) {
                touchStatusView.setVisibility(View.VISIBLE);
            }
            if (deltaX > 1) {
                progress += touchStep;
                if (progress > duration) {
                    progress = duration;
                }
                touchStatusImg.setImageResource(R.mipmap.ic_fast_forward_white_24dp);
                int[] time = getMinuteAndSecond(progress);
                touchStatusTime.setText(String.format("%02d:%02d/%s", time[0], time[1], formatTotalTime));
                videoView.seekTo(progress);
            } else if (deltaX < -1) {
                progress -= touchStep;
                if (progress < 0) {
                    progress = 0;
                }
                touchStatusImg.setImageResource(R.mipmap.ic_fast_rewind_white_24dp);
                int[] time = getMinuteAndSecond(progress);
                touchStatusTime.setText(String.format("%02d:%02d/%s", time[0], time[1], formatTotalTime));
                videoView.seekTo(progress);
            }

        }
    }

    /**
     * 左边上下滑动，调节亮度
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = ((Activity) context).getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }


        WindowManager.LayoutParams lpa = ((Activity) context).getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        ((Activity) context).getWindow().setAttributes(lpa);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private float volue = 0;

    private void onVolumeSlide(float percent) {
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volue = volue + percent * mMaxVolume;
        if (volue >= 1 || volue <= -1) {
            if (volue < 0) {
                //音量减小,调取系统的音量提示框
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                        AudioManager.FX_FOCUS_NAVIGATION_UP);
            } else {
                //音量变大,调取系统的音量提示框
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                        AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
            int index = (int) volue + mVolume;
            if (index > mMaxVolume)
                index = mMaxVolume;
            else if (index < 0)
                index = 0;
            // 变更声音
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
            volue = 0;
        }
    }

    public long getCurrentPosition() {
        return videoView.getCurrentPosition();
    }

    public void pause() {
        videoView.pause();
    }
}
