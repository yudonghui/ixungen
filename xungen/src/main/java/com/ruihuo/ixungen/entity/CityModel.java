package com.ruihuo.ixungen.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class CityModel {

    /**
     * code : 200
     * msg : success
     * data : [{"id":2,"name":"福建省","pid":1,"sort":1,"level":2,"longcode":35,"code":"35"},{"id":18047,"name":"浙江省","pid":1,"sort":2,"level":2,"longcode":33,"code":"33"},{"id":55845,"name":"甘肃省","pid":1,"sort":3,"level":2,"longcode":62,"code":"62"}]
     * servertime : 2017-01-05 17:08:19
     */

    private int code;
    private String msg;
    private String servertime;
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

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * name : 福建省
         * pid : 1
         * sort : 1
         * level : 2
         * longcode : 35
         * code : 35
         */

        private int id;
        private String name;
        private int pid;
        private int sort;
        private int level;
        private int longcode;
        private String code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLongcode() {
            return longcode;
        }

        public void setLongcode(int longcode) {
            this.longcode = longcode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
