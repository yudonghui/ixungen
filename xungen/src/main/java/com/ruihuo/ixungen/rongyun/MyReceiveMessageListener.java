package com.ruihuo.ixungen.rongyun;

import com.ruihuo.ixungen.utils.LogUtils;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * @author yudonghui
 * @date 2017/4/18
 * @describe May the Buddha bless bug-free!!!
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    @Override
    public boolean onReceived(Message message, int i) {
        LogUtils.e("onReceived", "消息内容：" + message.getExtra() + "数量：" + i);
        switch (message.getExtra()) {
            case "1":
                //帖子消息

                break;
            case "2":
                //系统消息
                break;
            case "3":
                //活动消息
                break;

        }
        return false;
    }
}
