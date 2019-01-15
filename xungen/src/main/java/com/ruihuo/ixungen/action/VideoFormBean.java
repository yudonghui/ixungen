package com.ruihuo.ixungen.action;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/31
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoFormBean {

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"16","user_rid":"1000254","url":"20170531/15ac131f14ac1b39c1aaa7107dde3e95.mp4","remark":"啦啦啦啦啦啦","img":"https://timgsa.baidu.com/timg?","total_play":"3","create_time":"1496213795"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"18","user_rid":"1000254","url":"20170531/19db42aa8b6b9809af9d32db7a91221d.mp4","remark":"哈哈哈哈哈哈","img":"https://timgsa.baidu.com/timg?","total_play":"4","create_time":"1496214248"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"25","user_rid":"1000254","url":"20170531/fb2f9c6d1c47d490d99866f0935ce1ce.mov","remark":"很刚哈个咯啦\n","img":"https://timgsa.baidu.com/timg?","total_play":"1","create_time":"1496225329"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"26","user_rid":"1000254","url":"20170531/33b7793511d201556039a1f57f1a3ad3.mp4","remark":null,"img":null,"total_play":null,"create_time":"1496225943"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"27","user_rid":"1000254","url":"20170531/0b1275ff409653f1c520e3ab565afb9d.mp4","remark":null,"img":null,"total_play":null,"create_time":"1496225965"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"30","user_rid":"1000254","url":"20170531/e325ab6cce7d3ddd329f0762993f2c7c.mp4","remark":null,"img":null,"total_play":null,"create_time":"1496226901"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"41","user_rid":"1000254","url":"20170601/378e04ba22ac42ae30220aa924cb061a.mp4","remark":null,"img":null,"total_play":"0","create_time":"1496286557"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"42","user_rid":"1000254","url":"20170601/6b2612143960e22542fb38f83ed5b892.mp4","remark":null,"img":null,"total_play":"0","create_time":"1496288601"},{"rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","id":"43","user_rid":"1000254","url":"20170601/40ad0044779e4e06dacef6f457b90ce6.mp4","remark":null,"img":null,"total_play":"0","create_time":"1496289609"}]
     * serverTime : 2017-06-02 10:42:31
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
         * rid : 1000254
         * avatar : 9,024533026b1b
         * nikename : 你二大爷
         * id : 16
         * user_rid : 1000254
         * url : 20170531/15ac131f14ac1b39c1aaa7107dde3e95.mp4
         * remark : 啦啦啦啦啦啦
         * img : https://timgsa.baidu.com/timg?
         * total_play : 3
         * create_time : 1496213795
         */

        private String rid;
        private String avatar;
        private String nikename;
        private String id;
        private String user_rid;
        private String url;
        private String remark;
        private String img;
        private String total_play;
        private String create_time;

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

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

        public String getUser_rid() {
            return user_rid;
        }

        public void setUser_rid(String user_rid) {
            this.user_rid = user_rid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTotal_play() {
            return total_play;
        }

        public void setTotal_play(String total_play) {
            this.total_play = total_play;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
