package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class JoinDiscussionActivity extends AppCompatActivity {

    private String id;//讨论组的ID
    private CompositionAvatarView mAvatar;
    private TextView mName;
    private TextView mNumber;
    private TextView mConfirm;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_discussion);
        mContext = this;
        id = getIntent().getStringExtra("id");
        initView();
        initData();
    }

    private void initView() {
        mAvatar = (CompositionAvatarView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mNumber = (TextView) findViewById(R.id.number);
        mConfirm = (TextView) findViewById(R.id.confirm);
    }

    private void initData() {
        RongIM.getInstance().getDiscussion(id, new RongIMClient.ResultCallback<Discussion>() {
            @Override
            public void onSuccess(Discussion discussion) {
                String name = discussion.getName();
                List<String> memberIdList = discussion.getMemberIdList();
                for (int i = 0; i < memberIdList.size(); i++) {
                    String rid = memberIdList.get(i);
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
                    Uri portraitUri = userInfo.getPortraitUri();
                    new DownloadImageTask(CallBackListener).execute(portraitUri.toString());
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    DownloadImageTask.InterfaceDrawable CallBackListener = new DownloadImageTask.InterfaceDrawable() {
        @Override
        public void callBack(Drawable drawable) {
            mAvatar.addDrawable(drawable);
        }
    };
}
