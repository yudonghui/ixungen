package com.ruihuo.ixungen.activity.merchant;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ruihuo.ixungen.utils.BitmapUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddGoodsFoodActivity extends BaseNoTitleActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private FrameLayout mLl_cover;
    private ImageView mCover;
    private ImageView mAdd_cover;
    private EditText mGoodsName;
    private EditText mGoodsContent;
    private ImageView mAdd_photo;
    private com.ruihuo.ixungen.view.MyGridView mGridView;
    private EditText mPrice;
    private EditText mBeforePrice;
    private EditText mGoodsNum;
    private ImageView mIsCancel;
    private TextView mConfirm;
    private PhotoUtils mPhotoUtils;
    private AddPhotoUtils addPhotoUtils;
    private LoadingDialogUtils loadingDialogUtils;
    private ImageDeleteAdapter mDeleteAdapter;
    private ShopsFormBean.DataBean dataBean;
    private String shopId;//商铺编号
    private int displayWidth;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_food);
        mContext = this;
        shopId = getIntent().getStringExtra("shopId");
        dataBean = (ShopsFormBean.DataBean) getIntent().getSerializableExtra("dataBean");
        type = getIntent().getIntExtra("type", 0);
        initView();
        mDeleteAdapter = new ImageDeleteAdapter(imgUrlList, mContext, DeleteImgListener);
        mGridView.setAdapter(mDeleteAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mAdd_cover = (ImageView) findViewById(R.id.add_cover);
        mGoodsName = (EditText) findViewById(R.id.goodsName);
        mLl_cover = (FrameLayout) findViewById(R.id.ll_cover);
        mCover = (ImageView) findViewById(R.id.cover);
        mGoodsContent = (EditText) findViewById(R.id.goodsContent);
        mAdd_photo = (ImageView) findViewById(R.id.add_photo);
        mGridView = (com.ruihuo.ixungen.view.MyGridView) findViewById(R.id.gridView);
        mPrice = (EditText) findViewById(R.id.price);
        mBeforePrice = (EditText) findViewById(R.id.beforePrice);
        mGoodsNum = (EditText) findViewById(R.id.goodsNum);
        mIsCancel = (ImageView) findViewById(R.id.isCancel);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mPhotoUtils = new PhotoUtils(mContext);
        addPhotoUtils = new AddPhotoUtils(mContext, UpdateImgListener);
        displayWidth = DisplayUtilX.getDisplayWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayWidth, displayWidth / 2);
        mLl_cover.setLayoutParams(layoutParams);


        int verspace = DisplayUtilX.dip2px(50);
        int resize = (displayWidth - verspace) / 4;
        int margin = DisplayUtilX.dip2px(10);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(resize, resize);
        layoutParams2.leftMargin = margin;
        layoutParams2.bottomMargin = margin;
        layoutParams2.topMargin = margin;
        mAdd_photo.setLayoutParams(layoutParams2);

        if (dataBean != null) {
            mAdd_photo.setVisibility(View.GONE);
            String name = dataBean.getName();
            String cover = dataBean.getCover();
            String img = dataBean.getImg();
            String price = dataBean.getPrice();
            String original_price = dataBean.getOriginal_price();
            String info = dataBean.getInfo();
            String amount = dataBean.getAmount();
            mGoodsName.setText(TextUtils.isEmpty(name) ? "" : name);
            mPrice.setText(TextUtils.isEmpty(price) ? "" : price);
            mBeforePrice.setText(TextUtils.isEmpty(original_price) ? "" : original_price);
            mGoodsContent.setText(TextUtils.isEmpty(info) ? "" : info);
            mGoodsNum.setText(TextUtils.isEmpty(amount) ? "" : amount);
            if (cover != null) {
                coverPath = cover;
                Picasso.with(mContext)
                        .load(cover)
                        .placeholder(R.mipmap.fengmian)
                        .error(R.mipmap.fengmian)
                        .into(mAdd_cover);
            }
            if (img != null) {
                String[] split = img.split("\\;");
                for (int i = 0; i < split.length; i++) {
                    imgUrlList.add(split[i]);
                }
                imgUrlList.add("");//后面加个可以继续添加的按钮
            }
        }
        if (type == 1) {//餐厅，发布商品

        } else if (type == 4) {//旅游发布商品

        }
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdd_cover.setOnClickListener(AddCoverListener);
        mAdd_photo.setOnClickListener(AddPhotoListener);
        //可否取消
        mIsCancel.setOnClickListener(IsCancelListener);
        mConfirm.setOnClickListener(ConfirmFoodListener);
    }

    private void addData() {

    }

    private String name;
    private String price;
    View.OnClickListener ConfirmFoodListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            name = mGoodsName.getText().toString();
            price = mPrice.getText().toString();
            String beforePrice = mBeforePrice.getText().toString();
            String info = mGoodsContent.getText().toString();
            String amount = mGoodsNum.getText().toString();
            StringBuilder img = new StringBuilder();
            for (int i = 0; i < imgUrlList.size() - 1; i++) {
                if (i == 0) img.append(imgUrlList.get(i));
                else img.append(";" + imgUrlList.get(i));
            }

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(mContext, "请输入商品名称", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(price)) {
                Toast.makeText(mContext, "请输入商品价格", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(beforePrice)) {
                Toast.makeText(mContext, "请输入商品原来价格", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(coverPath)) {
                Toast.makeText(mContext, "请选择一张图片作为封面", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(amount)) {
                Toast.makeText(mContext, "请输入商品的数量", Toast.LENGTH_SHORT).show();
            } else if (imgUrlList.size() <= 1) {
                Toast.makeText(mContext, "请选择几张商品图片", Toast.LENGTH_SHORT).show();
            } else {
                loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                String url;
                if (dataBean == null) {//发布新商品
                    url = Url.SHOP_ADD_SHOPS;
                    params.putString("shopId", shopId);
                    params.putString("type", type + "");//商品类型，1-餐饮；2-酒店 4.旅游
                } else {//修改商品信息
                    url = Url.UPDATE_GOODS;
                    params.putString("goodsId", dataBean.getId());
                }
                params.putString("token", XunGenApp.token);
                params.putString("shopId", shopId);
                params.putString("name", name);
                params.putString("price", price);
                params.putString("cover", coverPath);
                if (!TextUtils.isEmpty(info))
                    params.putString("info", info);
                params.putString("isCancel", isCancel + "");
                params.putString("price", price);
                params.putString("amount", amount);
                params.putString("originalPrice", beforePrice);
                if (!TextUtils.isEmpty(img))
                    params.putString("img", String.valueOf(img));
                mHttp.post(url, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        if (dataBean == null)
                            finish();
                        else {
                            Intent intent = new Intent();
                            setResult(666, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        }
    };
    private int isCover = 1;//1是封面，2是商品的图片
    private String coverPath;
    private List<String> imgList = new ArrayList<>();//选择的图片的本地路径
    private List<String> imgUrlList = new ArrayList<>();//图片网络地址
    View.OnClickListener AddCoverListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isCover = 1;
            mPhotoUtils.setMode(1);
            mPhotoUtils.showDialog();
        }
    };
    View.OnClickListener AddPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isCover = 2;
            mPhotoUtils.setMode(2);
            mPhotoUtils.showDialog();
        }
    };
    CallBackPositionInterface DeleteImgListener = new CallBackPositionInterface() {
        @Override
        public void callBack(int position) {
            if (position == imgUrlList.size() - 1) {//点击的是最后一张图片
                isCover = 2;
                mPhotoUtils.setMode(2);
                mPhotoUtils.showDialog();
            } else {//点击的是删除
                imgUrlList.remove(position);
                if (imgUrlList.size() == 1) {
                    mAdd_photo.setVisibility(View.VISIBLE);
                    imgUrlList.clear();
                    mDeleteAdapter.notifyDataSetChanged();
                } else
                    mDeleteAdapter.notifyDataSetChanged();
            }
        }
    };
    CallBackUrlInterface UpdateImgListener = new CallBackUrlInterface() {
        @Override
        public void callBack(List<String> imgUrlLis) {
            mAdd_photo.setVisibility(View.GONE);
            loadingDialogUtils.setDimiss();
            imgUrlList.clear();
            imgUrlList.addAll(imgUrlLis);
            imgUrlList.add("");
            for (int i = 0; i < imgUrlList.size(); i++) {
                Log.e("图片链接", imgUrlList.get(i) + "共size" + imgUrlList.size());
            }
            mDeleteAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError() {
            loadingDialogUtils.setDimiss();
        }
    };
    private int isCancel = 1;//1-可以取消退款；2-不可以取消退款
    View.OnClickListener IsCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCancel == 1) {
                mIsCancel.setImageResource(R.drawable.btn_off);
                isCancel = 2;
            } else {
                mIsCancel.setImageResource(R.drawable.btn_on);
                isCancel = 1;
            }
        }
    };
    private Bitmap bitmapCover;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO:
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    //拍照的结果
                    if (isCover == 1) {
                        String path = PhotoUtils.mPath;
                        bitmapCover = BitmapFactory.decodeFile(path);
                        addPhotoUtils.getImgUrl(path, CoverListener);
                    } else {
                        imgList.clear();
                        imgList.add(PhotoUtils.photoPath);
                        addPhotoUtils.getImgUrl(imgList, imgUrlList);
                    }
                    break;
                case PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL:
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    //相册选取的结果
                    if (isCover == 1) {
                        try {
                            ContentResolver resolver = mContext.getContentResolver();
                            Uri originalUri = data.getData(); // 获得图片的uri

                            bitmapCover = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            String path = BitmapUtils.saveBitmap(bitmapCover);
                            addPhotoUtils.getImgUrl(path, CoverListener);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        List<String> pathList = (List<String>) data.getSerializableExtra("data");
                        imgList.clear();
                        imgList.addAll(pathList);
                        addPhotoUtils.getImgUrl(imgList, imgUrlList);
                    }
                    break;
            }

        }
    }

    AddPhotoUtils.CallBack CoverListener = new AddPhotoUtils.CallBack() {
        @Override
        public void onSuccess(String url) {
            mAdd_cover.setImageBitmap(bitmapCover);
            coverPath = url;
            loadingDialogUtils.setDimiss();
        }

        @Override
        public void onError() {
            loadingDialogUtils.setDimiss();
        }
    };
}
