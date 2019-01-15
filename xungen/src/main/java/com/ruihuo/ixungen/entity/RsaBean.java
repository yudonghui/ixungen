package com.ruihuo.ixungen.entity;

/**
 * @author yudonghui
 * @date 2017/11/15
 * @describe May the Buddha bless bug-free!!!
 */
public class RsaBean {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 2
     * data :
     * serverTime : 2017-11-15 14:51:12
     */

    private int code;
    private String msg;
    private int totalPage;
    private Object data;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
