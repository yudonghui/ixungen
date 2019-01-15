package com.ruihuo.ixungen.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.VideoBaseBean;
import com.ruihuo.ixungen.view.MovieCardView;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/21
 * @describe May the Buddha bless bug-free!!!
 */
public class MovieGvAdapter extends BaseAdapter {
    List<VideoBaseBean> videoList;

    public MovieGvAdapter(List<VideoBaseBean> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView==null){
            convertView=View.inflate(parent.getContext(), R.layout.item_gv_movie,null);
            mViewHolder=new ViewHolder();
            mViewHolder.mCardView= (MovieCardView) convertView.findViewById(R.id.movieCardView);
            convertView.setTag(mViewHolder);
        }else mViewHolder= (ViewHolder) convertView.getTag();
        VideoBaseBean videoBean = videoList.get(position);
        String type = videoBean.getType();//0 普通，1花絮，2预告
        String title = videoBean.getTitle();//视频标题
        String info = videoBean.getInfo();//富文本介绍
        String img = videoBean.getImg();//封面图片
        String videoId = videoBean.getId();
        String url = videoBean.getUrl();//视频播放地址
        String state = videoBean.getState();//0,普通  1，火热
        mViewHolder.mCardView.setBg(img);
        mViewHolder.mCardView.setName(title);
        mViewHolder.mCardView.setRemark(info);
        if (!TextUtils.isEmpty(state))
            mViewHolder.mCardView.setRightTop("0".equals(state) ? "普通" : "火热");
        return convertView;
    }
    class ViewHolder{
        MovieCardView mCardView;
    }
}
