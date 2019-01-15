package com.ruihuo.ixungen.activity.merchant;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/3
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopsPhotoFormBean implements Serializable{

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"28","shop_id":"11","album_id":"1","img":"https://res.ixungen.cn/img/8,0913a4876bb9","create_time":"1501740634"},{"id":"29","shop_id":"11","album_id":"1","img":"https://res.ixungen.cn/img/12,0914ce6fc726","create_time":"1501740634"}]
     * serverTime : 2017-08-03 14:11:32
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
         * id : 28
         * shop_id : 11
         * album_id : 1
         * img : https://res.ixungen.cn/img/8,0913a4876bb9
         * create_time : 1501740634
         */

        private String id;
        private String shop_id;
        private String album_id;
        private String img;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
