package com.ruihuo.ixungen.entity;

import java.util.List;

public class HotNewsEntity {

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"2347","rid":"1000312","author":"网络","title":"寻找三百年来董氏迁徙路线和历史","summary":"我是董立，家在东北黑龙江哈尔滨东城区。最近偶然听起家里老人说过，祖上并不是东北人，是在抗战的时候迁徙过来的，所以想了解更多的关于整个姓氏的迁徙路线和变迁历史。希望寻找到志同道合的朋友，一起研究。\n                                                      \n                                                    电话:15136666666\n                                                    微信:369280741\n\n","picurl":null,"count":"2","create_time":"1501552603","status":"1","reply_number":"2","like":"0","isanonymous":"0","content_imgs":"","nikename":"董硕","avatar":"9,0518685d6820"},{"id":"2345","rid":"1000325","author":"网络","title":"于东辉","summary":"氏痛快量量模型\n\n","picurl":null,"count":"13","create_time":"1501473127","status":"1","reply_number":"37","like":"0","isanonymous":"0","content_imgs":"","nikename":"郁金香","avatar":"14,07fda5a96148"},{"id":"2344","rid":"1000312","author":"网络","title":"寻根问祖发布_测试","summary":"无痕呵呵呵呵呵呵呵呵呵呵呵呵呵\n时候\n","picurl":null,"count":"9","create_time":"1501472624","status":"1","reply_number":"0","like":"0","isanonymous":"0","content_imgs":"","nikename":"董硕","avatar":"9,0518685d6820"},{"id":"2343","rid":"1000312","author":"网络","title":"寻根问祖测试发布","summary":"我想要一个朋友也是这样啊哦那是什么的话就是哪里呢你自己要1\n你好ing我去给你\n你是红米新手机啊哦那是哪里呢你自己要你自己要你自己想想是不是这样想我一个朋友也是这样啊你自己想想是不是这样想和他在一起了\n","picurl":null,"count":"12","create_time":"1501227941","status":"1","reply_number":"4","like":"0","isanonymous":"0","content_imgs":"","nikename":"董硕","avatar":"9,0518685d6820"},{"id":"2342","rid":"1000254","author":"网络","title":"1241frgregqrg","summary":"Gqerrgqregerqgegqreg\nWgwgwgwgwgwegwgw\nGewgwegewgewgw\n","picurl":null,"count":"9","create_time":"1501227723","status":"1","reply_number":"0","like":"0","isanonymous":"0","content_imgs":"","nikename":"浪子","avatar":"9,08065ad83ed8"},{"id":"2341","rid":"1000325","author":"网络","title":"图我也","summary":null,"picurl":null,"count":"6","create_time":"1501226798","status":"1","reply_number":"0","like":"0","isanonymous":"0","content_imgs":"","nikename":"郁金香","avatar":"14,07fda5a96148"},{"id":"2340","rid":"1000312","author":"网络","title":"寻找董氏祠堂","summary":null,"picurl":null,"count":"10","create_time":"1500973930","status":"1","reply_number":"1","like":"0","isanonymous":"0","content_imgs":"","nikename":"董硕","avatar":"9,0518685d6820"}]
     */

    private int code;
    private String msg;
    private int totalPage;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2347
         * rid : 1000312
         * author : 网络
         * title : 寻找三百年来董氏迁徙路线和历史
         * summary : 我是董立，家在东北黑龙江哈尔滨东城区。最近偶然听起家里老人说过，祖上并不是东北人，是在抗战的时候迁徙过来的，所以想了解更多的关于整个姓氏的迁徙路线和变迁历史。希望寻找到志同道合的朋友，一起研究。
         * <p>
         * 电话:15136666666
         * 微信:369280741
         * <p>
         * <p>
         * picurl : null
         * count : 2
         * create_time : 1501552603
         * status : 1
         * reply_number : 2
         * like : 0
         * isanonymous : 0
         * content_imgs :
         * nikename : 董硕
         * avatar : 9,0518685d6820
         */

        private String id;
        private String rid;
        private String author;
        private String title;
        private String summary;
        private String picurl;
        private String count;
        private String create_time;
        private String status;
        private String reply_number;
        private String like;
        private String isanonymous;
        private String content_imgs;
        private String city;
        private String nikename;
        private String avatar;

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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReply_number() {
            return reply_number;
        }

        public void setReply_number(String reply_number) {
            this.reply_number = reply_number;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getIsanonymous() {
            return isanonymous;
        }

        public void setIsanonymous(String isanonymous) {
            this.isanonymous = isanonymous;
        }

        public String getContent_imgs() {
            return content_imgs;
        }

        public void setContent_imgs(String content_imgs) {
            this.content_imgs = content_imgs;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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
    }
}