package com.ruihuo.ixungen.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.PicassoUtils;


/**
 * @author yudonghui
 * @date 2017/7/19
 * @describe May the Buddha bless bug-free!!!
 */
public class MovieCardView extends LinearLayout {
    private View mInflate;
    private Context mContext;
    private ImageView mImage_bg;
    private TextView mRight_top;
    private TextView mRight_bottom;
    private TextView mName;
    private TextView mRemark;

    public MovieCardView(Context context) {
        super(context);
        mContext = context;
        mInflate = View.inflate(context, R.layout.movie_card, this);
    }

    public MovieCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflate = View.inflate(context, R.layout.movie_card, this);
    }

    public MovieCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflate = View.inflate(context, R.layout.movie_card, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImage_bg = (ImageView) mInflate.findViewById(R.id.image_bg);
        mRight_top = (TextView) mInflate.findViewById(R.id.right_top);
        mRight_bottom = (TextView) mInflate.findViewById(R.id.right_bottom);
        mName = (TextView) mInflate.findViewById(R.id.name);
        mRemark = (TextView) mInflate.findViewById(R.id.remark);
    }

    public void setBg(String imgUrl) {//设置背景图片
        PicassoUtils.setImgView(imgUrl, R.mipmap.default_long, mContext, mImage_bg);
    }

    public void setRightTop(String type) {
        mRight_top.setVisibility(VISIBLE);
        mRight_top.setText(TextUtils.isEmpty(type) ? "" : type);
    }

    public void setRightBottom(String string) {
        mRight_bottom.setVisibility(VISIBLE);
        mRight_bottom.setText(TextUtils.isEmpty(string) ? "" : string);
    }

    public void setName(String name) {
        mName.setText(TextUtils.isEmpty(name) ? "" : name);
    }

    public void setRemark(String remark) {
        mRemark.setText(TextUtils.isEmpty(remark) ? "" : remark);
    }
}
