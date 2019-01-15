package com.ruihuo.ixungen.activity.useractivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.BaseActivity;

/**
 * @ author yudonghui
 * @ date 2017/4/7
 * @ describe May the Buddha bless bug-free！！
 */
public class IntroduceActivity extends BaseActivity {
    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_introduce);
        mEditText= (EditText) findViewById(R.id.et_introduce);
        mTitleBar.setTitle("宗亲简介");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitleBar.mTextRegister.setText("保存");
        mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String info= mEditText.getText().toString();

                Intent intent= new Intent();
                intent.putExtra("info",info);
                setResult(200,intent);
                finish();
            }
        });
    }
}
