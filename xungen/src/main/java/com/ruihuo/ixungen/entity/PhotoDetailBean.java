package com.ruihuo.ixungen.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/8
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoDetailBean implements Serializable{

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"4","album_id":"63","img_url":"14,029a407c55c8","create_time":"1494224042"},{"id":"5","album_id":"63","img_url":"14,029b10e62f10","create_time":"1494224042"}]
     * serverTime : 2017-05-08 15:04:06
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
         * id : 4
         * album_id : 63
         * img_url : 14,029a407c55c8
         * create_time : 1494224042
         */

        private String id;
        private String album_id;
        private String img_url;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
