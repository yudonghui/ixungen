package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class LaunchAdapter extends PagerAdapter {
    List<ImageView> mImageList;
    Context mContext;
    public LaunchAdapter(List<ImageView> mImageList,Context mContext) {
        this.mImageList = mImageList;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView imageView = mImageList.get(position);
        container.addView(imageView);
        return mImageList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageList.get(position));
    }

}
