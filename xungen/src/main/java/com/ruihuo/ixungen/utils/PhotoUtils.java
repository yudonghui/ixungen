package com.ruihuo.ixungen.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.album.PickOrTakeImageActivity;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author yudonghui
 * @date 2017/4/5
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoUtils {
    private Context mContext;
    private TextView mCamera;
    private TextView mPhoto;
    private Button mCancel;
    // private PopupWindow popupWindow;
    private View inflate;
    //private String headerUrl;
    private Dialog mDialog;
    private Window window;
    public static boolean isTakePhoto = false;
    public static boolean isGetPic = false;
    public static String mPath;
    public static String photoPath;
    //调用相机和相册
    public static final String IMAGE_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/xungen/photo";
    public static final String IMAGE_SAVE_PATH = IMAGE_SAVE_DIR + "/demo.jpg";
    public static final String IMAGE_DETAIL_PATH = IMAGE_SAVE_DIR + "/deal.jpg";
    public static final int PERMISSIONS_REQUEST_PHOTO = 0x01;
    public static final int PERMISSIONS_REQUEST_FILE = 0x02;
    public static final int REQUEST_CODE_TAKING_PHOTO = 0x03;
    public static final int REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL = 0x04;
    public static final int REQUEST_CODE_CUT_PHOTO = 0x05;
    public static final int TARGET_HEAD_SIZE = 480;
    //多张图片请求的编号
    public static final int ACTIVITY_REQUEST_SELECT_PHOTO = 0x04;
    private Uri mUri;

    public PhotoUtils(Context mContext) {
        this.mContext = mContext;
        //有关相机调用
        inflate = View.inflate(mContext, R.layout.photo_popup, null);
        mCamera = (TextView) inflate.findViewById(R.id.buttonCamera);
        mPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        addListener();
        File file = new File(IMAGE_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public void showDialog() {
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
    }

    public void addListener() {
        //拍照
        mCamera.setOnClickListener(CameraListener);
        //从相册中选取
        mPhoto.setOnClickListener(PhotoListener);
        //取消
        mCancel.setOnClickListener(CancelListener);
    }

    private CallBackInterface mCallBack;

    public void setCallBack(CallBackInterface mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * mode
     * 1 裁剪的头像
     * 2. 宗亲相册上传图片
     */
    private int mode = 1;

    public void setMode(int mode) {
        this.mode = mode;

    }

    //拍照
    View.OnClickListener CameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();

            operTakePhoto();
        }
    };
    //从相册中选取
    View.OnClickListener PhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            if (mode == 1) {
                operChoosePic();
            } else {
                operChoosPicMore();
            }
        }
    };

    public void operChoosPicMore() {
/*         *//*   1. 使用默认风格，并指定选择数量：
               第一个参数Activity/Fragment； 第二个request_code； 第三个允许选择照片的数量，不填可以无限选择。
                Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 9);

              2. 使用默认风格，不指定选择数量：
                 Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO); // 第三个参数不填的话，可以选择无数个。

              3. 指定风格，并指定选择数量，如果不想限制数量传入Integer.MAX_VALUE;*//*
        Album.startAlbum((Activity) mContext, ACTIVITY_REQUEST_SELECT_PHOTO);
       *//* Album.startAlbum(mContext,
                ACTIVITY_REQUEST_SELECT_PHOTO
                , 9                                                         // 指定选择数量。
                , ContextCompat.getColor(mContext, R.color.blue)        // 指定Toolbar的颜色。
                , ContextCompat.getColor(mContext, R.color.blue));  // 指定状态栏的颜色。*/
        Intent intent = new Intent(mContext, PickOrTakeImageActivity.class);
        ((Activity) mContext).startActivityForResult(intent, ACTIVITY_REQUEST_SELECT_PHOTO);

    }


    //取消
    View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
        }
    };

    /**
     * 拍照操作
     */
    public void operTakePhoto() {
        isTakePhoto = true;
        isGetPic = false;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //如果sdk大于等于23那么提示是否获取调取相机的授权。否则直接请求授权
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA))
                showPhotoPerDialog();
            else
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO);
        } else {
            if (mode == 1) {
                takePhoto();
            } else {
                operTakePhotoMore();
            }
        }
    }

    /**
     * 选择图片操作
     */
    public void operChoosePic() {
        isTakePhoto = false;
        isGetPic = true;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showFilePerDialog();
            else
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
        } else getPictureFromLocal();
    }

    /**
     * 拍照权限提示
     */
    public void showPhotoPerDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("需要获取访问您的相机权限，以确保您可以正常拍照。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO);
                    }
                }).create().show();
    }

    /**
     * 文件权限提示
     */
    public void showFilePerDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("需要获取存储文件权限，以确保可以正常保存拍摄或选取的图片。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, PERMISSIONS_REQUEST_FILE);
                    }
                }).create().show();
    }


    /**
     * 拍照
     */
    public void takePhoto() {

        String mUUID = UUID.randomUUID().toString();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
        File file = new File(mPath);

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
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_TAKING_PHOTO);
    }

    public void operTakePhotoMore() {
        String mUUID = UUID.randomUUID().toString();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
        File file = new File(photoPath);
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
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_TAKING_PHOTO);
    }

    /**
     * 从本地选择图片
     */
    public void getPictureFromLocal() {
        Intent innerIntent =
                new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        ((Activity) mContext).startActivityForResult(wrapperIntent, REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL);
    }

    /**
     * 处理拍照并剪裁
     */
    public void dealTakePhotoThenZoom() {
        startPhotoZoom(new File(mPath), TARGET_HEAD_SIZE);
    }

    /**
     * 处理选择图片并剪裁
     */
    public void dealChoosePhotoThenZoom(Intent data) {
        if (data == null) return;
        Uri uri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = mContext.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
            }
            startPhotoZoom(new File(IMAGE_SAVE_PATH), TARGET_HEAD_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 剪裁图片
     */
    private Uri imagUri;

    public void startPhotoZoom(File file, int size) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //通过FileProvider创建一个content类型的Uri
                imagUri = FileProvider.getUriForFile(mContext, "com.ruihuo.ixungen.FileProvider", file);
                mUri = Uri.fromFile(new File(IMAGE_DETAIL_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            } else {
                imagUri = Uri.fromFile(file);
                mUri = Uri.fromFile(new File(IMAGE_DETAIL_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            intent.setDataAndType(imagUri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            //   intent.putExtra("return-data", true);

            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将头像数据上传到服务器
     */
    public void dealZoomPhoto(final ImageView mImageView, final int type, final String id) {
        try {
            if (mUri != null) {
                ContentResolver contentResolver = mContext.getContentResolver();
                InputStream is = contentResolver.openInputStream(mUri);
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                if (bitmap != null) {
                    boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_DETAIL_PATH);
                    if (b) {
                        File file = new File(IMAGE_DETAIL_PATH);
                        Luban.get(mContext)
                                .load(file)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                        LogUtils.e("onStart", "开始压缩");
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        LogUtils.e("onSuccess", "压缩成功");
                                        RequestParams entity;
                                        if (type == 1) {
                                            //用户头像上传
                                            entity = new RequestParams(Url.CHANGE_HEADER_URL);
                                            entity.addBodyParameter("image[]", file);
                                            entity.addBodyParameter("token", XunGenApp.token);
                                        } else {
                                            //宗亲会头像上传
                                            entity = new RequestParams(Url.AGNATION_HEADER_URL);
                                            entity.addBodyParameter("image[]", file);
                                            entity.addBodyParameter("id", id);
                                            entity.addBodyParameter("token", XunGenApp.token);
                                        }

                                        x.http().post(entity, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                if (result == null || result.length() == 0) {
                                                    Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    String code = jsonObject.getString("code");
                                                    String msg = jsonObject.getString("msg");
                                                    if ("0".equals(code)) {

                                                        mImageView.setImageBitmap(bitmap);

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable ex, boolean isOnCallback) {
                                                //LogUtils.e("错误结果： ", ex.getMessage());
                                                Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(CancelledException cex) {

                                            }

                                            @Override
                                            public void onFinished() {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        LogUtils.e("onError", "压缩失败");
                                    }
                                }).launch();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int n = 0;

    /**
     * 将tupian数据上传到服务器
     */
    public void uploadPhoto(final List<String> pathList, final String id) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        final RequestParams entity = new RequestParams(Url.PHOTO_UPLOAD_URL);
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            Luban.get(mContext)
                    .load(file)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            LogUtils.e("onStart", "开始压缩");
                        }

                        @Override
                        public void onSuccess(File file) {
                            LogUtils.e("onSuccess", "压缩成功");
                            n++;
                            entity.addBodyParameter("image[]", file);
                            //当最后一张压缩完成之后再上传。
                            if (n == pathList.size()) {
                                entity.addBodyParameter("id", id);
                                entity.addBodyParameter("token", XunGenApp.token);
                                x.http().post(entity, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        LogUtils.e("onSuccess", result);
                                        loadingDialogUtils.setDimiss();
                                        if (result == null || result.length() == 0) {
                                            Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String code = jsonObject.getString("code");
                                            String msg = jsonObject.getString("msg");
                                            if ("0".equals(code)) {
                                                mCallBack.callBack();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        loadingDialogUtils.setDimiss();
                                        LogUtils.e("错误结果： ", ex.getMessage());
                                        Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {
                                        LogUtils.e("onCancelled", "取消");
                                    }

                                    @Override
                                    public void onFinished() {
                                        LogUtils.e("onFinished", "完成");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e("onError", "压缩失败");
                        }
                    }).launch();
        }

    }

    /**
     * 将tupian数据上传到服务器
     */
    public void uploadPhotoAction(final List<String> pathList, final String id) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        final RequestParams entity = new RequestParams(Url.ACTION_ADD_PHOTO_DETAIL);
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            Luban.get(mContext)
                    .load(file)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            LogUtils.e("onStart", "开始压缩");
                        }

                        @Override
                        public void onSuccess(File file) {
                            LogUtils.e("onSuccess", "压缩成功");
                            n++;
                            entity.addBodyParameter("image[]", file);
                            //当最后一张压缩完成之后再上传。
                            if (n == pathList.size()) {
                                entity.addBodyParameter("id", id);
                                entity.addBodyParameter("token", XunGenApp.token);
                                x.http().post(entity, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        LogUtils.e("onSuccess", result);
                                        loadingDialogUtils.setDimiss();
                                        if (result == null || result.length() == 0) {
                                            Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String code = jsonObject.getString("code");
                                            String msg = jsonObject.getString("msg");
                                            if ("0".equals(code)) {
                                                mCallBack.callBack();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        loadingDialogUtils.setDimiss();
                                        LogUtils.e("错误结果： ", ex.getMessage());
                                        Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {
                                        LogUtils.e("onCancelled", "取消");
                                    }

                                    @Override
                                    public void onFinished() {
                                        LogUtils.e("onFinished", "完成");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e("onError", "压缩失败");
                        }
                    }).launch();
        }

    }

}
