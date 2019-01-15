package com.ruihuo.ixungen.geninterface;

import android.view.View;

/**
 * @author yudonghui
 * @date 2017/9/13
 * @describe May the Buddha bless bug-free!!!
 */
public interface ItemClickListener {
    void onLongClick(View v, int position);

    void onClick(View v, int position);
}
