package com.ruihuo.ixungen.activity.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.adapter.FriendFormAdapter;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.SearchViewY;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/4/12
 * @ describe May the Buddha bless bug-free！！
 */
public class SearchFriendActivity extends BaseActivity {
    private SearchViewY mSearchView;
    private TextView mNoData;
    private ListView mListView;
    private Context mContext;
    private FriendFormAdapter mAdapter;
    List<FriendBean.DataBean> friendList = new ArrayList<>();
    List<FriendBean.DataBean> intentList = new ArrayList<>();
    private String searchContent;
    private InputMethodManager inputManageer;
    private int type;//0默认 添加好友搜索界面  1在已经有的好友里面搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_search_friend);
        inputManageer = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mContext = this;
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        intentList = (List<FriendBean.DataBean>) intent.getSerializableExtra("intentList");
        searchContent = intent.getStringExtra("searchContent");
        initView();
    }

    private void initView() {
        mSearchView = (SearchViewY) findViewById(R.id.searchview);
        mNoData = (TextView) findViewById(R.id.no_data);
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new FriendFormAdapter(friendList, "searchFriend");
        mListView.setAdapter(mAdapter);
        addListener();
        if (type == 0) {
            mTitleBar.setTitle("添加朋友");
            mSearchView.setHint("手机号/根号/昵称");
        } else {
            mTitleBar.setTitle("搜索");
            mSearchView.setHint("昵称");
            mSearchView.setText(TextUtils.isEmpty(searchContent) ? "" : searchContent);
        }
    }


    private void addListener() {
        mSearchView.setSearchViewListener(new SearchViewY.SearchViewListener() {
            @Override
            public void onDelete() {
                friendList.clear();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSearch(String text) {
                friendList.clear();
                searchContent = text;
                addData();
            }

            @Override
            public void onCancel() {
                friendList.clear();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChange(String text) {
                if (type == 1) {
                    searchContent = text;
                    addData();
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                FriendBean.DataBean dataBean = friendList.get(position);
                intent.putExtra("rid", dataBean.getRid());
                startActivity(intent);
            }
        });
    }

    private void addData() {
        friendList.clear();
        if (!TextUtils.isEmpty(searchContent)) {
            if (type == 0) {
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("userRid", searchContent);
                mHttp.get(Url.FIND_ALL_FRIEND_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = GsonUtils.getGson();
                        FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                        friendList.addAll(friendBean.getData());
                        if (friendList.size() == 0) {
                            mNoData.setVisibility(View.VISIBLE);
                        } else {
                            mNoData.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String message) {

                        if (friendList.size() == 0) {
                            mNoData.setVisibility(View.VISIBLE);
                        } else {
                            mNoData.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            } else {
                for (int i = 0; i < intentList.size(); i++) {
                    FriendBean.DataBean dataBean = intentList.get(i);
                    String nikename = dataBean.getNikename();
                    if (nikename.contains(searchContent)) {
                        friendList.add(dataBean);
                    }
                }
                if (friendList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else {
                    mNoData.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            }

        } else {
            mNoData.setVisibility(View.VISIBLE);
        }

    }
}
