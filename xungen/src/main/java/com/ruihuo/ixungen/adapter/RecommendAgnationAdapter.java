package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.useractivity.AgnationNewActivity;
import com.ruihuo.ixungen.entity.RecommendData;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.CircleImageView;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/11
 * @describe May the Buddha bless bug-free!!!
 */
public class RecommendAgnationAdapter extends RecyclerView.Adapter<RecommendAgnationAdapter.ViewHolder> {
    private Context mContext;
    private List<RecommendData.DataBean.ClanAssociationBean> agnationList;

    public RecommendAgnationAdapter(Context mContext, List<RecommendData.DataBean.ClanAssociationBean> agnationList) {
        this.mContext = mContext;
        this.agnationList = agnationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(mContext, R.layout.item_recommend_agnation, null);
        inflate.setOnClickListener(ItemListener);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        RecommendData.DataBean.ClanAssociationBean clanAssociationBean = agnationList.get(position);
        holder.setData(clanAssociationBean);
    }

    @Override
    public int getItemCount() {
        return agnationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mHeader;
        private TextView mName;
        private TextView mNumber;
        private TextView mAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            mHeader = (CircleImageView) itemView.findViewById(R.id.header);
            mName = (TextView) itemView.findViewById(R.id.name);
            mNumber = (TextView) itemView.findViewById(R.id.number);
            mAdd = (TextView) itemView.findViewById(R.id.add);
        }

        public void setData(RecommendData.DataBean.ClanAssociationBean clanAssociationBean) {
            final String id = clanAssociationBean.getId();
            String name = clanAssociationBean.getName();
            String count = clanAssociationBean.getCount();
            String img_url = clanAssociationBean.getImg_url();
            mName.setText(TextUtils.isEmpty(name) ? "" : name);
            mNumber.setText(TextUtils.isEmpty(count) ? "0" : count + "位成员");
            PicassoUtils.setImgView(img_url, mContext, R.mipmap.recommend_agnation_default, mHeader);
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = mAdd.getText().toString();
                    if (!"+ 加入".equals(text)) return;
                    final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("associationId", id);
                    params.putString("userRid", XunGenApp.rid);
                    mHttp.post(Url.JOIN_AGNATION_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                            ToastUtils.toast(mContext, "发送请求成功，等待会长确定");
                            mAdd.setText("待审核");
                            mAdd.setTextColor(mContext.getResources().getColor(R.color.gray_txt));
                            mAdd.setBackgroundResource(0);
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                }
            });
        }
    }

    View.OnClickListener ItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            RecommendData.DataBean.ClanAssociationBean clanAssociationBean = agnationList.get(position);
            String id = clanAssociationBean.getId();
            //Intent intent = new Intent(mContext, AgnationActivity.class);
            Intent intent = new Intent(mContext, AgnationNewActivity.class);
            intent.putExtra("id", id);
            mContext.startActivity(intent);
        }
    };
}
