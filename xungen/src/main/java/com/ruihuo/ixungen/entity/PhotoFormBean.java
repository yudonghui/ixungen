package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/10
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoFormBean {
    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"62","association_id":"28","album_name":"第一个","status":"0","remark":"小车","create_time":"1493692534","update_time":"1493692534"}]
     * serverTime : 2017-05-08 14:38:33
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
         * id : 62
         * association_id : 28
         * album_name : 第一个
         * status : 0
         * remark : 小车
         * create_time : 1493692534
         * update_time : 1493692534
         */

        private String id;
        private String association_id;
        private String album_name;
        private String status;
        private String remark;
        private String create_time;
        private String update_time;
        private String img_url;
        private String count;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

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

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
/*
    *//**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"21","association_id":"15","album_name":"创建相册测试","status":"0","img_url":null,"remark":"大好时光","create_time":"1491725233","update_time":"1491725233"},{"id":"23","association_id":"15","album_name":"创建相册","status":"0","img_url":null,"remark":"大好时光","create_time":"1491725233","update_time":"1491725233"}]
     * serverTime : 2017-04-10 17:13:52
     *//*

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
        *//**
         * id : 21
         * association_id : 15
         * album_name : 创建相册测试
         * status : 0
         * img_url : null
         * remark : 大好时光
         * create_time : 1491725233
         * update_time : 1491725233
         *//*

        private String id;
        private String association_id;
        private String album_name;
        private String status;
        private String img_url;
        private String remark;
        private String create_time;
        private String update_time;

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

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }*/

}
