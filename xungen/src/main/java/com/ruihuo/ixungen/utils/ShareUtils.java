package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.ruihuo.ixungen.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author yudonghui
 * @date 2017/4/26
 * @describe May the Buddha bless bug-free!!!
 */
public class ShareUtils {
    private Context mContext;

    public ShareUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void share(final String title, final String text, final String url) {
//        ShareSDK.initSDK(mContext);
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setShareContentCustomizeCallback(new cn.sharesdk.onekeyshare.ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (WechatMoments.NAME.equals(platform.getName())
                        || Wechat.NAME.equals(platform.getName())) {

                    paramsToShare.setTitle(title);
                    paramsToShare.setText(text);
                    paramsToShare.setUrl(url);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                } else if (QQ.NAME.equals(platform.getName()) || QZone.NAME.equals(platform.getName())) {
                    paramsToShare.setTitle(title);
                    paramsToShare.setText(text);
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setSite(mContext.getString(R.string.app_name));
                    paramsToShare.setSiteUrl("http://www.ixungen.cn/portal.php");
                } else {
                    // paramsToShare.setTitle(title + "\n" + text);
                    paramsToShare.setText(title + "\n" + text + "\n" + url);
                    // paramsToShare.setUrl(url);
                }
            }
        });
        // 启动分享GUI
        oks.show(mContext);
    }

    /**
     * @param linkUrl 链接
     * @param title   标题
     * @param text    介绍
     * @param imgUrl  图片链接
     * @param type    分享方式（0-新浪，1-微信，4-QQ）
     */
    public void shareSelf(String linkUrl, String title, String text, String imgUrl, String type) {
        switch (type) {
            case "0":
                shareWB(title, text, linkUrl, imgUrl);
                break;
            case "1":
                shareWx(title, text, linkUrl, imgUrl);
                break;
            case "4":
                shareQQ(title, text, linkUrl, imgUrl);
                break;
        }
    }

    public void shareWx(String title, String text, String linkUrl, String imgUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        if (TextUtils.isEmpty(imgUrl)) {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon);
            shareParams.setImageData(bitmap);
        } else
            shareParams.setImageUrl(imgUrl);
        shareParams.setText(text);
        shareParams.setTitle(title);
        //如果是分享图片,那么不要设置url
        shareParams.setUrl(linkUrl);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("分享结果", "onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享结果", "onError");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("分享结果", "onCancel");
            }
        });
        platform.share(shareParams);
    }

    public void shareQQ(String title, String text, String linkUrl, String imgUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setTitleUrl(linkUrl);
        shareParams.setSite(mContext.getString(R.string.app_name));
        shareParams.setSiteUrl("http://www.ixungen.cn/portal.php");

        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("分享结果", "onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享结果", "onError");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("分享结果", "onCancel");
            }
        });
        platform.share(shareParams);
    }

    public void shareWB(String title, String text, String linkUrl, String imgUrl) {
        Platform.ShareParams shareParams = new SinaWeibo.ShareParams();
        shareParams.setText(title + "\n" + text + "\n" + linkUrl);
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("分享结果", "onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享结果", "onError");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("分享结果", "onCancel");
            }
        });
        platform.share(shareParams);
    }
}
