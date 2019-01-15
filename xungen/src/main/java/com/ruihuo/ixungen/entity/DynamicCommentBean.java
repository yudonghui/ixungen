package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/10
 * @describe May the Buddha bless bug-free!!!
 */
public class DynamicCommentBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":"6","pid":"2","rid":"1000795","content":"Hehahahahahahhah","img":"","private":"0","status":"0","count":"0","total_reply":"0","total_like":"0","create_time":"1510213834","reply_info":[{"reply_id":"1","comment_id":"6","reply_rid":"1000795","to_rid":"1000795","reply_content":"回复","reply_time":"11231212","nikename":"董小硕","to_nikename":"董小硕"}]},{"id":"10","pid":"2","rid":"1000796","content":"优齐都女特警路路通","img":"","private":"0","status":"0","count":"0","total_reply":"0","total_like":"0","create_time":"1510221736","reply_info":[]}]
     * serverTime : 2017-11-10 09:11:33
     */

    private int code;
    private String msg;
    private String serverTime;
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

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean extends CommentBaseBeand {

    }
}
