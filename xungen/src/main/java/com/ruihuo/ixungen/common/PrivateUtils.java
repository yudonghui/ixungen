package com.ruihuo.ixungen.common;

/**
 * @author yudonghui
 * @date 2017/11/7
 * @describe May the Buddha bless bug-free!!!
 */
public class PrivateUtils {
    public String getPrivateStr(String message) {
        switch (message) {
            case "0":
                return "禁止访问";
            case "1":
                return "自己可见";
            case "2":
                return "家族可见";
            case "9":
                return "完全公开";
            default:
                return "完全公开";
        }
    }

    public String getPrivateNum(String message) {
        switch (message) {
            case "禁止访问":
                return "0";
            case "自己可见":
                return "1";
            case "家族可见":
                return "2";
            case "完全公开":
                return "9";
            default:
                return "9";
        }
    }
}
