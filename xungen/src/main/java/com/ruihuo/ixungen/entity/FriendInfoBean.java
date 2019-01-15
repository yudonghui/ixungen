package com.ruihuo.ixungen.entity;

/**
 * @author yudonghui
 * @date 2017/4/17
 * @describe May the Buddha bless bug-free!!!
 */
public class FriendInfoBean {

    /**
     * code : 0
     * msg : success
     * data : {"nikename":"郁金香","avatar":null,"rid":"1000269","phone":"13162821161","sex":"0","region":"237641,240649,241594","birthday":null,"isFriend":0}
     * serverTime : 2017-04-17 14:55:18
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
         * nikename : 郁金香
         * avatar : null
         * rid : 1000269
         * phone : 13162821161
         * sex : 0
         * region : 237641,240649,241594
         * birthday : null
         * isFriend : 0
         */

        private String nikename;
        private String avatar;
        private String rid;
        private String phone;
        private String sex;
        private String region;
        private String birthday;
        private int isFriend;

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }
    }
}
