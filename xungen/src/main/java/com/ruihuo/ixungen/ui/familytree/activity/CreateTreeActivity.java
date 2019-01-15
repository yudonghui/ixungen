package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.login.SelectCityActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.ui.familytree.contract.CreateTreeContract;
import com.ruihuo.ixungen.ui.familytree.presenter.CreateTreePresenter;
import com.ruihuo.ixungen.utils.SelectorDialog;

import java.util.ArrayList;
import java.util.List;

import static com.ruihuo.ixungen.R.id.addressName;
import static com.ruihuo.ixungen.R.id.regionName;
import static com.ruihuo.ixungen.R.id.stemmaName;

public class CreateTreeActivity extends BaseActivity implements CreateTreeContract.View {
    CreateTreePresenter mPresenter = new CreateTreePresenter(this);
    private Context mContext;
    private EditText mStemmaName;
    private EditText mName;
    private TextView mSex;
    private EditText mGeneration;
    //private TextView mLookPrivate;
    private ImageView mIspublic;
    private TextView mAddressName;
    private TextView mRegionName;
    private TextView mCommit;
    private String sex = "1";
    private SelectorDialog selectorDialog;
    private List<String> dialogData;
    private InputMethodManager inputMethodManager;
    private String lookPrivate = "9";//0禁止访问,1自己可见,2家族可见.9完全开放,默认9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_create_tree);
        mContext = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("创建家谱");
        mStemmaName = (EditText) findViewById(stemmaName);
        mName = (EditText) findViewById(R.id.name);
        mSex = (TextView) findViewById(R.id.sex);
        mGeneration = (EditText) findViewById(R.id.generation);
        mIspublic = (ImageView) findViewById(R.id.ispublic);
        mAddressName = (TextView) findViewById(addressName);
        mRegionName = (TextView) findViewById(regionName);
        mCommit = (TextView) findViewById(R.id.commit);

        selectorDialog = new SelectorDialog(mContext);
        dialogData = new ArrayList<>();
    }

    private void addListener() {
        mSex.setOnClickListener(SexListener);
        mAddressName.setOnClickListener(AddressListener);//现居地
        mRegionName.setOnClickListener(RegionListener);//祖籍
        mCommit.setOnClickListener(CommitListener);
        mIspublic.setOnClickListener(LookPrivateListener);
    }

    View.OnClickListener SexListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogData.clear();
            dialogData.add("男");
            dialogData.add("女");
            dialogData.add("保密");
            selectorDialog.initData(dialogData);
            selectorDialog.show(R.layout.activity_tree_add);
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            selectorDialog.setListener(new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    setSex(message);
                    mSex.setText(TextUtils.isEmpty(message) ? "" : message);
                }

            });
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
    View.OnClickListener LookPrivateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("9".equals(lookPrivate)) {
                lookPrivate = "1";
                mIspublic.setImageResource(R.drawable.btn_off);
            } else {
                lookPrivate = "9";
                mIspublic.setImageResource(R.drawable.btn_on);
            }
        }
    };
    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String stemmaName = mStemmaName.getText().toString();
            String name = mName.getText().toString();
            String generation = mGeneration.getText().toString();

            Bundle parameters = new Bundle();
            parameters.putString("token", XunGenApp.token);
            if (TextUtils.isEmpty(stemmaName.replace(" ", ""))) {
                Toast.makeText(mContext, "家谱名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            parameters.putString("stemmaName", stemmaName);
            if (TextUtils.isEmpty(name.replace(" ", ""))) {
                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            parameters.putString("name", name);
            if (!TextUtils.isEmpty(sex))
                parameters.putString("sex", sex);
            if (!TextUtils.isEmpty(lookPrivate))
                parameters.putString("private", lookPrivate);
            if (!TextUtils.isEmpty(generation.replace(" ", "")))
                parameters.putString("generation", generation);

            if (!TextUtils.isEmpty(address_name))
                parameters.putString("address", address_name);
            if (!TextUtils.isEmpty(region_name))
                parameters.putString("region", region_name);
            mPresenter.getCommitData(parameters, mContext);
        }
    };

    private void setSex(String message) {
        switch (message) {
            case "男":
                sex = "1";
                break;
            case "女":
                sex = "2";
                break;
            case "保密":
                sex = "0";
                break;
        }
    }

    @Override
    public void getCommitSuccess(String result) {
        Intent intent = new Intent();
        setResult(2221, intent);
        finish();
    }

    @Override
    public void getCommitError(String error) {

    }

    private String address_name;
    private String region_name;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == 323) {
            //点击当前所在地，返回的值
            address_name = data.getStringExtra("cityName");
            mAddressName.setText(data.getStringExtra("cityName"));
        } else if (requestCode == 100 && resultCode == 323) {
            //点击籍贯所在地，返回的值
            region_name = data.getStringExtra("cityName");
            mRegionName.setText(data.getStringExtra("cityName"));
        }
    }
}
