package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.activity.home.XWFormDetailActivity;
import com.ruihuo.ixungen.entity.HotNewsEntity;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtils;
import com.ruihuo.ixungen.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/25
 * @describe May the Buddha bless bug-free!!!
 */
public class XWFormAdapter extends BaseAdapter {
    List<HotNewsEntity.DataBean> articleList;
    Context mContext;

    public XWFormAdapter(List<HotNewsEntity.DataBean> articleList, Context mContext) {
        this.articleList = articleList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold mViewHold;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_xw_form, null);
            mViewHold = new ViewHold();
            mViewHold.mAvatar = (ImageView) convertView.findViewById(R.id.avatar);
            mViewHold.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHold.mDate = (TextView) convertView.findViewById(R.id.date);
            mViewHold.mTitle = (TextView) convertView.findViewById(R.id.title);
            mViewHold.mContent = (TextView) convertView.findViewById(R.id.content);
            mViewHold.mGridView = (MyGridView) convertView.findViewById(R.id.gridView);
            mViewHold.mRemark = (TextView) convertView.findViewById(R.id.remark);
            mViewHold.mCity = (TextView) convertView.findViewById(R.id.city);
            convertView.setTag(mViewHold);
        } else mViewHold = (ViewHold) convertView.getTag();
        final HotNewsEntity.DataBean dataBean = articleList.get(position);
        String nikename = dataBean.getNikename();
        String create_time = dataBean.getCreate_time();
        String title = dataBean.getTitle();
        String avatar = dataBean.getAvatar();
        String summary = dataBean.getSummary();
        String content_imgs = dataBean.getContent_imgs();
        String city = dataBean.getCity();
        String count = dataBean.getCount();//阅读次数
        String reply_number = dataBean.getReply_number();//回复次数
        mViewHold.mName.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
        mViewHold.mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        if (!TextUtils.isEmpty(summary))
            setMaxEcplise(mViewHold.mContent, 2, summary);//只显示两行，多的省略号
        else mViewHold.mContent.setText("");
        //mViewHold.mContent.setText(TextUtils.isEmpty(summary) ? "" : summary);
        mViewHold.mDate.setText(DateFormatUtils.longToDate(Long.parseLong(create_time + "000")));
        mViewHold.mCity.setText(TextUtils.isEmpty(city) ? "" : city);
        mViewHold.mRemark.setText("回复 " + (TextUtils.isEmpty(reply_number) ? "" : reply_number)
                + " · " + "阅读 " + (TextUtils.isEmpty(count) ? "" : count));
        HttpUtils.display(mViewHold.mAvatar, avatar);
        if (!TextUtils.isEmpty(content_imgs)) {
            String[] urlArray = content_imgs.split("\\;");
            ArrayList<String> imgUrlList = new ArrayList<>();
            for (int i = 0; i < urlArray.length; i++) {
                if (!TextUtils.isEmpty(urlArray[i])) imgUrlList.add(urlArray[i]);
            }
            ImageAdapter imageAdapter = new ImageAdapter(imgUrlList, mContext);
            mViewHold.mGridView.setAdapter(imageAdapter);
        }else {
            mViewHold.mGridView.setAdapter(new ImageAdapter(null,mContext));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id1 = dataBean.getId();
                Intent intent = new Intent(mContext, XWFormDetailActivity.class);
                intent.putExtra("id", id1);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHold {
        private ImageView mAvatar;
        private TextView mName;
        private TextView mDate;
        private TextView mTitle;
        private TextView mContent;
        private MyGridView mGridView;
        private TextView mCity;
        private TextView mRemark;
    }

    /**
     * 参数：maxLines 要限制的最大行数
     * 参数：content  指TextView中要显示的内容
     */
    public void setMaxEcplise(final TextView mTextView, final int maxLines, final String content) {
        mTextView.setText(content);
        if (mTextView.getLineCount() > maxLines) {
            int lineEndIndex = mTextView.getLayout().getLineEnd(maxLines - 1);
            //下面这句代码中：我在项目中用数字3发现效果不好，改成1了
            String text = content.subSequence(0, lineEndIndex - 1) + "...";
            mTextView.setText(text);
        }
    }
}
