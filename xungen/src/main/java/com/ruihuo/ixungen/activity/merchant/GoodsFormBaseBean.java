package com.ruihuo.ixungen.activity.merchant;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/8/11
 * @describe May the Buddha bless bug-free!!!
 */
public class GoodsFormBaseBean implements Serializable {
    /**
     * id : 11
     * shop_id : 11
     * status : 0
     * name : 啥？
     * type : 2
     * classify : 单人间
     * cover : https://res.ixungen.cn/img/14,0938301f20a6;https://res.ixungen.cn/img/10,0939b566a6fe;https://res.ixungen.cn/img/9,093aa50dcea5;https://res.ixungen.cn
     * price : 0.01
     * original_price : 100.00
     * info : 第一个商店图文信息无我想问一下YY无所谓婆婆容身之所9曲去去去
     * amount : 1000
     * is_reserve : 1
     * is_cancel : 2
     * service : WIFI,空调,独立卫浴,早餐
     * cancel_rule : 我以为
     * create_time : 1501758908
     */

    private String id;
    private String shop_id;
    private String status;
    private String name;
    private String type;
    private String classify;
    private String cover;
    private String img;
    private String price;
    private String original_price;
    private String info;
    private String amount;
    private String sales_volume;
    private String is_reserve;
    private String is_cancel;
    private String service;
    private String cancel_rule;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIs_reserve() {
        return is_reserve;
    }

    public void setIs_reserve(String is_reserve) {
        this.is_reserve = is_reserve;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCancel_rule() {
        return cancel_rule;
    }

    public void setCancel_rule(String cancel_rule) {
        this.cancel_rule = cancel_rule;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
