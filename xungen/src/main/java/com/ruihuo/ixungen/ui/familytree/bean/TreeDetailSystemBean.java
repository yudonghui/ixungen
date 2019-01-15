package com.ruihuo.ixungen.ui.familytree.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/1
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeDetailSystemBean {


    /**
     * code : 0
     * msg : 成功
     * data : {"id":"5","create_rid":"0","spouse_id":"6","name":"王昌公","sex":"1","former_name":"","generation":"","region":"307715,327798,331316","address":"307715,327798,331316,331505","birthday_adbc":"1","birthday_year":"","birthday_month":"0","birthday_day":"0","text":"居万河嘴殁葬西首岗上子山午向<\/p>妣郑氏与夫同穴共向<\/p>传万家嘴一支<\/p><\/p>注：昌公第十一世到第十五世纲纪不台长幼紊乱因创修时尚未清在重修与三修时略理一二<\/p>","sort":2,"level_id":"2","father":"王昌公","mother":"郑氏","childs":[{"id":"7","name":"王克宽","sex":"1"},{"id":"9","name":"王克俭","sex":"1"},{"id":"11","name":"王克修","sex":"1"},{"id":"13","name":"王克敏","sex":"1"},{"id":"15","name":"未详","sex":"1"}],"spouse_info":{"id":"6","name":"郑氏","birthday_adbc":"1","birthday_year":"","birthday_month":"0","birthday_day":"0","deathday_adbc":"1","deathday_year":"","deathday_month":"0","deathday_day":"0","sort":4}}
     * serverTime : 2017-11-02 16:11:11
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
         * id : 5
         * create_rid : 0
         * spouse_id : 6
         * name : 王昌公
         * sex : 1
         * former_name :
         * generation :
         * region : 307715,327798,331316
         * address : 307715,327798,331316,331505
         * birthday_adbc : 1
         * birthday_year :
         * birthday_month : 0
         * birthday_day : 0
         * text : 居万河嘴殁葬西首岗上子山午向</p>妣郑氏与夫同穴共向</p>传万家嘴一支</p></p>注：昌公第十一世到第十五世纲纪不台长幼紊乱因创修时尚未清在重修与三修时略理一二</p>
         * sort : 2
         * level_id : 2
         * father : 王昌公
         * mother : 郑氏
         * childs : [{"id":"7","name":"王克宽","sex":"1"},{"id":"9","name":"王克俭","sex":"1"},{"id":"11","name":"王克修","sex":"1"},{"id":"13","name":"王克敏","sex":"1"},{"id":"15","name":"未详","sex":"1"}]
         * spouse_info : {"id":"6","name":"郑氏","birthday_adbc":"1","birthday_year":"","birthday_month":"0","birthday_day":"0","deathday_adbc":"1","deathday_year":"","deathday_month":"0","deathday_day":"0","sort":4}
         */


        private String phone;
        private String pid;
        private String mid;
        private String rid;
        private String is_root;
        private String region_name;
        private String address_name;
        private String address_suffix;
        private String birth;
        private String death;
        private String in_life;
        private String deathday_adbc;
        private String causa_mortis;
        private String create_time;
        private String update_time;
        private String avatar_url;
        private String ispublic;
        private String root_id;
        private String spouse_name;


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
        private int total;
        private SpouseInfoBean spouse_info;
        private List<ChildsBean> childs;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
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

        public String getSpouse_name() {
            return spouse_name;
        }

        public void setSpouse_name(String spouse_name) {
            this.spouse_name = spouse_name;
        }


        public void setMother(String mother) {
            this.mother = mother;
        }

        public SpouseInfoBean getSpouse_info() {
            return spouse_info;
        }

        public void setSpouse_info(SpouseInfoBean spouse_info) {
            this.spouse_info = spouse_info;
        }

        public List<ChildsBean> getChilds() {
            return childs;
        }

        public void setChilds(List<ChildsBean> childs) {
            this.childs = childs;
        }

        public static class SpouseInfoBean {
            /**
             * id : 6
             * name : 郑氏
             * birthday_adbc : 1
             * birthday_year :
             * birthday_month : 0
             * birthday_day : 0
             * deathday_adbc : 1
             * deathday_year :
             * deathday_month : 0
             * deathday_day : 0
             * sort : 4
             */

            private String id;
            private String name;
            private String birth;
            private String death;
            private String birthday_adbc;
            private String avatar_url;
            private String level_id;
            private String birthday_year;
            private String birthday_month;
            private String birthday_day;
            private String deathday_adbc;
            private String deathday_year;
            private String deathday_month;
            private String deathday_day;
            private int sort;
            private int total;

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

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getLevel_id() {
                return level_id;
            }

            public void setLevel_id(String level_id) {
                this.level_id = level_id;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

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

        public static class ChildsBean {
            /**
             * id : 7
             * name : 王克宽
             * sex : 1
             */

            private String id;
            private String name;
            private String sex;

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

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }
        }
    }
}
