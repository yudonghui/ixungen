package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.content.Intent;

import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2017/1/9.
 */
public class RongConnect {
    public static void connect(String token, final Context mContext) {
        // token = "H3BeOxFmvXjsKu4qKArafnZVxTK0u7t/7KJJLeyO9XNU/vHjsUp0pyCtlyhW+PDjVGSIAIYQlOwrvf8LkbUUgg==";
        if (mContext.getApplicationInfo().packageName.equals(XunGenApp.getCurProcessName(mContext.getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtils.e("融云链接失败", "onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    // Log.d("LoginActivity", "--onSuccess" + userid);
                   /* mContext.startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
                    finish();*/
                    LogUtils.e("融云链接成功", userid);
                    Intent intent = new Intent();
                    intent.setAction(ConstantNum.CONNECT_SUCEESS);
                    mContext.sendBroadcast(intent);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.e("融云链接失败", errorCode.toString());
                }
            });
        }
    }
}
