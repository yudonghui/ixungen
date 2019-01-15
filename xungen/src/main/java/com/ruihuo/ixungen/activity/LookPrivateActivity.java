package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruihuo.ixungen.R;

public class LookPrivateActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout mLl_one;
    private ImageView mOne;
    private LinearLayout mLl_two;
    private ImageView mTwo;
    private LinearLayout mLl_three;
    private ImageView mThree;
    private LinearLayout mLl_four;
    private ImageView mFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_look_private);
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitleBar.mTextRegister.setText("完成");
        mLl_one = (LinearLayout) findViewById(R.id.ll_one);
        mOne = (ImageView) findViewById(R.id.one);
        mLl_two = (LinearLayout) findViewById(R.id.ll_two);
        mTwo = (ImageView) findViewById(R.id.two);
        mLl_three = (LinearLayout) findViewById(R.id.ll_three);
        mThree = (ImageView) findViewById(R.id.three);
        mLl_four = (LinearLayout) findViewById(R.id.ll_four);
        mFour = (ImageView) findViewById(R.id.four);
    }

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(ConfirmListener);
        mLl_one.setOnClickListener(SelectListener);
        mLl_two.setOnClickListener(SelectListener);
        mLl_three.setOnClickListener(SelectListener);
        mLl_four.setOnClickListener(SelectListener);
    }

    private int privatex = 0;
    View.OnClickListener SelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOne.setImageResource(R.mipmap.gray);
            mTwo.setImageResource(R.mipmap.gray);
            mThree.setImageResource(R.mipmap.gray);
            mFour.setImageResource(R.mipmap.gray);
            switch (v.getId()) {
                case R.id.ll_one:
                    mOne.setImageResource(R.mipmap.green);
                    privatex = 0;
                    break;
                case R.id.ll_two:
                    mTwo.setImageResource(R.mipmap.green);
                    privatex = 1;
                    break;
                case R.id.ll_three:
                    mThree.setImageResource(R.mipmap.green);
                    privatex = 2;
                    break;
                case R.id.ll_four:
                    mFour.setImageResource(R.mipmap.green);
                    privatex = 3;
                    break;
            }
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("privatex", privatex);
            setResult(2212, intent);
            finish();
        }
    };
}
