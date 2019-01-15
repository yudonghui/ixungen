package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.geninterface.OrderInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/16
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderFormSFragment extends Fragment {
    private Context mContext;
    private View mInflate;

    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TextView mNoData;
    private int mode;
    private int type; //	0-普通消费者查看订单列表；1-店家查看订单列表
    private OrderFormSAdapter mAdapter;
    private LoadingDialogUtils loadingDialogUtils;

    public void setData(int type, int mode) {
        this.mode = mode;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_order_form, null);
        mContext = getContext();
        initView();
        mAdapter = new OrderFormSAdapter(dataList, type, mContext, OrderListener);
        mListView.setAdapter(mAdapter);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        dataList.clear();
        addData();
        addListener();
        return mInflate;
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) mInflate.findViewById(R.id.refresh);
        mListView = (ListView) mInflate.findViewById(R.id.listView);
        mNoData = (TextView) mInflate.findViewById(R.id.no_data);
        switch (mode) {

            case ConstantNum.ORDER_ALLS:
                status = "";
                break;
            case ConstantNum.ORDER_WAIT_PAYS://待付款
                status = "0";
                break;
            case ConstantNum.ORDER_WAIT_QUITS://退款中
                status = "3";
                break;
            case ConstantNum.ORDER_WAIT_REPLYS://待回复
                status = "8";
                break;
            case ConstantNum.ORDER_WAIT_USEDS://待使用
                status = "1";
                break;
            case ConstantNum.ORDER_FINISHEDS://消费完成
                status = "2";
                break;
        }

    }

    private void addListener() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                dataList.clear();
                addData();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    page++;
                    addData();
                } else {
                    mRefresh.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            mRefresh.finishLoadMore();
                        }
                    }, 1000);
                }
            }

        });

    }

    private int limit = 20;
    private int page = 1;
    private int totalPage;
    private String status = "0";
    List<OrderFormBean.DataBean> dataList = new ArrayList<>();

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        if (!TextUtils.isEmpty(status))
            params.putString("status", status);//0-未支付；1-支付完成
        params.putString("type", type + "");
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.ORDER_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                OrderFormBean orderFormBean = gson.fromJson(result, OrderFormBean.class);
                totalPage = orderFormBean.getTotalPage();
                dataList.addAll(orderFormBean.getData());
                if (dataList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                if (dataList.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    OrderInterface OrderListener = new OrderInterface() {
        @Override
        public void callBack(OrderFormBean.DataBean dataBean) {
            dataBean.setMode("1");
            BottomSkip bottomSkip = new BottomSkip(mContext, new DialogEditInterface() {
                @Override
                public void callBack(String message) {
                    if ("删除订单".equals(message) || "同意退款".equals(message) || "已使用".equals(message)) {
                        notifiData();
                    }
                }
            });
            bottomSkip.skip(dataBean);

        }
    };

    public void notifiData() {
        dataList.clear();
        page = 1;
        addData();
    }

}
