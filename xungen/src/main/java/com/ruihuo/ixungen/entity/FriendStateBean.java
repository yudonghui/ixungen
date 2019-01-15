package com.ruihuo.ixungen.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/8
 * @describe May the Buddha bless bug-free!!!
 */
public class FriendStateBean implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 1
     * data : [{"id":"1","pid":"0","rid":"1000124","content":"测试","img":"","private":"1","status":"0","count":"0","total_reply":"0","total_like":"0","create_time":"0","nikename":"浪迹天涯","avatar":"14,0cbe31c78d34"}]
     * serverTime : 2017-11-09 13:57:48
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

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * pid : 0
         * rid : 1000124
         * content : 测试
         * img :
         * private : 1
         * status : 0
         * count : 0
         * total_reply : 0
         * total_like : 0
         * create_time : 0
         * nikename : 浪迹天涯
         * avatar : 14,0cbe31c78d34
         */

        private String id;
        private String pid;
        private String rid;
        private String content;
        private String img;
        @SerializedName("private")
        private String privateX;
        private String status;
        private String count;
        private String total_reply;
        private String total_like;
        private String create_time;
        private String nikename;
        private String avatar;
        private String family_id;
        private String family_name;
        private String is_like;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPrivateX() {
            return privateX;
        }

        public void setPrivateX(String privateX) {
            this.privateX = privateX;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getTotal_reply() {
            return total_reply;
        }

        public void setTotal_reply(String total_reply) {
            this.total_reply = total_reply;
        }

        public String getTotal_like() {
            return total_like;
        }

        public void setTotal_like(String total_like) {
            this.total_like = total_like;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getFamily_id() {
            return family_id;
        }

        public void setFamily_id(String family_id) {
            this.family_id = family_id;
        }

        public String getFamily_name() {
            return family_name;
        }

        public void setFamily_name(String family_name) {
            this.family_name = family_name;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }
    }
}
