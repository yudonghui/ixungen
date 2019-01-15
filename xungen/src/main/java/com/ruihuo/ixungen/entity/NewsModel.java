package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class NewsModel {

    /**
     * code : 0
     * msg : success
     * data : [{"id":1,"type":0,"pic":"http://www.ixungen.cn/data/attachment/forum/201604/13/145657vjt49gjt4994c4v1.jpg","url":"http://www.ixungen.cn/","create_time":1451581261,"stay_time":3,"expire_time":1483203661,"begin_time":1451581261,"target":0,"status":1,"client":"ios"}]
     * servertime : 2017-03-27 16:31:37
     */

    private int code;
    private String msg;
    private String servertime;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean extends ScrollBaseBean{

    }
}
