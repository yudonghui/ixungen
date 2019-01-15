package com.ruihuo.ixungen.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.activity.home.XWFormActivity;
import com.ruihuo.ixungen.activity.login.PasswordLoginActivity;
import com.ruihuo.ixungen.activity.login.PhoneLoginActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.activity.TreeActivity;
import com.ruihuo.ixungen.ui.main.activity.XSJPActivity;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;

/**
 * Created by Administrator on 2017/3/23.
 */

public class IntentSkip {
    public void skipLogin(final Context mContext, int requestCode) {
        SharedPreferences sp = mContext.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        String loginMethod = sp.getString("loginMethod", "");
        Intent intent = new Intent();
        if ("password".equals(loginMethod)) {
            intent.setClass(mContext, PasswordLoginActivity.class);
        } else {
            intent.setClass(mContext, PhoneLoginActivity.class);
        }
        ((Activity) mContext).startActivityForResult(intent, requestCode);
        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_in_from_bottom);
    }

    public void skipType(Context mContext, String type, Bundle bundle) {
        switch (type) {
            case ConstantNum.XGWZ://寻根问祖
                if (!XunGenApp.isLogin) {
                    skipLogin(mContext, 0);
                    return;
                }
                Intent intentXgwz = new Intent(mContext, XWFormActivity.class);
                mContext.startActivity(intentXgwz);
                break;
            case ConstantNum.XSWH://姓氏文化
                String url;
                if (XunGenApp.isLogin) {
                    url = Url.H5_XSWH_URL + XunGenApp.surnameId + "/token/" + XunGenApp.token;
                    if (TextUtils.isEmpty(XunGenApp.surname)) {
                        setSurname(mContext);
                        return;
                    }
                } else url = Url.H5_XSWH_FROM_URL;

                Intent intentXswh = new Intent(mContext, H5Activity.class);
                intentXswh.putExtra("url", url);
                mContext.startActivity(intentXswh);
                break;
            case ConstantNum.MY_AGNATION://我的宗亲
                if (!XunGenApp.isLogin) {
                    skipLogin(mContext, 0);
                    return;
                }
                IsJoinAgantion isJoinAgantion = new IsJoinAgantion(mContext);
                isJoinAgantion.isJoin();
                break;
            case ConstantNum.ZQSY://宗亲商业

                break;
            case ConstantNum.ZQHD://宗亲活动
                if (bundle != null) {
                    String associationId = bundle.getString("associationId");
                    //标识 0全部,1最新,2最热,3精华
                    String url2 = Url.H5_ZZHD_URL + "0/userId/" + XunGenApp.rid + "/id/" + associationId + "/token/" + XunGenApp.token;
                    Intent intent = new Intent(mContext, H5Activity.class);
                    intent.putExtra("url", url2);
                    mContext.startActivity(intent);
                }

                break;
            case ConstantNum.JGJX://家规家训
                if (bundle != null) {
                    String associationId = bundle.getString("associationId");
                    String url2 = Url.H5_JGJX_URL + associationId + "/token/" + XunGenApp.token;
                    Intent intent = new Intent(mContext, H5Activity.class);
                    intent.putExtra("url", url2);
                    mContext.startActivity(intent);
                }
                break;
            case ConstantNum.ZQHZ://宗亲互助
                if (bundle != null) {
                    String associationId = bundle.getString("associationId");
                    //标识 0全部,1最新,2最热,3精华
                    String url2 = Url.H5_ZZHZ_URL + associationId + "/type/0/rid/" + XunGenApp.rid + "/token/" + XunGenApp.token;
                    Intent intent = new Intent(mContext, H5Activity.class);
                    intent.putExtra("url", url2);
                    mContext.startActivity(intent);
                }
                break;
            case ConstantNum.XSJP://姓氏家谱
                if (!XunGenApp.isLogin) {
                    skipLogin(mContext, 0);
                    return;
                }
                Intent intent = new Intent(mContext, XSJPActivity.class);
                mContext.startActivity(intent);
                /*String url1 = Url.H5_SURNAME_TREE_URL + "/keyword/" + XunGenApp.surname + "/createId/" + XunGenApp.rid + "/token/" + XunGenApp.token;
                Intent intentXsjp = new Intent(mContext, H5Activity.class);
                intentXsjp.putExtra("url", url1);
                mContext.startActivity(intentXsjp);*/
                break;
        }
    }

    private void setSurname(final Context mContext) {
        EditDialogUtils editDialogUtils = new EditDialogUtils(mContext);
        editDialogUtils.setTitle("请输入用户姓氏");
        editDialogUtils.setConfirm("确定", new DialogEditInterface() {
            @Override
            public void callBack(final String message) {
                /**
                 *如果没有设置姓氏的话，要先设置姓氏。
                 * 设置成功之后再进行提示创建宗亲会。
                 * 如果有姓氏的话就直接创建
                 */
                if (!TextUtils.isEmpty(message)) {
                    final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("surname", message);
                    mHttp.post(Url.USER_INFO_CHANG_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            XunGenApp.surname = message;
                            //姓氏文化需要姓氏id，所以要转化一下
                            NetWorkData netWorkData = new NetWorkData();
                            netWorkData.getByName(mContext);
                        }

                        @Override
                        public void onError(String message) {

                        }
                    });
                }

            }
        });
    }

    public void skipTreeActivity(Context mContext, Bundle bundle) {
        String stemmaId = bundle.getString("stemmaId");
        if (TextUtils.isEmpty(stemmaId)) {//家谱
            bundle.putBoolean("flag", false);
        } else {//族谱
            bundle.putBoolean("flag", true);
        }
        Intent intent = new Intent(mContext, TreeActivity.class);
        intent.putExtras(bundle);
        ((Activity) mContext).startActivityForResult(intent, 221);
    }

  /*  public void skipTreeActivity(Context mContext, String stemmaId, String id, boolean treeOrbook) {
        if (TextUtils.isEmpty(stemmaId)) {//家谱
            Intent intent = new Intent(mContext, TreeActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("flag", false);//家谱
            intent.putExtra("treeOrbook", treeOrbook);
            mContext.startActivity(intent);
        } else {//族谱
            Intent intent = new Intent(mContext, TreeActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("stemmaId", stemmaId);
            intent.putExtra("flag", true);//族谱
            intent.putExtra("treeOrbook", treeOrbook);
            mContext.startActivity(intent);
        }
    }*/
}

