package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.merchant.ImageDeleteAdapter;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.CallBackUrlInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.AddPhotoUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class AddNewsActivity extends BaseActivity {
    private Context mContext;
    private EditText mReply_content;
    private ImageView mAdd_photo;
    private MyGridView mGridView;
    private TextView mLookPrivate;
    private PhotoUtils mPhotoUtils;
    private AddPhotoUtils addPhotoUtils;
    private ImageDeleteAdapter mDeleteAdapter;
    private List<String> imgUrlList = new ArrayList<>();//图片网络地址
    private List<String> imgList = new ArrayList<>();//选择的图片的本地路径
    private LoadingDialogUtils loadingDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_add_news);
        mContext = this;
        pid = getIntent().getStringExtra("pid");
        initView();
        mDeleteAdapter = new ImageDeleteAdapter(imgUrlList, mContext, DeleteImgListener);
        mGridView.setAdapter(mDeleteAdapter);
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitleBar.setTitle("发布动态");
        mTitleBar.mTextRegister.setText("提交");
        mReply_content = (EditText) findViewById(R.id.reply_content);
        mAdd_photo = (ImageView) findViewById(R.id.add_photo);
        mGridView = (MyGridView) findViewById(R.id.gridView);
        mLookPrivate = (TextView) findViewById(R.id.lookPrivate);

        mPhotoUtils = new PhotoUtils(mContext);
        addPhotoUtils = new AddPhotoUtils(mContext, UpdateImgListener);
    }

    private void addListener() {
        mAdd_photo.setOnClickListener(AddPhotoListener);
        mLookPrivate.setOnClickListener(LookPrivateListener);
        mTitleBar.mTextRegister.setOnClickListener(CommitListener);
    }

    View.OnClickListener LookPrivateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, LookPrivateActivity.class);
            startActivityForResult(intent, 2212);
        }
    };
    private String pid = "0";//0-发帖；非0就是评论，传主贴编号，默认0
    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = mReply_content.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder imgs = new StringBuilder();
            for (int i = 0; i < imgUrlList.size() - 1; i++) {
                if (i == 0) imgs.append(imgUrlList.get(i));
                else imgs.append(";" + imgUrlList.get(i));
            }
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);

            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("content", content);
            params.putString("pid", pid);//0-发帖；非0就是评论，传主贴编号，默认0
            params.putString("img", String.valueOf(imgs));
            params.putString("private", privatex + "");
            mHttp.post(Url.ADD_COMMENT, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    Intent intent = new Intent();
                    setResult(8811, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    View.OnClickListener AddPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoUtils.setMode(2);
            mPhotoUtils.showDialog();
        }
    };
    CallBackPositionInterface DeleteImgListener = new CallBackPositionInterface() {
        @Override
        public void callBack(int position) {
            if (position == imgUrlList.size() - 1) {//点击的是最后一张图片
                mPhotoUtils.setMode(2);
                mPhotoUtils.showDialog();
            } else {//点击的是删除
                imgUrlList.remove(position);
                mDeleteAdapter.notifyDataSetChanged();
            }
        }
    };
    CallBackUrlInterface UpdateImgListener = new CallBackUrlInterface() {
        @Override
        public void callBack(List<String> imgUrlLis) {
            loadingDialogUtils.setDimiss();
            imgUrlList.clear();
            imgUrlList.addAll(imgUrlLis);
            imgUrlList.add("");
            mDeleteAdapter.notifyDataSetChanged();
            mAdd_photo.setVisibility(View.GONE);
        }

        @Override
        public void onError() {
            loadingDialogUtils.setDimiss();
        }
    };
    private int privatex = 0;

    private void setPrivate(int privatex) {
        switch (privatex) {
            case 0:
                mLookPrivate.setText("公开");
                break;
            case 1:
                mLookPrivate.setText("仅好友可见");
                break;
            case 2:
                mLookPrivate.setText("仅自己可见");
                break;
            case 3:
                mLookPrivate.setText("仅宗亲会可见");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2212 && resultCode == 2212) {
            if (data != null) {
                privatex = data.getIntExtra("privatex", 0);
                setPrivate(privatex);
            }
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO:
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    //拍照的结果
                    imgList.clear();
                    imgList.add(PhotoUtils.photoPath);
                    addPhotoUtils.getImgUrl(imgList, imgUrlList);

                    break;
                case PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL:
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    //相册选取的结果

                    List<String> pathList = (List<String>) data.getSerializableExtra("data");
                    imgList.clear();
                    imgList.addAll(pathList);
                    addPhotoUtils.getImgUrl(imgList, imgUrlList);

                    break;
            }

        }
    }
}
