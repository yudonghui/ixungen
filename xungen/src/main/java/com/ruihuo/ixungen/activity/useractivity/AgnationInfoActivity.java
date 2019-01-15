package com.ruihuo.ixungen.activity.useractivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.login.SelectCityActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/4/6
 * @ describe May the Buddha bless bug-free！！
 */
public class AgnationInfoActivity extends BaseActivity {
    private TextView mCreatTime;
    private TextView mAgnationNum;
    private ImageView mHeader;
    private LinearLayout mLlHeader;
    private TextView mAgnationName;
    private LinearLayout mLlAgnationName;
    private TextView mPhone;
    private LinearLayout mLlPhone;
    private ImageView mIsshowPhone;
    private LinearLayout mLlIsshowPhone;
    private TextView mAddress;
    private LinearLayout mLlAddress;
    private TextView mIntroduce;
    private TextView mQuit;
    private LinearLayout mLlCard;//宗亲会名片
    private AgnationFormBean.DataBean dataBean;
    private String joinStatus;
    private Context mContext;
    private PhotoUtils mPhotoUtils;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_agnation_info);
        mContext = this;
        Intent intent = getIntent();
        dataBean = (AgnationFormBean.DataBean) intent.getSerializableExtra("dataBean");
        joinStatus = intent.getStringExtra("joinStatus");
        from = intent.getStringExtra("from");
        initView();
        addListener();

    }


    private void initView() {
        mCreatTime = (TextView) findViewById(R.id.creattime);
        mAgnationNum = (TextView) findViewById(R.id.agnation_num);
        mHeader = (ImageView) findViewById(R.id.image_header);
        mLlHeader = (LinearLayout) findViewById(R.id.ll_header);
        mAgnationName = (TextView) findViewById(R.id.agnation_name);
        mLlAgnationName = (LinearLayout) findViewById(R.id.ll_agnation_name);
        mPhone = (TextView) findViewById(R.id.text_phone);
        mLlPhone = (LinearLayout) findViewById(R.id.ll_phone);
        mIsshowPhone = (ImageView) findViewById(R.id.image_isshow_phone);
        mLlIsshowPhone = (LinearLayout) findViewById(R.id.ll_isshow_phone);
        mLlAddress = (LinearLayout) findViewById(R.id.ll_address);
        mAddress = (TextView) findViewById(R.id.text_address);
        mIntroduce = (TextView) findViewById(R.id.agnation_introduce);
        mQuit = (TextView) findViewById(R.id.quit_agnation);
        mLlCard = (LinearLayout) findViewById(R.id.ll_card);
        mPhotoUtils = new PhotoUtils(mContext);
        if ("create".equals(from) || "createfinish".equals(from)) {
            mPhone.setText(XunGenApp.phone);
            //创建宗亲会
            mQuit.setText("完成");
            setGetFocus();
            mQuit.setOnClickListener(CreatAgnationListener);
            mCreatTime.setVisibility(View.GONE);
            mAgnationNum.setVisibility(View.GONE);
            mLlCard.setVisibility(View.GONE);
            mLlHeader.setVisibility(View.GONE);
            dataBean = new AgnationFormBean.DataBean();
            dataBean.setFlag("0");
        } else {
            mLlCard.setVisibility(View.VISIBLE);
            mLlHeader.setVisibility(View.VISIBLE);
            if ("1".equals(joinStatus)) mQuit.setVisibility(View.VISIBLE);
            else mQuit.setVisibility(View.GONE);
            /**
             *如果创建者rid和自己的rid相同就认为是会长具有编辑权利
             */
            if (dataBean.getPresident_rid().equals(XunGenApp.rid)) {
                mLlIsshowPhone.setVisibility(View.VISIBLE);
                mLlPhone.setVisibility(View.VISIBLE);
                if ("0".equals(dataBean.getFlag())) {
                    mPhone.setVisibility(View.VISIBLE);
                    mIsshowPhone.setImageResource(R.drawable.btn_on);
                } else {
                    mPhone.setVisibility(View.GONE);
                    mIsshowPhone.setImageResource(R.drawable.btn_off);
                }
                setGetFocus();//添加点击事件
            } else {
                //不是会长不显示手机号是否显示的选择按钮。同时判断会长是否公开了手机号。
                mLlIsshowPhone.setVisibility(View.GONE);
                if ("0".equals(dataBean.getFlag())) {
                    mLlPhone.setVisibility(View.VISIBLE);
                } else mLlPhone.setVisibility(View.GONE);
                setLoseFocus();//不添加点击事件
            }
            mQuit.setOnClickListener(QuitListener);
        }
        setView();
    }

    private void addListener() {
        mLlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBean == null) return;
                Intent intent = new Intent(mContext, TwoCodeActivity.class);
                intent.putExtra("city", dataBean.getArea_id_str());
                intent.putExtra("userName", dataBean.getName());
                intent.putExtra("avatar", dataBean.getImg_url());
                intent.putExtra("type", ConstantNum.AGNATION_TYPE);
                intent.putExtra("code", dataBean.getId());
                startActivity(intent);
            }
        });
    }

    private String count;
    private String phone;

    private void setView() {
        if (dataBean != null) {
            mCreatTime.setText("创建时间：" + DateFormatUtils.longToDateM(dataBean.getCreate_time()));

            String agnationName = dataBean.getName();
            mAgnationName.setText((TextUtils.isEmpty(agnationName) ? "" : agnationName));

            count = dataBean.getCount();
            mAgnationNum.setText("成员人数：" + (TextUtils.isEmpty(count) ? "" : count) + "人");

            List<AgnationFormBean.DataBean.PresidentInfoBean> president_rid = dataBean.getPresident_info();
            if (president_rid != null && president_rid.size() > 0) {
                phone = president_rid.get(0).getPhone();
                mPhone.setText(TextUtils.isEmpty(phone) ? "" : phone);
            }

            String address = dataBean.getArea_id_str();
            mAddress.setText(TextUtils.isEmpty(address) ? "" : address);

            String info = dataBean.getInfo();
            mIntroduce.setText(TextUtils.isEmpty(info) ? "" : info);

            String img_url = dataBean.getImg_url();
            PicassoUtils.setImgView(img_url, mContext, R.mipmap.default_header, mHeader);
        }
    }

    private void setGetFocus() {
        //修改头像
        mLlHeader.setOnClickListener(HeaderListener);
        //修改用户名
        mLlAgnationName.setOnClickListener(AgnationNameListener);
        //手机号
        mLlPhone.setOnClickListener(PhoneListener);
        //地址
        mLlAddress.setOnClickListener(AddressListener);
        //是否隐藏手机号
        mIsshowPhone.setOnClickListener(IsshowPhoneListener);
        //简介
        mIntroduce.setOnClickListener(IntroduceListener);
    }

    private void setLoseFocus() {
        //修改头像
        mLlHeader.setOnClickListener(null);
        //修改用户名
        mLlAgnationName.setOnClickListener(null);
        //手机号
        mLlPhone.setOnClickListener(null);
        //地址
        mLlAddress.setOnClickListener(null);
        //是否隐藏手机号
        mIsshowPhone.setOnClickListener(null);
        mIntroduce.setOnClickListener(null);
    }

    View.OnClickListener QuitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("id", dataBean.getId());
            mHttp.post(Url.QUIT_AGNATION_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    joinStatus = "-1";
                    Intent intent = new Intent();
                    intent.putExtra("joinStatus", joinStatus);
                    setResult(123, intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    View.OnClickListener CreatAgnationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(dataBean.getName())) {
                ToastUtils.toast(mContext, "宗亲昵称不能为空");
                return;
            }
            if (TextUtils.isEmpty(dataBean.getArea_id_str())) {
                ToastUtils.toast(mContext, "地址不能为空");
                return;
            }
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("name", dataBean.getName());
            params.putString("presidentRid", XunGenApp.rid);
            params.putString("areaIdStr", dataBean.getArea_id_str());
            params.putString("surname", XunGenApp.surname);
            params.putString("info", TextUtils.isEmpty(dataBean.getInfo()) ? "" : dataBean.getInfo());
            params.putString("flag", dataBean.getFlag() + "");
            mHttp.post(Url.CREATE_AGNATION_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String id = jsonObject.getString("data");
                        XunGenApp.clanAssociationIds = id;
                        if ("create".equals(from)) {
                            //Intent intent = new Intent(mContext, AgnationActivity.class);
                            Intent intent = new Intent(mContext, AgnationNewActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            setResult(ConstantNum.RESULT_2345, intent);
                        }
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    View.OnClickListener HeaderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null || TextUtils.isEmpty(dataBean.getId())) {
                ToastUtils.toast(mContext, "请先创建宗亲会才能设置头像");
                return;
            }
            mPhotoUtils.showDialog();
        }
    };
    View.OnClickListener AgnationNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
            editDialogUtils.setTitle("请输入宗亲会名字");
            editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        mAgnationName.setText(message);
                        dataBean.setName(message);
                        if (!"create".equals(from))//创建的时候是一起上传的
                            commitData();
                    }

                }
            });
        }
    };
    View.OnClickListener PhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener AddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 101);
        }
    };
    View.OnClickListener IsshowPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("0".equals(dataBean.getFlag())) {
                dataBean.setFlag("1");
                mPhone.setVisibility(View.GONE);
                mIsshowPhone.setImageResource(R.drawable.btn_off);
            } else {
                dataBean.setFlag("0");
                mPhone.setVisibility(View.VISIBLE);
                mIsshowPhone.setImageResource(R.drawable.btn_on);
            }
            if (!"create".equals(from))//创建的时候是一起上传的
                commitData();
        }
    };
    View.OnClickListener IntroduceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, IntroduceActivity.class);
            startActivityForResult(intent, 102);
        }
    };

    private void commitData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("id", TextUtils.isEmpty(dataBean.getId()) ? "" : dataBean.getId());
        params.putString("name", TextUtils.isEmpty(dataBean.getName()) ? "" : dataBean.getName());
        params.putString("presidentRid", XunGenApp.rid);
        params.putString("areaIdStr", TextUtils.isEmpty(dataBean.getArea_id_str()) ? "" : dataBean.getArea_id_str());
        params.putString("info", TextUtils.isEmpty(dataBean.getInfo()) ? "" : dataBean.getInfo());
        params.putString("flag", TextUtils.isEmpty(dataBean.getFlag()) ? "" : dataBean.getFlag());
        mHttp.post(Url.CHANG_AGNATION_INFO_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();

            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        try {
            switch (requestCode) {
                case PhotoUtils.PERMISSIONS_REQUEST_PHOTO: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (PhotoUtils.isTakePhoto) mPhotoUtils.takePhoto();
                        if (PhotoUtils.isGetPic) mPhotoUtils.getPictureFromLocal();
                    }
                }
                case PhotoUtils.PERMISSIONS_REQUEST_FILE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        mPhotoUtils.dealZoomPhoto(mHeader, 2, dataBean.getId());
                    break;
            }
        } catch (Exception e) {
            Log.e("", "Failed to request Permission" + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO: // 拍照的结果
                    mPhotoUtils.dealTakePhotoThenZoom();
                    break;
                case PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL://选择图片的结果
                    mPhotoUtils.dealChoosePhotoThenZoom(data);
                    break;
                case PhotoUtils.REQUEST_CODE_CUT_PHOTO: // 剪裁图片的结果
                    mPhotoUtils.dealZoomPhoto(mHeader, 2, dataBean.getId());
                    break;
            }
        } else if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            String address = data.getStringExtra("cityName");
            mAddress.setText(data.getStringExtra("cityName"));
            dataBean.setArea_id_str(address);
            if (!"create".equals(from))//创建的时候是一起上传的
                commitData();
        } else if (requestCode == 102 && resultCode == 200) {
            String info = data.getStringExtra("info");
            mIntroduce.setText(info);
            dataBean.setInfo(info);
            if (!"create".equals(from))//创建的时候是一起上传的
                commitData();
        }
    }
}
