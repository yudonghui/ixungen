package com.ruihuo.ixungen.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.MovieBaseActivity;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.activity.home.ClanSkillActivity;
import com.ruihuo.ixungen.activity.merchant.TravelActivity;
import com.ruihuo.ixungen.entity.ActionBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/8
 * @describe May the Buddha bless bug-free!!!
 */
public class Action2Adapter extends BaseAdapter {
    List<ActionBean.DataBean> actionList;

    public Action2Adapter(List<ActionBean.DataBean> actionList) {
        this.actionList = actionList;
    }

    @Override
    public int getCount() {
        return actionList.size();
    }

    @Override
    public Object getItem(int position) {
        return actionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_action2, null);
        LinearLayout mLl = convertView.findViewById(R.id.container);
        TextView mName = (TextView) convertView.findViewById(R.id.name);
        TextView mRemark = (TextView) convertView.findViewById(R.id.remark);
        ImageView mIcon = (ImageView) convertView.findViewById(R.id.icon);
        final ActionBean.DataBean actionBean = actionList.get(position);

        mLl.setBackgroundColor(Color.parseColor(actionBean.getBg_color()));
        mName.setText(TextUtils.isEmpty(actionBean.getName()) ? "" : actionBean.getName());
        mRemark.setText(TextUtils.isEmpty(actionBean.getSummary()) ? "" : actionBean.getSummary());
        mIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String imgUrl = actionBean.getImg_url();
        if (!TextUtils.isEmpty(imgUrl)) {
            Picasso.with(parent.getContext())
                    .load(imgUrl)
                    .placeholder(R.mipmap.default_long)
                    .error(R.mipmap.default_long)
                    .config(Bitmap.Config.RGB_565)
                    .into(mIcon);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = actionBean.getType();
                if (TextUtils.isEmpty(type)) return;
                /**
                 * 0  h5打开。
                 * 1 为炎黄影视
                 * 2 寻根旅游
                 * 3 炎黄影影视演员库
                 * 4 炎黄影视演员报名
                 * 5 能工巧匠
                 */
                switch (type) {
                    case "0":
                        Intent intent0 = new Intent(parent.getContext(), H5Activity.class);
                        intent0.putExtra("url", actionBean.getLink());
                        parent.getContext().startActivity(intent0);
                      /*  Intent intent0 = new Intent(parent.getContext(), TravelActivity.class);
                        parent.getContext().startActivity(intent0);*/
                        break;
                    case "1":
                        Intent intent1 = new Intent(parent.getContext(), MovieBaseActivity.class);
                        parent.getContext().startActivity(intent1);
                        break;
                    case "2":
                        Intent intent2 = new Intent(parent.getContext(), TravelActivity.class);
                        parent.getContext().startActivity(intent2);
                        break;
                    case "5":
                        Intent intent5 = new Intent(parent.getContext(), ClanSkillActivity.class);
                        parent.getContext().startActivity(intent5);
                        break;
                }

            }
        });
        return convertView;
    }
}
