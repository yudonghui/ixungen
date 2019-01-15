package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.RecommendData;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.utils.DisplayUtilX;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/11
 * @describe May the Buddha bless bug-free!!!
 */
public class RecommendStemmaAdapter extends RecyclerView.Adapter<RecommendStemmaAdapter.ViewHolder> {
    private Context mContext;
    private int width;
    private List<RecommendData.DataBean.StemmaBean> stemmaList;

    public RecommendStemmaAdapter(Context mContext, List<RecommendData.DataBean.StemmaBean> stemmaList) {
        this.mContext = mContext;
        this.stemmaList = stemmaList;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        width = (displayWidth) / 4;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(mContext, R.layout.item_clan_gv, null);
        inflate.setOnClickListener(ItemListener);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        RecommendData.DataBean.StemmaBean stemmaBean = stemmaList.get(position);
        holder.setData(stemmaBean);
    }

    @Override
    public int getItemCount() {
        return stemmaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mBook;
        private TextView mName;
        private TextView mGeneration;

        public ViewHolder(View itemView) {
            super(itemView);
            mBook = (ImageView) itemView.findViewById(R.id.book);
            mName = (TextView) itemView.findViewById(R.id.name);
            mGeneration = (TextView) itemView.findViewById(R.id.generation);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
            mBook.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            mName.setLayoutParams(layoutParams1);
            mGeneration.setLayoutParams(layoutParams1);
        }

        public void setData(RecommendData.DataBean.StemmaBean stemmaBean) {
            String name = stemmaBean.getName();
            String generation = stemmaBean.getGeneration();
            mName.setText("《" + (TextUtils.isEmpty(name) ? "--" : name) + "》");
            mGeneration.setText("辈分: " + (TextUtils.isEmpty(generation) ? "--" : generation));

        }
    }

    View.OnClickListener ItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            RecommendData.DataBean.StemmaBean stemmaBean = stemmaList.get(position);
            String id = stemmaBean.getId();
            Intent intent = new Intent(mContext, StemmaDetailActivity.class);
            intent.putExtra("stemmaId", id);
            intent.putExtra("flag", true);
            mContext.startActivity(intent);
        }
    };
}
