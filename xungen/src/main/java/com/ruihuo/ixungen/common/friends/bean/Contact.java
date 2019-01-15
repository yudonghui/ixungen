package com.ruihuo.ixungen.common.friends.bean;

import java.io.Serializable;

/**
 * @Author: duke
 * @DateTime: 2016-08-12 14:55
 * @Description:
 */
public class Contact implements Comparable<Contact>,Serializable {
    public String name;         //联系人名称
    public String pinYin;       //联系人拼音
    public String firstPinYin;  //联系人-姓拼音-首字母
    public String avatar;       //头像链接
    public String rid;          //根号
    public String phone;        //手机号
    public String sex;          //性别
    public String birthday;     //生日
    public String region;       //地址
    public String association_id; //宗亲id

    @Override
    public int compareTo(Contact another) {
        return firstPinYin.toUpperCase().compareTo(another.firstPinYin.toUpperCase());
    }
}