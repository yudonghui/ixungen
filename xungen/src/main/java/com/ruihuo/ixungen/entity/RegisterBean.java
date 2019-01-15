package com.ruihuo.ixungen.entity;

/**
 * @author yudonghui
 * @date 2017/4/28
 * @describe May the Buddha bless bug-free!!!
 */
public class RegisterBean {

    /**
     * code : 0
     * msg : success
     * rid : 1000276
     * token : {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOiIxMDAwMjc2IiwicGhvbmUiOiIxMzE2MjgyMTE2MSIsImV4cCI6MTQ5MzQ0NTU4NX0.B8H0F2_fKIETkPUccJfiEiystCFJl1joV-bgkGCqoa0","exp":1493445585}
     * rong_yun_token : J2KZtq4cj6B6qjI59HTpJQ1LEE/MkTj9euthjgs5Nxe7OO+0p4MxJ17qdiDPX/YmTW5kt+OUMVTrMoDbeVL1EKUL2wp8ckEm
     * data : {"id":"451","name":"于"}
     * serverTime : 2017-04-28 13:59:45
     */

    private int code;
    private String msg;
    private String rid;
    private TokenBean token;
    private String rong_yun_token;
    private String data;
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

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public TokenBean getToken() {
        return token;
    }

    public void setToken(TokenBean token) {
        this.token = token;
    }

    public String getRong_yun_token() {
        return rong_yun_token;
    }

    public void setRong_yun_token(String rong_yun_token) {
        this.rong_yun_token = rong_yun_token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class TokenBean {
        /**
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOiIxMDAwMjc2IiwicGhvbmUiOiIxMzE2MjgyMTE2MSIsImV4cCI6MTQ5MzQ0NTU4NX0.B8H0F2_fKIETkPUccJfiEiystCFJl1joV-bgkGCqoa0
         * exp : 1493445585
         */

        private String token;
        private int exp;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getExp() {
            return exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }
    }

   /* public static class DataBean {
        *//**
     * id : 451
     * name : 于
     *//*

        private String id;
        private String name;

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
    }*/
}
