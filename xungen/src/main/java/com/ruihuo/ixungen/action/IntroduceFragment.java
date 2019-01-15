package com.ruihuo.ixungen.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/5/26
 * @describe May the Buddha bless bug-free!!!
 */
public class IntroduceFragment extends Fragment {
    private View view;
    private IntroduceBean introduceBean;
    private Context mContext;
    private TextView mName;
    private TextView mSex;
    private TextView mAge;
    private TextView mBirthday;
    private TextView mHeight;
    private TextView mWeight;
    private TextView mHobby;
    private TextView mManifesto;

    /* private ImageButton mXuanfu;
     private PopupWindowView popupWindowView;
 */
    public void setData(IntroduceBean introduceBean) {
        this.introduceBean = introduceBean;
        setView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_introduce, null);
        mContext = getActivity();
        initView();
        addData();
        addListener();
        return view;
    }

    private void initView() {
        mName = (TextView) view.findViewById(R.id.name);
        mSex = (TextView) view.findViewById(R.id.sex);
        mAge = (TextView) view.findViewById(R.id.age);
        mBirthday = (TextView) view.findViewById(R.id.birthday);
        mHeight = (TextView) view.findViewById(R.id.height);
        mWeight = (TextView) view.findViewById(R.id.weight);
        mHobby = (TextView) view.findViewById(R.id.hobby);
        mManifesto = (TextView) view.findViewById(R.id.manifesto);
      /*  mXuanfu = (ImageButton) view.findViewById(R.id.xuanfu);
        popupWindowView = new PopupWindowView(mContext);*/
    }

    private void setView() {
        IntroduceBean.DataBean data = introduceBean.getData();
        String nikename = data.getNikename();
        String sex = data.getSex();
        String age = data.getAge();
        String birthday = data.getBirthday();
        String height = data.getHeight();
        String weight = data.getWeight();
        String interest = data.getInterest();
        String signature = data.getSignature();
        mName.setText(TextUtils.isEmpty(nikename) ? "" : nikename + "个人资料");
        mAge.setText(TextUtils.isEmpty(age) ? "" : age);
        mBirthday.setText(TextUtils.isEmpty(birthday) ? "" : birthday);
        mHeight.setText(TextUtils.isEmpty(height) ? "" : height + "CM");
        mWeight.setText(TextUtils.isEmpty(weight) ? "" : weight + "KG");
        mHobby.setText(TextUtils.isEmpty(interest) ? "" : interest);
        mManifesto.setText(TextUtils.isEmpty(signature) ? "" : signature);
        if (!TextUtils.isEmpty(sex)) {
            switch (sex) {
                case "0":
                    mSex.setText("保密");
                    break;
                case "1":
                    mSex.setText("男");
                    break;
                case "2":
                    mSex.setText("女");
                    break;
            }
        }
    }

    private void addData() {

    }

    private void addListener() {
        /*mXuanfu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowView.showUp(mXuanfu);
            }
        });*/

    }

    public void setXuanfu(PopupWindowView popupWindowView, ImageButton mXuanfu) {
        popupWindowView.setOneText("编辑资料", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                Intent intent = new Intent(mContext, ActionEditInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("introduceBean", introduceBean);
                intent.putExtras(bundle);
                ((Activity) mContext).startActivityForResult(intent, 220);
            }
        });
        popupWindowView.showUp(mXuanfu);
    }
}
