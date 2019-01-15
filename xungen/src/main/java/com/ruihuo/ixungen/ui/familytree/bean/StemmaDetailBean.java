package com.ruihuo.ixungen.ui.familytree.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/10/27
 * @describe May the Buddha bless bug-free!!!
 */
public class StemmaDetailBean implements Serializable{

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"97","surname":"王","hall_id":"0","name":"王氏族谱","summary":"宏开世业               广大光昌                   \r\n\r\n万家景福                永镇楚邦\r\n\r\n右十六字以十八世起派为始嗣後中为子孙\r\n\r\n取名者务须照此字样不可旁泰照此次序不可倒乱庶名分秩然矣","type":"族谱","region":"582414,591287,593238,-1","address":"","private":"9","rid":"7","generation":"宏       开         世           业                          广        大          光         昌                     \r\n\r\n万       家          景          福                          永          镇         楚        邦","status":"1","create_time":"1489732150","update_time":"1489732479"}
     * serverTime : 2017-10-27 10:46:36
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
         * id : 97
         * surname : 王
         * hall_id : 0
         * name : 王氏族谱
         * summary :
         * type : 族谱
         * region : 582414,591287,593238,-1
         * address :
         * private : 9
         * rid : 7
         * generation :
         * status : 1
         * create_time : 1489732150
         * update_time : 1489732479
         */

        private String id;
        private String surname;
        private String nikename;
        private String truename;
        private String hall_id;
        private String name;
        private String phone;
        private String summary;
        private String type;
        private String region;
        private String address;
        @SerializedName("private")
        private String privateX;
        private String rid;
        private String generation;
        private String status;
        private String create_time;
        private String update_time;
        private String surname_introduce;
        private String family_instruction;
        private String family_celebrity;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getHall_id() {
            return hall_id;
        }

        public void setHall_id(String hall_id) {
            this.hall_id = hall_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

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

        public String getGeneration() {
            return generation;
        }

        public void setGeneration(String generation) {
            this.generation = generation;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSurname_introduce() {
            return surname_introduce;
        }

        public void setSurname_introduce(String surname_introduce) {
            this.surname_introduce = surname_introduce;
        }

        public String getFamily_instruction() {
            return family_instruction;
        }

        public void setFamily_instruction(String family_instruction) {
            this.family_instruction = family_instruction;
        }

        public String getFamily_celebrity() {
            return family_celebrity;
        }

        public void setFamily_celebrity(String family_celebrity) {
            this.family_celebrity = family_celebrity;
        }
    }
}
