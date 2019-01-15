package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.CallBackUrlInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.AddPhotoUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class EditorCommentActivity extends BaseNoTitleActivity {
    private Context mContext;
    private com.ruihuo.ixungen.view.TitleBar mTitleBar;
    private com.ruihuo.ixungen.view.CircleImageView mLogo;
    private TextView mShopsName;
    private EditText mReply_content;
    private ImageView mAdd_photo;
    private GridView mGridView;
    private TextView mZeroStar;
    private ImageView mOneStar;
    private ImageView mTwoStar;
    private ImageView mThreeStar;
    private ImageView mFourStar;
    private ImageView mFiveStar;
    private PhotoUtils mPhotoUtils;
    private EditText mConsumeMoney;
    private AddPhotoUtils addPhotoUtils;
    private ImageDeleteAdapter mDeleteAdapter;
    private LoadingDialogUtils loadingDialogUtils;
    private List<String> imgList = new ArrayList<>();//选择的图片的本地路径
    private List<String> imgUrlList = new ArrayList<>();//图片网络地址
    private String orderNo;
    private String shopId;
    private String score = "0";
    private String shopName;
    private String logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_comment);
        mContext = this;
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        shopId = intent.getStringExtra("shopId");
        shopName = intent.getStringExtra("shopName");
        logo = intent.getStringExtra("logo");
        initView();
        mDeleteAdapter = new ImageDeleteAdapter(imgUrlList, mContext, DeleteImgListener);
        mGridView.setAdapter(mDeleteAdapter);
        addListener();
    }

    private void initView() {
        mTitleBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("发表评论");
        mTitleBar.mTextRegister.setText("发布");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mLogo = (com.ruihuo.ixungen.view.CircleImageView) findViewById(R.id.logo);
        mShopsName = (TextView) findViewById(R.id.shopsName);
        mConsumeMoney = (EditText) findViewById(R.id.consumeMoney);
        mReply_content = (EditText) findViewById(R.id.reply_content);
        mAdd_photo = (ImageView) findViewById(R.id.add_photo);
        mGridView = (GridView) findViewById(R.id.gridView);
        mZeroStar = (TextView) findViewById(R.id.zero);
        mOneStar = (ImageView) findViewById(R.id.oneStar);
        mTwoStar = (ImageView) findViewById(R.id.twoStar);
        mThreeStar = (ImageView) findViewById(R.id.threeStar);
        mFourStar = (ImageView) findViewById(R.id.fourStar);
        mFiveStar = (ImageView) findViewById(R.id.fiveStar);
        mPhotoUtils = new PhotoUtils(mContext);
        addPhotoUtils = new AddPhotoUtils(mContext, UpdateImgListener);
        mShopsName.setText(TextUtils.isEmpty(shopName) ? "" : shopName);
        PicassoUtils.setImgView(logo, R.mipmap.default_header, mContext, mLogo);
    }


    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdd_photo.setOnClickListener(AddPhotoListener);
        mZeroStar.setOnClickListener(StarListener);
        mOneStar.setOnClickListener(StarListener);
        mTwoStar.setOnClickListener(StarListener);
        mThreeStar.setOnClickListener(StarListener);
        mFourStar.setOnClickListener(StarListener);
        mFiveStar.setOnClickListener(StarListener);

        mTitleBar.mTextRegister.setOnClickListener(SendListener);
    }

    View.OnClickListener SendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = mReply_content.getText().toString();
            String cost = mConsumeMoney.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(mContext, "请输入评论内容！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(cost)) {
                Toast.makeText(mContext, "请输入人均消费金额", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder imgs = new StringBuilder();
            for (int i = 0; i < imgUrlList.size() - 1; i++) {
                if (i == 0) imgs.append(imgUrlList.get(i));
                else imgs.append(";" + imgUrlList.get(i));
            }
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("shopId", shopId);
            params.putString("content", content);
            params.putString("score", score);
            params.putString("cost", cost);
            params.putString("orderNo", orderNo);
            if (imgUrlList.size() > 1)
                params.putString("imgs", String.valueOf(imgs));
            mHttp.post(Url.SHOP_COMMENT, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent();
                    setResult(332, intent);
                    finish();
                }

                @Override
                public void onError(String message) {

                }
            });

        }
    };
    View.OnClickListener StarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.zero:
                    mOneStar.setImageResource(R.mipmap.star_gray);
                    mTwoStar.setImageResource(R.mipmap.star_gray);
                    mThreeStar.setImageResource(R.mipmap.star_gray);
                    mFourStar.setImageResource(R.mipmap.star_gray);
                    mFiveStar.setImageResource(R.mipmap.star_gray);
                    score = "0";
                    break;
                case R.id.oneStar:
                    mOneStar.setImageResource(R.mipmap.star_full);
                    mTwoStar.setImageResource(R.mipmap.star_gray);
                    mThreeStar.setImageResource(R.mipmap.star_gray);
                    mFourStar.setImageResource(R.mipmap.star_gray);
                    mFiveStar.setImageResource(R.mipmap.star_gray);
                    score = "1";
                    break;
                case R.id.twoStar:
                    mOneStar.setImageResource(R.mipmap.star_full);
                    mTwoStar.setImageResource(R.mipmap.star_full);
                    mThreeStar.setImageResource(R.mipmap.star_gray);
                    mFourStar.setImageResource(R.mipmap.star_gray);
                    mFiveStar.setImageResource(R.mipmap.star_gray);
                    score = "2";
                    break;
                case R.id.threeStar:
                    mOneStar.setImageResource(R.mipmap.star_full);
                    mTwoStar.setImageResource(R.mipmap.star_full);
                    mThreeStar.setImageResource(R.mipmap.star_full);
                    mFourStar.setImageResource(R.mipmap.star_gray);
                    mFiveStar.setImageResource(R.mipmap.star_gray);
                    score = "3";
                    break;
                case R.id.fourStar:
                    mOneStar.setImageResource(R.mipmap.star_full);
                    mTwoStar.setImageResource(R.mipmap.star_full);
                    mThreeStar.setImageResource(R.mipmap.star_full);
                    mFourStar.setImageResource(R.mipmap.star_full);
                    mFiveStar.setImageResource(R.mipmap.star_gray);
                    score = "4";
                    break;
                case R.id.fiveStar:
                    mOneStar.setImageResource(R.mipmap.star_full);
                    mTwoStar.setImageResource(R.mipmap.star_full);
                    mThreeStar.setImageResource(R.mipmap.star_full);
                    mFourStar.setImageResource(R.mipmap.star_full);
                    mFiveStar.setImageResource(R.mipmap.star_full);
                    score = "5";
                    break;
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
