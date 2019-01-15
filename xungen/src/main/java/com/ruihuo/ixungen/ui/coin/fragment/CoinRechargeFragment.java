package com.ruihuo.ixungen.ui.coin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.coin.adapter.CoinRechargeAdapter;
import com.ruihuo.ixungen.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/29
 * @describe May the Buddha bless bug-free!!!
 */
public class CoinRechargeFragment extends Fragment {
    private View mInflate;
    private Context mContext;
    private com.ruihuo.ixungen.view.MyGridView mGridView;
    private LinearLayout mLl_alipay;
    private ImageView mAlipay;
    private LinearLayout mLl_wxpay;
    private ImageView mWxpay;
    private TextView mConfirm;
    private CoinRechargeAdapter mRechargeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_coin_recharge, null);
        this.mContext = getContext();
        initView();
        addListener();
        addData();
        return mInflate;
    }

    private void initView() {
        mGridView = (MyGridView) mInflate.findViewById(R.id.gridView);
        mLl_alipay = (LinearLayout) mInflate.findViewById(R.id.ll_alipay);
        mAlipay = (ImageView) mInflate.findViewById(R.id.alipay);
        mLl_wxpay = (LinearLayout) mInflate.findViewById(R.id.ll_wxpay);
        mWxpay = (ImageView) mInflate.findViewById(R.id.wxpay);
        mConfirm = (TextView) mInflate.findViewById(R.id.confirm);
    }

    private void addListener() {
        mLl_alipay.setOnClickListener(SelectorPayListener);
        mLl_wxpay.setOnClickListener(SelectorPayListener);
        mGridView.setOnItemClickListener(OnItemClickListener);
    }

    private List<String> dataList = new ArrayList<>();

    private void addData() {
        for (int i = 0; i < 8; i++) {
            dataList.add((i + 2) * 10 + "枚");
        }
        mRechargeAdapter = new CoinRechargeAdapter(dataList, mContext, dataList.get(0));
        mGridView.setAdapter(mRechargeAdapter);
        //mRechargeAdapter.notifyDataSetChanged();
    }

    private int payMethod = 1;//1是微信支付。2是支付宝支付
    View.OnClickListener SelectorPayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_alipay:
                    mAlipay.setImageResource(R.mipmap.selected);
                    mWxpay.setImageResource(R.mipmap.unselected);
                    payMethod = 2;
                    break;
                case R.id.ll_wxpay:
                    mAlipay.setImageResource(R.mipmap.unselected);
                    mWxpay.setImageResource(R.mipmap.selected);
                    payMethod = 1;
                    break;
            }
        }
    };
    AdapterView.OnItemClickListener OnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long ido) {
            String string = dataList.get(position);
            mRechargeAdapter.setId(string);
            mRechargeAdapter.notifyDataSetChanged();
        }
    };
}
