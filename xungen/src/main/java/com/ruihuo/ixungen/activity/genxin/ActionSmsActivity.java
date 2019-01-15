package com.ruihuo.ixungen.activity.genxin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.ActionSmsAdapter;
import com.ruihuo.ixungen.entity.UserMsgBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/4/25
 * @ describe May the Buddha bless bug-free！！
 */
public class ActionSmsActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private ListView mListView;
    private ImageView mNoData;
    private ActionSmsAdapter mAdapter;
    private Context mContext;
    List<UserMsgBean.DataBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_sms);
        mContext = this;
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.actionsms_titlebar);
        mTitleBar.setTitle("活动消息");
        mListView = (ListView) findViewById(R.id.listview);
        mNoData = (ImageView) findViewById(R.id.no_data);
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        //0 系统消息，1 新朋友， 2 帖子消息 3 活动消息
        params.putString("type", "3");
        mHttp.get(Url.USERMSG_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                UserMsgBean userMsgBean = gson.fromJson(result, UserMsgBean.class);
                data = userMsgBean.getData();
                mAdapter = new ActionSmsAdapter(mContext, data);
                mListView.setAdapter(mAdapter);
                if (data.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                if (data.size() > 0) mNoData.setVisibility(View.GONE);
                else mNoData.setVisibility(View.VISIBLE);
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ActionSmsDetailActivity.class);
                intent.putExtra("relateTableId", data.get(position).getRelate_table_id());
                startActivity(intent);
            }
        });
    }
}
