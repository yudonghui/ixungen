package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.DownloadImageTask;
import com.ruihuo.ixungen.view.CompositionAvatarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.BaseAdapter;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.UserInfo;

/**
 * @author yudonghui
 * @date 2017/9/8
 * @describe May the Buddha bless bug-free!!!
 */
public class TestAdapter extends BaseAdapter<UIConversation> {
    private static final String TAG = "ConversationListAdapter";
    LayoutInflater mInflater;
    Context mContext;
    private ConversationListAdapter.OnPortraitItemClick mOnPortraitItemClick;

    public long getItemId(int position) {
        UIConversation conversation = (UIConversation) this.getItem(position);
        return conversation == null ? 0L : (long) conversation.hashCode();
    }

    public TestAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public int findGatheredItem(Conversation.ConversationType type) {
        int index = this.getCount();
        int position = -1;

        while (index-- > 0) {
            UIConversation uiConversation = (UIConversation) this.getItem(index);
            if (uiConversation.getConversationType().equals(type)) {
                position = index;
                break;
            }
        }

        return position;
    }

    public int findPosition(Conversation.ConversationType type, String targetId) {
        int index = this.getCount();
        int position = -1;

        while (index-- > 0) {
            if (((UIConversation) this.getItem(index)).getConversationType().equals(type) && ((UIConversation) this.getItem(index)).getConversationTargetId().equals(targetId)) {
                position = index;
                break;
            }
        }

        return position;
    }

    protected View newView(Context context, int position, ViewGroup group) {
        View result = this.mInflater.inflate(io.rong.imkit.R.layout.rc_item_conversation, (ViewGroup) null);
        TestAdapter.ViewHolder holder = new TestAdapter.ViewHolder();
        holder.layout = this.findViewById(result, io.rong.imkit.R.id.rc_item_conversation);
        holder.leftImageLayout = this.findViewById(result, io.rong.imkit.R.id.rc_item1);
        holder.mDiscussionAvatar = this.findViewById(result, io.rong.imkit.R.id.self_avatar);
        holder.rightImageLayout = this.findViewById(result, io.rong.imkit.R.id.rc_item2);
        holder.leftImageView = (AsyncImageView) this.findViewById(result, io.rong.imkit.R.id.rc_left);
        holder.rightImageView = (AsyncImageView) this.findViewById(result, io.rong.imkit.R.id.rc_right);
        holder.contentView = (ProviderContainerView) this.findViewById(result, io.rong.imkit.R.id.rc_content);
        holder.unReadMsgCount = (TextView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message);
        holder.unReadMsgCountRight = (TextView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message_right);
        holder.unReadMsgCountIcon = (ImageView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message_icon);
        holder.unReadMsgCountRightIcon = (ImageView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message_icon_right);
        result.setTag(holder);
        return result;
    }

    private HashMap<String, List<String>> map = new HashMap<>();//讨论组头像反复加载的解决
    private HashMap<String, HashMap<String, Drawable>> avatarMap = new HashMap<>();
    private HashMap<String, Drawable> drawables = new HashMap<>();

    protected void bindView(View v, int position, final UIConversation data) {
        if (data != null) {
            if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION))
                data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);
        }
        final TestAdapter.ViewHolder holder = (TestAdapter.ViewHolder) v.getTag();
        if (data != null) {
            IContainerItemProvider.ConversationProvider provider = RongContext.getInstance().getConversationTemplate(data.getConversationType().getName());
            if (provider == null) {
                RLog.e("TestAdapter", "provider is null");
            } else {
                View view = holder.contentView.inflate(provider);
                provider.bindView(view, position, data);
                if (data.isTop()) {
                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(io.rong.imkit.R.drawable.rc_item_top_list_selector));
                } else {
                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(io.rong.imkit.R.drawable.rc_item_list_selector));
                }

                ConversationProviderTag tag = RongContext.getInstance().getConversationProviderTag(data.getConversationType().getName());
                final boolean defaultId = false;
                int defaultId1;
                if (tag.portraitPosition() == 1) {
                    holder.leftImageLayout.setVisibility(View.VISIBLE);
                    if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_group_portrait;
                    } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
                    } else {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_portrait;
                    }

                    holder.leftImageLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (TestAdapter.this.mOnPortraitItemClick != null) {
                                TestAdapter.this.mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.leftImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            if (TestAdapter.this.mOnPortraitItemClick != null) {
                                TestAdapter.this.mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }

                            return true;
                        }
                    });
                    Log.e("会话列表执行次数", position + "");
                    if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                        holder.mDiscussionAvatar.setVisibility(View.VISIBLE);
                        holder.mDiscussionAvatar.clearDrawable();
                        holder.leftImageView.setVisibility(View.GONE);
                        final String discussionId = data.getConversationTargetId();
                        RongIM.getInstance().getDiscussion(discussionId, new RongIMClient.ResultCallback<Discussion>() {
                            @Override
                            public void onSuccess(Discussion discussion) {
                                List<String> memberIdList = discussion.getMemberIdList();
                                if (!map.containsKey(discussionId)) {//如果不存在这个讨论组就添加进去
                                    map.put(discussionId, memberIdList);
                                    addAvatar(discussionId, memberIdList, holder.mDiscussionAvatar);
                                } else {
                                    List<String> mList = map.get(discussionId);//存储的讨论组人员
                                    if (mList != null && memberIdList != null) {
                                        if (mList.size() == memberIdList.size()) {//存的讨论组的人数和当前的一样
                                            for (int i = 0; i < mList.size(); i++) {
                                                if (!memberIdList.contains(mList.get(i))) {//发现有人员不同就去更新头像
                                                    map.put(discussionId, memberIdList);
                                                    addAvatar(discussionId, memberIdList, holder.mDiscussionAvatar);
                                                    break;
                                                }
                                                HashMap<String, Drawable> drawableMap = avatarMap.get(discussionId);
                                                if (drawableMap!=null){
                                                    for (Map.Entry<String, Drawable> entry : drawableMap.entrySet()) {
                                                        holder.mDiscussionAvatar.addDrawable(Integer.parseInt(entry.getKey()), entry.getValue());
                                                    }
                                                }
                                            }
                                        } else {//存的改讨论组的成员数量和当前的数量已经不一致。这个时候以当前的为准。更新头像
                                            map.put(discussionId, memberIdList);
                                            addAvatar(discussionId, memberIdList, holder.mDiscussionAvatar);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    } else {
                        holder.mDiscussionAvatar.clearDrawable();
                        holder.mDiscussionAvatar.setVisibility(View.GONE);
                        holder.leftImageView.setVisibility(View.VISIBLE);
                        if (data.getConversationGatherState()) {
                            holder.leftImageView.setAvatar((String) null, defaultId1);
                        } else if (data.getIconUrl() != null) {
                            holder.leftImageView.setAvatar(data.getIconUrl().toString(), defaultId1);
                        } else {
                            holder.leftImageView.setAvatar((String) null, defaultId1);
                        }
                    }
                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountIcon.setVisibility(View.VISIBLE);
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCount.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCount.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCount.setVisibility(View.VISIBLE);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(View.GONE);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_list_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(View.GONE);
                        holder.unReadMsgCount.setVisibility(View.GONE);
                    }

                    holder.rightImageLayout.setVisibility(View.GONE);
                } else if (tag.portraitPosition() == 2) {
                    holder.rightImageLayout.setVisibility(View.VISIBLE);
                    holder.rightImageLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (TestAdapter.this.mOnPortraitItemClick != null) {
                                TestAdapter.this.mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.rightImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            if (TestAdapter.this.mOnPortraitItemClick != null) {
                                TestAdapter.this.mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }

                            return true;
                        }
                    });
                    if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_group_portrait;
                    } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
                    } else {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_portrait;
                    }

                    if (data.getConversationGatherState()) {
                        holder.rightImageView.setAvatar((String) null, defaultId1);
                    } else if (data.getIconUrl() != null) {
                        holder.rightImageView.setAvatar(data.getIconUrl().toString(), defaultId1);
                    } else {
                        holder.rightImageView.setAvatar((String) null, defaultId1);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountRightIcon.setVisibility(View.VISIBLE);
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            holder.unReadMsgCount.setVisibility(View.VISIBLE);
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCountRight.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCountRight.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(View.GONE);
                            holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_without_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(View.GONE);
                        holder.unReadMsgCount.setVisibility(View.GONE);
                    }

                    holder.leftImageLayout.setVisibility(View.GONE);
                } else {
                    if (tag.portraitPosition() != 3) {
                        throw new IllegalArgumentException("the portrait position is wrong!");
                    }

                    holder.rightImageLayout.setVisibility(View.GONE);
                    holder.leftImageLayout.setVisibility(View.GONE);
                }

            }
        }
    }


    private void addAvatar(final String discussionId, final List<String> memberIdList, final CompositionAvatarView mAvatar) {
       /* drawables.clear();
        mAvatar.setVisibility(View.VISIBLE);*/
        mAvatar.clearDrawable();
        final int is = memberIdList.size() > 5 ? 5 : memberIdList.size();
        for (int i = 0; i < is; i++) {
            final String rid = memberIdList.get(i);
            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
            if (userInfo != null) {
                Uri portraitUri = userInfo.getPortraitUri();
                new DownloadImageTask(new DownloadImageTask.InterfaceDrawable() {

                    @Override
                    public void callBack(Drawable drawable) {
                        if (drawable == null)
                            drawable = mContext.getResources().getDrawable(R.mipmap.default_header);
                        mAvatar.addDrawable(Integer.parseInt(rid), drawable);
                        HashMap<String, Drawable> drawableHashMap;
                        if (avatarMap.containsKey(discussionId)) {
                            drawableHashMap = avatarMap.get(discussionId);
                        } else {
                            drawableHashMap = new HashMap<String, Drawable>();
                        }
                        drawableHashMap.put(rid, drawable);
                        avatarMap.put(discussionId, drawableHashMap);//将该讨论组前五个drawable存起来
                        Log.e("结果", "讨论组ID" + discussionId + "讨论组人数" + drawableHashMap.size() + "讨论组具体人的ID" + rid);
                    }
                }).execute(portraitUri.toString());
            }
        }
    }

    public void setOnPortraitItemClick(ConversationListAdapter.OnPortraitItemClick onPortraitItemClick) {
        this.mOnPortraitItemClick = onPortraitItemClick;
    }

    public interface OnPortraitItemClick {
        void onPortraitItemClick(View var1, UIConversation var2);

        boolean onPortraitItemLongClick(View var1, UIConversation var2);
    }

    class ViewHolder {
        View layout;
        View leftImageLayout;
        View rightImageLayout;
        AsyncImageView leftImageView;
        TextView unReadMsgCount;
        ImageView unReadMsgCountIcon;
        AsyncImageView rightImageView;
        TextView unReadMsgCountRight;
        ImageView unReadMsgCountRightIcon;
        ProviderContainerView contentView;
        CompositionAvatarView mDiscussionAvatar;

        ViewHolder() {
        }
    }
}
