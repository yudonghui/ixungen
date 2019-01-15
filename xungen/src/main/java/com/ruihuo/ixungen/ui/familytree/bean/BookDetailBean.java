package com.ruihuo.ixungen.ui.familytree.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class BookDetailBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"3","create_rid":"0","spouse_id":"1","name":"梁氏","sex":"2","former_name":"","generation":"","region":"-1","address":"-1","birthday_adbc":"1","birthday_year":"","birthday_month":"0","birthday_day":"0","text":null,"sort":2,"level_id":null,"father":"梁氏","mother":"王有材","childs":[],"spouse_info":{"id":"1","name":"王有材","birthday_adbc":"1","birthday_year":"","birthday_month":"0","birthday_day":"0","deathday_adbc":"1","deathday_year":"","deathday_month":"0","deathday_day":"0","sort":1}}
     * serverTime : 2017-11-01 16:40:44
     */

    private int code;
    private String msg;
    private TreeDetailSystemBean.DataBean data;
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

    public TreeDetailSystemBean.DataBean getData() {
        return data;
    }

    public void setData(TreeDetailSystemBean.DataBean data) {
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
         * id : 3
         * create_rid : 0
         * spouse_id : 1
         * name : 梁氏
         * sex : 2
         * former_name :
         * generation :
         * region : -1
         * address : -1
         * birthday_adbc : 1
         * birthday_year :
         * birthday_month : 0
         * birthday_day : 0
         * text : null
         * sort : 2
         * level_id : null
         * father : 梁氏
         * mother : 王有材
         * childs : []
         * spouse_info : {"id":"1","name":"王有材","birthday_adbc":"1","birthday_year":"","birthday_month":"0","birthday_day":"0","deathday_adbc":"1","deathday_year":"","deathday_month":"0","deathday_day":"0","sort":1}
         */

        private String id;
        private String create_rid;
        private String spouse_id;
        private String name;
        private String sex;
        private String former_name;
        private String generation;
        private String region;
        private String address;
        private String birthday_adbc;
        private String birthday_year;
        private String birthday_month;
        private String birthday_day;
        private String text;
        private int sort;
        private String level_id;
        private String father;
        private String mother;
        private TreeDetailSystemBean.DataBean.SpouseInfoBean spouse_info;
        private List<?> childs;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreate_rid() {
            return create_rid;
        }

        public void setCreate_rid(String create_rid) {
            this.create_rid = create_rid;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getFormer_name() {
            return former_name;
        }

        public void setFormer_name(String former_name) {
            this.former_name = former_name;
        }

        public String getGeneration() {
            return generation;
        }

        public void setGeneration(String generation) {
            this.generation = generation;
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

        public String getBirthday_adbc() {
            return birthday_adbc;
        }

        public void setBirthday_adbc(String birthday_adbc) {
            this.birthday_adbc = birthday_adbc;
        }

        public String getBirthday_year() {
            return birthday_year;
        }

        public void setBirthday_year(String birthday_year) {
            this.birthday_year = birthday_year;
        }

        public String getBirthday_month() {
            return birthday_month;
        }

        public void setBirthday_month(String birthday_month) {
            this.birthday_month = birthday_month;
        }

        public String getBirthday_day() {
            return birthday_day;
        }

        public void setBirthday_day(String birthday_day) {
            this.birthday_day = birthday_day;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getLevel_id() {
            return level_id;
        }

        public void setLevel_id(String level_id) {
            this.level_id = level_id;
        }

        public String getFather() {
            return father;
        }

        public void setFather(String father) {
            this.father = father;
        }

        public String getMother() {
            return mother;
        }

        public void setMother(String mother) {
            this.mother = mother;
        }

        public TreeDetailSystemBean.DataBean.SpouseInfoBean getSpouse_info() {
            return spouse_info;
        }

        public void setSpouse_info(TreeDetailSystemBean.DataBean.SpouseInfoBean spouse_info) {
            this.spouse_info = spouse_info;
        }

        public List<?> getChilds() {
            return childs;
        }

        public void setChilds(List<?> childs) {
            this.childs = childs;
        }

        public static class SpouseInfoBean {
            /**
             * id : 1
             * name : 王有材
             * birthday_adbc : 1
             * birthday_year :
             * birthday_month : 0
             * birthday_day : 0
             * deathday_adbc : 1
             * deathday_year :
             * deathday_month : 0
             * deathday_day : 0
             * sort : 1
             */

            private String id;
            private String name;
            private String birthday_adbc;
            private String birthday_year;
            private String birthday_month;
            private String birthday_day;
            private String deathday_adbc;
            private String deathday_year;
            private String deathday_month;
            private String deathday_day;
            private int sort;

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

            public String getBirthday_adbc() {
                return birthday_adbc;
            }

            public void setBirthday_adbc(String birthday_adbc) {
                this.birthday_adbc = birthday_adbc;
            }

            public String getBirthday_year() {
                return birthday_year;
            }

            public void setBirthday_year(String birthday_year) {
                this.birthday_year = birthday_year;
            }

            public String getBirthday_month() {
                return birthday_month;
            }

            public void setBirthday_month(String birthday_month) {
                this.birthday_month = birthday_month;
            }

            public String getBirthday_day() {
                return birthday_day;
            }

            public void setBirthday_day(String birthday_day) {
                this.birthday_day = birthday_day;
            }

            public String getDeathday_adbc() {
                return deathday_adbc;
            }

            public void setDeathday_adbc(String deathday_adbc) {
                this.deathday_adbc = deathday_adbc;
            }

            public String getDeathday_year() {
                return deathday_year;
            }

            public void setDeathday_year(String deathday_year) {
                this.deathday_year = deathday_year;
            }

            public String getDeathday_month() {
                return deathday_month;
            }

            public void setDeathday_month(String deathday_month) {
                this.deathday_month = deathday_month;
            }

            public String getDeathday_day() {
                return deathday_day;
            }

            public void setDeathday_day(String deathday_day) {
                this.deathday_day = deathday_day;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
