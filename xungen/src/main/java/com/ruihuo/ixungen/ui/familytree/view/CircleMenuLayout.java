package com.ruihuo.ixungen.ui.familytree.view;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

/**
 * <pre>
 * @author zhy
 * http://blog.csdn.net/lmj623565791/article/details/43131133
 * </pre>
 */
public class CircleMenuLayout extends ViewGroup {
    private int mRadius;//圆盘的半径
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 5f;
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private float mPadding;
    /**
     * 布局时的开始角度
     */
    private double mStartAngle = 225;
    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;

    /**
     * 菜单的个数
     */
    private int mMenuItemCount;

    private int mMenuItemLayoutId = R.layout.circle_menu_item;
    private Context mContext;

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 无视padding
        setPadding(0, 0, 0, 0);
    }

    /**
     * 设置布局的宽高，并策略menu item宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果宽或者高的测量模式非精确值
         */
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            // 主要设置为背景图的高度
            resWidth = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            // 如果都设置为精确值，则直接取小值；
            resWidth = resHeight = Math.min(width, height);
        }

        setMeasuredDimension(resWidth, resHeight);

        // 获得半径
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        // menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        // 迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = -1;

            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
                        childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                        childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;

    }

    /**
     * MenuItem的点击事件接口
     *
     * @author zhy
     */
    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);
    }

    /**
     * MenuItem的点击事件接口
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置MenuItem的点击事件接口
     *
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    /**
     * 设置menu item的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;

        // Laying out the child views
        final int childCount = getChildCount();
        if (childCount <= 1) return;
        int left, top;
        // menu item 的尺寸
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        // 根据menu item的个数，计算角度
        //float angleDelay = 360 / (getChildCount() - 1);
        float angleDelay = 45;
        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getId() == R.id.id_circle_menu_item_center)
                continue;

            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;

            // 计算，中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

            // tmp cosa 即menu item中心点的横坐标
            left = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            // tmp sina 即menu item的纵坐标
            top = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);

            child.layout(left, top, left + cWidth, top + cWidth);
            if (child.getId() == R.id.id_circle_menu_item_text)
                adjustTvTextSize((TextView) child, cWidth);//字体大小自适应
            // 叠加尺寸
            mStartAngle += angleDelay;
        }

        // 找到中心的view，如果存在设置onclick事件
        View cView = findViewById(R.id.id_circle_menu_item_center);
        if (cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });
            // 设置center item位置
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredWidth();
            cView.layout(cl, cl, cr, cr);
        }

    }

    private void adjustTvTextSize(TextView tv, int maxWidth) {
        String text = tv.getText().toString();
        int avaiWidth = maxWidth - tv.getPaddingLeft() - tv.getPaddingRight() - 10;

        if (avaiWidth <= 0) {
            return;
        }

        TextPaint textPaintClone = new TextPaint(tv.getPaint());
        // note that Paint text size works in px not sp
        float trySize = textPaintClone.getTextSize();

        while (textPaintClone.measureText(text) > avaiWidth) {
            trySize--;
            textPaintClone.setTextSize(trySize);
        }

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
    }

    /**
     * 设置菜单条目的图标和文本
     */
    public void setMenuItemIconsAndTexts(String[] texts) {
        mItemTexts = texts;
        // 参数检查
        if (texts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = texts == null ? texts.length : texts.length;
        addMenuItems();

    }

    private boolean CompareTwoArray(String[] a1, String[] a2) {
        if (a1 == null || a2 == null) return false;
        if (a1.length != a2.length) return false;
        for (int i = 0; i < a1.length; i++) {
            if (!a1[i].equals(a2[i])) return false;
        }
        return true;
    }

    /**
     * 添加菜单项
     */
    private void addMenuItems() {
        mStartAngle = 225;
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View childAt = getChildAt(0);
        removeAllViews();
        addView(childAt);
        Log.e("数量：", getChildCount() + "");
        /**
         * 根据用户设置的参数，初始化view
         */
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            final View view = mInflater.inflate(mMenuItemLayoutId, this, false);
            TextView tv = (TextView) view
                    .findViewById(R.id.id_circle_menu_item_text);

            if (tv != null) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(mItemTexts[i]);
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemClick(view, j);
                    }
                }
            });
            // 添加view到容器中
            addView(view);
        }
    }

    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }


}
