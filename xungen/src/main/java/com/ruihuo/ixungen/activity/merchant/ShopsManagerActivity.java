package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
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
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ShopsManagerActivity extends AppCompatActivity {
    private ImageView mImagBack;
    private SmartRefreshLayout mRefresh;
    private SwipeMenuRecyclerView mRecyclerView;
    private ShopsFormAdapter mAdapter;
    private List<GoodsFormBaseBean> dataList = new ArrayList<>();
    private Context mContext;
    private int type;
    private String shopId;
    private HttpInterface mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_manager);
        mContext = this;
        type = getIntent().getIntExtra("type", 0);
        shopId = getIntent().getStringExtra("shopId");
        initView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ListItemDecoration(ContextCompat.getColor(mContext, R.color.gray_box)));
        mRecyclerView.setSwipeItemClickListener(mItemClickListener); // Item点击。
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mAdapter = new ShopsFormAdapter(dataList, mContext, type);
        mRecyclerView.setAdapter(mAdapter);

        addData();
        addListener();
    }

    private void initView() {
        mImagBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recyclerView);
        mHttp = HttpUtilsManager.getInstance(mContext);
    }

    private void addListener() {
        mImagBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    /**
     * Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(mContext, HotelDetailActivity.class);
            Bundle bundle = new Bundle();
            GoodsFormBaseBean dataBean = dataList.get(position);
            bundle.putSerializable("dataBean", dataBean);
            bundle.putInt("type", type);
            bundle.putString("shopId", shopId);
            bundle.putInt("mode", 1);
            intent.putExtras(bundle);
            startActivityForResult(intent, 662);
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
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("shopId", shopId);
                params.putString("id", dataList.get(adapterPosition).getId());
                mHttp.post(Url.DELETE_SHOPS, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        dataList.clear();
                        addData();
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
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;

    private void addData() {
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("shopId", shopId);
        params.putString("type", type + "");
        mHttp.get(Url.SHOPS_FORM, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();

                Gson gson = GsonUtils.getGson();
                ShopsFormBean shopsFormBean = gson.fromJson(result, ShopsFormBean.class);
                totalPage = shopsFormBean.getTotalPage();
                dataList.addAll(shopsFormBean.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 662 && resultCode == 666) {
            dataList.clear();
            page = 1;
            addData();
        }
    }
}
