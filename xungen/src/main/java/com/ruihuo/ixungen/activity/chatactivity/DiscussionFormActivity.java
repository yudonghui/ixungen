package com.ruihuo.ixungen.activity.chatactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.ListItemDecoration;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Discussion;

public class DiscussionFormActivity extends BaseNoTitleActivity {
    private com.ruihuo.ixungen.view.TitleBar mTitlBar;
    private SmartRefreshLayout mRefresh;
    private com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView mRecyclerView;
    private ImageView mNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_form);
        initView();
        addData();
        addListener();
    }

    private DiscussionFormAdapter mAdapter;
    private HttpInterface mHttp;

    private void initView() {
        mTitlBar = (com.ruihuo.ixungen.view.TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("讨论组");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView = (com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView) findViewById(R.id.recyclerView);
        mNoData = (ImageView) findViewById(R.id.no_data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ListItemDecoration(ContextCompat.getColor(mContext, R.color.gray_box)));
        mRecyclerView.setSwipeItemClickListener(mItemClickListener); // Item点击。
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mAdapter = new DiscussionFormAdapter(dataList, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mHttp = HttpUtilsManager.getInstance(mContext);
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dataList.clear();
                addData();
            }
        });
    }

    List<DiscussionFormBean.DataBean> dataList = new ArrayList<>();

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.DISCUSSION_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                Gson gson = GsonUtils.getGson();
                DiscussionFormBean discussionFormBean = gson.fromJson(result, DiscussionFormBean.class);
                dataList.clear();
                dataList.addAll(discussionFormBean.getData());
                if (dataList.size() > 0) {
                    mNoData.setVisibility(View.GONE);
                } else {
                    mNoData.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                if (dataList.size() > 0) {
                    mNoData.setVisibility(View.GONE);
                } else {
                    mNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            DiscussionFormBean.DataBean dataBean = dataList.get(position);
            final String group_id = dataBean.getGroup_id();
            RongIM.getInstance().getDiscussion(group_id, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {
                    RongIM.getInstance().startDiscussionChat(mContext, group_id, discussion.getName());
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    };
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            final int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("groupId", dataList.get(adapterPosition).getGroup_id());
                mHttp.post(Url.REMOVE_DISCUSSION, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        //dataList.clear();
                        dataList.remove(adapterPosition);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                //Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int i) {
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setBackgroundColor(Color.rgb(0xff, 0x2b, 0x30))
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(200)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };
}
