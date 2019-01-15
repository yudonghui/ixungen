package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.BaseMediaBitrateConfig;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.utils.LogUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class VideoPreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "VideoPreviewActivity";
    private static final int RES_CODE = 111;

    /**
     * 播放进度
     */
    private static final int PLAY_PROGRESS = 110;

    private VideoView videoViewShow;
    private ImageView imageViewShow;
    private ImageView mBack;
    private TextView mSend;
    private EditText mEditText;
    //private RelativeLayout rlBottomRoot;
    private Button buttonPlay;
    private TextView textViewCountDown;
    private ProgressBar progressVideo;
    /**
     * 视频路径
     */
    private String path;
    /**
     * 视频时间
     */
    private int time;
    private int currentTime;
    private Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_PROGRESS:
//                    LogUtil.e(LOG_TAG, "getDuration：" + videoViewShow.getDuration() + "...getCurrentPosition：" + videoViewShow.getCurrentPosition());
                    time = (videoViewShow.getDuration() + 1000) / 1000;
                    currentTime = (videoViewShow.getCurrentPosition() + 1500) / 1000;
                    progressVideo.setMax(videoViewShow.getDuration());
//                    LogUtil.e(LOG_TAG, time + "..time：" + currentTime);
                    progressVideo.setProgress(videoViewShow.getCurrentPosition());
                    if (currentTime < 10) {
                        textViewCountDown.setText("00:0" + currentTime);
                    } else {
                        textViewCountDown.setText("00:" + currentTime);
                    }
                    //currentTime++;
                    //达到指定时间，停止播放
                    if (!videoViewShow.isPlaying() && time > 0) {
                        progressVideo.setProgress(videoViewShow.getDuration());
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                    break;
            }
        }
    };
    /**
     * 要上传的视频文件
     */
    private File file;
    private String from;

    private void assignViews() {
        videoViewShow = (VideoView) findViewById(R.id.videoView_show);
        imageViewShow = (ImageView) findViewById(R.id.imageView_show);
        //rlBottomRoot = (RelativeLayout) findViewById(R.id.rl_bottom_root);
        buttonPlay = (Button) findViewById(R.id.button_play);
        textViewCountDown = (TextView) findViewById(R.id.textView_count_down);
        progressVideo = (ProgressBar) findViewById(R.id.progressBar_loading);
        mBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mSend = (TextView) findViewById(R.id.text_send);
        mEditText = (EditText) findViewById(R.id.edit_text);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        initSmallVideo(this);
        assignViews();
        initView();
        initData();
    }

    public void initView() {

        // buttonDone.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSend.setOnClickListener(this);
        textViewCountDown.setText("00:00");

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoViewShow.getLayoutParams();
        layoutParams.height = width * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        //LogUtil.e(LOG_TAG, "mSurfaceViewWidth:" + width + "...mSurfaceViewHeight:" + layoutParams.height);
        videoViewShow.setLayoutParams(layoutParams);
        imageViewShow.setLayoutParams(layoutParams);
    }

    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getStringExtra("path");
            from = intent.getStringExtra("from");
            file = new File(path);
        }

        //获取第一帧图片，预览使用
        if (file.length() != 0) {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(path);
            Bitmap bitmap = media.getFrameAtTime();
            imageViewShow.setImageBitmap(bitmap);
        }
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        textViewCountDown.setText("00:00");
        progressVideo.setProgress(0);

        //视频控制面板，不需要可以不设置
        //MediaController controller = new MediaController(this);
        //controller.setVisibility(View.GONE);
        //videoViewShow.setMediaController(controller);
        videoViewShow.setVideoPath(path);
        videoViewShow.start();
        videoViewShow.requestFocus();
        videoViewShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!videoViewShow.isPlaying()) {
                    buttonPlay.setVisibility(View.VISIBLE);
                }
            }
        });

        currentTime = 0;//时间计数器重新赋值
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(PLAY_PROGRESS);
            }
        }, 0, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                buttonPlay.setVisibility(View.GONE);
                imageViewShow.setVisibility(View.GONE);
                playVideo();
                break;
            case R.id.text_send:
                upload();
                break;
            case R.id.image_titlebar_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoViewShow.stopPlayback();
    }

    private LoadingDialogUtils loadingDialogUtils;

    private void upload() {
        if ("VideoEditActivity".equals(from)) {
            BaseMediaBitrateConfig compressMode = new AutoVBRMode();
            compressMode.setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST);
            int iRate = 0;//视频帧率，0 是使用原视频的帧率
            float fScale = (float) 2;//缩放视频比例
            LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
            final LocalMediaConfig config = buidler
                    .setVideoPath(path)
                    .captureThumbnailsTime(1)
                    .doH264Compress(compressMode)
                    .setFramerate(iRate)
                    .setScale(fScale)
                    .build();
            final long start = System.currentTimeMillis();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("", "开始压缩...");
                            loadingDialogUtils = new LoadingDialogUtils(VideoPreviewActivity.this);
                        }
                    });
                    OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  hideProgress();
                            Log.e("", "压缩完成...");
                        }
                    });
                    long end = System.currentTimeMillis();
                    Log.e("压缩时间", "---" + (end - start));
                    load(onlyCompressOverBean.getVideoPath());
                }
            }).start();
        } else {
            loadingDialogUtils = new LoadingDialogUtils(VideoPreviewActivity.this);
            load(path);
        }

    }

    //上传视频
    private void load(String path) {
        if (TextUtils.isEmpty(path)) {
            loadingDialogUtils.setDimiss();
            return;
        }
        RequestParams entity = new RequestParams(Url.UPLOAD_VIDEO);
        File file = new File(path);
        entity.addBodyParameter("token", XunGenApp.token);
        String value = mEditText.getText().toString();
        if (!TextUtils.isEmpty(value))
            entity.addBodyParameter("remark", value);
        entity.addBodyParameter("video", file);
        entity.setConnectTimeout(60000);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtils.toast(VideoPreviewActivity.this, "上传成功");
                        Intent intent = new Intent(ConstantNum.ACTION_USERINFO);
                        intent.putExtra("from", "VideoPreviewActivity");
                        sendBroadcast(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
                loadingDialogUtils.setDimiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.e("onCancelled", "取消");
            }

            @Override
            public void onFinished() {
                LogUtils.e("onFinished", "上传完成");
            }
        });
    }

    //压缩视频用
    public static void initSmallVideo(Context context) {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/xungen/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/xungen/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/xungen/");
        }
        // 初始化拍摄SDK，必须
        JianXiCamera.initialize(false, null);
    }

}
