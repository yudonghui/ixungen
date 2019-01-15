package com.ruihuo.ixungen.utils;

import android.util.Log;

import com.ruihuo.ixungen.entity.ColletionDb;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class DbManager {
    private org.xutils.DbManager.DaoConfig mDaoConfig;
    public DbManager(){
        mDaoConfig=new org.xutils.DbManager.DaoConfig();
        mDaoConfig.setDbName("collection");
        mDaoConfig.setDbVersion(1);
        mDaoConfig.setDbUpgradeListener(new org.xutils.DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(org.xutils.DbManager db, int oldVersion, int newVersion) {

            }
        });
    }
    public void inserts(ColletionDb colletionDb) throws DbException {
        x.getDb(mDaoConfig).save(colletionDb);
    }
    public void delete(ColletionDb colletionDb) throws DbException {
        x.getDb(mDaoConfig).delete(colletionDb);
        Log.e("查询结果",queryAll().toString());
    }
    public void deleteWhere(int match) throws DbException {
        x.getDb(mDaoConfig).delete(ColletionDb.class, WhereBuilder.b("match_id","=",match));

    }
    public void deleteWhereBasket(String match) throws DbException {
        x.getDb(mDaoConfig).delete(ColletionDb.class, WhereBuilder.b("basket_match_id","=",match));

    }
    /**
     * 查询所有
     *
     * @return
     * @throws DbException
     */
    public List<ColletionDb> queryAll() throws DbException {

        return x.getDb(mDaoConfig).findAll(ColletionDb.class);
    }
}
