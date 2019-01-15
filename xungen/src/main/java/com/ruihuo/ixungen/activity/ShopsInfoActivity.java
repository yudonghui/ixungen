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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.login.SelectCityActivity;
import com.ruihuo.ixungen.activity.merchant.EnvironmentFormActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.utils.AddPhotoUtils;
import com.ruihuo.ixungen.utils.BitmapUtils;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.SelectorDialog;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopsInfoActivity extends BaseActivity {
    public static final String TAG = ShopsInfoActivity.class.getSimpleName();
    private TextView mShopsEnir;
    private EditText mShopsName;
    private TextView mShopsAddress;
    private EditText mShopsDetailAddress;
    private EditText mShopsTime;
    private TextView mShopsType;
    private ImageView mShopsDoorImg;
    private TextView mCommitShopsDoor;
    private ImageView mShopsInImg;
    private TextView mCommitShopsIn;
    private EditText mShopsIntroduce;
    private TextView mNext;
    private Context mContext;
    private String city;//选择的城市id。三级以,号分割
    private String envirName;//景区名字
    private String envirId;//景区id
    private String shopCardA;//店面照片
    private String shopCardB;//店铺内部照片
    private String cityName;
    private SelectorDialog selectorDialog;
    private String type;//门店分类 类型 1-餐饮，2-酒店，3-家谱；4-景点
    private List<String> data;
    private SharedPreferences sp;
    private PhotoUtils mPhotoUtils;
    private AddPhotoUtils mAddPhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_shops_info);
        sp = getSharedPreferences("merchantInfo", MODE_PRIVATE);
        mContext = this;
        initView();
        addListener();
    }


    private void initView() {
        mTitleBar.setTitle("店铺信息");
        mShopsEnir = (TextView) findViewById(R.id.shopsEnir);
        mShopsName = (EditText) findViewById(R.id.shopsName);
        mShopsAddress = (TextView) findViewById(R.id.shopsAddress);
        mShopsDetailAddress = (EditText) findViewById(R.id.shopsDetailAddress);
        mShopsTime = (EditText) findViewById(R.id.shopsTime);
        mShopsType = (TextView) findViewById(R.id.shopsType);
        mShopsDoorImg = (ImageView) findViewById(R.id.shopsDoorImg);
        mCommitShopsDoor = (TextView) findViewById(R.id.commitShopsDoor);
        mShopsInImg = (ImageView) findViewById(R.id.shopsInImg);
        mCommitShopsIn = (TextView) findViewById(R.id.commitShopsIn);
        mShopsIntroduce = (EditText) findViewById(R.id.shopsIntroduce);
        mNext = (TextView) findViewById(R.id.next);
        selectorDialog = new SelectorDialog(mContext);

        mPhotoUtils = new PhotoUtils(mContext);
        mAddPhotoUtils = new AddPhotoUtils(mContext);
        data = new ArrayList<>();
        data.add("餐饮");
        data.add("住宿");
        selectorDialog.initData(data);
        setView();
    }

    private void setView() {
        envirId = sp.getString("scenicAreaId", "");
        String name = sp.getString("name", "");
        mShopsEnir.setText(name);
        String shopName = sp.getString("shopName", "");
        mShopsName.setText(shopName);
        String address = sp.getString("address", "");
        mShopsDetailAddress.setText(address);
        String region = sp.getString("region", "");
        cityName = region;
        mShopsAddress.setText(region);
        String shopTime = sp.getString("shopTime", "");
        mShopsTime.setText(shopTime);
        type = sp.getString("type", "");
        mShopsType.setText(type);
        String text = sp.getString("text", "");
        mShopsIntroduce.setText(text);
        shopCardA = sp.getString("shopCardA", "");
        shopCardB = sp.getString("shopCardB", "");
        PicassoUtils.setImgView(shopCardA, R.mipmap.door_default, mContext, mShopsDoorImg);
        PicassoUtils.setImgView(shopCardB, R.mipmap.in_default, mContext, mShopsInImg);
        mCommitShopsDoor.setText("换一张");
        mCommitShopsIn.setText("换一张");
    }

    private void addListener() {
        mNext.setOnClickListener(NextListener);
        mShopsEnir.setOnClickListener(ShopsEnir);//景区选择
        mCommitShopsDoor.setOnClickListener(ShopsDoorListener);
        mShopsInImg.setOnClickListener(ShopsInListener);
        mShopsAddress.setOnClickListener(AddressListener);//店铺地址
        mShopsType.setOnClickListener(ShopsTypeListener);
    }

    View.OnClickListener NextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String shopName = mShopsName.getText().toString();//店铺名
            String region = mShopsAddress.getText().toString();//店铺地址（省市区）
            String address = mShopsDetailAddress.getText().toString();//店铺详细地址
            String shopTime = mShopsTime.getText().toString();//营业时间
            String shopsType = mShopsType.getText().toString();//店铺类型。请选择一个分类
            String shopIntroduce = mShopsIntroduce.getText().toString();//店铺介绍
            if (TextUtils.isEmpty(envirId)) {
                Toast.makeText(mContext, "请选择景区", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(shopName)) {
                Toast.makeText(mContext, "请输入店铺名称", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(cityName)) {
                Toast.makeText(mContext, "请选择店铺地址", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(address)) {
                Toast.makeText(mContext, "请输入店铺详细地址", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(type)) {
                Toast.makeText(mContext, "请选择一个门店分类", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(shopIntroduce)) {
                Toast.makeText(mContext, "请输入店铺简介", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(shopCardA)) {
                Toast.makeText(mContext, "请上传店铺的门面照片", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(shopCardB)) {
                Toast.makeText(mContext, "请上传店铺的室内照片", Toast.LENGTH_SHORT).show();
            } else {

                SharedPreferences.Editor edit = sp.edit();
                edit.putString("scenicAreaId", envirId);//景区编号
                edit.putString("shopName", shopName);//店铺名字
                edit.putString("region", cityName);//店铺地址
                edit.putString("address", address);//店铺详细地址
                edit.putString("shopTime", shopTime);//店铺营业时间
                edit.putString("type", type);//商业类型
                edit.putString("text", shopIntroduce);//店铺简介
                edit.putString("shopCardA", shopCardA);//店铺门前照
                edit.putString("shopCardB", shopCardB);//店铺室内照
                edit.commit();
                Intent intent = new Intent(mContext, ShopsCommitActivity.class);
                startActivityForResult(intent, 402);
            }
        }
    };
    View.OnClickListener ShopsTypeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(type)) {
                for (int i = 0; i < data.size(); i++) {
                    if (type.equals(data.get(i))) {
                        selectorDialog.setSelector(i);
                        break;
                    }
                }
            }
            selectorDialog.show(R.layout.activity_shops_info);
            selectorDialog.setListener(new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    type = message;
                    mShopsType.setText(TextUtils.isEmpty(message) ? "" : message);
                }
            });
        }
    };
    View.OnClickListener ShopsEnir = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, EnvironmentFormActivity.class);
            startActivityForResult(intent, 321);
        }
    };
    View.OnClickListener AddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 101);
        }
    };
    private String shopsDoorPath;//店铺门牌子的照片路径
    private String shopsInPath;//店铺内部的照片路径
    private int doorOrIn;//1,门牌照，2是内部照片
    View.OnClickListener ShopsDoorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doorOrIn = 1;
            mPhotoUtils.setMode(1);
            mPhotoUtils.showDialog();
           /* if (requestPermission()) {
                String mUUID = UUID.randomUUID().toString();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                shopsDoorPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
                File file = new File(shopsDoorPath);
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
                ((Activity) mContext).startActivityForResult(intent, 225);
            }*/
        }
    };
    View.OnClickListener ShopsInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doorOrIn = 2;
            mPhotoUtils.setMode(1);
            mPhotoUtils.showDialog();
            /*if (requestPermission()) {
                String mUUID = UUID.randomUUID().toString();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                shopsInPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
                File file = new File(shopsInPath);
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
                ((Activity) mContext).startActivityForResult(intent, 226);
            }*/
        }
    };
    private Bitmap bitmapCover;
    private LoadingDialogUtils loadingDialogUtils;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == 225 && resultCode == RESULT_OK) {//店铺门牌的照片
            mCommitShopsDoor.setText("换一张");
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(shopsDoorPath, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    mShopsDoorImg.setImageURI(Uri.fromFile(new File(shopsDoorPath)));
                    loadingDialogUtils.setDimiss();
                    shopCardA = message;
                }
            });
        } else if (requestCode == 226 && resultCode == RESULT_OK) {//店铺内部照片
            mCommitShopsIn.setText("换一张");
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(shopsInPath, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    mShopsInImg.setImageURI(Uri.fromFile(new File(shopsInPath)));
                    loadingDialogUtils.setDimiss();
                    shopCardB = message;
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
        } else if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            city = data.getStringExtra("address");
            String s = data.getStringExtra("cityName");
            cityName = s.replaceAll(" ", ",");
            mShopsAddress.setText(cityName);
        } else if (requestCode == 321 & resultCode == 322) {
            //选择景区的结果
            envirId = data.getStringExtra("envirId");
            envirName = data.getStringExtra("envirName");
            mShopsEnir.setText(TextUtils.isEmpty(envirName) ? "--" : envirName);
        } else if (requestCode == 402 && resultCode == 403) {//成功提交
            Intent intent = new Intent();
            setResult(403, intent);
            finish();
        }

    }

    AddPhotoUtils.CallBack CoverListener = new AddPhotoUtils.CallBack() {
        @Override
        public void onSuccess(String url) {
            if (doorOrIn == 1) {//门牌照
                mCommitShopsDoor.setText("换一张");
                mShopsDoorImg.setImageBitmap(bitmapCover);
                shopCardA = url;
            }else {//室内照片
                mCommitShopsIn.setText("换一张");
                mShopsInImg.setImageBitmap(bitmapCover);
                shopCardB = url;
            }
            loadingDialogUtils.setDimiss();
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
