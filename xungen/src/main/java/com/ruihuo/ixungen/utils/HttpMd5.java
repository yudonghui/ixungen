package com.ruihuo.ixungen.utils;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class HttpMd5 {
    private static String key = "553e35c9f02c07589d8b2150f753e4a6";

    public static String buildSign(Map<String, Object> map) {
       /* if (!map.containsKey("timeStamp"))
            map.put("timeStamp", System.currentTimeMillis());*/
        StringBuffer buf = new StringBuffer();
        String sign;
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                //buf.append(entry.getKey() + "=" + entry.getValue() + "&");
                buf.append(URLEncoder.encode((String) entry.getKey(), "UTF-8") + "=");
                String value = URLEncoder.encode((String) entry.getValue(), "UTF-8");
                buf.append(("*".equals(value) ? "%2A" : value) + "&");
            }
            buf.delete(buf.length() - 1, buf.length());
            if (StringUtil.isNullOrEmpty(key)) {
                return null;
            } else {
                sign = StringUtil.getMD5(key + buf);
            }
        } catch (Exception e) {
            return null;
        }
        return sign;
    }
}
