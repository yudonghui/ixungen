package com.ruihuo.ixungen.activity.merchant;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/8
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopsInfoBean {

    /**
     * code : 0
     * msg : success
     * data : {"shop_info":{"id":"1","shop_name":"寻根小卖铺","address":"陆家嘴一号","shop_time":"早八点到早八点","business_type":"餐饮","mobile":"15136778516","text":"这是一个实现梦想的地方，梦开始的地方","region":"湖北省,黄冈市,麻城市","shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","consumption_per_person":"188","level":"4","check_in_time":"0","check_out_time":"0","service":"","total_comment":"0","total_img":"0","logo":""},"recommend_goods":[{"id":"1","shop_id":"1","status":"2","name":"红烧鱼","type":"0","classify":"","cover":"","price":"58","info":null,"amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":null},{"id":"2","shop_id":"1","status":"2","name":"红烧排骨","type":"0","classify":"","cover":"","price":"88","info":null,"amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":null},{"id":"3","shop_id":"1","status":"2","name":"麻婆豆腐","type":"0","classify":"","cover":"","price":"28","info":null,"amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":null},{"id":"50","shop_id":"1","status":"0","name":"钓鱼伞架","type":"1","classify":"","cover":"https://res.ixungen.cn/img/9,0a3b43441807","price":"0","info":"","amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":"1502329503"},{"id":"51","shop_id":"1","status":"1","name":"标间","type":"1","classify":"","cover":"https://res.ixungen.cn/img/9,0a3c0aaa6485","price":"1","info":"","amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":"1502329563"}],"comment":[{"id":"133","shop_id":"1","comment_rid":"1000254","comment_name":"","comment_avtar":"","reply_rid":"","content":"不错","cost":"0","score":"0","img":"","comment_time":"0","people_like":"0","total_reply":"1"}],"nearby_shop":[{"id":"1","consumption_per_person":"188","level":"4","shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","type":"2","shop_name":"寻根小卖铺","total_comment":"0"},{"id":"11","consumption_per_person":"288","level":"5","shop_card_a":"https://res.ixungen.cn/img/12,07d13570e72d","shop_card_b":"https://res.ixungen.cn/img/9,07d3e2901047","type":"0","shop_name":"于氏小店","total_comment":"0"},{"id":"12","consumption_per_person":"328","level":"4","shop_card_a":"http://res.ixungen.cn/img/11,07f783fd8475","shop_card_b":"http://res.ixungen.cn/img/12,07f8176a7b3d","type":"0","shop_name":"麻辣烫","total_comment":"0"},{"id":"13","consumption_per_person":"158","level":"3","shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","type":"0","shop_name":"店小二","total_comment":"0"}]}
     * serverTime : 2017-08-10 10:30:42
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
         * shop_info : {"id":"1","shop_name":"寻根小卖铺","address":"陆家嘴一号","shop_time":"早八点到早八点","business_type":"餐饮","mobile":"15136778516","text":"这是一个实现梦想的地方，梦开始的地方","region":"湖北省,黄冈市,麻城市","shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","consumption_per_person":"188","level":"4","check_in_time":"0","check_out_time":"0","service":"","total_comment":"0","total_img":"0","logo":""}
         * recommend_goods : [{"id":"1","shop_id":"1","status":"2","name":"红烧鱼","type":"0","classify":"","cover":"","price":"58","info":null,"amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":null},{"id":"2","shop_id":"1","status":"2","name":"红烧排骨","type":"0","classify":"","cover":"","price":"88","info":null,"amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":null},{"id":"3","shop_id":"1","status":"2","name":"麻婆豆腐","type":"0","classify":"","cover":"","price":"28","info":null,"amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":null},{"id":"50","shop_id":"1","status":"0","name":"钓鱼伞架","type":"1","classify":"","cover":"https://res.ixungen.cn/img/9,0a3b43441807","price":"0","info":"","amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":"1502329503"},{"id":"51","shop_id":"1","status":"1","name":"标间","type":"1","classify":"","cover":"https://res.ixungen.cn/img/9,0a3c0aaa6485","price":"1","info":"","amount":null,"is_reserve":null,"is_cancel":null,"service":null,"cancel_rule":null,"create_time":"1502329563"}]
         * comment : [{"id":"133","shop_id":"1","comment_rid":"1000254","comment_name":"","comment_avtar":"","reply_rid":"","content":"不错","cost":"0","score":"0","img":"","comment_time":"0","people_like":"0","total_reply":"1"}]
         * nearby_shop : [{"id":"1","consumption_per_person":"188","level":"4","shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","type":"2","shop_name":"寻根小卖铺","total_comment":"0"},{"id":"11","consumption_per_person":"288","level":"5","shop_card_a":"https://res.ixungen.cn/img/12,07d13570e72d","shop_card_b":"https://res.ixungen.cn/img/9,07d3e2901047","type":"0","shop_name":"于氏小店","total_comment":"0"},{"id":"12","consumption_per_person":"328","level":"4","shop_card_a":"http://res.ixungen.cn/img/11,07f783fd8475","shop_card_b":"http://res.ixungen.cn/img/12,07f8176a7b3d","type":"0","shop_name":"麻辣烫","total_comment":"0"},{"id":"13","consumption_per_person":"158","level":"3","shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","type":"0","shop_name":"店小二","total_comment":"0"}]
         */

        private ShopInfoBean shop_info;
        private List<RecommendGoodsBean> recommend_goods;
        private List<CommentBean> comment;
        private List<NearbyShopBean> nearby_shop;

        public ShopInfoBean getShop_info() {
            return shop_info;
        }

        public void setShop_info(ShopInfoBean shop_info) {
            this.shop_info = shop_info;
        }

        public List<RecommendGoodsBean> getRecommend_goods() {
            return recommend_goods;
        }

        public void setRecommend_goods(List<RecommendGoodsBean> recommend_goods) {
            this.recommend_goods = recommend_goods;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public List<NearbyShopBean> getNearby_shop() {
            return nearby_shop;
        }

        public void setNearby_shop(List<NearbyShopBean> nearby_shop) {
            this.nearby_shop = nearby_shop;
        }

        public static class ShopInfoBean {
            /**
             * id : 1
             * shop_name : 寻根小卖铺
             * address : 陆家嘴一号
             * shop_time : 早八点到早八点
             * business_type : 餐饮
             * mobile : 15136778516
             * text : 这是一个实现梦想的地方，梦开始的地方
             * region : 湖北省,黄冈市,麻城市
             * shop_card_a : http://res.ixungen.cn/img/11,07cfbe865e17
             * shop_card_b : http://res.ixungen.cn/img/9,07d0494252d5
             * consumption_per_person : 188
             * level : 4
             * check_in_time : 0
             * check_out_time : 0
             * service :
             * total_comment : 0
             * total_img : 0
             * logo :
             */

            private String id;
            private String shop_name;
            private String address;
            private String shop_time;
            private String business_type;
            private String mobile;
            private String text;
            private String region;
            private String shop_card_a;
            private String shop_card_b;
            private String consumption_per_person;
            private String level;
            private String check_in_time;
            private String check_out_time;
            private String service;
            private String total_comment;
            private String total_img;
            private String logo;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getShop_name() {
                return shop_name;
            }

            public void setShop_name(String shop_name) {
                this.shop_name = shop_name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getShop_time() {
                return shop_time;
            }

            public void setShop_time(String shop_time) {
                this.shop_time = shop_time;
            }

            public String getBusiness_type() {
                return business_type;
            }

            public void setBusiness_type(String business_type) {
                this.business_type = business_type;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
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

            public String getCheck_in_time() {
                return check_in_time;
            }

            public void setCheck_in_time(String check_in_time) {
                this.check_in_time = check_in_time;
            }

            public String getCheck_out_time() {
                return check_out_time;
            }

            public void setCheck_out_time(String check_out_time) {
                this.check_out_time = check_out_time;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String getTotal_comment() {
                return total_comment;
            }

            public void setTotal_comment(String total_comment) {
                this.total_comment = total_comment;
            }

            public String getTotal_img() {
                return total_img;
            }

            public void setTotal_img(String total_img) {
                this.total_img = total_img;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }
        }

        public static class RecommendGoodsBean extends GoodsFormBaseBean{

        }

        public static class CommentBean extends CommentBaseBean {

        }

        public static class NearbyShopBean extends NearbyShopBaseBean{

        }
    }
}
