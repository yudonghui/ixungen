package com.ruihuo.ixungen.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author yudonghui
 * @date 2017/7/24
 * @describe May the Buddha bless bug-free!!!
 */
public class WXBean {


    /**
     * code : 0
     * msg : success
     * data : {"appid":"wx421375206ae23aad","partnerid":"1486192542","prepayid":"wx201707241828591d7538f44e0887548120","package":"Sign=WXPay","noncestr":"K1ng7vT6LB5LdqDB6T2MqF03a6oSdvlV","timestamp":1500892139,"sign":"33C3B31BB4F9CA80F83C17C4F160D880"}
     * serverTime : 2017-07-24 18:28:59
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
         * appid : wx421375206ae23aad
         * partnerid : 1486192542
         * prepayid : wx201707241828591d7538f44e0887548120
         * package : Sign=WXPay
         * noncestr : K1ng7vT6LB5LdqDB6T2MqF03a6oSdvlV
         * timestamp : 1500892139
         * sign : 33C3B31BB4F9CA80F83C17C4F160D880
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
