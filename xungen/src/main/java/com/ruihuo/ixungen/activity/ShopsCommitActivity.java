package com.ruihuo.ixungen.activity;

import android.Manifest;
import android.content.ContentResolver;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.AddPhotoUtils;
import com.ruihuo.ixungen.utils.BitmapUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.io.IOException;

public class ShopsCommitActivity extends BaseActivity {
    public static final String TAG = ShopsCommitActivity.class.getSimpleName();
    private ImageView mBuyLicenseImg;
    private TextView mBuyLicense;
    private TextView mCommit;
    private Context mContext;
    private String buyLicensePath;
    private SharedPreferences sp;
    private PhotoUtils mPhotoUtils;
    private AddPhotoUtils mAddPhotoUtils;
    private Bitmap bitmapCover;
    private LoadingDialogUtils loadingDialogUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_shops_commit);
        sp = getSharedPreferences("merchantInfo", MODE_PRIVATE);
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("合作申请");
        mBuyLicenseImg = (ImageView) findViewById(R.id.buyLicenseImg);
        mBuyLicense = (TextView) findViewById(R.id.buyLicense);
        mCommit = (TextView) findViewById(R.id.commit);
        mPhotoUtils = new PhotoUtils(mContext);
        mAddPhotoUtils = new AddPhotoUtils(mContext);
        businessLicense = sp.getString("businessLicense", "");
        PicassoUtils.setImgView(businessLicense, R.mipmap.request_material, mContext, mBuyLicenseImg);
        mBuyLicense.setText("换一张");

    }

    private void addListener() {
        mBuyLicense.setOnClickListener(BuyLicenseListener);
        mCommit.setOnClickListener(CommitListener);
    }

    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(businessLicense)) {
                Toast.makeText(mContext, "请上传营业执照照片", Toast.LENGTH_SHORT).show();
                return;
            }

            String mobile = sp.getString("mobile", "");
            String shopName = sp.getString("shopName", "");
            String juridicalName = sp.getString("juridicalName", "");
            String juridicalIdNo = sp.getString("juridicalIdNo", "");
            String juridicalIdcardA = sp.getString("juridicalIdcardA", "");
            String juridicalIdcardB = sp.getString("juridicalIdcardB", "");
            String openBank = sp.getString("openBank", "");
            String scenicAreaId = sp.getString("scenicAreaId", "");
            String address = sp.getString("address", "");
            String region = sp.getString("region", "");
            String shopTime = sp.getString("shopTime", "");
            String type = sp.getString("type", "");
            String text = sp.getString("text", "");
            String branch = sp.getString("branch", "");
            String cardType = sp.getString("cardType", "");
            String bankCard = sp.getString("bankCard", "");
            String shopCardA = sp.getString("shopCardA", "");
            String shopCardB = sp.getString("shopCardB", "");
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("mobile", mobile);
            params.putString("shopName", shopName);
            params.putString("juridicalName", juridicalName);
            params.putString("juridicalIdNo", juridicalIdNo);
            params.putString("juridicalIdcardA", juridicalIdcardA);
            params.putString("juridicalIdcardB", juridicalIdcardB);
            params.putString("openBank", openBank);
            params.putString("scenicAreaId", scenicAreaId);
            params.putString("address", address);
            params.putString("region", region);
            params.putString("shopTime", shopTime);
            params.putString("type", type.equals("餐饮") ? "1" : "2");
            params.putString("text", text);
            params.putString("businessLicense", businessLicense);//营业执照/租赁合同
            params.putString("branch", branch);
            params.putString("cardType", cardType);
            params.putString("bankCard", bankCard);
            params.putString("shopCardA", shopCardA);
            params.putString("shopCardB", shopCardB);
            mHttp.post(Url.MERCHANT_ENTER, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    SharedPreferences.Editor edit = sp.edit();
                    edit.clear();
                    edit.commit();
                    Intent intent = new Intent();
                    setResult(403, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };

    View.OnClickListener BuyLicenseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mPhotoUtils.setMode(1);
            mPhotoUtils.showDialog();


            /*if (requestPermission()) {
                String mUUID = UUID.randomUUID().toString();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                buyLicensePath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
                File file = new File(buyLicensePath);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //通过FileProvider创建一个content类型的Uri
                    Uri mUri = FileProvider.getUriForFile(mContext, "com.ruihuo.ixungen.FileProvider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                } else {
                    Uri mUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                }
                ((Activity) mContext).startActivityForResult(intent, 229);
            }*/
        }
    };

    private String businessLicense;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == 229 && resultCode == RESULT_OK) {//手持身份证半身照
            mBuyLicense.setText("换一张");
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(buyLicensePath, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    mBuyLicenseImg.setImageURI(Uri.fromFile(new File(buyLicensePath)));
                    loadingDialogUtils.setDimiss();
                    businessLicense = message;
                }
            });
        }*/
        if (resultCode == RESULT_OK && requestCode == PhotoUtils.REQUEST_CODE_TAKING_PHOTO) {//拍照结果

            String path = PhotoUtils.mPath;
            bitmapCover = BitmapFactory.decodeFile(path);
            loadingDialogUtils = new LoadingDialogUtils(mContext);
            mAddPhotoUtils.getImgUrl(path, CoverListener);
        } else if (resultCode == RESULT_OK && requestCode == PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL) {//相册选取结果
            try {
                ContentResolver resolver = mContext.getContentResolver();
                Uri originalUri = data.getData(); // 获得图片的uri
                bitmapCover = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                String path = BitmapUtils.saveBitmap(bitmapCover);
                loadingDialogUtils = new LoadingDialogUtils(mContext);
                mAddPhotoUtils.getImgUrl(path, CoverListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    AddPhotoUtils.CallBack CoverListener = new AddPhotoUtils.CallBack() {
        @Override
        public void onSuccess(String url) {
            loadingDialogUtils.setDimiss();
            mBuyLicense.setText("换一张");
            businessLicense=url;
            mBuyLicenseImg.setImageBitmap(bitmapCover);
        }

        @Override
        public void onError() {
            loadingDialogUtils.setDimiss();
        }
    };
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
