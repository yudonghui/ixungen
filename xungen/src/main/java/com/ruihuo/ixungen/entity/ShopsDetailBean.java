package com.ruihuo.ixungen.entity;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/7/18
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopsDetailBean implements Serializable {

    /**
     * code : 0
     * msg : success
     * data : {"id":"11","rid":"1000325","shop_name":"于氏小店","scenic_area_id":"1","address":"麻城路311号","region":"湖北省,黄冈市,麻城市","house_number":"","shop_time":"6：00-21:00","business_type":"住宿","mobile":"13162821161","text":"这是一个有文化底蕴的小店！在这里住的好吃的好！还有美女相陪！一切服务免费！敬请光顾！","consumption_per_person":"288","level":"5","type":"0","state":"0","juridical_name":"辉冬于","juridical_idno":"510222198712264553","juridical_idcard_a":"https://res.ixungen.cn/img/9,07d590bf8919","juridical_idcard_b":"https://res.ixungen.cn/img/9,07cebcfc5eb6","business_license":"https://res.ixungen.cn/img/12,07d67940a48c","card_type":"1","bank_card":"22218866877556699","branch":"浦东新区支行","shop_card_a":"https://res.ixungen.cn/img/12,07d13570e72d","shop_card_b":"https://res.ixungen.cn/img/9,07d3e2901047","qr_code":"","open_bank":"招商银行","failure_cause":"","logo":"https://res.ixungen.cn/img/8,09b1492ccc17","service":"WIFI,行李寄存,停车场,餐厅,叫醒服务,送餐服务","check_in_time":"0","check_out_time":"0","create_time":"1500366879","update_time":"0","name":"龟峰山风景区"}
     * serverTime : 2017-08-09 10:07:14
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

    public static class DataBean implements Serializable {
        /**
         * id : 11
         * rid : 1000325
         * shop_name : 于氏小店
         * scenic_area_id : 1
         * address : 麻城路311号
         * region : 湖北省,黄冈市,麻城市
         * house_number :
         * shop_time : 6：00-21:00
         * business_type : 住宿
         * mobile : 13162821161
         * text : 这是一个有文化底蕴的小店！在这里住的好吃的好！还有美女相陪！一切服务免费！敬请光顾！
         * consumption_per_person : 288
         * level : 5
         * type : 0
         * state : 0
         * juridical_name : 辉冬于
         * juridical_idno : 510222198712264553
         * juridical_idcard_a : https://res.ixungen.cn/img/9,07d590bf8919
         * juridical_idcard_b : https://res.ixungen.cn/img/9,07cebcfc5eb6
         * business_license : https://res.ixungen.cn/img/12,07d67940a48c
         * card_type : 1
         * bank_card : 22218866877556699
         * branch : 浦东新区支行
         * shop_card_a : https://res.ixungen.cn/img/12,07d13570e72d
         * shop_card_b : https://res.ixungen.cn/img/9,07d3e2901047
         * qr_code :
         * open_bank : 招商银行
         * failure_cause :
         * logo : https://res.ixungen.cn/img/8,09b1492ccc17
         * service : WIFI,行李寄存,停车场,餐厅,叫醒服务,送餐服务
         * check_in_time : 0
         * check_out_time : 0
         * create_time : 1500366879
         * update_time : 0
         * name : 龟峰山风景区
         */

        private String id;
        private String rid;
        private String shop_name;
        private String scenic_area_id;
        private String address;
        private String region;
        private String house_number;
        private String shop_time;
        private String business_type;
        private String mobile;
        private String text;
        private String consumption_per_person;
        private String level;
        private String type;
        private String state;
        private String juridical_name;
        private String juridical_idno;
        private String juridical_idcard_a;
        private String juridical_idcard_b;
        private String business_license;
        private String card_type;
        private String bank_card;
        private String branch;
        private String shop_card_a;
        private String shop_card_b;
        private String qr_code;
        private String open_bank;
        private String failure_cause;
        private String logo;
        private String service;
        private String check_in_time;
        private String check_out_time;
        private String create_time;
        private String update_time;
        private String name;

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

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getScenic_area_id() {
            return scenic_area_id;
        }

        public void setScenic_area_id(String scenic_area_id) {
            this.scenic_area_id = scenic_area_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getHouse_number() {
            return house_number;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getJuridical_name() {
            return juridical_name;
        }

        public void setJuridical_name(String juridical_name) {
            this.juridical_name = juridical_name;
        }

        public String getJuridical_idno() {
            return juridical_idno;
        }

        public void setJuridical_idno(String juridical_idno) {
            this.juridical_idno = juridical_idno;
        }

        public String getJuridical_idcard_a() {
            return juridical_idcard_a;
        }

        public void setJuridical_idcard_a(String juridical_idcard_a) {
            this.juridical_idcard_a = juridical_idcard_a;
        }

        public String getJuridical_idcard_b() {
            return juridical_idcard_b;
        }

        public void setJuridical_idcard_b(String juridical_idcard_b) {
            this.juridical_idcard_b = juridical_idcard_b;
        }

        public String getBusiness_license() {
            return business_license;
        }

        public void setBusiness_license(String business_license) {
            this.business_license = business_license;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getBank_card() {
            return bank_card;
        }

        public void setBank_card(String bank_card) {
            this.bank_card = bank_card;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
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

        public String getQr_code() {
            return qr_code;
        }

        public void setQr_code(String qr_code) {
            this.qr_code = qr_code;
        }

        public String getOpen_bank() {
            return open_bank;
        }

        public void setOpen_bank(String open_bank) {
            this.open_bank = open_bank;
        }

        public String getFailure_cause() {
            return failure_cause;
        }

        public void setFailure_cause(String failure_cause) {
            this.failure_cause = failure_cause;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
