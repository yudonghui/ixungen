package com.ruihuo.ixungen.ui.familytree.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/6
 * @describe May the Buddha bless bug-free!!!
 */
public class SpouesBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":"1013","phone":null,"stemma_id":"123","pid":"-1509960757779818518","mid":"-1509960757779896710","rid":"0","create_rid":"1000797","is_root":"0","level_id":"2","spouse_id":"1011","name":"于一媳","former_name":null,"sex":"2","generation":null,"region_name":null,"address_name":null,"address_suffix":null,"birthday_adbc":"1","birth":"","death":null,"in_life":"1","deathday_adbc":"1","causa_mortis":null,"text":null,"sort":"0","create_time":"1509960757","update_time":"0","avatar_url":"","ispublic":"1","root_id":"1008"},{"id":"1014","phone":null,"stemma_id":"123","pid":"-1509960773194775761","mid":"-1509960773194753376","rid":"0","create_rid":"1000797","is_root":"0","level_id":"2","spouse_id":"1011","name":"于一媳二","former_name":null,"sex":"1","generation":null,"region_name":null,"address_name":null,"address_suffix":null,"birthday_adbc":"1","birth":"","death":null,"in_life":"1","deathday_adbc":"1","causa_mortis":null,"text":null,"sort":"0","create_time":"1509960773","update_time":"0","avatar_url":"","ispublic":"1","root_id":"1008"},{"id":"1015","phone":null,"stemma_id":"123","pid":"-1509960798588887632","mid":"-1509960798588871478","rid":"0","create_rid":"1000797","is_root":"0","level_id":"2","spouse_id":"1011","name":"于一媳三","former_name":null,"sex":"2","generation":null,"region_name":null,"address_name":null,"address_suffix":null,"birthday_adbc":"1","birth":"","death":null,"in_life":"1","deathday_adbc":"1","causa_mortis":null,"text":null,"sort":"0","create_time":"1509960798","update_time":"0","avatar_url":"","ispublic":"1","root_id":"1008"}]
     * serverTime : 2017-11-06 18:26:09
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
         * id : 1013
         * phone : null
         * stemma_id : 123
         * pid : -1509960757779818518
         * mid : -1509960757779896710
         * rid : 0
         * create_rid : 1000797
         * is_root : 0
         * level_id : 2
         * spouse_id : 1011
         * name : 于一媳
         * former_name : null
         * sex : 2
         * generation : null
         * region_name : null
         * address_name : null
         * address_suffix : null
         * birthday_adbc : 1
         * birth : 
         * death : null
         * in_life : 1
         * deathday_adbc : 1
         * causa_mortis : null
         * text : null
         * sort : 0
         * create_time : 1509960757
         * update_time : 0
         * avatar_url : 
         * ispublic : 1
         * root_id : 1008
         */

        private String id;
        private String phone;
        private String stemma_id;
        private String pid;
        private String mid;
        private String rid;
        private String create_rid;
        private String is_root;
        private String level_id;
        private String spouse_id;
        private String name;
        private String former_name;
        private String sex;
        private String generation;
        private String region_name;
        private String address_name;
        private String address_suffix;
        private String birthday_adbc;
        private String birth;
        private String death;
        private String in_life;
        private String deathday_adbc;
        private String causa_mortis;
        private String text;
        private String sort;
        private String create_time;
        private String update_time;
        private String avatar_url;
        private String ispublic;
        private String root_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStemma_id() {
            return stemma_id;
        }

        public void setStemma_id(String stemma_id) {
            this.stemma_id = stemma_id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getCreate_rid() {
            return create_rid;
        }

        public void setCreate_rid(String create_rid) {
            this.create_rid = create_rid;
        }

        public String getIs_root() {
            return is_root;
        }

        public void setIs_root(String is_root) {
            this.is_root = is_root;
        }

        public String getLevel_id() {
            return level_id;
        }

        public void setLevel_id(String level_id) {
            this.level_id = level_id;
        }

        public String getSpouse_id() {
            return spouse_id;
        }

        public void setSpouse_id(String spouse_id) {
            this.spouse_id = spouse_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormer_name() {
            return former_name;
        }

        public void setFormer_name(String former_name) {
            this.former_name = former_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getGeneration() {
            return generation;
        }

        public void setGeneration(String generation) {
            this.generation = generation;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getAddress_suffix() {
            return address_suffix;
        }

        public void setAddress_suffix(String address_suffix) {
            this.address_suffix = address_suffix;
        }

        public String getBirthday_adbc() {
            return birthday_adbc;
        }

        public void setBirthday_adbc(String birthday_adbc) {
            this.birthday_adbc = birthday_adbc;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getDeath() {
            return death;
        }

        public void setDeath(String death) {
            this.death = death;
        }

        public String getIn_life() {
            return in_life;
        }

        public void setIn_life(String in_life) {
            this.in_life = in_life;
        }

        public String getDeathday_adbc() {
            return deathday_adbc;
        }

        public void setDeathday_adbc(String deathday_adbc) {
            this.deathday_adbc = deathday_adbc;
        }

        public String getCausa_mortis() {
            return causa_mortis;
        }

        public void setCausa_mortis(String causa_mortis) {
            this.causa_mortis = causa_mortis;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
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

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getIspublic() {
            return ispublic;
        }

        public void setIspublic(String ispublic) {
            this.ispublic = ispublic;
        }

        public String getRoot_id() {
            return root_id;
        }

        public void setRoot_id(String root_id) {
            this.root_id = root_id;
        }
    }
}
