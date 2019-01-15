package com.ruihuo.ixungen.entity;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class LoginModel {


    /**
     * code : 0
     * msg : SUCCESS
     * data : {"rid":1000214,"phone":"15827429514","token":{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOjEwMDAyMTQsInBob25lIjoiMTU4Mjc0Mjk1MTQiLCJleHAiOjE0OTA4NTExMTJ9.cu5Y-hZ6JuWdHAegdp3WhTJr0AbmX9pc6xGkKuJN6zM","exp":1490851112}}
     * serverTime : 2017-03-30 11:18:32
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
         * rid : 1000214
         * phone : 15827429514
         * token : {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOjEwMDAyMTQsInBob25lIjoiMTU4Mjc0Mjk1MTQiLCJleHAiOjE0OTA4NTExMTJ9.cu5Y-hZ6JuWdHAegdp3WhTJr0AbmX9pc6xGkKuJN6zM","exp":1490851112}
         */

        private String rid;
        private String phone;
        private TokenBean token;
        private String rong_yun_token;

        public String getRong_yun_token() {
            return rong_yun_token;
        }

        public void setRong_yun_token(String rong_yun_token) {
            this.rong_yun_token = rong_yun_token;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public TokenBean getToken() {
            return token;
        }

        public void setToken(TokenBean token) {
            this.token = token;
        }

        public static class TokenBean {
            /**
             * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOjEwMDAyMTQsInBob25lIjoiMTU4Mjc0Mjk1MTQiLCJleHAiOjE0OTA4NTExMTJ9.cu5Y-hZ6JuWdHAegdp3WhTJr0AbmX9pc6xGkKuJN6zM
             * exp : 1490851112
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
    }
}
