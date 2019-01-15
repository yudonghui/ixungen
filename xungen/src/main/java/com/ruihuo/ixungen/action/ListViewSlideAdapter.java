package com.ruihuo.ixungen.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/28
 * @describe May the Buddha bless bug-free!!!
 */
public class ListViewSlideAdapter extends RecyclerView.Adapter<ListViewSlideAdapter.ViewHolder> {
 /*   private List<VideoFormBean.DataBean> mList;
    private Context context;
    private OnClickListenerEditOrDelete onClickListenerEditOrDelete;
    boolean isCheck;
    int resize;
    public ListViewSlideAdapter(Context context, List<VideoFormBean.DataBean> mList) {
        this.mList = mList;
        this.context = context;
        int displayWidth = DisplayUtilX.getDisplayWidth(context);
        resize = displayWidth / 3;
    }
     public void setFlag(boolean isCheck){
         this.isCheck = isCheck;
     }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.item_listview, null);
            viewHolder = new ViewHolder();
           tvName = (TextView) convertView.findViewById(R.id.tvName);
           img = (ImageView) convertView.findViewById(R.id.imgLamp);
           tvTime = (TextView) convertView.findViewById(R.id.tvtime);
            viewHolder.tvTimes = (TextView) convertView.findViewById(R.id.tvtimes);
            viewHolder.tvDelete = (TextView) convertView.findViewById(R.id.delete);
            viewHolder.mFlag = (ImageView) convertView.findViewById(R.id.flag);
            convertView.setTag(viewHolder);//store up viewHolder
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isCheck) {
            viewHolder.mFlag.setVisibility(View.VISIBLE);
        } else{
            viewHolder.mFlag.setVisibility(View.GONE);
            viewHolder.mFlag.setImageResource(R.drawable.unselect);
        }
        VideoFormBean.DataBean dataBean = mList.get(position);
        viewHolder.img.setImageResource(R.mipmap.default_long);
        viewHolder.tvName.setText(dataBean.getRemark());
        viewHolder.tvTime.setText(DateFormatUtils.longToDateM(dataBean.getCreate_time()));
        viewHolder.tvTimes.setText((TextUtils.isEmpty(dataBean.getTotal_play())?"0":dataBean.getTotal_play())+"次播放");
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerEditOrDelete != null) {
                    onClickListenerEditOrDelete.OnClickListenerDelete(position);
                }
            }
        });
        String img = dataBean.getImg();
        if (!TextUtils.isEmpty(img)){
            Picasso.with(context)
                   .load(img)
                   // .load("https://res.ixungen.cn/img/9,024533026b1b")
                    .config(Bitmap.Config.RGB_565)
                    .resize(resize, resize * 2 / 3)
                    .centerCrop()
                    .placeholder(R.mipmap.default_photo)
                    .error(R.mipmap.default_photo)
                    .into(viewHolder.img);
        }
    *//*    viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerEditOrDelete!=null){
                    onClickListenerEditOrDelete.OnClickListenerEdit(position);
                }
            }
        });*//*
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvDelete, tvTime, tvTimes;
        ImageView img, mFlag;
    }

    public interface OnClickListenerEditOrDelete {
        // void OnClickListenerEdit(int position);
        void OnClickListenerDelete(int position);
    }

    public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerEditOrDelete1) {
        this.onClickListenerEditOrDelete = onClickListenerEditOrDelete1;
    }*/

    List<VideoFormBean.DataBean> dataList;
    private Context mContext;
    private final int verspace;
    private final int left;
    private int resize;
    private int type;
    boolean isCheck;

    public ListViewSlideAdapter(List<VideoFormBean.DataBean> dataList, Context mContext, int type) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.type = type;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = displayWidth * 2 / 7;
        verspace = DisplayUtilX.dip2px(6);
        left = DisplayUtilX.dip2px(10);
    }

    public void setFlag(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public ListViewSlideAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);

        return new ListViewSlideAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ListViewSlideAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(dataList.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDelete, tvTime, tvTimes;
        ImageView imageView, mFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imageView = (ImageView) itemView.findViewById(R.id.imgLamp);
            tvTime = (TextView) itemView.findViewById(R.id.tvtime);
            tvTimes = (TextView) itemView.findViewById(R.id.tvtimes);
            tvDelete = (TextView) itemView.findViewById(R.id.delete);
            mFlag = (ImageView) itemView.findViewById(R.id.flag);
        }

        public void setData(VideoFormBean.DataBean dataBean) {
            if (isCheck) {
                mFlag.setVisibility(View.VISIBLE);
            } else {
                mFlag.setVisibility(View.GONE);
                mFlag.setImageResource(R.drawable.unselect);
            }
            imageView.setImageResource(R.mipmap.default_long);
            tvName.setText(dataBean.getRemark());
            tvTime.setText(DateFormatUtils.longToDateM(dataBean.getCreate_time()));
            tvTimes.setText((TextUtils.isEmpty(dataBean.getTotal_play()) ? "0" : dataBean.getTotal_play()) + "次播放");
            String img = dataBean.getImg();
            if (!TextUtils.isEmpty(img)) {
                Picasso.with(mContext)
                        .load(img)
                        // .load("https://res.ixungen.cn/img/9,024533026b1b")
                        .config(Bitmap.Config.RGB_565)
                        .resize(resize, resize * 2 / 3)
                        .centerCrop()
                        .placeholder(R.mipmap.default_photo)
                        .error(R.mipmap.default_photo)
                        .into(imageView);
            }
        }
    }
}
