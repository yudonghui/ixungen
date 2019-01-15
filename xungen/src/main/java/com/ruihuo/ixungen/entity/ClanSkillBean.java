package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/13
 * @describe May the Buddha bless bug-free!!!
 */
public class ClanSkillBean {

    /**
     * code : 0
     * msg : success
     * totalPage : 2
     * data : [{"id":"2","title":"林氏木板水印","pic":"12,039829f90961","content":"","user_rid":"1000305","status":"1","sort":"10","create_time":"1496741456"},{"id":"12","title":"图形","pic":"14,03d048f54a27","content":"","user_rid":"1000305","status":"1","sort":"10","create_time":"1496741203"}]
     * serverTime : 2017-06-07 10:03:52
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
         * id : 2
         * title : 林氏木板水印
         * pic : 12,039829f90961
         * content :
         * user_rid : 1000305
         * status : 1
         * sort : 10
         * create_time : 1496741456
         */

        private String id;
        private String title;
        private String pic;
        private String content;
        private String user_rid;
        private String status;
        private String sort;
        private String create_time;
        private String nikename;

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_rid() {
            return user_rid;
        }

        public void setUser_rid(String user_rid) {
            this.user_rid = user_rid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
