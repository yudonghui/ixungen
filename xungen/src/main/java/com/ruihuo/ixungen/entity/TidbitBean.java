package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/20
 * @describe May the Buddha bless bug-free!!!
 */
public class TidbitBean {

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"45","relation_id":"0","activity_id":"2","episod_info_id":"1","title":"国学少年颁奖晚会","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_guoxueshaonian.mp4","info":"国学","img":"https://res.ixungen.cn/img/360x200/11,04516d08bbe0","sort":"10","total_play":"15","total_like":"13","total_comment":"22","create_time":"1496842723"},{"id":"49","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第二届全国体育院校联赛","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"},{"id":"50","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第三届全国体育院校联赛","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"},{"id":"51","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第四届全国体育院校联赛","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"}]
     * serverTime : 2017-07-20 14:51:13
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
         * relation_id : 0
         * activity_id : 2
         * episod_info_id : 1
         * title : 国学少年颁奖晚会
         * type : 1
         * status : 1
         * state : 1
         * episod : 1
         * url : https://api.ixungen.cn/video/20170607_guoxueshaonian.mp4
         * info : 国学
         * img : https://res.ixungen.cn/img/360x200/11,04516d08bbe0
         * sort : 10
         * total_play : 15
         * total_like : 13
         * total_comment : 22
         * create_time : 1496842723
         */

        private String id;
        private String relation_id;
        private String activity_id;
        private String episod_info_id;
        private String title;
        private String type;
        private String status;
        private String state;
        private String episod;
        private String url;
        private String info;
        private String img;
        private String sort;
        private String total_play;
        private String total_like;
        private String total_comment;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(String relation_id) {
            this.relation_id = relation_id;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getEpisod_info_id() {
            return episod_info_id;
        }

        public void setEpisod_info_id(String episod_info_id) {
            this.episod_info_id = episod_info_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getEpisod() {
            return episod;
        }

        public void setEpisod(String episod) {
            this.episod = episod;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getTotal_play() {
            return total_play;
        }

        public void setTotal_play(String total_play) {
            this.total_play = total_play;
        }

        public String getTotal_like() {
            return total_like;
        }

        public void setTotal_like(String total_like) {
            this.total_like = total_like;
        }

        public String getTotal_comment() {
            return total_comment;
        }

        public void setTotal_comment(String total_comment) {
            this.total_comment = total_comment;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
