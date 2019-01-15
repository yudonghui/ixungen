package com.ruihuo.ixungen.activity.merchant;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/8/18
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderBaseBean implements Serializable {
    /**
     * id : 119
     * price : 0.01
     * service : WIFI,空调,独立卫浴,早餐
     * amount : 1.00
     * order_no : 302907012a6039b9
     * cover : https://res.ixungen.cn/img/14,
     * logo : https://res.ixungen.cn/img/8,09b1492ccc17
     * shop_name : 于氏小店
     * status : 0
     * shop_id : 11
     * goods_id : 11
     * content : null
     * img : null
     * classify : 单人间
     * is_reserve : 1
     * consume_start_time : 2017-08-16 00:00:00
     * consume_end_time : 2017-08-17 00:00:00
     * type : 2
     * goods_name :
     */

    private String id;
    private String price;
    private String service;
    private String amount;
    private String order_no;
    private String cover;
    private String logo;
    private String shop_name;
    private String status;
    private String shop_id;
    private String goods_id;
    private String content;
    private String img;
    private String classify;
    private String is_reserve;
    private String consume_start_time;
    private String consume_end_time;
    private String user_name;
    private String phone;
    private String type;
    private String goods_name;
    private String is_cancel;
    private String cost;
    private String info;
    private String refund_reason;//申请方
    private String refuse_reason;//拒绝原因
    private String mobile;
    private String text;
    private String avatar;
    private String create_time;
    private String comment_rid;
    private String deadline;//订单有效期（未付款）
    private String rid;
    private String mode;//商家还是用户,0是用户，1是商家

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getComment_rid() {
        return comment_rid;
    }

    public void setComment_rid(String comment_rid) {
        this.comment_rid = comment_rid;
    }

    public String getRefuse_reason() {
        return refuse_reason;
    }

    public void setRefuse_reason(String refuse_reason) {
        this.refuse_reason = refuse_reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getIs_reserve() {
        return is_reserve;
    }

    public void setIs_reserve(String is_reserve) {
        this.is_reserve = is_reserve;
    }

    public String getConsume_start_time() {
        return consume_start_time;
    }

    public void setConsume_start_time(String consume_start_time) {
        this.consume_start_time = consume_start_time;
    }

    public String getConsume_end_time() {
        return consume_end_time;
    }

    public void setConsume_end_time(String consume_end_time) {
        this.consume_end_time = consume_end_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
