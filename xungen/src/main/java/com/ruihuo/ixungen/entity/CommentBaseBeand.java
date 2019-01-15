package com.ruihuo.ixungen.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/10
 * @describe May the Buddha bless bug-free!!!
 */
public class CommentBaseBeand {
    /**
     * id : 103
     * topic_id : 2345
     * comment_rid : 1000312
     * reply_rid : 1000312
     * content : 食品
     * comment_time : 1501481102
     * people_like : 0
     * nikename : 董硕
     * avatar : 9,0518685d6820
     * reply_info : [{"reply_id":128,"comment_id":103,"reply_rid":1000312,"to_rid":1000312,"reply_content":"极乐汤","reply_time":1501481168,"nikename":"董硕","to_nikename":"董硕"},{"reply_id":132,"comment_id":103,"reply_rid":1000312,"to_rid":1000312,"reply_content":"E12e122","reply_time":1501492543,"nikename":"董硕","to_nikename":"董硕"}]
     */

    private String id;
    private String pid;
    private String rid;
    private String img;
    @SerializedName("private")
    private String privateX;
    private String total_reply;
    private String total_like;
    private String create_time;
    private String topic_id;
    private String comment_rid;
    private String reply_rid;
    private String content;
    private String comment_time;
    private String people_like;
    private String nikename;
    private String avatar;
    private List<ReplyInfoBean> reply_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getComment_rid() {
        return comment_rid;
    }

    public void setComment_rid(String comment_rid) {
        this.comment_rid = comment_rid;
    }

    public String getReply_rid() {
        return reply_rid;
    }

    public void setReply_rid(String reply_rid) {
        this.reply_rid = reply_rid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getPeople_like() {
        return people_like;
    }

    public void setPeople_like(String people_like) {
        this.people_like = people_like;
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

    public List<ReplyInfoBean> getReply_info() {
        return reply_info;
    }

    public void setReply_info(List<ReplyInfoBean> reply_info) {
        this.reply_info = reply_info;
    }

    public static class ReplyInfoBean {
        /**
         * reply_id : 128
         * comment_id : 103
         * reply_rid : 1000312
         * to_rid : 1000312
         * reply_content : 极乐汤
         * reply_time : 1501481168
         * nikename : 董硕
         * to_nikename : 董硕
         */

        private String reply_id;
        private String comment_id;
        private String reply_rid;
        private String to_rid;
        private String reply_content;
        private String reply_time;
        private String nikename;
        private String to_nikename;

        public String getReply_id() {
            return reply_id;
        }

        public void setReply_id(String reply_id) {
            this.reply_id = reply_id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getReply_rid() {
            return reply_rid;
        }

        public void setReply_rid(String reply_rid) {
            this.reply_rid = reply_rid;
        }

        public String getTo_rid() {
            return to_rid;
        }

        public void setTo_rid(String to_rid) {
            this.to_rid = to_rid;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getReply_time() {
            return reply_time;
        }

        public void setReply_time(String reply_time) {
            this.reply_time = reply_time;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getTo_nikename() {
            return to_nikename;
        }

        public void setTo_nikename(String to_nikename) {
            this.to_nikename = to_nikename;
        }
    }
}
