package com.ruihuo.ixungen.entity;

/**
 * @author yudonghui
 * @date 2017/4/25
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionSmsDetailBean {

    /**
     * code : 0
     * msg : success
     * data : {"id":"8","name":"林子发起的活动测试","organizer_id":"0","rid":"1000254","type":"1","start_time":"1493611200","end_time":"1493625600","address":"123,123","cost":"0","content":"测试","total_planned":"20","count":"0","participation_status":"0","activity_status":"0","sort":"1","create_time":"1491016159","update_time":"1491016159","picurl":"","reminder":"","summary":"","classify":"0"}
     * serverTime : 2017-04-25 18:00:29
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
         * id : 8
         * name : 林子发起的活动测试
         * organizer_id : 0
         * rid : 1000254
         * type : 1
         * start_time : 1493611200
         * end_time : 1493625600
         * address : 123,123
         * cost : 0
         * content : 测试
         * total_planned : 20
         * count : 0
         * participation_status : 0
         * activity_status : 0
         * sort : 1
         * create_time : 1491016159
         * update_time : 1491016159
         * picurl :
         * reminder :
         * summary :
         * classify : 0
         * status:
         */

        private String id;
        private String name;
        private String organizer_id;
        private String rid;
        private String type;
        private String start_time;
        private String end_time;
        private String address;
        private String cost;
        private String content;
        private String total_planned;
        private String count;
        private String participation_status;
        private String activity_status;
        private String sort;
        private String create_time;
        private String update_time;
        private String picurl;
        private String reminder;
        private String summary;
        private String classify;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getOrganizer_id() {
            return organizer_id;
        }

        public void setOrganizer_id(String organizer_id) {
            this.organizer_id = organizer_id;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTotal_planned() {
            return total_planned;
        }

        public void setTotal_planned(String total_planned) {
            this.total_planned = total_planned;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getParticipation_status() {
            return participation_status;
        }

        public void setParticipation_status(String participation_status) {
            this.participation_status = participation_status;
        }

        public String getActivity_status() {
            return activity_status;
        }

        public void setActivity_status(String activity_status) {
            this.activity_status = activity_status;
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

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }
    }
}
