package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.LocationUtils;
import com.ruihuo.ixungen.utils.NetworkUtils;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.dialog.SexDialogUtils;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionEditInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImage_titlebar_back;
    private LinearLayout mLl_header;
    private ImageView mImage_header;
    private LinearLayout mLl_username;
    private TextView mText_username;
    private LinearLayout mLl_sex;
    private TextView mText_sex;
    private LinearLayout mLl_age;
    private TextView mText_age;
    private LinearLayout mLl_birthday;
    private TextView mText_birthday;
    private LinearLayout mLl_height;
    private TextView mText_height;
    private LinearLayout mLl_weight;
    private TextView mText_weight;
    private LinearLayout mLl_hobby;
    private TextView mText_hobby;
    private EditText mManifesto;
    private TextView mConfirm;
    private Context mContext;
    private String avatar;
    private String userName;
    private String sex;
    private String age;
    private String birthday;
    private String height;
    private String weight;
    private String hobby;
    private String manifesto;
    private IntroduceBean introduceBean;
    private PhotoUtils mPhotoUtils;
    private String loginLat;
    private String loginLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_edit_info);
        introduceBean = (IntroduceBean) getIntent().getSerializableExtra("introduceBean");
        mContext = this;
        String location = LocationUtils.getLocation(this);
        //第一个是jing度，第二个wei度
        String[] split = location.split(",");
        loginLng = split[0];
        loginLat = split[1];
        initView();
        setView();
        addData();
        addListener();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mLl_header = (LinearLayout) findViewById(R.id.ll_header);
        mImage_header = (ImageView) findViewById(R.id.image_header);
        mLl_username = (LinearLayout) findViewById(R.id.ll_username);
        mText_username = (TextView) findViewById(R.id.text_username);
        mLl_sex = (LinearLayout) findViewById(R.id.ll_sex);
        mText_sex = (TextView) findViewById(R.id.text_sex);
        mLl_age = (LinearLayout) findViewById(R.id.ll_age);
        mText_age = (TextView) findViewById(R.id.text_age);
        mLl_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        mText_birthday = (TextView) findViewById(R.id.text_birthday);
        mLl_height = (LinearLayout) findViewById(R.id.ll_height);
        mText_height = (TextView) findViewById(R.id.text_height);
        mLl_weight = (LinearLayout) findViewById(R.id.ll_weight);
        mText_weight = (TextView) findViewById(R.id.text_weight);
        mLl_hobby = (LinearLayout) findViewById(R.id.ll_hobby);
        mText_hobby = (TextView) findViewById(R.id.text_hobby);
        mManifesto = (EditText) findViewById(R.id.manifesto);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mPhotoUtils = new PhotoUtils(mContext);
    }

    private void setView() {
        IntroduceBean.DataBean data = introduceBean.getData();
        userName = data.getNikename();
        sex = data.getSex();
        age = data.getAge();
        birthday = data.getBirthday();
        height = data.getHeight();
        weight = data.getWeight();
        hobby = data.getInterest();
        manifesto = data.getSignature();
        avatar = data.getAvatar();
        mText_username.setText(TextUtils.isEmpty(userName) ? "" : userName);
        mText_age.setText(TextUtils.isEmpty(age) ? "" : age);
        mText_birthday.setText(TextUtils.isEmpty(birthday) ? "" : birthday);
        mText_height.setText(TextUtils.isEmpty(height) ? "" : height + "CM");
        mText_weight.setText(TextUtils.isEmpty(weight) ? "" : weight + "KG");
        mText_hobby.setText(TextUtils.isEmpty(hobby) ? "" : hobby);
        mManifesto.setText(TextUtils.isEmpty(manifesto) ? "" : manifesto);
        if (!TextUtils.isEmpty(sex)) {
            switch (sex) {
                case "0":
                    mText_sex.setText("保密");
                    break;
                case "1":
                    mText_sex.setText("男");
                    break;
                case "2":
                    mText_sex.setText("女");
                    break;
            }
        }
        //头像
        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(mContext)
                    .load(Url.PHOTO_URL + avatar)
                    .placeholder(R.mipmap.default_header)
                    .error(R.mipmap.default_header)
                    .into(mImage_header);
        }
    }

    private void addData() {

    }

    private void addListener() {
        mLl_header.setOnClickListener(HeaderListener);
        mLl_username.setOnClickListener(this);
        mLl_sex.setOnClickListener(SexListener);
        mLl_age.setOnClickListener(BirthdayListener);
        mLl_birthday.setOnClickListener(BirthdayListener);
        mLl_height.setOnClickListener(this);
        mLl_weight.setOnClickListener(this);
        mLl_hobby.setOnClickListener(this);
        mConfirm.setOnClickListener(ConfirmListener);
    }

    @Override
    public void onClick(View v) {
        String title = "";
        EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
        switch (v.getId()) {
            case R.id.ll_username:
                title = "请输入用户名";
                editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mText_username.setText(message);
                            userName = message;
                        }

                    }
                });
                break;
            case R.id.ll_height:
                title = "请输入身高(CM)";
                editDialogUtils.setConfirmNum("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mText_height.setText(message + "CM");
                            height = message;
                        }

                    }
                });
                break;
            case R.id.ll_weight:
                title = "请输入体重(KG)";
                editDialogUtils.setConfirmNum("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mText_weight.setText(message + "KG");
                            weight = message;
                        }

                    }
                });
                break;
            case R.id.ll_hobby:
                title = "请输入兴趣爱好";
                editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mText_hobby.setText(message);
                            hobby = message;
                        }

                    }
                });
                break;
        }
        editDialogUtils.setTitle(title);
    }

    View.OnClickListener HeaderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoUtils.showDialog();
        }
    };
    View.OnClickListener SexListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SexDialogUtils.setDialog(mContext, sex, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    sex = message;
                    if (!TextUtils.isEmpty(sex)) {
                        //性别
                        switch (sex) {
                            case "0":
                                mText_sex.setText("保密");
                                break;
                            case "1":
                                mText_sex.setText("男");
                                break;
                            case "2":
                                mText_sex.setText("女");
                                break;
                        }
                    }
                }
            });
        }
    };
    View.OnClickListener BirthdayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ChooseDateUtil dateUtil = new ChooseDateUtil();
            if (TextUtils.isEmpty(birthday)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                birthday = dateFormat.format(new Date());
            }
            String[] split = birthday.split("\\-");
            int[] oldDateArray = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
            dateUtil.createDialog(mContext, oldDateArray, new ChooseDateInterface() {
                @Override
                public void sure(int[] newDateArray) {
                    birthday = newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2];
                    mText_birthday.setText(birthday);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String surrentTime = dateFormat.format(new Date());
                    String[] split1 = surrentTime.split("\\-");
                    int ages = Integer.parseInt(split1[0]) - newDateArray[0];
                    if (Integer.parseInt(split1[1]) > newDateArray[1]) {
                        age = ages + "";
                    } else if (Integer.parseInt(split1[1]) == newDateArray[1]) {
                        if (Integer.parseInt(split1[2]) >= newDateArray[2]) {
                            age = ages + "";
                        } else {
                            age = (ages - 1) + "";
                        }
                    } else {
                        age = (ages - 1) + "";
                    }
                    mText_age.setText(age);
                }
            });
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            manifesto = mManifesto.getText().toString();
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            if (!TextUtils.isEmpty(userName)) params.putString("nikename", userName);
            if (!TextUtils.isEmpty(birthday)) params.putString("birthday", birthday);
            if (!TextUtils.isEmpty(sex)) params.putString("sex", sex);
            if (!TextUtils.isEmpty(height)) params.putString("height", height);
            if (!TextUtils.isEmpty(age)) params.putString("age", age);
            if (!TextUtils.isEmpty(weight)) params.putString("weight", weight);
            if (!TextUtils.isEmpty(loginLng)) params.putString("loginLng", loginLng);
            if (!TextUtils.isEmpty(loginLat)) params.putString("loginLat", loginLat);
            String loginIp = NetworkUtils.getIPAddress();
            if (!TextUtils.isEmpty(loginIp)) params.putString("loginIp", loginIp);
            if (!TextUtils.isEmpty(hobby)) params.putString("interest", hobby);
            if (!TextUtils.isEmpty(manifesto)) params.putString("signature", manifesto);
            mHttp.post(Url.USER_INFO_CHANG_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    Intent intent = new Intent();
                    setResult(320, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PhotoUtils.PERMISSIONS_REQUEST_PHOTO: {
                //调取相机的权限申请结果（调取相机和存储权限）
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (PhotoUtils.isTakePhoto) mPhotoUtils.takePhoto();
                    if (PhotoUtils.isGetPic) mPhotoUtils.getPictureFromLocal();
                }
            }
            case PhotoUtils.PERMISSIONS_REQUEST_FILE:
                //选择图片的时候申请的存储权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoUtils.dealZoomPhoto(mImage_header, 1, "");
                   /* if (PhotoUtils.isTakePhoto) mPhotoUtils.takePhoto();
                    if (PhotoUtils.isGetPic) mPhotoUtils.getPictureFromLocal();*/
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoUtils.REQUEST_CODE_TAKING_PHOTO) {
                mPhotoUtils.dealTakePhotoThenZoom();// 拍照的结果
            } else if (requestCode == PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL) {
                mPhotoUtils.dealChoosePhotoThenZoom(data);//选择图片的结果
            } else if (requestCode == PhotoUtils.REQUEST_CODE_CUT_PHOTO) {
                mPhotoUtils.dealZoomPhoto(mImage_header, 1, "");// 剪裁图片的结果
            }
        }
    }
}
