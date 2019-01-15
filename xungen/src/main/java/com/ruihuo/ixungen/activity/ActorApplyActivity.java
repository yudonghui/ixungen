package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GetPhotoUrl;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.IDCardValidate;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.dialog.SexDialogUtils;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ruihuo.ixungen.XunGenApp.surname;

public class ActorApplyActivity extends AppCompatActivity {
    public static final String TAG = ActorApplyActivity.class.getSimpleName();
    private Context mContext;
    private ImageView mTitleBack;
    private LinearLayout mLl_avator;
    private ImageView mAvator;
    private EditText mSurname;
    private LinearLayout mLl_name;
    private EditText mEdit_name;
    private LinearLayout mLl_phone;
    private EditText mEdit_phone;
    private LinearLayout mLl_sex;
    private TextView mText_sex;
    private LinearLayout mLl_birthday;
    private TextView mText_birthday;
    private LinearLayout mLl_cardnum;
    private LinearLayout mLl_check;
    private ImageView mImg_check;
    private TextView mText_check;
    private EditText mEdit_cardnum;
    private TextView mRule;
    private TextView mConfirm;
    private String sex = "1";
    private String birthday;
    private int state;//1.没有报名，2，审核中，3通过审核，4审核不通过
    private PhotoUtils mPhotoUtils;
    private String img;//头像链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_apply);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mContext = this;
        state = getIntent().getIntExtra("state", 0);
        mPhotoUtils = new PhotoUtils(mContext);
        addData();
        initView();
        addListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initView() {
        mTitleBack = (ImageView) findViewById(R.id.titleBack);
        mLl_avator = (LinearLayout) findViewById(R.id.ll_avator);
        mAvator = (ImageView) findViewById(R.id.avatar);
        mSurname = (EditText) findViewById(R.id.edit_surname);
        mLl_name = (LinearLayout) findViewById(R.id.ll_name);
        mEdit_name = (EditText) findViewById(R.id.edit_name);
        mLl_phone = (LinearLayout) findViewById(R.id.ll_phone);
        mEdit_phone = (EditText) findViewById(R.id.edit_phone);
        mLl_sex = (LinearLayout) findViewById(R.id.ll_sex);
        mText_sex = (TextView) findViewById(R.id.text_sex);
        mLl_check = (LinearLayout) findViewById(R.id.ll_check);
        mImg_check = (ImageView) findViewById(R.id.img_check);
        mText_check = (TextView) findViewById(R.id.text_check);
        mLl_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        mText_birthday = (TextView) findViewById(R.id.text_birthday);
        mLl_cardnum = (LinearLayout) findViewById(R.id.ll_cardnum);
        mEdit_cardnum = (EditText) findViewById(R.id.edit_cardnum);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mRule = (TextView) findViewById(R.id.rule);
        mEdit_phone.setText(XunGenApp.phone);
        mText_birthday.setText(XunGenApp.birthday);
        birthday = XunGenApp.birthday;
        if (!TextUtils.isEmpty(XunGenApp.sex)) {
            switch (XunGenApp.sex) {
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
        if (!TextUtils.isEmpty(XunGenApp.trueName) && !TextUtils.isEmpty(XunGenApp.idCard)) {
            mEdit_name.setText(XunGenApp.trueName);
            mSurname.setText(surname);
            mEdit_name.setFocusable(false);
            mSurname.setFocusable(false);
        }
        if (!TextUtils.isEmpty(XunGenApp.idCard) &&
                XunGenApp.idCard.equals(IDCardValidate.validate_effective(XunGenApp.idCard))) {//如果实名认证，并且身份号是对的。
            String start = XunGenApp.idCard.substring(0, 3);
            String end = XunGenApp.idCard.substring(XunGenApp.idCard.length() - 3, XunGenApp.idCard.length());
            mEdit_cardnum.setText(start + "***" + end);
            mEdit_cardnum.setFocusable(false);
        }
        if (state == 1) {
            mLl_check.setVisibility(View.GONE);
        } else {
            mLl_check.setVisibility(View.VISIBLE);//2，审核中，3通过审核，4审核不通过
            switch (state) {
                case 2:
                    mText_check.setText("审核中");
                    break;
                case 3:
                    mText_check.setText("通过审核");
                    break;
                case 4:
                    mText_check.setText("审核不通过");
                    break;
            }
        }
    }

    private void addListener() {
        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_sex.setOnClickListener(SexListener);
        mLl_birthday.setOnClickListener(BirthdayListener);
        mConfirm.setOnClickListener(ConfirmListener);
        mLl_avator.setOnClickListener(AvatorListener);
    }

    View.OnClickListener AvatorListener = new View.OnClickListener() {
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
            birthday = mText_birthday.getText().toString();
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
                }
            });
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String truename = mEdit_name.getText().toString();
            final String phone = mEdit_phone.getText().toString();
            String idcard = mEdit_cardnum.getText().toString();
            if (idcard.contains("*")) {
                idcard = XunGenApp.idCard;
            }
            final String surname = mSurname.getText().toString();
            final String birthday = mText_birthday.getText().toString();
            if (TextUtils.isEmpty(surname)) {
                Toast.makeText(mContext, "请输入真实姓", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(truename)) {
                Toast.makeText(mContext, "请输入真实名", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(img)) {
                Toast.makeText(mContext, "请选择一个头像", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
            } else if (!StringUtil.isLegalPhone(phone)) {
                Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(idcard)) {
                Toast.makeText(mContext, "请输入身份证号码", Toast.LENGTH_SHORT).show();
            } else if (!idcard.equals(IDCardValidate.validate_effective(idcard))) {
                Toast.makeText(mContext, IDCardValidate.validate_effective(idcard), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(birthday)) {
                Toast.makeText(mContext, "请选择出生日期", Toast.LENGTH_SHORT).show();
            } else {
                if (truename.startsWith(surname)) {
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    hintDialogUtils.setMessage("您确定您的姓名是" + surname + truename + "?");
                    final String finalIdcard = idcard;
                    hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                        @Override
                        public void callBack(View view) {
                            confirm(truename, surname, phone, finalIdcard);
                        }
                    });
                } else {
                    confirm(truename, surname, phone, idcard);
                }
            }
        }
    };

    private void confirm(String truename, String surname, String phone, String finalIdcard) {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("truename", truename);
        params.putString("surname", surname);
        params.putString("phone", phone);
        params.putString("idcard", finalIdcard);
        params.putString("sex", sex);
        params.putString("birthday", birthday);
        params.putString("age", StringUtil.getAgeByBirthday(birthday) + "");
        params.putString("img", img);
        mHttp.post(Url.ACTOR_APPLY, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Intent intent = new Intent();
                setResult(323, intent);
                finish();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void addData() {
        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        instance.get(Url.ACTOR_APPLY_RULE, new Bundle(), new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String info = data.getString("info");
                    mRule.setText(TextUtils.isEmpty(info) ? "" : info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        try {
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
                        mPhotoUtils.dealZoomPhoto(mAvator, 1, "");
                   /* if (PhotoUtils.isTakePhoto) mPhotoUtils.takePhoto();
                    if (PhotoUtils.isGetPic) mPhotoUtils.getPictureFromLocal();*/
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to request Permission" + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.REQUEST_CODE_TAKING_PHOTO && resultCode == RESULT_OK) {
            mPhotoUtils.dealTakePhotoThenZoom();// 拍照的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL && resultCode == RESULT_OK) {
            mPhotoUtils.dealChoosePhotoThenZoom(data);//选择图片的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_CUT_PHOTO && resultCode == RESULT_OK) {
            // mPhotoUtils.dealZoomPhoto(mAvator, 1, "");// 剪裁图片的结果
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtils.IMAGE_DETAIL_PATH);
            mAvator.setImageBitmap(bitmap);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(PhotoUtils.IMAGE_DETAIL_PATH, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    loadingDialogUtils.setDimiss();
                    img = message;
                }
            });
        }
    }
}
