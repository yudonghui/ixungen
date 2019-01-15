package com.ruihuo.ixungen.activity.merchant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ruihuo.ixungen.R;

public class PredictLookActivity extends AppCompatActivity {
    public static final String TAG = PredictLookActivity.class.getSimpleName();

    private ImageView mOliveappLivenessImageView;
    private ImageView mCancel;
    private ImageView mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict_look);
        initView();
        byte[] imageContent = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
        mOliveappLivenessImageView.setImageBitmap(image);
    }

    private void initView() {
        mOliveappLivenessImageView = (ImageView) findViewById(R.id.oliveappLivenessImageView);
        mCancel = (ImageView) findViewById(R.id.cancel);
        mConfirm = (ImageView) findViewById(R.id.confirm);
    }
}
