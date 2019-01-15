package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.ui.familytree.contract.EditorStemmaContract;
import com.ruihuo.ixungen.ui.familytree.presenter.EditorStemmaPresenter;

public class EditorStemmaActivity extends BaseActivity implements EditorStemmaContract.View {
    EditorStemmaPresenter mPresenter = new EditorStemmaPresenter(this);
    private Context mContext;
    private EditText mEditText;
    private TextView mConfirm;
    private int treeTitle;//1姓氏来源，2家规家训，3姓氏名人
    private String content;
    private String familyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_editor_stemma);
        mContext = this;
        treeTitle = getIntent().getIntExtra("treeTitle", -1);
        content = getIntent().getStringExtra("content");
        familyId = getIntent().getStringExtra("familyId");
        initView();
        addListener();
    }

    private void initView() {
        mEditText = (EditText) findViewById(R.id.edit);
        mConfirm = (TextView) findViewById(R.id.confirm);
        if (treeTitle == 1) {
            mTitleBar.setTitle("姓氏起源");
            mEditText.setHint("请输入姓氏起源");
        } else if (treeTitle == 2) {
            mTitleBar.setTitle("家规家训");
            mEditText.setHint("请输入家规家训");
        } else if (treeTitle == 3) {
            mTitleBar.setTitle("家族名人");
            mEditText.setHint("请输入家族名人");
        }
        mEditText.setText(TextUtils.isEmpty(content) ? "" : content);
    }

    private void addListener() {
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle parameters = new Bundle();
                parameters.putString("token", XunGenApp.token);
                parameters.putString("id", familyId);
                if (treeTitle == 1)
                    parameters.putString("surnameIntroduce", content);
                else if (treeTitle == 2)
                    parameters.putString("familyInstruction", content);
                else if (treeTitle == 3)
                    parameters.putString("familyCelebrity", content);
                mPresenter.commitEditor(parameters, mContext);
            }
        });
    }

    @Override
    public void getEditorSuccess(String result) {
        String content = mEditText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("content", content);
        setResult(9991, intent);
        finish();
    }

    @Override
    public void getEditorError(String error) {

    }
}
