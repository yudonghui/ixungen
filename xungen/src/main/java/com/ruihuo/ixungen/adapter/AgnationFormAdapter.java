package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.utils.PicassoUtils;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/6
 * @describe May the Buddha bless bug-free!!!
 */
public class AgnationFormAdapter extends BaseAdapter {
    List<AgnationFormBean.DataBean> data;

    public AgnationFormAdapter(List<AgnationFormBean.DataBean> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private Context mContext;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if(convertView==null){
            mContext=parent.getContext();
            convertView=View.inflate(mContext, R.layout.item_agnation_form,null);
            mViewHolder=new ViewHolder();
            mViewHolder.mHeader= (ImageView) convertView.findViewById(R.id.image_header);
            mViewHolder.mName= (TextView) convertView.findViewById(R.id.agnation_name);
            mViewHolder.mDetail= (TextView) convertView.findViewById(R.id.agnation_detail);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        AgnationFormBean.DataBean dataBean = data.get(position);
        mViewHolder.mName.setText(dataBean.getName());
        mViewHolder.mDetail.setText("地址："+dataBean.getArea_id_str()+"·成员："+dataBean.getCount()+"人");
        String img_url = dataBean.getImg_url();
        PicassoUtils.setImgView(img_url,mContext,R.mipmap.default_header,mViewHolder.mHeader);
        return convertView;
    }
    class ViewHolder{
        ImageView mHeader;
        TextView mName;
        TextView mDetail;
    }
}
