package com.ydh.refresh_layout.header;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydh.refresh_layout.R;
import com.ydh.refresh_layout.api.RefreshHeader;
import com.ydh.refresh_layout.api.RefreshKernel;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.constant.RefreshState;
import com.ydh.refresh_layout.constant.SpinnerStyle;
import com.ydh.refresh_layout.selfview.GifView;

/**
 * Created by Android on 2018/9/10.
 */

public class YuHeaderGif extends LinearLayout implements RefreshHeader {
    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    private View inflate;
    private ImageView mPauseImg;
    private GifView mAninationImg;
    private TextView mTitel;

    public YuHeaderGif(Context context) {
        super(context);
    }

    public YuHeaderGif(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate = View.inflate(context, R.layout.yu_header_gif, this);
        mPauseImg = (ImageView) inflate.findViewById(R.id.pause);
        mAninationImg = (GifView) inflate.findViewById(R.id.animation);
        mTitel = (TextView) inflate.findViewById(R.id.title);
        mAninationImg.setMovieResource(R.raw.timg);
        mAninationImg.setPaused(true);//刚开始时候是停止播放状态
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (mAninationImg.getVisibility() != VISIBLE) {
            mAninationImg.setVisibility(VISIBLE);
            /*Drawable drawable = mAninationImg.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            } else {
                mAninationImg.animate().rotation(36000).setDuration(100000);
            }*/
            mAninationImg.setPaused(false);//开始播放
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
       /* Drawable drawable = mAninationImg.getDrawable();
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        } else {
            mAninationImg.animate().rotation(0).setDuration(0);
        }*/
        if (success)
            mTitel.setText("刷新完成");
        else mTitel.setText("刷新失败");
        mAninationImg.setPaused(true);
        mAninationImg.setVisibility(GONE);
        return 100;//延迟500毫秒之后再弹回
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
                Log.e("位置", "None");
            case PullDownToRefresh:
                mTitel.setText("下拉可以刷新");
                mPauseImg.setVisibility(VISIBLE);
                mAninationImg.setVisibility(GONE);
                Log.e("位置", "PullDownToRefresh");
                break;
            case Refreshing:
            case RefreshReleased:
                mTitel.setText("正在刷新...");
                mPauseImg.setVisibility(GONE);
                mAninationImg.setVisibility(VISIBLE);
             /*   AnimationDrawable drawable = (AnimationDrawable)(mAninationImg.getDrawable());
                drawable.start();*/
                mAninationImg.setPaused(false);
                Log.e("位置", "RefreshReleased，Refreshing");
                break;
            case ReleaseToRefresh:
                mTitel.setText("释放立即刷新");
                Log.e("位置", "ReleaseToRefresh");
                break;
            case ReleaseToTwoLevel:
                mTitel.setText("释放进入二楼");
                Log.e("位置", "ReleaseToTwoLevel");
                break;
            case Loading:
                mPauseImg.setVisibility(GONE);
                mAninationImg.setVisibility(GONE);
                mTitel.setText("正在加载...");
                Log.e("位置", "Loading");
                break;
        }
    }
}

