package com.ruihuo.ixungen.activity.merchant;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/15
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderFormBean implements Serializable{


    /**
     * code : 0
     * msg : 成功
     * totalPage : 1
     * data : [{"id":"119","price":"0.01","service":"WIFI,空调,独立卫浴,早餐","amount":"1.00","order_no":"302907012a6039b9","cover":"https://res.ixungen.cn/img/14,","logo":"https://res.ixungen.cn/img/8,09b1492ccc17","shop_name":"于氏小店","status":"0","shop_id":"11","goods_id":"11","content":null,"img":null,"classify":"单人间","is_reserve":"1","consume_start_time":"2017-08-16 00:00:00","consume_end_time":"2017-08-17 00:00:00","type":"2","goods_name":""},{"id":"122","price":"99.00","service":"早餐,独立卫浴","amount":"1.00","order_no":"f8546837cf2e0c7d","cover":"https://res.ixungen.cn/img/13,","logo":"https://res.ixungen.cn/img/8,09b1492ccc17","shop_name":"于氏小店","status":"0","shop_id":"11","goods_id":"46","content":null,"img":null,"classify":"复式套间","is_reserve":"1","consume_start_time":"2017-08-16 00:00:00","consume_end_time":"2017-08-17 00:00:00","type":"2","goods_name":"我以为"}]
     * serverTime : 2017-08-16 16:10:44
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

    public static class DataBean extends OrderBaseBean{

    }
}
