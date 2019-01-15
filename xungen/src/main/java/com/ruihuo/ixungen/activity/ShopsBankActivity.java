package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.utils.BankTypeDialog;
import com.ruihuo.ixungen.view.TitleBar;

public class
ShopsBankActivity extends AppCompatActivity {
    private TextView mBankType;
    private EditText mBankNum;
    private EditText mBankName;
    private EditText mBankBranch;
    private TitleBar mTitleBar;
    private TextView mNext;
    private Context mContext;
    private SharedPreferences sp;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_bank);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        sp = getSharedPreferences("merchantInfo", MODE_PRIVATE);
        mContext = this;
        initView();
        addListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.shopstitlebar);
        mTitleBar.setTitle("银行卡绑定");
        mBankType = (TextView) findViewById(R.id.bankType);
        mBankNum = (EditText) findViewById(R.id.bankNum);
        mBankName = (EditText) findViewById(R.id.bankName);
        mBankBranch = (EditText) findViewById(R.id.bankBranch);
        mNext = (TextView) findViewById(R.id.next);
        setView();
    }

    private void setView() {
        bankType = sp.getString("cardType", "0");
        if ("0".equals(bankType)) {
            mBankType.setText("对私账户");
        } else {
            mBankType.setText("对公账户");
        }
        String openBank = sp.getString("openBank", "");
        mBankName.setText(openBank);
        String branch = sp.getString("branch", "");
        mBankBranch.setText(branch);
        String bankCard = sp.getString("bankCard", "");
        mBankNum.setText(bankCard);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBankType.setOnClickListener(BankTypeListener);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bank = mBankType.getText().toString();//银行类型
                String bankNum = mBankNum.getText().toString();//银行卡号码
                String bankName = mBankName.getText().toString();//开户行名称
                String bankBranch = mBankBranch.getText().toString();//支行
                if (bank.equals("请选择银行卡类型")) {
                    Toast.makeText(mContext, "请选择银行卡类型", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(bankNum)) {
                    Toast.makeText(mContext, "请输入银行卡号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(bankName)) {
                    Toast.makeText(mContext, "请输入开户行名称", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(bankBranch)) {
                    Toast.makeText(mContext, "请输入支行名称", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("cardType", bankType);//银行卡类型，0-对私账户；1-对公账户
                    edit.putString("bankCard", bankNum);// 	银行卡号
                    edit.putString("openBank", bankName);//开户行
                    edit.putString("branch", bankBranch);//开户支行
                    edit.commit();
                    Intent intent = new Intent(mContext, ShopsInfoActivity.class);
                    startActivityForResult(intent, 401);
                }
            }
        });

    }

    private String bankType = "0";
    View.OnClickListener BankTypeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BankTypeDialog bankTypeDialog = new BankTypeDialog();
            bankTypeDialog.setDialog(mContext, bankType, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    bankType = message;
                    if (!TextUtils.isEmpty(bankType)) {
                        switch (bankType) {
                            case "0":
                                mBankType.setText("对私账户");
                                break;
                            case "1":
                                mBankType.setText("对公账户");
                                break;
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 401 && resultCode == 403) {//成功提交
            Intent intent = new Intent();
            setResult(403, intent);
            finish();
        }
    }
}
