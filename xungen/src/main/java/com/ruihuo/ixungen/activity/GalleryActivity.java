package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.ConsalTransformation;
import com.ruihuo.ixungen.entity.PhotoDetailBean;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/3/29
 * @ describe May the Buddha bless bug-free！！
 */
public class GalleryActivity extends AppCompatActivity {
    private TextView mTextView;
    private ViewPager mViewPager;
    private int position;
    //int[] resId = {R.drawable.test1, R.drawable.test2, R.drawable.test3};
    private ArrayList<ImageView> mList = new ArrayList<>();
    private List<PhotoDetailBean.DataBean> photoList = new ArrayList<>();
    private Context mContext;
    private int totalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);
        mContext = this;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        photoList = (List<PhotoDetailBean.DataBean>) intent.getSerializableExtra("photoList");
        initView();
        if (photoList!=null&&photoList.size()>0){
            for (int i = 0; i < photoList.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                if (!TextUtils.isEmpty(photoList.get(i).getImg_url())){
                    Picasso.with(mContext)
                            .load(photoList.get(i).getImg_url())
                            .config(Bitmap.Config.RGB_565)
                            .error(R.mipmap.default_photo)
                            .placeholder(R.mipmap.default_photo)
                            .transform(new ConsalTransformation(mContext, displayWidth,displayWidth))
                            .into(imageView);
                    mList.add(imageView);
                }
            }
            totalPage = mList.size();
            VpAdapter vpAdapter = new VpAdapter();
            mViewPager.setAdapter(vpAdapter);
            mViewPager.setCurrentItem(position);
            mTextView.setText(position + 1 + "/" + totalPage);
            addListener();
        }
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.numpage);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void addListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextView.setText(position + 1 + "/" + totalPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            mList.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }
}
