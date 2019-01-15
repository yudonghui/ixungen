package com.ruihuo.ixungen.ui.familytree.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/24
 * @describe May the Buddha bless bug-free!!!
 */
public class Tree implements Serializable {
    /**
     * id : 1
     * pid : 0
     * name : 王有材
     * level_id : 1
     * generation : 有
     * spouse_id : 3
     * sex : 1
     * spouse_name : 梁氏
     * childs : [{"id":"2","pid":"1","name":"王礼公","level_id":"1","generation":"","spouse_id":"4","sex":"1","spouse_name":"白氏","childs":[]},{"id":"5","pid":"1","name":"王昌公","level_id":"1","generation":"","spouse_id":"6","sex":"1","spouse_name":"郑氏","childs":[{"id":"7","pid":"5","name":"王克宽","level_id":"1","generation":"克","spouse_id":"8","sex":"1","spouse_name":"朱氏","childs":[]},{"id":"9","pid":"5","name":"王克俭","level_id":"1","generation":"","spouse_id":"10","sex":"1","spouse_name":"朱氏","childs":[]},{"id":"11","pid":"5","name":"王克修","level_id":"1","generation":"","spouse_id":"12","sex":"1","spouse_name":"丁氏","childs":[]},{"id":"13","pid":"5","name":"王克敏","level_id":"1","generation":"","spouse_id":"14","sex":"1","spouse_name":"袁氏","childs":[]},{"id":"15","pid":"5","name":"未详","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[{"id":"16","pid":"15","name":"王深公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"17","pid":"15","name":"王溥公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"18","pid":"15","name":"王连公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"19","pid":"15","name":"王泗公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"20","pid":"15","name":"王槐公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"21","pid":"15","name":"王济公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"22","pid":"15","name":"王澄公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"23","pid":"15","name":"王汝公","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]},{"id":"24","pid":"15","name":"未详","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[{"id":"26","pid":"24","name":"王时正","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"25","pid":"24","name":"王时明","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"27","pid":"24","name":"王时雍","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"28","pid":"24","name":"王时左","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"29","pid":"24","name":"王时忠","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"30","pid":"24","name":"王时荣","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"31","pid":"24","name":"王时秀","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"32","pid":"24","name":"王时爵","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"33","pid":"24","name":"王时祥","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"34","pid":"24","name":"未详","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null}]},{"id":"2413","pid":"15","name":"王九龄*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[{"id":"2422","pid":"2413","name":"王文焕*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2423","pid":"2413","name":"王文炳*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2425","pid":"2413","name":"王文灿*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2426","pid":"2413","name":"王文兴*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null}]},{"id":"2414","pid":"15","name":"王如璋*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[{"id":"2434","pid":"2414","name":"王士鉴*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2439","pid":"2414","name":"王士明*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2440","pid":"2414","name":"王士钊*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2441","pid":"2414","name":"王士模*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2442","pid":"2414","name":"王士镇*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2443","pid":"2414","name":"王士钿*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null}]},{"id":"2416","pid":"15","name":"王子明*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[{"id":"2445","pid":"2416","name":"王维让*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null},{"id":"2489","pid":"2416","name":"王为兴","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null}]},{"id":"2418","pid":"15","name":"王子先*","level_id":"1","generation":"","spouse_id":"0","sex":"1","spouse_name":null,"childs":[]}]}]}]
     */

    public String id;
    public String pid;
    public String mid;
    public String name;
    public int level_id;
    public String create_rid;
    public String rid;
    public String generation;
    public String spouse_id;
    public String sex;
    public String spouse_name;
    public String avatar_url;
    public String flag;
    public int type;
    public List<Tree> childs;
    public CenterData center = new CenterData();
    public TopData topPoint = new TopData();
    public BottomData bottomPoint = new BottomData();

    public static class CenterData implements Serializable {
        public float x;
        public float y;
    }

    public static class TopData implements Serializable {
        public float x;
        public float y;
    }

    public static class BottomData implements Serializable {
        public float x;
        public float y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getSpouse_id() {
        return spouse_id;
    }

    public void setSpouse_id(String spouse_id) {
        this.spouse_id = spouse_id;
    }

    public String getSex() {
        return sex;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSpouse_name() {
        return spouse_name;
    }

    public void setSpouse_name(String spouse_name) {
        this.spouse_name = spouse_name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCreate_rid() {
        return create_rid;
    }

    public void setCreate_rid(String create_rid) {
        this.create_rid = create_rid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public List<Tree> getChilds() {
        return childs;
    }

    public void setChilds(List<Tree> childs) {
        this.childs = childs;
    }

}
