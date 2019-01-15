package com.ruihuo.ixungen.entity;

/**
 * @author yudonghui
 * @date 2017/4/10
 * @describe May the Buddha bless bug-free!!!
 */
public class AdBean {

    /**
     * code : 0
     * msg : success
     * data : {"id":"10","association_id":"15","user_rid":"1000269","title":"今天","info":"我在学习！","create_time":"0","name":"于"}
     * serverTime : 2017-04-10 15:53:18
     */

    private int code;
    private String msg;
    private DataBean data;
    private String serverTime;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean {
        /**
         * id : 10
         * association_id : 15
         * user_rid : 1000269
         * title : 今天
         * info : 我在学习！
         * create_time : 0
         * name : 于
         */

        private String id;
        private String association_id;
        private String user_rid;
        private String title;
        private String info;
        private String create_time;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAssociation_id() {
            return association_id;
        }

        public void setAssociation_id(String association_id) {
            this.association_id = association_id;
        }

        public String getUser_rid() {
            return user_rid;
        }

        public void setUser_rid(String user_rid) {
            this.user_rid = user_rid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
