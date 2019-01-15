package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.R.bool;
import io.rong.imkit.R.drawable;
import io.rong.imkit.R.id;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.model.Event.AudioListenedEvent;
import io.rong.imkit.model.Event.ClearConversationEvent;
import io.rong.imkit.model.Event.ConnectEvent;
import io.rong.imkit.model.Event.ConversationNotificationEvent;
import io.rong.imkit.model.Event.ConversationRemoveEvent;
import io.rong.imkit.model.Event.ConversationTopEvent;
import io.rong.imkit.model.Event.ConversationUnreadEvent;
import io.rong.imkit.model.Event.CreateDiscussionEvent;
import io.rong.imkit.model.Event.DraftEvent;
import io.rong.imkit.model.Event.MessageDeleteEvent;
import io.rong.imkit.model.Event.MessageLeftEvent;
import io.rong.imkit.model.Event.MessageRecallEvent;
import io.rong.imkit.model.Event.MessagesClearEvent;
import io.rong.imkit.model.Event.OnMessageSendErrorEvent;
import io.rong.imkit.model.Event.OnReceiveMessageEvent;
import io.rong.imkit.model.Event.PublicServiceFollowableEvent;
import io.rong.imkit.model.Event.QuitDiscussionEvent;
import io.rong.imkit.model.Event.QuitGroupEvent;
import io.rong.imkit.model.Event.ReadReceiptEvent;
import io.rong.imkit.model.Event.RemoteMessageRecallEvent;
import io.rong.imkit.model.Event.SyncReadStatusEvent;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.utilities.OptionsPopupDialog.OnOptionsItemClickedListener;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ReadReceiptMessage;
import io.rong.push.RongPushClient;

/**
 * @author yudonghui
 * @date 2017/9/8
 * @describe May the Buddha bless bug-free!!!
 */
public class TestFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ConversationListAdapter.OnPortraitItemClick {
    private String TAG = "ConversationListFragment";
    private List<TestFragment.ConversationConfig> mConversationsConfig;
    private TestFragment mThis;
    private TestAdapter mAdapter;
    private ListView mList;
    private LinearLayout mNotificationBar;
    private ImageView mNotificationBarImage;
    private TextView mNotificationBarText;
    private boolean isShowWithoutConnected = false;

    public TestFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mThis = this;
        this.TAG = this.getClass().getSimpleName();
        this.mConversationsConfig = new ArrayList();
        EventBus.getDefault().register(this);
        InternalModuleManager.getInstance().onLoaded();
    }

    protected void initFragment(Uri uri) {
        RLog.d(this.TAG, "initFragment " + uri);
        ConversationType[] defConversationType = new ConversationType[]{ConversationType.PRIVATE, ConversationType.GROUP, ConversationType.DISCUSSION, ConversationType.SYSTEM, ConversationType.CUSTOMER_SERVICE, ConversationType.CHATROOM, ConversationType.PUBLIC_SERVICE, ConversationType.APP_PUBLIC_SERVICE};
        ConversationType[] type = defConversationType;
        int arr$ = defConversationType.length;

        int len$;
        for (len$ = 0; len$ < arr$; ++len$) {
            ConversationType i$ = type[len$];
            if (uri.getQueryParameter(i$.getName()) != null) {
                TestFragment.ConversationConfig conversationType = new TestFragment.ConversationConfig();
                conversationType.conversationType = i$;
                conversationType.isGathered = uri.getQueryParameter(i$.getName()).equals("true");
                this.mConversationsConfig.add(conversationType);
            }
        }

        if (this.mConversationsConfig.size() == 0) {
            String var9 = uri.getQueryParameter("type");
            ConversationType[] var10 = defConversationType;
            len$ = defConversationType.length;

            for (int var11 = 0; var11 < len$; ++var11) {
                ConversationType var12 = var10[var11];
                if (var12.getName().equals(var9)) {
                    TestFragment.ConversationConfig config = new TestFragment.ConversationConfig();
                    config.conversationType = var12;
                    config.isGathered = false;
                    this.mConversationsConfig.add(config);
                    break;
                }
            }
        }

        this.mAdapter.clear();
        if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.DISCONNECTED)) {
            RLog.d(this.TAG, "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
            this.isShowWithoutConnected = true;
        } else {
            this.getConversationList(this.getConfigConversationTypes());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.rc_fr_conversationlist, container, false);
        this.mNotificationBar = (LinearLayout) this.findViewById(view, id.rc_status_bar);
        this.mNotificationBar.setVisibility(View.GONE);
        this.mNotificationBarImage = (ImageView) this.findViewById(view, id.rc_status_bar_image);
        this.mNotificationBarText = (TextView) this.findViewById(view, id.rc_status_bar_text);
        View emptyView = this.findViewById(view, id.rc_conversation_list_empty_layout);
        TextView emptyText = (TextView) this.findViewById(view, id.rc_empty_tv);
        emptyText.setText(this.getActivity().getResources().getString(string.rc_conversation_list_empty_prompt));
        this.mList = (ListView) this.findViewById(view, id.rc_list);
        this.mList.setEmptyView(emptyView);
        this.mList.setOnItemClickListener(this);
        this.mList.setOnItemLongClickListener(this);
        if (this.mAdapter == null) {
            this.mAdapter = this.onResolveAdapter(this.getActivity());
        }

        this.mAdapter.setOnPortraitItemClick(this);
        this.mList.setAdapter(this.mAdapter);
        return view;
    }

    public void onResume() {
        super.onResume();
        RLog.d(this.TAG, "onResume " + RongIM.getInstance().getCurrentConnectionStatus());
        RongPushClient.clearAllPushNotifications(this.getActivity());
        this.setNotificationBarVisibility(RongIM.getInstance().getCurrentConnectionStatus());
    }

    private void getConversationList(ConversationType[] conversationTypes) {
        this.getConversationList(conversationTypes, new IHistoryDataResultCallback<List<Conversation>>() {
            @Override
            public void onResult(List<Conversation> data) {
                if (data != null && data.size() > 0) {
                    TestFragment.this.makeUiConversationList(data);
                    TestFragment.this.mAdapter.notifyDataSetChanged();
                } else {
                    RLog.w(TestFragment.this.TAG, "getConversationList return null " + RongIMClient.getInstance().getCurrentConnectionStatus());
                    TestFragment.this.isShowWithoutConnected = true;
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    public void getConversationList(ConversationType[] conversationTypes, final IHistoryDataResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (callback != null) {
                    callback.onResult(conversations);
                }
            }

            @Override
            public void onError(ErrorCode errorCode) {
                if (callback != null) {
                    callback.onError();
                }
            }
        }, conversationTypes);
    }

    public void focusUnreadItem() {
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        int visibleCount = last - first + 1;
        int count = this.mList.getCount();
        if (visibleCount < count) {
            int index;
            if (last < count - 1) {
                index = first + 1;
            } else {
                index = 0;
            }

            if (!this.selectNextUnReadItem(index, count)) {
                this.selectNextUnReadItem(0, count);
            }
        }

    }

    private boolean selectNextUnReadItem(int startIndex, int totalCount) {
        int index = -1;

        for (int i = startIndex; i < totalCount; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (uiConversation.getUnReadMessageCount() > 0) {
                index = i;
                break;
            }
        }

        if (index >= 0 && index < totalCount) {
            this.mList.setSelection(index);
            return true;
        } else {
            return false;
        }
    }

    private void setNotificationBarVisibility(ConnectionStatus status) {
        if (!this.getResources().getBoolean(bool.rc_is_show_warning_notification)) {
            RLog.e(this.TAG, "rc_is_show_warning_notification is disabled.");
        } else {
            String content = null;
            if (status.equals(ConnectionStatus.NETWORK_UNAVAILABLE)) {
                content = this.getResources().getString(string.rc_notice_network_unavailable);
            } else if (status.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                content = this.getResources().getString(string.rc_notice_tick);
            } else if (status.equals(ConnectionStatus.CONNECTED)) {
                this.mNotificationBar.setVisibility(View.GONE);
            } else if (status.equals(ConnectionStatus.DISCONNECTED)) {
                content = this.getResources().getString(string.rc_notice_disconnect);
            } else if (status.equals(ConnectionStatus.CONNECTING)) {
                content = this.getResources().getString(string.rc_notice_connecting);
            }

            if (content != null) {
                if (this.mNotificationBar.getVisibility() == View.GONE) {
                    final String finalContent = content;
                    this.getHandler().postDelayed(new Runnable() {
                        public void run() {
                            if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                                TestFragment.this.mNotificationBar.setVisibility(View.VISIBLE);
                                TestFragment.this.mNotificationBarText.setText(finalContent);
                                if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTING)) {
                                    TestFragment.this.mNotificationBarImage.setImageResource(drawable.rc_notification_connecting_animated);
                                } else {
                                    TestFragment.this.mNotificationBarImage.setImageResource(drawable.rc_notification_network_available);
                                }
                            }

                        }
                    }, 4000L);
                } else {
                    this.mNotificationBarText.setText(content);
                    if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTING)) {
                        this.mNotificationBarImage.setImageResource(drawable.rc_notification_connecting_animated);
                    } else {
                        this.mNotificationBarImage.setImageResource(drawable.rc_notification_network_available);
                    }
                }
            }

        }
    }

    public boolean onBackPressed() {
        return false;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setAdapter(TestAdapter adapter) {
        this.mAdapter = adapter;
        if (this.mList != null) {
            this.mList.setAdapter(adapter);
        }

    }

    public TestAdapter onResolveAdapter(Context context) {
        this.mAdapter = new TestAdapter(context);
        return this.mAdapter;
    }

    public void onEventMainThread(SyncReadStatusEvent event) {
        ConversationType conversationType = event.getConversationType();
        String targetId = event.getTargetId();
        RLog.d(this.TAG, "SyncReadStatusEvent " + conversationType + " " + targetId);
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        int position;
        if (this.getGatherState(conversationType)) {
            position = this.mAdapter.findGatheredItem(conversationType);
        } else {
            position = this.mAdapter.findPosition(conversationType, targetId);
        }

        if (position >= 0) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            uiConversation.clearUnRead(conversationType, targetId);
            if (position >= first && position <= last) {
                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(ReadReceiptEvent event) {
        ConversationType conversationType = event.getMessage().getConversationType();
        String targetId = event.getMessage().getTargetId();
        int originalIndex = this.mAdapter.findPosition(conversationType, targetId);
        boolean gatherState = this.getGatherState(conversationType);
        if (!gatherState && originalIndex >= 0) {
            UIConversation conversation = (UIConversation) this.mAdapter.getItem(originalIndex);
            ReadReceiptMessage content = (ReadReceiptMessage) event.getMessage().getContent();
            if (content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                conversation.setSentStatus(SentStatus.READ);
                this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(AudioListenedEvent event) {
        Message message = event.getMessage();
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        RLog.d(this.TAG, "Message: " + message.getObjectName() + " " + conversationType + " " + message.getSentStatus());
        if (this.isConfigured(conversationType)) {
            boolean gathered = this.getGatherState(conversationType);
            int position = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            if (position >= 0) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
                if (message.getMessageId() == uiConversation.getLatestMessageId()) {
                    uiConversation.updateConversation(message, gathered);
                    this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }
        }

    }

    public boolean shouldUpdateConversation(Message message, int left) {
        return true;
    }

    public void onEventMainThread(OnReceiveMessageEvent event) {
        Message message = event.getMessage();
        String targetId = message.getTargetId();
        ConversationType conversationType = message.getConversationType();
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        if (this.isConfigured(message.getConversationType()) && this.shouldUpdateConversation(event.getMessage(), event.getLeft())) {
            if (message.getMessageId() > 0) {
                boolean gathered = this.getGatherState(conversationType);
                int position;
                if (gathered) {
                    position = this.mAdapter.findGatheredItem(conversationType);
                } else {
                    position = this.mAdapter.findPosition(conversationType, targetId);
                }

                UIConversation uiConversation;
                int index;
                if (position < 0) {
                    uiConversation = UIConversation.obtain(message, gathered);
                    index = this.getPosition(uiConversation);
                    this.mAdapter.add(uiConversation, index);
                    this.mAdapter.notifyDataSetChanged();
                } else {
                    uiConversation = (UIConversation) this.mAdapter.getItem(position);
                    if (event.getMessage().getSentTime() > uiConversation.getUIConversationTime()) {
                        uiConversation.updateConversation(message, gathered);
                        this.mAdapter.remove(position);
                        index = this.getPosition(uiConversation);
                        if (index == position) {
                            this.mAdapter.add(uiConversation, index);
                            if (index >= first && index <= last) {
                                this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                            }
                        } else {
                            this.mAdapter.add(uiConversation, index);
                            if (index >= first && index <= last) {
                                this.mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        RLog.i(this.TAG, "ignore update message " + event.getMessage().getObjectName());
                    }
                }

                RLog.i(this.TAG, "conversation unread count : " + uiConversation.getUnReadMessageCount() + " " + conversationType + " " + targetId);
            }

            if (event.getLeft() == 0) {
                this.syncUnreadCount();
            }

            RLog.d(this.TAG, "OnReceiveMessageEvent: " + message.getObjectName() + " " + event.getLeft() + " " + conversationType + " " + targetId);
        }

    }

    public void onEventMainThread(MessageLeftEvent event) {
        if (event.left == 0) {
            this.syncUnreadCount();
        }

    }

    private void syncUnreadCount() {
        if (this.mAdapter.getCount() > 0) {
            final int first = this.mList.getFirstVisiblePosition();
            final int last = this.mList.getLastVisiblePosition();

            for (int i = 0; i < this.mAdapter.getCount(); ++i) {
                final UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                ConversationType conversationType = uiConversation.getConversationType();
                String targetId = uiConversation.getConversationTargetId();
                final int position;
                if (this.getGatherState(conversationType)) {
                    position = this.mAdapter.findGatheredItem(conversationType);
                    RongIMClient.getInstance().getUnreadCount(new ResultCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            uiConversation.setUnReadMessageCount(integer.intValue());
                            if (position >= first && position <= last) {
                                TestFragment.this.mAdapter.getView(position, TestFragment.this.mList.getChildAt(position - TestFragment.this.mList.getFirstVisiblePosition()), TestFragment.this.mList);
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    }, new ConversationType[]{conversationType});
                } else {
                    position = this.mAdapter.findPosition(conversationType, targetId);
                    RongIMClient.getInstance().getUnreadCount(conversationType, targetId, new ResultCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            uiConversation.setUnReadMessageCount(integer.intValue());
                            if (position >= first && position <= last) {
                                TestFragment.this.mAdapter.getView(position, TestFragment.this.mList.getChildAt(position - TestFragment.this.mList.getFirstVisiblePosition()), TestFragment.this.mList);
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    });
                }
            }
        }

    }

    public void onEventMainThread(MessageRecallEvent event) {
        RLog.d(this.TAG, "MessageRecallEvent");
        int count = this.mAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (event.getMessageId() == uiConversation.getLatestMessageId()) {
                boolean gatherState = ((UIConversation) this.mAdapter.getItem(i)).getConversationGatherState();
                if (gatherState) {
                    RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversations) {
                            if (conversations != null && conversations.size() > 0) {
                                UIConversation uiConversation = TestFragment.this.makeUIConversation(conversations);
                                int oldPos = TestFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if (oldPos >= 0) {
                                    TestFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = TestFragment.this.getPosition(uiConversation);
                                TestFragment.this.mAdapter.add(uiConversation, newIndex);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    }, new ConversationType[]{uiConversation.getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new ResultCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation conversation) {
                            if (conversation != null) {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = TestFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if (pos >= 0) {
                                    TestFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = TestFragment.this.getPosition(temp);
                                TestFragment.this.mAdapter.add(temp, newPosition);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(RemoteMessageRecallEvent event) {
        RLog.d(this.TAG, "RemoteMessageRecallEvent");
        int count = this.mAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (event.getMessageId() == uiConversation.getLatestMessageId()) {
                boolean gatherState = uiConversation.getConversationGatherState();
                if (gatherState) {
                    RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversations) {
                            if (conversations != null && conversations.size() > 0) {
                                UIConversation uiConversation = TestFragment.this.makeUIConversation(conversations);
                                int oldPos = TestFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if (oldPos >= 0) {
                                    TestFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = TestFragment.this.getPosition(uiConversation);
                                TestFragment.this.mAdapter.add(uiConversation, newIndex);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    }, new ConversationType[]{((UIConversation) this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new ResultCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation conversation) {
                            if (conversation != null) {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = TestFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if (pos >= 0) {
                                    TestFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = TestFragment.this.getPosition(temp);
                                TestFragment.this.mAdapter.add(temp, newPosition);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Message message) {
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        RLog.d(this.TAG, "Message: " + message.getObjectName() + " " + message.getMessageId() + " " + conversationType + " " + message.getSentStatus());
        boolean gathered = this.getGatherState(conversationType);
        if (this.isConfigured(conversationType) && message.getMessageId() > 0) {
            int position = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            UIConversation uiConversation;
            int index;
            if (position < 0) {
                uiConversation = UIConversation.obtain(message, gathered);
                index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                this.mAdapter.notifyDataSetChanged();
            } else {
                uiConversation = (UIConversation) this.mAdapter.getItem(position);
                this.mAdapter.remove(position);
                uiConversation.updateConversation(message, gathered);
                index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                if (position == index) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    public void onEventMainThread(ConnectionStatus status) {
        RLog.d(this.TAG, "ConnectionStatus, " + status.toString());
        this.setNotificationBarVisibility(status);
    }

    public void onEventMainThread(ConnectEvent event) {
        if (this.isShowWithoutConnected) {
            this.getConversationList(this.getConfigConversationTypes());
            this.isShowWithoutConnected = false;
        }

    }

    public void onEventMainThread(final CreateDiscussionEvent createDiscussionEvent) {
        RLog.d(this.TAG, "createDiscussionEvent");
        final String targetId = createDiscussionEvent.getDiscussionId();
        if (this.isConfigured(ConversationType.DISCUSSION)) {
            RongIMClient.getInstance().getConversation(ConversationType.DISCUSSION, targetId, new ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation != null) {
                        int position;
                        if (TestFragment.this.getGatherState(ConversationType.DISCUSSION)) {
                            position = TestFragment.this.mAdapter.findGatheredItem(ConversationType.DISCUSSION);
                        } else {
                            position = TestFragment.this.mAdapter.findPosition(ConversationType.DISCUSSION, targetId);
                        }

                        conversation.setConversationTitle(createDiscussionEvent.getDiscussionName());
                        UIConversation uiConversation;
                        if (position < 0) {
                            uiConversation = UIConversation.obtain(conversation, TestFragment.this.getGatherState(ConversationType.DISCUSSION));
                            int index = TestFragment.this.getPosition(uiConversation);
                            TestFragment.this.mAdapter.add(uiConversation, index);
                            TestFragment.this.mAdapter.notifyDataSetChanged();
                        } else {
                            uiConversation = (UIConversation) TestFragment.this.mAdapter.getItem(position);
                            uiConversation.updateConversation(conversation, TestFragment.this.getGatherState(ConversationType.DISCUSSION));
                            TestFragment.this.mAdapter.getView(position, TestFragment.this.mList.getChildAt(position - TestFragment.this.mList.getFirstVisiblePosition()), TestFragment.this.mList);
                        }
                    }
                }

                @Override
                public void onError(ErrorCode errorCode) {

                }
            });
        }

    }

    public void onEventMainThread(final DraftEvent draft) {
        ConversationType conversationType = draft.getConversationType();
        String targetId = draft.getTargetId();
        RLog.i(this.TAG, "Draft : " + conversationType);
        if (this.isConfigured(conversationType)) {
            final boolean gathered = this.getGatherState(conversationType);
            final int position = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            RongIMClient.getInstance().getConversation(conversationType, targetId, new ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation != null) {
                        UIConversation uiConversation;
                        if (position < 0) {
                            if (!TextUtils.isEmpty(draft.getContent())) {
                                uiConversation = UIConversation.obtain(conversation, gathered);
                                int index = TestFragment.this.getPosition(uiConversation);
                                TestFragment.this.mAdapter.add(uiConversation, index);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            uiConversation = (UIConversation) TestFragment.this.mAdapter.getItem(position);
                            if (TextUtils.isEmpty(draft.getContent()) && !TextUtils.isEmpty(uiConversation.getDraft()) || !TextUtils.isEmpty(draft.getContent()) && TextUtils.isEmpty(uiConversation.getDraft()) || !TextUtils.isEmpty(draft.getContent()) && !TextUtils.isEmpty(uiConversation.getDraft()) && !draft.getContent().equals(uiConversation.getDraft())) {
                                uiConversation.updateConversation(conversation, gathered);
                                TestFragment.this.mAdapter.getView(position, TestFragment.this.mList.getChildAt(position - TestFragment.this.mList.getFirstVisiblePosition()), TestFragment.this.mList);
                            }
                        }
                    }
                }

                @Override
                public void onError(ErrorCode errorCode) {

                }
            });
        }

    }

    public void onEventMainThread(Group groupInfo) {
        RLog.d(this.TAG, "Group: " + groupInfo.getName() + " " + groupInfo.getId());
        int count = this.mAdapter.getCount();
        if (groupInfo.getName() != null) {
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();

            for (int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                uiConversation.updateConversation(groupInfo);
                if (i >= first && i <= last) {
                    this.mAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(Discussion discussion) {
        RLog.d(this.TAG, "Discussion: " + discussion.getName() + " " + discussion.getId());
        if (this.isConfigured(ConversationType.DISCUSSION)) {
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();
            int position;
            if (this.getGatherState(ConversationType.DISCUSSION)) {
                position = this.mAdapter.findGatheredItem(ConversationType.DISCUSSION);
            } else {
                position = this.mAdapter.findPosition(ConversationType.DISCUSSION, discussion.getId());
            }

            if (position >= 0) {
                for (int i = 0; i == position; ++i) {
                    UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                    uiConversation.updateConversation(discussion);
                    if (i >= first && i <= last) {
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }
        }

    }

    public void onEventMainThread(GroupUserInfo groupUserInfo) {
        RLog.d(this.TAG, "GroupUserInfo " + groupUserInfo.getGroupId() + " " + groupUserInfo.getUserId() + " " + groupUserInfo.getNickname());
        if (groupUserInfo.getNickname() != null && groupUserInfo.getGroupId() != null) {
            int count = this.mAdapter.getCount();
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();

            for (int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                if (!this.getGatherState(ConversationType.GROUP) && uiConversation.getConversationTargetId().equals(groupUserInfo.getGroupId()) && uiConversation.getConversationSenderId().equals(groupUserInfo.getUserId())) {
                    uiConversation.updateConversation(groupUserInfo);
                    if (i >= first && i <= last) {
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }

        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        RLog.i(this.TAG, "UserInfo " + userInfo.getUserId() + " " + userInfo.getName());
        int count = this.mAdapter.getCount();
        int last = this.mList.getLastVisiblePosition();
        int first = this.mList.getFirstVisiblePosition();

        for (int i = 0; i < count && userInfo.getName() != null; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (uiConversation.hasNickname(userInfo.getUserId())) {
                RLog.i(this.TAG, "has nick name");
            } else {
                uiConversation.updateConversation(userInfo);
                if (i >= first && i <= last) {
                    this.mAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }
        }

    }

    public void onEventMainThread(PublicServiceProfile profile) {
        RLog.d(this.TAG, "PublicServiceProfile");
        int count = this.mAdapter.getCount();
        boolean gatherState = this.getGatherState(profile.getConversationType());

        for (int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (uiConversation.getConversationType().equals(profile.getConversationType()) && uiConversation.getConversationTargetId().equals(profile.getTargetId()) && !gatherState) {
                uiConversation.setUIConversationTitle(profile.getName());
                uiConversation.setIconUrl(profile.getPortraitUri());
                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                break;
            }
        }

    }

    public void onEventMainThread(PublicServiceFollowableEvent event) {
        RLog.d(this.TAG, "PublicServiceFollowableEvent");
        if (!event.isFollow()) {
            int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
            if (originalIndex >= 0) {
                this.mAdapter.remove(originalIndex);
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void onEventMainThread(ConversationUnreadEvent unreadEvent) {
        RLog.d(this.TAG, "ConversationUnreadEvent");
        ConversationType conversationType = unreadEvent.getType();
        String targetId = unreadEvent.getTargetId();
        int position = this.getGatherState(conversationType) ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
        if (position >= 0) {
            int first = this.mList.getFirstVisiblePosition();
            int last = this.mList.getLastVisiblePosition();
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            uiConversation.clearUnRead(conversationType, targetId);
            if (position >= first && position <= last) {
                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(ConversationTopEvent setTopEvent) {
        RLog.d(this.TAG, "ConversationTopEvent");
        ConversationType conversationType = setTopEvent.getConversationType();
        String targetId = setTopEvent.getTargetId();
        int position = this.mAdapter.findPosition(conversationType, targetId);
        if (position >= 0 && !this.getGatherState(conversationType)) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            if (uiConversation.isTop() != setTopEvent.isTop()) {
                uiConversation.setTop(!uiConversation.isTop());
                this.mAdapter.remove(position);
                int index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                if (index == position) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    public void onEventMainThread(ConversationRemoveEvent removeEvent) {
        RLog.d(this.TAG, "ConversationRemoveEvent");
        ConversationType conversationType = removeEvent.getType();
        this.removeConversation(conversationType, removeEvent.getTargetId());
    }

    public void onEventMainThread(ClearConversationEvent clearConversationEvent) {
        RLog.d(this.TAG, "ClearConversationEvent");
        List typeList = clearConversationEvent.getTypes();

        for (int i = this.mAdapter.getCount() - 1; i >= 0; --i) {
            if (typeList.indexOf(((UIConversation) this.mAdapter.getItem(i)).getConversationType()) >= 0) {
                this.mAdapter.remove(i);
            }
        }

        this.mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(MessageDeleteEvent event) {
        RLog.d(this.TAG, "MessageDeleteEvent");
        int count = this.mAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            if (event.getMessageIds().contains(Integer.valueOf(((UIConversation) this.mAdapter.getItem(i)).getLatestMessageId()))) {
                boolean gatherState = ((UIConversation) this.mAdapter.getItem(i)).getConversationGatherState();
                if (gatherState) {
                    RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversations) {
                            if (conversations != null && conversations.size() != 0) {
                                UIConversation uiConversation = TestFragment.this.makeUIConversation(conversations);
                                int oldPos = TestFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if (oldPos >= 0) {
                                    TestFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = TestFragment.this.getPosition(uiConversation);
                                TestFragment.this.mAdapter.add(uiConversation, newIndex);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    }, new ConversationType[]{((UIConversation) this.mAdapter.getItem(i)).getConversationType()});

                } else {
                    RongIM.getInstance().getConversation(((UIConversation) this.mAdapter.getItem(i)).getConversationType(), ((UIConversation) this.mAdapter.getItem(i)).getConversationTargetId(), new ResultCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation conversation) {
                            if (conversation == null) {
                                RLog.d(TestFragment.this.TAG, "onEventMainThread getConversation : onSuccess, conversation = null");
                            } else {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = TestFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if (pos >= 0) {
                                    TestFragment.this.mAdapter.remove(pos);
                                }

                                int newIndex = TestFragment.this.getPosition(temp);
                                TestFragment.this.mAdapter.add(temp, newIndex);
                                TestFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(ConversationNotificationEvent notificationEvent) {
        int originalIndex = this.mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
        if (originalIndex >= 0) {
            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(MessagesClearEvent clearMessagesEvent) {
        RLog.d(this.TAG, "MessagesClearEvent");
        ConversationType conversationType = clearMessagesEvent.getType();
        String targetId = clearMessagesEvent.getTargetId();
        int position = this.getGatherState(conversationType) ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
        if (position >= 0) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            uiConversation.clearLastMessage();
            this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(OnMessageSendErrorEvent sendErrorEvent) {
        Message message = sendErrorEvent.getMessage();
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        if (this.isConfigured(conversationType)) {
            int first = this.mList.getFirstVisiblePosition();
            int last = this.mList.getLastVisiblePosition();
            boolean gathered = this.getGatherState(conversationType);
            int index = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            if (index >= 0) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(index);
                message.setSentStatus(SentStatus.FAILED);
                uiConversation.updateConversation(message, gathered);
                if (index >= first && index <= last) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }
        }

    }

    public void onEventMainThread(QuitDiscussionEvent event) {
        RLog.d(this.TAG, "QuitDiscussionEvent");
        this.removeConversation(ConversationType.DISCUSSION, event.getDiscussionId());
    }

    public void onEventMainThread(QuitGroupEvent event) {
        RLog.d(this.TAG, "QuitGroupEvent");
        this.removeConversation(ConversationType.GROUP, event.getGroupId());
    }

    private void removeConversation(final ConversationType conversationType, String targetId) {
        boolean gathered = this.getGatherState(conversationType);
        int index;
        if (gathered) {
            index = this.mAdapter.findGatheredItem(conversationType);
            if (index >= 0) {
                RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        int oldPos = TestFragment.this.mAdapter.findGatheredItem(conversationType);
                        if (oldPos >= 0) {
                            TestFragment.this.mAdapter.remove(oldPos);
                            if (conversations != null && conversations.size() > 0) {
                                UIConversation uiConversation = TestFragment.this.makeUIConversation(conversations);
                                int newIndex = TestFragment.this.getPosition(uiConversation);
                                TestFragment.this.mAdapter.add(uiConversation, newIndex);
                            }

                            TestFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {

                    }
                }, new ConversationType[]{conversationType});

            }
        } else {
            index = this.mAdapter.findPosition(conversationType, targetId);
            if (index >= 0) {
                this.mAdapter.remove(index);
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void onPortraitItemClick(View v, UIConversation data) {
        ConversationType type = data.getConversationType();
        if (this.getGatherState(type)) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
        } else {
            if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(this.getActivity(), type, data.getConversationTargetId());
                if (isDefault) {
                    return;
                }
            }

            data.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), type, data.getConversationTargetId(), data.getUIConversationTitle());
        }

    }

    public boolean onPortraitItemLongClick(View v, UIConversation data) {
        ConversationType type = data.getConversationType();
        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(this.getActivity(), type, data.getConversationTargetId());
            if (isDealt) {
                return true;
            }
        }

        if (!this.getGatherState(type)) {
            this.buildMultiDialog(data);
            return true;
        } else {
            this.buildSingleDialog(data);
            return true;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
        ConversationType conversationType = uiConversation.getConversationType();
        if (this.getGatherState(conversationType)) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), conversationType);
        } else {
            if (RongContext.getInstance().getConversationListBehaviorListener() != null && RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(this.getActivity(), view, uiConversation)) {
                return;
            }

            uiConversation.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), conversationType, uiConversation.getConversationTargetId(), uiConversation.getUIConversationTitle());
        }

    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
            if (isDealt) {
                return true;
            }
        }

        if (!this.getGatherState(uiConversation.getConversationType())) {
            this.buildMultiDialog(uiConversation);
            return true;
        } else {
            this.buildSingleDialog(uiConversation);
            return true;
        }
    }

    private void buildMultiDialog(final UIConversation uiConversation) {
        String[] items = new String[2];
        if (uiConversation.isTop()) {
            items[0] = RongContext.getInstance().getString(string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = RongContext.getInstance().getString(string.rc_conversation_list_dialog_set_top);
        }

        items[1] = RongContext.getInstance().getString(string.rc_conversation_list_dialog_remove);
        OptionsPopupDialog.newInstance(this.getActivity(), items).setOptionsPopupDialogListener(new OnOptionsItemClickedListener() {
            public void onOptionsItemClicked(int which) {
                if (which == 0) {
                    RongIM.getInstance().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            if (uiConversation.isTop()) {
                                Toast.makeText(RongContext.getInstance(), TestFragment.this.getString(string.rc_conversation_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RongContext.getInstance(), TestFragment.this.getString(string.rc_conversation_list_dialog_set_top), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }
                    });
                } else if (which == 1) {
                    RongIM.getInstance().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), (ResultCallback) null);
                }

            }
        }).show();
    }

    private void buildSingleDialog(final UIConversation uiConversation) {
        String[] items = new String[]{RongContext.getInstance().getString(string.rc_conversation_list_dialog_remove)};
        OptionsPopupDialog.newInstance(this.getActivity(), items).setOptionsPopupDialogListener(new OnOptionsItemClickedListener() {
            public void onOptionsItemClicked(int which) {
                RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        if (conversations != null && conversations.size() > 0) {
                            Iterator i$ = conversations.iterator();

                            while (i$.hasNext()) {
                                Conversation conversation = (Conversation) i$.next();
                                RongIMClient.getInstance().removeConversation(conversation.getConversationType(), conversation.getTargetId(), (ResultCallback) null);
                            }
                        }
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {

                    }
                }, new ConversationType[]{uiConversation.getConversationType()});

                int position = TestFragment.this.mAdapter.findGatheredItem(uiConversation.getConversationType());
                TestFragment.this.mAdapter.remove(position);
                TestFragment.this.mAdapter.notifyDataSetChanged();
            }
        }).show();
    }

    private void makeUiConversationList(List<Conversation> conversationList) {
        Iterator i$ = conversationList.iterator();
        while (i$.hasNext()) {
            Conversation conversation = (Conversation) i$.next();
            ConversationType conversationType = conversation.getConversationType();
            String targetId = conversation.getTargetId();
            boolean gatherState = this.getGatherState(conversationType);
            UIConversation uiConversation;
            int originalIndex;
            if (gatherState) {
                originalIndex = this.mAdapter.findGatheredItem(conversationType);
                if (originalIndex >= 0) {
                    uiConversation = (UIConversation) this.mAdapter.getItem(originalIndex);
                    uiConversation.updateConversation(conversation, true);
                } else {
                    uiConversation = UIConversation.obtain(conversation, true);
                    this.mAdapter.add(uiConversation);
                }
            } else {
                originalIndex = this.mAdapter.findPosition(conversationType, targetId);
                if (originalIndex < 0) {
                    uiConversation = UIConversation.obtain(conversation, false);
                    this.mAdapter.add(uiConversation);
                } else {
                    uiConversation = (UIConversation) this.mAdapter.getItem(originalIndex);
                    uiConversation.setUnReadMessageCount(conversation.getUnreadMessageCount());
                }
            }
        }

    }

    private UIConversation makeUIConversation(List<Conversation> conversations) {
        int unreadCount = 0;
        boolean topFlag = false;
        boolean isMentioned = false;
        Conversation newest = (Conversation) conversations.get(0);

        Conversation conversation;
        for (Iterator uiConversation = conversations.iterator(); uiConversation.hasNext(); unreadCount += conversation.getUnreadMessageCount()) {
            conversation = (Conversation) uiConversation.next();
            if (newest.isTop()) {
                if (conversation.isTop() && conversation.getSentTime() > newest.getSentTime()) {
                    newest = conversation;
                }
            } else if (conversation.isTop() || conversation.getSentTime() > newest.getSentTime()) {
                newest = conversation;
            }

            if (conversation.isTop()) {
                topFlag = true;
            }

            if (conversation.getMentionedCount() > 0) {
                isMentioned = true;
            }
        }

        UIConversation uiConversation1 = UIConversation.obtain(newest, this.getGatherState(newest.getConversationType()));
        uiConversation1.setUnReadMessageCount(unreadCount);
        uiConversation1.setTop(false);
        uiConversation1.setMentionedFlag(isMentioned);
        return uiConversation1;
    }

    private int getPosition(UIConversation uiConversation) {
        int count = this.mAdapter.getCount();
        int position = 0;

        for (int i = 0; i < count; ++i) {
            if (uiConversation.isTop()) {
                if (!((UIConversation) this.mAdapter.getItem(i)).isTop() || ((UIConversation) this.mAdapter.getItem(i)).getUIConversationTime() <= uiConversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            } else {
                if (!((UIConversation) this.mAdapter.getItem(i)).isTop() && ((UIConversation) this.mAdapter.getItem(i)).getUIConversationTime() <= uiConversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            }
        }

        return position;
    }

    private boolean isConfigured(ConversationType conversationType) {
        for (int i = 0; i < this.mConversationsConfig.size(); ++i) {
            if (conversationType.equals(((TestFragment.ConversationConfig) this.mConversationsConfig.get(i)).conversationType)) {
                return true;
            }
        }

        return false;
    }

    public boolean getGatherState(ConversationType conversationType) {
        Iterator i$ = this.mConversationsConfig.iterator();

        TestFragment.ConversationConfig config;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            config = (TestFragment.ConversationConfig) i$.next();
        } while (!config.conversationType.equals(conversationType));

        return config.isGathered;
    }

    private ConversationType[] getConfigConversationTypes() {
        ConversationType[] conversationTypes = new ConversationType[this.mConversationsConfig.size()];

        for (int i = 0; i < this.mConversationsConfig.size(); ++i) {
            conversationTypes[i] = ((TestFragment.ConversationConfig) this.mConversationsConfig.get(i)).conversationType;
        }

        return conversationTypes;
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this.mThis);
        super.onDestroyView();
    }

    private class ConversationConfig {
        ConversationType conversationType;
        boolean isGathered;

        private ConversationConfig() {
        }
    }
}
