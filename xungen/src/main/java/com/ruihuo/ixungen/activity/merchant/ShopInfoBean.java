package com.ruihuo.ixungen.activity.merchant;

/**
 * @author yudonghui
 * @date 2017/8/3
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopInfoBean {

    /**
     * code : 0
     * msg : success
     * data : {"rid":"1000325","shop_name":"6:00-12:00","id":"11","business_type":"住宿","logo":"https://res.ixungen.cn/img/8,09b1492ccc17","total_visit":0,"total_sale":0}
     * serverTime : 2017-08-05 17:08:10
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
         * rid : 1000325
         * shop_name : 6:00-12:00
         * id : 11
         * business_type : 住宿
         * logo : https://res.ixungen.cn/img/8,09b1492ccc17
         * total_visit : 0
         * total_sale : 0
         */

        private String rid;
        private String shop_name;
        private String id;
        private String type;
        private String logo;
        private String total_visit;
        private String total_sale;

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getTotal_visit() {
            return total_visit;
        }

        public void setTotal_visit(String total_visit) {
            this.total_visit = total_visit;
        }

        public String getTotal_sale() {
            return total_sale;
        }

        public void setTotal_sale(String total_sale) {
            this.total_sale = total_sale;
        }
    }
}
