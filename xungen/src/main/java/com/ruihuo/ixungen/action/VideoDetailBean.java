package com.ruihuo.ixungen.action;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/6/1
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoDetailBean {

    /**
     * code : 0
     * msg : success
     * data : {"id":18,"user_rid":1000254,"url":"20170531/f4498e34b8e61037cbd56356eaa51398.mp4","remark":null,"img":null,"total_play":null,"create_time":1496214248,"comment_info":[{"id":62,"video_id":18,"comment_rid":1000123,"comment_name":"","comment_avtar":"","reply_rid":"","content":"视频不错","comment_time":2147483647,"people_like":0,"reply_info":[{"reply_id":1,"comment_id":62,"reply_name":"熊大","reply_rid":"1000500","to_name":"光头强","to_rid":1000123,"reply_content":"那是必须的","reply_time":0},{"reply_id":128,"comment_id":62,"reply_name":"光头强","reply_rid":"1000123","to_name":"熊大","to_rid":1000,"reply_content":"呵呵。。。","reply_time":0}]},{"id":63,"video_id":18,"comment_rid":1000124,"comment_name":"","comment_avtar":"","reply_rid":"","content":"nice","comment_time":2147483647,"people_like":0,"reply_info":[]}]}
     * serverTime : 2017-05-31 17:43:11
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
         * id : 18
         * user_rid : 1000254
         * url : 20170531/f4498e34b8e61037cbd56356eaa51398.mp4
         * remark : null
         * img : null
         * total_play : null
         * create_time : 1496214248
         * comment_info : [{"id":62,"video_id":18,"comment_rid":1000123,"comment_name":"","comment_avtar":"","reply_rid":"","content":"视频不错","comment_time":2147483647,"people_like":0,"reply_info":[{"reply_id":1,"comment_id":62,"reply_name":"熊大","reply_rid":"1000500","to_name":"光头强","to_rid":1000123,"reply_content":"那是必须的","reply_time":0},{"reply_id":128,"comment_id":62,"reply_name":"光头强","reply_rid":"1000123","to_name":"熊大","to_rid":1000,"reply_content":"呵呵。。。","reply_time":0}]},{"id":63,"video_id":18,"comment_rid":1000124,"comment_name":"","comment_avtar":"","reply_rid":"","content":"nice","comment_time":2147483647,"people_like":0,"reply_info":[]}]
         */

        private String id;
        private String user_rid;
        private String url;
        private String remark;
        private String img;
        private String total_play;
        private String create_time;
        private String total_like;

        public String getTotal_like() {
            return total_like;
        }

        public void setTotal_like(String total_like) {
            this.total_like = total_like;
        }

        private List<CommentInfoBean> comment_info;

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

        public List<CommentInfoBean> getComment_info() {
            return comment_info;
        }

        public void setComment_info(List<CommentInfoBean> comment_info) {
            this.comment_info = comment_info;
        }

        public static class CommentInfoBean {
            /**
             * id : 62
             * video_id : 18
             * comment_rid : 1000123
             * comment_name : 
             * comment_avtar : 
             * reply_rid : 
             * content : 视频不错
             * comment_time : 2147483647
             * people_like : 0
             * reply_info : [{"reply_id":1,"comment_id":62,"reply_name":"熊大","reply_rid":"1000500","to_name":"光头强","to_rid":1000123,"reply_content":"那是必须的","reply_time":0},{"reply_id":128,"comment_id":62,"reply_name":"光头强","reply_rid":"1000123","to_name":"熊大","to_rid":1000,"reply_content":"呵呵。。。","reply_time":0}]
             */

            private String id;
            private String video_id;
            private String comment_rid;
            private String comment_name;
            private String comment_avtar;
            private String reply_rid;
            private String content;
            private String comment_time;
            private String people_like;
            private List<ReplyInfoBean> reply_info;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getComment_rid() {
                return comment_rid;
            }

            public void setComment_rid(String comment_rid) {
                this.comment_rid = comment_rid;
            }

            public String getComment_name() {
                return comment_name;
            }

            public void setComment_name(String comment_name) {
                this.comment_name = comment_name;
            }

            public String getComment_avtar() {
                return comment_avtar;
            }

            public void setComment_avtar(String comment_avtar) {
                this.comment_avtar = comment_avtar;
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

            public List<ReplyInfoBean> getReply_info() {
                return reply_info;
            }

            public void setReply_info(List<ReplyInfoBean> reply_info) {
                this.reply_info = reply_info;
            }

            public static class ReplyInfoBean {
                /**
                 * reply_id : 1
                 * comment_id : 62
                 * reply_name : 熊大
                 * reply_rid : 1000500
                 * to_name : 光头强
                 * to_rid : 1000123
                 * reply_content : 那是必须的
                 * reply_time : 0
                 */

                private String reply_id;
                private String comment_id;
                private String reply_name;
                private String reply_rid;
                private String to_name;
                private String to_rid;
                private String reply_content;
                private String reply_time;

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

                public String getReply_name() {
                    return reply_name;
                }

                public void setReply_name(String reply_name) {
                    this.reply_name = reply_name;
                }

                public String getReply_rid() {
                    return reply_rid;
                }

                public void setReply_rid(String reply_rid) {
                    this.reply_rid = reply_rid;
                }

                public String getTo_name() {
                    return to_name;
                }

                public void setTo_name(String to_name) {
                    this.to_name = to_name;
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
            }
        }
    }
}
