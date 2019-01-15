package com.ruihuo.ixungen.ui.familytree.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.ui.main.bean.BaseClanFormBean;
import com.ruihuo.ixungen.utils.DateFormatUtils;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeFormAdapter extends BaseAdapter {
    List<BaseClanFormBean> dataList;
    Context mContext;

    public TreeFormAdapter(List<BaseClanFormBean> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_clan_form, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mCover = (ImageView) convertView.findViewById(R.id.cover);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
            mViewHolder.mAddress = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();
        BaseClanFormBean dataBean = dataList.get(position);
        String stemma_generation = dataBean.getStemma_generation();//族谱的辈分
        String region_name = dataBean.getRegion_name();//族谱的地址
        String stemma_name = dataBean.getStemma_name();//家谱名字
        String generation = dataBean.getGeneration();//家谱的辈分。
        String stemma_id = dataBean.getStemma_id();
        String region = dataBean.getRegion();
        String name = dataBean.getName();
        String sex = dataBean.getSex();
        String create_time = dataBean.getCreate_time();
        String address_name = dataBean.getAddress_name();
        String surname = dataBean.getSurname();
        if (!TextUtils.isEmpty(stemma_id)) {//只有族谱才有这个字段
            mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            if (TextUtils.isEmpty(stemma_generation)) {
                mViewHolder.mTime.setText("字    辈: " + (TextUtils.isEmpty(generation) ? "--" : generation));
            } else {
                mViewHolder.mTime.setText("字    辈: " + stemma_generation);
            }
            if (TextUtils.isEmpty(stemma_name)) {
                if (!TextUtils.isEmpty(region)) {
                    new NetWorkData().getAddress(mContext, region, new NetWorkData.AddressInterface() {
                        @Override
                        public void callBack(String result) {
                            mViewHolder.mAddress.setText("来源于: " + (TextUtils.isEmpty(result) ? "--" : result));
                        }
                    });
                } else {
                    mViewHolder.mAddress.setText("来源于: --");
                }
            } else {
                mViewHolder.mAddress.setText("来源于: " + (TextUtils.isEmpty(stemma_name) ? "--" : stemma_name));
            }

            mViewHolder.mCover.setImageResource(R.mipmap.system_book);
        } else {
            mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            if (TextUtils.isEmpty(generation)) {
                if (TextUtils.isEmpty(sex)) {
                    mViewHolder.mTime.setText("创建时间: " + DateFormatUtils.longToDateM(create_time));
                } else {
                    if ("1".equals(sex))
                        mViewHolder.mTime.setText("性    别: 男");
                    else if ("2".equals(sex))
                        mViewHolder.mTime.setText("性    别: 女");
                    else mViewHolder.mTime.setText("性    别: 未知");
                }
            } else
                mViewHolder.mTime.setText("字    辈: " + generation);

            if (TextUtils.isEmpty(region_name)) {
                if (TextUtils.isEmpty(region)) {
                    mViewHolder.mAddress.setText("现居地: " + (TextUtils.isEmpty(address_name) ? "--" : address_name));
                } else
                    mViewHolder.mAddress.setText("来源于: " + region);
            } else mViewHolder.mAddress.setText("来源于: " + region_name);
            mViewHolder.mCover.setImageResource(R.mipmap.family_book);
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView mCover;
        private TextView mName;
        private TextView mTime;
        private TextView mAddress;
    }
}
