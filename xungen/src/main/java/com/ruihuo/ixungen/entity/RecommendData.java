package com.ruihuo.ixungen.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/13
 * @describe May the Buddha bless bug-free!!!
 */
public class RecommendData implements Serializable{


    /**
     * code : 0
     * msg : 成功
     * data : {"stemma":[{"id":"2","name":"周氏族谱","surname":"周","region":"428836,448209,450045,450633","generation":"旧编字派\r\n\r\n十一世        可             十二世         正            十三世         塳             十四世           永               十五世         远\r\n十六世        如             十七世         金            十八世         对             十九世           大               二十世         廷\r\n金字派即世字派后须遵新编字取名庶不烦查对\r\n\r\n\r\n新编字派\r\n\r\n十七世           世           十八世            运         十九世           承         二十世           纯\r\n二十一世        家           二十二世        泽         二十三世       常         二十四世        新\r\n二十五世        文           二十六世        章         二十七世       炳         二十八世        焕\r\n二十九世        德           三十世            业         三十一世       维         三十二世        兴\r\n\r\n\r\n新添派行\r\n\r\n三十三世            礼              三十四世        为         三十五世         教           三十六世       本\r\n三十七世            福              三十八世        自         三十九世         天           四十世           申\r\n四十一世            宗              四十二世        傅         四十三世         必           四十四世        大\r\n四十五世            功              四十六世        立         四十七世         名           四十八世        成\r\n\r\n\r\n\r\n东西合派\r\n\r\n二十一世             忠                  二十二世             厚                二十三世          昭           二十四世            先             二十五世          荫\r\n二十六世             诗                  二十七世             书                二十八世          信           二十九世            克             三十世              昌\r\n三十一世             高                  三十二世             风                三十三世          傳           三十四世            礼             三十五世          学 \r\n三十六世             华                  三十七世             典                三十八世           焕          三十九世            文             四十世              章"},{"id":"3","name":"马氏宗谱","surname":"马","region":"237641","generation":""},{"id":"4","name":"陈氏宗谱","surname":"陈","region":"237641","generation":""},{"id":"5","name":"宋氏宗谱","surname":"宋","region":"428836","generation":""}],"clan_association":[{"id":"59","name":"常氏宗亲会","surname_id":"30","stemma_id":"0","president_rid":"1000800","vice_president_rid":[],"secretary_general_rid":[],"assistant_secretary_rid":[],"area_id_str":"湖北省,黄冈市,麻城市","address":"","img_url":"","info":"大家都是一家人，和谐共处宗亲会。","family_instructions":"","status":"1","level":"4","flag":"0","count":"1","sort":"1","create_time":"1505722739","update_time":"1505722845","president_info":[{"rid":"1000800","nikename":"Fonda","avatar":"14,0d023ff59730","phone":"18221216750"}]},{"id":"65","name":"董家村","surname_id":"65","stemma_id":"0","president_rid":"1000795","vice_president_rid":[],"secretary_general_rid":[],"assistant_secretary_rid":[],"area_id_str":"湖北省,黄冈市,麻城市","address":"","img_url":"14,0f7a1e689583","info":"董家村","family_instructions":"寻找同性有缘人寻找同性有缘人寻找同性有缘人寻找同性有缘人寻找同性有缘人同性有缘人寻找同性有缘人寻找同性有缘人寻找同性有缘人寻找同性有缘人同性有缘人同性有缘人寻找同性有缘人寻找同性有缘人寻找同性有缘人寻找同性有缘人","status":"1","level":"4","flag":"1","count":"5","sort":"1","create_time":"1505801512","update_time":"1505807360","president_info":[{"rid":"1000795","nikename":"董小硕","avatar":"10,0c04fbec28b4","phone":"13164691936"}]},{"id":"66","name":"林氏一家亲","surname_id":"214","stemma_id":"0","president_rid":"1000124","vice_president_rid":[],"secretary_general_rid":[],"assistant_secretary_rid":[],"area_id_str":"湖北省,黄冈市,麻城市","address":"","img_url":"","info":"","family_instructions":"","status":"1","level":"4","flag":"0","count":"1","sort":"1","create_time":"1506311228","update_time":"1506311228","president_info":[{"rid":"1000124","nikename":"浪迹天涯","avatar":"14,0cbe31c78d34","phone":"15827429516"}]},{"id":"67","name":"各地","surname_id":"65","stemma_id":"0","president_rid":"1000123","vice_president_rid":[],"secretary_general_rid":[],"assistant_secretary_rid":[],"area_id_str":"湖北省,黄冈市,麻城市","address":"","img_url":"","info":"么得","family_instructions":"","status":"1","level":"4","flag":"1","count":"1","sort":"1","create_time":"1506311234","update_time":"1506311234","president_info":[{"rid":"1000123","nikename":"浪迹天涯","avatar":"13,0c5fda98bdec","phone":"15827429515"}]}],"friend":[{"rid":"1000794","nikename":"浪迹天涯","avatar":null,"surname":"林","truename":null,"sex":"0","region":"2,3631,3974"},{"rid":"1000123","nikename":"浪迹天涯","avatar":"13,0c5fda98bdec","surname":"董","truename":"辉","sex":"0","region":"2,3631,3974"},{"rid":"1000254","nikename":"浪迹天涯","avatar":"13,0c5fda98bdec","surname":"林","truename":null,"sex":"0","region":"2,3631,3974"},{"rid":"10000","nikename":"炎黄寻根网","avatar":"8,05cb776a8e5e","surname":"杨","truename":null,"sex":"0","region":null}]}
     * serverTime : 2017-11-14 14:50:20
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
        private List<StemmaBean> stemma;
        private List<ClanAssociationBean> clan_association;
        private List<FriendBean> friend;

        public List<StemmaBean> getStemma() {
            return stemma;
        }

        public void setStemma(List<StemmaBean> stemma) {
            this.stemma = stemma;
        }

        public List<ClanAssociationBean> getClan_association() {
            return clan_association;
        }

        public void setClan_association(List<ClanAssociationBean> clan_association) {
            this.clan_association = clan_association;
        }

        public List<FriendBean> getFriend() {
            return friend;
        }

        public void setFriend(List<FriendBean> friend) {
            this.friend = friend;
        }

        public static class StemmaBean implements Serializable{
            /**
             * id : 2
             * name : 周氏族谱
             * surname : 周
             * region : 428836,448209,450045,450633
             * generation : 旧编字派

             十一世        可             十二世         正            十三世         塳             十四世           永               十五世         远
             十六世        如             十七世         金            十八世         对             十九世           大               二十世         廷
             金字派即世字派后须遵新编字取名庶不烦查对


             新编字派

             十七世           世           十八世            运         十九世           承         二十世           纯
             二十一世        家           二十二世        泽         二十三世       常         二十四世        新
             二十五世        文           二十六世        章         二十七世       炳         二十八世        焕
             二十九世        德           三十世            业         三十一世       维         三十二世        兴


             新添派行

             三十三世            礼              三十四世        为         三十五世         教           三十六世       本
             三十七世            福              三十八世        自         三十九世         天           四十世           申
             四十一世            宗              四十二世        傅         四十三世         必           四十四世        大
             四十五世            功              四十六世        立         四十七世         名           四十八世        成



             东西合派

             二十一世             忠                  二十二世             厚                二十三世          昭           二十四世            先             二十五世          荫
             二十六世             诗                  二十七世             书                二十八世          信           二十九世            克             三十世              昌
             三十一世             高                  三十二世             风                三十三世          傳           三十四世            礼             三十五世          学 
             三十六世             华                  三十七世             典                三十八世           焕          三十九世            文             四十世              章
             */

            private String id;
            private String name;
            private String surname;
            private String region;
            private String generation;

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

            public String getSurname() {
                return surname;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getGeneration() {
                return generation;
            }

            public void setGeneration(String generation) {
                this.generation = generation;
            }
        }

        public static class ClanAssociationBean implements Serializable{
            /**
             * id : 59
             * name : 常氏宗亲会
             * surname_id : 30
             * stemma_id : 0
             * president_rid : 1000800
             * vice_president_rid : []
             * secretary_general_rid : []
             * assistant_secretary_rid : []
             * area_id_str : 湖北省,黄冈市,麻城市
             * address : 
             * img_url : 
             * info : 大家都是一家人，和谐共处宗亲会。
             * family_instructions : 
             * status : 1
             * level : 4
             * flag : 0
             * count : 1
             * sort : 1
             * create_time : 1505722739
             * update_time : 1505722845
             * president_info : [{"rid":"1000800","nikename":"Fonda","avatar":"14,0d023ff59730","phone":"18221216750"}]
             */

            private String id;
            private String name;
            private String surname_id;
            private String stemma_id;
            private String president_rid;
            private String area_id_str;
            private String address;
            private String img_url;
            private String info;
            private String family_instructions;
            private String status;
            private String level;
            private String flag;
            private String count;
            private String sort;
            private String create_time;
            private String update_time;
            private List<SecretaryGeneralRidBean> secretary_general_rid;
            private List<VicePresidentRidBean> vice_president_rid;
            private List<AssistantSecretaryRidBean> assistant_secretary_rid;
            private List<PresidentInfoBean> president_info;

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

            public String getSurname_id() {
                return surname_id;
            }

            public void setSurname_id(String surname_id) {
                this.surname_id = surname_id;
            }

            public String getStemma_id() {
                return stemma_id;
            }

            public void setStemma_id(String stemma_id) {
                this.stemma_id = stemma_id;
            }

            public String getPresident_rid() {
                return president_rid;
            }

            public void setPresident_rid(String president_rid) {
                this.president_rid = president_rid;
            }

            public String getArea_id_str() {
                return area_id_str;
            }

            public void setArea_id_str(String area_id_str) {
                this.area_id_str = area_id_str;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getFamily_instructions() {
                return family_instructions;
            }

            public void setFamily_instructions(String family_instructions) {
                this.family_instructions = family_instructions;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
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

            public List<VicePresidentRidBean> getVice_president_rid() {
                return vice_president_rid;
            }

            public void setVice_president_rid(List<VicePresidentRidBean> vice_president_rid) {
                this.vice_president_rid = vice_president_rid;
            }

            public List<SecretaryGeneralRidBean> getSecretary_general_rid() {
                return secretary_general_rid;
            }

            public void setSecretary_general_rid(List<SecretaryGeneralRidBean> secretary_general_rid) {
                this.secretary_general_rid = secretary_general_rid;
            }

            public List<AssistantSecretaryRidBean> getAssistant_secretary_rid() {
                return assistant_secretary_rid;
            }

            public void setAssistant_secretary_rid(List<AssistantSecretaryRidBean> assistant_secretary_rid) {
                this.assistant_secretary_rid = assistant_secretary_rid;
            }

            public List<PresidentInfoBean> getPresident_info() {
                return president_info;
            }

            public void setPresident_info(List<PresidentInfoBean> president_info) {
                this.president_info = president_info;
            }
            public static class SecretaryGeneralRidBean implements Serializable{
                /**
                 * rid : 1000177
                 * nikename : 王大伟
                 * avatar : null
                 * phone : 13601666310
                 */

                private String rid;
                private String nikename;
                private String avatar;
                private String phone;

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

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }
            }

            public static class VicePresidentRidBean implements Serializable{
                /**
                 * rid : 1000172
                 * nikename : PU1
                 * avatar : null
                 * phone : 18653078615
                 */

                private String rid;
                private String nikename;
                private String avatar;
                private String phone;

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

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }
            }

            public static class AssistantSecretaryRidBean implements Serializable{
                /**
                 * rid : 1000184
                 * nikename : 样的
                 * avatar : null
                 * phone : 15120128923
                 */

                private String rid;
                private String nikename;
                private String avatar;
                private String phone;

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

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }
            }
            public static class PresidentInfoBean implements Serializable{
                /**
                 * rid : 1000800
                 * nikename : Fonda
                 * avatar : 14,0d023ff59730
                 * phone : 18221216750
                 */

                private String rid;
                private String nikename;
                private String avatar;
                private String phone;

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

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }
            }

        }

        public static class FriendBean implements Serializable{
            /**
             * rid : 1000794
             * nikename : 浪迹天涯
             * avatar : null
             * surname : 林
             * truename : null
             * sex : 0
             * region : 2,3631,3974
             */

            private String rid;
            private String nikename;
            private String avatar;
            private String surname;
            private String truename;
            private String sex;
            private String region;

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

            public String getSurname() {
                return surname;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }

            public String getTruename() {
                return truename;
            }

            public void setTruename(String truename) {
                this.truename = truename;
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
        }
    }
}
