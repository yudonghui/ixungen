package com.ruihuo.ixungen.ui.familytree.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public class ViewAdapter extends PagerAdapter {
    List<View> mViewList;
    Context mContext;

    public ViewAdapter(List<View> mViewList, Context mContext) {
        this.mViewList = mViewList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}
