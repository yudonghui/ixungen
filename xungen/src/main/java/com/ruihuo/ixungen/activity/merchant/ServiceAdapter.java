package com.ruihuo.ixungen.activity.merchant;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;


/**
 * @author yudonghui
 * @date 2017/8/22
 * @describe May the Buddha bless bug-free!!!
 */
public class ServiceAdapter extends BaseAdapter {
    String[] split;

    public ServiceAdapter(String[] split) {
        this.split = split;
    }

    @Override
    public int getCount() {
        return split.length;
    }

    @Override
    public Object getItem(int position) {
        return split[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_service, null);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView mTextView = (TextView) convertView.findViewById(R.id.textView);
        mTextView.setText(TextUtils.isEmpty(split[position]) ? "" : split[position]);
        switch (split[position]) {
            case "WIFI":
                mImageView.setImageResource(R.mipmap.wifi);
                break;
            case "停车场":
                mImageView.setImageResource(R.mipmap.tingchechang);
                break;
            case "沙发位":
                mImageView.setImageResource(R.mipmap.shafa);
                break;
            case "宽带":
                mImageView.setImageResource(R.mipmap.internet);
                break;
            case "无烟区":
                mImageView.setImageResource(R.mipmap.wuyan);
                break;
            case "餐厅":
                mImageView.setImageResource(R.mipmap.canting);
                break;

            case "包间":
                mImageView.setImageResource(R.mipmap.baojian);
                break;
            case "行李寄存":
                mImageView.setImageResource(R.mipmap.xingli);
                break;

            case "儿童区":
                mImageView.setImageResource(R.mipmap.ertongqu);
                break;
            case "租车服务":
                mImageView.setImageResource(R.mipmap.zuche);
                break;

            case "表演":
                mImageView.setImageResource(R.mipmap.biaoyan);
                break;
            case "叫醒服务":
                mImageView.setImageResource(R.mipmap.jiaoxing);
                break;
            case "刷卡":
                mImageView.setImageResource(R.mipmap.shuaka);
                break;
            case "送餐服务":
                mImageView.setImageResource(R.mipmap.songcan);
                break;

            case "宝宝骑":
                mImageView.setImageResource(R.mipmap.baobaoyi);
                break;
            case "洗衣服务":
                mImageView.setImageResource(R.mipmap.xiyi);
                break;
        }
        return convertView;
    }
}
