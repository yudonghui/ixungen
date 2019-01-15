package com.ruihuo.ixungen.ui.familytree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.InterfaceTreeClick;

/**
 * @author yudonghui
 * @date 2017/11/21
 * @describe May the Buddha bless bug-free!!!
 */
public class ClickTreeView extends LinearLayout {
    private Context mContext;
    private View mInflate;
    public CircleMenuLayout mMenuLayout;
    private InterfaceTreeClick mCallBack;

    public ClickTreeView(Context context) {
        super(context);
        this.mContext = context;
        mInflate = View.inflate(mContext, R.layout.click_tree_view, this);
        initView();
    }

    public ClickTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mInflate = View.inflate(mContext, R.layout.click_tree_view, this);
        initView();
    }

    private void initView() {
        mMenuLayout = (CircleMenuLayout) mInflate.findViewById(R.id.menulayout);
        mMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                mCallBack.itemClick(view, pos);
            }

            @Override
            public void itemCenterClick(View view) {
                mCallBack.itemCenterClick(view);
            }
        });
    }

    public void setData(String[] text, InterfaceTreeClick mCallBack) {
        this.mCallBack = mCallBack;
        mMenuLayout.setMenuItemIconsAndTexts(text);
    }

}
