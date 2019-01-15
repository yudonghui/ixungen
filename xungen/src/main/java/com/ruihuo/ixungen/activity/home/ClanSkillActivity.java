package com.ruihuo.ixungen.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.activity.home.clanskill.CreatSkillActivity;
import com.ruihuo.ixungen.adapter.ClanSkillAdapter;
import com.ruihuo.ixungen.entity.ClanSkillBean;
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
 * @ date 2017/4/13
 * @ describe May the Buddha bless bug-free！！
 */
public class ClanSkillActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private Context mContext;
    private ClanSkillAdapter mAdapter;

    private int limit = 15;
    private int page = 1;
    private int totalPage = 1;
    List<ClanSkillBean.DataBean> skillList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_skill);
        mContext = this;
        initView();
        mAdapter = new ClanSkillAdapter(skillList, mContext);
        mListView.setAdapter(mAdapter);
        addListener();
        addData();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.clanskill_titlebar);
        mTitleBar.setTitle("能工巧匠");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.ptlv);
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mTitleBar.mTextRegister.setText("创建");
        mTitleBar.mTextRegister.setOnClickListener(CreatSkillListener);
    }

    private void addListener() {
        mTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                skillList.clear();
                page = 1;
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ClanSkillDetailActivity.class);
                ClanSkillBean.DataBean dataBean = skillList.get(position);
                intent.putExtra("skillName", dataBean.getTitle());
                intent.putExtra("info", dataBean.getContent());
                startActivity(intent);
            }
        });
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        //params.putString("id", skillId);
        // params.putString("areaIdStr", areaIdStr);
        // params.putString("surnameId", surnameId);
        // params.putString("associationId", associationId);
        // params.putString("status", status);
        mHttp.get(Url.CLANSKILL_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                ClanSkillBean clanSkillBean = gson.fromJson(result, ClanSkillBean.class);
                totalPage = clanSkillBean.getTotalPage();
                skillList.addAll(clanSkillBean.getData());
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

    View.OnClickListener CreatSkillListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ClanSkillActivity.this, CreatSkillActivity.class);
            startActivityForResult(intent, 620);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 620 && resultCode == 621) {
            skillList.clear();
            page = 1;
            addData();
        }
    }
}
