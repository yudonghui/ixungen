package com.ruihuo.ixungen.activity.merchant;

import com.ruihuo.ixungen.entity.ScrollBaseBean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/25
 * @describe May the Buddha bless bug-free!!!
 */
public class TravelBean {


    /**
     * code : 0
     * msg : 成功
     * totalPage : 1
     * data : {"ad":[{"id":"11","pic":"https://res.ixungen.cn/img/13,045254ffdc52","url":null,"create_time":"2147483647","status":"1"}],"tour_portal":[{"id":"1","name":"麻城旅游","img_url":"https://res.ixungen.cn/img/13,0be0f90263a1","summary":"聚集麻城特色景点","link":"","sort":"10","type":"7","status":"0","create_time":"2147483647"},{"id":"2","name":"麻城酒店","img_url":"https://res.ixungen.cn/img/11,0be198a2efa1","summary":"各色酒店名宿","link":"","sort":"10","type":"8","status":"0","create_time":"2147483647"},{"id":"3","name":"麻城美食","img_url":"https://res.ixungen.cn/img/10,0be24a2ff9ae","summary":"汇聚麻城传统特色美食","link":"","sort":"10","type":"9","status":"0","create_time":"2147483647"}],"popular_scenic_spot":[{"id":"19","consumption_per_person":"167","level":"4","shop_card_a":"http://res.ixungen.cn/img/9,0b5fedd9e299","shop_card_b":"http://res.ixungen.cn/img/13,0b60ef8355eb","type":"4","shop_name":"龟峰山风景区","total_comment":"3"}],"recommend_scenic_spot":[{"id":"19","consumption_per_person":"167","level":"4","shop_card_a":"http://res.ixungen.cn/img/9,0b5fedd9e299","shop_card_b":"http://res.ixungen.cn/img/13,0b60ef8355eb","type":"4","shop_name":"龟峰山风景区","total_comment":"3"}]}
     * serverTime : 2017-08-25 11:08:12
     */

    private int code;
    private String msg;
    private int totalPage;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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
        private List<AdBean> ad;
        private List<TourPortalBean> tour_portal;
        private List<PopularScenicSpotBean> popular_scenic_spot;
        private List<RecommendScenicSpotBean> recommend_scenic_spot;

        public List<AdBean> getAd() {
            return ad;
        }

        public void setAd(List<AdBean> ad) {
            this.ad = ad;
        }

        public List<TourPortalBean> getTour_portal() {
            return tour_portal;
        }

        public void setTour_portal(List<TourPortalBean> tour_portal) {
            this.tour_portal = tour_portal;
        }

        public List<PopularScenicSpotBean> getPopular_scenic_spot() {
            return popular_scenic_spot;
        }

        public void setPopular_scenic_spot(List<PopularScenicSpotBean> popular_scenic_spot) {
            this.popular_scenic_spot = popular_scenic_spot;
        }

        public List<RecommendScenicSpotBean> getRecommend_scenic_spot() {
            return recommend_scenic_spot;
        }

        public void setRecommend_scenic_spot(List<RecommendScenicSpotBean> recommend_scenic_spot) {
            this.recommend_scenic_spot = recommend_scenic_spot;
        }

        public static class AdBean extends ScrollBaseBean{
        }

        public static class TourPortalBean {
            /**
             * id : 1
             * name : 麻城旅游
             * img_url : https://res.ixungen.cn/img/13,0be0f90263a1
             * summary : 聚集麻城特色景点
             * link :
             * sort : 10
             * type : 7
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

        public static class PopularScenicSpotBean {
            /**
             * id : 19
             * consumption_per_person : 167
             * level : 4
             * shop_card_a : http://res.ixungen.cn/img/9,0b5fedd9e299
             * shop_card_b : http://res.ixungen.cn/img/13,0b60ef8355eb
             * type : 4
             * shop_name : 龟峰山风景区
             * total_comment : 3
             */

            private String id;
            private String consumption_per_person;
            private String level;
            private String shop_card_a;
            private String shop_card_b;
            private String type;
            private String shop_name;
            private String total_comment;
            private String cover;

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getConsumption_per_person() {
                return consumption_per_person;
            }

            public void setConsumption_per_person(String consumption_per_person) {
                this.consumption_per_person = consumption_per_person;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getShop_card_a() {
                return shop_card_a;
            }

            public void setShop_card_a(String shop_card_a) {
                this.shop_card_a = shop_card_a;
            }

            public String getShop_card_b() {
                return shop_card_b;
            }

            public void setShop_card_b(String shop_card_b) {
                this.shop_card_b = shop_card_b;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getShop_name() {
                return shop_name;
            }

            public void setShop_name(String shop_name) {
                this.shop_name = shop_name;
            }

            public String getTotal_comment() {
                return total_comment;
            }

            public void setTotal_comment(String total_comment) {
                this.total_comment = total_comment;
            }
        }

        public static class RecommendScenicSpotBean extends NearbyShopBaseBean{
        }
    }
}
