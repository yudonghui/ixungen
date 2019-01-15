package com.ruihuo.ixungen.ui.familytree.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/28
 * @describe May the Buddha bless bug-free!!!
 */
public class PriceBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":"1","price":"0.01","name":"一天查看权限（体验）","validity_period":"1"},{"id":"2","price":"20.00","name":"一个月查看权限","validity_period":"30"},{"id":"3","price":"58.00","name":"三个月查看权限","validity_period":"90"},{"id":"4","price":"200.00","name":"一年查看权限","validity_period":"365"}]
     * serverTime : 2017-10-28 16:42:33
     */

    private int code;
    private String msg;
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
         * id : 1
         * price : 0.01
         * name : 一天查看权限（体验）
         * validity_period : 1
         */

        private String id;
        private String price;
        private String name;
        private int validity_period;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValidity_period() {
            return validity_period;
        }

        public void setValidity_period(int validity_period) {
            this.validity_period = validity_period;
        }
    }
}
