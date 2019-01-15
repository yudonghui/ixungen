package com.ruihuo.ixungen.ui.coin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.coin.activity.CoinActivity;
import com.ruihuo.ixungen.ui.coin.activity.PaymentCoinActivity;
import com.ruihuo.ixungen.utils.Money;

/**
 * @author yudonghui
 * @date 2017/11/29
 * @describe May the Buddha bless bug-free!!!
 */
public class RewardDialog {
    private Context mContext;
    private Dialog mDialog;
    private RelativeLayout mRecharge;
    private TextView mRecharge_text;
    private View mRecharge_block;
    private RelativeLayout mDetail;
    private TextView mDetail_text;
    private View mDetail_block;
    private TextView mMoney;
    private EditText mCoinNum;
    private EditText mRemark;
    private LinearLayout mLinearlayout;
    private TextView mLeft;
    private TextView mRight;
    private TextView mCancel;
    private TextView mConfirm;

    public RewardDialog(Context mContext) {
        this.mContext = mContext;
        mDialog = new Dialog(mContext, R.style.HintDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_reward_coin, null);
        mRecharge = (RelativeLayout) view.findViewById(R.id.recharge);
        mRecharge_text = (TextView) view.findViewById(R.id.recharge_text);
        mRecharge_block = (View) view.findViewById(R.id.recharge_block);
        mDetail = (RelativeLayout) view.findViewById(R.id.detail);
        mDetail_text = (TextView) view.findViewById(R.id.detail_text);
        mDetail_block = (View) view.findViewById(R.id.detail_block);
        mMoney = (TextView) view.findViewById(R.id.money);
        mCoinNum = (EditText) view.findViewById(R.id.coinNum);
        mRemark = (EditText) view.findViewById(R.id.remark);
        mLinearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);
        mLeft = (TextView) view.findViewById(R.id.left);
        mRight = (TextView) view.findViewById(R.id.right);
        mCancel = (TextView) view.findViewById(R.id.cancel);
        mConfirm = (TextView) view.findViewById(R.id.confirm);
        addListener();
        mDialog.setContentView(view);
    }

    private void addListener() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRecharge.setOnClickListener(TabListener);
        mDetail.setOnClickListener(TabListener);
        mLinearlayout.setOnClickListener(SkipListener);
        mConfirm.setOnClickListener(ConfirmListener);
        Money.setPricePoint(mCoinNum);
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    View.OnClickListener SkipListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (tabFlag) {
                Intent intent = new Intent(mContext, CoinActivity.class);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, PaymentCoinActivity.class);
                mContext.startActivity(intent);
            }
        }
    };
    private boolean tabFlag = true;//true根币打赏，false现金打赏
    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recharge:
                    mRecharge_block.setVisibility(View.VISIBLE);
                    mRecharge_text.setTextColor(mContext.getResources().getColor(R.color.green_txt));
                    mDetail_block.setVisibility(View.INVISIBLE);
                    mDetail_text.setTextColor(mContext.getResources().getColor(R.color.deep_txt));
                    mCoinNum.setKeyListener(DigitsKeyListener.getInstance("0987654321"));
                    mMoney.setText("根币:");
                    mCoinNum.setHint("请输入您打赏根币的数量");
                    mCoinNum.setText("");
                    mLeft.setText("您当前剩余123枚根币");
                    mRight.setText("  充值>");
                    tabFlag = true;
                    break;
                case R.id.detail:
                    mRecharge_block.setVisibility(View.INVISIBLE);
                    mRecharge_text.setTextColor(mContext.getResources().getColor(R.color.deep_txt));
                    mDetail_block.setVisibility(View.VISIBLE);
                    mDetail_text.setTextColor(mContext.getResources().getColor(R.color.green_txt));
                    mCoinNum.setKeyListener(DigitsKeyListener.getInstance("0987654321."));
                    mMoney.setText("金额:");
                    mCoinNum.setHint("请输入您的打赏金额");
                    mCoinNum.setText("");
                    mLeft.setText("您当前使用的是微信付款");
                    mRight.setText("  选择>");
                    tabFlag = false;
                    break;
            }
        }
    };
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String money = mCoinNum.getText().toString();
            String remark = mRemark.getText().toString();
            if (TextUtils.isEmpty(money) || Float.parseFloat(money) == 0) {
                String toastText;
                if (tabFlag) {//根币打赏
                    toastText = "请输入打赏根币数量";
                } else {//现金打赏
                    toastText = "请输入打赏金额";
                }
                Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                return;
            }
            if (tabFlag) {//根币打赏

            } else {//现金打赏

            }
        }
    };
}
