package com.ruihuo.ixungen.ui.main.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/20
 * @describe May the Buddha bless bug-free!!!
 */
public class SurnameStarBean {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 1
     * data : [{"id":"3","rid":"0","surnameid":"451","surname":"于","name":"于冬辉","vocation":"程序猿","birthday":"1990-1-1","synopsis":"哦珍惜眼前人PSP体育用品","coverpic":"12,0e4c56e03ad6","handpic":"10,0e4ba5a87623","status":"0","create_time":"1505181225"}]
     * serverTime : 2017-10-20 16:03:05
     */

    private int code;
    private String msg;
    private int totalPage;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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
         * id : 3
         * rid : 0
         * surnameid : 451
         * surname : 于
         * name : 于冬辉
         * vocation : 程序猿
         * birthday : 1990-1-1
         * synopsis : 哦珍惜眼前人PSP体育用品
         * coverpic : 12,0e4c56e03ad6
         * handpic : 10,0e4ba5a87623
         * status : 0
         * create_time : 1505181225
         */

        private String id;
        private String rid;
        private String surnameid;
        private String surname;
        private String name;
        private String vocation;
        private String birthday;
        private String synopsis;
        private String coverpic;
        private String handpic;
        private String status;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getSurnameid() {
            return surnameid;
        }

        public void setSurnameid(String surnameid) {
            this.surnameid = surnameid;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVocation() {
            return vocation;
        }

        public void setVocation(String vocation) {
            this.vocation = vocation;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public String getCoverpic() {
            return coverpic;
        }

        public void setCoverpic(String coverpic) {
            this.coverpic = coverpic;
        }

        public String getHandpic() {
            return handpic;
        }

        public void setHandpic(String handpic) {
            this.handpic = handpic;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
