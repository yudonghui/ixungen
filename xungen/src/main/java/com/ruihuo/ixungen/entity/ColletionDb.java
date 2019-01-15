package com.ruihuo.ixungen.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
@Table(name = "name")
public class ColletionDb {
    @Column(name="id",isId = true)
    private int id;
    @Column(name = "match_id")
    private int match_id;
    @Column(name = "basket_match_id")
    private String basket_match_id;

    @Override
    public String toString() {
        return "ColletionDb{" +
                "id=" + id +
                ", match_id=" + match_id +
                ", basket_match_id='" + basket_match_id + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public String getBasket_match_id() {
        return basket_match_id;
    }

    public void setBasket_match_id(String basket_match_id) {
        this.basket_match_id = basket_match_id;
    }
}
