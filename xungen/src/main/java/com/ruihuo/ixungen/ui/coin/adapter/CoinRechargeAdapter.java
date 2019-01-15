package com.ruihuo.ixungen.ui.coin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/29
 * @describe May the Buddha bless bug-free!!!
 */
public class CoinRechargeAdapter extends BaseAdapter {
    private final int colorWhite;
    private final int colorGreen;
    List<String> priceList;
    Context mContext;
    String id;

    public CoinRechargeAdapter(List<String> priceList, Context mContext, String id) {
        this.priceList = priceList;
        this.mContext = mContext;
        this.id = id;
        colorWhite = mContext.getResources().getColor(R.color.white);
        colorGreen = mContext.getResources().getColor(R.color.green_txt);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return priceList.size();
    }

    @Override
    public Object getItem(int position) {
        return priceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_coin_recharge, null);
        LinearLayout mLinearLayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
        TextView mMoney = (TextView) convertView.findViewById(R.id.money);
        TextView mTime = (TextView) convertView.findViewById(R.id.time);
        String string = priceList.get(position);
        mMoney.setText(string);
        mTime.setText("售价: ￥" + (position + 5));
        if (string.equals(id)) {
            mLinearLayout.setBackgroundResource(R.drawable.shape_btn_bg);
            mMoney.setTextColor(colorWhite);
            mTime.setTextColor(colorWhite);
        } else {
            mLinearLayout.setBackgroundResource(R.drawable.shape_green_box_1);
            mMoney.setTextColor(colorGreen);
            mTime.setTextColor(colorGreen);
        }
        return convertView;
    }
}
