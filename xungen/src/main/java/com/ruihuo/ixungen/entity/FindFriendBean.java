package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/12
 * @describe May the Buddha bless bug-free!!!
 */
public class FindFriendBean {

    /**
     * code : 0
     * msg : success
     * data : [{"rid":"1000269","nikename":"郁金香","phone":"13162821161","sex":"0","birthday":null,"avatar":null,"region":"237641,240649,241594"}]
     * serverTime : 2017-04-12 11:30:43
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
         * rid : 1000269
         * nikename : 郁金香
         * phone : 13162821161
         * sex : 0
         * birthday : null
         * avatar : null
         * region : 237641,240649,241594
         */

        private String rid;
        private String nikename;
        private String phone;
        private String sex;
        private String birthday;
        private String avatar;
        private String region;
        private String is_friend;

        public String getIsFriend() {
            return is_friend;
        }

        public void setIsFriend(String isFriend) {
            this.is_friend = isFriend;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }
}
