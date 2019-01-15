package com.ruihuo.ixungen.ui.familytree.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public class CatalagBean {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 272
     * data : [{"id":"1","stemma_id":"1","level_id":"1","tree_id":"1","tree_pid":"0","name":"王有材"},{"id":"2","stemma_id":"1","level_id":"2","tree_id":"2","tree_pid":"1","name":"王礼公"},{"id":"3","stemma_id":"1","level_id":"2","tree_id":"5","tree_pid":"1","name":"王昌公"},{"id":"149","stemma_id":"1","level_id":"3","tree_id":"15","tree_pid":"5","name":"未详"},{"id":"148","stemma_id":"1","level_id":"3","tree_id":"13","tree_pid":"5","name":"王克敏"},{"id":"147","stemma_id":"1","level_id":"3","tree_id":"11","tree_pid":"5","name":"王克修"},{"id":"146","stemma_id":"1","level_id":"3","tree_id":"9","tree_pid":"5","name":"王克俭"},{"id":"145","stemma_id":"1","level_id":"3","tree_id":"7","tree_pid":"5","name":"王克宽"},{"id":"153","stemma_id":"1","level_id":"4","tree_id":"17","tree_pid":"15","name":"王溥公"},{"id":"154","stemma_id":"1","level_id":"4","tree_id":"18","tree_pid":"15","name":"王连公"}]
     * serverTime : 2017-10-31 14:08:46
     */

    private int code;
    private String msg;
    private int totalPage;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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
         * id : 1
         * stemma_id : 1
         * level_id : 1
         * tree_id : 1
         * tree_pid : 0
         * name : 王有材
         */

        private String phone;
        private String pid;
        private String mid;
        private String rid;
        private String create_rid;
        private String is_root;
        private String spouse_id;
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


        private String id;
        private String stemma_id;
        private String level_id;
        private String tree_id;
        private String tree_pid;
        private String name;
        private int page;//页码

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

        public String getSpouse_id() {
            return spouse_id;
        }

        public void setSpouse_id(String spouse_id) {
            this.spouse_id = spouse_id;
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

        public String getStemma_id() {
            return stemma_id;
        }

        public void setStemma_id(String stemma_id) {
            this.stemma_id = stemma_id;
        }

        public String getLevel_id() {
            return level_id;
        }

        public void setLevel_id(String level_id) {
            this.level_id = level_id;
        }

        public String getTree_id() {
            return tree_id;
        }

        public void setTree_id(String tree_id) {
            this.tree_id = tree_id;
        }

        public String getTree_pid() {
            return tree_pid;
        }

        public void setTree_pid(String tree_pid) {
            this.tree_pid = tree_pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }
}
