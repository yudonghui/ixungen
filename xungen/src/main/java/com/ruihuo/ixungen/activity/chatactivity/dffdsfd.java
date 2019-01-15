package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import io.rong.imkit.fragment.ConversationListFragment;

/**
 * @author yudonghui
 * @date 2017/9/8
 * @describe May the Buddha bless bug-free!!!
 */
public class dffdsfd extends ConversationListFragment {
    private LinearLayout mNotificationBar;
    private ImageView mNotificationBarImage;
    private TextView mNotificationBarText;
    private TestAdapter mAdapter;
    private ListView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(io.rong.imkit.R.layout.rc_fr_conversationlist, container, false);
        this.mNotificationBar = (LinearLayout) this.findViewById(view, io.rong.imkit.R.id.rc_status_bar);
        this.mNotificationBar.setVisibility(View.GONE);
        this.mNotificationBarImage = (ImageView) this.findViewById(view, io.rong.imkit.R.id.rc_status_bar_image);
        this.mNotificationBarText = (TextView) this.findViewById(view, io.rong.imkit.R.id.rc_status_bar_text);
        View emptyView = this.findViewById(view, io.rong.imkit.R.id.rc_conversation_list_empty_layout);
        TextView emptyText = (TextView) this.findViewById(view, io.rong.imkit.R.id.rc_empty_tv);
        emptyText.setText(this.getActivity().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
        this.mList = (ListView) this.findViewById(view, io.rong.imkit.R.id.rc_list);
        this.mList.setEmptyView(emptyView);
        this.mList.setOnItemClickListener(this);
        this.mList.setOnItemLongClickListener(this);
        if (this.mAdapter == null) {
            this.mAdapter = this.onResolveAdapter1(this.getActivity());
        }

        this.mAdapter.setOnPortraitItemClick(this);
        this.mList.setAdapter(this.mAdapter);
        return view;
    }

    public TestAdapter onResolveAdapter1(Context context) {
        mAdapter = new TestAdapter(context);
        return mAdapter;
    }
}
