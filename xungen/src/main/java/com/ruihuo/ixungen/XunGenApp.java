package com.ruihuo.ixungen;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.multidex.MultiDexApplication;
import android.view.View;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.ui.familytree.bean.TreeDetailSystemBean;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * ************************************************************************
 * **                              _oo0oo_                               **
 * **                             o8888888o                              **
 * **                             88" . "88                              **
 * **                             (| -_- |)                              **
 * **                             0\  =  /0                              **
 * **                           ___/'---'\___                            **
 * **                        .' \\\|     |// '.                          **
 * **                       / \\\|||  :  |||// \\                        **
 * **                      / _ ||||| -:- |||||- \\                       **
 * **                      | |  \\\\  -  /// |   |                       **
 * **                      | \_|  ''\---/''  |_/ |                       **
 * **                      \  .-\__  '-'  __/-.  /                       **
 * **                    ___'. .'  /--.--\  '. .'___                     **
 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
 * **                              '=---='                               **
 * ************************************************************************
 * **                        佛祖保佑      镇类之宝                         **
 * ************************************************************************
 */
public class XunGenApp extends MultiDexApplication {
    private static XunGenApp instance;
    public static int Privilege = 1;
    public static IWXAPI api;
    public static String rid = "";
    public static boolean isLogin = false;
    public static String token = "";
    public static String surname = "";
    public static String ry_token = "";
    public static String clanAssociationIds = "";
    public static String nikename = "";
    public static String surnameId = "";
    public static String phone = "";
    public static String trueName = "";
    public static String idCard = "";//绑定真实身份证后的身份证号
    public static String message = "";//获取不到姓氏id的信息提醒
    public static String sex = "";//性别
    public static String birthday = "";//出生日期
    public static Uri discussionAvatar;
    public static Map<Integer, List<TreeDetailSystemBean.DataBean>> mBookMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        MobSDK.init(this, "1d5a6d043d499", "6dd490df862f2e4e10f856ad0a2db324");//mob初始化
        MobclickAgent.enableEncrypt(true);//友盟 日志加密。加密模式可以有效防止网络攻击，提高数据安全性。
        /*
        * 初始化图文混排jar包
        * */
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);

        //BQMM.getInstance().initConfig(getApplicationContext(),"089b5f0a9bfc4f608193cd3c2ccc01b9", "c61bc4405bb44643bac45c716ebbe052");//表情包初始化

        ZXingLibrary.initDisplayOpinion(this);
        x.Ext.init(this);

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(ConstantNum.WX_APP_ID);

        RongIM.init(this);
        userInfoInit();
        // RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if ("io.rong.fast".equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }
        //会话界面的点击事件
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Intent intent = new Intent(context, FriendInfoActivity.class);
                //这里用户id应该是根号
                intent.putExtra("rid", userInfo.getUserId());
                context.startActivity(intent);
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
        //  Thread.setDefaultUncaughtExceptionHandler(restartHandler);// 程序崩溃时触发线程  用来捕获程序崩溃异常

    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            System.exit(1);//退出程序

        }
    };

    //设置个人信息，和群组信息
    private void userInfoInit() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                NetWorkData netWorkData = new NetWorkData();
                netWorkData.getUserInfo(s);
                return null;
            }
        }, true);
        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String s) {
                NetWorkData netWorkData = new NetWorkData();
                netWorkData.getGroupInfo(s);
                return null;
            }
        }, true);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    public static XunGenApp getInstance() {
        return instance;
    }
}
