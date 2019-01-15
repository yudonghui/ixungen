package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class RecommendFoodActivity extends BaseNoTitleActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private HttpInterface mHttp;
    private LoadingDialogUtils loadingDialogUtils;
    private String shopId;
    private List<GoodsFormBaseBean> dataList = new ArrayList<>();
    private int verspace;
    private int left;
    private int resize;
    private RecommendFormAdapter mAdapter;
    private int mode;//1,推荐菜。2，我要推荐

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_food);
        mContext = this;
        shopId = getIntent().getStringExtra("shopId");
        mode = getIntent().getIntExtra("mode", 0);
        initView();
        mAdapter = new RecommendFormAdapter();
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        if (mode == 1) {
            mTitleBar.setTitle("推荐菜");
            mTitleBar.mTextRegister.setText("我要推荐");
            mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        } else mTitleBar.setTitle("我要推荐");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.listView);
        mHttp = HttpUtilsManager.getInstance(mContext);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth * 2 / 7;
        verspace = DisplayUtilX.dip2px(6);
        left = DisplayUtilX.dip2px(10);
    }

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(SendRecommendListener);
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

    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;

    private void addData() {
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("shopId", shopId);
        params.putString("type", 1 + "");
        mHttp.get(Url.SHOPS_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
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
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();

            }
        });
    }

    View.OnClickListener SendRecommendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    class RecommendFormAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_recommend_form, null);
                mViewHolder = new ViewHolder();
                mViewHolder.mFl_cover = (FrameLayout) convertView.findViewById(R.id.fl_cover);
                mViewHolder.mCover = (ImageView) convertView.findViewById(R.id.cover);
                mViewHolder.mRecommendNum = (TextView) convertView.findViewById(R.id.recommendNum);
                mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
                mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
                mViewHolder.mFl_order = (FrameLayout) convertView.findViewById(R.id.fl_order);
                mViewHolder.mOrder = (TextView) convertView.findViewById(R.id.order);
                mViewHolder.mPraise = (ImageView) convertView.findViewById(R.id.praise);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 11 / 15);
            layoutParams.topMargin = verspace;
            layoutParams.bottomMargin = verspace;
            layoutParams.leftMargin = left;
            mViewHolder.mFl_cover.setLayoutParams(layoutParams);
            GoodsFormBaseBean dataBean = dataList.get(position);
            String cover = dataBean.getCover();
            String price = dataBean.getPrice();
            String name = dataBean.getName();
            mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            mViewHolder.mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "--" : price));
            if (!TextUtils.isEmpty(cover)) {
                String[] split = cover.split("\\;");
                Picasso.with(mContext)
                        .load(split[0])
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.default_long)
                        .error(R.mipmap.default_long)
                        .into(mViewHolder.mCover);
            }
            if (mode == 1 && position < 3) {
                mViewHolder.mFl_order.setVisibility(View.VISIBLE);
                switch (position) {
                    case 0:
                        mViewHolder.mOrder.setText("1");
                        break;
                    case 1:
                        mViewHolder.mOrder.setText("2");
                        break;
                    case 2:
                        mViewHolder.mOrder.setText("3");
                        break;
                }
            } else mViewHolder.mFl_order.setVisibility(View.GONE);
            if (mode == 2) {
                mViewHolder.mPraise.setVisibility(View.VISIBLE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, HotelDetailActivity.class);
                    Bundle bundle = new Bundle();
                    GoodsFormBaseBean dataBean = dataList.get(position);
                    bundle.putSerializable("dataBean", dataBean);
                    bundle.putInt("type", 1);
                    bundle.putString("shopId", shopId);
                    bundle.putInt("mode", 2);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 662);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private FrameLayout mFl_cover;
            private ImageView mCover;
            private TextView mRecommendNum;
            private TextView mName;
            private TextView mPrice;
            private FrameLayout mFl_order;
            private TextView mOrder;
            private ImageView mPraise;//点赞
        }
    }
}
