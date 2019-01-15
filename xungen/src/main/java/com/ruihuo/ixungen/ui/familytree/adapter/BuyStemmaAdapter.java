package com.ruihuo.ixungen.ui.familytree.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.bean.PriceBean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/28
 * @describe May the Buddha bless bug-free!!!
 */
public class BuyStemmaAdapter extends BaseAdapter {
    private final int colorWhite;
    private final int colorBrown;
    List<PriceBean.DataBean> priceList;
    String id;
    Context mContext;

    public BuyStemmaAdapter(List<PriceBean.DataBean> priceList, Context mContext, String id) {
        this.priceList = priceList;
        this.id = id;
        this.mContext = mContext;
        colorWhite = mContext.getResources().getColor(R.color.white);
        colorBrown = mContext.getResources().getColor(R.color.brown_bg);
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
        convertView = View.inflate(parent.getContext(), R.layout.item_buy_gv, null);
        LinearLayout mLinearLayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
        TextView mMoney = (TextView) convertView.findViewById(R.id.money);
        TextView mTime = (TextView) convertView.findViewById(R.id.time);
        PriceBean.DataBean dataBean = priceList.get(position);
        String name = dataBean.getName();
        String price = dataBean.getPrice();
        int validity_period = dataBean.getValidity_period();
        mMoney.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price) + "元");
        mTime.setText(TextUtils.isEmpty(name) ? "--" : name);
        if (dataBean.getId().equals(id)) {
            mLinearLayout.setBackgroundResource(R.drawable.buy_select);
            mMoney.setTextColor(colorWhite);
            mTime.setTextColor(colorWhite);
        } else {
            mLinearLayout.setBackgroundResource(R.drawable.buy_unselect);
            mMoney.setTextColor(colorBrown);
            mTime.setTextColor(colorBrown);
        }
        return convertView;
    }
}
