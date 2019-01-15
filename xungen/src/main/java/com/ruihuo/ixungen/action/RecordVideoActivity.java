package com.ruihuo.ixungen.action;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.utils.DisplayUtilX;

import java.io.File;
import java.io.IOException;

public class RecordVideoActivity extends AppCompatActivity {
    private static final String LOG_TAG = "RecordVideoActivity";

    private static final int RES_CODE = 111;
    /**
     * 录制进度
     */
    private static final int RECORD_PROGRESS = 100;
    /**
     * 录制结束
     */
    private static final int RECORD_FINISH = 101;

    private ImageView mBack;
    private MovieRecorderView movieRecorderView;
    private Button buttonShoot;
    private ProgressBar progressVideo;
    private TextView textViewCountDown;
    private TextView textViewUpToCancel;//上移取消
    private TextView textViewReleaseToCancel;//释放取消
    /**
     * 是否结束录制
     */
    private boolean isFinish = true;
    /**
     * 是否触摸在松开取消的状态
     */
    private boolean isTouchOnUpToCancel = false;
    /**
     * 当前进度
     */
    private int currentTime = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD_PROGRESS:
                    progressVideo.setProgress(currentTime);
                    if (currentTime < 10) {
                        textViewCountDown.setText("00:0" + currentTime);
                    } else {
                        textViewCountDown.setText("00:" + currentTime);
                    }
                    break;
                case RECORD_FINISH:
                    if (isTouchOnUpToCancel) {//录制结束，还在上移删除状态没有松手，就复位录制
                        resetData();
                    } else {//录制结束，在正常位置，录制完成跳转页面
                        isFinish = true;
                        buttonShoot.setEnabled(false);
                        finishActivity();
                    }
                    break;
            }
        }
    };
    /**
     * 按下的位置
     */
    private float startY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        initView();
        registerBoradcastReceiver();
    }

    public void initView() {
        mBack = (ImageView) findViewById(R.id.back);
        movieRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        buttonShoot = (Button) findViewById(R.id.button_shoot);
        //progressVideo = (DonutProgress) findViewById(R.id.progress_video);
        progressVideo = (ProgressBar) findViewById(R.id.progressBar_loading);
        textViewCountDown = (TextView) findViewById(R.id.textView_count_down);
        textViewCountDown.setText("00:00");
        textViewUpToCancel = (TextView) findViewById(R.id.textView_up_to_cancel);
        textViewReleaseToCancel = (TextView) findViewById(R.id.textView_release_to_cancel);

        // DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = DisplayUtilX.getDisplayWidth();
        // FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) movieRecorderView.getLayoutParams();
        // layoutParams.height = width * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        //LogUtil.e(LOG_TAG, "mSurfaceViewWidth:" + width + "...mSurfaceViewHeight:" + layoutParams.height);
        // movieRecorderView.setLayoutParams(layoutParams);

     /*   FrameLayout.LayoutParams rlBottomRootLayoutParams = (FrameLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = width / 3 * 2;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);*/
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //处理触摸事件
        buttonShoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textViewUpToCancel.setVisibility(View.VISIBLE);//提示上移取消

                    isFinish = false;//开始录制
                    startY = event.getY();//记录按下的坐标
                    movieRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(RECORD_FINISH);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textViewUpToCancel.setVisibility(View.GONE);
                    textViewReleaseToCancel.setVisibility(View.GONE);

                    if (startY - event.getY() > 100) {//上移超过一定距离取消录制，删除文件
                        if (!isFinish) {
                            resetData();
                        }
                    } else {
                        if (movieRecorderView.getTimeCount() > 3) {//录制时间超过三秒，录制完成
                            handler.sendEmptyMessage(RECORD_FINISH);
                        } else {//时间不足取消录制，删除文件
                            Toast.makeText(RecordVideoActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                            resetData();
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //根据触摸上移状态切换提示
                    if (startY - event.getY() > 100) {
                        isTouchOnUpToCancel = true;//触摸在松开就取消的位置
                        if (textViewUpToCancel.getVisibility() == View.VISIBLE) {
                            textViewUpToCancel.setVisibility(View.GONE);
                            textViewReleaseToCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        isTouchOnUpToCancel = false;//触摸在正常录制的位置
                        if (textViewUpToCancel.getVisibility() == View.GONE) {
                            textViewUpToCancel.setVisibility(View.VISIBLE);
                            textViewReleaseToCancel.setVisibility(View.GONE);
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    resetData();
                }
                return true;
            }
        });

        progressVideo.setMax(30);
        movieRecorderView.setOnRecordProgressListener(new MovieRecorderView.OnRecordProgressListener() {
            @Override
            public void onProgressChanged(int maxTime, int currentTime) {
                RecordVideoActivity.this.currentTime = currentTime;
                handler.sendEmptyMessage(RECORD_PROGRESS);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCameraPermission();
    }

    /**
     * 检测摄像头和录音权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            Toast.makeText(this, "视频录制和录音没有授权", Toast.LENGTH_LONG);
            this.finish();
        } else {
            resetData();
        }
    }

    /**
     * 重置状态
     */
    private void resetData() {
        if (movieRecorderView.getRecordFile() != null)
            movieRecorderView.getRecordFile().delete();
        movieRecorderView.stop();
        isFinish = true;
        currentTime = 0;
        progressVideo.setProgress(0);
        textViewCountDown.setText("00:00");
        buttonShoot.setEnabled(true);
        textViewUpToCancel.setVisibility(View.GONE);
        textViewReleaseToCancel.setVisibility(View.GONE);
        try {
            movieRecorderView.initCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = true;
        movieRecorderView.stop();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                if (!deleteDir(new File(dir, children[i]))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(br);
        //TODO 退出界面删除文件，如果要删除文件夹，需要提供文件夹路径
        if (movieRecorderView.getRecordFile() != null) {
            File file = new File(movieRecorderView.getRecordFile().getAbsolutePath());
            if (file != null && file.exists()) {
                Log.e(LOG_TAG, "file.exists():" + file.exists());
                file.delete();
            }
        }
        super.onDestroy();
    }

    /**
     * TODO 录制完成需要做的事情
     */
    private void finishActivity() {
        if (isFinish) {
            movieRecorderView.stop();
            Intent intent = new Intent(this, VideoPreviewActivity.class);
            intent.putExtra("path", movieRecorderView.getRecordFile().getAbsolutePath());
            startActivityForResult(intent, 602);
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 602 && resultCode == 601) {
            setResult(603, data);
            finish();
        }
    }
}
