package com.ruihuo.ixungen.activity.chatactivity;

import android.net.Uri;

import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.widget.provider.DiscussionConversationProvider;

/**
 * @author yudonghui
 * @date 2017/9/7
 * @describe May the Buddha bless bug-free!!!
 */
@ConversationProviderTag(
        conversationType = "discussion",
        portraitPosition = 1
)
public class ContProvider  extends DiscussionConversationProvider{

    public Uri getPortraitUri(String id){
        return Uri.parse("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=3d2175db3cd3d539d530078052ee8325/b7003af33a87e950c1e1a6491a385343fbf2b425.jpg");
    }
}
