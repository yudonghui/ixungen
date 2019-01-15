package com.ruihuo.ixungen.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.home.clanskill.DealPhotoActivity;
import com.ruihuo.ixungen.common.ConstantNum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ruihuo.ixungen.utils.PhotoUtils.PERMISSIONS_REQUEST_FILE;

/**
 * @author yudonghui
 * @date 2017/6/6
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoUtilsX {
    private Context mContext;
    private View inflate;
    private TextView mCamera;
    private TextView mPhoto;
    private Button mCancel;
    private Dialog mDialog;
    private Window window;
    public String mImgPath;//拍照图片的路径

    public PhotoUtilsX(Context mContext) {
        this.mContext = mContext;
        inflate = View.inflate(mContext, R.layout.photo_popup, null);
        mCamera = (TextView) inflate.findViewById(R.id.buttonCamera);
        mPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        addListener();
    }

    //显示对话框，相册，相机 取消
    public void showDialog() {
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
    }

    private void addListener() {
        //拍照
        mCamera.setOnClickListener(CameraListener);
        //从相册中选取
        mPhoto.setOnClickListener(PhotoListener);
        //取消
        mCancel.setOnClickListener(CancelListener);
    }

    //拍照需要请求相机，和访问存储卡的权限
    private String[] VIDEO_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> noPermissionCamera = new ArrayList<String>();//没有给的权限。
    //拍照
    View.OnClickListener CameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            /**
             * 检测摄像头权限，具备相关权限才能继续
             */
            noPermissionCamera.clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < VIDEO_PERMISSION.length; i++) {
                    if (ActivityCompat.checkSelfPermission(mContext, VIDEO_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                        noPermissionCamera.add(VIDEO_PERMISSION[i]);
                    }
                }
                if (noPermissionCamera.size() == 0) {
                    //调取相机
                    takePhoto();
                } else {
                    ActivityCompat.requestPermissions((Activity) mContext,
                            noPermissionCamera.toArray(new String[noPermissionCamera.size()]), ConstantNum.PERMISSION_CAMERA);
                }
            } else {
                //调取相机
                takePhoto();
            }
        }
    };
    //从相册中选取(只需要存储权限)
    View.OnClickListener PhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
                } else {
                    getPictureFromLocal();
                }
            } else {
                getPictureFromLocal();
            }
        }
    };
    //取消
    View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
        }
    };

    /**
     * 打开相机
     */
    public void takePhoto() {
        String mUUID = UUID.randomUUID().toString();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mImgPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
        File file = new File(mImgPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
    }

    /**
     * 打开相册
     */
    public void getPictureFromLocal() {
        Intent innerIntent =
                new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        ((Activity) mContext).startActivityForResult(wrapperIntent, ConstantNum.REQUEST_PHOTO);
    }

    /**
     * 处理拍照并剪裁
     */
    public void dealTakePhotoThenZoom() {
        if (!TextUtils.isEmpty(mImgPath)) {
            // Bitmap bitmap = BitmapFactory.decodeFile(mImgPath);
            Intent intent = new Intent(mContext, DealPhotoActivity.class);
            intent.putExtra("imgPath", mImgPath);
            ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_DEAL_PHOTO);
        }
    }

    /**
     * 处理选择图片并剪裁
     */
    public void dealChoosePhotoThenZoom(Intent data) {
        if (data==null)return;
        try {
            ContentResolver resolver = mContext.getContentResolver();
            Uri originalUri = data.getData(); // 获得图片的uri

            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            String path = BitmapUtils.saveBitmap(bm);
            Intent intent = new Intent(mContext, DealPhotoActivity.class);
            intent.putExtra("imgPath", path);
            ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_DEAL_PHOTO);
        } catch (IOException e) {
            Log.e("TAG-->Error", e.toString());

        } finally {
            return;
        }
    }




   /* Uri uri = data.getData();
        String path = uri.getPath();
        if (!TextUtils.isEmpty(path)) {
            Intent intent = new Intent(mContext, DealPhotoActivity.class);
            intent.putExtra("imgPath", path);
            ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_DEAL_PHOTO);
        }
    }*/
}
