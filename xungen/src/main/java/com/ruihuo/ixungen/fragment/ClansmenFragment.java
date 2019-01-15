package com.ruihuo.ixungen.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.chatactivity.FriendFormActivity;
import com.ruihuo.ixungen.activity.home.ClanBizActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationAdActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationFormActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationInfoActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationMemeberActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationPhotoActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationStatusActivity;
import com.ruihuo.ixungen.activity.useractivity.FamilyWorkActivity;
import com.ruihuo.ixungen.adapter.ClansmenAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.entity.ClansmenBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClansmenFragment extends Fragment {
    private View view;
    private Context mContext;
    private SmartRefreshLayout mRefresh;
    private ImageView mRight_icon;
    private ImageView mClansForm;
    private TextView mRight_status;
    private com.ruihuo.ixungen.view.CircleImageView mImage_header;
    private TextView mAgnation_name;
    private TextView mAgnation_introduce;
    private TextView mAgnation_detail;
    private GridView mGridView;
    private String[] mStrings = {"宗亲相册", "宗亲成员", "宗亲公告", "家规家训",
            "宗亲互助", "宗亲商业", "宗亲活动", "宗亲动态",
            "宗亲职务", "其他宗亲", "邀请成员", "退出宗亲会"};
    private int[] mResId = {R.mipmap.agnation_photo, R.mipmap.agnation_memeber, R.mipmap.agnation_ad,
            R.mipmap.agnation_rules, R.mipmap.agnation_helper, R.mipmap.agnation_shoping,
            R.mipmap.agnation_activity, R.mipmap.agnation_dynatice, R.mipmap.agnation_work,
            R.mipmap.agnation_other, R.mipmap.agnation_request, R.mipmap.agnation_quit};
    private int[] mTypes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    List<ClansmenBean> mList = new ArrayList<>();
    private AgnationFormBean.DataBean mDataBean;
    ClansmenAdapter clansmenAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clansmen, null);
        mContext = getContext();
        initView();
        clansmenAdapter = new ClansmenAdapter(mList);
        mGridView.setAdapter(clansmenAdapter);
        addListener();
        refreshData();
        return view;
    }

    private void initView() {
        mRefresh = view.findViewById(R.id.refresh);
        mClansForm = view.findViewById(R.id.clansForm);
        mRight_icon = (ImageView) view.findViewById(R.id.right_icon);
        mRight_status = view.findViewById(R.id.right_status);
        mImage_header = (com.ruihuo.ixungen.view.CircleImageView) view.findViewById(R.id.image_header);
        mAgnation_name = (TextView) view.findViewById(R.id.agnation_name);
        mAgnation_introduce = (TextView) view.findViewById(R.id.agnation_introduce);
        mAgnation_detail = (TextView) view.findViewById(R.id.agnation_detail);
        mGridView = (GridView) view.findViewById(R.id.gridView);
    }

    private String joinStatus = "-1";//-1：没有加入；0：待宗亲会会长审核；1：审核通过；2：审核不通过

    public void refreshData() {
        if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
            //  Toast.makeText(mContext, "没有宗亲会", Toast.LENGTH_SHORT).show();
            return;
        }
        final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        String[] split = XunGenApp.clanAssociationIds.split("\\,");
        if (split.length > 1) mClansForm.setVisibility(View.VISIBLE);
        else mClansForm.setVisibility(View.GONE);
        String id = split[0];
        params.putString("id", id);
        mHttp.get(Url.AGNATION_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AgnationFormBean agnationFormBean = gson.fromJson(result, AgnationFormBean.class);
                List<AgnationFormBean.DataBean> data = agnationFormBean.getData();
                mDataBean = data.get(0);
                if (mDataBean == null) {
                    mRefresh.finishRefresh();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("token", XunGenApp.token);
                bundle.putString("id", mDataBean.getId());
                mHttp.get(Url.AGNATION_ISJOIN, bundle, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        mRefresh.finishRefresh();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String data = jsonObject.getString("data");
                            joinStatus = data;
                            setView(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        mRefresh.finishRefresh();
                    }
                });
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
            }
        });

    }

    /**
     * @param status -1：没有加入；0：待宗亲会会长审核；1：审核通过；2：审核不通过
     */
    private void setView(String status) {
        if (mDataBean == null) return;
        String img_url = mDataBean.getImg_url();
        String name = mDataBean.getName();
        String area_id_str = mDataBean.getArea_id_str();
        String count = mDataBean.getCount();
        String create_time = mDataBean.getCreate_time();
        String date = DateFormatUtils.longToDateM(create_time);

        PicassoUtils.setImgView(img_url, mContext, R.mipmap.default_header_clan, mImage_header);
        mAgnation_name.setText(TextUtils.isEmpty(name) ? "" : name);
        mAgnation_detail.setText("地址：" + area_id_str + "·成员：" + count + "人·创建日期: " + date);
        setGridView(status);
    }

    private void setGridView(String status) {
        int totalNum = 0;
        if ("-1".equals(status) || "2".equals(status)) {
            totalNum = 10;
            mRight_icon.setVisibility(View.GONE);
            mRight_status.setVisibility(View.VISIBLE);
            mRight_status.setText("加入");
            mRight_status.setOnClickListener(JoinListener);
        } else if ("0".equals(status)) {
            totalNum = 10;
            mRight_icon.setVisibility(View.GONE);
            mRight_status.setVisibility(View.VISIBLE);
            mRight_status.setText("审核中");
        } else if ("1".equals(status)) {
            if (XunGenApp.rid.equals(mDataBean.getPresident_rid())) {//会长
                totalNum = 11;
            } else {//成员
                totalNum = 12;
            }
            mRight_icon.setVisibility(View.VISIBLE);
            mRight_status.setVisibility(View.GONE);
        }
        mList.clear();
        for (int i = 0; i < totalNum; i++) {
            ClansmenBean e = new ClansmenBean();
            e.setResId(mResId[i]);
            e.setTitle(mStrings[i]);
            e.setType(mTypes[i]);
            mList.add(e);
        }
        clansmenAdapter.notifyDataSetChanged();
    }

    private void addListener() {
        mImage_header.setOnClickListener(AgnationInfoListener);
        mRight_icon.setOnClickListener(SaoSaoListener);
        mGridView.setOnItemClickListener(OnItemClickListener);
        mClansForm.setOnClickListener(ClansFormListener);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });
    }

    View.OnClickListener AgnationInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mDataBean != null) {
                Intent intent = new Intent();
                intent.setClass(mContext, AgnationInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", mDataBean);
                bundle.putString("joinStatus", joinStatus);
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
            }

        }
    };
    View.OnClickListener ClansFormListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AgnationFormActivity.class);
            mContext.startActivity(intent);
        }
    };
    AdapterView.OnItemClickListener OnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
            if (mDataBean == null) return;
            ClansmenBean clansmenBean = mList.get(position);
            int type = clansmenBean.getType();
            Intent intent = new Intent();
            switch (type) {
                case 1://宗亲相册
                    intent.setClass(mContext, AgnationPhotoActivity.class);
                    intent.putExtra("presidentId", mDataBean.getPresident_rid());
                    intent.putExtra("associationId", mDataBean.getId());
                    startActivity(intent);
                    break;
                case 2://宗亲成员
                    intent.setClass(mContext, AgnationMemeberActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("associationId", mDataBean.getId());
                    bundle.putString("presidentId", mDataBean.getPresident_rid());
                    bundle.putString("from", "AgationActivity");
                    bundle.putSerializable("dataBean", mDataBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 3://宗亲公告
                    intent.setClass(mContext, AgnationAdActivity.class);
                    intent.putExtra("isPresident", (XunGenApp.rid).equals(mDataBean.getPresident_rid()));
                    //宗亲会id
                    intent.putExtra("associationId", mDataBean.getId());
                    startActivity(intent);
                    break;
                case 4://家规家训
                    IntentSkip intentSkip = new IntentSkip();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("associationId", mDataBean.getId());
                    intentSkip.skipType(mContext, ConstantNum.JGJX, bundle2);
                    break;
                case 5://宗亲互助
                    IntentSkip intentSkip2 = new IntentSkip();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("associationId", mDataBean.getId());
                    intentSkip2.skipType(mContext, ConstantNum.ZQHZ, bundle3);
                    break;
                case 6://宗亲商业
                    intent.setClass(mContext, ClanBizActivity.class);
                    startActivity(intent);
                    break;
                case 7://宗亲活动
                    IntentSkip intentSkip3 = new IntentSkip();
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("associationId", mDataBean.getId());
                    intentSkip3.skipType(mContext, ConstantNum.ZQHD, bundle4);
                    break;
                case 8://宗亲动态
                    intent.setClass(mContext, AgnationStatusActivity.class);
                    intent.putExtra("associationId", mDataBean.getId());
                    startActivity(intent);
                    break;
                case 9://宗亲职务
                    String president_rid = mDataBean.getPresident_rid();
                    if (president_rid.equals(XunGenApp.rid)) {
                        List<AgnationFormBean.DataBean.PresidentInfoBean> presidentInfo = mDataBean.getPresident_info();
                        List<AgnationFormBean.DataBean.VicePresidentRidBean> vice_president_rid = mDataBean.getVice_president_rid();
                        List<AgnationFormBean.DataBean.SecretaryGeneralRidBean> secretary_general_rid = mDataBean.getSecretary_general_rid();
                        List<AgnationFormBean.DataBean.AssistantSecretaryRidBean> assistant_secretary_rid = mDataBean.getAssistant_secretary_rid();
                        String presidentName = "";
                        String subPresidentName = "";
                        String secretaryName = "";
                        String subSecretaryName = "";
                        if (presidentInfo != null && presidentInfo.size() != 0) {
                            presidentName = presidentInfo.get(0).getNikename();
                        }
                        if (vice_president_rid != null && vice_president_rid.size() != 0) {
                            subPresidentName = vice_president_rid.get(0).getNikename();
                        }
                        if (secretary_general_rid != null && secretary_general_rid.size() != 0) {
                            secretaryName = secretary_general_rid.get(0).getNikename();
                        }
                        if (assistant_secretary_rid != null && assistant_secretary_rid.size() != 0) {
                            subSecretaryName = assistant_secretary_rid.get(0).getNikename();
                        }


                        intent.setClass(mContext, FamilyWorkActivity.class);
                        intent.putExtra("associationId", mDataBean.getId());
                        //会长根号
                        intent.putExtra("rid", mDataBean.getPresident_rid());
                        intent.putExtra("presidentName", TextUtils.isEmpty(presidentName) ? "" : presidentName);
                        intent.putExtra("subPresidentName", TextUtils.isEmpty(subPresidentName) ? "" : subPresidentName);
                        intent.putExtra("secretaryName", TextUtils.isEmpty(secretaryName) ? "" : secretaryName);
                        intent.putExtra("subSecretaryName", TextUtils.isEmpty(subSecretaryName) ? "" : subSecretaryName);
                        startActivity(intent);
                    }
                    break;
                case 10://其他宗亲
                    intent.setClass(mContext, AgnationFormActivity.class);
                    startActivity(intent);
                    break;
                case 11://邀请成员
                    intent.setClass(mContext, FriendFormActivity.class);
                    intent.putExtra("from", "inviteAgnation");
                    intent.putExtra("associationId", mDataBean.getId());
                    startActivity(intent);
                    break;
                case 12://退出宗亲
                    final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                    //退出宗亲会
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("id", mDataBean.getId());
                    mHttp.post(Url.QUIT_AGNATION_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                            joinStatus = "-1";
                            setGridView(joinStatus);
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                    break;

            }

        }
    };
    View.OnClickListener JoinListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("-1".equals(joinStatus) || "2".equals(joinStatus)) {//没有加入和审核不通过的时候。
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("associationId", mDataBean.getId());
                params.putString("userRid", XunGenApp.rid);
                mHttp.post(Url.JOIN_AGNATION_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        mRight_icon.setVisibility(View.GONE);
                        mRight_status.setVisibility(View.VISIBLE);
                        mRight_status.setText("审核中");
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
        }
    };
    View.OnClickListener SaoSaoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ClansmenFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ConstantNum.PERMISSION_CAMERA);
            } else {
                //调取二维码扫描
                Intent intent = new Intent(mContext, CaptureActivity.class);
                ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
            }
        }
    };

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
}
