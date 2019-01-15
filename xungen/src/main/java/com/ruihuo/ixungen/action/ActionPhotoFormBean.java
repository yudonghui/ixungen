package com.ruihuo.ixungen.action;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/27
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionPhotoFormBean implements Serializable{

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"5","user_rid":"1000123","album_name":"春光无限","img_url":"12,02b0be08439c","address":null,"create_time":"2147483647"}]
     * serverTime : 2017-05-27 10:33:26
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

    public static class DataBean implements Serializable{
        /**
         * id : 5
         * user_rid : 1000123
         * album_name : 春光无限
         * img_url : 12,02b0be08439c
         * address : null
         * create_time : 2147483647
         */

        private String id;
        private String user_rid;
        private String album_name;
        private String img_url;
        private String address;
        private String create_time;

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

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
