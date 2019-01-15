package com.ruihuo.ixungen.activity.useractivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.chatactivity.FriendFormActivity;
import com.ruihuo.ixungen.activity.home.ClanBizActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/3/30
 * @ describe May the Buddha bless bug-free！！
 */
public class AgnationActivity extends AppCompatActivity {
    private ImageView mBack;
    private ImageView mHeader;
    private TextView mJoin;
    private ImageView mMore;
    private TextView mName;
    private TextView mIntroduce;
    private TextView mDetail;
    private LinearLayout mLlPhoto;
    private LinearLayout mLlMemeber;
    private LinearLayout mLlAd;
    private LinearLayout mLl_jgjx;
    private LinearLayout mLl_zqhz;
    private LinearLayout mLl_zqsy;
    private LinearLayout mLl_zqhd;
    private LinearLayout mLlAgnationStatus;
    private LinearLayout mLlOther;
    private LinearLayout mLlFamilyWork;
    private LinearLayout mLlPresident;
    private LinearLayout mLlSubPresident;
    private LinearLayout mLlSecretary;
    private LinearLayout mLlSubSecretary;
    private TextView mPresidentName;
    private TextView mSubPresidentName;
    private TextView mSecretatyName;
    private TextView mSubSecretatyName;

    private ImageView mPresidentAvatar;
    private ImageView mSubPresidentAvatar;
    private ImageView mSecretatyAvatar;
    private ImageView mSubSecretatyAvatar;

    private Intent intent;
    private Context mContext;
    //本宗亲会id
    private String id;
    private AgnationFormBean.DataBean dataBean;

    private View inflate;
    private PopupWindow mPopupWindow;
    private TextView mPopup_question;
    private View mFirstline;
    private TextView mPopup_smshint;
    private View mSecondline;
    private TextView mPopup_three;

    private String joinStatus = "-1";//-1：没有加入；0：待宗亲会会长审核；1：审核通过；2：审核不通过

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnation);
        mContext = this;
        Intent intent = getIntent();
        //本宗亲会id
        id = intent.getStringExtra("id");
        initView();
        // addData();
        addListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addData();
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mHeader = (ImageView) findViewById(R.id.image_header);
        mMore = (ImageView) findViewById(R.id.more);
        mJoin = (TextView) findViewById(R.id.join);

        mName = (TextView) findViewById(R.id.agnation_name);
        mIntroduce = (TextView) findViewById(R.id.agnation_introduce);
        mDetail = (TextView) findViewById(R.id.agnation_detail);
        mLlPhoto = (LinearLayout) findViewById(R.id.ll_photo);
        mLlMemeber = (LinearLayout) findViewById(R.id.ll_member);
        mLlAd = (LinearLayout) findViewById(R.id.ll_ad);
        mLl_jgjx = (LinearLayout) findViewById(R.id.ll_jgjx);
        mLl_zqhz = (LinearLayout) findViewById(R.id.ll_zqhz);
        mLl_zqsy = (LinearLayout) findViewById(R.id.ll_zqsy);
        mLl_zqhd = (LinearLayout) findViewById(R.id.ll_zqhd);
        mLlAgnationStatus = (LinearLayout) findViewById(R.id.ll_agnation_status);
        mLlOther = (LinearLayout) findViewById(R.id.ll_agnation_other);
        mLlFamilyWork = (LinearLayout) findViewById(R.id.ll_agnation_work);
        mLlPresident = (LinearLayout) findViewById(R.id.ll_president);
        mLlSubPresident = (LinearLayout) findViewById(R.id.ll_president2);
        mLlSecretary = (LinearLayout) findViewById(R.id.ll_secretary);
        mLlSubSecretary = (LinearLayout) findViewById(R.id.ll_secretary2);
        mPresidentName = (TextView) findViewById(R.id.president_name);
        mSubPresidentName = (TextView) findViewById(R.id.president_name2);
        mSecretatyName = (TextView) findViewById(R.id.secretary_name);
        mSubSecretatyName = (TextView) findViewById(R.id.secretary_name2);
        mPresidentAvatar = (ImageView) findViewById(R.id.president_header);
        mSubPresidentAvatar = (ImageView) findViewById(R.id.subpresident_header);
        mSecretatyAvatar = (ImageView) findViewById(R.id.secretary_header);
        mSubSecretatyAvatar = (ImageView) findViewById(R.id.subsecretary_header);


        inflate = View.inflate(mContext, R.layout.popup_window, null);

        mPopup_question = (TextView) inflate.findViewById(R.id.popup_question);
        mPopup_question.setVisibility(View.VISIBLE);
        mPopup_question.setText("邀请成员");
        mFirstline = (View) inflate.findViewById(R.id.firstline);
        mFirstline.setVisibility(View.VISIBLE);
        mPopup_smshint = (TextView) inflate.findViewById(R.id.popup_smshint);
        mPopup_smshint.setText("扫一扫");
        mPopup_smshint.setVisibility(View.VISIBLE);
        mPopup_three = (TextView) inflate.findViewById(R.id.popup_look);
        mPopup_three.setText("退出宗亲会");
        mPopup_three.setVisibility(View.VISIBLE);
        mSecondline = (View) inflate.findViewById(R.id.secondline);
        mSecondline.setVisibility(View.VISIBLE);

        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(DisplayUtilX.dip2px(150));
        mPopupWindow.setHeight(DisplayUtilX.dip2px(140));
        mPopupWindow.setContentView(inflate);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOnDismissListener(poponDismissListener);
    }

    private void addListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //加入、待审核
        mJoin.setOnClickListener(JoinListener);
        //更多
        mMore.setOnClickListener(MoreListener);
        //邀请成员
        mPopup_question.setOnClickListener(InviteListener);
        //扫一扫
        mPopup_smshint.setOnClickListener(SaoSaoListener);
        //退出宗亲会
        mPopup_three.setOnClickListener(QuitListener);
        //宗亲的详细信息
        mHeader.setOnClickListener(AgnationInfoListener);
        //宗亲相册
        mLlPhoto.setOnClickListener(PhotoListener);
        //宗亲成员
        mLlMemeber.setOnClickListener(MemeberListener);
        //宗亲公告
        mLlAd.setOnClickListener(AdListener);
        //家规家训
        mLl_jgjx.setOnClickListener(FamilyRuleListener);
        //宗亲互助
        mLl_zqhz.setOnClickListener(ZqhzListener);
        //宗亲商业
        mLl_zqsy.setOnClickListener(ZqsyListener);
        //宗亲活动
        mLl_zqhd.setOnClickListener(ActionListener);
        //宗亲动态
        mLlAgnationStatus.setOnClickListener(AgnationStatusListener);
        //其他宗亲
        mLlOther.setOnClickListener(AgnationOtherListener);
        //宗亲职务
        mLlFamilyWork.setOnClickListener(FamilyWorkListener);
    }

    PopupWindow.OnDismissListener poponDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 1f;
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    };
    View.OnClickListener MoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.showAsDropDown(mMore, -DisplayUtilX.dip2px(130), 0);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    };
    View.OnClickListener InviteListener = new View.OnClickListener() {//邀请成员
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            Intent intent = new Intent(mContext, FriendFormActivity.class);
            intent.putExtra("from", "inviteAgnation");
            intent.putExtra("associationId", id);
            startActivity(intent);
        }
    };
    View.OnClickListener SaoSaoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ConstantNum.PERMISSION_CAMERA);
            } else {
                //调取二维码扫描
                Intent intent = new Intent(mContext, CaptureActivity.class);
                ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
            }
        }
    };
    View.OnClickListener QuitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            //退出宗亲会
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("id", id);
            mHttp.post(Url.QUIT_AGNATION_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    joinStatus = "-1";
                    mJoin.setText("加入");
                    finish();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    };
    /**
     * 已加入
     * 待审核
     */
    View.OnClickListener JoinListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("-1".equals(joinStatus) || "2".equals(joinStatus)) {//没有加入和审核不通过的时候。
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("associationId", id);
                params.putString("userRid", XunGenApp.rid);
                mHttp.post(Url.JOIN_AGNATION_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        mJoin.setText("待会长审核");
                        joinStatus = "0";
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            } else {
                //待审核时候点击没有反应
            }
           /* if (isJoin) {
                //退出宗亲会
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("id", id);
                mHttp.post(Url.QUIT_AGNATION_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        mJoin.setText("加入");
                        isJoin = false;
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            } else {
                //加入宗亲会

            }*/

        }
    };
    View.OnClickListener FamilyRuleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) {
                return;
            }

            IntentSkip intentSkip = new IntentSkip();
            Bundle bundle = new Bundle();
            bundle.putString("associationId", dataBean.getId());
            intentSkip.skipType(mContext, ConstantNum.JGJX, bundle);
           /* IsJoinAgantion isJoinAgantion = new IsJoinAgantion(mContext);
            isJoinAgantion.isJoinHome(ConstantNum.JGJX);*/
        }
    };
    View.OnClickListener AgnationInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean != null) {
                intent = new Intent();
                intent.setClass(mContext, AgnationInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", dataBean);
                bundle.putString("joinStatus", joinStatus);
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
            }

        }
    };
    View.OnClickListener PhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) {
                return;
            }
            Intent intent = new Intent(mContext, AgnationPhotoActivity.class);
            intent.putExtra("presidentId", dataBean.getPresident_rid());
            intent.putExtra("associationId", id);
            startActivity(intent);
        }
    };
    View.OnClickListener MemeberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) {
                return;
            }
            Intent intent = new Intent(mContext, AgnationMemeberActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("associationId", id);
            bundle.putString("presidentId", dataBean.getPresident_rid());
            bundle.putString("from", "AgationActivity");
            bundle.putSerializable("dataBean", dataBean);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    View.OnClickListener FamilyWorkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean != null) {
                String president_rid = dataBean.getPresident_rid();
                if (president_rid.equals(XunGenApp.rid)) {
                    String presidentName = mPresidentName.getText().toString();
                    String subPresidentName = mSubPresidentName.getText().toString();
                    String secretaryName = mSecretatyName.getText().toString();
                    String subSecretaryName = mSubSecretatyName.getText().toString();

                    Intent intent = new Intent(mContext, FamilyWorkActivity.class);
                    intent.putExtra("associationId", id);
                    //会长根号
                    intent.putExtra("rid", dataBean.getPresident_rid());
                    intent.putExtra("presidentName", TextUtils.isEmpty(presidentName) ? "" : presidentName);
                    intent.putExtra("subPresidentName", TextUtils.isEmpty(subPresidentName) ? "" : subPresidentName);
                    intent.putExtra("secretaryName", TextUtils.isEmpty(secretaryName) ? "" : secretaryName);
                    intent.putExtra("subSecretaryName", TextUtils.isEmpty(subSecretaryName) ? "" : subSecretaryName);
                    startActivity(intent);
                }
            }
        }
    };
    View.OnClickListener AdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) {
                return;
            }
            intent = new Intent();
            intent.setClass(mContext, AgnationAdActivity.class);
            intent.putExtra("isPresident", (XunGenApp.rid).equals(dataBean.getPresident_rid()));
            //宗亲会id
            intent.putExtra("associationId", dataBean.getId());
            startActivity(intent);
        }
    };
    View.OnClickListener ZqhzListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentSkip intentSkip = new IntentSkip();
            Bundle bundle = new Bundle();
            bundle.putString("associationId", id);
            intentSkip.skipType(mContext, ConstantNum.ZQHZ, bundle);
        }
    };
    View.OnClickListener ZqsyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ClanBizActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener AgnationOtherListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AgnationFormActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener AgnationStatusListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AgnationStatusActivity.class);
            intent.putExtra("associationId",id);
            startActivity(intent);
        }
    };
    View.OnClickListener ActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) {
                return;
            }
            IntentSkip intentSkip = new IntentSkip();
            Bundle bundle = new Bundle();
            bundle.putString("associationId", dataBean.getId());
            intentSkip.skipType(mContext, ConstantNum.ZQHD, bundle);
        }
    };

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("id", id);
        mHttp.get(Url.AGNATION_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = new Gson();
                AgnationFormBean agnationFormBean = gson.fromJson(result, AgnationFormBean.class);
                List<AgnationFormBean.DataBean> data = agnationFormBean.getData();
                String status = data.get(0).getStatus();//0待审核 1审核通过  2审核不通过
                if ("1".equals(status)) {
                    dataBean = data.get(0);
                    setView();
                } else {
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    hintDialogUtils.setMessage("宗亲会正在审核中，请耐心等待！");
                    hintDialogUtils.setVisibilityCancel();
                    hintDialogUtils.setCancelable(false);
                    hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                        @Override
                        public void callBack(View view) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
        if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {//宗亲会Id为空，肯定没有加入
            joinStatus = "-1";
            joinStatus();
        } else {
            String[] split = XunGenApp.clanAssociationIds.split("\\,");
            for (int i = 0; i < split.length; i++) {
                if (id.equals(split[i])) {//如果包含id 这个时候再去判断呢自己是否加入这个宗亲会所处的状态
                    Bundle bundle = new Bundle();
                    bundle.putString("token", XunGenApp.token);
                    bundle.putString("id", id);
                    mHttp.get(Url.AGNATION_ISJOIN, bundle, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String data = jsonObject.getString("data");
                                if (data != null) joinStatus = data;
                                joinStatus();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String message) {

                        }
                    });
                }
            }
        }
    }

    private void joinStatus() {
        switch (joinStatus) {//-1：没有加入；0：待宗亲会会长审核；1：审核通过；2：审核不通过
            case "-1":
                mMore.setVisibility(View.GONE);
                mJoin.setVisibility(View.VISIBLE);
                mJoin.setText("加入");
                break;
            case "0":
                mMore.setVisibility(View.GONE);
                mJoin.setVisibility(View.VISIBLE);
                mJoin.setText("待会长审核");
                break;
            case "1":
                mMore.setVisibility(View.VISIBLE);
                mJoin.setVisibility(View.GONE);
                break;
            case "2":
                mMore.setVisibility(View.GONE);
                mJoin.setVisibility(View.VISIBLE);
                mJoin.setText("加入");
                break;
        }
    }

    private void setView() {
        if (dataBean == null) {
            return;
        }
        String president_rid = dataBean.getPresident_rid();
        /**
         *如果是本宗亲会的会长，那么右上角的加入按钮隐藏。
         * 如果你还没有加入任何的宗亲会，那么右上角的加入按钮显示。
         * 否则按钮隐藏
         */
      /*  if (XunGenApp.rid.equals(president_rid)) {
            mJoin.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
            mJoin.setVisibility(View.VISIBLE);
        } else mJoin.setVisibility(View.GONE);*/

        String img_url = dataBean.getImg_url();
        PicassoUtils.setImgView(img_url, mContext, R.mipmap.default_header_clan, mHeader);
        mName.setText(dataBean.getName());
        String create_time = dataBean.getCreate_time();
        String date = DateFormatUtils.longToDateM(create_time);
        mDetail.setText("地址：" + dataBean.getArea_id_str() + "·成员：" + dataBean.getCount() + "人·创建日期" + date);
        List<AgnationFormBean.DataBean.PresidentInfoBean> presidentInfo = dataBean.getPresident_info();
        List<AgnationFormBean.DataBean.VicePresidentRidBean> vice_president_rid = dataBean.getVice_president_rid();
        List<AgnationFormBean.DataBean.SecretaryGeneralRidBean> secretary_general_rid = dataBean.getSecretary_general_rid();
        List<AgnationFormBean.DataBean.AssistantSecretaryRidBean> assistant_secretary_rid = dataBean.getAssistant_secretary_rid();
        if (presidentInfo != null && presidentInfo.size() != 0) {
            mPresidentName.setText(presidentInfo.get(0).getNikename());
            String avatar = presidentInfo.get(0).getAvatar();
            PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mPresidentAvatar);
        }
        if (vice_president_rid != null && vice_president_rid.size() != 0) {
            mSubPresidentName.setText(vice_president_rid.get(0).getNikename());
            String avatar = vice_president_rid.get(0).getAvatar();
            PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mSubPresidentAvatar);
        }
        if (secretary_general_rid != null && secretary_general_rid.size() != 0) {
            mSecretatyName.setText(secretary_general_rid.get(0).getNikename());
            String avatar = secretary_general_rid.get(0).getAvatar();
            PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mSecretatyAvatar);
        }
        if (assistant_secretary_rid != null && assistant_secretary_rid.size() != 0) {
            mSubSecretatyName.setText(assistant_secretary_rid.get(0).getNikename());
            String avatar = assistant_secretary_rid.get(0).getAvatar();
            PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mSubSecretatyAvatar);
        }
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        try {
            switch (requestCode) {
                case ConstantNum.PERMISSION_CAMERA:
                    //调取扫描二维码界面
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 123) {
            //从宗亲信息界面退出宗亲的时候回来的
            if (data != null) {
                joinStatus = data.getStringExtra("joinStatus");
            }
        } else if (requestCode == 211 && resultCode == 311)

        {
            //从宗亲职务返回

        } else if (requestCode == ConstantNum.REQUEST_CAMERA)

        {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);//扫描出来的结果
                    NetWorkData netWorkData = new NetWorkData();
                    netWorkData.jieTwoCode(mContext, result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(mContext, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
