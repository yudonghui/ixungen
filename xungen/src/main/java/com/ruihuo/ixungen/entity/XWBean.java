package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/9/18
 * @describe May the Buddha bless bug-free!!!
 */
public class XWBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":"9","name":"寻根问祖","img_url":"https://res.ixungen.cn/img/9,0f70aea5bb9a","link":null,"sort":"1","type":"11","status":"1"},{"id":"10","name":"姓氏文化","img_url":"https://res.ixungen.cn/img/9,0f714dd180de","link":null,"sort":"2","type":"12","status":"1"},{"id":"16","name":"姓氏家谱","img_url":"https://res.ixungen.cn/img/11,0f775eb566df","link":null,"sort":"3","type":"18","status":"1"},{"id":"11","name":"我的宗亲","img_url":"https://res.ixungen.cn/img/12,0f72cfb6b598","link":null,"sort":"4","type":"13","status":"1"},{"id":"12","name":"宗亲商业","img_url":"https://res.ixungen.cn/img/11,0f7307b315cd","link":null,"sort":"5","type":"14","status":"1"},{"id":"13","name":"宗亲活动","img_url":"https://res.ixungen.cn/img/14,0f741e15c04d","link":null,"sort":"6","type":"15","status":"1"},{"id":"14","name":"家规家训","img_url":"https://res.ixungen.cn/img/8,0f7566245990","link":null,"sort":"7","type":"16","status":"1"},{"id":"15","name":"宗亲互助","img_url":"https://res.ixungen.cn/img/10,0f76835bd6d7","link":null,"sort":"8","type":"17","status":"1"}]
     * serverTime : 2017-09-18 15:20:00
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
         * id : 9
         * name : 寻根问祖
         * img_url : https://res.ixungen.cn/img/9,0f70aea5bb9a
         * link : null
         * sort : 1
         * type : 11
         * status : 1
         */

        private String id;
        private String name;
        private String img_url;
        private String link;
        private String sort;
        private String type;
        private String status;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
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
    }
}
