package com.ruihuo.ixungen.action;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/5/27
 * @describe May the Buddha bless bug-free!!!
 */
public class IntroduceBean implements Serializable{

    /**
     * code : 0
     * msg : success
     * data : {"rid":"1000123","nikename":"浪子","avatar":null,"sex":"1","age":"23","birthday":"2017-05-26","height":"177","weight":"70","interest":"唱歌，爬山","signature":"不要迷恋哥，哥就是个传说","total_vote":"122"}
     * serverTime : 2017-05-27 09:44:58
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

    public static class DataBean implements Serializable{
        /**
         * rid : 1000123
         * nikename : 浪子
         * avatar : null
         * sex : 1
         * age : 23
         * birthday : 2017-05-26
         * height : 177
         * weight : 70
         * interest : 唱歌，爬山
         * signature : 不要迷恋哥，哥就是个传说
         * total_vote : 122
         */

        private String rid;
        private String nikename;
        private String avatar;
        private String sex;
        private String age;
        private String birthday;
        private String height;
        private String weight;
        private String interest;
        private String signature;
        private String total_vote;
        private String is_performer;

        public String getIs_performer() {
            return is_performer;
        }

        public void setIs_performer(String is_performer) {
            this.is_performer = is_performer;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getTotal_vote() {
            return total_vote;
        }

        public void setTotal_vote(String total_vote) {
            this.total_vote = total_vote;
        }
    }
}
