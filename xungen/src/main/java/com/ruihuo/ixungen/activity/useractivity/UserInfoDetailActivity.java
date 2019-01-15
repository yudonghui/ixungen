package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.login.SelectCityActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.UserInfoBean;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
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

/**
 * @ author yudonghui
 * @ date 2017/3/31
 * @ describe May the Buddha bless bug-free！！
 */
public class UserInfoDetailActivity extends BaseActivity {
    public static final String TAG = UserInfoDetailActivity.class.getSimpleName();
    private LinearLayout mLl_header;
    private ImageView mImage_header;
    private LinearLayout mLl_username;
    private TextView mText_username;
    private LinearLayout mLl_rid;
    private TextView mText_rid;
    private LinearLayout mLl_surname;
    private TextView mText_surname;
    private LinearLayout mLl_realname;
    private TextView mText_realname;
    private LinearLayout mLl_sex;
    private TextView mText_sex;
    private LinearLayout mLl_birthday;
    private TextView mText_birthday;
    private LinearLayout mLl_phone;
    private TextView mText_phone;
    private LinearLayout mLl_isshow_phone;
    private ImageView mImage_isshow_phone;
    private LinearLayout mLl_city;
    private TextView mText_city;
    private LinearLayout mLl_region;
    private TextView mText_region;
    private LinearLayout mLlTwocode;
    private Context mContext;
    private PhotoUtils mPhotoUtils;
    private UserInfoBean userInfoBean;
    private String userName;
    private String surname;
    private String realName;
    private String realSurname;
    private String sex;
    private String regionCityName;
    private String cityName;
    private String birthday;
    private String phone;
    private String idcard;
    private String loginLat;
    private String loginLng;
    private String avatar;
    private static final int PERMISSION_CODE = 230;
    private static final int REQUEST_CODE = 231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_user_info_detail);
        String location = LocationUtils.getLocation(this);
        //第一个是jing度，第二个wei度
        String[] split = location.split(",");
        loginLng = split[0];
        loginLat = split[1];
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userInfoBean");
        mContext = this;
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mTitleBar.setTitle("个人信息");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitleBar.mTextRegister.setText("保存");
        mLl_header = (LinearLayout) findViewById(R.id.ll_header);
        mImage_header = (ImageView) findViewById(R.id.image_header);
        mLl_username = (LinearLayout) findViewById(R.id.ll_username);
        mText_username = (TextView) findViewById(R.id.text_username);
        mLl_rid = (LinearLayout) findViewById(R.id.ll_rid);
        mText_rid = (TextView) findViewById(R.id.text_rid);
        mLl_surname = (LinearLayout) findViewById(R.id.ll_surname);
        mText_surname = (TextView) findViewById(R.id.text_surname);
        mLl_realname = (LinearLayout) findViewById(R.id.ll_realname);
        mText_realname = (TextView) findViewById(R.id.text_realname);
        mLl_sex = (LinearLayout) findViewById(R.id.ll_sex);
        mText_sex = (TextView) findViewById(R.id.text_sex);
        mLl_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        mText_birthday = (TextView) findViewById(R.id.text_birthday);
        mLl_phone = (LinearLayout) findViewById(R.id.ll_phone);
        mText_phone = (TextView) findViewById(R.id.text_phone);
        mLl_isshow_phone = (LinearLayout) findViewById(R.id.ll_isshow_phone);
        mImage_isshow_phone = (ImageView) findViewById(R.id.image_isshow_phone);
        mLl_city = (LinearLayout) findViewById(R.id.ll_city);
        mText_city = (TextView) findViewById(R.id.text_city);
        mLl_region = (LinearLayout) findViewById(R.id.ll_region);
        mText_region = (TextView) findViewById(R.id.text_region);
        mLlTwocode = (LinearLayout) findViewById(R.id.ll_twocode);
        mPhotoUtils = new PhotoUtils(mContext);
        setView();
    }

    private void setView() {

        UserInfoBean.DataBean data = userInfoBean.getData();
        userName = data.getNikename();
        surname = data.getSurname();
        realName = data.getTruename();
        sex = data.getSex();
        birthday = data.getBirthday();
        phone = data.getPhone();
        cityName = data.getCity();
        regionCityName = data.getRegion();
        idcard = data.getIdcard();
        avatar = data.getAvatar();
        isShowPhone = data.getFlag();
        //用户名
        mText_username.setText(TextUtils.isEmpty(userName) ? "" : userName);
        //根号
        mText_rid.setText(XunGenApp.rid);
        //真实姓
        mText_surname.setText(TextUtils.isEmpty(surname) ? "" : surname);
        //真实名
        mText_realname.setText(TextUtils.isEmpty(realName) ? "" : realName);
        //现居地
        mText_city.setText(TextUtils.isEmpty(cityName) ? "" : cityName);
        //祖籍
        mText_region.setText(TextUtils.isEmpty(regionCityName) ? "" : regionCityName);
        //性别
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
        //出生日期
        mText_birthday.setText(TextUtils.isEmpty(birthday) ? "" : birthday);
        //手机号
        mText_phone.setText(TextUtils.isEmpty(phone) ? "" : phone);
        //是否显示手机号
        mImage_isshow_phone.setImageResource("0".equals(isShowPhone) ? R.drawable.btn_on : R.drawable.btn_off);
        //头像
        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(mContext)
                    .load(avatar)
                    .placeholder(R.mipmap.default_header)
                    .error(R.mipmap.default_header)
                    .into(mImage_header);
        }
    }

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(EditorListener);
        mLlTwocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TwoCodeActivity.class);
                intent.putExtra("city", mText_city.getText().toString());
                intent.putExtra("userName", userName);
                intent.putExtra("avatar", avatar);
                intent.putExtra("type", ConstantNum.FRIEND_TYPE);
                intent.putExtra("code", XunGenApp.rid);
                startActivity(intent);
            }
        });
        //头像
        mLl_header.setOnClickListener(HeaderListener);
        //用户名
        mLl_username.setOnClickListener(UsernameListener);
        //真实姓
        mLl_surname.setOnClickListener(SurnameListener);
        //真实名
        mLl_realname.setOnClickListener(RealnameListener);
        //性别
        mLl_sex.setOnClickListener(SexListener);
        //出生日期
        mLl_birthday.setOnClickListener(BirthdayListener);
        //手机号
        mLl_phone.setOnClickListener(PhoneListener);
        //现居住地
        mLl_city.setOnClickListener(CityListener);
        //祖籍
        mLl_region.setOnClickListener(RegionListener);
        //是否隐藏手机号
        mImage_isshow_phone.setOnClickListener(IsshowPhoneListener);
    }

    // private boolean isEditor = true;
    View.OnClickListener EditorListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    if (!TextUtils.isEmpty(surname)) params.putString("surname", surname);
                    if (!TextUtils.isEmpty(regionCityName))
                        params.putString("region", regionCityName);
                    if (!TextUtils.isEmpty(cityName)) params.putString("city", cityName);
                    if (!TextUtils.isEmpty(userName)) params.putString("nikename", userName);
                    if (!TextUtils.isEmpty(loginLng)) params.putString("loginLng", loginLng);
                    if (!TextUtils.isEmpty(loginLat)) params.putString("loginLat", loginLat);
                    if (!TextUtils.isEmpty(isShowPhone)) params.putString("flag", isShowPhone);
                    String loginIp = NetworkUtils.getIPAddress();
                    if (!TextUtils.isEmpty(loginIp)) params.putString("loginIp", loginIp);
                    if (!TextUtils.isEmpty(birthday)) params.putString("birthday", birthday);
                    if (!TextUtils.isEmpty(sex)) params.putString("sex", sex);
                    mHttp.post(Url.USER_INFO_CHANG_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                }
            };
    View.OnClickListener HeaderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoUtils.showDialog();
        }
    };
    View.OnClickListener UsernameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
            editDialogUtils.setTitle("请输入用户名");
            editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        mText_username.setText(message);
                        userName = message;
                    }

                }
            });
        }
    };
    View.OnClickListener SurnameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
            editDialogUtils.setTitle("请输入用户姓氏");
            editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        mText_surname.setText(message);
                        surname = message;
                    }

                }
            });*/
        }
    };
    View.OnClickListener RealnameListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(phone)) {
                Intent intent = new Intent(mContext, RealNameActivity.class);
                intent.putExtra("truename", realName);
                intent.putExtra("phone", phone);
                intent.putExtra("idCard", idcard);
                startActivityForResult(intent, 102);
            }
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
            chooseDateDialog();
        }
    };
    View.OnClickListener PhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener CityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 101);
        }
    };
    View.OnClickListener RegionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 100);
        }
    };

    public void chooseDateDialog() {
        final ChooseDateUtil dateUtil = new ChooseDateUtil();
        if (TextUtils.isEmpty(birthday)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birthday = dateFormat.format(new Date());
        }
        String[] split = birthday.split("\\-");
        int[] oldDateArray = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        dateUtil.createDialog(this, oldDateArray, new ChooseDateInterface() {
            @Override
            public void sure(int[] newDateArray) {
                long birthdayTime = DateFormatUtils.StringToLong(newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2]) / 1000;
                long currentTime = System.currentTimeMillis() / 1000;
                if (birthdayTime > currentTime) {
                    Toast.makeText(mContext, "出生日期选择有误！", Toast.LENGTH_SHORT).show();
                } else {
                    birthday = newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2];
                    mText_birthday.setText(birthday);
                }
            }
        });
    }

    private void addData() {

    }

    private String isShowPhone = "0";
    View.OnClickListener IsshowPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("0".equals(isShowPhone)) {
                isShowPhone = "1";
                mImage_isshow_phone.setImageResource(R.drawable.btn_off);
            } else {
                isShowPhone = "0";
                mImage_isshow_phone.setImageResource(R.drawable.btn_on);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.REQUEST_CODE_TAKING_PHOTO && resultCode == RESULT_OK) {
            mPhotoUtils.dealTakePhotoThenZoom();// 拍照的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL && resultCode == RESULT_OK) {
            mPhotoUtils.dealChoosePhotoThenZoom(data);//选择图片的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_CUT_PHOTO && resultCode == RESULT_OK) {
            mPhotoUtils.dealZoomPhoto(mImage_header, 1, "");// 剪裁图片的结果
        } else if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            cityName = data.getStringExtra("cityName");
            mText_city.setText(cityName);
        } else if (requestCode == 100 && resultCode == 323) {
            //点击籍贯所在地，返回的值
            regionCityName = data.getStringExtra("cityName");
            mText_region.setText(regionCityName);
        } else if (requestCode == 102 && resultCode == 306) {
            //真实姓名修改完成
            idcard = data.getStringExtra("idcard");
            realName = data.getStringExtra("truename");
            realSurname = data.getStringExtra("trueSurname");
            mText_realname.setText(TextUtils.isEmpty(realName) ? "" : realName);
            mText_surname.setText(TextUtils.isEmpty(realSurname) ? "" : realSurname);
        }
    }

}
