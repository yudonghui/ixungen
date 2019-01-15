package com.ruihuo.ixungen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.action.ActionUserInfoActivity;
import com.ruihuo.ixungen.activity.AddNewsActivity;
import com.ruihuo.ixungen.activity.h5activity.H5Activity;
import com.ruihuo.ixungen.activity.merchant.MyShopsActivity;
import com.ruihuo.ixungen.activity.merchant.OrderFormActivity;
import com.ruihuo.ixungen.activity.merchant.ProcessActivity;
import com.ruihuo.ixungen.activity.useractivity.MyVideoActivity;
import com.ruihuo.ixungen.activity.useractivity.SettingActivity;
import com.ruihuo.ixungen.activity.useractivity.UserInfoDetailActivity;
import com.ruihuo.ixungen.adapter.Action2Adapter;
import com.ruihuo.ixungen.adapter.FriendStateAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IsJoinAgantion;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.entity.ActionBean;
import com.ruihuo.ixungen.entity.FriendStateBean;
import com.ruihuo.ixungen.entity.PayResult;
import com.ruihuo.ixungen.entity.UserInfoBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.activity.MyTreeActivity;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PaymentUtils;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.MyListView;
import com.squareup.picasso.Picasso;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ author yudonghui
 * @ date 2017/3/28
 * @ describe May the Buddha bless bug-free！！
 */
public class UserFragment extends Fragment {
    private View view;
    private CircleImageView mHeader;
    private SmartRefreshLayout mRefresh;
    //实名认证小图标
    private ImageView mProve;
    private TextView mUserName;
    private TextView mRootId;
    //我的家谱
    private LinearLayout mLlGenealogy;
    //我的宗亲
    private LinearLayout mLlAgnation;
    //我的帖子
    private LinearLayout mLlArticle;
    //我的视频
    private LinearLayout mLlVideo;
    //我的空间
    private LinearLayout mLlKongjian;
    //设置
    private LinearLayout mLlSetting;
    //我的订单
    private LinearLayout mLl_allorder;
    //我的店铺
    private LinearLayout mLlShops;
    private MyGridView mGridView2;
    //发布我的动态
    private ImageView mAddDy;
    //我要加盟
    private LinearLayout mLlAddShops;
    private MyListView mDynamicLv;
    private Context mContext;
    private UserInfoBean userInfoBean;
    private String clanAssociationIds;
    private static final int USER_FRAGMENT = 111;
    private Action2Adapter mActionAdapter;//名人，国学诵
    private List<ActionBean.DataBean> actionList = new ArrayList<>();
    private FriendStateAdapter mFriendAdapter;
    private List<FriendStateBean.DataBean> dynamicList = new ArrayList<>();

    public void setData() {
        addData();
    }

    private int page = 1;
    private int totalPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_user, null);
        mContext = getContext();
        initView();
        mActionAdapter = new Action2Adapter(actionList);
        mGridView2.setAdapter(mActionAdapter);
        mFriendAdapter = new FriendStateAdapter(dynamicList, mContext, 0);
        mDynamicLv.setAdapter(mFriendAdapter);
        addData();
        addListener();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        addData();
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
        mHeader = (CircleImageView) view.findViewById(R.id.user_header);
        mProve = (ImageView) view.findViewById(R.id.shiming);
        mUserName = (TextView) view.findViewById(R.id.user_name);
        mRootId = (TextView) view.findViewById(R.id.rootid);
        mLl_allorder = (LinearLayout) view.findViewById(R.id.ll_order);

        mLlGenealogy = (LinearLayout) view.findViewById(R.id.ll_genealogy);
        mLlAgnation = (LinearLayout) view.findViewById(R.id.ll_agnation);
        mLlArticle = (LinearLayout) view.findViewById(R.id.ll_article);
        mLlVideo = (LinearLayout) view.findViewById(R.id.ll_video);
        mLlKongjian = (LinearLayout) view.findViewById(R.id.ll_kongjian);
        mLlSetting = (LinearLayout) view.findViewById(R.id.ll_setting);
        mLlShops = (LinearLayout) view.findViewById(R.id.ll_shops);
        mLlAddShops = (LinearLayout) view.findViewById(R.id.ll_add_shops);
        mGridView2 = (MyGridView) view.findViewById(R.id.gridView2);
        mAddDy = view.findViewById(R.id.add_dy);
        mDynamicLv = view.findViewById(R.id.dynamic);
    }

    private void addListener() {
        mRefresh.setOnRefreshListener(RefreshListener);
        mRefresh.setOnLoadMoreListener(LoadMoreListener);
        //点击头像进入个人信息
        mHeader.setOnClickListener(UserInfoDetailListener);
        //我的家谱
        mLlGenealogy.setOnClickListener(GenealogyListener);
        //我的宗亲
        mLlAgnation.setOnClickListener(AgnationListener);
        //我的帖子
        mLlArticle.setOnClickListener(QuestionListener);
        //我的视频
        mLlVideo.setOnClickListener(VideoListener);
        //我的空间
        mLlKongjian.setOnClickListener(KongjianListener);
        //设置
        mLlSetting.setOnClickListener(SettingListener);
        //我要加盟
        mLlAddShops.setOnClickListener(AddShopsListener);
        //我的店铺
        mLlShops.setOnClickListener(ShopsListener);
        //我的订单
        mLl_allorder.setOnClickListener(OrderFormListener);
        //我的动态
        mAddDy.setOnClickListener(AddDyListener);
    }

    View.OnClickListener AddDyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AddNewsActivity.class);
            intent.putExtra("pid", "0");
            ((Activity) mContext).startActivityForResult(intent, 222);
        }
    };
    View.OnClickListener OrderFormListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, OrderFormActivity.class);
            intent.putExtra("from", ConstantNum.ORDER_ALLN);
            intent.putExtra("type", 0);//0-普通消费者查看订单列表；1-店家查看订单列表
            startActivity(intent);
        }
    };
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            actionList.clear();
            dynamicList.clear();
            page = 1;
            addData();
        }
    };
    OnLoadMoreListener LoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            if (page < totalPage) {
                page++;
                myDynamicData();
            } else {
                mRefresh.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                        mRefresh.finishLoadMore();
                    }
                }, 1000);
            }
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PaymentUtils.SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //   Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        //  isSuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    View.OnClickListener UserInfoDetailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (userInfoBean != null) {
                Intent intent = new Intent(mContext, UserInfoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInfoBean", userInfoBean);
                intent.putExtras(bundle);
                startActivityForResult(intent, USER_FRAGMENT);
            }
        }
    };
    View.OnClickListener GenealogyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(mContext, H5Activity.class);
            String url = Url.H5_MYGLEAN_URL + XunGenApp.rid + "/token/" + XunGenApp.token;
            intent.putExtra("url", url);
            startActivity(intent);*/
            Intent intent = new Intent(mContext, MyTreeActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener AgnationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (userInfoBean != null) {
                IsJoinAgantion isJoinAgantion = new IsJoinAgantion(mContext);
                isJoinAgantion.isJoin();
            }
        }
    };

    View.OnClickListener QuestionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, H5Activity.class);
            intent.putExtra("from", ConstantNum.MYQUESTION);
            startActivity(intent);
        }
    };
    View.OnClickListener VideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MyVideoActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener SettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SettingActivity.class);
            startActivityForResult(intent, 10);
        }
    };
    View.OnClickListener AddShopsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ProcessActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener ShopsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MyShopsActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener KongjianListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ActionUserInfoActivity.class);
            intent.putExtra("rid", XunGenApp.rid);
            startActivity(intent);
        }
    };

    private void addData() {
        actionData();//能工巧匠 名人堂
        myDynamicData();//我的动态
        NetWorkData netWorkData = new NetWorkData();
        netWorkData.getUserInfo(mContext, new NetWorkData.UserInfoInterface() {
            @Override
            public void callBack(UserInfoBean userInfoBean1) {
                mRefresh.finishRefresh();
                userInfoBean = userInfoBean1;
                String nikename = userInfoBean1.getData().getNikename();
                String rid = userInfoBean1.getData().getRid();
                clanAssociationIds = userInfoBean1.getData().getClanAssociationIds();
                mUserName.setText(nikename);
                mRootId.setText("根号：" + (TextUtils.isEmpty(rid) ? "" : rid));
                String shop_status = userInfoBean.getData().getShop_status();
                if ("0".equals(shop_status)) {//0：没有店铺，1：店铺存在
                    mLlShops.setVisibility(View.GONE);
                    mLlAddShops.setVisibility(View.VISIBLE);
                } else {
                    mLlShops.setVisibility(View.VISIBLE);
                    mLlAddShops.setVisibility(View.GONE);
                }
                mLlAddShops.setVisibility(View.GONE);
                mLlShops.setVisibility(View.GONE);
                Picasso.with(mContext)
                        .load(userInfoBean1.getData().getAvatar())
                        .placeholder(R.mipmap.default_header_clan)
                        .error(R.mipmap.default_header_clan)
                        .into(mHeader);
            }
        });
    }

    //刷新动态数据
    public void refreshDynamicData() {
        dynamicList.clear();
        page = 1;
        myDynamicData();
    }

    private void actionData() {
        Bundle params = new Bundle();
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        params.putString("bundle", "2");
        mHttp.get(Url.ACTION_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                actionList.clear();
                Gson gson = GsonUtils.getGson();
                ActionBean actionBean = gson.fromJson(result, ActionBean.class);
                actionList.addAll(actionBean.getData());
                mActionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void myDynamicData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("limit", "10");
        params.putString("page", page + "");
        params.putString("rid", XunGenApp.rid);
        params.putString("type", "2");//0-查看所有动态，1-获取宗亲会动态；2-获取自己发表的动态，3-好友动态 默认0
        mHttp.get(Url.FRIEND_STATUS, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                FriendStateBean friendStateBean = gson.fromJson(result, FriendStateBean.class);
                if (friendStateBean.getCode() == 0) {
                    dynamicList.addAll(friendStateBean.getData());
                    totalPage = friendStateBean.getTotalPage();
                    mFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                mFriendAdapter.notifyDataSetChanged();
            }
        });
    }


}
