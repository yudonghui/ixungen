package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/31
 * @describe May the Buddha bless bug-free!!!
 */
public class XWFormDetailBean {

    /**
     * code : 0
     * msg : success
     * data : {"id":2345,"type":"glean","bundle":1,"title":"于东辉","subtitle":null,"summary":"氏痛快量量模型\n\n","content":"","picurl":null,"rid":1000325,"author":"郁金香","like":0,"status":1,"count":13,"create_time":1501473127,"public_time":0,"update_time":1501473127,"isanonymous":0,"reply_number":35,"content_imgs":"","nikename":"郁金香","avatar":"14,07fda5a96148","comment_info":[{"id":103,"topic_id":2345,"comment_rid":1000312,"reply_rid":"1000312","content":"食品","comment_time":1501481102,"people_like":0,"nikename":"董硕","avatar":"9,0518685d6820","reply_info":[{"reply_id":128,"comment_id":103,"reply_rid":1000312,"to_rid":1000312,"reply_content":"极乐汤","reply_time":1501481168,"nikename":"董硕","to_nikename":"董硕"},{"reply_id":132,"comment_id":103,"reply_rid":1000312,"to_rid":1000312,"reply_content":"E12e122","reply_time":1501492543,"nikename":"董硕","to_nikename":"董硕"}]},{"id":104,"topic_id":2345,"comment_rid":1000312,"reply_rid":"1000325","content":"GPS地","comment_time":1501481109,"people_like":0,"nikename":"董硕","avatar":"9,0518685d6820","reply_info":[{"reply_id":130,"comment_id":104,"reply_rid":1000325,"to_rid":1000312,"reply_content":"图图他","reply_time":1501491144,"nikename":"郁金香","to_nikename":"董硕"}]},{"id":105,"topic_id":2345,"comment_rid":1000312,"reply_rid":"1000312","content":"14个","comment_time":1501481120,"people_like":0,"nikename":"董硕","avatar":"9,0518685d6820","reply_info":[{"reply_id":131,"comment_id":105,"reply_rid":1000312,"to_rid":0,"reply_content":"1312321321","reply_time":1501492535,"nikename":"董硕","to_nikename":null}]}]}
     * serverTime : 2017-07-31 18:23:39
     */

    private int code;
    private String msg;
    private DataBean data;
    private String serverTime;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean {
        /**
         * id : 2345
         * type : glean
         * bundle : 1
         * title : 于东辉
         * subtitle : null
         * summary : 氏痛快量量模型
         * <p>
         * <p>
         * content :
         * picurl : null
         * rid : 1000325
         * author : 郁金香
         * like : 0
         * status : 1
         * count : 13
         * create_time : 1501473127
         * public_time : 0
         * update_time : 1501473127
         * isanonymous : 0
         * reply_number : 35
         * content_imgs :
         * nikename : 郁金香
         * avatar : 14,07fda5a96148
         * comment_info : [{"id":103,"topic_id":2345,"comment_rid":1000312,"reply_rid":"1000312","content":"食品","comment_time":1501481102,"people_like":0,"nikename":"董硕","avatar":"9,0518685d6820","reply_info":[{"reply_id":128,"comment_id":103,"reply_rid":1000312,"to_rid":1000312,"reply_content":"极乐汤","reply_time":1501481168,"nikename":"董硕","to_nikename":"董硕"},{"reply_id":132,"comment_id":103,"reply_rid":1000312,"to_rid":1000312,"reply_content":"E12e122","reply_time":1501492543,"nikename":"董硕","to_nikename":"董硕"}]},{"id":104,"topic_id":2345,"comment_rid":1000312,"reply_rid":"1000325","content":"GPS地","comment_time":1501481109,"people_like":0,"nikename":"董硕","avatar":"9,0518685d6820","reply_info":[{"reply_id":130,"comment_id":104,"reply_rid":1000325,"to_rid":1000312,"reply_content":"图图他","reply_time":1501491144,"nikename":"郁金香","to_nikename":"董硕"}]},{"id":105,"topic_id":2345,"comment_rid":1000312,"reply_rid":"1000312","content":"14个","comment_time":1501481120,"people_like":0,"nikename":"董硕","avatar":"9,0518685d6820","reply_info":[{"reply_id":131,"comment_id":105,"reply_rid":1000312,"to_rid":0,"reply_content":"1312321321","reply_time":1501492535,"nikename":"董硕","to_nikename":null}]}]
         */

        private int id;
        private String type;
        private int bundle;
        private String title;
        private String subtitle;
        private String summary;
        private String content;
        private String picurl;
        private int rid;
        private String author;
        private int like;
        private int status;
        private String count;
        private String create_time;
        private int public_time;
        private String update_time;
        private int isanonymous;
        private int reply_number;
        private String content_imgs;
        private String nikename;
        private String avatar;
        private List<CommentInfoBean> comment_info;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getBundle() {
            return bundle;
        }

        public void setBundle(int bundle) {
            this.bundle = bundle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public int getPublic_time() {
            return public_time;
        }

        public void setPublic_time(int public_time) {
            this.public_time = public_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getIsanonymous() {
            return isanonymous;
        }

        public void setIsanonymous(int isanonymous) {
            this.isanonymous = isanonymous;
        }

        public int getReply_number() {
            return reply_number;
        }

        public void setReply_number(int reply_number) {
            this.reply_number = reply_number;
        }

        public String getContent_imgs() {
            return content_imgs;
        }

        public void setContent_imgs(String content_imgs) {
            this.content_imgs = content_imgs;
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

        public List<CommentInfoBean> getComment_info() {
            return comment_info;
        }

        public void setComment_info(List<CommentInfoBean> comment_info) {
            this.comment_info = comment_info;
        }

        public static class CommentInfoBean extends CommentBaseBeand {

        }
    }
}
