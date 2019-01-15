package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/30
 * @describe May the Buddha bless bug-free!!!
 */
public class ServiceView1 extends LinearLayout {
    private Context mContext;
    private View inflate;

    public ServiceView1(Context context) {
        super(context);
        mContext = context;
        inflate = View.inflate(context, R.layout.service1, this);
        initView();
    }

    public ServiceView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate = View.inflate(context, R.layout.service1, this);
        initView();
    }

    public ServiceView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate = View.inflate(context, R.layout.service1, this);
        initView();
    }

    private GridView mGridView;
    private List<String> dataList = new ArrayList<>();
    private GridViewAdapter mAdapter;

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new GridViewAdapter();
        mGridView.setAdapter(mAdapter);
    }

    private ServiceView.SelectorService mListener;

    public void setListener(ServiceView.SelectorService mListener) {
        this.mListener = mListener;
    }

    public void setData(int type) {
        if (type == 1) {//餐厅
            dataList.clear();
            dataList.add("WIFI");
            dataList.add("停车场");
            dataList.add("沙发位");
            dataList.add("无烟区");
            dataList.add("包间");
            dataList.add("儿童区");
            dataList.add("表演");
            dataList.add("刷卡");
            dataList.add("宝宝骑");

        } else if (type == 2) {//酒店
            dataList.clear();
            dataList.add("WIFI");
            dataList.add("停车场");
            dataList.add("宽带");
            dataList.add("餐厅");
            dataList.add("行李寄存");
            dataList.add("租车服务");
            dataList.add("叫醒服务");
            dataList.add("送餐服务");
            dataList.add("洗衣服务");
        } else {//旅游
            dataList.add("餐厅");
            dataList.add("停车场");
            dataList.add("表演");
        }
        mAdapter.notifyDataSetChanged();
    }

    private HashMap<String, String> mapService = new HashMap<>();

    public void setService(String service) {
        if (!TextUtils.isEmpty(service)) {
            mapService.clear();
            dataList.clear();
            String[] split = service.split("\\,");
            for (int i = 0; i < split.length; i++) {
                dataList.add(split[i]);
                mapService.put(split[i], split[i]);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    class GridViewAdapter extends BaseAdapter {

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
            convertView = View.inflate(mContext, R.layout.item_service, null);
            final ImageView mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView mTextView = (TextView) convertView.findViewById(R.id.textView);
            final String text = dataList.get(position);
            mTextView.setText(TextUtils.isEmpty(text) ? "--" : text);
            if (mapService.containsKey(text)) mImageView.setImageResource(R.mipmap.green);
            else mImageView.setImageResource(R.mipmap.gray);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mapService.containsKey(text)) {
                        mapService.remove(text);
                        mImageView.setImageResource(R.mipmap.gray);
                    } else {
                        mapService.put(text, text);
                        mImageView.setImageResource(R.mipmap.green);
                    }
                    mListener.callBack(mapService);
                }
            });
            return convertView;
        }
    }
}
