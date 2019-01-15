package com.ruihuo.ixungen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class SlideListView extends ListView {
    private int mScreenWidth;   // 屏幕宽度
    private int mDownX;         // 按下点的x值
    private int mDownY;         // 按下点的y值
    private int mDeleteBtnWidth;// 删除按钮的宽度

    private boolean isDeleteShown;  // 删除按钮是否正在显示

    private ViewGroup mPointChild;  // 当前处理的item
    private LinearLayout.LayoutParams mLayoutParams;    // 当前处理的item的LayoutParams

    public SlideListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                return super.onTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                int nowX = (int) ev.getX();
                int abs = Math.abs(nowX - mDownX);
                if (abs < 10)

                    performActionUp(ev);
                break;

        }
        return super.onTouchEvent(ev);

    }

    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {
        if (isDeleteShown) {
            turnToNormal();
        }

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        // 获取当前点的item
        mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition());
        // 获取删除按钮的宽度
        mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        int abs = Math.abs(nowX - mDownX);

        if (abs > Math.abs(nowY - mDownY)) {

            if (nowX < mDownX) {// 如果向左滑动
                // 计算要偏移的距离
                int scroll = nowX - mDownX;
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if (-scroll >= mDeleteBtnWidth) {
                    scroll = -mDeleteBtnWidth;
                }
                // 重新设置leftMargin
                mLayoutParams.leftMargin = scroll;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            } else {// 如果向右滑动
                if (isDeleteShown) {//当删除按钮显示的时候右滑动才有反应
                    mLayoutParams.leftMargin += mDownY - nowX;
                    mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
                }
            }
            return true;
        }

        return super.onTouchEvent(ev);
    }

    // 处理action_up事件
    private void performActionUp(MotionEvent ev) {
        // 偏移量大于button的一半，则显示button
        // 否则恢复默认
        if (isDeleteShown) {
            if (-mLayoutParams.leftMargin <= mDeleteBtnWidth / 2) {
                turnToNormal();
            } else {
                mLayoutParams.leftMargin = -mDeleteBtnWidth;
                isDeleteShown = true;
            }
        } else {
            if (-mLayoutParams.leftMargin >= mDeleteBtnWidth / 2) {
                mLayoutParams.leftMargin = -mDeleteBtnWidth;
                isDeleteShown = true;
            } else {
                turnToNormal();
            }
        }

        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    /**
     * 变为正常状态
     */
    public void turnToNormal() {
        mLayoutParams.leftMargin = 0;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isDeleteShown = false;
    }

    /**
     * 当前是否可点击
     *
     * @return 是否可点击
     */
    public boolean canClick() {
        return !isDeleteShown;
    }
}