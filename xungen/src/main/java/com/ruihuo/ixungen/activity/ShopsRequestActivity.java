package com.ruihuo.ixungen.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oliveapp.face.idcardcaptorsdk.captor.IDCardCaptor;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.merchant.SampleIdcardCaptorActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.CodeDialogUtils;
import com.ruihuo.ixungen.utils.GetPhotoUrl;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.IDCardValidate;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.SDCardUtils;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ShopsRequestActivity extends BaseActivity {
    public static final String TAG = ShopsRequestActivity.class.getSimpleName();
    private EditText mRealName;
    private EditText mCardNum;
    private TextView mCardEndTime;
    private ImageView mCardZhengImg;
    private TextView mCardZheng;
    private ImageView mCardFanImg;
    private TextView mCommitCardFan;
    private ImageView mCardHalfImg;
    private TextView mCommitCardHalf;
    private ImageView mHalfImg;
    private TextView mCommitHalf;
    private ImageView mForever;//永久
    private EditText mMobile;
    private TextView mNext;
    private Context mContext;
    private String juridicalIdcardA;//身份证正面。
    private String juridicalIdcardB;//身份证反面
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_shops_request);
        sp = getSharedPreferences("merchantInfo", MODE_PRIVATE);
        mContext = this;
        initView();
        addListener();
        requestPermission();
    }

    private void initView() {
        mTitleBar.setTitle("申请入驻");
        mRealName = (EditText) findViewById(R.id.realName);
        mCardNum = (EditText) findViewById(R.id.cardNum);
        mCardEndTime = (TextView) findViewById(R.id.cardEndTime);
        mCardZhengImg = (ImageView) findViewById(R.id.cardZhengImg);
        mCardZheng = (TextView) findViewById(R.id.cardZheng);
        mCardFanImg = (ImageView) findViewById(R.id.cardFanImg);
        mCommitCardFan = (TextView) findViewById(R.id.commitCardFan);
        mCardHalfImg = (ImageView) findViewById(R.id.cardHalfImg);
        mCommitCardHalf = (TextView) findViewById(R.id.commitCardHalf);
        mHalfImg = (ImageView) findViewById(R.id.halfImg);
        mForever = (ImageView) findViewById(R.id.forever);
        mCommitHalf = (TextView) findViewById(R.id.commitHalf);
        mMobile = (EditText) findViewById(R.id.mobile);
        mNext = (TextView) findViewById(R.id.next);
        setView();
    }

    private void setView() {
        String mobile = sp.getString("mobile", "");
        mMobile.setText(mobile);
        String juridicalName = sp.getString("juridicalName", "");
        mRealName.setText(juridicalName);
        String juridicalIdNo = sp.getString("juridicalIdNo", "");
        mCardNum.setText(juridicalIdNo);
        juridicalIdcardA = sp.getString("juridicalIdcardA", "");
        juridicalIdcardB = sp.getString("juridicalIdcardB", "");
        PicassoUtils.setImgView(juridicalIdcardA, R.mipmap.card_zheng, mContext, mCardZhengImg);
        PicassoUtils.setImgView(juridicalIdcardB, R.mipmap.card_fan, mContext, mCardFanImg);
        mCardZheng.setText("换一张");
        mCommitCardFan.setText("换一张");
    }

    private void addListener() {
        mNext.setOnClickListener(NextListener);
        mCardZheng.setOnClickListener(CardZhengListener);//身份证正面
        mCommitCardFan.setOnClickListener(CardFanListener);//身份证反面
        mCommitCardHalf.setOnClickListener(CardHalfListener);//手持身份证的半身照
        mCommitHalf.setOnClickListener(HalfListener);//半身照
        mCardEndTime.setOnClickListener(CardEndTimeListener);//身份证到期时间
        mForever.setOnClickListener(ForeverListener);//身份证永久有效
    }

    View.OnClickListener ForeverListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener CardEndTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final ChooseDateUtil dateUtil = new ChooseDateUtil();
            String birthday = mCardEndTime.getText().toString();
            if (TextUtils.isEmpty(birthday)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                birthday = dateFormat.format(new Date());
            }
            String[] split = birthday.split("\\-");
            int[] oldDateArray = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
            dateUtil.createDialog(mContext, oldDateArray, new ChooseDateInterface() {
                @Override
                public void sure(int[] newDateArray) {
                    String birthday = newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2];
                    mCardEndTime.setText(birthday);
                }
            });

        }
    };
    View.OnClickListener CardZhengListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /**
             * 设置捕获模式，共有三种模式模式：
             * 1.SampleIdcardCaptorActivity.CAPTURE_MODE_AUTO 自动捕获模式
             * 2.SampleIdcardCaptorActivity.CAPTURE_MODE_MANUAL 手动拍摄模式
             * 3.SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED 混合模式，首先尝试自动捕获，指定时间后，采取手动拍摄
             * 在本例中，使用混合捕获模式，需要设置自动捕获持续时间，单位为秒，默认10秒
             */
            Intent intent = new Intent(mContext, SampleIdcardCaptorActivity.class);
            intent.putExtra(SampleIdcardCaptorMainActivity.EXTRA_CARD_TYPE, IDCardCaptor.CARD_TYPE_FRONT);
            intent.putExtra(SampleIdcardCaptorActivity.EXTRA_CAPTURE_MODE, SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED);
            intent.putExtra(SampleIdcardCaptorActivity.EXTRA_DURATION_TIME, 10);
            if (requestPermission())
                startActivityForResult(intent, 221);
        }
    };
    View.OnClickListener CardFanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SampleIdcardCaptorActivity.class);
            intent.putExtra(SampleIdcardCaptorMainActivity.EXTRA_CARD_TYPE, IDCardCaptor.CARD_TYPE_BACK);
            intent.putExtra(SampleIdcardCaptorActivity.EXTRA_CAPTURE_MODE, SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED);
            intent.putExtra(SampleIdcardCaptorActivity.EXTRA_DURATION_TIME, 10);
            if (requestPermission())
                startActivityForResult(intent, 222);
        }
    };
    private String cardHalfPath;
    private String halfPath;
    View.OnClickListener CardHalfListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (requestPermission()) {
                String mUUID = UUID.randomUUID().toString();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                cardHalfPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
                File file = new File(cardHalfPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                ((Activity) mContext).startActivityForResult(intent, 223);
            }
        }
    };
    View.OnClickListener HalfListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (requestPermission()) {
                String mUUID = UUID.randomUUID().toString();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                halfPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
                File file = new File(halfPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                ((Activity) mContext).startActivityForResult(intent, 224);
            }
        }
    };

    View.OnClickListener NextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String realName = mRealName.getText().toString().trim();
            final String cardNum = mCardNum.getText().toString().trim();
            final String mobile = mMobile.getText().toString().trim();
            String zheng = mCardZheng.getText().toString();
            String fan = mCommitCardFan.getText().toString();
            if (TextUtils.isEmpty(realName)) {
                Toast.makeText(mContext, "请输入真实姓名", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(cardNum)) {
                Toast.makeText(mContext, "请输入身份证号", Toast.LENGTH_SHORT).show();
            } else if (!cardNum.equals(IDCardValidate.validate_effective(cardNum))) {
                Toast.makeText(mContext, IDCardValidate.validate_effective(cardNum), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mobile)) {
                Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
            } else if (!StringUtil.isLegalPhone(mobile)) {
                Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(juridicalIdcardA)) {
                Toast.makeText(mContext, "请拍张身份证正面照片", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(juridicalIdcardB)) {
                Toast.makeText(mContext, "请拍张身份证反面照片", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("mobile", mobile);//法人手机号
                edit.putString("juridicalName", realName);//法人姓名
                edit.putString("juridicalIdNo", cardNum);//法人身份证号码
                edit.putString("juridicalIdcardA", juridicalIdcardA);
                edit.putString("juridicalIdcardB", juridicalIdcardB);
                edit.commit();
                CodeDialogUtils codeDialogUtils = new CodeDialogUtils(mContext, mobile);
                codeDialogUtils.setMessage("申请入驻需要短信认证，校验码已经发送至您的手机号" + mobile);
                codeDialogUtils.setConfirm("确认入驻", new CodeDialogUtils.CodeDialogInterface() {
                    @Override
                    public void callBack(String code) {
                        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                        Bundle params = new Bundle();
                        params.putString("phone", mobile);
                        params.putString("code", code);
                        mHttp.post(Url.CHECK_SMS_CODE_URL, params, new JsonInterface() {
                            @Override
                            public void onSuccess(String result) {
                                loadingDialogUtils.setDimiss();
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("mobile", mobile);//法人手机号
                                edit.putString("juridicalName", realName);//法人姓名
                                edit.putString("juridicalIdNo", cardNum);//法人身份证号码
                                edit.putString("juridicalIdcardA", juridicalIdcardA);
                                edit.putString("juridicalIdcardB", juridicalIdcardB);
                                edit.commit();
                                Intent intent = new Intent(mContext, ShopsBankActivity.class);
                                startActivityForResult(intent, 400);
                            }

                            @Override
                            public void onError(String message) {
                                loadingDialogUtils.setDimiss();
                            }
                        });
                    }
                });
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 221 && resultCode == 321) {//身份证正面
            byte[] imageContent = data.getByteArrayExtra("image");
            Bitmap image = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
            mCardZhengImg.setImageBitmap(image);
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(image, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    loadingDialogUtils.setDimiss();
                    juridicalIdcardA = message;
                }
            });
            mCardZheng.setText("换一张");
        } else if (requestCode == 222 && resultCode == 321) {//身份证反面
            byte[] imageContent = data.getByteArrayExtra("image");
            Bitmap image = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
            mCardFanImg.setImageBitmap(image);
            mCommitCardFan.setText("换一张");
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(image, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    loadingDialogUtils.setDimiss();
                    juridicalIdcardB = message;
                }
            });
        } else if (requestCode == 223 && resultCode == RESULT_OK) {//手持身份证半身照
            mCardHalfImg.setImageURI(Uri.fromFile(new File(cardHalfPath)));
            mCommitCardHalf.setText("换一张");
        } else if (requestCode == 224 && resultCode == RESULT_OK) {//半身照
            mHalfImg.setImageURI(Uri.fromFile(new File(halfPath)));
            mCommitHalf.setText("换一张");
        } else if (requestCode == 400 && resultCode == 403) {//成功提交
            finish();
        }

    }


    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 101;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 102;
    private static final int PERMISSION_CAMERA = 103;

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            switch (requestCode) {
                case PERMISSION_CAMERA: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(this, "没有摄像头权限我什么都做不了哦!", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PERMISSION_READ_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(this, "请打开存储读写权限，确保APP正常运行", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(this, "请打开存储读写权限，确保APP正常运行", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to request Permission" + e.getMessage());
        }
    }
}
