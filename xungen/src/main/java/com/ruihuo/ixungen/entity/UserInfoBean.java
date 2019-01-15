package com.ruihuo.ixungen.entity;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/3/31
 * @describe May the Buddha bless bug-free!!!
 */
public class UserInfoBean implements Serializable {

    /**
     * code : 0
     * msg : success
     * data : {"rid":"1000269","phone":"13162821161","nikename":"郁金香","truename":"于东辉","idcard":"41022219871226455X","surname":"于","email":null,"avatar":"11,01b4c47ff8d7","coin":"0","money":"0.00","sex":"0","birthday":null,"region":"237641,240649,241594","city":"645121,645122,648114","flag":"0","token":{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOiIxMDAwMjY5IiwicGhvbmUiOiIxMzE2MjgyMTE2MSIsImV4cCI6MTQ5MzM3NDQ3M30.6TUvB2AsAbo5Kjli1SVUgoPnzkJpWdccvyphMonGof8","exp":1493374473},"rong_yun_token":"jIpRp3tP6SqmZRiDu6dAHE6s+bUDgIwCeesbdRzadO//FL3BfWTKVVAGICas9fnz/+m6rX2YohtnV97sVDRO5Ylr/rdwWcg2","clanAssociationIds":"15,120,2,120"}
     * serverTime : 2017-04-27 18:14:33
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
         * rid : 1000269
         * phone : 13162821161
         * nikename : 郁金香
         * truename : 于东辉
         * idcard : 41022219871226455X
         * surname : 于
         * email : null
         * avatar : 11,01b4c47ff8d7
         * coin : 0
         * money : 0.00
         * sex : 0
         * birthday : null
         * region : 237641,240649,241594
         * city : 645121,645122,648114
         * flag : 0
         * token : {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOiIxMDAwMjY5IiwicGhvbmUiOiIxMzE2MjgyMTE2MSIsImV4cCI6MTQ5MzM3NDQ3M30.6TUvB2AsAbo5Kjli1SVUgoPnzkJpWdccvyphMonGof8","exp":1493374473}
         * rong_yun_token : jIpRp3tP6SqmZRiDu6dAHE6s+bUDgIwCeesbdRzadO//FL3BfWTKVVAGICas9fnz/+m6rX2YohtnV97sVDRO5Ylr/rdwWcg2
         * clanAssociationIds : 15,120,2,120
         */

        private String rid;
        private String phone;
        private String nikename;
        private String truename;
        private String idcard;
        private String surname;
        private String email;
        private String avatar;
        private String coin;
        private String money;
        private String sex;
        private String birthday;
        private String region;
        private String city;
        private String flag;
        private String shop_status;
        private TokenBean token;
        private String rong_yun_token;
        private String clanAssociationIds;

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

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getShop_status() {
            return shop_status;
        }

        public void setShop_status(String shop_status) {
            this.shop_status = shop_status;
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

        public String getClanAssociationIds() {
            return clanAssociationIds;
        }

        public void setClanAssociationIds(String clanAssociationIds) {
            this.clanAssociationIds = clanAssociationIds;
        }

        public static class TokenBean implements Serializable {
            /**
             * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyaWQiOiIxMDAwMjY5IiwicGhvbmUiOiIxMzE2MjgyMTE2MSIsImV4cCI6MTQ5MzM3NDQ3M30.6TUvB2AsAbo5Kjli1SVUgoPnzkJpWdccvyphMonGof8
             * exp : 1493374473
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
