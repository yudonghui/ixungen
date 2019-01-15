package com.ruihuo.ixungen.activity.merchant;

/**
 * @author yudonghui
 * @date 2017/8/17
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderDetailBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"142","price":"0.02","service":"早餐,独立卫浴","amount":"1.00","cover":"https://res.ixungen.cn/img/13,0b5bdaa7c447","goods_name":"图文信息","classify":"套间","consume_start_time":"2017-08-17 00:00:00","consume_end_time":"2017-08-18 00:00:00","user_name":"弄","phone":"13162821161","cost":"0.02","status":"0","type":"2","is_cancel":"2","order_no":"74bc4e37d19d0954","info":"","refund_reason":"","create_time":"1502954600","avatar":"10,0a6e85cfd6be"}
     * serverTime : 2017-08-17 16:40:41
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

    public static class DataBean extends OrderBaseBean {
    }
}
