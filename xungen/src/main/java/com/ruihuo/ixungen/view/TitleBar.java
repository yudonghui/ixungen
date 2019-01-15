package com.ruihuo.ixungen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;


/**
 * @ author yudonghui
 * @ date 2017/1/19
 * @ describe May the Buddha bless bug-free！！
 */
public class TitleBar extends LinearLayout {
    private View mInflate;
    private TextView mTextView;
    public ImageView mImageBack;
    public TextView mTextRegister;
    public ImageView mSetting;
    private LinearLayout mLlTitle;
    private TextView mLlText;
    private ImageView mLlImage;
    public ImageView mShare;
    public ImageView mGroups;
    public Context mContext;

    public TitleBar(Context context) {
        super(context);
        mContext = context;
        mInflate = View.inflate(context, R.layout.titlebar, this);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflate = View.inflate(context, R.layout.titlebar, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextView = (TextView) mInflate.findViewById(R.id.titlebar);
        mImageBack = (ImageView) mInflate.findViewById(R.id.image_titlebar_back);
        mTextRegister = (TextView) mInflate.findViewById(R.id.text_register);
        mSetting = (ImageView) mInflate.findViewById(R.id.setting);
        mLlTitle = (LinearLayout) mInflate.findViewById(R.id.ll_titlebar);
        mLlText = (TextView) mInflate.findViewById(R.id.text_ll_titlebar);
        mLlImage = (ImageView) mInflate.findViewById(R.id.image_ll_titlebar);
        mShare = (ImageView) mInflate.findViewById(R.id.share);
        mGroups= (ImageView) mInflate.findViewById(R.id.groups);
    }

    //设置标题
    public void setTitle(String title) {
        mTextView.setText(title);
    }

    public void setTitleSize(float size) {
        mTextView.setTextSize(size);
    }

    //设置文字标题的显隐
    public void setTitleVisibility(boolean visibility) {
        mTextView.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setTitleRegisterVisibility(boolean visibility) {
        mTextRegister.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setBackVisibility(boolean visibility) {
        mImageBack.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setSettingVisibility(boolean visibility) {
        mSetting.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setLlVisibility(boolean visibility) {
        mLlTitle.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setLlText(String llText) {
        mLlText.setText(llText);
    }
}
