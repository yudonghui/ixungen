package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/19
 * @describe May the Buddha bless bug-free!!!
 */
public class MovieBean {

    /**
     * code : 0
     * msg : success
     * data : {"ads":[{"id":"11","pic":"/480x320/13,045254ffdc52","url":"http://www.baidu.com","create_time":"22222","status":"0"}],"portals":[{"id":"9","name":"影视演员库","img_url":"https://res.ixungen.cn/img/13,03fed4d1fdd2","summary":"炎黄演员聚集地","link":"http://www.ixungen.cn","sort":"10","type":"1","status":"0","create_time":"2147483647"},{"id":"10","name":"演员报名","img_url":"https://res.ixungen.cn/img/9,03fd8590039f","summary":"演员报名","link":"http://www.ixungen.cn","sort":"10","type":"1","status":"0","create_time":"2147483647"}],"tidbits":[{"id":"45","relation_id":"0","activity_id":"2","episod_info_id":"1","title":"国学少年颁奖晚会","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_guoxueshaonian.mp4","info":"国学","img":"https://res.ixungen.cn/img/360x200/11,04516d08bbe0","sort":"10","total_play":"15","total_like":"13","total_comment":"22","create_time":"1496842723"},{"id":"49","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第二届全国体育院校联赛","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"},{"id":"50","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第三届全国体育院校联赛","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"},{"id":"51","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第四届全国体育院校联赛","type":"1","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"}],"video":[{"id":"44","relation_id":"0","activity_id":"2","episod_info_id":"1","title":"国学少年颁奖晚会","type":"0","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_guoxueshaonian.mp4","info":"国学","img":"https://res.ixungen.cn/img/360x200/11,04516d08bbe0","sort":"10","total_play":"365","total_like":"14","total_comment":"63","create_time":"1496842723"},{"id":"48","relation_id":"0","activity_id":"2","episod_info_id":"0","title":"第二届全国体育院校联赛","type":"0","status":"1","state":"1","episod":"1","url":"https://api.ixungen.cn/video/20170607_lanqiusai.mp4","info":"2017届全国体育院校篮球联赛（SCBA）冠亚军队将于6月13日在大别山（麻城体育馆）开赛","img":"https://res.ixungen.cn/img/360x200/14,04365dcc152d","sort":"10","total_play":"23","total_like":"0","total_comment":"1","create_time":"1496842723"}]}
     * serverTime : 2017-07-19 14:56:33
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
        private List<AdsBean> ads;
        private List<PortalsBean> portals;
        private List<TidbitsBean> tidbits;
        private List<VideoBean> video;

        public List<AdsBean> getAds() {
            return ads;
        }

        public void setAds(List<AdsBean> ads) {
            this.ads = ads;
        }

        public List<PortalsBean> getPortals() {
            return portals;
        }

        public void setPortals(List<PortalsBean> portals) {
            this.portals = portals;
        }

        public List<TidbitsBean> getTidbits() {
            return tidbits;
        }

        public void setTidbits(List<TidbitsBean> tidbits) {
            this.tidbits = tidbits;
        }

        public List<VideoBean> getVideo() {
            return video;
        }

        public void setVideo(List<VideoBean> video) {
            this.video = video;
        }

        public static class AdsBean {
            /**
             * id : 11
             * pic : /480x320/13,045254ffdc52
             * url : http://www.baidu.com
             * create_time : 22222
             * status : 0
             */

            private String id;
            private String pic;
            private String url;
            private String create_time;
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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
        }

        public static class PortalsBean {
            /**
             * id : 9
             * name : 影视演员库
             * img_url : https://res.ixungen.cn/img/13,03fed4d1fdd2
             * summary : 炎黄演员聚集地
             * link : http://www.ixungen.cn
             * sort : 10
             * type : 1
             * status : 0
             * create_time : 2147483647
             */

            private String id;
            private String name;
            private String img_url;
            private String summary;
            private String link;
            private String sort;
            private String type;
            private String status;
            private String create_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
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

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }
        }

        public static class TidbitsBean extends VideoBaseBean {

        }

        public static class VideoBean extends VideoBaseBean {

        }
    }
}
