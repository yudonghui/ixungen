package com.ruihuo.ixungen.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.chatactivity.FriendFormActivity;
import com.ruihuo.ixungen.activity.family.SearchFriendActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class ChatFragment extends Fragment {
    private View view;
    private Context mContext;
    private LinearLayout mLl_sms;
    private TextView mSms_title;
    private View mSms_bottom;
    private LinearLayout mLl_family;
    private TextView mFamily_title;
    private View mFamily_bottom;
    private ImageView mSaoyisao;
    private ImageView mSms;
    private ImageView mAdd;
    private FrameLayout mFl_chat;
    private SmsFragment mSmsFragment;
    private FamilyFragment mFamilyFragment;
    private FragmentManager mFragmentManager;
    private int seletColor;
    private int unseletColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, null);
        mContext = getContext();
        initView();
        initFragment();
        addListener();
        return view;
    }

    private void initFragment() {
        mSmsFragment = new SmsFragment();
        mFamilyFragment = new FamilyFragment();
        mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_chat, mSmsFragment).add(R.id.fl_chat, mFamilyFragment)
                .show(mSmsFragment).hide(mFamilyFragment);
        fragmentTransaction.commit();
    }

    private void initView() {
        mLl_sms = (LinearLayout) view.findViewById(R.id.ll_sms);
        mSms_title = (TextView) view.findViewById(R.id.sms_title);
        mSms_bottom = (View) view.findViewById(R.id.sms_bottom);
        mLl_family = (LinearLayout) view.findViewById(R.id.ll_family);
        mFamily_title = (TextView) view.findViewById(R.id.family_title);
        mFamily_bottom = (View) view.findViewById(R.id.family_bottom);
        mSaoyisao = view.findViewById(R.id.saoyisao);
        mSms = (ImageView) view.findViewById(R.id.sms);
        mAdd = (ImageView) view.findViewById(R.id.add);
        mFl_chat = (FrameLayout) view.findViewById(R.id.fl_chat);
        seletColor = getResources().getColor(R.color.brown_bg);
        unseletColor = getResources().getColor(R.color.deep_txt);

    }

    private void addListener() {
        mLl_family.setOnClickListener(TabListener);
        mLl_sms.setOnClickListener(TabListener);
        mSms.setOnClickListener(SmsListener);
        mSaoyisao.setOnClickListener(new View.OnClickListener() {//扫一扫
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ConstantNum.PERMISSION_CAMERA);
                } else {
                    //调取二维码扫描
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
                }
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {//搜索朋友
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            switch (v.getId()) {
                case R.id.ll_sms:
                    mSms_bottom.setVisibility(View.VISIBLE);
                    mSms_title.setTextColor(seletColor);
                    mSms.setVisibility(View.VISIBLE);
                    mFamily_bottom.setVisibility(View.INVISIBLE);
                    mFamily_title.setTextColor(unseletColor);
                    mAdd.setVisibility(View.INVISIBLE);
                    mSaoyisao.setVisibility(View.INVISIBLE);
                    fragmentTransaction.show(mSmsFragment).hide(mFamilyFragment);
                    break;
                case R.id.ll_family:
                    mSms_bottom.setVisibility(View.INVISIBLE);
                    mSms_title.setTextColor(unseletColor);
                    mSms.setVisibility(View.INVISIBLE);
                    mFamily_bottom.setVisibility(View.VISIBLE);
                    mFamily_title.setTextColor(seletColor);
                    mAdd.setVisibility(View.VISIBLE);
                    mSaoyisao.setVisibility(View.VISIBLE);
                    fragmentTransaction.show(mFamilyFragment).hide(mSmsFragment);
                    break;
            }
            fragmentTransaction.commit();

        }
    };
    View.OnClickListener SmsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, FriendFormActivity.class);
            intent.putExtra("from", "creatDiscussion");//创建讨论组
            startActivity(intent);
        }
    };

    public void refreshFamily() {
        if (mFamilyFragment != null)
            mFamilyFragment.setDatac();
    }

    public void setFamilyRedpoints(String extra) {
        if (mFamilyFragment != null)
            mFamilyFragment.setRedPointsStatus(extra);
    }

    public void setFamilyListener(CallBackInterface callBackInterface) {
        if (mFamilyFragment != null)
            mFamilyFragment.setListener(callBackInterface);
    }

    public void refreshSms() {
        if (mSmsFragment != null)
            mSmsFragment.refreshList();//刷新会话列表。
    }

    public void redPoints(String extra) {
        if (mSmsFragment != null)
            mSmsFragment.redPoints(extra);
    }

    public void setSmsListener(CallBackInterface callBackInterface) {
        if (mSmsFragment != null)
            mSmsFragment.setListener(callBackInterface);
    }
}
