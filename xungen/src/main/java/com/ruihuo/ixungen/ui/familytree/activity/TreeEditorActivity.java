package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.login.SelectCityActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.ui.familytree.bean.Tree;
import com.ruihuo.ixungen.ui.familytree.contract.TreeEditorContract;
import com.ruihuo.ixungen.ui.familytree.presenter.TreeEditorPresenter;
import com.ruihuo.ixungen.utils.GetPhotoUrl;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.SelectorDialog;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateInterface;
import com.ruihuo.ixungen.utils.wheelview.ChooseDateUtil;
import com.ruihuo.ixungen.view.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ruihuo.ixungen.R.id.avatar;

public class TreeEditorActivity extends BaseActivity implements TreeEditorContract.View {
    private TreeEditorPresenter mPresenter = new TreeEditorPresenter(this);
    private CircleImageView mAvatar;
    private EditText mName;
    private EditText mFormerName;
    private TextView mSex;
    private EditText mGeneration;
    private TextView mBirthday;
    private ImageView mInLife;
    private LinearLayout mLl_death;
    private TextView mDeathDate;
    private TextView mCausaMortis;
    private TextView mAddressName;
    private TextView mRegionName;
    private EditText mText;
    private ImageView mIsPublic;
    private TextView mCommit;
    private Context mContext;
    private Tree clickTree;
    private PhotoUtils photoUtils;
    private String avatarUrl;//头像链接
    private String sex = "1";
    private SelectorDialog selectorDialog;
    private List<String> dialogData;
    private String inLife = "1";//是否在世: 1:在世; 0:己逝世;2未知
    private String ispublic = "1";//是否公开（收费查看） 0表示不公开；1表示公开
    private String birth;//出生日期
    private String death;//逝世日期
    private String addressName;//现居地
    private String regionName;//祖籍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_tree_editor);
        mContext = this;
        clickTree = (Tree) getIntent().getSerializableExtra("clickTree");
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("修改信息");
        mAvatar = (CircleImageView) findViewById(avatar);
        mName = (EditText) findViewById(R.id.name);
        mFormerName = (EditText) findViewById(R.id.formerName);
        mSex = (TextView) findViewById(R.id.sex);
        mGeneration = (EditText) findViewById(R.id.generation);
        mBirthday = (TextView) findViewById(R.id.birthday);
        mInLife = (ImageView) findViewById(R.id.inLife);
        mLl_death = (LinearLayout) findViewById(R.id.ll_death);
        mDeathDate = (TextView) findViewById(R.id.deathDate);
        mCausaMortis = (TextView) findViewById(R.id.causaMortis);
        mAddressName = (TextView) findViewById(R.id.addressName);
        mRegionName = (TextView) findViewById(R.id.regionName);
        mText = (EditText) findViewById(R.id.text);
        mIsPublic = (ImageView) findViewById(R.id.ispublic);
        mCommit = (TextView) findViewById(R.id.commit);

        photoUtils = new PhotoUtils(mContext);
        selectorDialog = new SelectorDialog(mContext);
        dialogData = new ArrayList<>();
        setView();
    }

    private void setView() {
        if (clickTree != null) {
            String name = clickTree.getName();
            String generation = clickTree.getGeneration();
            avatarUrl = clickTree.getAvatar_url();
            sex = clickTree.getSex();
            mName.setText(TextUtils.isEmpty(name) ? "" : name);
            mGeneration.setText(TextUtils.isEmpty(generation) ? "" : generation);
            if ("1".equals(sex)) {
                mSex.setText("男");
            } else if ("2".equals(sex))
                mSex.setText("女");
            else mSex.setText("未知");
            PicassoUtils.setImgView(avatarUrl, R.drawable.family_header, mContext, mAvatar);
        }
    }

    private void addListener() {
        mAvatar.setOnClickListener(AvatarListener);
        mSex.setOnClickListener(SexListener);
        mBirthday.setOnClickListener(BirthdayListener);
        mInLife.setOnClickListener(InLifeListener);//是否逝世
        mDeathDate.setOnClickListener(DeathDateListener);//逝世日期
        mAddressName.setOnClickListener(AddressListener);//现居地
        mRegionName.setOnClickListener(RegionListener);//祖籍
        mIsPublic.setOnClickListener(IsPublicListener);//是否公开
        mCommit.setOnClickListener(CommitListener);
    }

    View.OnClickListener AvatarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            photoUtils.showDialog();
        }
    };
    View.OnClickListener SexListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogData.clear();
            dialogData.add("男");
            dialogData.add("女");
            dialogData.add("未知");
            selectorDialog.initData(dialogData);
            selectorDialog.show(R.layout.activity_tree_add);
            selectorDialog.setListener(new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    setSex(message);
                    mSex.setText(TextUtils.isEmpty(message) ? "" : message);
                }

            });
        }
    };
    View.OnClickListener BirthdayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseDateDialog(1);
        }
    };
    View.OnClickListener DeathDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseDateDialog(2);
        }
    };
    View.OnClickListener InLifeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("1".equals(inLife)) {
                inLife = "0";
                mInLife.setImageResource(R.drawable.btn_on);
                mLl_death.setVisibility(View.VISIBLE);
            } else {
                inLife = "1";
                mInLife.setImageResource(R.drawable.btn_off);
                mLl_death.setVisibility(View.GONE);
            }
        }
    };
    View.OnClickListener AddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 101);
        }
    };
    View.OnClickListener RegionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SelectCityActivity.class);
            startActivityForResult(intent, 100);
        }
    };
    View.OnClickListener IsPublicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("1".equals(ispublic)) {
                ispublic = "0";
                mIsPublic.setImageResource(R.drawable.btn_off);
            } else {
                ispublic = "1";
                mIsPublic.setImageResource(R.drawable.btn_on);
            }
        }
    };
    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String id = clickTree.getId();
            String name = mName.getText().toString();
            String formerName = mFormerName.getText().toString();
            String generation = mGeneration.getText().toString();
            String causaMortis = mCausaMortis.getText().toString();
            String text = mText.getText().toString();

            Bundle parameters = new Bundle();
            parameters.putString("token", XunGenApp.token);
            parameters.putString("id", id);
            if (!TextUtils.isEmpty(avatarUrl))
                parameters.putString("avatarUrl", avatarUrl);
            if (!TextUtils.isEmpty(name))
                parameters.putString("name", name);
            if (!TextUtils.isEmpty(formerName))
                parameters.putString("formerName", formerName);
            if (!TextUtils.isEmpty(sex))
                parameters.putString("sex", sex);
            if (!TextUtils.isEmpty(generation))
                parameters.putString("generation", generation);
            if (!TextUtils.isEmpty(birth))
                parameters.putString("birth", birth);
            if (!TextUtils.isEmpty(inLife)) {
                parameters.putString("inLife", inLife);
                if ("0".equals(inLife)) {
                    if (!TextUtils.isEmpty(death))
                        parameters.putString("death", death);
                    if (!TextUtils.isEmpty(causaMortis))
                        parameters.putString("causaMortis", causaMortis);
                }
            }
            if (!TextUtils.isEmpty(addressName))
                parameters.putString("addressName", addressName);
            if (!TextUtils.isEmpty(regionName))
                parameters.putString("regionName", regionName);
            if (!TextUtils.isEmpty(text))
                parameters.putString("text", text);
            if (!TextUtils.isEmpty(ispublic))
                parameters.putString("ispublic", ispublic);
            mPresenter.getCommitData(parameters, mContext);
        }
    };

    public void chooseDateDialog(int num) {//1,出生日期，2，逝世日期
        final ChooseDateUtil dateUtil = new ChooseDateUtil();
        if (num == 1) {
            if (TextUtils.isEmpty(birth)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                birth = dateFormat.format(new Date());
            }
            String[] split = birth.split("\\-");
            int[] oldDateArray = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
            dateUtil.createDialog(this, oldDateArray, new ChooseDateInterface() {
                @Override
                public void sure(int[] newDateArray) {
                    birth = newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2];
                    mBirthday.setText(birth);
                }
            });
        } else if (num == 2) {
            if (TextUtils.isEmpty(death)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                death = dateFormat.format(new Date());
            }
            String[] split = death.split("\\-");
            int[] oldDateArray = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
            dateUtil.createDialog(this, oldDateArray, new ChooseDateInterface() {
                @Override
                public void sure(int[] newDateArray) {
                    death = newDateArray[0] + "-" + newDateArray[1] + "-" + newDateArray[2];
                    mDeathDate.setText(death);
                }
            });
        }


    }

    private void setSex(String message) {
        switch (message) {
            case "男":
                sex = "1";
                break;
            case "女":
                sex = "2";
                break;
            case "未知":
                sex = "0";
                break;
        }
    }

    @Override
    public void getCommitSuccess(String result) {
        Intent intent = new Intent();
        setResult(223, intent);
        finish();
    }

    @Override
    public void getCommitError(String error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.REQUEST_CODE_TAKING_PHOTO && resultCode == RESULT_OK) {
            photoUtils.dealTakePhotoThenZoom();// 拍照的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL && resultCode == RESULT_OK) {
            photoUtils.dealChoosePhotoThenZoom(data);//选择图片的结果
        } else if (requestCode == PhotoUtils.REQUEST_CODE_CUT_PHOTO && resultCode == RESULT_OK) {
            // mPhotoUtils.dealZoomPhoto(mAvator, 1, "");// 剪裁图片的结果
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtils.IMAGE_DETAIL_PATH);
            mAvatar.setImageBitmap(bitmap);
            GetPhotoUrl getPhotoUrl = new GetPhotoUrl(mContext);
            getPhotoUrl.getPhotoUrl(PhotoUtils.IMAGE_DETAIL_PATH, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    loadingDialogUtils.setDimiss();
                    avatarUrl = message;
                }
            });
        } else if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            // addressName = data.getStringExtra("address");
            addressName = data.getStringExtra("cityName");
            mAddressName.setText(data.getStringExtra("cityName"));
        } else if (requestCode == 100 && resultCode == 323) {
            //点击籍贯所在地，返回的值
            regionName = data.getStringExtra("cityName");
            mRegionName.setText(data.getStringExtra("cityName"));
        }
    }
}
