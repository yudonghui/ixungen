package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/25
 * @describe May the Buddha bless bug-free!!!
 */
public class UserMsgBean {

    /**
     * code : 0
     * msg : success
     * data : [{"title":"","content":"发送融云消息接口测试","avatar":"","relate_table":"user_friend","relate_table_id":"2","create_time":"1493106783","type":"3"}]
     * serverTime : 2017-04-25 16:04:38
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
         * title :
         * content : 发送融云消息接口测试
         * avatar :
         * relate_table : user_friend
         * relate_table_id : 2
         * create_time : 1493106783
         * type : 3
         */
        private String id;
        private String title;
        private String content;
        private String avatar;
        private String relate_table;
        private String relate_table_id;
        private String create_time;
        private String type;
        private String flag;//1,普通用户，2，商户
        private String invite_rid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInvite_rid() {
            return invite_rid;
        }

        public void setInvite_rid(String invite_rid) {
            this.invite_rid = invite_rid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getRelate_table() {
            return relate_table;
        }

        public void setRelate_table(String relate_table) {
            this.relate_table = relate_table;
        }

        public String getRelate_table_id() {
            return relate_table_id;
        }

        public void setRelate_table_id(String relate_table_id) {
            this.relate_table_id = relate_table_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
