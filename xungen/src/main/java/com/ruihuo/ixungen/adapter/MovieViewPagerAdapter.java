package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruihuo.ixungen.activity.PictureActivity;
import com.ruihuo.ixungen.entity.MovieBean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/19
 * @describe May the Buddha bless bug-free!!!
 */
public class MovieViewPagerAdapter extends PagerAdapter {
    List<ImageView> listImage;
    Context mContext;
    List<MovieBean.DataBean.AdsBean> adsList;

    public MovieViewPagerAdapter(List<ImageView> listImage, List<MovieBean.DataBean.AdsBean> adsList, Context mContext) {
        this.listImage = listImage;
        this.mContext = mContext;
        this.adsList = adsList;
    }

    public void setData(List<MovieBean.DataBean.AdsBean> adsList) {
        this.adsList = adsList;
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(listImage.get(position));
        listImage.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adsList.size() == 0) {
                    return;
                }
                MovieBean.DataBean.AdsBean dataBean = adsList.get(position - 1);
                String url = dataBean.getUrl();
                switch ("") {
                    //内部打开
                    case "0":
                        if (!TextUtils.isEmpty(url)) {
                            Intent intent1 = new Intent(mContext, PictureActivity.class);
                            intent1.putExtra("url", url);
                            mContext.startActivity(intent1);
                        }
                        break;
                    //外部打开
                    case "1":
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
        return listImage.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listImage.get(position));
    }
}