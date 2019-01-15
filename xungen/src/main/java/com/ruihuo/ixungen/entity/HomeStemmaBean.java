package com.ruihuo.ixungen.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author yudonghui
 * @date 2017/11/9
 * @describe May the Buddha bless bug-free!!!
 */
public class HomeStemmaBean {


    /**
     * code : 0
     * msg : 成功
     * data : {"region":"北京","address":"北京","generation":"水、代、文、玉、草","create_time":"1510047251","private":"9","rid":"0","family_id":"180","name":"家谱模板"}
     * serverTime : 2017-11-09 10:16:09
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
         * region : 北京
         * address : 北京
         * generation : 水、代、文、玉、草
         * create_time : 1510047251
         * private : 9
         * rid : 0
         * family_id : 180
         * name : 家谱模板
         */

        private String region;
        private String address;
        private String generation;
        private String create_time;
        @SerializedName("private")
        private String privateX;
        private String rid;
        private String family_id;
        private String name;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGeneration() {
            return generation;
        }

        public void setGeneration(String generation) {
            this.generation = generation;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPrivateX() {
            return privateX;
        }

        public void setPrivateX(String privateX) {
            this.privateX = privateX;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getFamily_id() {
            return family_id;
        }

        public void setFamily_id(String family_id) {
            this.family_id = family_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
