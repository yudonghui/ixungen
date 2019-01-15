package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseActivity;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.activity.useractivity.TwoCodeActivity;
import com.ruihuo.ixungen.adapter.IVRecyclerAdapter;
import com.ruihuo.ixungen.adapter.SpaceItemDecoration;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;

public class DiscussionInfoActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout mLl_member;
    private RecyclerView mRecyclerView;
    private LinearLayout mLl_name;
    private TextView mName;
    private LinearLayout mLl_card;
    private ImageView mIsSave;
    private ImageView mSmsStop;
    private TextView mClear;
    private Discussion discussion;
    private String creatorId;
    private String discussionId;
    private IVRecyclerAdapter mAdapter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_discussion_indo);
        mContext = this;
        discussion = getIntent().getParcelableExtra("discussion");
        initView();
        addData();
        addListener();
    }

    private List<String> memberIdList = new ArrayList<>();
    private boolean isCreator = false;//是否是创建者

    private void initView() {
        mTitleBar.setTitle("讨论组设置");
        mTitleBar.mTextRegister.setText("退出");
        mTitleBar.mTextRegister.setTextColor(getResources().getColor(R.color.green_txt));
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
        mLl_member = (LinearLayout) findViewById(R.id.ll_member);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLl_name = (LinearLayout) findViewById(R.id.ll_name);
        mName = (TextView) findViewById(R.id.name);
        mLl_card = (LinearLayout) findViewById(R.id.ll_card);
        mIsSave = (ImageView) findViewById(R.id.isSave);
        mSmsStop = (ImageView) findViewById(R.id.smsStop);
        mClear = (TextView) findViewById(R.id.clear);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DisplayUtilX.dip2px(5)));


    }

    private void addListener() {
        mTitleBar.mTextRegister.setOnClickListener(QuitListener);//退出讨论组
        mLl_name.setOnClickListener(DiscussionNameListener);//讨论组名称
        mLl_card.setOnClickListener(CardListener);//讨论组名片
        mLl_member.setOnClickListener(MemberListener);//讨论组成员
        mSmsStop.setOnClickListener(SmsStopListener);//消息免打扰
        mIsSave.setOnClickListener(IsSaveListener);//保存到家族列表
        mClear.setOnClickListener(ClearMessageListener);//清空聊天记录
    }

    private void addData() {
        if (discussion != null) {
            memberIdList.clear();
            creatorId = discussion.getCreatorId();
            discussionId = discussion.getId();
            name = discussion.getName();
            if (XunGenApp.rid.equals(creatorId)) {
                isCreator = true;
                memberIdList.add("add");//加
                //memberIdList.add("delete");//减
            } else {
                isCreator = false;
                memberIdList.add("add");//加
            }
            memberIdList.addAll(discussion.getMemberIdList());
            String name = discussion.getName();
            mName.setText(TextUtils.isEmpty(name) ? "" : name);
            mAdapter = new IVRecyclerAdapter(mContext, memberIdList, RecyclerViewListener);
            mRecyclerView.setAdapter(mAdapter);
            isSmsStop();
        }
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("groupId", discussionId);
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        mHttp.get(Url.DISCUSSION_EXIST, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int data = jsonObject.getInt("data");
                    if (data == 1) {
                        isSave = true;
                        mIsSave.setImageResource(R.drawable.btn_on);
                    } else {
                        isSave = false;
                        mIsSave.setImageResource(R.drawable.btn_off);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    View.OnClickListener QuitListener = new View.OnClickListener() {//退出群组
        @Override
        public void onClick(View v) {
            if (isSave) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("groupId", discussionId);
                mHttp.post(Url.REMOVE_DISCUSSION, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {

                        RongIM.getInstance().quitDiscussion(discussionId, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                loadingDialogUtils.setDimiss();
                                Intent intent = new Intent();
                                sendBroadcast(new Intent(ConstantNum.QUITE_SUCEESS));
                                finish();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                loadingDialogUtils.setDimiss();
                            }
                        });

                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            } else {
                RongIM.getInstance().quitDiscussion(discussionId, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent();
                        setResult(888, intent);
                        finish();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
            }
        }
    };
    View.OnClickListener ClearMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RongIM.getInstance().clearMessages(Conversation.ConversationType.DISCUSSION, discussionId, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Toast.makeText(mContext, "已清空", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(mContext, "无法清空", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    boolean isSave = false;
    View.OnClickListener IsSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isSave) {
                isSave = false;
                mIsSave.setImageResource(R.drawable.btn_off);
            } else {
                isSave = true;
                mIsSave.setImageResource(R.drawable.btn_on);
            }
            dealDiscussion();
        }

    };
    boolean isSmsStop = false;
    View.OnClickListener SmsStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Conversation.ConversationNotificationStatus doNotDisturb;
            if (isSmsStop) {
                isSmsStop = false;
                // mSmsStop.setImageResource(R.drawable.btn_off);
                doNotDisturb = Conversation.ConversationNotificationStatus.NOTIFY;
            } else {
                isSmsStop = true;
                //mSmsStop.setImageResource(R.drawable.btn_on);
                doNotDisturb = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
            }
            RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.DISCUSSION, discussionId, doNotDisturb, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
            isSmsStop();
        }
    };
    View.OnClickListener DiscussionNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
            editDialogUtils.setTitle("修改讨论组名称");
            editDialogUtils.setConfirm("确定", new DialogEditInterface() {
                @Override
                public void callBack(final String message) {
                    if (!TextUtils.isEmpty(message)) {
                        mName.setText(message);
                        RongIM.getInstance().setDiscussionName(discussionId, message, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                discussion.setName(message);
                                RongIM.getInstance().refreshDiscussionCache(discussion);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }
    };
    View.OnClickListener CardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, TwoCodeActivity.class);
            intent.putExtra("city", "");
            intent.putExtra("userName", name);
            intent.putExtra("avatar", "");
            intent.putExtra("type", ConstantNum.DISCUSSION_TYPE);
            intent.putExtra("code", discussionId);
            startActivity(intent);
        }
    };
    View.OnClickListener MemberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DiscussionMemberActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("discussion", discussion);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    CallBackPositionInterface RecyclerViewListener = new CallBackPositionInterface() {
        @Override
        public void callBack(int position) {
            Log.e("点击的条目", position + "");
            String rid = memberIdList.get(position);
            if (isCreator && position == 0) {
                Intent intent = new Intent(mContext, FriendFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("memberIdList", (ArrayList<String>) memberIdList);
                bundle.putString("from", "addFriendToDiscussion");//
                bundle.putString("discussionId", discussionId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            } else if (isCreator && position == 1) {
                Intent intent = new Intent(mContext, FriendFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("memberIdList", (ArrayList<String>) memberIdList);
                bundle.putString("from", "deleteFriendFromDiscussion");//
                bundle.putString("discussionId", discussionId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            } else if (!isCreator && position == 0) {
                Intent intent = new Intent(mContext, FriendFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("memberIdList", (ArrayList<String>) memberIdList);
                bundle.putString("from", "addFriendToDiscussion");//
                bundle.putString("discussionId", discussionId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            } else {

                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                intent.putExtra("rid", rid);
                startActivity(intent);
            }

        }
    };

    private void isSmsStop() {
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.DISCUSSION, discussionId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {//免打扰
                    mSmsStop.setImageResource(R.drawable.btn_on);
                    isSmsStop = true;
                } else {//不免打扰
                    mSmsStop.setImageResource(R.drawable.btn_off);
                    isSmsStop = false;
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void dealDiscussion() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        String url;
        if (isSave) {
            url = Url.SAVE_DISCUSSION;
        } else {
            url = Url.REMOVE_DISCUSSION;
        }
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("groupId", discussionId);
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        mHttp.post(url, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //获取讨论组的信息
        RongIM.getInstance().getDiscussion(discussionId, new RongIMClient.ResultCallback<Discussion>() {
            @Override
            public void onSuccess(Discussion discussio) {
                discussion = discussio;
                addData();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 662 && resultCode == 666) {
            finish();
        }
    }
}
