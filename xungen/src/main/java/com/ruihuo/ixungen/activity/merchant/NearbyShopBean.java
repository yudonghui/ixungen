package com.ruihuo.ixungen.activity.merchant;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class NearbyShopBean {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 1
     * data : [{"id":1,"consumption_per_person":188,"level":4,"shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","type":2,"shop_name":"寻根小卖铺","total_comment":0},{"id":11,"consumption_per_person":288,"level":5,"shop_card_a":"https://res.ixungen.cn/img/12,07d13570e72d","shop_card_b":"https://res.ixungen.cn/img/9,07d3e2901047","type":0,"shop_name":"于氏小店","total_comment":0},{"id":12,"consumption_per_person":328,"level":4,"shop_card_a":"http://res.ixungen.cn/img/11,07f783fd8475","shop_card_b":"http://res.ixungen.cn/img/12,07f8176a7b3d","type":0,"shop_name":"麻辣烫","total_comment":0},{"id":13,"consumption_per_person":158,"level":3,"shop_card_a":"http://res.ixungen.cn/img/11,07cfbe865e17","shop_card_b":"http://res.ixungen.cn/img/9,07d0494252d5","type":0,"shop_name":"店小二","total_comment":0}]
     * serverTime : 2017-08-10 16:34:19
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

    public static class DataBean extends NearbyShopBaseBean {

    }
}
