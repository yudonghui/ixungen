package com.ruihuo.ixungen.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.ruihuo.ixungen.activity.login.PhoneLoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 泽群 on 2015/8/28.
 */
public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        String code = "";
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));
                if (!address.equals("1065502076830030148")||!address.equals("10690205187639")) {
                    return;
                }
               // LogUtils.e("DEBUG", "发件人为：" + address + " " + "短信内容为：" + body);
                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    code = matcher.group(0);
                   // LogUtils.e("DEBUG", "code is " + code);
                    mHandler.obtainMessage(PhoneLoginActivity.MSG_RECEIVED_CODE, code).sendToTarget();
                }

            }
            c.close();
        }

    }
}
