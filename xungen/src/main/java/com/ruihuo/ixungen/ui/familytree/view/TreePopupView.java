package com.ruihuo.ixungen.ui.familytree.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.action.PopupWindowView;
import com.ruihuo.ixungen.utils.DisplayUtilX;

/**
 * @author yudonghui
 * @date 2017/11/21
 * @describe May the Buddha bless bug-free!!!
 */
public class TreePopupView extends PopupWindow {
    private int popupWidth;
    private int popupHeight;
    private TextView mOne;
    private View parentView;
    private TextView mTwo;
    private TextView mThree;
    private PopupWindowView.PopupWindowInterface mCallBack;
    private int spaceHor;
    private Context mContext;

    public TreePopupView(Context context) {
        super(context);
        mContext = context;
        spaceHor = DisplayUtilX.dip2px(10);
        initView(context);
        setPopConfig();
    }

    public void setOneText(String content, final PopupWindowView.PopupWindowInterface mCallBack) {
        mOne.setVisibility(View.VISIBLE);
        mOne.setText(content);
        mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mCallBack.callBack();
            }
        });
    }

    public void setTwoText(String content, final PopupWindowView.PopupWindowInterface mCallBack) {
        mTwo.setVisibility(View.VISIBLE);
        mTwo.setText(content);
        mTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mCallBack.callBack();
            }
        });
    }

    public void setThreeText(String content, final PopupWindowView.PopupWindowInterface mCallBack) {
        mThree.setVisibility(View.VISIBLE);
        mThree.setText(content);
        mThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mCallBack.callBack();
            }
        });
    }

    /**
     * 初始化控件
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/29,22:00
     * <h3>UpdateTime</h3> 2016/6/29,22:00
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.pop_xuanfu, null);
        mOne = (TextView) parentView.findViewById(R.id.one);
        mTwo = (TextView) parentView.findViewById(R.id.two);
        mThree = (TextView) parentView.findViewById(R.id.three);
        setContentView(parentView);
    }

    /**
     * 配置弹出框属性
     *
     * @version 1.0
     * @createTime 2015/12/1,12:45
     * @updateTime 2015/12/1,12:45
     * @createAuthor
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void setPopConfig() {
//        this.setContentView(mDataView);//设置要显示的视图
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);// 设置外部触摸会关闭窗口

    }

    public void setGon() {
        mOne.setVisibility(View.GONE);
        mTwo.setVisibility(View.GONE);
        mThree.setVisibility(View.GONE);
    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     *
     * @param v
     */
    public void showUp(View v) {
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
         /*
        * 获取悬浮按钮y轴的值，然后往下面加上悬浮按钮的半径。
        * 这个时候弹出框的顶端位置正好和悬浮球圆心高度一致。
        * 这个时候需要再往上移动弹出框高度的一半。
        * 这个时候正好弹出框的中心和悬浮球的中心位置在一条水平线上面。
        * */
        int sss = location[1] + DisplayUtilX.dip2px(25) - popupHeight / 2;
        showAtLocation(v, Gravity.NO_GRAVITY,
                (location[0]) - popupWidth - spaceHor, sss);
    }

    /**
     * 设置显示在v上方（以v的中心位置为开始位置）
     *
     * @param v
     */
    public void showUp2(View v) {
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
         /*
        * 获取悬浮按钮y轴的值，然后往下面加上悬浮按钮的半径。这个时候弹出框的顶端位置正好
        * */
        int sss = location[1] + DisplayUtilX.dip2px(25) - popupHeight / 2;
        showAtLocation(v, Gravity.NO_GRAVITY,
                (location[0]) - popupWidth - spaceHor, sss);
    }

    public interface PopupWindowInterface {
        void callBack();
    }
}
