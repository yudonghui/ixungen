package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.BitmapUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddShopsActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private LinearLayout mLl_hotel;
    private LinearLayout mLl_hotel2;
    private TextView mTextInfo;//商品简介/房间说明
    private ImageView mAdd_photo;
    private TextView mPhotos;
    private ImageView mOne;
    private ImageView mTwo;
    private ImageView mBig;
    private ImageView mAdmin;
    private ImageView mSuite;
    private ImageView mSuitef;
    private ImageView mWIFI;
    private ImageView mInternet;
    private ImageView mBath;
    private ImageView mAir;
    private ImageView mWater;
    private ImageView mBreakfast;
    private EditText mRoomName;
    private EditText mRoomPrice;
    private EditText mRoomNum;
    private ImageView mIsReserve;
    private ImageView mIsCancel;
    private EditText mCancelRule;
    private EditText mInfo;
    private TextView mConfirm;
    private PhotoUtils photoUtils;
    private List<String> imgList = new ArrayList<>();
    private HttpInterface mHttp;
    private LoadingDialogUtils loadingDialogUtils;
    private int type;//商品类型，1-餐饮；2-酒店
    private ShopsFormBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_add_shops);
        shopId = getIntent().getStringExtra("shopId");
        type = getIntent().getIntExtra("type", 0);
        dataBean = (ShopsFormBean.DataBean) getIntent().getSerializableExtra("dataBean");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mLl_hotel = (LinearLayout) findViewById(R.id.ll_hotel);
        mLl_hotel2 = (LinearLayout) findViewById(R.id.ll_hotel2);
        mTextInfo = (TextView) findViewById(R.id.text_info);

        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mAdd_photo = (ImageView) findViewById(R.id.add_photo);
        mPhotos = (TextView) findViewById(R.id.photos);
        mOne = (ImageView) findViewById(R.id.one);
        mTwo = (ImageView) findViewById(R.id.two);
        mBig = (ImageView) findViewById(R.id.big);
        mAdmin = (ImageView) findViewById(R.id.admin);
        mSuite = (ImageView) findViewById(R.id.suite);
        mSuitef = (ImageView) findViewById(R.id.suitef);
        mWIFI = (ImageView) findViewById(R.id.WIFI);
        mInternet = (ImageView) findViewById(R.id.internet);
        mBath = (ImageView) findViewById(R.id.bath);
        mAir = (ImageView) findViewById(R.id.air);
        mWater = (ImageView) findViewById(R.id.water);
        mBreakfast = (ImageView) findViewById(R.id.breakfast);
        mRoomName = (EditText) findViewById(R.id.roomName);
        mRoomPrice = (EditText) findViewById(R.id.roomPrice);
        mRoomNum = (EditText) findViewById(R.id.roomNum);
        mIsReserve = (ImageView) findViewById(R.id.isReserve);
        mIsCancel = (ImageView) findViewById(R.id.isCancel);
        mCancelRule = (EditText) findViewById(R.id.cancelRule);
        mInfo = (EditText) findViewById(R.id.info);
        mConfirm = (TextView) findViewById(R.id.confirm);

        if (type == 1) {//商品类型，1-餐饮；2-酒店
            mLl_hotel.setVisibility(View.GONE);
            mLl_hotel2.setVisibility(View.GONE);
            mTextInfo.setText("商品简介");
            mInfo.setHint("请输入商品的基本介绍");
        } else {
            mLl_hotel.setVisibility(View.VISIBLE);
            mLl_hotel2.setVisibility(View.VISIBLE);
            mTextInfo.setText("房间说明");
            mInfo.setHint("请输入房间的基本介绍和注意事项以及房间价格所包含的内容。");
        }
        int displayWidth = DisplayUtilX.getDisplayWidth();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(displayWidth, displayWidth / 2);
        mAdd_photo.setLayoutParams(layoutParams);

        photoUtils = new PhotoUtils(mContext);
        mHttp = HttpUtilsManager.getInstance(mContext);
        imgList.add("");

        if (dataBean != null) {
            String name = dataBean.getName();
            String price = dataBean.getPrice();
            String amount = dataBean.getAmount();
            String cancel_rule = dataBean.getCancel_rule();
            String info = dataBean.getInfo();
            classify = dataBean.getClassify();
            String cover = dataBean.getCover();
            String is_cancel = dataBean.getIs_cancel();
            String is_reserve = dataBean.getIs_reserve();
            String service = dataBean.getService();
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomPrice.setText(TextUtils.isEmpty(price) ? "" : price);
            mRoomNum.setText(TextUtils.isEmpty(amount) ? "" : amount);
            mCancelRule.setText(TextUtils.isEmpty(cancel_rule) ? "" : cancel_rule);
            mInfo.setText(TextUtils.isEmpty(info) ? "" : info);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            mRoomName.setText(TextUtils.isEmpty(name) ? "" : name);
            if ("1".equals(is_reserve)) {
                mIsReserve.setImageResource(R.drawable.btn_on);
                isReserve = 1;
            } else {
                mIsReserve.setImageResource(R.drawable.btn_off);
                isReserve = 2;
            }
            if ("1".equals(is_cancel)) {
                mIsCancel.setImageResource(R.drawable.btn_off);
                isCancel = 1;
            } else {
                mIsCancel.setImageResource(R.drawable.btn_on);
                isCancel = 2;
            }
            mOne.setImageResource(R.mipmap.gray);
            switch (classify) {
                case "单人间":
                    mOne.setImageResource(R.mipmap.green);
                    break;
                case "双人标间":
                    mTwo.setImageResource(R.mipmap.green);
                    break;
                case "豪华间":
                    mBig.setImageResource(R.mipmap.green);
                    break;
                case "大床房":
                    mAdmin.setImageResource(R.mipmap.green);
                    break;
                case "套间":
                    mSuite.setImageResource(R.mipmap.green);
                    break;
                case "复式套间":
                    mSuitef.setImageResource(R.mipmap.green);
                    break;
            }
            if (!TextUtils.isEmpty(service)) {
                String[] split = service.split("\\,");
                for (int i = 0; i < split.length; i++) {
                    switch (split[i]) {
                        case "WIFI":
                            mapFactivity.put("WIFI", "WIFI");
                            mWIFI.setImageResource(R.mipmap.green);
                            break;
                        case "电脑宽带":
                            mapFactivity.put("电脑宽带", "电脑宽带");
                            mInternet.setImageResource(R.mipmap.green);
                            break;
                        case "独立卫浴":
                            mapFactivity.put("独立卫浴", "独立卫浴");
                            mBath.setImageResource(R.mipmap.green);
                            break;
                        case "空调":
                            mapFactivity.put("空调", "空调");
                            mAir.setImageResource(R.mipmap.green);
                            break;
                        case "茶水饮料":
                            mapFactivity.put("茶水饮料", "茶水饮料");
                            mWater.setImageResource(R.mipmap.green);
                            break;
                        case "早餐":
                            mapFactivity.put("早餐", "早餐");
                            mBreakfast.setImageResource(R.mipmap.green);
                            break;
                    }
                }
            }
            if (!TextUtils.isEmpty(cover)) {
                String[] split = cover.split("\\;");
                for (int i = 0; i < split.length; i++) {
                    imgList.add(split[i]);
                }
                Picasso.with(mContext)
                        .load(split[0])
                        .placeholder(R.mipmap.add_photo_1)
                        .error(R.mipmap.add_photo_1)
                        .into(mAdd_photo);
                mPhotos.setText(split.length + "");
            }
        }


    }

    private void addData() {

    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdd_photo.setOnClickListener(AddPhotoListener);
        //选择房间类型
        mOne.setOnClickListener(RoomTypeListener);
        mTwo.setOnClickListener(RoomTypeListener);
        mBig.setOnClickListener(RoomTypeListener);
        mAdmin.setOnClickListener(RoomTypeListener);
        mSuite.setOnClickListener(RoomTypeListener);
        mSuitef.setOnClickListener(RoomTypeListener);
        //提供的设施
        mWIFI.setOnClickListener(FacilityListener);
        mInternet.setOnClickListener(FacilityListener);
        mBath.setOnClickListener(FacilityListener);
        mAir.setOnClickListener(FacilityListener);
        mWater.setOnClickListener(FacilityListener);
        mBreakfast.setOnClickListener(FacilityListener);
        //可否预定
        mIsReserve.setOnClickListener(IsReserveListener);
        //可否取消
        mIsCancel.setOnClickListener(IsCancelListener);
        //提交
        if (type == 1)
            mConfirm.setOnClickListener(ConfirmFoodListener);
        else mConfirm.setOnClickListener(ConfirmHotelListener);

    }

    private String name;
    private String price;
    private String amount;
    View.OnClickListener ConfirmHotelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            name = mRoomName.getText().toString();
            price = mRoomPrice.getText().toString();
            amount = mRoomNum.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(mContext, "请输入商品名称", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(price)) {
                Toast.makeText(mContext, "请输入商品价格", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(amount)) {
                Toast.makeText(mContext, "请输入房间数量", Toast.LENGTH_SHORT).show();
            } else if (imgList.size() <= 1) {
                Toast.makeText(mContext, "请选择几张商品图片", Toast.LENGTH_SHORT).show();
            } else {
                updateImg();
            }
        }
    };
    View.OnClickListener ConfirmFoodListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            name = mRoomName.getText().toString();
            price = mRoomPrice.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(mContext, "请输入商品名称", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(price)) {
                Toast.makeText(mContext, "请输入商品价格", Toast.LENGTH_SHORT).show();
            } else if (imgList.size() <= 1) {
                Toast.makeText(mContext, "请选择几张商品图片", Toast.LENGTH_SHORT).show();
            } else {
                updateImg();
            }
        }
    };
    private int isReserve = 1;//1-可以预定；2-不可以预定
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
    View.OnClickListener IsReserveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isReserve == 1) {
                mIsReserve.setImageResource(R.drawable.btn_off);
                isReserve = 2;
            } else {
                mIsReserve.setImageResource(R.drawable.btn_on);
                isReserve = 1;
            }
        }
    };
    private HashMap<String, String> mapFactivity = new HashMap<>();
    View.OnClickListener FacilityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.WIFI:
                    if (mapFactivity.containsKey("WIFI")) {
                        mapFactivity.remove("WIFI");
                        mWIFI.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("WIFI", "WIFI");
                        mWIFI.setImageResource(R.mipmap.green);
                    }
                    break;
                case R.id.internet:
                    if (mapFactivity.containsKey("电脑宽带")) {
                        mapFactivity.remove("电脑宽带");
                        mInternet.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("电脑宽带", "电脑宽带");
                        mInternet.setImageResource(R.mipmap.green);
                    }
                    break;
                case R.id.bath:
                    if (mapFactivity.containsKey("独立卫浴")) {
                        mapFactivity.remove("独立卫浴");
                        mBath.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("独立卫浴", "独立卫浴");
                        mBath.setImageResource(R.mipmap.green);
                    }
                    break;
                case R.id.air:
                    if (mapFactivity.containsKey("空调")) {
                        mapFactivity.remove("空调");
                        mAir.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("空调", "空调");
                        mAir.setImageResource(R.mipmap.green);
                    }
                    break;
                case R.id.water:
                    if (mapFactivity.containsKey("茶水饮料")) {
                        mapFactivity.remove("茶水饮料");
                        mWater.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("茶水饮料", "茶水饮料");
                        mWater.setImageResource(R.mipmap.green);
                    }
                    break;
                case R.id.breakfast:
                    if (mapFactivity.containsKey("早餐")) {
                        mapFactivity.remove("早餐");
                        mBreakfast.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("早餐", "早餐");
                        mBreakfast.setImageResource(R.mipmap.green);
                    }
                    break;
            }
        }
    };
    private String classify = "单人间";//房间类型
    View.OnClickListener RoomTypeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOne.setImageResource(R.mipmap.gray);
            mTwo.setImageResource(R.mipmap.gray);
            mBig.setImageResource(R.mipmap.gray);
            mAdmin.setImageResource(R.mipmap.gray);
            mSuitef.setImageResource(R.mipmap.gray);
            mSuite.setImageResource(R.mipmap.gray);
            switch (v.getId()) {
                case R.id.one:
                    mOne.setImageResource(R.mipmap.green);
                    classify = "单人间";
                    break;
                case R.id.two:
                    mTwo.setImageResource(R.mipmap.green);
                    classify = "双人标间";
                    break;
                case R.id.big:
                    mBig.setImageResource(R.mipmap.green);
                    classify = "豪华间";
                    break;
                case R.id.admin:
                    mAdmin.setImageResource(R.mipmap.green);
                    classify = "大床房";
                    break;
                case R.id.suite:
                    mSuite.setImageResource(R.mipmap.green);
                    classify = "套间";
                    break;
                case R.id.suitef:
                    mSuitef.setImageResource(R.mipmap.green);
                    classify = "复式套间";
                    break;
            }
        }
    };
    View.OnClickListener AddPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (imgList.size() > 1) {
                Intent intent = new Intent(mContext, RoomPhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgList", (Serializable) imgList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 234);
            } else {
                photoUtils.setMode(2);
                photoUtils.showDialog();
            }
        }
    };
    private StringBuilder imgs;
    private int n = 0;

    /**
     * 上传图片，根据图片路径一个一个上传后得到 图片网上链接，拼接到一起，
     * 作为一个参数上传。
     */
    private List<String> urlList = new ArrayList<>();
    private List<String> pathList = new ArrayList<>();

    private void updateImg() {
        urlList.clear();
        pathList.clear();
        imgs = new StringBuilder();
        if (imgList.size() > 1)
            loadingDialogUtils = new LoadingDialogUtils(mContext);

        for (int i = 1; i < imgList.size(); i++) {
            String path = imgList.get(i);
            if (path.contains("https://")) {
                urlList.add(path);
            } else if (!TextUtils.isEmpty(path)) {
                pathList.add(path);
            }
        }
        for (int i = 0; i < urlList.size(); i++) {
            if (i == 0) imgs.append(urlList.get(i));
            else imgs.append(";" + urlList.get(i));
        }
        if (pathList.size() == 0) {//pathList是0 的时候urlList肯定不为0.这个时候需要直接请求网络。所以要判断一下。
            if (type == 1)
                addFoodImage();//把拼接好的图片链接上传
            else addHotelImage();//把拼接好的图片链接上传
        } else {
            for (int i = 0; i < pathList.size(); i++) {
                Log.e("本地路径", pathList.get(i));
                getImgUrl(pathList.get(i));
            }
        }
    }

    private void getImgUrl(String path) {
        File file = new File(path);
        RequestParams entity = new RequestParams(Url.PHOTO_UPLOAD);
        entity.addBodyParameter("token", XunGenApp.token);
        entity.addBodyParameter("image", file);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String fileUrl = jsonObject.getString("fid");
                    if (urlList.size() > 0) {
                        imgs.append(";" + Url.PHOTO_URL + fileUrl);
                    } else {
                        if (n == 0) imgs.append(Url.PHOTO_URL + fileUrl);
                        else imgs.append(";" + Url.PHOTO_URL + fileUrl);
                    }
                    n++;
                    Log.e("网络请求的路径", imgs.toString());
                    if (n >= pathList.size()) {
                        if (type == 1)
                            addFoodImage();//把拼接好的图片链接上传
                        else addHotelImage();//把拼接好的图片链接上传
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingDialogUtils.setDimiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("返回错误", ex.getMessage());
                loadingDialogUtils.setDimiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private String shopId;//商铺编号

    private void addHotelImage() {
        String info = mInfo.getText().toString();
        String cancelRule = mCancelRule.getText().toString();
        StringBuilder service = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : mapFactivity.entrySet()) {
            if (i == 0)
                service.append(entry.getKey());
            else service.append("," + entry.getKey());
            i++;
        }
        String url;
        Bundle params = new Bundle();
        if (dataBean == null) {//发布新商品
            url = Url.SHOP_ADD_SHOPS;
            params.putString("shopId", shopId);
            params.putString("type", type + "");//商品类型，1-餐饮；2-酒店
        } else {//修改商品信息
            url = Url.UPDATE_GOODS;
            params.putString("goodsId", dataBean.getId());
        }
        params.putString("token", XunGenApp.token);
        params.putString("name", name);
        params.putString("price", price);
        params.putString("cover", String.valueOf(imgs));
        params.putString("info", info);
        params.putString("amount", amount);//房间数量
        params.putString("isReserve", isReserve + "");
        params.putString("isCancel", isCancel + "");
        params.putString("classify", classify);
        if (!TextUtils.isEmpty(cancelRule))
            params.putString("cancelRule", cancelRule);//退款规则
        if (service.length() > 0)
            params.putString("service", String.valueOf(service));//提供的设施服务
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

    private void addFoodImage() {
        String info = mInfo.getText().toString();

        Bundle params = new Bundle();
        String url;
        if (dataBean == null) {//发布新商品
            url = Url.SHOP_ADD_SHOPS;
            params.putString("shopId", shopId);
            params.putString("type", type + "");//商品类型，1-餐饮；2-酒店
        } else {//修改商品信息
            url = Url.UPDATE_GOODS;
            params.putString("goodsId", dataBean.getId());
        }
        params.putString("token", XunGenApp.token);
        params.putString("name", name);
        params.putString("price", price);
        params.putString("cover", String.valueOf(imgs));
        params.putString("info", info);
        mHttp.post(url, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                finish();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 234 && resultCode == 611) {
            imgList.clear();
            imgList.addAll((List<String>) data.getSerializableExtra("imgList"));
            if (imgList.size() == 1) {
                mAdd_photo.setImageResource(R.mipmap.add_photo_1);
                mPhotos.setText("0");
            } else {
                String pathName = imgList.get(1);
                if (pathName.contains("https://")) {
                    Picasso.with(mContext)
                            .load(pathName)
                            .placeholder(R.mipmap.default_photo)
                            .error(R.mipmap.default_photo)
                            .into(mAdd_photo);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(pathName);
                    mAdd_photo.setImageBitmap(bitmap);
                }
                mPhotos.setText(imgList.size() - 1 + "");
            }

        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO:
                    //拍照的结果
                    imgList.add(PhotoUtils.photoPath);
                    if (imgList.size() > 1) {
                        /*Bitmap bitmap = BitmapFactory.decodeFile(imgList.get(1));
                        mAdd_photo.setImageBitmap(bitmap);*/
                        BitmapUtils.setImageView(mContext, mAdd_photo, imgList.get(1));
                        mPhotos.setText(imgList.size() - 1 + "");
                    }
                    break;
                case PhotoUtils.ACTIVITY_REQUEST_SELECT_PHOTO:
                    //相册选取的结果
                    List<String> pathList = (List<String>) data.getSerializableExtra("data");
                    imgList.addAll(pathList);
                    if (imgList.size() > 1) {
                       /* Bitmap bitmap = BitmapFactory.decodeFile(imgList.get(1));
                        mAdd_photo.setImageBitmap(bitmap);*/
                        BitmapUtils.setImageView(mContext, mAdd_photo, imgList.get(1));
                        mPhotos.setText(imgList.size() - 1 + "");
                    }
                    break;
            }

        }
    }
}
