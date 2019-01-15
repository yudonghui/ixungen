package com.ruihuo.ixungen.activity.genxin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.ClanGleanActivity;
import com.ruihuo.ixungen.activity.WebViewActivity;
import com.ruihuo.ixungen.activity.merchant.OrderDetailActivity;
import com.ruihuo.ixungen.activity.merchant.UnusedOrderActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationInviteActivity;
import com.ruihuo.ixungen.adapter.SystemSmsAdapter;
import com.ruihuo.ixungen.entity.UserMsgBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author yudonghui
 * @ date 2017/4/13
 * @ describe May the Buddha bless bug-free！！
 */
public class SystemSmsActivity extends BaseActivity {
    private ListView mListView;
    private ImageView mNoData;
    private Context mContext;
    private SystemSmsAdapter mAdapter;
    private List<UserMsgBean.DataBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_system_sms);
        mContext = this;
        initView();
        mAdapter = new SystemSmsAdapter(mContext, data);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mTitleBar.setTitle("系统消息");
        mListView = (ListView) findViewById(R.id.listview);
        mNoData = (ImageView) findViewById(R.id.no_data);

    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("type", "0");
        mHttp.get(Url.USERMSG_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                UserMsgBean userMsgBean = gson.fromJson(result, UserMsgBean.class);
                data.addAll(userMsgBean.getData());
                mAdapter.notifyDataSetChanged();
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

                UserMsgBean.DataBean dataBean = data.get(position);
                String relate_table = dataBean.getRelate_table();
                String relate_table_id = dataBean.getRelate_table_id();
                String title = dataBean.getTitle();
                /**
                 *  1,普通用户，2，商户
                 * 宗亲会邀请：被邀请时消息flag=0,拒绝时flag=1
                 * 家谱邀请：邀请中flag=0，拒绝flag=1，同意flag=2
                 *
                 */
                String flag = dataBean.getFlag();//
                switch (relate_table) {
                    case "tour_order"://订单消息
                        if (TextUtils.isEmpty(relate_table_id)) break;
                        if ("1".equals(flag)) {
                            Intent intent = new Intent(mContext, OrderDetailActivity.class);
                            intent.putExtra("orderNo", relate_table_id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, UnusedOrderActivity.class);
                            intent.putExtra("orderNo", relate_table_id);
                            startActivity(intent);
                        }
                        break;
                    case "user_friend"://好友消息
                    case "cms_activity"://活动消息
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("relateTable", relate_table);
                        intent.putExtra("relateTableId", data.get(position).getRelate_table_id());
                        intent.putExtra("title", data.get(position).getTitle());
                        startActivity(intent);
                        break;
                    case "user_association"://邀请加入宗亲会消息
                        Intent intent1 = new Intent(mContext, AgnationInviteActivity.class);
                        UserMsgBean.DataBean dataBean1 = data.get(position);
                        String content = dataBean1.getContent();
                        String smsId = dataBean1.getId();
                        String invite_rid = dataBean1.getInvite_rid();//邀请人的根号
                        intent1.putExtra("agnationId", relate_table_id);//宗亲会ID
                        intent1.putExtra("content", content);
                        intent1.putExtra("flag", flag);
                        intent1.putExtra("userRid", invite_rid);
                        intent1.putExtra("smsId", smsId);
                        startActivityForResult(intent1, 1111);
                        break;
                    case "clan_glean":
                        Intent intent2 = new Intent(mContext, ClanGleanActivity.class);
                        UserMsgBean.DataBean dataBean2 = data.get(position);
                        String content2 = dataBean2.getContent();
                        String smsId2 = dataBean2.getId();
                        String invite_rid2 = dataBean2.getInvite_rid();//邀请人的根号
                        intent2.putExtra("clanId", relate_table_id);//家谱ID
                        intent2.putExtra("content", content2);
                        intent2.putExtra("flag", flag);
                        intent2.putExtra("userRid", invite_rid2);
                        intent2.putExtra("smsId", smsId2);
                        startActivityForResult(intent2, 1111);
                        break;
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1111 && resultCode == 2222) {
            this.data.clear();
            addData();
        }
    }
}
