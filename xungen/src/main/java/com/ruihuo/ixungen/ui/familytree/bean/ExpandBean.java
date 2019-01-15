package com.ruihuo.ixungen.ui.familytree.bean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/24
 * @describe May the Buddha bless bug-free!!!
 */
public class ExpandBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":"2422","pid":"2413","name":"王文焕*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2423","pid":"2413","name":"王文炳*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2425","pid":"2413","name":"王文灿*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2426","pid":"2413","name":"王文兴*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null}]
     * serverTime : 2017-10-24 16:21:11
     */

    private int code;
    private String msg;
    private String serverTime;
    private List<Tree> data;

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

    public List<Tree> getData() {
        return data;
    }

    public void setData(List<Tree> data) {
        this.data = data;
    }

}
