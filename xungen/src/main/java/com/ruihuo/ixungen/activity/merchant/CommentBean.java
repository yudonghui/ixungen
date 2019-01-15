package com.ruihuo.ixungen.activity.merchant;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/10
 * @describe May the Buddha bless bug-free!!!
 */
public class CommentBean {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 1
     * data : [{"id":138,"shop_id":10,"comment_rid":1000312,"comment_name":"董硕","comment_avtar":"9,0518685d6820","reply_rid":"","content":"这个店真的很好","cost":122,"score":4,"img":"http://res.ixungen.cn/img/14,0a34b53dea50","comment_time":1502269953,"people_like":0,"total_reply":0},{"id":136,"shop_id":10,"comment_rid":1000312,"comment_name":"董硕","comment_avtar":"9,0518685d6820","reply_rid":"","content":"这是什么的话就是哪里呢你自己想想是不是这样想和他在一起了不算什么的话就是哪里呢","cost":1123,"score":4,"img":"http://res.ixungen.cn/img/9,0a2dfc9fa86d;http://res.ixungen.cn/img/9,0a2e09899a2d;http://res.ixungen.cn/img/13,0a2fa4c9c709;http://res.ixungen.cn/img/11,0a3000ddfd7e;http://res.ixungen.cn/img/12,0a32d13c30cf;http://res.ixungen.cn/img/9,0a314e5d4f05","comment_time":1502267249,"people_like":0,"total_reply":0}]
     * serverTime : 2017-08-10 08:49:39
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

    public static class DataBean extends CommentBaseBean {

    }
}
