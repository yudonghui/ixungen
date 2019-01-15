package com.ruihuo.ixungen.common.friends.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ruihuo.ixungen.R;


/**
 * @Author: duke
 * @DateTime: 2016-08-12 16:40
 * @Description: 中间提示view, 圆角矩形或者圆形背景
 */
public class CenterTipView extends View {
    //画笔
    private Paint mPaint;
    //画笔防锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    //图形背景颜色
    private int bgColor;
    //文本内容
    private String text;
    //文本颜色
    private int textColor;
    //字体大小
    private int textSize;
    //类型
    private int type;
    //圆角矩形或者圆形
    public static final int TYPE_ROUND = 0;
    public static final int TYPE_CIRCLE = 1;

    private int mWidth;//宽
    private int mHeight;//高
    private int mMin;//宽高中的最小值

    //文本边界
    private Rect mBound;

    /**
     * 设置文本，重绘界面
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public String getText() {
        return text;
    }

    public CenterTipView(Context context) {
        this(context, null);
    }

    public CenterTipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterTipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        //画笔防锯齿
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CenterTipView);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CenterTipView_bgColor:
                    //背景颜色
                    bgColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CenterTipView_textColor:
                    //文本颜色
                    textColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CenterTipView_text:
                    //文本内容
                    text = typedArray.getString(attr);
                    break;
                case R.styleable.CenterTipView_type:
                    //图形类型
                    type = typedArray.getInt(R.styleable.CenterTipView_type, 0);
                    break;
                case R.styleable.CenterTipView_textSizes:
                    //字体大小
                    textSize = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        //回收属性数组
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mMin = Math.min(mWidth, mHeight);
        //依据最小的边，方便画圆
        setMeasuredDimension(mMin, mMin);
    }

    @Override
    public void draw(Canvas canvas) {
        //防锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);
        mPaint.setColor(bgColor);
        if (type == TYPE_ROUND) {
            //画圆角矩形
            RectF rectF = new RectF(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(rectF, 10, 10, mPaint);
        } else if (type == TYPE_CIRCLE) {
            //画圆
            canvas.drawCircle(mMin >> 1, mMin >> 1, mMin >> 1, mPaint);
        }
        //设置文本颜色
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        if (mBound == null)
            mBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        canvas.drawText(text, (mWidth - mBound.width()) >> 1, (mHeight + mBound.height()) >> 1, mPaint);
        super.draw(canvas);
    }
}