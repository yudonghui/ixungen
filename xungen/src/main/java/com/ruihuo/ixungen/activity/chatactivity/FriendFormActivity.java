package com.ruihuo.ixungen.activity.chatactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.useractivity.FriendInfoActivity;
import com.ruihuo.ixungen.adapter.FriendFormsAdapter;
import com.ruihuo.ixungen.adapter.IVRecyclerAdapter;
import com.ruihuo.ixungen.adapter.SpaceItemDecoration;
import com.ruihuo.ixungen.common.friends.ContactAdapter;
import com.ruihuo.ixungen.common.friends.bean.Contact;
import com.ruihuo.ixungen.common.friends.utils.RecyclerViewDivider;
import com.ruihuo.ixungen.common.friends.utils.TinyPY;
import com.ruihuo.ixungen.common.friends.view.CenterTipView;
import com.ruihuo.ixungen.common.friends.view.RightIndexViewY;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.UserInfo;

public class FriendFormActivity extends AppCompatActivity implements RightIndexViewY.OnRightTouchMoveListener {
    private Context mContext;
    private FriendFormsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TitleBar mTitleBar;
    // private SearchViewY mSearchView;
    private android.support.v7.widget.RecyclerView mRecyclerViewTop;
    private EditText mSearch_et_input;
    private ImageView mSearch_iv_delete;
    private RecyclerView recyclerView;
    private TextView tvHeader;//固定头view
    private CenterTipView tipView;//中间字母提示view
    private RightIndexViewY rightContainer;//右侧索引view

    private String from;//true,添加好友到讨论组
    private List<FriendBean.DataBean> memeberList = new ArrayList<>();//列表展示的数据
    private ArrayList<Contact> list = new ArrayList<>();//列表展示的数据（组装）
    private ArrayList<String> firstList = new ArrayList<>();//字母索引集合
    private HashSet<String> set = new HashSet<>();//中间临时集合
    private int limit = 2000;
    private int page = 1;
    private ArrayList<String> memberIdList = new ArrayList<>();
    private String discussionId;
    private IVRecyclerAdapter mAdapterTop;
    private String associationId;
    private List<FriendBean.DataBean> memeberedList;//宗亲会已经有的成员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_form);
        mContext = this;
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        memberIdList = intent.getStringArrayListExtra("memberIdList");
        discussionId = intent.getStringExtra("discussionId");
        associationId = intent.getStringExtra("associationId");
        memeberedList = (List<FriendBean.DataBean>) intent.getSerializableExtra("memeberList");
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.friend_titlebar);
        mTitleBar.mImageBack.setVisibility(View.GONE);
        mTitleBar.mTextRegister.setText("完成");
        mTitleBar.mTextRegister.setVisibility(View.VISIBLE);

        mRecyclerViewTop = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerViewTop);
        mSearch_et_input = (EditText) findViewById(R.id.search_et_input);
        mSearch_iv_delete = (ImageView) findViewById(R.id.search_iv_delete);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //设置布局
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL));
        adapter = new FriendFormsAdapter(list, mContext);
        recyclerView.setAdapter(adapter);
        //固定头item
        tvHeader = (TextView) findViewById(R.id.tv_header);
        //center tip view
        tipView = (CenterTipView) findViewById(R.id.tv_center_tip);

        //右侧字母表索引
        rightContainer = (RightIndexViewY) findViewById(R.id.vg_right_container);

        //右侧字母索引容器注册touch回调
        rightContainer.setOnRightTouchMoveListener(this);

        switch (from) {
            case "inviteAgnation":
                mTitleBar.setTitle("邀请加入宗亲会");
                mTitleBar.mTextRegister.setText("发送");
                adapter.setIsDelete(true);
                break;
            case "creatDiscussion":
                mTitleBar.setTitle("创建讨论组");
                adapter.setIsDelete(true);
                break;
            case "addFriendToDiscussion":
                mTitleBar.setTitle("添加到讨论组");
                adapter.setIsDelete(true);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(memberIdList);
                memberIdList.clear();
                for (int i = 0; i < arrayList.size(); i++) {
                    String rid = arrayList.get(i);
                    if (!"add".equals(rid) && !"delete".equals(rid)) {
                        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
                        if (userInfo != null) {
                            String name = userInfo.getName();
                            ridMap.put(rid, name);
                            memberIdList.add(rid);
                        }
                    }
                }
                break;
            case "deleteFriendFromDiscussion":
                mTitleBar.setTitle("从讨论组移除");
                adapter.setIsDelete(true);
                ArrayList<String> arrayList1 = new ArrayList<>();
                arrayList1.addAll(memberIdList);
                memberIdList.clear();
                for (int i = 0; i < arrayList1.size(); i++) {
                    String rid = arrayList1.get(i);
                    if (!"add".equals(rid) && !"delete".equals(rid)) {
                        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rid);
                        if (userInfo != null) {
                            String name = userInfo.getName();
                            ridMap.put(rid, name);
                            memberIdList.add(rid);
                        }
                    }
                }
                break;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewTop.setLayoutManager(linearLayoutManager);
        mRecyclerViewTop.addItemDecoration(new SpaceItemDecoration(DisplayUtilX.dip2px(5)));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (memberIdList != null && memberIdList.size() >= 4) {
            layoutParams.weight = 1;
            mRecyclerViewTop.setLayoutParams(layoutParams);
        } else {
            layoutParams.weight = 0;
            mRecyclerViewTop.setLayoutParams(layoutParams);
        }
        if (memberIdList == null) memberIdList = new ArrayList<>();
        mAdapterTop = new IVRecyclerAdapter(mContext, memberIdList, RecyclerViewListener);
        mRecyclerViewTop.setAdapter(mAdapterTop);
    }

    private void addData() {
        loadingDialogUtils = new LoadingDialogUtils(mContext);
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
                loadingDialogUtils.setDimiss();
                memeberList.clear();
                list.clear();
                set.clear();
                firstList.clear();
                Gson gson = GsonUtils.getGson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                List<FriendBean.DataBean> data = friendBean.getData();

                switch (from) {
                    case "inviteAgnation"://邀请宗亲会
                        if (memeberedList != null) {
                            List<String> idList = new ArrayList<String>();
                            for (int i = 0; i < memeberedList.size(); i++) {
                                FriendBean.DataBean dataBean = memeberedList.get(i);
                                String rid = dataBean.getRid();
                                idList.add(rid);
                            }
                            for (int i = 0; i < data.size(); i++) {
                                FriendBean.DataBean dataBean = data.get(i);
                                String rid = dataBean.getRid();
                                if (!idList.contains(rid)) memeberList.add(dataBean);
                            }
                            dataList.addAll(memeberList);//把原始数据存起来
                            refreshUserInfo();
                            updateData();//处理数据，并且更新适配器
                        } else {//可能是宗亲会首页进来邀请的时候
                            agnationMemeber(data);
                        }
                        break;
                    case "creatDiscussion":
                        memeberList.addAll(data);
                        dataList.addAll(memeberList);//把原始数据存起来
                        refreshUserInfo();
                        updateData();//处理数据，并且更新适配器
                        break;
                    case "addFriendToDiscussion":
                        for (int i = 0; i < data.size(); i++) {//将已经在讨论组,或者宗亲会中的好友去掉
                            FriendBean.DataBean dataBean = data.get(i);
                            String rid = dataBean.getRid();
                            if (!memberIdList.contains(rid)) {
                                memeberList.add(dataBean);
                            }
                        }
                        dataList.addAll(memeberList);//把原始数据存起来
                        refreshUserInfo();
                        updateData();//处理数据，并且更新适配器
                        break;
                    case "deleteFriendFromDiscussion":
                        for (int i = 0; i < data.size(); i++) {//这里要显示的全是讨论组中的朋友
                            FriendBean.DataBean dataBean = data.get(i);
                            String rid = dataBean.getRid();
                            if (memberIdList.contains(rid)) {
                                memeberList.add(dataBean);
                            }
                        }
                        dataList.addAll(memeberList);//把原始数据存起来
                        refreshUserInfo();
                        updateData();//处理数据，并且更新适配器
                        break;
                }


            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private void agnationMemeber(final List<FriendBean.DataBean> data) {
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("associationId", associationId);
        params.putString("status", "1");
        params.putString("limit", limit + "");
        mHttp.get(Url.AGNATION_MEMEBER_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = new Gson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                List<FriendBean.DataBean> dataBeanList = friendBean.getData();
                if (dataBeanList != null) {
                    List<String> idList = new ArrayList<String>();
                    for (int i = 0; i < dataBeanList.size(); i++) {
                        FriendBean.DataBean dataBean = dataBeanList.get(i);
                        String rid = dataBean.getRid();
                        idList.add(rid);
                    }
                    for (int i = 0; i < data.size(); i++) {
                        FriendBean.DataBean dataBean = data.get(i);
                        String rid = dataBean.getRid();
                        if (!idList.contains(rid)) memeberList.add(dataBean);
                    }
                } else {
                    memeberList.addAll(data);
                }
                dataList.addAll(memeberList);//把原始数据存起来
                refreshUserInfo();
                updateData();//处理数据，并且更新适配器

            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    private HashMap<String, String> ridMap = new HashMap<>();

    private void addListener() {
        //item的点击事件
        adapter.setOnItemClickListener(new FriendFormsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ImageView imageView = (ImageView) view.findViewById(R.id.isSelect);
                Contact contact = list.get(position);
                String rid = contact.rid;
                String name = contact.name;
                if (imageView.getVisibility() == View.VISIBLE) {
                    if (ridMap.containsKey(rid)) {
                        imageView.setImageResource(R.mipmap.unselect_friend);
                        ridMap.remove(rid);
                    } else {
                        imageView.setImageResource(R.mipmap.select_friend);
                        ridMap.put(rid, name);
                    }

                }
                if (memberIdList == null) memberIdList = new ArrayList<String>();
                memberIdList.clear();
                for (Map.Entry<String, String> entry : ridMap.entrySet()) {
                    memberIdList.add(entry.getKey());
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (memberIdList != null && memberIdList.size() >= 4) {
                    layoutParams.weight = 1;
                    mRecyclerViewTop.setLayoutParams(layoutParams);
                } else {
                    layoutParams.weight = 0;
                    mRecyclerViewTop.setLayoutParams(layoutParams);
                }
                mAdapterTop.notifyDataSetChanged();
            }
        });
        mTitleBar.mTextRegister.setOnClickListener(FinishListener);
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
        mSearch_et_input.addTextChangedListener(ChangTextListener);
    }

    private ArrayList<String> ridList = new ArrayList<>();
    private int m = 0;
    private LoadingDialogUtils loadingDialogUtils;
    View.OnClickListener FinishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ridList.clear();
            if (ridMap == null) return;
            if (ridMap.size() == 1 && !"inviteAgnation".equals(from)) {
                for (Map.Entry<String, String> entry : ridMap.entrySet()) {
                    String rid = entry.getKey();
                    String name = entry.getValue();
                    RongIM.getInstance().startPrivateChat(mContext, rid, name);
                }
                return;
            }
            final StringBuilder names = new StringBuilder();
            StringBuilder userRids = new StringBuilder();
            int n = 0;
            for (Map.Entry<String, String> entry : ridMap.entrySet()) {
                String rid = entry.getKey();
                String name = entry.getValue();
                if (n == 0) {
                    names.append(name);
                    userRids.append(rid);
                } else {
                    names.append("," + name);
                    userRids.append("," + rid);
                }
                ridList.add(rid);
                Log.e("选中的人的根号", rid);
                n++;
            }
            switch (from) {
                case "inviteAgnation"://这里对选中的朋友发通知邀请加入宗亲会
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    HttpUtils mHttp = new HttpUtils();
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("associationId", associationId);
                    params.putString("userRids", String.valueOf(userRids));
                    mHttp.postW(Url.AGNATION_APPLY, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {//发送邀请成功
                                    finish();
                                } else if (code == -1) {//发送邀请全部失败
                                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                                    hintDialogUtils.setMessage(msg);
                                    hintDialogUtils.setVisibilityCancel();
                                } else if (code == -2) {//发送的邀请部分失败
                                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                                    hintDialogUtils.setMessage(msg);
                                    hintDialogUtils.setCancelable(false);
                                    hintDialogUtils.setVisibilityCancel();
                                    hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                                        @Override
                                        public void callBack(View view) {
                                            finish();
                                        }
                                    });
                                } else {//其他异常
                                    HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                                    hintDialogUtils.setMessage(msg);
                                    hintDialogUtils.setVisibilityCancel();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                    break;
                case "creatDiscussion":
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    RongIM.getInstance().createDiscussion(String.valueOf(names), ridList, new RongIMClient.CreateDiscussionCallback() {
                        @Override
                        public void onSuccess(final String s) {
                            loadingDialogUtils.setDimiss();
                            RongIM.getInstance().getDiscussion(s, new RongIMClient.ResultCallback<Discussion>() {
                                @Override
                                public void onSuccess(Discussion discussion) {
                                    RongIM.getInstance().refreshDiscussionCache(discussion);
                                    RongIM.getInstance().startDiscussionChat(mContext, s, String.valueOf(names));
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });

                            finish();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            loadingDialogUtils.setDimiss();
                            Toast.makeText(mContext, "创建失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    break;
                case "addFriendToDiscussion":
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    RongIM.getInstance().addMemberToDiscussion(discussionId, ridList, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            loadingDialogUtils.setDimiss();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("ridList", ridList);
                            intent.putExtras(bundle);
                            setResult(666, intent);
                            finish();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            loadingDialogUtils.setDimiss();
                            Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    break;
                case "deleteFriendFromDiscussion":
                    loadingDialogUtils = new LoadingDialogUtils(mContext);
                    m = 0;
                    for (int i = 0; i < ridList.size(); i++) {
                        RongIM.getInstance().removeMemberFromDiscussion(discussionId, ridList.get(i), new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                if (m >= ridList.size() - 1) {
                                    loadingDialogUtils.setDimiss();
                                    Intent intent = new Intent();

                                    setResult(666, intent);
                                    finish();
                                }
                                m++;
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                loadingDialogUtils.setDimiss();
                                Toast.makeText(mContext, "移除失败", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    break;
            }
        }
    };
    private ArrayList<FriendBean.DataBean> dataList = new ArrayList<>();
    TextWatcher ChangTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            memeberList.clear();
            for (int i = 0; i < dataList.size(); i++) {
                FriendBean.DataBean dataBean = dataList.get(i);
                String nikename = dataBean.getNikename();
                if (nikename.contains(s)) {
                    memeberList.add(dataBean);
                }
            }
            list.clear();
            set.clear();
            firstList.clear();
            updateData();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    CallBackPositionInterface RecyclerViewListener = new CallBackPositionInterface() {
        @Override
        public void callBack(int position) {
            String rid = memberIdList.get(position);
            Intent intent = new Intent(mContext, FriendInfoActivity.class);
            intent.putExtra("rid", rid);
            startActivity(intent);
        }
    };

    private void updateData() {
        Contact contact = null;
        //是否有非字母数据(拼音首字符不在26个字母范围当中)
        boolean hasIncognizance = false;
        //装载非字母数据的结合
        ArrayList<Contact> incognizanceList = new ArrayList<>();
        //第一排
        Contact contact1 = new Contact();
        contact1.firstPinYin = "$";
        set.add(contact1.firstPinYin);
              /*  list.add(contact1);
                list.add(contact1);*/
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
        adapter.updateData(list);
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
}
