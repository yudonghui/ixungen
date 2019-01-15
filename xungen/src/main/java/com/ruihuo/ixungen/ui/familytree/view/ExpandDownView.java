package com.ruihuo.ixungen.ui.familytree.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/10/24
 * @describe May the Buddha bless bug-free!!!
 */
public class ExpandDownView extends LinearLayout {
    private View mInflate;
    private TextView mExpand;
    private Context mContext;

    public ExpandDownView(Context context) {
        super(context);
        mContext = context;
        mInflate = View.inflate(context, R.layout.view_expand_down, this);
        mExpand = (TextView) mInflate.findViewById(R.id.expand);
    }

    public void setViewSize(int textsize) {
        mExpand.setTextSize(textsize);
    }

}
