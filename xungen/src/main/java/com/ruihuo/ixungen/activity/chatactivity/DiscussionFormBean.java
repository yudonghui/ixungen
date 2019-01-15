package com.ruihuo.ixungen.activity.chatactivity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/9/8
 * @describe May the Buddha bless bug-free!!!
 */
public class DiscussionFormBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":"17","user_rid":"1000796","group_id":"aec5679f-33f8-4b3b-a8f3-052d189a238d"}]
     * serverTime : 2017-09-08 13:53:04
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

    public static class DataBean {
        /**
         * id : 17
         * user_rid : 1000796
         * group_id : aec5679f-33f8-4b3b-a8f3-052d189a238d
         */

        private String id;
        private String user_rid;
        private String group_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_rid() {
            return user_rid;
        }

        public void setUser_rid(String user_rid) {
            this.user_rid = user_rid;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }
    }
}
