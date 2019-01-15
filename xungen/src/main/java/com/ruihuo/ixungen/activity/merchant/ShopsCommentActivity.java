package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class ShopsCommentActivity extends AppCompatActivity {
    private Context mContext;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private TitleBar mTitleBar;
    private TextView mAllComment;
    private TextView mImgComment;
    private Drawable grayBg;
    private Drawable greenBg;
    private int grayTxt;
    private int whitTxt;
    private String imgFlag = "";//只要不为空就查带图片的评论，否则查所有
    private HttpInterface mHttp;
    private CommentAdapter mAdapter;
    private LoadingDialogUtils loadingDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_comment);
        mContext = this;
        shopId = getIntent().getStringExtra("shopId");
        grayBg = getResources().getDrawable(R.drawable.shape_gray_bg);
        greenBg = getResources().getDrawable(R.drawable.shape_green_bg);
        grayTxt = getResources().getColor(R.color.gray_txt);
        whitTxt = getResources().getColor(R.color.white);
        initView();
        mAdapter = new CommentAdapter(dataList, mContext, 2);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.commentTitlebar);
        mTitleBar.setTitle("评价");
        mListView = (ListView) findViewById(R.id.listView);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mAllComment = (TextView) findViewById(R.id.allComment);
        mImgComment = (TextView) findViewById(R.id.imgComment);
        mHttp = HttpUtilsManager.getInstance(mContext);
        loadingDialogUtils = new LoadingDialogUtils(mContext);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAllComment.setOnClickListener(SelectorListener);
        mImgComment.setOnClickListener(SelectorListener);
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

    private String shopId;
    private int limit = 15;
    private int page = 1;
    private int totalPage;
    List<CommentBaseBean> dataList = new ArrayList<>();

    private void addData() {
        Bundle params = new Bundle();
        params.putString("shopId", shopId);
        params.putString("imgFlag", imgFlag);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.SHOP_COMMENT_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                CommentBean commentBean = gson.fromJson(result, CommentBean.class);
                totalPage = commentBean.getTotalPage();
                dataList.addAll(commentBean.getData());
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

    View.OnClickListener SelectorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAllComment.setBackgroundDrawable(grayBg);
            mImgComment.setBackgroundDrawable(grayBg);
            mAllComment.setTextColor(grayTxt);
            mImgComment.setTextColor(grayTxt);
            switch (v.getId()) {
                case R.id.allComment:
                    mAllComment.setBackgroundDrawable(greenBg);
                    mAllComment.setTextColor(whitTxt);
                    imgFlag = "";
                    dataList.clear();
                    page = 1;
                    addData();
                    break;
                case R.id.imgComment:
                    mImgComment.setBackgroundDrawable(greenBg);
                    mImgComment.setTextColor(whitTxt);
                    imgFlag = "img";
                    dataList.clear();
                    page = 1;
                    addData();
                    break;
            }
        }
    };
}
