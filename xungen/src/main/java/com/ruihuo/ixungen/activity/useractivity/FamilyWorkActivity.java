package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;

public class FamilyWorkActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private String associationId;
    private LinearLayout mLl_president;
    private TextView mText_president;
    private LinearLayout mLl_president2;
    private TextView mText_president2;
    private LinearLayout mLl_secretary;
    private TextView mText_secretary;
    private LinearLayout mLl_secretary2;
    private TextView mText_secretary2;
    private String presidentRid;
    private String presidentName;
    private String subPresidentName;
    private String secretaryName;
    private String subSecretaryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_family_work);
        mContext = this;
        Intent intent = getIntent();
        associationId = intent.getStringExtra("associationId");
        presidentRid = intent.getStringExtra("rid");
        presidentName = intent.getStringExtra("presidentName");
        subPresidentName = intent.getStringExtra("subPresidentName");
        secretaryName = intent.getStringExtra("secretaryName");
        subSecretaryName = intent.getStringExtra("subSecretaryName");
        initView();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("宗亲职务");
        mLl_president = (LinearLayout) findViewById(R.id.ll_president);
        mText_president = (TextView) findViewById(R.id.text_president);
        mLl_president2 = (LinearLayout) findViewById(R.id.ll_president2);
        mText_president2 = (TextView) findViewById(R.id.text_president2);
        mLl_secretary = (LinearLayout) findViewById(R.id.ll_secretary);
        mText_secretary = (TextView) findViewById(R.id.text_secretary);
        mLl_secretary2 = (LinearLayout) findViewById(R.id.ll_secretary2);
        mText_secretary2 = (TextView) findViewById(R.id.text_secretary2);
        mText_president.setText(presidentName);
        mText_president2.setText(subPresidentName);
        mText_secretary.setText(secretaryName);
        mText_secretary2.setText(subSecretaryName);
    }

    private void addListener() {
     /*   mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flag 如果为true，说明更改了职务信息，返回之后要进行数据的刷新
                if (flag){
                    Intent intent=new Intent();
                    setResult(311,intent);
                }
                finish();
            }
        });*/
        mLl_president.setOnClickListener(this);
        mLl_president2.setOnClickListener(this);
        mLl_secretary.setOnClickListener(this);
        mLl_secretary2.setOnClickListener(this);
    }

/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //flag 如果为true，说明更改了职务信息，返回之后要进行数据的刷新
            if (flag){
                Intent intent=new Intent();
                setResult(311,intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_president:
                Intent intent = new Intent(mContext, AgnationMemeberActivity.class);
                intent.putExtra("associationId", associationId);
                startActivityForResult(intent, 201);
                break;
            case R.id.ll_president2:
                Intent intent1 = new Intent(mContext, AgnationMemeberActivity.class);
                intent1.putExtra("associationId", associationId);
                startActivityForResult(intent1, 202);
                break;
            case R.id.ll_secretary:
                Intent intent2 = new Intent(mContext, AgnationMemeberActivity.class);
                intent2.putExtra("associationId", associationId);
                startActivityForResult(intent2, 203);
                break;
            case R.id.ll_secretary2:
                Intent intent3 = new Intent(mContext, AgnationMemeberActivity.class);
                intent3.putExtra("associationId", associationId);
                startActivityForResult(intent3, 204);
                break;
        }
    }
private boolean flag=false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            String rid = data.getStringExtra("rid");
            String name = data.getStringExtra("nikeName");
            if (requestCode == 201 && resultCode == 300) {
                addData(rid, name, 201);
            } else if (requestCode == 202 && resultCode == 300) {
                addData(rid, name, 202);
            } else if (requestCode == 203 && resultCode == 300) {
                addData(rid, name, 203);
            } else if (requestCode == 204 && resultCode == 300) {
                addData(rid, name, 204);
            }
        }
    }

    private void addData(String rid, final String name, final int ok) {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("id", associationId);
        switch (ok) {
            case 201:
                params.putString("presidentRid", rid);
                break;
            case 202:
                params.putString("vicePresidentRid", rid);
                break;
            case 203:
                params.putString("secretaryGeneralRid", rid);
                break;
            case 204:
                params.putString("assistantSecretaryRid", rid);
                break;
        }
        mHttp.post(Url.CHANG_AGNATION_INFO_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                flag=true;
                switch (ok) {
                    case 201:
                        mText_president.setText(name);
                        break;
                    case 202:
                        mText_president2.setText(name);
                        break;
                    case 203:
                        mText_secretary.setText(name);
                        break;
                    case 204:
                        mText_secretary2.setText(name);
                        break;

                }
            }

            @Override
            public void onError(String message) {
                flag=false;
            }
        });
    }
}
