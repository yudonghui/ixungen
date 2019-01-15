package com.ruihuo.ixungen.activity.merchant;

import android.content.Intent;
import android.os.Bundle;

import com.oliveapp.face.idcardcaptorsdk.captor.CapturedIDCardImage;
import com.ruihuo.ixungen.activity.SampleIdcardCaptorMainActivity;

public class SampleIdcardCaptorActivity extends SampleIdcardCaptorMainActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        // 如果有设置全局包名的需要, 在这里进行设置
//        PackageNameManager.setPackageName();
        super.onCreate(savedInstance);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFrameResult(int status) {
        super.onFrameResult(status);
    }

    /**
     * 捕获到最好的一张身份证照片
     */
    @Override
    public void onIDCardCaptured(CapturedIDCardImage data) {
        super.onIDCardCaptured(data);

        // 处理Activity跳转逻辑
       /* Intent i = new Intent(SampleIdcardCaptorActivity.this, PredictLookActivity.class);
        i.putExtra("image", data.idcardImageData);
        startActivity(i);*/
        Intent intent = new Intent();
        intent.putExtra("image", data.idcardImageData);
        setResult(321, intent);
        finish();
    }
}
