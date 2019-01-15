package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

public class ActionEditorPhotoInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mPhoto_name;
    private TextView mDeletePhoto;
    private TextView mPhoto_remark;
    private LinearLayout mLl_photo_cover;
    private ImageView mImage_photo_cover;
    private Context mContext;
    private String photoName;
    private String address;
    private String associationId;
    private String photoId;
    private ImageView mImageBack;
    private TextView mEdit;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_editor_photo_info);
        Intent intent = getIntent();
        photoId = intent.getStringExtra("photoId");
        photoName = intent.getStringExtra("photoName");
        address = intent.getStringExtra("address");
        mContext = this;
        initView();
        addListener();
    }

    private void initView() {
        mImageBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mTitle = (TextView) findViewById(R.id.text_title);
        mEdit = (TextView) findViewById(R.id.edit);
        mPhoto_name = (TextView) findViewById(R.id.photo_name);
        mPhoto_remark = (TextView) findViewById(R.id.photo_remark);
        mLl_photo_cover = (LinearLayout) findViewById(R.id.ll_photo_cover);
        mImage_photo_cover = (ImageView) findViewById(R.id.image_photo_cover);
        mDeletePhoto = (TextView) findViewById(R.id.delete_photo);
        mPhoto_name.setText(TextUtils.isEmpty(photoName) ? "" : photoName);
        mPhoto_remark.setText(TextUtils.isEmpty(address) ? "" : address);
    }

    private void addListener() {
        mImageBack.setOnClickListener(this);
        mPhoto_name.setOnClickListener(this);
        mPhoto_remark.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mDeletePhoto.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_titlebar_back:
                finish();
                break;
            case R.id.photo_name:
                EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
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
                EditDialogUtils editDialogUtils1 = new EditDialogUtils(mContext);
                editDialogUtils1.setTitle("请输入相册信息");
                editDialogUtils1.setConfirm("确定", new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            mPhoto_remark.setText(message);
                            address = message;
                        }

                    }
                });
                break;
            case R.id.edit:
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("id", photoId);
                if (!TextUtils.isEmpty(photoName)) params.putString("albumName", photoName);
                if (!TextUtils.isEmpty(address)) params.putString("address", address);
                mHttp.post(Url.ACTION_EDIT_PHOTO_INFO, params, new JsonInterface() {
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
                params1.putString("id", String.valueOf(photoId));
                mHttp1.post(Url.ACTION_DELETE_PHOTO, params1, new JsonInterface() {
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

