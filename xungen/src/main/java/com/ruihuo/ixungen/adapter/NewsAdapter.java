package com.ruihuo.ixungen.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.ActorApplyActivity;
import com.ruihuo.ixungen.activity.MovieActorsActivity;
import com.ruihuo.ixungen.activity.MovieBaseActivity;
import com.ruihuo.ixungen.activity.PictureActivity;
import com.ruihuo.ixungen.activity.home.ClanSkillActivity;
import com.ruihuo.ixungen.activity.merchant.HotelActivity;
import com.ruihuo.ixungen.activity.merchant.NearByShopActivity;
import com.ruihuo.ixungen.activity.merchant.TravelActivity;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.entity.ScrollBaseBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class NewsAdapter extends PagerAdapter {
    List<ImageView> listImage;
    Context mContext;
    List<ScrollBaseBean> results;

    public NewsAdapter(List<ImageView> listImage, List<ScrollBaseBean> results, Context mContext) {
        this.listImage = listImage;
        this.mContext = mContext;
        this.results = results;
    }

    public void setData(List<ScrollBaseBean> results) {
        this.results.clear();
        this.results.addAll(results);
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
                if (results.size() == 0) {
                    return;
                }
                if (position == 0) return;
                ScrollBaseBean dataBean = results.get(position - 1);
                String url = dataBean.getUrl();
                String open_type = dataBean.getOpen_type();
                if (!TextUtils.isEmpty(open_type)) {
                    switch (open_type) {
                        case "0"://打开连接
                            if (dataBean.getTarget() == 0) { //内部打开连接
                                if (!TextUtils.isEmpty(url)) {
                                    Intent intent1 = new Intent(mContext, PictureActivity.class);
                                    intent1.putExtra("url", url);
                                    mContext.startActivity(intent1);
                                }
                            } else {//外部打开连接
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                intent.setData(content_url);
                                mContext.startActivity(intent);
                            }
                            break;
                        case "1"://炎黄影视
                            Intent intent1 = new Intent(mContext, MovieBaseActivity.class);
                            mContext.startActivity(intent1);
                            break;
                        case "2"://寻根旅游
                            Intent intent2 = new Intent(mContext, TravelActivity.class);
                            mContext.startActivity(intent2);
                            break;
                        case "3"://炎黄影视演员库
                            Intent intent3 = new Intent(mContext, MovieActorsActivity.class);
                            mContext.startActivity(intent3);
                            break;
                        case "4"://炎黄影视报名页
                            if (XunGenApp.isLogin) {
                                //在进入报名入口之前先网络请求判断自己是什么状态。1.没有报名，2，审核中，3通过审核，4审核不通过
                                getStatus();

                            } else {
                                IntentSkip intentSkip = new IntentSkip();
                                intentSkip.skipLogin(mContext, 211);
                            }
                            break;
                        case "5"://能工巧匠
                            Intent intent5 = new Intent(mContext, ClanSkillActivity.class);
                            mContext.startActivity(intent5);
                            break;
                        case "6"://个人空间

                            break;
                        case "7"://麻城旅游
                            Intent intent7 = new Intent(mContext, NearByShopActivity.class);
                            intent7.putExtra("type", 4);
                            mContext.startActivity(intent7);
                            break;
                        case "8"://麻城酒店
                            Intent intent8 = new Intent(mContext, NearByShopActivity.class);
                            intent8.putExtra("type", 2);
                            mContext.startActivity(intent8);
                            break;
                        case "9"://麻城美食
                            Intent intent9 = new Intent(mContext, NearByShopActivity.class);
                            intent9.putExtra("type", 1);
                            mContext.startActivity(intent9);
                            break;
                        case "10"://店铺首页
                            if (!TextUtils.isEmpty(url)) {
                                String[] split = url.split("\\&|\\=");
                                if (split.length == 4) {
                                    Intent intent = new Intent(mContext, HotelActivity.class);
                                    intent.putExtra("type", Integer.parseInt(split[1]));
                                    intent.putExtra("shopId", split[3]);
                                    mContext.startActivity(intent);
                                }
                            }
                            break;
                    }
                }
            }
        });
        return listImage.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listImage.get(position));
    }

    private void getStatus() {
        /* Intent intent = new Intent(mContext, ActorApplyActivity.class);
                startActivity(intent);*/
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.get(Url.ACTOR_APPLY_STATE, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int data = jsonObject.getInt("data");//1.没有报名，2，审核中，3通过审核，4审核不通过
                    Intent intent = new Intent(mContext, ActorApplyActivity.class);
                    intent.putExtra("state", data);
                    mContext.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });

    }
}
