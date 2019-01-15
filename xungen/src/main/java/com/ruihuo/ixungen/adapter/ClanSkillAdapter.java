package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.entity.ClanSkillBean;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ruihuo.ixungen.R.id.skill_name;

/**
 * @author yudonghui
 * @date 2017/4/13
 * @describe May the Buddha bless bug-free!!!
 */
public class ClanSkillAdapter extends BaseAdapter {
    List<ClanSkillBean.DataBean> skillList;
    Context mContext;
    int x;
    int y;
    int dp10;

    public ClanSkillAdapter(List<ClanSkillBean.DataBean> skillList, Context mContext) {
        this.skillList = skillList;
        this.mContext = mContext;
        x = DisplayUtilX.getDisplayWidth();
        y = DisplayUtilX.getDisplayHeight();
        dp10 = DisplayUtilX.dip2px(8);
    }

    @Override
    public int getCount() {
        return skillList.size();
    }

    @Override
    public Object getItem(int position) {
        return skillList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_clanskill, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mImg_skill_photo = (ImageView) convertView.findViewById(R.id.img_skill_photo);
            mViewHolder.mSkill_name = (TextView) convertView.findViewById(skill_name);
            mViewHolder.mSkill_clan = (TextView) convertView.findViewById(R.id.skill_clan);
            mViewHolder.mSkill_time = (TextView) convertView.findViewById(R.id.skill_time);
            mViewHolder.mLlText = (LinearLayout) convertView.findViewById(R.id.ll_text);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        int a = x - (2 * dp10);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(a, a / 2);
        mViewHolder.mImg_skill_photo.setLayoutParams(params);
        mViewHolder.mImg_skill_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //mViewHolder.mLlText.setLayoutParams(params);
        ClanSkillBean.DataBean dataBean = skillList.get(position);

        String name = dataBean.getTitle();
        mViewHolder.mSkill_name.setText(TextUtils.isEmpty(name) ? "" : name);
        String nikename = dataBean.getNikename();
        mViewHolder.mSkill_clan.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
        String creat_time = dataBean.getCreate_time();
        if (!TextUtils.isEmpty(creat_time)) {
            String time = DateFormatUtils.longToDateM(creat_time);
            mViewHolder.mSkill_time.setText(time);
        }
        if (!TextUtils.isEmpty(dataBean.getPic())) {
            Picasso.with(mContext)
                    .load(dataBean.getPic())
                    .centerCrop()
                    .resize(a, a / 2)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.default_skill)
                    .placeholder(R.mipmap.default_skill)
                    .into(mViewHolder.mImg_skill_photo);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mImg_skill_photo;
        TextView mSkill_name;
        TextView mSkill_clan;
        TextView mSkill_time;
        LinearLayout mLlText;
    }
}
