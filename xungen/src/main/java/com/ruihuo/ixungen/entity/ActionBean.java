package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/24
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":"1","name":"七仙女","img_url":null,"summary":null,"link":null,"type":null,"status":null,"create_time":null},{"id":"2","name":"国学少年","img_url":null,"summary":null,"link":null,"type":null,"status":null,"create_time":null},{"id":"3","name":"能工巧匠","img_url":null,"summary":null,"link":null,"type":null,"status":null,"create_time":null}]
     * serverTime : 2017-05-24 16:38:09
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
         * id : 1
         * name : 七仙女
         * img_url : null
         * summary : null
         * link : null
         * type : null
         * status : null
         * create_time : null
         */

        private String id;
        private String name;
        private String img_url;
        private String summary;
        private String link;
        private String sort;
        private String rule;
        private String flag;
        private String bg_color;
        private String subtitle;
        private String bundle;
        private String type;       //0-app打开，1-h5打开
        private String status;     //0-显示，1-不显示
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getBg_color() {
            return bg_color;
        }

        public void setBg_color(String bg_color) {
            this.bg_color = bg_color;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getBundle() {
            return bundle;
        }

        public void setBundle(String bundle) {
            this.bundle = bundle;
        }
    }
}
