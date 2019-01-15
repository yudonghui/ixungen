package com.ruihuo.ixungen.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.AddNewsActivity;
import com.ruihuo.ixungen.adapter.FriendStateAdapter;
import com.ruihuo.ixungen.entity.FriendStateBean;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.MyListView;
import com.ydh.refresh_layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/9
 * @describe May the Buddha bless bug-free!!!
 */
public class DynamicFragment extends Fragment {
    private Context mContext;
    private View mInflate;
    private MyListView mListView;
    private ImageView mNoData;
    private FriendStateAdapter mAdapter;
    private String rid;

    public void setData(String rid) {
        this.rid = rid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_dynamic, null);
        mContext = getContext();
        initView();
        mAdapter = new FriendStateAdapter(dataList, mContext, 2);
        mAdapter.setRid(rid);
        mAdapter.setDelete(DeleteListener);
        mListView.setAdapter(mAdapter);
        addData();
        return mInflate;
    }

    private void initView() {
        mListView = (MyListView) mInflate.findViewById(R.id.listView);
        mNoData = (ImageView) mInflate.findViewById(R.id.no_data);
    }

    CallBackInterface DeleteListener = new CallBackInterface() {
        @Override
        public void callBack() {
            setRefresh();
        }
    };

    public void setRefresh() {
        page = 1;
        dataList.clear();
        addData();
    }

    private List<FriendStateBean.DataBean> dataList = new ArrayList<>();
    private int page = 1;
    private int totalPage = 1;
    private int limit = 10;

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("type", "2");//0-查看所有动态，1-获取宗亲会动态；2-获取个人的发表的动态，默认0
        params.putString("rid", rid);
        mHttp.get(Url.FRIEND_STATUS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                FriendStateBean friendStateBean = gson.fromJson(result, FriendStateBean.class);
                if (friendStateBean.getCode() == 0) {
                    dataList.addAll(friendStateBean.getData());
                    totalPage = friendStateBean.getTotalPage();
                    mAdapter.notifyDataSetChanged();
                    if (dataList.size() == 0) {
                        mNoData.setVisibility(View.VISIBLE);
                    } else mNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                if (dataList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }
        });
    }

    public void setDownRefresh(SmartRefreshLayout mRefresh) {
        dataList.clear();
        page = 1;
        addDataRefresh(mRefresh);
    }

    public void setUpRefresh(final SmartRefreshLayout mRefresh) {
        if (page < totalPage) {
            page++;
            addDataRefresh(mRefresh);
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

    private void addDataRefresh(final SmartRefreshLayout mRefresh) {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("type", "2");//0-查看所有动态，1-获取宗亲会动态；2-获取自己发表的动态，默认0
        params.putString("rid", rid);
        mHttp.get(Url.FRIEND_STATUS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                FriendStateBean friendStateBean = gson.fromJson(result, FriendStateBean.class);
                if (friendStateBean.getCode() == 0) {
                    dataList.addAll(friendStateBean.getData());
                    totalPage = friendStateBean.getTotalPage();
                    mAdapter.notifyDataSetChanged();
                    if (dataList.size() == 0) {
                        mNoData.setVisibility(View.VISIBLE);
                    } else mNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                if (dataList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }
        });
    }

    public void setXuanfu(PopupWindowView popupWindowView, ImageButton mXuanfu) {
        popupWindowView.setOneText("发表动态", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                Intent intent = new Intent(mContext, AddNewsActivity.class);
                intent.putExtra("pid", "0");
                ((Activity) mContext).startActivityForResult(intent, 221);
            }
        });
        popupWindowView.showUp(mXuanfu);
    }
}
