package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

/**
 * @ author yudonghui
 * @ date 2017/4/11
 * @ describe May the Buddha bless bug-free！！
 */
public class EditorPhotoInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView mPhoto_name;
    private TextView mDeletePhoto;
    private TextView mPhoto_remark;
    private LinearLayout mLl_photo_cover;
    private ImageView mImage_photo_cover;
    private Context mContext;
    private String photoName;
    private String photoRemark;
    private String associationId;
    private String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_editor_photo_info);
        Intent intent = getIntent();
        associationId = intent.getStringExtra("associationId");
        photoId = intent.getStringExtra("photoId");
        photoName = intent.getStringExtra("photoName");
        photoRemark = intent.getStringExtra("photoRemark");
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("编辑相册信息");
        mTitleBar.mTextRegister.setText("完成");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mPhoto_name = (TextView) findViewById(R.id.photo_name);
        mPhoto_remark = (TextView) findViewById(R.id.photo_remark);
        mLl_photo_cover = (LinearLayout) findViewById(R.id.ll_photo_cover);
        mImage_photo_cover = (ImageView) findViewById(R.id.image_photo_cover);
        mDeletePhoto = (TextView) findViewById(R.id.delete_photo);
        mPhoto_name.setText(TextUtils.isEmpty(photoName) ? "" : photoName);
        mPhoto_remark.setText(TextUtils.isEmpty(photoRemark) ? "" : photoRemark);
    }

    private void addListener() {
        mPhoto_name.setOnClickListener(this);
        mPhoto_remark.setOnClickListener(this);
        mTitleBar.mTextRegister.setOnClickListener(this);
        mDeletePhoto.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_name:
                EditDialogUtils editDialogUtils=new EditDialogUtils(mContext);
                editDialogUtils.setTitle("请输入相册名称");
                editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mPhoto_name.setText(message);
                            photoName = message;
                        }

                    }
                });
                break;
            case R.id.photo_remark:
                EditDialogUtils editDialogUtils1=new EditDialogUtils(mContext);
                editDialogUtils1.setTitle("请输入相册信息");
                editDialogUtils1.setConfirm("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mPhoto_remark.setText(message);
                            photoRemark = message;
                        }

                    }
                });
                break;
            case R.id.text_register:
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("associationId", associationId);
                params.putString("id", photoId);
                if (!TextUtils.isEmpty(photoName)) params.putString("albumName", photoName);
                if (!TextUtils.isEmpty(photoRemark)) params.putString("remark", photoRemark);
                mHttp.post(Url.EDITOR_AGNATION_PHOTO_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                break;
            case R.id.delete_photo:
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp1 = HttpUtilsManager.getInstance(mContext);
                Bundle params1 = new Bundle();
                params1.putString("token", XunGenApp.token);
                params1.putString("associationId", associationId);
                params1.putString("id", String.valueOf(photoId));
                mHttp1.post(Url.DELETE_AGNATION_PHOTO_URL, params1, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        Intent intent = new Intent();
                        setResult(303, intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
                break;
        }
    }
}
