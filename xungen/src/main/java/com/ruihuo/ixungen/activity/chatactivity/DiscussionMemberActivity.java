package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.adapter.DiscussionMemberAdapter;
import com.ruihuo.ixungen.adapter.SpaceItemDecoration;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Discussion;

public class DiscussionMemberActivity extends BaseNoTitleActivity {
    private Context mContext;
    private TitleBar mTitlBar;
    private SearchViewY mSearch;
    private RecyclerView mRecyclerView;
    private Discussion discussion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_member);
        mContext = this;
        discussion = getIntent().getParcelableExtra("discussion");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mTitlBar = (TitleBar) findViewById(R.id.titlBar);
        mTitlBar.setTitle("讨论组成员");
        mSearch = (SearchViewY) findViewById(R.id.search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void addListener() {
        mTitlBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String creatorId;
    private String discussionId;
    private List<String> memberIdList = new ArrayList<>();
    private boolean isCreator = false;//是否是创建者
    private DiscussionMemberAdapter mAdapter;

    private void addData() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 5);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DisplayUtilX.dip2px(5)));

        if (discussion != null) {
            memberIdList.clear();
            creatorId = discussion.getCreatorId();
            discussionId = discussion.getId();
            if (XunGenApp.rid.equals(creatorId)) {
                isCreator = true;
                memberIdList.add("add");//加
                memberIdList.add("delete");//减
            } else {
                isCreator = false;
                memberIdList.add("add");//加
            }
            memberIdList.addAll(discussion.getMemberIdList());
            String name = discussion.getName();
            mAdapter = new DiscussionMemberAdapter(mContext, memberIdList, ItemListener);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private boolean isDelete;
    CallBackPositionInterface ItemListener = new CallBackPositionInterface() {
        @Override
        public void callBack(final int position) {
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
                if (isDelete) isDelete = false;
                else isDelete = true;
                mAdapter.isDelete(isDelete);
                mAdapter.notifyDataSetChanged();
            } else if (!isCreator && position == 0) {
                Intent intent = new Intent(mContext, FriendFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("memberIdList", (ArrayList<String>) memberIdList);
                bundle.putString("from", "addFriendToDiscussion");//
                bundle.putString("discussionId", discussionId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            } else {
                if (isDelete) {
                    RongIM.getInstance().removeMemberFromDiscussion(discussionId, rid, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            memberIdList.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(mContext, "移除失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    intent.putExtra("rid", rid);
                    startActivity(intent);
                }

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 662 && resultCode == 666) {//添加新的成员回来
            if (data != null) {
                ArrayList<String> ridList = data.getStringArrayListExtra("ridList");
                memberIdList.addAll(ridList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
