package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/17
 * @describe May the Buddha bless bug-free!!!
 */
public class PostsSmsBean {

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"45","topic_id":"175","content":"第一次评论","comment_time":"1494559961","comment_rid":"1000254","nikename":"董硕123","avatar":"11,0297d4b24367","rid":"1000286","title":"我要提问","comment_info":{"comment_id":"45","reply_rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","to_rid":"1000286","to_nikename":"董硕123","to_avatar":"11,0297d4b24367","reply_comtent":"第一次评论","reply_time":"1494559961"},"reply_info":[{"id":"45","topic_id":"175","reply_id":"65","reply_content":"第一次回复","reply_time":"1494559976","reply_rid":"1000286","to_rid":"1000254","nikename":"董硕123","avatar":"11,0297d4b24367","to_nikename":"你二大爷","to_avatar":"9,024533026b1b"},{"id":"45","topic_id":"175","reply_id":"66","reply_content":"第二场比赛","reply_time":"1494561756","reply_rid":"1000286","to_rid":"1000254","nikename":"董硕123","avatar":"11,0297d4b24367","to_nikename":"你二大爷","to_avatar":"9,024533026b1b"}],"reply_comtent":"第一次评论","reply_nikename":"你二大爷","reply_time":"1494559961","reply_rid":"1000254","reply_avatar":"9,024533026b1b","to_nikename":"董硕123","to_avatar":"11,0297d4b24367","to_rid":"1000286"}]
     * serverTime : 2017-05-12 13:00:41
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
         * id : 45
         * topic_id : 175
         * content : 第一次评论
         * comment_time : 1494559961
         * comment_rid : 1000254
         * nikename : 董硕123
         * avatar : 11,0297d4b24367
         * rid : 1000286
         * title : 我要提问
         * comment_info : {"comment_id":"45","reply_rid":"1000254","avatar":"9,024533026b1b","nikename":"你二大爷","to_rid":"1000286","to_nikename":"董硕123","to_avatar":"11,0297d4b24367","reply_comtent":"第一次评论","reply_time":"1494559961"}
         * reply_info : [{"id":"45","topic_id":"175","reply_id":"65","reply_content":"第一次回复","reply_time":"1494559976","reply_rid":"1000286","to_rid":"1000254","nikename":"董硕123","avatar":"11,0297d4b24367","to_nikename":"你二大爷","to_avatar":"9,024533026b1b"},{"id":"45","topic_id":"175","reply_id":"66","reply_content":"第二场比赛","reply_time":"1494561756","reply_rid":"1000286","to_rid":"1000254","nikename":"董硕123","avatar":"11,0297d4b24367","to_nikename":"你二大爷","to_avatar":"9,024533026b1b"}]
         * reply_comtent : 第一次评论
         * reply_nikename : 你二大爷
         * reply_time : 1494559961
         * reply_rid : 1000254
         * reply_avatar : 9,024533026b1b
         * to_nikename : 董硕123
         * to_avatar : 11,0297d4b24367
         * to_rid : 1000286
         */

        private String id;
        private String topic_id;
        private String content;
        private String comment_time;
        private String comment_rid;
        private String nikename;
        private String avatar;
        private String rid;
        private String title;
        private CommentInfoBean comment_info;
        private String reply_content;
        private String reply_nikename;
        private String reply_time;
        private String reply_rid;
        private String reply_avatar;
        private String to_nikename;
        private String to_avatar;
        private String to_rid;
        private String type;
        private List<ReplyInfoBean> reply_info;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

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

        public String getComment_rid() {
            return comment_rid;
        }

        public void setComment_rid(String comment_rid) {
            this.comment_rid = comment_rid;
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

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public CommentInfoBean getComment_info() {
            return comment_info;
        }

        public void setComment_info(CommentInfoBean comment_info) {
            this.comment_info = comment_info;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getReply_nikename() {
            return reply_nikename;
        }

        public void setReply_nikename(String reply_nikename) {
            this.reply_nikename = reply_nikename;
        }

        public String getReply_time() {
            return reply_time;
        }

        public void setReply_time(String reply_time) {
            this.reply_time = reply_time;
        }

        public String getReply_rid() {
            return reply_rid;
        }

        public void setReply_rid(String reply_rid) {
            this.reply_rid = reply_rid;
        }

        public String getReply_avatar() {
            return reply_avatar;
        }

        public void setReply_avatar(String reply_avatar) {
            this.reply_avatar = reply_avatar;
        }

        public String getTo_nikename() {
            return to_nikename;
        }

        public void setTo_nikename(String to_nikename) {
            this.to_nikename = to_nikename;
        }

        public String getTo_avatar() {
            return to_avatar;
        }

        public void setTo_avatar(String to_avatar) {
            this.to_avatar = to_avatar;
        }

        public String getTo_rid() {
            return to_rid;
        }

        public void setTo_rid(String to_rid) {
            this.to_rid = to_rid;
        }

        public List<ReplyInfoBean> getReply_info() {
            return reply_info;
        }

        public void setReply_info(List<ReplyInfoBean> reply_info) {
            this.reply_info = reply_info;
        }

        public static class CommentInfoBean {
            /**
             * comment_id : 45
             * reply_rid : 1000254
             * avatar : 9,024533026b1b
             * nikename : 你二大爷
             * to_rid : 1000286
             * to_nikename : 董硕123
             * to_avatar : 11,0297d4b24367
             * reply_content : 第一次评论
             * reply_time : 1494559961
             */

            private String comment_id;
            private String reply_rid;
            private String avatar;
            private String nikename;
            private String to_rid;
            private String to_nikename;
            private String to_avatar;
            private String reply_content;
            private String reply_time;

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

            public String getTo_rid() {
                return to_rid;
            }

            public void setTo_rid(String to_rid) {
                this.to_rid = to_rid;
            }

            public String getTo_nikename() {
                return to_nikename;
            }

            public void setTo_nikename(String to_nikename) {
                this.to_nikename = to_nikename;
            }

            public String getTo_avatar() {
                return to_avatar;
            }

            public void setTo_avatar(String to_avatar) {
                this.to_avatar = to_avatar;
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
        }

        public static class ReplyInfoBean {
            /**
             * id : 45
             * topic_id : 175
             * reply_id : 65
             * reply_content : 第一次回复
             * reply_time : 1494559976
             * reply_rid : 1000286
             * to_rid : 1000254
             * nikename : 董硕123
             * avatar : 11,0297d4b24367
             * to_nikename : 你二大爷
             * to_avatar : 9,024533026b1b
             */

            private String id;
            private String topic_id;
            private String reply_id;
            private String reply_content;
            private String reply_time;
            private String reply_rid;
            private String to_rid;
            private String nikename;
            private String avatar;
            private String to_nikename;
            private String to_avatar;

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

            public String getReply_id() {
                return reply_id;
            }

            public void setReply_id(String reply_id) {
                this.reply_id = reply_id;
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

            public String getTo_nikename() {
                return to_nikename;
            }

            public void setTo_nikename(String to_nikename) {
                this.to_nikename = to_nikename;
            }

            public String getTo_avatar() {
                return to_avatar;
            }

            public void setTo_avatar(String to_avatar) {
                this.to_avatar = to_avatar;
            }
        }
    }
}
