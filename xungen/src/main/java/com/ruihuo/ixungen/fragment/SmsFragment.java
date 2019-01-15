package com.ruihuo.ixungen.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.chatactivity.TestAdapter;
import com.ruihuo.ixungen.activity.chatactivity.TestFragment;
import com.ruihuo.ixungen.activity.genxin.ActionSmsActivity;
import com.ruihuo.ixungen.activity.genxin.PostsSmsActivity;
import com.ruihuo.ixungen.activity.genxin.SystemSmsActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.CallBackInterface;

import io.rong.imkit.RongContext;
import io.rong.imlib.model.Conversation;

/**
 * @author yudonghui
 * @date 2017/10/26
 * @describe May the Buddha bless bug-free!!!
 */
public class SmsFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Context mContext;
    private LinearLayout mLlPostsSms;
    private LinearLayout mLlSystemSms;
    private LinearLayout mLlActionSms;
    //帖子消息后面的红点
    private TextView mRedPosts;
    private TextView mRedSystem;
    private TextView mRedAction;
    private FrameLayout mFlChatList;
    private FragmentManager mFragmentManager;
    /**
     * 会话列表的fragment
     */
    private TestFragment mConversationListFragment = null;
    // private Conversation.ConversationType[] mConversationsTypes = null;

    private TestAdapter adapter;

    /*  public void setFragmentManager(FragmentManager mFragmentManager) {
          this.mFragmentManager = mFragmentManager;
      }
  */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sms, null);
        mContext = getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        initView();
        addListener();
        return view;
    }

    private void initView() {
        mLlPostsSms = (LinearLayout) view.findViewById(R.id.ll_postsms);
        mLlSystemSms = (LinearLayout) view.findViewById(R.id.ll_systemsms);
        mLlActionSms = (LinearLayout) view.findViewById(R.id.ll_actionsms);
        mRedPosts = (TextView) view.findViewById(R.id.posts_red_point);
        mRedSystem = (TextView) view.findViewById(R.id.system_red_point);
        mRedAction = (TextView) view.findViewById(R.id.action_red_point);
        mFlChatList = (FrameLayout) view.findViewById(R.id.fl_chatlist);
        Fragment listFragment = initConversationList();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_chatlist, listFragment).show(listFragment);
        fragmentTransaction.commit();
        redPointsStatus();
        //注册一个动态广播
        registerBoradcastReceiver();
    }


    private void redPointsStatus() {
        SharedPreferences sp = mContext.getSharedPreferences("redPoints", Context.MODE_PRIVATE);
        int redPosts = sp.getInt("redPosts", 0);
        int redSystem = sp.getInt("redSystem", 0);
        int redAction = sp.getInt("redAction", 0);
        if (redPosts > 0) mRedPosts.setVisibility(View.VISIBLE);
        else mRedPosts.setVisibility(View.GONE);

        if (redSystem > 0) mRedSystem.setVisibility(View.VISIBLE);
        else mRedSystem.setVisibility(View.GONE);

        if (redAction > 0) mRedAction.setVisibility(View.VISIBLE);
        else mRedAction.setVisibility(View.GONE);
    }

    public void setData() {
        Log.e("会话fragment", "唤醒适配器");
        adapter.notifyDataSetChanged();
    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            TestFragment listFragment = new TestFragment();
            adapter = new TestAdapter(RongContext.getInstance());
            listFragment.setAdapter(adapter);
            Uri uri;
            uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .build();
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    public void refreshList() {

        if (mConversationListFragment != null) {
            Log.e("会话fragment", "不为空");
            Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .build();
            mConversationListFragment.setUri(uri);
            mConversationListFragment.onRestoreUI();
        } else {
            Log.e("会话fragment", "会话列表为空");
        }
    }

    private void addListener() {
        mLlPostsSms.setOnClickListener(this);
        mLlSystemSms.setOnClickListener(this);
        mLlActionSms.setOnClickListener(this);
    }

    private CallBackInterface mCallBack;

    public void setListener(CallBackInterface mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sp = mContext.getSharedPreferences("redPoints", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switch (v.getId()) {
            case R.id.ll_postsms:
                editor.putInt("redPosts", 0);
                mRedPosts.setVisibility(View.GONE);
                Intent intent = new Intent(mContext, PostsSmsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_systemsms:
                editor.putInt("redSystem", 0);
                mRedSystem.setVisibility(View.GONE);
                Intent intent1 = new Intent(mContext, SystemSmsActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_actionsms:
                editor.putInt("redAction", 0);
                mRedAction.setVisibility(View.GONE);
                Intent intent2 = new Intent(mContext, ActionSmsActivity.class);
                startActivity(intent2);
                break;
        }
        editor.commit();
        mCallBack.callBack();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                //帖子消息
                mRedPosts.setVisibility(View.VISIBLE);
            } else if (msg.what == 0) {
                //系统消息
                mRedSystem.setVisibility(View.VISIBLE);
            } else if (msg.what == 3) {
                //活动消息
                mRedAction.setVisibility(View.VISIBLE);
            }
        }
    };

    public void redPoints(String extra) {
        Message message = Message.obtain();

        if ("2".equals(extra)) {
            message.what = 2;
        } else if ("0".equals(extra) || "99".equals(extra)) {
            message.what = 0;
        } else if ("3".equals(extra)) {
            message.what = 3;
        } else message.what = 9;
        handler.sendMessage(message);

    }

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.CONNECT_SUCEESS);
        mContext.registerReceiver(br, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(br);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.CONNECT_SUCEESS)) {
            }
        }
    };
}
