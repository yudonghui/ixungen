package com.ruihuo.ixungen.activity.merchant;

import java.io.Serializable;

/**
 * @author yudonghui
 * @date 2017/8/9
 * @describe May the Buddha bless bug-free!!!
 */
public class OrderData implements Serializable {//订单数据
    private int type;               //订单类型：1-订餐；2-定酒店；3-家谱查询
    private String goodsId;         // 	商品编号（家谱编号）
    private String shopId;          // 	店铺编号
    private String consumeStartTime;// 	消费开始时间
    private String consumeEndTime;  // 	消费结束时间
    private String cost;//消费金额
    private String name;//商品名
    private String price;//商品单价
    private String allAmount;//总数量，不是购买数量
    private String amount;//购买的数量
    private String info;//商品详情
    private String classify;//分类，定酒店时传
    private String service;//服务，定酒店时传
    private String logo;//店铺头像
    private String shopName;//店铺名称
    private String phone;//手机号
    private String userName;//入住人的姓名
    private String cover;//购买的商品的图片
    private int days;//酒店预订天数
    private float allMoney;//总钱数
    private String isCancel;//是否可取消

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public float getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(float allMoney) {
        this.allMoney = allMoney;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getConsumeStartTime() {
        return consumeStartTime;
    }

    public void setConsumeStartTime(String consumeStartTime) {
        this.consumeStartTime = consumeStartTime;
    }

    public String getConsumeEndTime() {
        return consumeEndTime;
    }

    public void setConsumeEndTime(String consumeEndTime) {
        this.consumeEndTime = consumeEndTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(String allAmount) {
        this.allAmount = allAmount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
