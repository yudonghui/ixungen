package com.ruihuo.ixungen.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.OnClickInterface;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.view.CircleImageView;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/9/21
 * @describe May the Buddha bless bug-free!!!
 */
public class NewMemeberAdapter extends BaseAdapter {
    List<FriendBean.DataBean> memeberList;
    OnClickInterface mListener;

    public NewMemeberAdapter(List<FriendBean.DataBean> memeberList, OnClickInterface mListener) {
        this.memeberList = memeberList;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return memeberList == null ? 0 : memeberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memeberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_new_memeber, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mRid = (TextView) convertView.findViewById(R.id.rid);
            mViewHolder.mRefund = (TextView) convertView.findViewById(R.id.refund);
            mViewHolder.mAgree = (TextView) convertView.findViewById(R.id.agree);
            mViewHolder.mRefunded = (TextView) convertView.findViewById(R.id.refunded);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        FriendBean.DataBean dataBean = memeberList.get(position);
        final String associationId = dataBean.getAssociation_id();
        String nikename = dataBean.getNikename();
        String rid = dataBean.getRid();
        String avatar = dataBean.getAvatar();
        final String status = dataBean.getStatus();
        mViewHolder.mName.setText(TextUtils.isEmpty(nikename) ? "--" : nikename);
        mViewHolder.mRid.setText("ID：" + (TextUtils.isEmpty(rid) ? "--" : rid));
        //头像
        if (!TextUtils.isEmpty(avatar))
            PicassoUtils.setImgView(dataBean.getAvatar(), parent.getContext(), R.mipmap.default_header, mViewHolder.mAvatar);
        if (status!=null){
            switch (status) {//0：待宗亲会会长审核；1：审核通过；2：审核不通过
                case "0":
                    mViewHolder.mRefund.setVisibility(View.VISIBLE);
                    mViewHolder.mAgree.setVisibility(View.VISIBLE);
                    mViewHolder.mRefunded.setVisibility(View.GONE);
                    break;
                case "1":
                    mViewHolder.mRefund.setVisibility(View.GONE);
                    mViewHolder.mAgree.setVisibility(View.GONE);
                    mViewHolder.mRefunded.setVisibility(View.VISIBLE);
                    mViewHolder.mRefunded.setText("已添加");
                    break;
                case "2":
                    mViewHolder.mRefund.setVisibility(View.GONE);
                    mViewHolder.mAgree.setVisibility(View.GONE);
                    mViewHolder.mRefunded.setVisibility(View.VISIBLE);
                    mViewHolder.mRefunded.setText("已拒绝");
                    break;
            }
        }
        //拒绝
        mViewHolder.mRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(mViewHolder.mRefund, position);
            }
        });
        //同意
        mViewHolder.mAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(mViewHolder.mAgree, position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private CircleImageView mAvatar;
        private TextView mName;
        private TextView mRid;
        private TextView mRefund;
        private TextView mAgree;
        private TextView mRefunded;
    }
}
