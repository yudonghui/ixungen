package com.ruihuo.ixungen.activity.merchant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.PaymentActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import io.rong.imkit.RongIM;

/**
 * @author yudonghui
 * @date 2017/8/18
 * @describe May the Buddha bless bug-free!!!
 */
public class BottomSkip {
    private Context mContext;
    private DialogEditInterface mListener;

    public BottomSkip(Context mContext) {
        this.mContext = mContext;
    }

    public BottomSkip(Context mContext, DialogEditInterface mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }


    public void skip(final OrderBaseBean dataBean) {
        String text = dataBean.getText();
        String orderNo = dataBean.getOrder_no();
        final String mode = dataBean.getMode();
        switch (text) {
            case "取消订单":
                HintDialogUtils cancelDialog = new HintDialogUtils(mContext);
                cancelDialog.setMessage("您要取消订单?");
                cancelDialog.setTvCancel("取消");
                cancelDialog.setConfirm("确定", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {
                        cancelOrder(dataBean);
                    }
                });
                break;
            case "申请退款":
                Intent intent = new Intent(mContext, ApplyQuitOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", dataBean);
                intent.putExtras(bundle);
                ((Activity) mContext).startActivityForResult(intent, 233);
                break;
            case "删除订单":
                HintDialogUtils deleteDialog = new HintDialogUtils(mContext);
                deleteDialog.setMessage("您要删除订单?");
                deleteDialog.setTvCancel("取消");
                deleteDialog.setConfirm("确定", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {
                        deleteOrder(dataBean);
                    }
                });
                break;
            case "立即支付":
                payment(dataBean);
                break;
            case "立即使用":
                Intent intent1 = new Intent(mContext, OrderDetailActivity.class);
                intent1.putExtra("orderNo", orderNo);
                ((Activity) mContext).startActivityForResult(intent1, 233);
                break;
            case "立即评价":
                Intent intent2 = new Intent(mContext, EditorCommentActivity.class);
                intent2.putExtra("orderNo", orderNo);
                intent2.putExtra("shopId", dataBean.getShop_id());
                intent2.putExtra("cost", dataBean.getCost());
                intent2.putExtra("logo", dataBean.getLogo());
                intent2.putExtra("shopName", dataBean.getShop_name());
                ((Activity) mContext).startActivityForResult(intent2, 233);
                break;
            case "取消退款":
                cancelRefund(dataBean);
                break;
            case "投诉建议":
                RongIM.getInstance().startPrivateChat(mContext, ConstantNum.SERVICE, "寻根客服");
                break;
            case "再来一单":
                Intent intent6 = new Intent(mContext, HotelActivity.class);
                intent6.putExtra("shopId", dataBean.getShop_id());
                intent6.putExtra("type", Integer.parseInt(dataBean.getType()));
                mContext.startActivity(intent6);
                break;

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^下面是商户^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            case "回复评论":
                Intent intent3 = new Intent(mContext, ReplyRecommentActivity.class);
                intent3.putExtra("orderNo", orderNo);
                intent3.putExtra("comment_rid", dataBean.getComment_rid());
                ((Activity) mContext).startActivityForResult(intent3, 233);
                break;
            case "已使用":
                used(dataBean);
                break;
            case "拒单":
            case "拒绝退款":
            case "修改价钱":
                Intent intent5 = new Intent(mContext, RefundActivity.class);
                intent5.putExtra("text", text);
                intent5.putExtra("orderNo", orderNo);
                mContext.startActivity(intent5);
                break;
            case "同意退款":
                dealRefund(dataBean);
                break;
        }
    }

    private void deleteOrder(final OrderBaseBean dataBean) {
        if (dataBean == null) return;
        String order_no = dataBean.getOrder_no();
        String mode = dataBean.getMode();
        String type;
        if ("1".equals(mode)) {//店家删除
            type = "2";
        } else {//普通用户删除订单
            type = "1";
        }
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        params.putString("type", type); //1-消费者删除订单；2-商家删除订单
        mHttp.post(Url.DELETE_ORDER, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mListener.callBack(dataBean.getText());
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void cancelRefund(final OrderBaseBean dataBean) {
        String order_no = dataBean.getOrder_no();
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        mHttp.post(Url.CANCEL_REFUND, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mListener.callBack(dataBean.getText());
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void dealRefund(final OrderBaseBean dataBean) {
        String order_no = dataBean.getOrder_no();
        String cost = dataBean.getCost();
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        params.putString("amount", cost);
        params.putString("dealResult", "1"); //1-同意退款；2-拒绝退款
        mHttp.post(Url.DEAL_REFUND, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mListener.callBack(dataBean.getText());
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void cancelOrder(final OrderBaseBean dataBean) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        String order_no = dataBean.getOrder_no();
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        mHttp.post(Url.CANCEL_ORDER, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mListener.callBack(dataBean.getText());
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void payment(OrderBaseBean dataBean) {
        String amount = dataBean.getAmount();
        if (TextUtils.isEmpty(amount)) return;
        int num = (int) Float.parseFloat(amount);
        OrderData orderData = new OrderData();
        orderData.setType(Integer.parseInt(dataBean.getType()));
        orderData.setAllMoney(Float.parseFloat(dataBean.getCost()));
        orderData.setName(dataBean.getGoods_name());
        orderData.setPrice(dataBean.getPrice());
        String consume_start_time = dataBean.getConsume_start_time();
        String consume_end_time = dataBean.getConsume_end_time();
        long in = DateFormatUtils.StringToLong(consume_start_time);
        long out = DateFormatUtils.StringToLong(consume_end_time);
        int days = (int) ((out - in) / 86400000);
        String start = DateFormatUtils.longToDate(in);
        String end = DateFormatUtils.longToDate(out);
        orderData.setDays(days);
        orderData.setConsumeStartTime(start);
        orderData.setConsumeEndTime(end);
        orderData.setAmount(num + "");
        orderData.setCover(dataBean.getCover());
        orderData.setShopName(dataBean.getShop_name());
        orderData.setLogo(dataBean.getLogo());
        Intent intent = new Intent(mContext, PaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderData", orderData);
        bundle.putSerializable("orderNo", dataBean.getOrder_no());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    private void used(final OrderBaseBean dataBean) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        String order_no = dataBean.getOrder_no();
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", order_no);
        mHttp.post(Url.CONSUME_ORDER, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mListener.callBack(dataBean.getText());
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }
}
