package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.LogUtils;
import com.ruihuo.ixungen.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class SuccessActivity extends AppCompatActivity {
    private ImageView mImageView;
    private EditText mEditText;
    private String imgUrl;
    private Context mContext;
    private ImageView mBack;
    private TextView mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        mContext = this;
        imgUrl = getIntent().getStringExtra("imgUrl");
        initView();
        int x = DisplayUtilX.dip2px(100);
        mImageView.setImageBitmap(getImageThumbnail(x, x));
        addListener();
    }


    //初始化
    private void initView() {
        mBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mSend = (TextView) findViewById(R.id.text_send);
        mEditText = (EditText) findViewById(R.id.remark);
        mImageView = (ImageView) findViewById(R.id.video_bg);
    }

    private Bitmap getImageThumbnail(int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imgUrl, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imgUrl, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private void addListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    private void upload() {
        RequestParams entity = new RequestParams(Url.UPLOAD_VIDEO);
        File file = new File(imgUrl);
        entity.addBodyParameter("token", XunGenApp.token);
        String value = mEditText.getText().toString();
        if (!TextUtils.isEmpty(value))
            entity.addBodyParameter("remark", value);
        entity.addBodyParameter("video", file);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtils.toast(mContext, "上传成功");
                        Intent intent = new Intent();
                        setResult(601, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.e("onCancelled", "取消");
            }

            @Override
            public void onFinished() {
                LogUtils.e("onFinished", "上传完成");
            }
        });
    }
}
