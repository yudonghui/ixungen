package com.ruihuo.ixungen.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/7
 * @describe May the Buddha bless bug-free!!!
 */
public class FriendBean implements Serializable {

    /**
     * code : 0
     * msg : success
     * data : [{"id":"21","association_id":"6","remark":null,"nikename":"董硕","rid":"1000266","phone":"15136778516","sex":"0","birthday":"2017-04-07","avatar":null,"region":"107712,113125,114019"},{"id":"23","association_id":"6","remark":"浪迹天涯","nikename":"你二大爷","rid":"1000254","phone":"15827429514","sex":"0","birthday":null,"avatar":null,"region":"237641,240649,2"},{"id":"24","association_id":"6","remark":null,"nikename":"PU1","rid":"1000172","phone":"18653078615","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"25","association_id":"6","remark":null,"nikename":"123456","rid":"1000173","phone":"18123237372","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"26","association_id":"6","remark":null,"nikename":"赵永辉","rid":"1000174","phone":"13682104380","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"27","association_id":"6","remark":null,"nikename":"李小安","rid":"1000175","phone":"18611100419","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"28","association_id":"6","remark":null,"nikename":"王大伟","rid":"1000177","phone":"13601666310","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"29","association_id":"6","remark":null,"nikename":"qq125491660","rid":"1000179","phone":"18500373720","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"30","association_id":"6","remark":null,"nikename":"唐海生","rid":"1000181","phone":"18090289782","sex":null,"birthday":null,"avatar":null,"region":null},{"id":"31","association_id":"6","remark":null,"nikename":"土鳖","rid":"1000183","phone":"18885280703","sex":null,"birthday":null,"avatar":null,"region":null}]
     * serverTime : 2017-04-07 14:15:25
     */

    private int code;
    private String msg;
    private String serverTime;
    private int totalPage;
    private List<DataBean> data;
    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

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

    public static class DataBean implements Serializable {
        /**
         * id : 21
         * association_id : 6
         * remark : null
         * nikename : 董硕
         * rid : 1000266
         * phone : 15136778516
         * sex : 0
         * birthday : 2017-04-07
         * avatar : null
         * region : 107712,113125,114019
         */

        private String id;
        private String association_id;
        private String remark;
        private String nikename;
        private String rid;
        private String isFriend;
        private String status;
        private String activity_id;
        private String phone;
        private String sex;
        private String birthday;
        private String avatar;
        private String region;

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(String isFriend) {
            this.isFriend = isFriend;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAssociation_id() {
            return association_id;
        }

        public void setAssociation_id(String association_id) {
            this.association_id = association_id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
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
