package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.RecommendData;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/11
 * @describe May the Buddha bless bug-free!!!
 */
public class RecommendPersonAdapter extends BaseAdapter {


    private Context mContext;
    private List<RecommendData.DataBean.FriendBean> friendList;
    private int width;
    private NetWorkData netWorkData;
    private LinearLayout.LayoutParams layoutParams;

    public RecommendPersonAdapter(Context mContext, List<RecommendData.DataBean.FriendBean> friendList) {
        this.mContext = mContext;
        this.friendList = friendList;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        int i = DisplayUtilX.dip2px(35);
        width = (displayWidth - i) / 4;
        netWorkData = new NetWorkData();
        layoutParams = new LinearLayout.LayoutParams(width, width);
    }


    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_recommend_person, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mHeader = (XCRoundRectImageView) convertView.findViewById(R.id.header);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mNumber = (TextView) convertView.findViewById(R.id.number);
            mViewHolder.mAdd = (TextView) convertView.findViewById(R.id.add);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendData.DataBean.FriendBean friendBean = friendList.get(position);
        final String rid = friendBean.getRid();
        String nikename = friendBean.getNikename();
        String surname = friendBean.getSurname();
        String truename = friendBean.getTruename();
        String avatar = friendBean.getAvatar();
        final String region = friendBean.getRegion();
        if (TextUtils.isEmpty(truename)) {
            mViewHolder.mName.setText(TextUtils.isEmpty(nikename) ? "--" : nikename);
        } else {
            mViewHolder.mName.setText(TextUtils.isEmpty(surname) ? "" : surname + truename);
        }
        mViewHolder.mHeader.setLayoutParams(layoutParams);
        if (avatar != null) {
            Picasso.with(mContext)
                    .load(avatar)
                    .resize(width, width)
                    .centerCrop()
                    .placeholder(R.mipmap.recommend_person_default)
                    .error(R.mipmap.recommend_person_default)
                    .into(mViewHolder.mHeader);
        }
        netWorkData.getAddress(mContext, region, new NetWorkData.AddressInterface() {
            @Override
            public void callBack(String result) {
                mViewHolder.mNumber.setText(TextUtils.isEmpty(result) ? "--" : result);
            }
        });
        mViewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);

                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("toUserRid", rid);
                params.putString("nikename", XunGenApp.nikename);
                // params.putString("",);
                mHttp.post(Url.ADD_FRIEND_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        ToastUtils.toast(mContext, "发送请求成功，等待好友确定");
                        mViewHolder.mAdd.setText("待同意");
                        mViewHolder.mAdd.setTextColor(mContext.getResources().getColor(R.color.gray_txt));
                        mViewHolder.mAdd.setBackgroundResource(0);
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                intent.putExtra("rid", rid);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private XCRoundRectImageView mHeader;
        private TextView mName;
        private TextView mNumber;
        private TextView mAdd;
    }

}
