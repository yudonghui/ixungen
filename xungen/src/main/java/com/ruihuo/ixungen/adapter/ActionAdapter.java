package com.ruihuo.ixungen.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
 * @date 2017/5/24
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionAdapter extends BaseAdapter {
    List<ActionBean.DataBean> actionList;

    public ActionAdapter(List<ActionBean.DataBean> actionList) {
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
        int size = actionList.size();
        if (size == 1) {
            convertView = View.inflate(parent.getContext(), R.layout.item_home_one, null);
        } else if (size == 3) {
            convertView = View.inflate(parent.getContext(), R.layout.item_home_three, null);
        } else {
            convertView = View.inflate(parent.getContext(), R.layout.item_home_two, null);
        }
        TextView mTitle = (TextView) convertView.findViewById(R.id.action_title);
        TextView mDesc = (TextView) convertView.findViewById(R.id.action_desc);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.action_img);
        if (size == 2 || size > 3) {
            View lines = convertView.findViewById(R.id.lines);
            if (position == size - 1) {
                lines.setVisibility(View.GONE);
            } else lines.setVisibility(View.VISIBLE);
        }

        final ActionBean.DataBean actionBean = actionList.get(position);
        mTitle.setText(TextUtils.isEmpty(actionBean.getName()) ? "" : actionBean.getName());
        mDesc.setText(TextUtils.isEmpty(actionBean.getSummary()) ? "" : actionBean.getSummary());
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String imgUrl = actionBean.getImg_url();
        if (!TextUtils.isEmpty(imgUrl)) {
            Picasso.with(parent.getContext())
                    .load(imgUrl)
                    .placeholder(R.mipmap.default_long)
                    .error(R.mipmap.default_long)
                    .config(Bitmap.Config.RGB_565)
                    .into(mImageView);
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
