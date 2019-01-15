package com.ruihuo.ixungen.ui.familytree.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ruihuo.ixungen.ui.familytree.fragment.BookFragment;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class BookFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> mViewList;

    public BookFragmentAdapter(FragmentManager fm, List<Fragment> mViewList) {
        super(fm);
        this.mViewList = mViewList;
    }

    @Override
    public Fragment getItem(int position) {
        return mViewList.get(position);
    }

    @Override
    public int getCount() {
        if (BookFragment.updateData) {
            BookFragment.updateData = false;
            notifyDataSetChanged();
        }
        return mViewList.size();
    }
}
