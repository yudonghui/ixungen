package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/19
 * @describe May the Buddha bless bug-free!!!
 */
public class SystemSmsBean {

    /**
     * code : 0
     * msg : success
     * data : [{"title":"最新公告","content":"活动","avatar":"http://img4.duitang.com/uploads/item/201411/14/201","relate_table":"cms_announcement","relate_table_id":"10","create_time":"1492135792"},{"title":"最新公告","content":"最新公告","avatar":"http://www.wed114.cn/jiehun/uploads/allimg/150508/","relate_table":"cms_announcement","relate_table_id":"11","create_time":"1492135792"}]
     * serverTime : 2017-04-19 14:06:32
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
         * title : 最新公告
         * content : 活动
         * avatar : http://img4.duitang.com/uploads/item/201411/14/201
         * relate_table : cms_announcement
         * relate_table_id : 10
         * create_time : 1492135792
         */

        private String title;
        private String content;
        private String avatar;
        private String relate_table;
        private String relate_table_id;
        private String create_time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
    }
}
