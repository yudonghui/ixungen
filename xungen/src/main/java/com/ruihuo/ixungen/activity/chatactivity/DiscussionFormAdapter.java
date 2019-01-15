package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.DownloadImageTask;
import com.ruihuo.ixungen.view.CompositionAvatarView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.UserInfo;

/**
 * @author yudonghui
 * @date 2017/9/8
 * @describe May the Buddha bless bug-free!!!
 */
public class DiscussionFormAdapter extends RecyclerView.Adapter<DiscussionFormAdapter.ViewHolder> {
    List<DiscussionFormBean.DataBean> dataList;
    private Context mContext;

    public DiscussionFormAdapter(List<DiscussionFormBean.DataBean> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public DiscussionFormAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discussion_form, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(dataList.get(position));
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CompositionAvatarView mAvatar;
        private TextView mName;
        private TextView mDelete;
        private TextView mNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            mAvatar = (CompositionAvatarView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mDelete = (TextView) itemView.findViewById(R.id.delete);
            mNumber = (TextView) itemView.findViewById(R.id.number);
        }

        public void setData(DiscussionFormBean.DataBean dataBean) {
            String group_id = dataBean.getGroup_id();
            mAvatar.clearDrawable();
            RongIM.getInstance().getDiscussion(group_id, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {
                    String name = discussion.getName();
                    mName.setText(TextUtils.isEmpty(name) ? "" : name);
                    List<String> memberIdList = discussion.getMemberIdList();
                    mNumber.setText("(" + memberIdList.size() + ")");
                    for (int i = 0; i < memberIdList.size(); i++) {
                        final String rid = memberIdList.get(i);
                        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
                        if (userInfo != null) {
                            Uri portraitUri = userInfo.getPortraitUri();
                            new DownloadImageTask(new DownloadImageTask.InterfaceDrawable() {

                                @Override
                                public void callBack(Drawable drawable) {
                                    if (drawable == null)
                                        drawable = mContext.getResources().getDrawable(R.mipmap.default_header);
                                    mAvatar.addDrawable(Integer.parseInt(rid),drawable);
                                }
                            }).execute(portraitUri.toString());
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}
