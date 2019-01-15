package com.ruihuo.ixungen.ui.familytree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class XSLYFragment extends Fragment {
    private View mInflate;
    private TextView mContent;
    private Context mContext;
    private String surnameIntroduce;

    public void setData(String surnameIntroduce) {
        this.surnameIntroduce = surnameIntroduce;
        if (mContent != null) mContent.setText(TextUtils.isEmpty(surnameIntroduce) ? "无" : surnameIntroduce);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_xsly, null);
        mContext = getContext();
        initView();
        addData();
        addListener();
        return mInflate;
    }

    private void initView() {
        mContent = (TextView) mInflate.findViewById(R.id.content);
        mContent.setText(TextUtils.isEmpty(surnameIntroduce) ? "无" : surnameIntroduce);
    }

    private void addListener() {

    }

    private void addData() {

    }

}
