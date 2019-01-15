package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/8/8
 * @describe May the Buddha bless bug-free!!!
 */
public class StarView extends LinearLayout {
    private Context mContext;
    private View inflate;
    private ImageView mOne_star;
    private ImageView mTwo_star;
    private ImageView mThree_star;
    private ImageView mFour_star;
    private ImageView mFive_star;

    public StarView(Context context) {
        super(context);
        mContext = context;
        inflate = View.inflate(context, R.layout.star_view, this);

    }

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate = View.inflate(context, R.layout.star_view, this);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate = View.inflate(context, R.layout.star_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mOne_star = (ImageView) findViewById(R.id.one_star);
        mTwo_star = (ImageView) findViewById(R.id.two_star);
        mThree_star = (ImageView) findViewById(R.id.three_star);
        mFour_star = (ImageView) findViewById(R.id.four_star);
        mFive_star = (ImageView) findViewById(R.id.five_star);
    }

    public void setLevel(String level) {
        switch (level) {
            case "1":
                mOne_star.setImageResource(R.mipmap.star_half);
                mTwo_star.setImageResource(R.mipmap.star_gray);
                mThree_star.setImageResource(R.mipmap.star_gray);
                mFour_star.setImageResource(R.mipmap.star_gray);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "2":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_gray);
                mThree_star.setImageResource(R.mipmap.star_gray);
                mFour_star.setImageResource(R.mipmap.star_gray);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "3":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_half);
                mThree_star.setImageResource(R.mipmap.star_gray);
                mFour_star.setImageResource(R.mipmap.star_gray);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "4":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_gray);
                mFour_star.setImageResource(R.mipmap.star_gray);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "5":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_half);
                mFour_star.setImageResource(R.mipmap.star_gray);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "6":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_full);
                mFour_star.setImageResource(R.mipmap.star_gray);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "7":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_full);
                mFour_star.setImageResource(R.mipmap.star_half);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "8":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_full);
                mFour_star.setImageResource(R.mipmap.star_full);
                mFive_star.setImageResource(R.mipmap.star_gray);
                break;
            case "9":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_full);
                mFour_star.setImageResource(R.mipmap.star_full);
                mFive_star.setImageResource(R.mipmap.star_half);
                break;
            case "10":
                mOne_star.setImageResource(R.mipmap.star_full);
                mTwo_star.setImageResource(R.mipmap.star_full);
                mThree_star.setImageResource(R.mipmap.star_full);
                mFour_star.setImageResource(R.mipmap.star_full);
                mFive_star.setImageResource(R.mipmap.star_full);
                break;
        }
    }
}
