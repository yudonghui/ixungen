package com.ruihuo.ixungen.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.family.NewFriendActivity;
import com.ruihuo.ixungen.activity.genxin.ActionSmsActivity;
import com.ruihuo.ixungen.activity.genxin.PostsSmsActivity;
import com.ruihuo.ixungen.activity.genxin.SystemSmsActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.common.IsJoinAgantion;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.fragment.ChatFragment;
import com.ruihuo.ixungen.fragment.ClansmenFragment;
import com.ruihuo.ixungen.fragment.HomeFragment;
import com.ruihuo.ixungen.fragment.UserFragment;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.main.fragment.XSJPBookFragment;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.LogUtils;
import com.ruihuo.ixungen.utils.StatusBarUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.umeng.analytics.MobclickAgent;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.model.Event;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static com.ruihuo.ixungen.utils.PhotoUtils.PERMISSIONS_REQUEST_FILE;

public class XunGenActivity extends AppCompatActivity implements IUnReadMessageObserver {
    private LinearLayout mLlHome;
    private LinearLayout mLlChat;
    private LinearLayout mLlStemma;
    private LinearLayout mLlWonderful;
    private LinearLayout mLlUser;

    private ImageView mImageHome;
    private ImageView mImageChat;
    private ImageView mImageStemma;
    private ImageView mImageWonderful;
    private ImageView mImageUser;


    private TextView mTextHome;
    private TextView mTextChat;
    private TextView mTextStemma;
    private TextView mTextWonderful;
    private TextView mTextUser;

    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private XSJPBookFragment mXsjpBookFragment;
    private ClansmenFragment mClansmenFragment;
    private ChatFragment mChatFragment;
    private UserFragment mUserFragment;
    //private int colorSelect=getResources().getColor(R.color.green_txt);
    // private int colorGray=getResources().getColor(R.color.brown_txt);
    private int colorSelect;
    private int colorUnSelect;
    private TextView mRedPointChat;
    private View mTitleView;
    private Context mContext;
    private int identifier;//电量条的高度
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    showLoginHint();
                    break;
            }
        }
    };
    private StatusBarUtils mStatusBarUtils;

    /**
     * 消息类型 0 系统消息
     * 1 新的朋友
     * 2 帖子消息
     * 3 活动消息
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mStatusBarUtils = StatusBarUtils.with(this);
        mStatusBarUtils.init();
        LogUtils.e("执行了Activity", "oncreate");
        mContext = this;
        getPression();
        //初始化控件
        initView();
        //版本检查
        updaterVersion();
        initData();
        //初始化Fragment
        initFragment();
        addListener();
        //注册一个动态广播
        registerBoradcastReceiver();
        //在别的地方登陆的时候进行的监听
        setLoginStatusListener();
    }

    public void getPression() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setLoginStatusListener() {
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                if (connectionStatus.getValue() == 3) {
                    handler.sendEmptyMessage(1);
                }
            }

        });
    }

    private void showLoginHint() {
        final Dialog dialog = new Dialog(mContext);
        View view = View.inflate(mContext, R.layout.dialog_login_hint, null);
        TextView mConfirm = (TextView) view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("username", "");
                edit.putString("password", "");
                edit.putBoolean("islogin", false);
                edit.commit();
                XunGenApp.isLogin = false;
                //融云退出登录
                RongIM.getInstance().logout();
                Intent intent = new Intent(mContext, XunGenActivity.class);
                //关闭了所有的activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.LOGIN_SUCCESS);
        registerReceiver(br, intentFilter);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //个人信息成功了的话，更新左上角的姓氏
            if (intent.getBooleanExtra("isUpdate", false) && action.equals(ConstantNum.LOGIN_SUCCESS)) {
                // mHomeFragment.updateSurname();
                return;
            }
            //添加、删除好友成功后发送的广播
            if (intent.getBooleanExtra("deleteFriend", false) && action.equals(ConstantNum.LOGIN_SUCCESS)) {
                if (mChatFragment != null)
                    mChatFragment.refreshFamily();
                return;
            }
            //删除家谱之后发送的广播.登录成功
            if (action.equals(ConstantNum.LOGIN_SUCCESS)) {
                NetWorkData netWorkData = new NetWorkData();
                netWorkData.getRecommend(mContext, 2);
                netWorkData.getByName(mContext);
                if (mChatFragment != null)
                    mChatFragment.refreshSms();//登录的时候刷新会话列表。
                if (mHomeFragment != null)
                    mHomeFragment.refreshSms();
                if (mXsjpBookFragment != null)
                    mXsjpBookFragment.myData();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                setHome(fragmentTransaction);
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    };

    private void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };
        /**
         * 监听未读消息数量
         * 第一个参数  接收未读消息消息的监听器。
         * 第二个参数 接收未读消息的会话类型。
         */
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
    }

    private int unReadNum = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                mRedPointChat.setVisibility(View.VISIBLE);
            } else if (msg.what == 2) {
                mRedPointChat.setVisibility(View.GONE);
            }

        }
    };
    private static final int NOTIFICATION_FLAG = 1;
    private String extra;

    private void addListener() {
        mLlHome.setOnClickListener(TabListener);
        mLlStemma.setOnClickListener(TabListener);
        mLlChat.setOnClickListener(TabListener);
        mLlWonderful.setOnClickListener(TabListener);
        mLlUser.setOnClickListener(TabListener);
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                LogUtils.e("MessageListener", i + "");
                extra = ((TextMessage) message.getContent()).getExtra();
                Conversation.ConversationType conversationType = message.getConversationType();//消息类型
                String content = ((TextMessage) message.getContent()).getContent();
                SharedPreferences sp = mContext.getSharedPreferences("redPoints", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                switch (TextUtils.isEmpty(extra) ? "" : extra) {
                    case "0":
                        int redSystem = sp.getInt("redSystem", 0);
                        redSystem++;
                        editor.putInt("redSystem", redSystem);
                        break;
                    case "1":
                        int redNewFriend = sp.getInt("redNewFriend", 0);
                        redNewFriend++;
                        editor.putInt("redNewFriend", redNewFriend);
                        if (mChatFragment != null)
                            mChatFragment.refreshFamily();
                        break;
                    case "2":
                        int redPosts = sp.getInt("redPosts", 0);
                        redPosts++;
                        editor.putInt("redPosts", redPosts);
                        break;
                    case "3":
                        int redAction = sp.getInt("redAction", 0);
                        redAction++;
                        editor.putInt("redAction", redAction);
                        break;
                    case "99":
                        int redSystem1 = sp.getInt("redSystem", 0);
                        redSystem1++;
                        editor.putInt("redSystem", redSystem1);
                        Intent intent = new Intent();
                        intent.setAction(ConstantNum.CONSUME_SUCEESS);
                        sendBroadcast(intent);
                        break;
                    default:
                        int privateChat = sp.getInt("privateChat", 0);
                        privateChat++;
                        editor.putInt("privateChat", privateChat);
                        break;
                }
                editor.commit();
                if (mChatFragment != null) {
                    mChatFragment.redPoints(extra);
                    mChatFragment.setFamilyRedpoints(extra);
                    setRedPoint();
                    //对于接收到消息后 通知处理
                    isBackground(message, extra, content);
                }
                return false;
            }
        });
    }

    private void setRedPoint() {
        SharedPreferences sp = getSharedPreferences("redPoints", Context.MODE_PRIVATE);
        int redNewFriend = sp.getInt("redNewFriend", 0);
        int redPosts = sp.getInt("redPosts", 0);
        int redSystem = sp.getInt("redSystem", 0);
        int redAction = sp.getInt("redAction", 0);
        //boolean newFriend = sp.getBoolean("newFriend", false);
        if (redPosts != 0 || redSystem != 0 || redAction != 0 || redNewFriend != 0) {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = 1;
            mHandler.sendMessage(msg);
        } else {
            RongIMClient.getInstance().clearConversations(new RongIMClient.ResultCallback() {
                @Override
                public void onSuccess(Object o) {
                    LogUtils.e("成功清除系统消息", "onSuccess");
                    RongContext.getInstance().getEventBus().post(new Event.ConversationUnreadEvent(Conversation.ConversationType.SYSTEM, "20000"));
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.e("清除系统消息失败", "errorCode");
                }
            }, Conversation.ConversationType.SYSTEM);

        }
    }

    private boolean isBackground(Message message, String extra, String content) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(mContext.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // Log.i("后台", appProcess.processName);
                    notifycation(message, extra, content);
                    return true;
                } else {
                    // Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    private void notifycation(Message message, String extra, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch (extra) {
            case "99":
            case "0":
                //系统消息
                //活动消息
                PendingIntent intentSystem = PendingIntent.getActivity(mContext, 0, new Intent(mContext, SystemSmsActivity.class), 0);
                Notification notifySystem = new Notification.Builder(mContext)
                        /**
                         *设置状态栏中的小图片，尺寸一般建议在24×24，
                         * 这个图片同样也是在下拉状态栏中所显示，
                         * 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
                         */
                        .setSmallIcon(R.drawable.notification) //
                        // 设置在status
                        .setTicker(content)
                        // bar上显示的提示文字
                        .setContentTitle(content)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                        .setContentText(content)// TextView中显示的详细内容
                        .setContentIntent(intentSystem) // 关联PendingIntent
                        .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                        .getNotification(); // 需要注意build()是在API level
                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                notifySystem.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_FLAG, notifySystem);
                break;
            case "1":
                //新的朋友
                PendingIntent intentNewFriend = PendingIntent.getActivity(mContext, 0, new Intent(mContext, NewFriendActivity.class), 0);
                Notification notifyNewFriend = new Notification.Builder(mContext)
                        /**
                         *设置状态栏中的小图片，尺寸一般建议在24×24，
                         * 这个图片同样也是在下拉状态栏中所显示，
                         * 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
                         */
                        .setSmallIcon(R.drawable.notification) //
                        // 设置在status
                        .setTicker(content)
                        // bar上显示的提示文字
                        .setContentTitle(content)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                        .setContentText(content)// TextView中显示的详细内容
                        .setContentIntent(intentNewFriend) // 关联PendingIntent
                        .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                        .getNotification(); // 需要注意build()是在API level
                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                notifyNewFriend.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_FLAG, notifyNewFriend);

                break;
            case "2":
                //帖子消息
                PendingIntent intentPosts = PendingIntent.getActivity(mContext, 0, new Intent(mContext, PostsSmsActivity.class), 0);
                Notification notifyPosts = new Notification.Builder(mContext)
                        /**
                         *设置状态栏中的小图片，尺寸一般建议在24×24，
                         * 这个图片同样也是在下拉状态栏中所显示，
                         * 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
                         */
                        .setSmallIcon(R.drawable.notification) //
                        // 设置在status
                        .setTicker(content)
                        // bar上显示的提示文字
                        .setContentTitle(content)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                        .setContentText(content)// TextView中显示的详细内容
                        .setContentIntent(intentPosts) // 关联PendingIntent
                        .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                        .getNotification(); // 需要注意build()是在API level
                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                notifyPosts.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_FLAG, notifyPosts);
                break;
            case "3":
                //活动消息
                PendingIntent intentAction = PendingIntent.getActivity(mContext, 0, new Intent(mContext, ActionSmsActivity.class), 0);
                Notification notifyAction = new Notification.Builder(mContext)
                        /**
                         *设置状态栏中的小图片，尺寸一般建议在24×24，
                         * 这个图片同样也是在下拉状态栏中所显示，
                         * 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
                         */
                        .setSmallIcon(R.drawable.notification) //
                        // 设置在status
                        .setTicker(content)
                        // bar上显示的提示文字
                        .setContentTitle(content)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                        .setContentText(content)// TextView中显示的详细内容
                        .setContentIntent(intentAction) // 关联PendingIntent
                        .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                        .getNotification(); // 需要注意build()是在API level
                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                notifyAction.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_FLAG, notifyAction);
                break;
            default:
                //好友聊天
                String targetUserId = message.getTargetId();
                String title = message.getObjectName();
                String s = message.toString();
                Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase()).appendQueryParameter("targetId", targetUserId).appendQueryParameter("title", title).build();
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                PendingIntent intentChat = PendingIntent.getActivity(mContext, 0, intent, 0);
                Notification notifyChat = new Notification.Builder(mContext)
                        /**
                         *设置状态栏中的小图片，尺寸一般建议在24×24，
                         * 这个图片同样也是在下拉状态栏中所显示，
                         * 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
                         */
                        .setSmallIcon(R.drawable.notification) //
                        // 设置在status
                        .setTicker(content)
                        // bar上显示的提示文字
                        .setContentTitle(content)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                        .setContentText(content)// TextView中显示的详细内容
                        .setContentIntent(intentChat) // 关联PendingIntent
                        .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                        .getNotification(); // 需要注意build()是在API level
                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                notifyChat.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_FLAG, notifyChat);
                break;
        }
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
       /* mXsjpBookFragment = new XSJPBookFragment();
        mClansmenFragment = new ClansmenFragment();
        mChatFragment = new ChatFragment();
        mUserFragment = new UserFragment();*/
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl, mHomeFragment)
                .show(mHomeFragment);
        fragmentTransaction.commit();
    }

    private void initView() {
        colorSelect = getResources().getColor(R.color.tab_select);
        colorUnSelect = getResources().getColor(R.color.tab_unselect);
        mLlHome = (LinearLayout) findViewById(R.id.ll_home);
        mLlStemma = (LinearLayout) findViewById(R.id.ll_stemma);
        mLlChat = (LinearLayout) findViewById(R.id.ll_chat);
        mLlWonderful = (LinearLayout) findViewById(R.id.ll_wonderful);
        mLlUser = (LinearLayout) findViewById(R.id.ll_user);
        mImageHome = (ImageView) findViewById(R.id.image_home);
        mImageStemma = (ImageView) findViewById(R.id.image_stemma);
        mImageChat = (ImageView) findViewById(R.id.image_chat);
        mImageWonderful = (ImageView) findViewById(R.id.image_wonderful);
        mImageUser = (ImageView) findViewById(R.id.image_user);
        mTextHome = (TextView) findViewById(R.id.text_home);
        mTextChat = (TextView) findViewById(R.id.text_chat);
        mTextStemma = findViewById(R.id.text_stemma);
        mTextUser = (TextView) findViewById(R.id.text_user);
        mTextWonderful = (TextView) findViewById(R.id.text_wonderful);
        mRedPointChat = (TextView) findViewById(R.id.red_point_chat);
        mTitleView = findViewById(R.id.titleView);
    }

    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

            mStatusBarUtils.setColor(ContextCompat.getColor(XunGenActivity.this, R.color.brown_bg));
            statusBar();
            switch (v.getId()) {
                case R.id.ll_home:
                    setHome(fragmentTransaction);
                    break;
                case R.id.ll_stemma:
                    setXsjpBook(fragmentTransaction);
                    break;
                case R.id.ll_chat:
                    setChat(fragmentTransaction);
                    break;
                case R.id.ll_wonderful:
                    setClansmen(fragmentTransaction);
                    break;
                case R.id.ll_user:
                    setUser(fragmentTransaction);
                    break;
            }
            fragmentTransaction.commit();
        }
    };

    private void setHome(FragmentTransaction fragmentTransaction) {
        mImageHome.setImageResource(R.drawable.home_select);
        mTextHome.setTextColor(colorSelect);

        mImageChat.setImageResource(R.drawable.chat_unselect);
        mTextChat.setTextColor(colorUnSelect);

        mImageStemma.setImageResource(R.drawable.stemma_unselect);
        mTextStemma.setTextColor(colorUnSelect);

        mImageWonderful.setImageResource(R.drawable.clansmen_unselect);
        mTextWonderful.setTextColor(colorUnSelect);

        mImageUser.setImageResource(R.drawable.user_unselect);
        mTextUser.setTextColor(colorUnSelect);

        mTitleView.setVisibility(View.GONE);

        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.fl, mHomeFragment);
        }
        if (mChatFragment != null) {
            fragmentTransaction.hide(mChatFragment);
        }
        if (mXsjpBookFragment != null) {
            fragmentTransaction.hide(mXsjpBookFragment);
        }
        if (mClansmenFragment != null) {
            fragmentTransaction.hide(mClansmenFragment);
        }
        if (mUserFragment != null) {
            fragmentTransaction.hide(mUserFragment);
        }
        fragmentTransaction.show(mHomeFragment);
    }

    private void setChat(FragmentTransaction fragmentTransaction) {
        if (!XunGenApp.isLogin) {
            IntentSkip intentSkip = new IntentSkip();
            intentSkip.skipLogin(mContext, 207);
        } else {
            mImageHome.setImageResource(R.drawable.home_unselect);
            mTextHome.setTextColor(colorUnSelect);

            mImageChat.setImageResource(R.drawable.chat_select);
            mTextChat.setTextColor(colorSelect);

            mImageStemma.setImageResource(R.drawable.stemma_unselect);
            mTextStemma.setTextColor(colorUnSelect);

            mImageWonderful.setImageResource(R.drawable.clansmen_unselect);
            mTextWonderful.setTextColor(colorUnSelect);

            mImageUser.setImageResource(R.drawable.user_unselect);
            mTextUser.setTextColor(colorUnSelect);

            if (mChatFragment == null) {
                mChatFragment = new ChatFragment();
                fragmentTransaction.add(R.id.fl, mChatFragment);
            } else {
                mChatFragment.setSmsListener(new CallBackInterface() {
                    @Override
                    public void callBack() {
                        setRedPoint();
                    }
                });
                mChatFragment.setFamilyListener(new CallBackInterface() {
                    @Override
                    public void callBack() {
                        setRedPoint();

                    }
                });
                mChatFragment.refreshFamily();
            }
            if (mHomeFragment != null) {
                fragmentTransaction.hide(mHomeFragment);
            }
            if (mXsjpBookFragment != null) {
                fragmentTransaction.hide(mXsjpBookFragment);
            }
            if (mClansmenFragment != null) {
                fragmentTransaction.hide(mClansmenFragment);
            }
            if (mUserFragment != null) {
                fragmentTransaction.hide(mUserFragment);
            }
            fragmentTransaction.show(mChatFragment);

        }
    }

    private void setXsjpBook(FragmentTransaction fragmentTransaction) {
        if (!XunGenApp.isLogin) {
            IntentSkip intentSkip = new IntentSkip();
            intentSkip.skipLogin(mContext, 208);
        } else {
            mImageHome.setImageResource(R.drawable.home_unselect);
            mTextHome.setTextColor(colorUnSelect);

            mImageChat.setImageResource(R.drawable.chat_unselect);
            mTextChat.setTextColor(colorUnSelect);

            mImageStemma.setImageResource(R.drawable.stemma_select);
            mTextStemma.setTextColor(colorSelect);

            mImageWonderful.setImageResource(R.drawable.clansmen_unselect);
            mTextWonderful.setTextColor(colorUnSelect);

            mImageUser.setImageResource(R.drawable.user_unselect);
            mTextUser.setTextColor(colorUnSelect);

            if (mXsjpBookFragment == null) {
                mXsjpBookFragment = new XSJPBookFragment();
                fragmentTransaction.add(R.id.fl, mXsjpBookFragment);
            }
            if (mHomeFragment != null) {
                fragmentTransaction.hide(mHomeFragment);
            }
            if (mChatFragment != null) {
                fragmentTransaction.hide(mChatFragment);
            }
            if (mClansmenFragment != null) {
                fragmentTransaction.hide(mClansmenFragment);
            }
            if (mUserFragment != null) {
                fragmentTransaction.hide(mUserFragment);
            }
            fragmentTransaction.show(mXsjpBookFragment);

        }
    }

    private void setClansmen(FragmentTransaction fragmentTransaction) {
        if (!XunGenApp.isLogin) {
            IntentSkip intentSkip = new IntentSkip();
            intentSkip.skipLogin(mContext, 209);
        } else if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
            IsJoinAgantion isJoinAgantion = new IsJoinAgantion(XunGenActivity.this);
            isJoinAgantion.showDialog(ConstantNum.REQUEST_1876);
        } else {
            mImageHome.setImageResource(R.drawable.home_unselect);
            mTextHome.setTextColor(colorUnSelect);

            mImageChat.setImageResource(R.drawable.chat_unselect);
            mTextChat.setTextColor(colorUnSelect);

            mImageStemma.setImageResource(R.drawable.stemma_unselect);
            mTextStemma.setTextColor(colorUnSelect);

            mImageWonderful.setImageResource(R.drawable.clansmen_select);
            mTextWonderful.setTextColor(colorSelect);

            mImageUser.setImageResource(R.drawable.user_unselect);
            mTextUser.setTextColor(colorUnSelect);

            if (mClansmenFragment == null) {
                mClansmenFragment = new ClansmenFragment();
                fragmentTransaction.add(R.id.fl, mClansmenFragment);
            }
            if (mHomeFragment != null) {
                fragmentTransaction.hide(mHomeFragment);
            }
            if (mChatFragment != null) {
                fragmentTransaction.hide(mChatFragment);
            }
            if (mXsjpBookFragment != null) {
                fragmentTransaction.hide(mXsjpBookFragment);
            }
            if (mUserFragment != null) {
                fragmentTransaction.hide(mUserFragment);
            }
            fragmentTransaction.show(mClansmenFragment);
        }
    }

    private void setUser(FragmentTransaction fragmentTransaction) {
        if (!XunGenApp.isLogin) {
            IntentSkip intentSkip = new IntentSkip();
            intentSkip.skipLogin(mContext, 10);
        } else {
            mImageHome.setImageResource(R.drawable.home_unselect);
            mTextHome.setTextColor(colorUnSelect);

            mImageChat.setImageResource(R.drawable.chat_unselect);
            mTextChat.setTextColor(colorUnSelect);

            mImageStemma.setImageResource(R.drawable.stemma_unselect);
            mTextStemma.setTextColor(colorUnSelect);

            mImageWonderful.setImageResource(R.drawable.clansmen_unselect);
            mTextWonderful.setTextColor(colorUnSelect);

            mImageUser.setImageResource(R.drawable.user_select);
            mTextUser.setTextColor(colorSelect);

            mTitleView.setVisibility(View.GONE);
            if (mUserFragment == null) {
                mUserFragment = new UserFragment();
                fragmentTransaction.add(R.id.fl, mUserFragment);
            } else {
                mUserFragment.setData();
            }
            if (mHomeFragment != null) {
                fragmentTransaction.hide(mHomeFragment);
            }
            if (mChatFragment != null) {
                fragmentTransaction.hide(mChatFragment);
            }
            if (mXsjpBookFragment != null) {
                fragmentTransaction.hide(mXsjpBookFragment);
            }
            if (mClansmenFragment != null) {
                fragmentTransaction.hide(mClansmenFragment);
            }
            fragmentTransaction.show(mUserFragment);
        }
    }

    private void statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mTitleView.setVisibility(View.VISIBLE);

            if (identifier > 0) {
                int dimensionPixelSize = getResources().getDimensionPixelSize(identifier);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dimensionPixelSize);
                mTitleView.setLayoutParams(layoutParams);
            }
        } else mTitleView.setVisibility(View.GONE);
    }

    private String apkUrl;

    private void updaterVersion() {
        final int appVersionCode = com.ruihuo.ixungen.utils.VersionInfo.getAppVersionCode(this);
        HttpInterface mHttp = HttpUtilsManager.getInstance(this);
        Bundle params = new Bundle();
        params.putString("type", "android");
        mHttp.post(Url.VERSION_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    // int code = jsonObject.getInt("code");
                    final JSONObject data = jsonObject.getJSONObject("data");
                    //线上的版本号
                    int code = data.getInt("build");
                    String status = data.getString("status");
                    if (appVersionCode < code) {

                        //强制更新
                        //如果版本本地的版本号和发布的版本号不同，那么就提示是否更新
                        HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                        hintDialogUtils.setTitleVisiable(true);
                        hintDialogUtils.setTitle("发现新版本 " + data.getString("version"));
                        //verName,updateInfo
                        hintDialogUtils.setVersionMessage("【更新说明】\n" + data.getString("remark"));
                        hintDialogUtils.setMessage("发现新版本 " + data.getString("version") + "\n\n" + "【更新说明】\n" + data.getString("remark"));
                        //HintDialogUtils.setTvCancel("以后再说");
                        hintDialogUtils.setConfirm("立即更新", new DialogHintInterface() {

                            @Override
                            public void callBack(View view) {
                                try {
                                    apkUrl = data.getString("download_url");
                                    checkSDPermission();
                                    // updateApk();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        if ("1".equals(status)) {
                            hintDialogUtils.setCancelable(false);
                            hintDialogUtils.setVisibilityCancel();
                        } else {
                            hintDialogUtils.setTvCancel("以后再说");
                        }
                        //不自动更新采用百度，默默的 偷偷的下载，下次进来的时候 进行提示更新
                        //   updateAuto();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private int PERMISSIONS_CODE = 100;

    private void checkSDPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSIONS_CODE);
        } else {
            //有读写sd卡的权限
            updateApk();
        }

    }

    private void updateApk() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(this);
        mHttp.updateFile(apkUrl, this, new JsonInterface() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("返回数据：", "requestCode" + requestCode + "resultCode" + resultCode);
        if (resultCode == 30 || (requestCode == ConstantNum.REQUEST_1876 && resultCode != ConstantNum.RESULT_2345)) {//退出登录 或者点击宗亲导航之后回来。
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            setHome(fragmentTransaction);
            mHomeFragment.refreshSms();
            fragmentTransaction.commitAllowingStateLoss();
        } else if (requestCode == ConstantNum.REQUEST_1876 && resultCode == ConstantNum.RESULT_2345) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            setClansmen(fragmentTransaction);
            if (mClansmenFragment != null)
                mClansmenFragment.refreshData();//刷新宗亲数据
            fragmentTransaction.commitAllowingStateLoss();
        } else if (requestCode == ConstantNum.REQUEST_CAMERA) {//扫描添加好友或者加入宗亲会
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);//扫描出来的结果
                    NetWorkData netWorkData = new NetWorkData();
                    netWorkData.jieTwoCode(mContext, result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(mContext, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == 1111 && resultCode == 2221) {//创建家谱回来
            if (mXsjpBookFragment != null)
                mXsjpBookFragment.myData();
        } else if (requestCode == 6626 && resultCode == 6626) {//删除家谱回来
            if (mXsjpBookFragment != null)
                mXsjpBookFragment.myData();//重新获取书架上面的数据
            mHomeFragment.refreshSms();//重新获取首页中间那个家谱的数据
        } else if (requestCode == 221 && resultCode == 8811) {//右上角照相机发布动态回来
            if (mHomeFragment != null)
                mHomeFragment.refreshFriendState();
        } else if (requestCode == 222 && resultCode == 8811) {
            if (mUserFragment != null)
                mUserFragment.refreshDynamicData();
        }
    }


    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(XunGenActivity.this, "再按一次退出掌上寻根", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            Log.e("退出app", "tuichule");
            System.exit(0);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("执行了Activity", "onRestart");
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("执行了Activity", "onDestroy");
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        RongIM.getInstance().logout();
        unregisterReceiver(br);
    }

    @Override
    public void onCountChanged(int i) {
        LogUtils.e("未读消息个数", "-----" + i);
        SharedPreferences sp = getSharedPreferences("redPoints", Context.MODE_PRIVATE);
        int redNewFriend = sp.getInt("redNewFriend", 0);
        int redPosts = sp.getInt("redPosts", 0);
        int redSystem = sp.getInt("redSystem", 0);
        int redAction = sp.getInt("redAction", 0);
        if (redPosts > 0 || redSystem > 0 || redAction > 0 || redNewFriend > 0 || i > 0) {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = 1;
            mHandler.sendMessage(msg);
        } else {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }
        /*if (i == 0) {
            mRedPoint.setVisibility(View.GONE);
        } else if (i > 0 && i < 100) {
            mRedPoint.setVisibility(View.VISIBLE);
            mRedPoint.setText(i + "");
        } else {
            mRedPoint.setVisibility(View.VISIBLE);
            mRedPoint.setText("...");
        }*/
    }

   /* private void updateAuto() {
        BDAutoUpdateSDK.silenceUpdateAction(this);
    }*/
}
