package com.ruihuo.ixungen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.chatactivity.DiscussionFormActivity;
import com.ruihuo.ixungen.activity.family.NewFriendActivity;
import com.ruihuo.ixungen.activity.family.SearchFriendActivity;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.adapter.SearchFriendAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IsJoinAgantion;
import com.ruihuo.ixungen.common.friends.ContactAdapter;
import com.ruihuo.ixungen.common.friends.bean.Contact;
import com.ruihuo.ixungen.common.friends.utils.RecyclerViewDivider;
import com.ruihuo.ixungen.common.friends.utils.TinyPY;
import com.ruihuo.ixungen.common.friends.view.CenterTipView;
import com.ruihuo.ixungen.common.friends.view.RightIndexViewY;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class FamilyFragment extends Fragment implements RightIndexViewY.OnRightTouchMoveListener {
    private View view;
    private Context mContext;
    private ContactAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SearchViewY mSearchView;
    private RecyclerView recyclerView;
    private TextView tvHeader;//固定头view
    private CenterTipView tipView;//中间字母提示view
    private RightIndexViewY rightContainer;//右侧索引view

    private List<FriendBean.DataBean> memeberList = new ArrayList<>();//列表展示的数据
    private ArrayList<Contact> list = new ArrayList<>();//列表展示的数据（组装）
    private ArrayList<String> firstList = new ArrayList<>();//字母索引集合
    private HashSet<String> set = new HashSet<>();//中间临时集合
    private int limit = 2000;
    private int page = 1;
    private SearchFriendAdapter mSearchFriendAdapter;

    public void setRedPointsStatus(String redPointsStatus) {
        adapter.setRedPointsStatus(redPointsStatus);
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_family, null);
        mContext = getContext();
        initView();
        addData();
        addListener();
        return view;
    }

    private void initView() {
        mSearchView = view.findViewById(R.id.searchView);
        mSearchView.setBackGround(R.color.gray_bg);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //设置布局
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(LinearLayoutManager.VERTICAL, DisplayUtilX.dip2px(0.5), getResources().getColor(R.color.gray_bg)));
        adapter = new ContactAdapter(list, true, mContext);

        SharedPreferences sp = mContext.getSharedPreferences("redPoints", Context.MODE_PRIVATE);
        int redNewFriend = sp.getInt("redNewFriend", 0);

        adapter.setRedPointsStatus(redNewFriend > 0 ? "1" : "9");
        recyclerView.setAdapter(adapter);
        //固定头item
        tvHeader = (TextView) view.findViewById(R.id.tv_header);
        //center tip view
        tipView = (CenterTipView) view.findViewById(R.id.tv_center_tip);

        //右侧字母表索引
        rightContainer = (RightIndexViewY) view.findViewById(R.id.vg_right_container);

        //右侧字母索引容器注册touch回调
        rightContainer.setOnRightTouchMoveListener(this);


    }


    private void addListener() {
        //item的点击事件
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    //点击新朋友将红点去掉同时更改缓存里的状态。
                    SharedPreferences sp = mContext.getSharedPreferences("redPoints", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("redNewFriend", 0);
                    editor.commit();
                    //设置红点状态，唤醒适配器。
                    adapter.setRedPointsStatus("9");
                    TextView redPoints = (TextView) view.findViewById(R.id.red_point);
                    redPoints.setVisibility(View.GONE);

                    Intent intent = new Intent(mContext, NewFriendActivity.class);
                    startActivity(intent);
                    mCallBack.callBack();
                } else if (position == 1) {
                   /* Intent intent = new Intent(mContext, AgnationFormActivity.class);
                    startActivity(intent);*/
                    IsJoinAgantion isJoinAgantion = new IsJoinAgantion(mContext);
                    isJoinAgantion.isJoinGroup();

                } else if (position == 2) {
                    Intent intent = new Intent(mContext, DiscussionFormActivity.class);
                    startActivity(intent);
                } else {
                    Contact contact1 = list.get(position);
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("rid", contact1.rid);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 205);

                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /**
                 * 查找(width>>1,1)点处的view，差不多是屏幕最上边，距顶部1px
                 * recyclerview上层的header所在的位置
                 */
                View itemView = recyclerView.findChildViewUnder(tvHeader.getMeasuredWidth() >> 1, 1);

                /**
                 * recyclerview中如果有item占据了这个位置，那么header的text就为item的text
                 * 很显然，这个tiem是recyclerview的任意item
                 * 也就是说，recyclerview每滑过一个item，tvHeader就被赋了一次值
                 */
                if (itemView != null && itemView.getContentDescription() != null) {
                    tvHeader.setText(String.valueOf(itemView.getContentDescription()));
                }

                /**
                 * 指定可能印象外层header位置的item范围[-tvHeader.getMeasuredHeight()+1, tvHeader.getMeasuredHeight() + 1]
                 * 得到这个item
                 */
                View transInfoView = recyclerView.findChildViewUnder(
                        tvHeader.getMeasuredWidth() >> 1, tvHeader.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - tvHeader.getMeasuredHeight();
                    if (transViewStatus == ContactAdapter.SHOW_HEADER_VIEW) {
                        /**
                         * 如果这个item有tag参数，而且是显示header的，正好是我们需要关注的item的header部分
                         */
                        if (transInfoView.getTop() > 0) {
                            //说明item还在屏幕内，只是占据了外层header部分空间
                            tvHeader.setTranslationY(dealtY);
                        } else {
                            //说明item已经超出了recyclerview上边界，故此时外层的header的位置固定不变
                            tvHeader.setTranslationY(0);
                        }
                    } else if (transViewStatus == ContactAdapter.DISMISS_HEADER_VIEW) {
                        //如果此项的header隐藏了，即与外层的header无关，外层的header位置不变
                        tvHeader.setTranslationY(0);
                    }
                }
            }
        });
        //对搜索框的监听
        mSearchView.setSearchViewListener(SearchListener);
    }

    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            Intent intent = new Intent(mContext, SearchFriendActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("intentList", (Serializable) memeberList);
            bundle.putInt("type", 1);
            bundle.putString("searchContent", text);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onChange(String text) {

        }
    };


    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        //关系状态，0-正常，1-拉黑
        params.putString("status", "0");
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.post(Url.OBTAIN_FRIEND_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                memeberList.clear();
                list.clear();
                set.clear();
                firstList.clear();
                Gson gson = GsonUtils.getGson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                memeberList.addAll(friendBean.getData());
                refreshUserInfo();
                Contact contact = null;
                //是否有非字母数据(拼音首字符不在26个字母范围当中)
                boolean hasIncognizance = false;
                //装载非字母数据的结合
                ArrayList<Contact> incognizanceList = new ArrayList<>();
                //第一排
                Contact contact1 = new Contact();
                contact1.firstPinYin = "$";
                set.add(contact1.firstPinYin);
                list.add(contact1);
                list.add(contact1);
                list.add(contact1);
                for (int i = 0; i < memeberList.size(); i++) {
                    contact = new Contact();
                    contact.avatar = memeberList.get(i).getAvatar();
                    contact.rid = memeberList.get(i).getRid();
                    contact.name = memeberList.get(i).getNikename();
                    contact.sex = memeberList.get(i).getSex();
                    contact.birthday = memeberList.get(i).getBirthday();
                    contact.region = memeberList.get(i).getRegion();
                    contact.phone = memeberList.get(i).getPhone();
                    contact.pinYin = TinyPY.toPinYin(contact.name);
                    contact.firstPinYin = TinyPY.firstPinYin(contact.pinYin);
                    if (!TextUtils.isEmpty(contact.firstPinYin)) {
                        char first = contact.firstPinYin.charAt(0);
                        //A(65), Z(90), a(97), z(122) 根据数据的类型分开装进集合
                        if (first < 'A' || (first > 'Z' && first < 'a') || first > 'z') {
                            //非字母
                            contact.firstPinYin = "#";
                            //标记含有#集合
                            hasIncognizance = true;
                            //添加数据到#集合
                            incognizanceList.add(contact);
                        } else {
                            //字母索引(set可以去重复)
                            set.add(contact.firstPinYin);
                            //添加数据到字母a-z集合
                            list.add(contact);
                        }
                    }
                }
                //对contact集合数据排序
                Collections.sort(list);
                //把排序后的字母顺序装进字母索引集合
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    firstList.add(iterator.next());
                }

                Collections.sort(firstList);

                //最后加上#
                if (hasIncognizance) {
                    //把#装进索引集合
                    firstList.add("#");
                    //把非字母的contact数据装进数据集合
                    list.addAll(incognizanceList);
                }
                //清空中间缓存集合
                incognizanceList.clear();
                set.clear();
                if (list != null && list.size() > 0)
                    tvHeader.setText(list.get(0).firstPinYin);
                // rightContainer.setData(firstList);
                if (n > 1)
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message) {
            }
        });
    }

    /**
     * 右侧字母表touch回调
     *
     * @param position 当前touch的位置
     * @param content  当前位置的内容
     * @param isShow   显示与隐藏中间的tip view
     */
    @Override
    public void showTip(int position, String content, boolean isShow) {
        if (isShow) {
            tipView.setVisibility(View.VISIBLE);
            tipView.setText(content);
        } else {
            tipView.setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).firstPinYin.equals(content)) {
                recyclerView.stopScroll();
                int firstItem = layoutManager.findFirstVisibleItemPosition();
                int lastItem = layoutManager.findLastVisibleItemPosition();
                if (i <= firstItem) {
                    recyclerView.scrollToPosition(i);
                } else if (i <= lastItem) {
                    int top = recyclerView.getChildAt(i - firstItem).getTop();
                    recyclerView.scrollBy(0, top);
                } else {
                    recyclerView.scrollToPosition(i);
                }
                break;
            }
        }
    }

    public void refreshUserInfo() {
        for (int i = 0; i < memeberList.size(); i++) {
            FriendBean.DataBean dataBean = memeberList.get(i);
            String avatar = dataBean.getAvatar();
            Uri uri = Uri.parse(avatar);
            UserInfo userInfo = new UserInfo(dataBean.getRid(), dataBean.getNikename(), uri);
            RongIM.getInstance().refreshUserInfoCache(userInfo);
        }
    }

    private CallBackInterface mCallBack;

    public void setListener(CallBackInterface mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setData() {
        ToastUtils.toast(mContext, "已删除好友");
        addData();
    }

    private int n = 0;//第一次点击家族这个tab的时候不去更新adapter。会自动更新。如果点击大于1了那么就是需要更新adapter

    public void setDatac() {
        n++;
        addData();
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        try {
            switch (requestCode) {
                case ConstantNum.PERMISSION_CAMERA:
                    //调取扫描二维码界面
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
                    break;
            }
        } catch (Exception e) {

        }
    }

}
