/**
 *
 */
package com.ruihuo.ixungen.ui.familytree.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ruihuo.ixungen.R;

/**
 * 自定义的View,绘制线调条
 *
 * @author 于东辉2017年7月27日 14:15:34
 */
@SuppressLint("DrawAllocation")
public class DrawGeometryView extends View {
    private float beginx;
    private float beginy;
    private float stopx;
    private float stopy;
    private float width;
    private float height;
    private Context mContext;

    /**
     * @param context
     * @param attrs
     */
    public DrawGeometryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * @param context
     */
    public DrawGeometryView(Context context, float beginx, float beginy, float stopx, float stopy) {
        super(context);
        this.mContext = context;
        this.beginx = beginx;
        this.beginy = beginy;
        this.stopx = stopx;
        this.stopy = stopy;
        width = Math.abs(this.beginx - this.stopx);
        height = Math.abs(this.beginy - this.stopy);
        // Log.e("起点-终点", beginx + "," + beginy + "-" + stopx + "," + stopy);

    }

    public DrawGeometryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private float paintWidth = 2;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint redPaint = new Paint(); // 红色画笔
        redPaint.setAntiAlias(true); // 抗锯齿效果,显得绘图平滑
        redPaint.setColor(ContextCompat.getColor(mContext, R.color.browns_bg)); // 设置画笔颜色
        redPaint.setStrokeWidth(paintWidth);// 设置笔触宽度
        redPaint.setStyle(Style.STROKE);// 设置画笔的填充类型(完全填充)
        redPaint.setTextSize(50);//字体

        Path mPath = new Path();


        mPath.reset();
        if (stopx < beginx) {
            //起点
            mPath.moveTo(width + paintWidth / 2, 0);
            //贝塞尔曲线
            // mPath.cubicTo(width - width / 5, height, width / 5, 0, 2.5f, height);
            mPath.lineTo(width + paintWidth / 2, height / 2);
            mPath.lineTo(paintWidth / 2, height / 2);
            mPath.lineTo(paintWidth / 2, height);
            //画path
            canvas.drawPath(mPath, redPaint);
        } else if (stopx > beginx) {
            mPath.moveTo(paintWidth / 2, 0);
            mPath.lineTo(paintWidth / 2, height / 2);
            mPath.lineTo(width, height / 2);
            mPath.lineTo(width, height);
            //贝塞尔曲线
            //  mPath.cubicTo(width / 5, height, width - width / 5, 0, width, height);
            //画path
            canvas.drawPath(mPath, redPaint);
        } else {
            canvas.drawLine(paintWidth / 2, 0, paintWidth / 2, height, redPaint);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) (width + 5), (int) (height + 5));
    }
}
