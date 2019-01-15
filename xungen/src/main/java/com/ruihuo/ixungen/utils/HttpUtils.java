package com.ruihuo.ixungen.utils;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.mabeijianxi.smallvideorecord2.Log;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.security.PrivateKey;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/25 0025.
 */
public class HttpUtils implements HttpInterface {
    private Context mContext;
    public static HttpUtils mHttpUtils;

    public HttpUtils() {
    }

    public HttpUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static HttpUtils getInstance() {
        if (mHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new HttpUtils();
                }
            }
        }
        return mHttpUtils;
    }

    /*
     * 带参数的get请求
     * */
    public void get(String url, Bundle params, final JsonInterface mJsonInterface) {

        RequestParams entity = new RequestParams(url);
        Set<String> strings = params.keySet();
        for (String key : strings) {
            entity.addQueryStringParameter(key, String.valueOf(params.get(key)));
        }
        LogUtils.e("访问接口：", entity.toString());
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0 || code == 200) {
                        mJsonInterface.onSuccess(result);
                    } else {
                       /* //20007是Token校验错误
                        if (code != 20007) {
                            HintDialogUtils hintDialogUtils=new HintDialogUtils(mContext);
                            hintDialogUtils.setMessage(msg);
                        }*/
                        mJsonInterface.onError(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
               /* HintDialogUtils.setHintDialog(mContext);
                HintDialogUtils.setMessage(ex.getMessage());
                HintDialogUtils.setTvCancel("CANCEL");
                HintDialogUtils.setTvConfirm("OK");*/
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /*
     * 带参数的get请求
     * */
    public void getW(String url, Bundle params, final JsonInterface mJsonInterface) {

        RequestParams entity = new RequestParams(url);
        Set<String> strings = params.keySet();
        for (String key : strings) {
            entity.addQueryStringParameter(key, String.valueOf(params.get(key)));
        }
        String s = entity.toString();
        LogUtils.e("访问接口：", s);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0 || code == 200) {
                        mJsonInterface.onSuccess(result);
                    } else {
                        //20007是Token校验错误
                        if (code != 20007) {
                            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                            hintDialogUtils.setMessage(msg);
                        }
                        mJsonInterface.onError(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
               /* HintDialogUtils.setHintDialog(mContext);
                HintDialogUtils.setMessage(ex.getMessage());
                HintDialogUtils.setTvCancel("CANCEL");
                HintDialogUtils.setTvConfirm("OK");*/
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 对请求下来的数据进行解密
     */
    @Override
    public void getRSA(String url, Bundle params, final JsonInterface mJsonInterface) {
        final String flagSelf = params.getString("flagSelf");
        params.remove("flagSelf");//"true" 需要进行解密的数据
        RequestParams entity = new RequestParams(url);
        Set<String> strings = params.keySet();
        for (String key : strings) {
            entity.addQueryStringParameter(key, String.valueOf(params.get(key)));
        }
        LogUtils.e("访问接口：", entity.toString());
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        if ("true".equals(flagSelf)) {
                            String data = jsonObject.getString("data");
                            if (TextUtils.isEmpty(data) || "[]".equals(data)) {
                                mJsonInterface.onSuccess(result);
                                return;
                            }
                            String serverTime = jsonObject.getString("serverTime");
                            PrivateKey privateKey = RSAUtils.loadPrivateKey(ConstantNum.PRIVATE_KEY);
                            // 因为RSA加密后的内容经Base64再加密转换了一下，所以先Base64解密回来再给RSA解密
                            int up = 0;
                            int total = data.length() / 172;
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < total; i++) {
                                String subString;
                                if (i == total - 1) {
                                    subString = data.substring(up, data.length());
                                } else {
                                    subString = data.substring(up, 172 * (i + 1));
                                }
                                up = 172 * (i + 1);
                                byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(subString), privateKey);
                                String str = new String(decryptByte);
                                Log.e("结果：", i + "%%%" + str);
                                stringBuilder.append(str);
                            }
                            String s = String.valueOf(stringBuilder);
                            int dataIndex = result.indexOf("data");
                            String substring = result.substring(0, dataIndex);
                            String string = substring + "data\":" + s + ",\"serverTime\":\"" + serverTime + "\"}";
                            LogUtils.e("onSuccess", string);
                            mJsonInterface.onSuccess(string);
                        } else {
                            mJsonInterface.onSuccess(result);
                        }

                    } else {
                        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                        hintDialogUtils.setMessage(msg);
                        mJsonInterface.onError(result);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private ProgressDialog progressDialog;

    @Override
    public void updateFile(String url, final Context mContext, JsonInterface mJsonInterface) {
        // url = "http://res.51caixiang.com/apk/lottery_android_daxiang.apk";
        String[] split = url.split("\\/");
        // final String path = Environment.getExternalStorageDirectory().getPath() + separator + split[split.length - 1];
        final String path = mContext.getExternalCacheDir() + File.separator + split[split.length - 1];
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.setIcon(R.drawable.icon_1);
        LogUtils.e("路径:", path);
        RequestParams entity = new RequestParams(url);
        entity.setSaveFilePath(path);
        entity.setConnectTimeout(10000);
        LogUtils.e("访问接口：", entity.toString());
        x.http().get(entity, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("努力下载中......");
                progressDialog.show();
                progressDialog.setMax(100);
                int cur = (int) (current * 100 / total);
                progressDialog.setProgress(cur);
            }

            @Override
            public void onSuccess(File result) {
                Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                //安装apk
                Intent intent = new Intent();
                //执行动作
                intent.setAction(Intent.ACTION_VIEW);
                Uri mUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //通过FileProvider创建一个content类型的Uri
                    mUri = FileProvider.getUriForFile(mContext, "com.ruihuo.ixungen.FileProvider", result);
                } else {
                    mUri = Uri.fromFile(result);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                //执行的数据类型
                intent.setDataAndType(mUri, "application/vnd.android.package-archive");
                mContext.startActivity(intent);
                System.exit(0);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(mContext, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
    public void jsonByUrl(String url, JsonInterface mJsonInterface) {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void post(String url, Bundle params, final JsonInterface mJsonInterface) {
        RequestParams entity = new RequestParams(url);
        //entity.setBodyContent();
        entity.setAsJsonContent(true);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        Set<String> strings = params.keySet();
        TreeMap<String, Object> map = new TreeMap<>();
        try {
            for (String key : strings) {
                data.put(key, params.get(key));
                map.put(key, params.get(key));
                //LogUtils.e("key&&&", key + "==" + params.get(key));
            }
            json.put("sign", HttpMd5.buildSign(map));
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        entity.setBodyContent(json.toString());
        LogUtils.e("请求数据：", json.toString());
        LogUtils.e("访问接口：", entity.toString());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0 || code == 200) {
                        mJsonInterface.onSuccess(result);
                    } else {
                        //20007是Token校验错误
                        if (code != 20007) {
                            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                            hintDialogUtils.setMessage(msg);
                            hintDialogUtils.setVisibilityCancel();
                        }
                        mJsonInterface.onError(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
                /*HintDialogUtils.setHintDialog(mContext);
                HintDialogUtils.setMessage("网络请求错误");*/
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void postW(String url, Bundle params, final JsonInterface mJsonInterface) {
        RequestParams entity = new RequestParams(url);
        //entity.setBodyContent();
        entity.setAsJsonContent(true);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        Set<String> strings = params.keySet();
        TreeMap<String, Object> map = new TreeMap<>();
        try {
            for (String key : strings) {
                data.put(key, params.get(key));
                map.put(key, params.get(key));
                //LogUtils.e("key&&&", key + "==" + params.get(key));
            }
            json.put("sign", HttpMd5.buildSign(map));
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        entity.setBodyContent(json.toString());
        LogUtils.e("请求数据：", json.toString());
        LogUtils.e("访问接口：", entity.toString());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                mJsonInterface.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /*
     * 带参数的get请求
     * */
    public void getC(String url, Bundle params, final JsonInterface mJsonInterface) {

        RequestParams entity = new RequestParams(url);
        Set<String> strings = params.keySet();
        for (String key : strings) {
            entity.addQueryStringParameter(key, String.valueOf(params.get(key)));
        }
        LogUtils.e("访问接口：", entity.toString());
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                mJsonInterface.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public static void display(ImageView imageView, String avatar, int radius, int width, int height) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setSize(width, height)//图片大小
                .setRadius(radius)//ImageView圆角半径
                .setCrop(true)
                .setFailureDrawableId(R.mipmap.default_actors)
                .setLoadingDrawableId(R.mipmap.default_actors)
                .build();
        LogUtils.e("访问接口：", Url.PHOTO_URL + avatar);
        x.image().bind(imageView, avatar, imageOptions);
    }

    public static void display(String avatar, ImageView imageView, int radius, int width, int height) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setSize(width, height)//图片大小
                .setRadius(radius)//ImageView圆角半径
                .setCrop(true)
                .setFailureDrawableId(R.mipmap.default_actors)
                .setLoadingDrawableId(R.mipmap.default_actors)
                .build();
        LogUtils.e("访问接口：", avatar);
        x.image().bind(imageView, avatar, imageOptions);
    }

    //圆形图
    public static void display(ImageView imageView, String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(true)
                .setCrop(true)
                .setIgnoreGif(false)
                .setFailureDrawableId(R.mipmap.default_header)
                .setLoadingDrawableId(R.mipmap.default_header)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    /*   *//*
     * * 带参数的post请求
     * *//*
    public void post(String url, Bundle params, final JsonInterface mJsonInterface) {
        RequestParams entity = new RequestParams(url);
        Set<String> strings = params.keySet();
        for (String key : strings) {
            entity.addBodyParameter(key, String.valueOf(params.get(key)));
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                mJsonInterface.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }*/

   /* public void post(String url, Bundle params, final JsonInterface mJsonInterface) {
        RequestParams entity = new RequestParams(url);
        //entity.setBodyContent();
        entity.setAsJsonContent(true);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        Set<String> strings = params.keySet();
        TreeMap<String, Object> map = new TreeMap<>();
        try {
            for (String key : strings) {
                data.put(key, params.get(key));
                map.put(key, params.get(key));
                LogUtils.e("key&&&", key + "==" + params.get(key));
            }
            json.put("sign", HttpMd5.buildSign(map));
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        entity.setBodyContent(json.toString());
        LogUtils.e("数据：", json.toString());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                mJsonInterface.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());

                mJsonInterface.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }*/
}
