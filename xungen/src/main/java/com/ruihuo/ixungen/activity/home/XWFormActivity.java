package com.ruihuo.ixungen.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.genxin.PostsSmsActivity;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.activity.home.clanskill.CreatSkillActivity;
import com.ruihuo.ixungen.adapter.XWFormAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class XWFormActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mText_title;
    private ImageView mImage_edit;
    private ImageView mMore;
    private SearchViewY mSearchview;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private List<HotNewsEntity.DataBean> articleList = new ArrayList<>();
    private List<HotNewsEntity.DataBean> saveList = new ArrayList<>();
    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;
    private String title;//搜索的内容
    private XWFormAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwform);
        mContext = this;
        initView();
        mAdapter = new XWFormAdapter(articleList, mContext);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private View inflate;
    private TextView mPopup_question;
    private TextView mPopup_smshint;
    private PopupWindow mPopupWindow;

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mImage_edit = (ImageView) findViewById(R.id.image_edit);
        mMore = (ImageView) findViewById(R.id.more);
        mSearchview = (SearchViewY) findViewById(R.id.searchview);
        mListView = (ListView) findViewById(R.id.listView);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);

        inflate = View.inflate(mContext, R.layout.popup_window, null);
        mPopup_question = (TextView) inflate.findViewById(R.id.popup_question);
        mPopup_smshint = (TextView) inflate.findViewById(R.id.popup_smshint);
        mPopup_question.setVisibility(View.VISIBLE);
        mPopup_smshint.setVisibility(View.VISIBLE);
        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(DisplayUtilX.dip2px(150));
        mPopupWindow.setHeight(DisplayUtilX.dip2px(100));
        mPopupWindow.setContentView(inflate);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOnDismissListener(poponDismissListener);
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发布，
        mImage_edit.setOnClickListener(EditListener);
        //更多
        mMore.setOnClickListener(MoreListener);
        //搜索
        mSearchview.setSearchViewListener(SearchListener);
        //上下拉的刷新
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        /*//点击事件
        mListView.setOnItemClickListener(ItemClickListener);*/
        //我的提问，消息提示
        mPopup_question.setOnClickListener(TitleListener);
        mPopup_smshint.setOnClickListener(TitleListener);
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("type", "glean");
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("sort", "0");
        if (!TextUtils.isEmpty(title)) {
            params.putString("title", title);
        }
        mHttp.get(Url.NEWS_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                HotNewsEntity hotNewsEntity = gson.fromJson(result, HotNewsEntity.class);
                if (hotNewsEntity.getCode() == 0) {
                    articleList.addAll(hotNewsEntity.getData());
                    //当搜索框里面搜索的有内容的话，这个时候请求的数据不保存
                    if (TextUtils.isEmpty(title)) {
                        saveList.addAll(hotNewsEntity.getData());
                    }
                    totalPage = hotNewsEntity.getTotalPage();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
            }
        });
    }

    PopupWindow.OnDismissListener poponDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
        }
    };
    View.OnClickListener TitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popup_question://我的提问
                    Intent intent = new Intent(mContext, H5Activity.class);
                    intent.putExtra("from", ConstantNum.MYQUESTION);
                    startActivity(intent);
                    mPopupWindow.dismiss();
                    break;
                case R.id.popup_smshint://消息提示
                    Intent intent1 = new Intent(mContext, PostsSmsActivity.class);
                    startActivity(intent1);
                    mPopupWindow.dismiss();
                    break;
            }
        }
    };
    View.OnClickListener EditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CreatSkillActivity.class);
            intent.putExtra("from", "xgwz");
            startActivityForResult(intent, 200);
        }
    };
    View.OnClickListener MoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.showAsDropDown(mMore, -DisplayUtilX.dip2px(130), 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
        }
    };
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            title = text;
            page = 1;
            totalPage = 1;
            articleList.clear();
            addData();
        }

        @Override
        public void onCancel() {
            title = "";
            articleList.clear();
            articleList.addAll(saveList);
            //saveList.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChange(String text) {

        }
    };
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            articleList.clear();
            if (TextUtils.isEmpty(title)) {
                saveList.clear();
            }
            page = 1;
            addData();
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
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
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == 621) {
            articleList.clear();
            saveList.clear();
            page = 1;
            addData();
        }
    }
    /* AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };*/
}
