package com.ruihuo.ixungen.entity;

/**
 * @author yudonghui
 * @date 2017/8/25
 * @describe May the Buddha bless bug-free!!!
 */
public class ScrollBaseBean {//轮播图的模型
    /**
     * id : 1
     * type : 0
     * pic : http://www.ixungen.cn/data/attachment/forum/201604/13/145657vjt49gjt4994c4v1.jpg
     * url : http://www.ixungen.cn/
     * create_time : 1451581261
     * stay_time : 3
     * expire_time : 1483203661
     * begin_time : 1451581261
     * target : 0
     * status : 1
     * client : ios
     */

    private int id;
    private int type;
    private String pic;
    private String activity_id;
    private String url;
    private int create_time;
    private int stay_time;
    private int expire_time;
    private int begin_time;
    private int target;
    private int status;
    private String client;
    private String open_type;

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getStay_time() {
        return stay_time;
    }

    public void setStay_time(int stay_time) {
        this.stay_time = stay_time;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public int getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(int begin_time) {
        this.begin_time = begin_time;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
