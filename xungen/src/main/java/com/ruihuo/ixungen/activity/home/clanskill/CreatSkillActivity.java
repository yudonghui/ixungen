package com.ruihuo.ixungen.activity.home.clanskill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.ContentData;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PhotoUtilsX;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.ruihuo.ixungen.R.id.reward_img;

public class CreatSkillActivity extends AppCompatActivity {
    private LinearLayout mLlRoot;
    private TitleBar mTitleBar;
    private InterceptLinearLayout mLine_intercept;
    private FrameLayout mLl_cover;
    private ImageView mImage_cover;
    private ImageView mImage_cover1;
    private ImageView mImage_reward;
    private TextView mExchange;
    private EditText mSkill_title;
    private RichTextEditor mRichText;
    private LinearLayout mLlAddImg;
    private ImageView mAddImg;
    private LinearLayout mLl_reward;
    private EditText mIconNum;
    private TextView mExplain;
    private Context mContext;
    private FileUtils mFileUtils;
    private int displayWidth;
    private boolean isKeyBoardUp, isEditTouch;// 判断软键盘的显示与隐藏
    private PhotoUtils photoUtils;
    private String savePath;
    private String skillName;
    private LoadingDialogUtils loadingDialogUtils;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_creat_skill);
        mContext = this;
        from = getIntent().getStringExtra("from");
        displayWidth = DisplayUtilX.getDisplayWidth();
        initView();
        addListener();
    }

    private void initView() {
        mLlRoot = (LinearLayout) findViewById(R.id.activity_creat_skill);
        mTitleBar = (TitleBar) findViewById(R.id.titlebar_skill);
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitleBar.mTextRegister.setText("发布");
        mLine_intercept = (InterceptLinearLayout) findViewById(R.id.line_intercept);
        mLl_cover = (FrameLayout) findViewById(R.id.ll_cover);
        mImage_cover = (ImageView) findViewById(R.id.image_cover);
        mImage_cover1 = findViewById(R.id.image_cover1);
        mExchange = (TextView) findViewById(R.id.exchange);
        mSkill_title = (EditText) findViewById(R.id.skill_title);
        mRichText = (RichTextEditor) findViewById(R.id.richText);
        mLlAddImg = (LinearLayout) findViewById(R.id.line_addImg);
        mAddImg = (ImageView) findViewById(R.id.skill_addPicture);
        mImage_reward = (ImageView) findViewById(reward_img);
        mLl_reward = (LinearLayout) findViewById(R.id.ll_reward);
        mIconNum = (EditText) findViewById(R.id.iconNum);
        mExplain = (TextView) findViewById(R.id.explain);
        mFileUtils = new FileUtils(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayWidth, displayWidth / 2);
        mLl_cover.setLayoutParams(layoutParams);
        photoUtils = new PhotoUtils(mContext);
        if ("xgwz".equals(from)) {
            mLl_cover.setVisibility(View.GONE);
            mTitleBar.setTitle("寻根问祖");
        } else mTitleBar.setTitle("能工巧匠");
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSkill_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isEditTouch = false;
                    mLlAddImg.setVisibility(View.GONE);
                }
            }
        });
        mRichText.setLayoutClickListener(new RichTextEditor.LayoutClickListener() {
            @Override
            public void layoutClick() {
                isEditTouch = true;
                mLlAddImg.setVisibility(View.VISIBLE);
            }
        });
        mLlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = mLlRoot.getRootView()
                        .getHeight() - mLlRoot.getHeight();
                if (isEditTouch) {
                    if (heightDiff > 500) {// 大小超过500时，一般为显示虚拟键盘事件,此判断条件不唯一
                        isKeyBoardUp = true;
                        mLlAddImg.setVisibility(View.VISIBLE);
                    } else {
                        if (isKeyBoardUp) {
                            isKeyBoardUp = false;
                            isEditTouch = false;
                            rewardFlag = false;
                            mImage_reward.setImageResource(R.mipmap.reward_unselecte);
                            mLlAddImg.setVisibility(View.GONE);
                            mLl_reward.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        mAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.setMode(2);
                photoUtils.showDialog();
            }
        });
        //发布
        if ("xgwz".equals(from)) {
            mTitleBar.mTextRegister.setOnClickListener(SendXWListener);
        } else mTitleBar.mTextRegister.setOnClickListener(SendListener);

        //添加封面
        mImage_cover1.setOnClickListener(AddCoverListener);
        //换一张
        mExchange.setOnClickListener(AddCoverListener);
        //悬赏按钮
        mImage_reward.setOnClickListener(RewardListener);
    }

    private PhotoUtilsX photoUtilsX;
    View.OnClickListener AddCoverListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            photoUtilsX = new PhotoUtilsX(mContext);
            photoUtilsX.showDialog();
        }
    };
    private boolean rewardFlag = false;//false没显示下面根币，true显示
    View.OnClickListener RewardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (rewardFlag) {
                rewardFlag = false;
                mLl_reward.setVisibility(View.GONE);
                mImage_reward.setImageResource(R.mipmap.reward_unselecte);
            } else {
                rewardFlag = true;
                mLl_reward.setVisibility(View.VISIBLE);
                mImage_reward.setImageResource(R.mipmap.reward_selecte);
            }
        }
    };
    View.OnClickListener SendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            skillName = mSkill_title.getText().toString();
            if (TextUtils.isEmpty(skillName)) {
                ToastUtils.toast(mContext, "请输入标题");
                return;
            }
            if (TextUtils.isEmpty(savePath)) {
                ToastUtils.toast(mContext, "请设置一张封面图");
                return;
            }
            dataMap = mRichText.getRichEditData();
            loadingDialogUtils = new LoadingDialogUtils(mContext);
            if (dataMap.size() > 1) {
               /* for (Map.Entry<Integer, ContentData> entry : dataMap.entrySet()) {
                    ContentData contentData = entry.getValue();
                    Integer key = entry.getKey();
                    //获取图片链接
                    if (!TextUtils.isEmpty(contentData.imagePath)) {
                        getImgUrl(contentData, key);
                    }
                    Log.e(TextUtils.isEmpty(contentData.title) ? "" : contentData.title,
                            TextUtils.isEmpty(contentData.imagePath) ? "" : contentData.imagePath);
                }*/
                n = 0;
                getImgUrl();
            } else {
                fabu();
            }

        }
    };
    View.OnClickListener SendXWListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            skillName = mSkill_title.getText().toString();
            String s = skillName.replaceAll(" ", "");
            if (TextUtils.isEmpty(s)) {
                ToastUtils.toast(mContext, "请输入标题");
                return;
            }
            dataMap = mRichText.getRichEditData();
            loadingDialogUtils = new LoadingDialogUtils(mContext);
            if (dataMap.size() > 1) {
               /* for (Map.Entry<Integer, ContentData> entry : dataMap.entrySet()) {
                    ContentData contentData = entry.getValue();
                    Integer key = entry.getKey();
                    //获取图片链接
                    if (!TextUtils.isEmpty(contentData.imagePath)) {
                        getImgUrl(contentData, key);
                    }
                    Log.e(TextUtils.isEmpty(contentData.title) ? "" : contentData.title,
                            TextUtils.isEmpty(contentData.imagePath) ? "" : contentData.imagePath);
                }*/
                n = 0;
                getImgUrl();
            } else {
                XWfabu();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFileUtils.deleteRichTextImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantNum.PERMISSION_CAMERA:
                //相机请求回调
                photoUtilsX.takePhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO:
                    //拍照的结果
                    List<String> pathList1 = new ArrayList<>();
                    pathList1.add(PhotoUtils.photoPath);
                    for (int i = 0; i < pathList1.size(); i++) {
                        mRichText.insertImage(pathList1.get(i));
                    }
                    // photoUtils.uploadPhoto(pathList1, photoId);
                    break;
                case PhotoUtils.ACTIVITY_REQUEST_SELECT_PHOTO:
                    //相册选取的结果
                    List<String> pathList = (List<String>) data.getSerializableExtra("data");
                    // photoUtils.uploadPhoto(pathList, photoId);
                    for (int i = 0; i < pathList.size(); i++) {
                        mRichText.insertImage(pathList.get(i));
                    }
                    break;
                case ConstantNum.REQUEST_CAMERA:
                    //封面选择相机回调
                    photoUtilsX.dealTakePhotoThenZoom();
                    break;
                case ConstantNum.REQUEST_PHOTO:
                    //封面选择相册回调
                    photoUtilsX.dealChoosePhotoThenZoom(data);
                    break;
            }

        } else if (resultCode == ConstantNum.RESULT_DEAL_PHOTO) {
            savePath = data.getStringExtra("savePath");
            Bitmap bitmap = BitmapFactory.decodeFile(savePath);
            mImage_cover.setImageBitmap(bitmap);
            mImage_cover1.setVisibility(View.GONE);
            mExchange.setVisibility(View.VISIBLE);
        }
    }

    private int n = 0;
    private HashMap<Integer, ContentData> dataMap;

    private void getImgUrl() {//目前是单线程
        final ContentData contentData = dataMap.get(n);
        File file = new File(contentData.imagePath);
        Luban.get(mContext)
                .load(file)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        RequestParams entity = new RequestParams(Url.PHOTO_UPLOAD);
                        entity.addBodyParameter("token", XunGenApp.token);
                        entity.addBodyParameter("image", file);
                        x.http().post(entity, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("上传结果", result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String fileUrl = jsonObject.getString("fid");
                                    contentData.imgUrl = Url.PHOTO_URL + fileUrl;
                                    Log.e("图片网址", contentData.imgUrl + "是" + n);
                                    dataMap.put(n, contentData);
                                    n++;
                                    if (n >= dataMap.size() - 1) {
                                        if ("xgwz".equals(from))
                                            XWfabu();
                                        else
                                            fabu();
                                    } else getImgUrl();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                loadingDialogUtils.setDimiss();
                                Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
                                finish();
                                Log.e("返回错误", ex.getMessage());
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).launch();

    }

    private void fabu() {
        StringBuilder stringBuilder = new StringBuilder();
       /* for (Map.Entry<Integer, ContentData> entry : dataMap.entrySet()) {
            ContentData value = entry.getValue();
            value
            stringBuilder.append("<p>" + entry.getKey() + "</p>");
        }*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        stringBuilder.append("<h2  style=\"margin:10px 0px 5px\">" + skillName + "</h2><p style=\"margin:5px 0px 10px; color:#999999; font-size:12px\">发表于" + time + "</p>");
        for (int i = 0; i < dataMap.size(); i++) {
            ContentData contentData = dataMap.get(i);
            if (i == dataMap.size() - 1) {
                stringBuilder.append("<p style=\"margin:0px 0px 0px;text-align: justify;color:#333333;\">&nbsp;&nbsp;&nbsp;&nbsp;" + contentData.title + "</p>");
            } else
                stringBuilder.append("<p style=\"margin:0px 0px 0px;text-align: justify;color:#333333;\">&nbsp;&nbsp;&nbsp;&nbsp;" + contentData.title + "</p><img src=" + contentData.imgUrl + "/>");
        }
        RequestParams entity = new RequestParams(Url.SEND_SKILL);
        entity.addBodyParameter("token", XunGenApp.token);
        entity.addBodyParameter("image[]", new File(savePath));
        entity.addBodyParameter("content", String.valueOf(stringBuilder));
        entity.addBodyParameter("title", skillName);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("返回结果", result);
                loadingDialogUtils.setDimiss();
                Intent intent = new Intent();
                setResult(621, intent);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadingDialogUtils.setDimiss();
                Log.e("网络链接出错", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void XWfabu() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        StringBuilder contentImgs = new StringBuilder();
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String time = simpleDateFormat.format(new Date());
        // stringBuilder.append("<h2  style=\"margin:10px 0px 5px\">" + skillName + "</h2><p style=\"margin:5px 0px 10px; color:#999999; font-size:12px\">发表于" + time + "</p>");
        for (int i = 0; i < dataMap.size(); i++) {
            ContentData contentData = dataMap.get(i);
            Log.e("dataMap", contentData.imgUrl + "");
            if (i == dataMap.size() - 1) {
                if (!TextUtils.isEmpty(contentData.title))
                    summary.append(contentData.title);
                if (!TextUtils.isEmpty(contentData.imgUrl))
                    contentImgs.append(contentData.imgUrl);
                stringBuilder.append("<p style=\"margin:0px 0px 0px;text-align: justify;color:#333333;\">&nbsp;&nbsp;&nbsp;&nbsp;" + contentData.title + "</p>");
            } else {
                if (!TextUtils.isEmpty(contentData.title)) {
                    if (i == 0) summary.append(contentData.title);
                    else summary.append("\n" + contentData.title);
                }

                if (!TextUtils.isEmpty(contentData.imgUrl)) {
                    if (i == 0) contentImgs.append(contentData.imgUrl);
                    else contentImgs.append(";" + contentData.imgUrl);
                }


                stringBuilder.append("<p style=\"margin:0px 0px 0px;text-align: justify;color:#333333;\">&nbsp;&nbsp;&nbsp;&nbsp;" + contentData.title + "</p><img src=" + contentData.imgUrl + "/>");
            }
        }
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("bundle", "1");
        params.putString("type", "glean");
        params.putString("title", skillName);
        params.putString("summary", String.valueOf(summary));
        params.putString("content", String.valueOf(stringBuilder));
        params.putString("contentImgs", String.valueOf(contentImgs));
        mHttp.post(Url.SEND_TIEZI, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Intent intent = new Intent();
                setResult(621, intent);
                finish();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
