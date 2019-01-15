package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionPhotoFormActivity extends AppCompatActivity {
    //  private List<ActionPhotoFormBean.DataBean> mList;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private TextView mCancel;
    private ImageView mNoData;
    private GridView mGridView;
    private SmartRefreshLayout mRefresh;
    private TextView mDeletePhoto;
    private int limit = 20;
    private int page = 1;
    private int totalPage = 1;
    private Context mContext;
    private String rid;
    List<ActionPhotoFormBean.DataBean> mList = new ArrayList<>();
    private ActionPhotoFormAdapter mAdapter;
    //存放点击选中的条目
    private HashMap<Integer, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_photo_form);
        mContext = this;
        rid = getIntent().getStringExtra("rid");
        initView();
        addData();
        mAdapter = new ActionPhotoFormAdapter(mList, mContext);
        mGridView.setAdapter(mAdapter);
        addListener();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mCancel = (TextView) findViewById(R.id.cancel);
        mNoData = (ImageView) findViewById(R.id.no_data);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mGridView = (GridView) findViewById(R.id.gridview);
        mDeletePhoto = (TextView) findViewById(R.id.delete_photo);
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", rid);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ACTION_PHOTO_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                ActionPhotoFormBean actionPhotoFormBean = gson.fromJson(result, ActionPhotoFormBean.class);
                List<ActionPhotoFormBean.DataBean> data = actionPhotoFormBean.getData();
                mList.addAll(data);
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
                mAdapter.setFlag(true);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                mNoData.setVisibility(View.VISIBLE);
            }
        });
    }


    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDeletePhoto.setOnClickListener(DeletePhotoListener);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mList.clear();
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
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view.findViewById(R.id.check);
                if (map.containsKey(position)) {
                    imageView.setImageResource(R.drawable.unselect);
                    map.remove(position);
                } else {
                    imageView.setImageResource(R.drawable.select);
                    map.put(position, mList.get(position).getId());
                }

            }
        });
    }

    View.OnClickListener DeletePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (map.size() == 0) {
                ToastUtils.toast(mContext, "未选中删除项");
            } else {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                StringBuilder id = new StringBuilder();
                int i = 0;
                //循环取出相册编号
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (i == 0) {
                        id.append(entry.getValue());
                    } else id.append("," + entry.getValue());
                    i++;
                }
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("id", String.valueOf(id));
                mHttp.post(Url.ACTION_DELETE_PHOTO, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        //成功的时候更新数据源
                        Intent intent = new Intent();
                        setResult(321, intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        }
    };
}
