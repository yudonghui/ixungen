package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author yudonghui
 * @date 2017/7/17
 * @describe May the Buddha bless bug-free!!!
 */
public class GetPhotoUrl {
    Context mContext;

    public GetPhotoUrl(Context mContext) {
        this.mContext = mContext;
    }

    public void getPhotoUrl(String path, final DialogEditInterface editInterface) {

        File file = new File(path);
        Luban.get(mContext)
                .load(file)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        RequestParams entity = new RequestParams(Url.PHOTO_UPLOAD);
                        entity.addBodyParameter("token", XunGenApp.token);
                        entity.addBodyParameter("image", file);
                        x.http().post(entity, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String fileUrl = jsonObject.getString("fid");
                                    String url = Url.PHOTO_URL + fileUrl;
                                    editInterface.callBack(url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("返回错误", ex.getMessage());
                            }

                            @Override
                            public void onCancelled(Callback.CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        editInterface.callBack("");
                    }
                }).launch();

    }

    public void getPhotoUrl(Bitmap bitmap, final DialogEditInterface editInterface) {
        String mUUID = UUID.randomUUID().toString();
        String mImgPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
        BitmapUtils.saveBitmap(bitmap, mImgPath);
        File file = new File(mImgPath);
        Luban.get(mContext)
                .load(file)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        RequestParams entity = new RequestParams(Url.PHOTO_UPLOAD);
                        entity.addBodyParameter("token", XunGenApp.token);
                        entity.addBodyParameter("image", file);
                        x.http().post(entity, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String fileUrl = jsonObject.getString("fid");
                                    String url = Url.PHOTO_URL + fileUrl;
                                    editInterface.callBack(url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("返回错误", ex.getMessage());
                                editInterface.callBack("");
                            }

                            @Override
                            public void onCancelled(Callback.CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        editInterface.callBack("");
                    }
                }).launch();

    }

}
