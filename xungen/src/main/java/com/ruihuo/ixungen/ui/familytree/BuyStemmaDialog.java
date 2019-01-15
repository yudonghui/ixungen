package com.ruihuo.ixungen.ui.familytree;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.geninterface.OrderSuccessInterface;
import com.ruihuo.ixungen.ui.familytree.adapter.BuyStemmaAdapter;
import com.ruihuo.ixungen.ui.familytree.bean.PriceBean;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public class BuyStemmaDialog {
    private BuyStemmaAdapter buyStemmaAdapter;
    private Context mContext;
    private View mInflate;
    private GridView mGridView;
    private LinearLayout mLl_alipay;
    private ImageView mAlipay;
    private LinearLayout mLl_wxpay;
    private ImageView mWxpay;
    private TextView mConfirm;
    private Dialog mDialog;
    private Window window;

    private OrderSuccessInterface mOrderSuccess;

    public BuyStemmaDialog(Context mContext, OrderSuccessInterface mOrderSuccess) {
        this.mContext = mContext;
        this.mOrderSuccess = mOrderSuccess;
        //购买的dialog
        mInflate = View.inflate(mContext, R.layout.dialog_buy_stemma, null);
        mGridView = (GridView) mInflate.findViewById(R.id.gridView);
        mLl_alipay = (LinearLayout) mInflate.findViewById(R.id.ll_alipay);
        mAlipay = (ImageView) mInflate.findViewById(R.id.alipay);
        mLl_wxpay = (LinearLayout) mInflate.findViewById(R.id.ll_wxpay);
        mWxpay = (ImageView) mInflate.findViewById(R.id.wxpay);
        mConfirm = (TextView) mInflate.findViewById(R.id.confirm);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(mInflate);
        addListener();
    }

    private void addListener() {
        mLl_alipay.setOnClickListener(SelectorPayListener);
        mLl_wxpay.setOnClickListener(SelectorPayListener);
        mConfirm.setOnClickListener(ConfirmListener);
        mGridView.setOnItemClickListener(OnItemClickListener);
    }

    private int validity_period;//多少天的查看权限
    private String price;
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
    View.OnClickListener ConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            long consumeStartTime = System.currentTimeMillis();
            long consumeEndTime = consumeStartTime + validity_period * 86400000;
            final Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("type", "3");//订单类型：1-订餐；2-定酒店；3-家谱查询
            params.putString("goodsId", id);
            params.putString("shopId", "1");//店铺编号,家谱购买值为1
            params.putString("cost", price);
            params.putString("consumeStartTime", DateFormatUtils.longToDateS(consumeStartTime));
            params.putString("consumeEndTime", DateFormatUtils.longToDateS(consumeEndTime));
            //params.putString("consumeStartTime", "2017-10-29");
            // params.putString("consumeEndTime", "2017-10-30");
            params.putString("client", "1");
            params.putString("name", name);
            params.putString("price", price);//单价
            params.putString("amount", "1");//预定的商品数
            params.putString("info", name);
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            mHttp.post(Url.SHOP_PLACE_ORDER, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String orderNo = jsonObject.getString("data");//订单号
                        Log.e("订单号：", orderNo);
                        if (orderNo != null) {
                            mOrderSuccess.callBack(payMethod, price, orderNo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(String message) {

                }
            });
        }
    };
    AdapterView.OnItemClickListener OnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long ido) {
            PriceBean.DataBean dataBean = priceList.get(position);
            String id = dataBean.getId();
            buyStemmaAdapter.setId(id);
            buyStemmaAdapter.notifyDataSetChanged();
            price = dataBean.getPrice();
            validity_period = dataBean.getValidity_period();
        }
    };
    private String id;//家谱编号
    private String name;//家谱名字
    List<PriceBean.DataBean> priceList;

    public void showDialog(String id, String name, List<PriceBean.DataBean> priceList) {
        this.id = id;
        this.name = name;
        this.priceList = priceList;
        PriceBean.DataBean dataBean = priceList.get(0);//首次是第一个
        price = dataBean.getPrice();
        validity_period = dataBean.getValidity_period();
        buyStemmaAdapter = new BuyStemmaAdapter(priceList, mContext, dataBean.getId());
        mGridView.setAdapter(buyStemmaAdapter);
        mDialog.show();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
    }
}
