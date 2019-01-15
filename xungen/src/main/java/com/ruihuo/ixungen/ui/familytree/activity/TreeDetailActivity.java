package com.ruihuo.ixungen.ui.familytree.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.ui.familytree.bean.Tree;
import com.ruihuo.ixungen.ui.familytree.bean.TreeDetailBean;
import com.ruihuo.ixungen.ui.familytree.bean.TreeDetailSystemBean;
import com.ruihuo.ixungen.ui.familytree.contract.TreeDetailContract;
import com.ruihuo.ixungen.ui.familytree.presenter.TreeDetailPresenter;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;

public class TreeDetailActivity extends BaseNoTitleActivity implements TreeDetailContract.View {
    TreeDetailPresenter mPresenter = new TreeDetailPresenter(this);
    private TitleBar mTitlBar;
    private TextView mName;
    private TextView mSpouseName;
    private TextView mSpouseNameDetail;
    private TextView mSex;
    private TextView mGeneration;
    private TextView mPhone;
    private TextView mBirthday;
    private TextView mFromStemma;
    private TextView mRegionName;
    private TextView mIntroduce;
    private TextView mInvite;
    private ImageView mAvatar;
    private ImageView mAvatarBg;
    private String id;
    private boolean flag;//false 家谱，true族谱
    private String createRid;//创建者rid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_detail);
        id = getIntent().getStringExtra("id");
        flag = getIntent().getBooleanExtra("flag", false);
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("个人详情");
        mTitlBar.mShare.setImageResource(R.mipmap.icon_edit);
        mName = (TextView) findViewById(R.id.name);
        mSpouseName = (TextView) findViewById(R.id.spouseName);
        mSpouseNameDetail = (TextView) findViewById(R.id.spouseNameDetail);
        mSex = (TextView) findViewById(R.id.sex);
        mGeneration = (TextView) findViewById(R.id.generation);
        mPhone = (TextView) findViewById(R.id.phone);
        mBirthday = (TextView) findViewById(R.id.birthday);
        mFromStemma = (TextView) findViewById(R.id.fromStemma);
        mRegionName = (TextView) findViewById(R.id.regionName);
        mIntroduce = (TextView) findViewById(R.id.introduce);
        mInvite = (TextView) findViewById(R.id.invite);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mAvatarBg = (ImageView) findViewById(R.id.avatar_bg);
    }

    private void addData() {
        if (flag) {
            Bundle parameters = new Bundle();
            parameters.putString("id", id);
            parameters.putString("token", XunGenApp.token);
            parameters.putString("flagSelf", flag + "");
            mPresenter.getSystemInfoData(parameters, mContext);
        } else {
            Bundle parameters = new Bundle();
            parameters.putString("id", id);
            mPresenter.getInfoData(parameters, mContext);
        }
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitlBar.mShare.setOnClickListener(EditeInfoListener);
        mInvite.setOnClickListener(InviteListener);
        mSpouseNameDetail.setOnClickListener(SpousesListener);
    }

    View.OnClickListener SpousesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String id;
            String name;
            String spouse_id;
            int spouseNum = 0;
            if (flag) {
                if (systemData == null) return;
                spouse_id = systemData.getSpouse_id();
                TreeDetailSystemBean.DataBean.SpouseInfoBean spouse_info = systemData.getSpouse_info();
                if (spouse_info == null) {
                    Toast.makeText(mContext, "暂无配偶的相关信息", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    id = systemData.getId();
                    name = systemData.getName();
                }
            } else {
                if (appData == null) return;
                spouse_id = appData.getSpouse_id();
                TreeDetailBean.DataBean.SpouseInfoBean spouse_info = appData.getSpouse_info();
                if (spouse_info == null) {
                    Toast.makeText(mContext, "暂无配偶的相关信息", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    id = appData.getId();
                    name = appData.getName();
                }
            }
            if (spouse_id != null)
                spouseNum = spouse_id.split("\\,").length;
            if (spouseNum == 1) {
                Intent intent = new Intent(mContext, TreeDetailActivity.class);
                intent.putExtra("id", spouse_id);
                intent.putExtra("flag", flag);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mContext, SpouseFromActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("flag", flag);
                startActivity(intent);
            }
        }
    };
    View.OnClickListener EditeInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appData == null) return;
            Tree clickTree = new Tree();
            String id = appData.getId();
            String name = appData.getName();
            String avatar_url = appData.getAvatar_url();
            String generation = appData.getGeneration();
            String sex = appData.getSex();
            clickTree.setId(TextUtils.isEmpty(id) ? "" : id);
            clickTree.setName(TextUtils.isEmpty(name) ? "" : name);
            clickTree.setAvatar_url(TextUtils.isEmpty(avatar_url) ? "" : avatar_url);
            clickTree.setGeneration(TextUtils.isEmpty(generation) ? "" : generation);
            clickTree.setSex(TextUtils.isEmpty(sex) ? "" : sex);
            Intent intent = new Intent(mContext, TreeEditorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("clickTree", clickTree);
            intent.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intent, 112);
        }
    };
    View.OnClickListener InviteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, InviteActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    };
    private TreeDetailSystemBean.DataBean systemData;//族谱详情数据
    private TreeDetailBean.DataBean appData;//家谱详情数据

    @Override
    public void getSystemInfoSuccess(String result) {
        Gson gson = GsonUtils.getGson();
        TreeDetailSystemBean treeDetailSystemBean = gson.fromJson(result, TreeDetailSystemBean.class);
        systemData = treeDetailSystemBean.getData();
        setView();
    }

    private void setView() {
        String name;
        String spouse_name = "";
        String sex;
        String generation;
        String region_name;
        String birth;
        String avatar_url = "";
        String former_name;
        String text;
        if (flag) {
            if (systemData == null) return;
            name = systemData.getName();
            generation = systemData.getGeneration();
            sex = systemData.getSex();
            if (systemData.getSpouse_info() != null)
                spouse_name = systemData.getSpouse_info().getName();
            String year = systemData.getBirthday_year();
            String month = systemData.getBirthday_month();
            String day = systemData.getBirthday_day();
            birth = TextUtils.isEmpty(year) ? "0" : year + "-" + (TextUtils.isEmpty(month) ? "0" : month) + "-" + (TextUtils.isEmpty(day) ? "0" : day);
            region_name = systemData.getRegion();
            former_name = systemData.getFormer_name();
            text = systemData.getText();
            createRid = systemData.getCreate_rid();
        } else {
            if (appData == null) return;
            name = appData.getName();
            if (appData.getSpouse_info() != null)
                spouse_name = appData.getSpouse_info().getName();
            generation = appData.getGeneration();
            sex = appData.getSex();
            avatar_url = appData.getAvatar_url();
            birth = appData.getBirth();
            region_name = appData.getRegion_name();
            former_name = appData.getFormer_name();
            text = appData.getText();
            createRid = appData.getCreate_rid();
            if (XunGenApp.rid.equals(createRid)) mTitlBar.mShare.setVisibility(View.VISIBLE);
            else mTitlBar.mShare.setVisibility(View.GONE);
        }
        mName.setText(TextUtils.isEmpty(name) ? "--" : name);
        mSpouseName.setText("配偶: " + (TextUtils.isEmpty(spouse_name) ? "--" : spouse_name));
        switch (sex) {
            case "1":
                mSex.setText("性        别: 男");
                break;
            case "2":
                mSex.setText("性        别: 女");
                break;
            case "0":
                mSex.setText("性        别: 未知");
                break;

        }
        mGeneration.setText("辈        分: " + (TextUtils.isEmpty(generation) ? "--" : generation));
        mBirthday.setText("出生日期: " + (TextUtils.isEmpty(birth) ? "--" : birth));
        mFromStemma.setText("来自家谱: " + (TextUtils.isEmpty(former_name) ? "--" : former_name));
        if (flag) {
            NetWorkData netWorkData = new NetWorkData();
            netWorkData.getAddress(mContext, region_name, new NetWorkData.AddressInterface() {
                @Override
                public void callBack(String result) {
                    mRegionName.setText("出  生  地: " + (TextUtils.isEmpty(result) ? "--" : result));
                }
            });
        } else {
            mRegionName.setText("出  生  地: " + (TextUtils.isEmpty(region_name) ? "--" : region_name));
        }
        if (!TextUtils.isEmpty(text)) {
            mIntroduce.setText(Html.fromHtml(text));
        } else
            mIntroduce.setText("");
        if (!TextUtils.isEmpty(avatar_url)) {
            mAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(mContext)
                    .load(avatar_url)
                    .placeholder(R.mipmap.default_header_clan)
                    .error(R.mipmap.default_header_clan)
                    .into(mAvatar);
        }
    }

    @Override
    public void getSystemInfoError(String error) {

    }

    @Override
    public void getInfoSuccess(String result) {
        Gson gson = GsonUtils.getGson();
        TreeDetailBean treeDetailBean = gson.fromJson(result, TreeDetailBean.class);
        appData = treeDetailBean.getData();
        setView();
    }

    @Override
    public void getInfoError(String error) {

    }
}
