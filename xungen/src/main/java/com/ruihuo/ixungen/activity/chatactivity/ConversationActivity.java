package com.ruihuo.ixungen.activity.chatactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.useractivity.AgnationMemeberActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.view.TitleBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class ConversationActivity extends AppCompatActivity {

    private String mTargetId;
    private String mTargetTitle;
    private TitleBar mTitlerBar;
    private final static int SET_TEXT_TYPING_TITLE = 1;//对方正在输入
    private final static int SET_VOICE_TYPING_TITLE = 2;//对方正在讲话
    private final static int SET_TARGETID_TITLE = 3;//当前会话没有用户输入
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();
        if (intent == null || intent.getData() == null)
            return;
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetTitle = intent.getData().getQueryParameter("title");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        initView();
        initData();
        inputStatus();//输入状态监听
        addListener();
        registerBroad();
    }

    private Discussion discussion;//讨论组创建者ID

    private void initData() {
        //获取讨论组的信息
        if (mConversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            RongIM.getInstance().getDiscussion(mTargetId, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussio) {
                    discussion = discussio;
                    if (discussion != null)
                        mTitlerBar.setTitle(TextUtils.isEmpty(discussion.getName()) ? "" : discussion.getName());
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void inputStatus() {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGETID_TITLE);
                    }
                }
            }
        });
    }

    private List<UserInfo> list = new ArrayList<>();

    private void initView() {
        mTitlerBar = (TitleBar) findViewById(R.id.conversation_titlebar);
        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
            mTitlerBar.mGroups.setVisibility(View.VISIBLE);
            RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
                @Override
                public void getGroupMembers(String s, final RongIM.IGroupMemberCallback iGroupMemberCallback) {
                    HttpInterface mHttp = HttpUtilsManager.getInstance(ConversationActivity.this);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("associationId", mTargetId);
                    params.putString("limit", 10000 + "");
                    mHttp.get(Url.AGNATION_MEMEBER_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                            int code = friendBean.getCode();
                            if (code == 0) {
                                list.clear();
                                List<FriendBean.DataBean> data = friendBean.getData();
                                for (int i = 0; i < data.size(); i++) {
                                    String avatar = data.get(i).getAvatar();
                                    String rid = data.get(i).getRid();
                                    String nikename = data.get(i).getNikename();
                                    Uri uri = Uri.parse(avatar);
                                    UserInfo userInfo = new UserInfo(rid, nikename, uri);
                                    list.add(userInfo);
                                }
                                iGroupMemberCallback.onGetGroupMembersResult(list);
                            }
                        }

                        @Override
                        public void onError(String message) {
                        }
                    });
                }
            });

        } else if (mConversationType.equals(Conversation.ConversationType.DISCUSSION)) {//讨论组
            mTitlerBar.mGroups.setVisibility(View.VISIBLE);
          /*  RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
                @Override
                public void getGroupMembers(String s, final RongIM.IGroupMemberCallback iGroupMemberCallback) {
                    HttpInterface mHttp = HttpUtilsManager.getInstance(ConversationActivity.this);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    //关系状态，0-正常，1-拉黑
                    params.putString("status", "0");
                    params.putString("limit", "10000");
                    params.putString("page", "1");
                    mHttp.post(Url.OBTAIN_FRIEND_FORM_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                            int code = friendBean.getCode();
                            if (code == 0) {
                                list.clear();
                                List<FriendBean.DataBean> data = friendBean.getData();
                                for (int i = 0; i < data.size(); i++) {
                                    String avatar = data.get(i).getAvatar();
                                    String rid = data.get(i).getRid();
                                    String nikename = data.get(i).getNikename();
                                    Uri uri = Uri.parse(Url.PHOTO_URL + avatar);
                                    UserInfo userInfo = new UserInfo(rid, nikename, uri);
                                    list.add(userInfo);
                                }
                                iGroupMemberCallback.onGetGroupMembersResult(list);
                            }
                        }

                        @Override
                        public void onError(String message) {
                        }
                    });
                }
            });*/
        } else {
            mTitlerBar.mGroups.setVisibility(View.GONE);
        }
        mTitlerBar.setTitle(TextUtils.isEmpty(mTargetTitle) ? "" : mTargetTitle);
    }

    private void addListener() {
        mTitlerBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitlerBar.mGroups.setOnClickListener(GroupListener);
    }

    View.OnClickListener GroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
                Intent intent = new Intent(ConversationActivity.this, AgnationMemeberActivity.class);
                intent.putExtra("associationId", mTargetId);
                intent.putExtra("from", "AgationActivity");
                startActivity(intent);
            } else if (mConversationType.equals(Conversation.ConversationType.DISCUSSION)) {
                Intent intent = new Intent(ConversationActivity.this, DiscussionInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("discussion", discussion);
                intent.putExtras(bundle);
                startActivityForResult(intent, 881);
                /*Intent intent2 = new Intent(ConversationActivity.this, JoinDiscussionActivity.class);
                intent2.putExtra("id", mTargetId);
                startActivity(intent2);*/
            }
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    mTitlerBar.setTitle("对方正在输入");
                    break;
                case SET_VOICE_TYPING_TITLE:
                    mTitlerBar.setTitle("对方正在讲话");
                    break;
                case SET_TARGETID_TITLE:
                    mTitlerBar.setTitle(TextUtils.isEmpty(mTargetTitle) ? "" : mTargetTitle);
                    break;
            }
        }
    };

    private void registerBroad() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.QUITE_SUCEESS);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == ConstantNum.QUITE_SUCEESS) {
                finish();
            }
        }
    };
}
