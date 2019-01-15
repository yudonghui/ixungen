package com.ruihuo.ixungen.rongyun;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @author yudonghui
 * @date 2017/4/18
 * @describe May the Buddha bless bug-free!!!
 */
public class XunGenNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        //根据需要确定点击这个推送消息后跳转的界面。并且将返回值设置成true
        return false;
    }
}
