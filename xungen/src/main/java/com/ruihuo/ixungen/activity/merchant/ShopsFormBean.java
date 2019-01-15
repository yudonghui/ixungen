package com.ruihuo.ixungen.activity.merchant;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/4
 * @describe May the Buddha bless bug-free!!!
 */
public class ShopsFormBean implements Serializable{


    /**
     * code : 0
     * msg : success
     * totalPage : 1
     * data : [{"id":"11","shop_id":"11","status":"0","name":"啥？","type":"2","classify":"单人间","cover":"https://res.ixungen.cn/img/14,0938301f20a6;https://res.ixungen.cn/img/10,0939b566a6fe;https://res.ixungen.cn/img/9,093aa50dcea5;https://res.ixungen.cn","price":"0.01","original_price":"100.00","info":"第一个商店图文信息无我想问一下YY无所谓婆婆容身之所9曲去去去","amount":"1000","is_reserve":"1","is_cancel":"2","service":"WIFI,空调,独立卫浴,早餐","cancel_rule":"我以为","create_time":"1501758908"},{"id":"15","shop_id":"11","status":"0","name":"找我玩","type":"2","classify":"单人间","cover":"https://res.ixungen.cn/img/8,096bdac33b10;https://res.ixungen.cn/img/12,096c9b73f5ec","price":"258.00","original_price":"100.00","info":" www应用数学","amount":"2","is_reserve":"1","is_cancel":"1","service":"电脑宽带,空调,茶水饮料","cancel_rule":"他无我想问一下","create_time":"1501847810"},{"id":"45","shop_id":"11","status":"0","name":"我以为","type":"2","classify":"套间","cover":"https://res.ixungen.cn/img/11,0a177fc14164;https://res.ixungen.cn/img/11,0a18f4ceb293","price":"288.00","original_price":"100.00","info":"","amount":"9","is_reserve":"1","is_cancel":"1","service":"电脑宽带,独立卫浴,早餐","cancel_rule":null,"create_time":"1502248233"},{"id":"46","shop_id":"11","status":"0","name":"我以为","type":"2","classify":"复式套间","cover":"https://res.ixungen.cn/img/13,0a1937b272a5;https://res.ixungen.cn/img/8,0a1a67d001b7","price":"99.00","original_price":"100.00","info":"","amount":"99","is_reserve":"1","is_cancel":"1","service":"早餐,独立卫浴","cancel_rule":null,"create_time":"1502248269"},{"id":"47","shop_id":"11","status":"0","name":"我","type":"2","classify":"行政间","cover":"https://res.ixungen.cn/img/9,0a1b37212ca4;https://res.ixungen.cn/img/10,0a1c1e7f565b","price":"9.00","original_price":"100.00","info":" 我磨破庸人自扰之且行且珍惜rosy知我者谓我心忧无我想问一下YY","amount":"99","is_reserve":"1","is_cancel":"1","service":"WIFI,电脑宽带,早餐,茶水饮料,独立卫浴,空调","cancel_rule":"我摸摸弄璋之喜","create_time":"1502248321"},{"id":"48","shop_id":"11","status":"0","name":"有滋有味","type":"2","classify":"豪华间","cover":"https://res.ixungen.cn/img/12,0a1dcdc9c74f;https://res.ixungen.cn/img/8,0a1e291da537","price":"369.00","original_price":"100.00","info":" 需要勇气我也@","amount":"9","is_reserve":"2","is_cancel":"2","service":null,"cancel_rule":"味","create_time":"1502248360"}]
     * serverTime : 2017-08-14 15:14:38
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

    public static class DataBean extends GoodsFormBaseBean{

    }
}
