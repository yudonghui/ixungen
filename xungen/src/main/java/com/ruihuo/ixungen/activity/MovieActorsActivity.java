package com.ruihuo.ixungen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.action.ActionUserInfoActivity;
import com.ruihuo.ixungen.entity.ActorBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
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
 * @ date 2017/7/19
 * @ describe May the Buddha bless bug-free！！
 */
public class MovieActorsActivity extends AppCompatActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private SmartRefreshLayout mRefresh;
    private GridView mGridView;
    private GridAdapter mAdapter;
    private LoadingDialogUtils loadingDialogUtils;
    private HttpInterface mHttp;
    private int width;
    private int height;
    private int radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_actors);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mContext = this;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        radius = DisplayUtilX.dip2px(5);
        int lineWidth = DisplayUtilX.dip2px(3);
        int i = DisplayUtilX.dip2px(40);
        width = (displayWidth - lineWidth - i) / 4;
        height = width * 18 / 15;
        initView();
        mAdapter = new GridAdapter();
        mGridView.setAdapter(mAdapter);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        mHttp = HttpUtilsManager.getInstance(mContext);

        addData();
        addListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mDataList.clear();
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
        mLl_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLl_photo.setVisibility(View.GONE);
            }
        });
    }

    private int limit = 20;
    private int page = 1;
    private int totalPage = 1;
    List<ActorBean.DataBean> mDataList = new ArrayList<>();

    private void addData() {
        mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ACTORS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                ActorBean actorBean = gson.fromJson(result, ActorBean.class);
                totalPage = actorBean.getTotalPage();
                mDataList.addAll(actorBean.getData());
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

    private ImageView mPhoto_actor;
    private LinearLayout mLl_photo;

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.movieTitleBar);
        mTitleBar.setTitle("影视演员库");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mGridView = (GridView) findViewById(R.id.gridView);
        mPhoto_actor = (ImageView) findViewById(R.id.photo_actor);
        mLl_photo = (LinearLayout) findViewById(R.id.ll_photo);
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_actor, null);
                mViewHolder = new ViewHolder();
                mViewHolder.mAvatar = (ImageView) convertView.findViewById(R.id.avatar);
                mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(mViewHolder);
            } else mViewHolder = (ViewHolder) convertView.getTag();
            final ActorBean.DataBean dataBean = mDataList.get(position);
            final String avatar = dataBean.getImg();
            String truename = dataBean.getTruename();
            mViewHolder.mName.setText(TextUtils.isEmpty(truename) ? "--" : truename);
          /*  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            mViewHolder.mAvatar.setLayoutParams(layoutParams);*/
            HttpUtils.display(avatar, mViewHolder.mAvatar, radius, width, height);
            mViewHolder.mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rid = dataBean.getRid();
                    Intent intent = new Intent(mContext, ActionUserInfoActivity.class);
                    intent.putExtra("rid", rid);
                    startActivity(intent);
                }
            });
            mViewHolder.mAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicassoUtils.setImgView(avatar, R.mipmap.default_photo, mContext, mPhoto_actor);
                    mLl_photo.setVisibility(View.VISIBLE);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView mAvatar;
        private TextView mName;
        private ImageView mCover;
    }
}
