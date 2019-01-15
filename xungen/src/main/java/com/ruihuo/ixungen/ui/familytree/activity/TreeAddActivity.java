package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.ui.familytree.bean.Tree;
import com.ruihuo.ixungen.ui.familytree.contract.TreeAddContract;
import com.ruihuo.ixungen.ui.familytree.presenter.TreeAddPresenter;
import com.ruihuo.ixungen.utils.SelectorDialog;

import java.util.ArrayList;
import java.util.List;

public class TreeAddActivity extends BaseActivity implements TreeAddContract.View {
    TreeAddPresenter mPresenter = new TreeAddPresenter(this);

    private LinearLayout mLl_relation;
    private TextView mRelation_title;
    private TextView mRelation;
    private EditText mName;
    private LinearLayout mLl_sex;
    private TextView mSex;
    private TextView mCommit;

    private String id;
    private String name;
    private Context mContext;

    private SelectorDialog selectorDialog;
    private List<String> dialogData;
    private InputMethodManager inputMethodManager;
    private Tree clickTree;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_tree_add);
        mContext = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Tree dataResure = (Tree) getIntent().getSerializableExtra("clickTree");
        initView(dataResure);
        addListener();
    }

    private void initView(Tree dataResure) {
        mTitleBar.setTitle("添加亲属");
        mLl_relation = (LinearLayout) findViewById(R.id.ll_relation);
        mRelation_title = (TextView) findViewById(R.id.relation_title);
        mRelation = (TextView) findViewById(R.id.relation);
        mName = (EditText) findViewById(R.id.name);
        mLl_sex = (LinearLayout) findViewById(R.id.ll_sex);
        mSex = (TextView) findViewById(R.id.sex);
        mCommit = (TextView) findViewById(R.id.commit);
        if (dataResure != null) {
            if (TextUtils.isEmpty(dataResure.getId()) || dataResure.getId().contains("-")) {//如果id为空的话说明点击的是一个虚拟的人。这个时候要以他的其中一个儿子作为基准。
                List<Tree> childs = dataResure.getChilds();
                if (childs == null) return;
                clickTree = childs.get(0);
                relation = "father";
                mRelation.setText("父亲");
            } else clickTree = dataResure;
            id = clickTree.getId();
            name = clickTree.getName();
            pid = clickTree.getPid();
        }
        mRelation_title.setText("与[" + (TextUtils.isEmpty(name) ? "--" : name) + "]的关系");

        selectorDialog = new SelectorDialog(mContext);
        dialogData = new ArrayList<>();
    }

    private void addListener() {
        mCommit.setOnClickListener(CommitListener);
        mLl_relation.setOnClickListener(RelationListener);//关系
        mLl_sex.setOnClickListener(SexListener);//性别
    }

    View.OnClickListener CommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = mName.getText().toString();
            if (TextUtils.isEmpty(relation)) {
                Toast.makeText(mContext, "关系不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(name)) {
                Toast.makeText(mContext, "关系不能为空", Toast.LENGTH_SHORT).show();
            } else {
                Bundle parameters = new Bundle();
                parameters.putString("token", XunGenApp.token);
                parameters.putString("id", id);
                parameters.putString("relation", relation);
                parameters.putString("name", name);
                parameters.putString("sex", sex);
                mPresenter.getCommitData(parameters, mContext);
            }
        }
    };
    private String relation;
    private String sex = "1";
    View.OnClickListener RelationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogData.clear();
            dialogData.add("兄弟姐妹");
            dialogData.add("子女");
            dialogData.add("父亲");
            dialogData.add("母亲");
            dialogData.add("配偶");
            if (pid != null && !"0".equals(pid) && !pid.startsWith("-"))//有父亲的时候不能再添加父亲
                dialogData.remove("父亲");
            selectorDialog.initData(dialogData);
            selectorDialog.show(R.layout.activity_tree_add);
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            selectorDialog.setListener(new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    setRelation(message);
                    mRelation.setText(TextUtils.isEmpty(message) ? "" : message);
                }
            });
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

    private void setRelation(String message) {

        switch (message) {
            case "父亲":
                relation = "father";
                sex = "1";
                mSex.setText("男");
                break;
            case "母亲":
                relation = "mother";
                sex = "2";
                mSex.setText("女");
                break;
            case "兄弟姐妹":
                relation = "brothers";
                break;
            case "子女":
                relation = "children";
                break;
            case "配偶":
                relation = "spouse";
                break;
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
        setResult(222, intent);
        finish();
    }

    @Override
    public void getCommitError(String error) {

    }
}
