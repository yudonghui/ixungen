package com.ruihuo.ixungen.activity.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.NewFriendAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.CallBackViewInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/4/12
 * @ describe May the Buddha bless bug-free！！
 */
public class NewFriendActivity extends AppCompatActivity {
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TitleBar mTitleBar;
    private Context mContext;
    private NewFriendAdapter mAdapter;
    //private SearchView mSearchView;
    private int limit = 20;
    private int page = 1;
    private int totalPage = 1;
    List<FriendBean.DataBean> friendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        mContext = this;
        initView();
        mAdapter = new NewFriendAdapter(friendList, mContext);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.newfriend_titlebar);
        mTitleBar.setTitle("新的朋友");
        mListView = (ListView) findViewById(R.id.ptlv);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        //关系状态，0-正常，1-拉黑
        params.putString("status", "0");
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.post(Url.NEW_FRIEND_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                totalPage = friendBean.getTotalPage();
                friendList.addAll(friendBean.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter.setIsAccept(new CallBackViewInterface() {
            @Override
            public void callBack(final TextView mTextView, int position) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("toUserRid", friendList.get(position).getRid());
                params.putString("dealResult", "1");
                mHttp.post(Url.DEAL_FRIEND_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        mTextView.setText("已添加");
                        mTextView.setTextColor(mContext.getResources().getColor(R.color.gray_txt));
                        mTextView.setBackgroundResource(R.drawable.shape_gray_box);
                        Intent intent = new Intent(ConstantNum.LOGIN_SUCCESS);
                        intent.putExtra("deleteFriend", true);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                friendList.clear();
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
}
