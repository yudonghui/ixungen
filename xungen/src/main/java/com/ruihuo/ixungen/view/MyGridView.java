package com.ruihuo.ixungen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author yudonghui
 * @date 2017/3/28
 * @describe May the Buddha bless bug-free!!!
 */
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
