package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.action.FragmentAdapter;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;

import java.util.ArrayList;
import java.util.List;


public class ShopsPhotoActivity extends BaseNoTitleActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private TextView mEdit;
    private android.support.design.widget.TabLayout mTablayout;
    private android.support.v4.view.ViewPager mViewpager;
    private FragmentAdapter mVpAdapter;
    private ArrayList<Fragment> mFragmentList;
    private List<String> mTitleList;
    private int type;//1-餐饮；2-酒店,4-旅游
    private String shopId;
    private int select;//选中的tab;
    private boolean mode;//true是商户自己，可以上传照片。false是普通用户，只能查看


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_photo2);
        mContext = this;
        initView();
        type = getIntent().getIntExtra("type", 0);
        shopId = getIntent().getStringExtra("shopId");
        mode = getIntent().getBooleanExtra("mode", false);
        if (type == 1) select = ConstantNum.FOOD_CP;//初始化，刚跳进相册列表。酒店是全部，餐厅是菜品
        else select = ConstantNum.HOTEL_ALL;
        addFragment();
        mVpAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        //填充适配器
        mViewpager.setAdapter(mVpAdapter);
        //TabLayout加载viewpager
        mTablayout.setupWithViewPager(mViewpager);
        addListener();
    }


    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mEdit = (TextView) findViewById(R.id.edit);
        mEdit.setVisibility(View.GONE);
        mTablayout = (android.support.design.widget.TabLayout) findViewById(R.id.tablayout);
        mViewpager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
        mViewpager.setOffscreenPageLimit(5);
    }

    //住宿
    private PhotoFragment AllHFragment;
    private PhotoFragment WGFragment;
    private PhotoFragment DTFragment;
    private PhotoFragment KFFragment;
    private PhotoFragment GGFragment;
    //餐厅
    private PhotoFragment AllFFragment;
    private PhotoFragment CPFragment;
    private PhotoFragment HJFragment;
    //旅游
    private PhotoFragment AllTFragment;
    private PhotoFragment FJFragment;
    private PhotoFragment ZBFragment;

    private void addFragment() {
        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        if (type == 2) {
            mTitleList.add("全部");
            mTitleList.add("外观");
            mTitleList.add("大堂");
            mTitleList.add("客房");
            mTitleList.add("设施");
            //设置TabLayout的模式
            mTablayout.setTabMode(TabLayout.MODE_FIXED);
            //为TabLayout添加tab名称
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(3)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(4)));
            AllHFragment = new PhotoFragment();
            AllHFragment.setData(ConstantNum.HOTEL_ALL, shopId, mode, RefreshData);

            WGFragment = new PhotoFragment();
            WGFragment.setData(ConstantNum.HOTEL_WG, shopId, mode, RefreshData);

            DTFragment = new PhotoFragment();
            DTFragment.setData(ConstantNum.HOTEL_DT, shopId, mode, RefreshData);

            KFFragment = new PhotoFragment();
            KFFragment.setData(ConstantNum.HOTEL_KF, shopId, mode, RefreshData);

            GGFragment = new PhotoFragment();
            GGFragment.setData(ConstantNum.HOTEL_GG, shopId, mode, RefreshData);

            mFragmentList.add(AllHFragment);
            mFragmentList.add(WGFragment);
            mFragmentList.add(DTFragment);
            mFragmentList.add(KFFragment);
            mFragmentList.add(GGFragment);
        } else if (type == 1) {
            mTitleList.add("菜品");
            mTitleList.add("环境");
            mTitleList.add("全部");
            //设置TabLayout的模式
            mTablayout.setTabMode(TabLayout.MODE_FIXED);
            //为TabLayout添加tab名称
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));

            CPFragment = new PhotoFragment();
            CPFragment.setData(ConstantNum.FOOD_CP, shopId, mode, RefreshData);

            HJFragment = new PhotoFragment();
            HJFragment.setData(ConstantNum.FOOD_HJ, shopId, mode, RefreshData);

            AllFFragment = new PhotoFragment();
            AllFFragment.setData(ConstantNum.FOOD_ALL, shopId, mode, RefreshData);

            mFragmentList.add(CPFragment);
            mFragmentList.add(HJFragment);
            mFragmentList.add(AllFFragment);
        } else if (type == 4) {
            mTitleList.add("全部");
            mTitleList.add("风景");
            mTitleList.add("周边");
            //设置TabLayout的模式
            mTablayout.setTabMode(TabLayout.MODE_FIXED);
            //为TabLayout添加tab名称
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(0)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(1)));
            mTablayout.addTab(mTablayout.newTab().setText(mTitleList.get(2)));
            AllTFragment = new PhotoFragment();
            AllTFragment.setData(ConstantNum.TOUR_ALL, shopId, mode, RefreshData);

            FJFragment = new PhotoFragment();
            FJFragment.setData(ConstantNum.TOUR_FJ, shopId, mode, RefreshData);

            ZBFragment = new PhotoFragment();
            ZBFragment.setData(ConstantNum.TOUR_ZB, shopId, mode, RefreshData);


            mFragmentList.add(AllTFragment);
            mFragmentList.add(FJFragment);
            mFragmentList.add(ZBFragment);
        }

    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ////选中了tab的逻辑
                String s = tab.getText().toString();
                switch (s) {
                    case "全部":
                        if (type == 1) select = ConstantNum.FOOD_ALL;
                        else if (type == 2) select = ConstantNum.HOTEL_ALL;
                        else select = ConstantNum.TOUR_ALL;
                        break;
                    case "外观":
                        select = ConstantNum.HOTEL_WG;
                        break;
                    case "大堂":
                        select = ConstantNum.HOTEL_DT;
                        break;
                    case "客房":
                        select = ConstantNum.HOTEL_KF;
                        break;
                    case "设施":
                        select = ConstantNum.HOTEL_GG;
                        break;
                    case "菜品":
                        select = ConstantNum.FOOD_CP;
                        break;
                    case "环境":
                        select = ConstantNum.FOOD_HJ;
                        break;
                    case "风景":
                        select = ConstantNum.TOUR_FJ;
                        break;
                    case "周边":
                        select = ConstantNum.TOUR_ZB;
                        break;


                }
                Log.e("onTabSelected", s);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中tab的逻辑
                String s = tab.getText().toString();
                Log.e("onTabUnselected", s);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //再次选中tab的逻辑
                String s = tab.getText().toString();
                Log.e("onTabReselected", s);
            }
        });
    }

    CallBackPositionInterface RefreshData = new CallBackPositionInterface() {
        @Override
        public void callBack(int mode) {
            if (mode == ConstantNum.FOOD_ALL) {
                CPFragment.refreshData();
                HJFragment.refreshData();
                return;
            }
            if (mode == ConstantNum.HOTEL_ALL) {
                WGFragment.refreshData();
                DTFragment.refreshData();
                KFFragment.refreshData();
                GGFragment.refreshData();
                return;
            }
            if (mode == ConstantNum.TOUR_ALL) {
                ZBFragment.refreshData();
                FJFragment.refreshData();
                return;
            }
            if (type == 1) {//餐厅
                AllFFragment.refreshData();
            } else if (type == 2) {//住宿
                AllHFragment.refreshData();
            } else if (type == 4) {//旅游
                AllTFragment.refreshData();
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
