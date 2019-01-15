package com.ruihuo.ixungen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class ForumScrollView extends ScrollView {
    private OnScrollistener onScrollistener;

    public OnScrollistener getOnScrollistener() {
        return onScrollistener;
    }

    public void setOnScrollistener(OnScrollistener onScrollistener) {
        this.onScrollistener = onScrollistener;
    }

    public ForumScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ForumScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForumScrollView(Context context) {
        super(context);
    }

    public interface OnScrollistener {
        void onScroll(int startY, int endY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        onScrollistener.onScroll(oldt, t);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
