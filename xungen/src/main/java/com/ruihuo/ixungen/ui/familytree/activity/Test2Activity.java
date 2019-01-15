package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.view.BookBoxView;

public class Test2Activity extends AppCompatActivity {
    private Context mContext;
    private ViewFlipper mViewFlipper;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mContext = this;
        mGestureDetector = new GestureDetector(GestureListener);
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
        mViewFlipper.addView(new BookBoxView(this));
        mViewFlipper.addView(new BookBoxView(this));
        mViewFlipper.addView(new BookBoxView(this));
        mViewFlipper.addView(new BookBoxView(this));
    }
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return this.mGestureDetector.onTouchEvent(event);
    }
    GestureDetector.OnGestureListener GestureListener = new GestureDetector.OnGestureListener() {
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }


        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 120) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
                        R.anim.push_left_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                        R.anim.push_left_out));
                mViewFlipper.showNext();
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
                        R.anim.push_right_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                        R.anim.push_right_out));
                mViewFlipper.showPrevious();
                return true;
            }

            return false;
        }
    };
}
