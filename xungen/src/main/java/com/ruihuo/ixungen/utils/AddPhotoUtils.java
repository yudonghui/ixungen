package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.CallBackUrlInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author yudonghui
 * @date 2017/8/14
 * @describe May the Buddha bless bug-free!!!
 */
public class AddPhotoUtils {
    private List<String> imgList;
    private CallBackUrlInterface mListener;
    private Context mContext;

    public AddPhotoUtils(Context mContext, CallBackUrlInterface mListener) {
        this.mListener = mListener;
        this.mContext = mContext;
    }

    public AddPhotoUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void getImgUrl(List<String> imgList, List<String> urlList) {
        this.imgList = imgList;
        this.imgUrlList.clear();
        this.imgUrlList.addAll(urlList);
        if (imgUrlList.contains("")) {
            int i = imgUrlList.indexOf("");
            imgUrlList.remove(i);//这是第第二次 选择的时候。
        }
        n = 0;
        for (int i = 0; i < imgList.size(); i++) {
            getImgUrl(imgList.get(i));
        }
    }

    private int n = 0;
    private List<String> imgUrlList = new ArrayList<>();

    private void getImgUrl(String path) {
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
                                Log.e("onSuccess", result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String fileUrl = jsonObject.getString("fid");
                                    imgUrlList.add(Url.PHOTO_URL + fileUrl);
                                    n++;
                                    if (n == imgList.size()) {
                                        mListener.callBack(imgUrlList);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mListener.onError();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("返回错误", ex.getMessage());
                                mListener.onError();
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
                        mListener.onError();
                    }
                }).launch();

    }

    public void getImgUrl(String path, final CallBack mListener) {
        if (TextUtils.isEmpty(path)) return;
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
                                    mListener.onSuccess(Url.PHOTO_URL + fileUrl);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("返回错误", ex.getMessage());
                                mListener.onError();
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
                        mListener.onError();
                    }
                }).launch();

    }

    public interface CallBack {
        void onSuccess(String url);

        void onError();
    }
}
