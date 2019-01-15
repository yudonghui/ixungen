package com.ruihuo.ixungen.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.RecommendActivity;
import com.ruihuo.ixungen.activity.XunGenActivity;
import com.ruihuo.ixungen.activity.chatactivity.JoinDiscussionActivity;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.activity.merchant.UnusedOrderActivity;
import com.ruihuo.ixungen.activity.useractivity.AgnationNewActivity;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.entity.RecommendData;
import com.ruihuo.ixungen.entity.UserInfoBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.LogUtils;
import com.ruihuo.ixungen.utils.RongConnect;
import com.ruihuo.ixungen.utils.StringUtil;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * @author yudonghui
 * @date 2017/4/7
 * @describe May the Buddha bless bug-free!!!
 */
public class NetWorkData {
    UserInfoBean userInfoBean = new UserInfoBean();
 /*   private UserInfoInterface mInterface;
    private Context mContext;
    public NetWorkData(Context mContext,UserInfoInterface mInterface) {
        this.mInterface = mInterface;
        this.mContext=mContext;
    }*/

    public void getUserInfo(final Context mContext, final UserInfoInterface mInterface) {

        //获取个人信息
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.getW(Url.GETUSERINFO_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                userInfoBean = gson.fromJson(result, UserInfoBean.class);
                String clanAssociationIds = userInfoBean.getData().getClanAssociationIds();
                XunGenApp.surname = userInfoBean.getData().getSurname();
                XunGenApp.clanAssociationIds = clanAssociationIds;
                XunGenApp.ry_token = userInfoBean.getData().getRong_yun_token();
                XunGenApp.nikename = userInfoBean.getData().getNikename();
                XunGenApp.rid = userInfoBean.getData().getRid();
                XunGenApp.phone = userInfoBean.getData().getPhone();
                XunGenApp.trueName = userInfoBean.getData().getTruename();
                XunGenApp.idCard = userInfoBean.getData().getIdcard();
                XunGenApp.sex = userInfoBean.getData().getSex();
                XunGenApp.birthday = userInfoBean.getData().getBirthday();
                getByName(mContext);
                RongConnect.connect(userInfoBean.getData().getRong_yun_token(), mContext);
                mInterface.callBack(userInfoBean);

                /**
                 *刷新自己在融云上缓存的信息
                 */
                String avatar = userInfoBean.getData().getAvatar();
                Uri uri = Uri.parse(avatar);
                UserInfo userInfo = new UserInfo(XunGenApp.rid, XunGenApp.nikename, uri);
                RongIM.getInstance().refreshUserInfoCache(userInfo);
                Intent intent = new Intent();
                intent.setAction(ConstantNum.LOGIN_SUCCESS);
                intent.putExtra("isUpdate", true);
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void getUserInfo(final Context mContext, String token) {

        //获取个人信息
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", token);
        mHttp.getW(Url.GETUSERINFO_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                userInfoBean = gson.fromJson(result, UserInfoBean.class);
                String clanAssociationIds = userInfoBean.getData().getClanAssociationIds();
                XunGenApp.surname = userInfoBean.getData().getSurname();
                XunGenApp.clanAssociationIds = clanAssociationIds;
                XunGenApp.ry_token = userInfoBean.getData().getRong_yun_token();
                XunGenApp.nikename = userInfoBean.getData().getNikename();
                XunGenApp.rid = userInfoBean.getData().getRid();
                XunGenApp.phone = userInfoBean.getData().getPhone();
                XunGenApp.trueName = userInfoBean.getData().getTruename();
                XunGenApp.idCard = userInfoBean.getData().getIdcard();
                XunGenApp.sex = userInfoBean.getData().getSex();
                XunGenApp.birthday = userInfoBean.getData().getBirthday();
                getByName(mContext);
                RongConnect.connect(userInfoBean.getData().getRong_yun_token(), mContext);

                /**
                 *刷新自己在融云上缓存的信息
                 */
                String avatar = userInfoBean.getData().getAvatar();
                Uri uri = Uri.parse(avatar);
                UserInfo userInfo = new UserInfo(XunGenApp.rid, XunGenApp.nikename, uri);
                RongIM.getInstance().refreshUserInfoCache(userInfo);
                Intent intent = new Intent();
                intent.setAction(ConstantNum.LOGIN_SUCCESS);
                intent.putExtra("isUpdate", true);
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void getGroupInfo(final String groupId) {
        if (!TextUtils.isEmpty(groupId)) {
            HttpUtils httpUtils = new HttpUtils();
            Bundle params = new Bundle();
            params.putString("id", groupId);
            httpUtils.get(Url.AGNATION_FORM_URL, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    AgnationFormBean agnationFormBean = gson.fromJson(result, AgnationFormBean.class);
                    int code = agnationFormBean.getCode();
                    if (code == 0) {
                        List<AgnationFormBean.DataBean> data = agnationFormBean.getData();
                        String name1 = data.get(0).getName();
                        String name = TextUtils.isEmpty(name1) ? "" : name1;
                        String img_url = data.get(0).getImg_url();
                        Uri uri = Uri.parse(img_url);
                        Group group = new Group(groupId, name, uri);
                        RongIM.getInstance().refreshGroupInfoCache(group);
                    }

                }

                @Override
                public void onError(String message) {
                }
            });
        }
    }

    public void getUserInfo(String rid) {
        HttpUtils httpUtils = new HttpUtils();
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("userRid", rid);
        httpUtils.get(Url.FIND_ALL_FRIEND_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                FriendBean.DataBean dataBean = friendBean.getData().get(0);
                String avatar = dataBean.getAvatar();
                Uri uri = Uri.parse(avatar);
                UserInfo userInfo = new UserInfo(dataBean.getRid(), dataBean.getNikename(), uri);
                RongIM.getInstance().refreshUserInfoCache(userInfo);
            }

            @Override
            public void onError(String message) {

            }
        });

    }

    public void getByName(Context mContext) {
        RequestParams entity = new RequestParams(Url.GET_BYNAME);
        entity.addQueryStringParameter("name", XunGenApp.surname);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        XunGenApp.surnameId = data.getString("id");
                    } else {
                        XunGenApp.surnameId = "";
                        switch (code) {
                            case 20021:
                                XunGenApp.message = "姓氏不存在";
                                break;
                            case 20023:
                                XunGenApp.message = "该姓氏正在审核中";
                                break;
                            case 20024:
                                XunGenApp.message = "该姓氏非法";
                                break;
                            default:
                                XunGenApp.message = "";
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("onError", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void getAddress(Context mContext, String region, final AddressInterface mAddress) {

        if (region == null) return;
        if (!StringUtil.isInteger(region)) {//如果region不是2234,6543,7899这种只有数字和逗号的情况下认为是汉字不需要转换
            mAddress.callBack(region);
            return;
        }
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("act", "getaddress");
        params.putString("id", region);
        mHttp.get(Url.CITY_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    mAddress.callBack(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }


    public void jiaTwoCode(Context mContext, String code, String type, final AddressInterface mAddress) {
        this.mContext = mContext;
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("code", code);
        params.putString("token", XunGenApp.token);
        params.putString("type", type);//1-订单数据加密；2-添加好友数据加密 3-宗亲会好友数据加密 4-讨论组数据加密
        mHttp.get(Url.TWO_CODE_JIA, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");//加密后的码
                    mAddress.callBack(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private Context mContext;
    private String reg = "^[0-9]+[_]+[\\s|\\S]*";

    public void jieTwoCode(final Context mContext, final String code) {
        if (code == null) return;
        Log.e("扫描结果", "" + code);
        this.mContext = mContext;
        if (code.startsWith("http")) {
            Intent intent = new Intent(mContext, H5Activity.class);
            intent.putExtra("url", code);
            mContext.startActivity(intent);
        } else {
            if (!XunGenApp.isLogin) {
                IntentSkip intentSkip = new IntentSkip();
                intentSkip.skipLogin(mContext, 0);
                return;
            }
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            final HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("code", code);
            params.putString("token", XunGenApp.token);
            mHttp.get(Url.TWO_CODE_JIE, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        String data = jsonObject.getString("data");
                        if (data == null) return;
                        if (data.matches(reg)) {//说明是自己app定义的方式
                            String[] split = data.split("\\_");
                            if (split.length == 2) {
                                switch (split[0]) {
                                    case "001"://扫描出来的是订单号
                                        consumOrder(mHttp, split[1]);
                                        break;
                                    case "002"://添加好友
                                        Intent intent = new Intent(mContext, FriendInfoActivity.class);
                                        intent.putExtra("rid", split[1]);
                                        mContext.startActivity(intent);
                                        break;
                                    case "003"://宗亲会
                                        // Intent intent1 = new Intent(mContext, AgnationActivity.class);
                                        Intent intent1 = new Intent(mContext, AgnationNewActivity.class);
                                        intent1.putExtra("id", split[1]);
                                        mContext.startActivity(intent1);
                                        break;
                                    case "004"://讨论组
                                        Intent intent2 = new Intent(mContext, JoinDiscussionActivity.class);
                                        intent2.putExtra("id", split[1]);
                                        mContext.startActivity(intent2);
                                        break;
                                }
                            }
                        } else {//无法进行解密
                            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                            hintDialogUtils.setVisibilityCancel();
                            hintDialogUtils.setTitle("扫描结果");
                            hintDialogUtils.setMessage(code);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                    hintDialogUtils.setVisibilityCancel();
                    hintDialogUtils.setTitle("扫描结果");
                    hintDialogUtils.setMessage(code);
                    loadingDialogUtils.setDimiss();
                }
            });
        }
    }

    private void consumOrder(HttpInterface mHttp, final String orderNo) {
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("orderNo", orderNo);
        mHttp.post(Url.CONSUME_ORDER, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Intent intent = new Intent(mContext, UnusedOrderActivity.class);
                intent.putExtra("orderNo", orderNo);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void getRecommend(final Context mContext, final int mode) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpUtils mHttp = (HttpUtils) HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        mHttp.getC(Url.RECOMMEND, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Gson gson = GsonUtils.getGson();
                        RecommendData recommendData = gson.fromJson(result, RecommendData.class);
                        RecommendData.DataBean data = recommendData.getData();
                        if (mode == 1) {//启动页跳转
                            Intent intent = new Intent(mContext, XunGenActivity.class);
                            mContext.startActivity(intent);
                            if (XunGenApp.isLogin) {
                                Intent intent1 = new Intent(mContext, RecommendActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("dataBean", data);
                                intent1.putExtras(bundle);
                                mContext.startActivity(intent1);
                            }
                            ((Activity) mContext).finish();
                            ((Activity) mContext).overridePendingTransition(R.anim.launch_in, R.anim.launch_out);
                        } else {//登录成功后的跳转
                            Intent intent = new Intent(mContext, RecommendActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("dataBean", data);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }

                    } else {
                        if (mode == 1) {
                            Intent intent = new Intent(mContext, XunGenActivity.class);
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                if (mode == 1) {
                    Intent intent = new Intent(mContext, XunGenActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                }
                loadingDialogUtils.setDimiss();
            }
        });
    }

    public interface UserInfoInterface {
        void callBack(UserInfoBean userInfoBean);
    }

    public interface AddressInterface {
        void callBack(String result);
    }
}
