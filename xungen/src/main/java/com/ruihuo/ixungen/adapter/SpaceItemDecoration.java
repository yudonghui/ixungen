package com.ruihuo.ixungen.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author yudonghui
 * @date 2017/9/6
 * @describe May the Buddha bless bug-free!!!
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != 0)
            outRect.left = mSpace;
    }
}
