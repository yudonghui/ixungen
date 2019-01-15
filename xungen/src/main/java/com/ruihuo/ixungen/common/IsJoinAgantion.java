package com.ruihuo.ixungen.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.useractivity.AgnationFormActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationInfoActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationMemeberActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationNewActivity;
import com.ruihuo.ixungen.activity.useractivity.RealNameActivity;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.EditDialogUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;

import io.rong.imkit.RongIM;

/**
 * @author yudonghui
 * @date 2017/4/22
 * @describe May the Buddha bless bug-free!!!
 */
public class IsJoinAgantion {
    /**
     * 如果没有宗亲id那么说明还没有加入任何宗亲会，弹出提示创建还是加入宗亲会。
     * 有宗亲id 1.一个id 那说明只有一个宗亲直接显示宗亲会
     * 2.多个id 直接跳到宗亲列表然后再选择进入哪一个宗亲会。
     */
    private Context mContext;
    private Activity mActivity;

    public IsJoinAgantion(Context mContext) {
        this.mContext = mContext;
    }

    public IsJoinAgantion(Activity mActivity) {
        this.mActivity = mActivity;
    }


    public void isJoin() {
        if (TextUtils.isEmpty(XunGenApp.surname)) {
            setSurname();
            return;
        }
        if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
            showDialog();
        } else {
            String[] split = (XunGenApp.clanAssociationIds).split("\\,");
            if (split.length > 1) {
                Intent intent = new Intent(mContext, AgnationFormActivity.class);
                intent.putExtra("from", "self");
                mContext.startActivity(intent);
            } else {
               // Intent intent = new Intent(mContext, AgnationActivity.class);
                Intent intent = new Intent(mContext, AgnationNewActivity.class);
                intent.putExtra("id", split[0]);
                mContext.startActivity(intent);
            }
        }
    }

    //这个方法是如果加入的有宗亲会那么直接就跳转到宗亲会成员列表。专门为底部第二个导航做的
    public void isJoin(String from) {
        if (TextUtils.isEmpty(XunGenApp.surname)) {
            setSurname();
            return;
        }
        if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
            showDialog();
        } else {
            String[] split = (XunGenApp.clanAssociationIds).split("\\,");
            if (split.length > 1) {
                Intent intent = new Intent(mContext, AgnationFormActivity.class);
                intent.putExtra("from", from);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, AgnationMemeberActivity.class);
                intent.putExtra("associationId", split[0]);
                intent.putExtra("from", "AgationActivity");
                mContext.startActivity(intent);
            }
        }
    }

    public void isJoinGroup() {
        if (TextUtils.isEmpty(XunGenApp.surname)) {
            setSurname();
            return;
        }
        if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
            showDialog();
        } else {
            String[] split = (XunGenApp.clanAssociationIds).split("\\,");
            if (split.length > 1) {
                Intent intent = new Intent(mContext, AgnationFormActivity.class);
                intent.putExtra("from", "groups");
                mContext.startActivity(intent);
            } else {
                /*Intent intent = new Intent(mContext, AgnationMemeberActivity.class);

                intent.putExtra("associationId", split[0]);
                intent.putExtra("from", "AgationActivity");
                mContext.startActivity(intent);*/
                RongIM.getInstance().startGroupChat(mContext, split[0], "宗亲会");
            }
        }
    }

    private void setSurname() {
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
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("surname", message);
                    mHttp.post(Url.USER_INFO_CHANG_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            XunGenApp.surname = message;
                            //创建姓氏成功后提醒创建还是加入宗亲会。
                            if (TextUtils.isEmpty(XunGenApp.clanAssociationIds)) {
                                showDialog();
                            } else {
                                String[] split = (XunGenApp.clanAssociationIds).split("\\,");
                                if (split.length > 1) {
                                    Intent intent = new Intent(mContext, AgnationFormActivity.class);
                                    intent.putExtra("from", "self");
                                    mContext.startActivity(intent);
                                } else {
                                   // Intent intent = new Intent(mContext, AgnationActivity.class);
                                    Intent intent = new Intent(mContext, AgnationNewActivity.class);
                                    intent.putExtra("id", split[0]);
                                    mContext.startActivity(intent);
                                }

                            }
                        }

                        @Override
                        public void onError(String message) {

                        }
                    });
                }

            }
        });
    }

    public void showDialog() {
        final View inflate = View.inflate(mContext, R.layout.photo_popup, null);
        final TextView mCamera = (TextView) inflate.findViewById(R.id.buttonCamera);
        TextView mPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        Button mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        final Dialog mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        Window window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
        mCamera.setText("创建宗亲会");
        mPhoto.setText("加入宗亲会");
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
               /*
               在创建宗亲会的时候需要先判断，姓氏是否存在。
                  如果姓氏不存在分为两种情况
                      1.已经进行过实名验证了。这个时候只是把弹出框取消没有修改姓氏的提示。无法创建宗亲会
                      2.没有进行过实名认证。这个时候提示可以进行实名认证后再创建宗亲会
                  如果姓氏存在，继续判断是否进行了实名认证。
                      1.进行过实名认证那么直接跳转到创建界面。
                      2.没有认证过，跳转到认证界面。

               */
                if (TextUtils.isEmpty(XunGenApp.surnameId)) {
                    //如果姓氏id为空说明没有这个姓氏
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    if (TextUtils.isEmpty(XunGenApp.trueName)) {
                        hintDialogUtils.setMessage(XunGenApp.message + "\n是否实名认证修改姓氏？");
                        hintDialogUtils.setConfirm("是", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                if (!TextUtils.isEmpty(XunGenApp.phone)) {
                                    Intent intent = new Intent(mContext, RealNameActivity.class);
                                    intent.putExtra("phone", XunGenApp.phone);
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                        hintDialogUtils.setTvCancel("否");
                    } else {
                        hintDialogUtils.setVisibilityCancel();
                        hintDialogUtils.setMessage(XunGenApp.message);
                    }
                } else if (TextUtils.isEmpty(XunGenApp.trueName)) {

                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    hintDialogUtils.setMessage("创建宗亲会需要实名认证,是否进行实名认证？");
                    hintDialogUtils.setConfirm("是", new DialogHintInterface() {
                        @Override
                        public void callBack(View view) {
                            if (!TextUtils.isEmpty(XunGenApp.phone)) {
                                Intent intent = new Intent(mContext, RealNameActivity.class);
                                intent.putExtra("phone", XunGenApp.phone);
                                mContext.startActivity(intent);
                            }
                        }
                    });
                    hintDialogUtils.setTvCancel("否");
                } else {
                    Intent intent = new Intent(mContext, AgnationInfoActivity.class);
                    intent.putExtra("from", "create");
                    mContext.startActivity(intent);
                }
            }
        });
        //加入宗亲会
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(mContext, AgnationFormActivity.class);
                mContext.startActivity(intent);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void showDialog(final int requestCode) {
        final View inflate = View.inflate(mActivity, R.layout.photo_popup, null);
        final TextView mCamera = (TextView) inflate.findViewById(R.id.buttonCamera);
        TextView mPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        Button mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        final Dialog mDialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        Window window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        mDialog.show();
        window.setGravity(Gravity.BOTTOM);
        mCamera.setText("创建宗亲会");
        mPhoto.setText("加入宗亲会");
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
               /*
               在创建宗亲会的时候需要先判断，姓氏是否存在。
                  如果姓氏不存在分为两种情况
                      1.已经进行过实名验证了。这个时候只是把弹出框取消没有修改姓氏的提示。无法创建宗亲会
                      2.没有进行过实名认证。这个时候提示可以进行实名认证后再创建宗亲会
                  如果姓氏存在，继续判断是否进行了实名认证。
                      1.进行过实名认证那么直接跳转到创建界面。
                      2.没有认证过，跳转到认证界面。

               */
                if (TextUtils.isEmpty(XunGenApp.surnameId)) {
                    //如果姓氏id为空说明没有这个姓氏
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mActivity);
                    if (TextUtils.isEmpty(XunGenApp.trueName)) {
                        hintDialogUtils.setMessage(XunGenApp.message + "\n是否实名认证修改姓氏？");
                        hintDialogUtils.setConfirm("是", new DialogHintInterface() {
                            @Override
                            public void callBack(View view) {
                                if (!TextUtils.isEmpty(XunGenApp.phone)) {
                                    Intent intent = new Intent(mActivity, RealNameActivity.class);
                                    intent.putExtra("phone", XunGenApp.phone);
                                    mActivity.startActivityForResult(intent, requestCode);
                                }
                            }
                        });
                        hintDialogUtils.setTvCancel("否");
                    } else {
                        hintDialogUtils.setVisibilityCancel();
                        hintDialogUtils.setMessage(XunGenApp.message);
                    }
                } else if (TextUtils.isEmpty(XunGenApp.trueName)) {

                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mActivity);
                    hintDialogUtils.setMessage("创建宗亲会需要实名认证,是否进行实名认证？");
                    hintDialogUtils.setConfirm("是", new DialogHintInterface() {
                        @Override
                        public void callBack(View view) {
                            if (!TextUtils.isEmpty(XunGenApp.phone)) {
                                Intent intent = new Intent(mActivity, RealNameActivity.class);
                                intent.putExtra("phone", XunGenApp.phone);
                                mActivity.startActivityForResult(intent, requestCode);
                            }
                        }
                    });
                    hintDialogUtils.setTvCancel("否");
                } else {
                    Intent intent = new Intent(mActivity, AgnationInfoActivity.class);
                    intent.putExtra("from", "createfinish");
                    mActivity.startActivityForResult(intent, requestCode);
                }
            }
        });
        //加入宗亲会
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(mActivity, AgnationFormActivity.class);
                mActivity.startActivityForResult(intent, requestCode);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

}
