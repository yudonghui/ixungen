package com.ruihuo.ixungen.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/4/6
 * @describe May the Buddha bless bug-free!!!
 */
public class AgnationFormBean implements Serializable{

    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"15","name":"于","surname_id":"451","img_url":"","president_rid":"1000269","secretary_general_rid":[{"rid":"1000177","nikename":"王大伟","avatar":null,"phone":"13601666310"}],"vice_president_rid":[{"rid":"1000172","nikename":"PU1","avatar":null,"phone":"18653078615"}],"assistant_secretary_rid":[{"rid":"1000184","nikename":"样的","avatar":null,"phone":"15120128923"}],"info":"了了里咯哦了一哦你你后破好好好好","family_instructions":"","flag":"0","level":"4","area_id_str":"河北省保定市北市区","address":"","user_rid":"1000269","president_info":[{"rid":"1000269","nikename":"郁金香","avatar":null,"phone":"13162821161"}]}]
     * serverTime : 2017-04-10 14:36:24
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

    public static class DataBean implements Serializable{
        /**
         * id : 15
         * name : 于
         * surname_id : 451
         * img_url : 
         * president_rid : 1000269
         * secretary_general_rid : [{"rid":"1000177","nikename":"王大伟","avatar":null,"phone":"13601666310"}]
         * vice_president_rid : [{"rid":"1000172","nikename":"PU1","avatar":null,"phone":"18653078615"}]
         * assistant_secretary_rid : [{"rid":"1000184","nikename":"样的","avatar":null,"phone":"15120128923"}]
         * info : 了了里咯哦了一哦你你后破好好好好
         * family_instructions : 
         * flag : 0
         * level : 4
         * area_id_str : 河北省保定市北市区
         * address : 
         * user_rid : 1000269
         * president_info : [{"rid":"1000269","nikename":"郁金香","avatar":null,"phone":"13162821161"}]
         */

        private String id;
        private String name;
        private String surname_id;
        private String img_url;
        private String president_rid;
        private String info;
        private String family_instructions;
        private String flag;
        private String level;
        private String area_id_str;
        private String address;
        private String user_rid;
        private String create_time;
        private String update_time;
        private String sort;
        private String status;
        private String stemma_id;
        private String count;
        private List<SecretaryGeneralRidBean> secretary_general_rid;
        private List<VicePresidentRidBean> vice_president_rid;
        private List<AssistantSecretaryRidBean> assistant_secretary_rid;
        private List<PresidentInfoBean> president_info;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStemma_id() {
            return stemma_id;
        }

        public void setStemma_id(String stemma_id) {
            this.stemma_id = stemma_id;
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

        public String getSurname_id() {
            return surname_id;
        }

        public void setSurname_id(String surname_id) {
            this.surname_id = surname_id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getPresident_rid() {
            return president_rid;
        }

        public void setPresident_rid(String president_rid) {
            this.president_rid = president_rid;
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

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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

        public String getUser_rid() {
            return user_rid;
        }

        public void setUser_rid(String user_rid) {
            this.user_rid = user_rid;
        }

        public List<SecretaryGeneralRidBean> getSecretary_general_rid() {
            return secretary_general_rid;
        }

        public void setSecretary_general_rid(List<SecretaryGeneralRidBean> secretary_general_rid) {
            this.secretary_general_rid = secretary_general_rid;
        }

        public List<VicePresidentRidBean> getVice_president_rid() {
            return vice_president_rid;
        }

        public void setVice_president_rid(List<VicePresidentRidBean> vice_president_rid) {
            this.vice_president_rid = vice_president_rid;
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
             * rid : 1000269
             * nikename : 郁金香
             * avatar : null
             * phone : 13162821161
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
}
