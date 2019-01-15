package com.ruihuo.ixungen.activity.useractivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.chatactivity.FriendFormActivity;
import com.ruihuo.ixungen.adapter.AgnationMemeberAdapter;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.entity.AgnationFormBean;
import com.ruihuo.ixungen.entity.FriendBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ItemClickListener;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.SearchViewY;
import com.ruihuo.ixungen.view.TitleBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgnationMemeberActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private SmartRefreshLayout mRefresh;
    private ListView mListView;
    private SearchViewY mSearchView;
    private LinearLayout mLl_newmemeber;
    private View view;
    private TextView mDelete;
    private Context mContext;
    private String from;
    private String presidentId;
    private String associationId;//宗亲会id
    private List<FriendBean.DataBean> memeberList = new ArrayList<>();//列表展示的数据
    private List<FriendBean.DataBean> saveList = new ArrayList<>();
    private AgnationMemeberAdapter mAdapter;
    //存放点击选中的条目
    private HashMap<Integer, String> map = new HashMap<>();
    private int limit = 10000;

    private View inflate;
    private PopupWindow mPopupWindow;
    private TextView mPopup_question;
    private View mFirstline;
    private TextView mPopup_smshint;
    private View mSecondline;
    private TextView mPopup_three;
    private AgnationFormBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnation_memeber1);
        mContext = this;
        Intent intent = getIntent();
        associationId = intent.getStringExtra("associationId");
        from = intent.getStringExtra("from");
        presidentId = intent.getStringExtra("presidentId");
        dataBean = (AgnationFormBean.DataBean) intent.getSerializableExtra("dataBean");
        initView();
        mAdapter = new AgnationMemeberAdapter(memeberList, ItemClick);
        mListView.setAdapter(mAdapter);
        addData();
        addListener();
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mTitleBar = (TitleBar) findViewById(R.id.memeber_titlebar);
        mTitleBar.setTitle("宗亲成员");
        if ("AgationActivity".equals(from)) {
            mTitleBar.mShare.setVisibility(View.VISIBLE);
        } else mTitleBar.mShare.setVisibility(View.GONE);
        mTitleBar.mShare.setImageResource(R.drawable.more);
        mListView = (ListView) findViewById(R.id.ptlv);
        mSearchView = (SearchViewY) findViewById(R.id.searchview);

        mLl_newmemeber = (LinearLayout) findViewById(R.id.newMemeber);
        view = findViewById(R.id.view);
        if (XunGenApp.rid.equals(presidentId)) {
            mLl_newmemeber.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        } else {
            mLl_newmemeber.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
        mSearchView.setHint("请输入名称/根号");
        mDelete = (TextView) findViewById(R.id.delete);

        inflate = View.inflate(mContext, R.layout.popup_window, null);

        mPopup_question = (TextView) inflate.findViewById(R.id.popup_question);
        mPopup_question.setVisibility(View.VISIBLE);
        mPopup_question.setText("邀请成员");
        mFirstline = (View) inflate.findViewById(R.id.firstline);
        mFirstline.setVisibility(View.VISIBLE);
        mPopup_smshint = (TextView) inflate.findViewById(R.id.popup_smshint);
        mPopup_smshint.setText("扫一扫");
        mPopup_smshint.setVisibility(View.VISIBLE);
        mPopup_three = (TextView) inflate.findViewById(R.id.popup_look);
        mPopup_three.setText("宗亲会二维码");
        mPopup_three.setVisibility(View.VISIBLE);
        mSecondline = (View) inflate.findViewById(R.id.secondline);
        mSecondline.setVisibility(View.VISIBLE);

        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(DisplayUtilX.dip2px(150));
        mPopupWindow.setHeight(DisplayUtilX.dip2px(140));
        mPopupWindow.setContentView(inflate);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOnDismissListener(poponDismissListener);
    }

    private boolean isEditor = true;

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAsDropDown(mTitleBar.mShare, -DisplayUtilX.dip2px(130), 0);
                WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                lp.alpha = 0.6f;
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
        //邀请成员
        mPopup_question.setOnClickListener(InviteListener);
        //扫一扫
        mPopup_smshint.setOnClickListener(SaoSaoListener);
        //宗亲会二维码
        mPopup_three.setOnClickListener(AgnationCodeListener);
        //新申请的成员
        mLl_newmemeber.setOnClickListener(NewMemeberListener);
      /*  mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditor) {
                    isEditor = false;
                    mTitleBar.mTextRegister.setText("取消");
                    mDelete.setVisibility(View.VISIBLE);
                    mAdapter.isCheck(true);
                    mAdapter.notifyDataSetChanged();
                } else {
                    isEditor = true;
                    mTitleBar.mTextRegister.setText("编辑");
                    mDelete.setVisibility(View.GONE);
                    mAdapter.isCheck(false);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });*/
        //批量删除成员
        mDelete.setOnClickListener(DeleteListener);
        mRefresh.setOnRefreshListener(RefreshListener);
        mSearchView.setSearchViewListener(SearchListener);
    }

    View.OnClickListener InviteListener = new View.OnClickListener() {//邀请成员
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            Intent intent = new Intent(mContext, FriendFormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("from", "inviteAgnation");
            bundle.putString("associationId", associationId);
            bundle.putSerializable("memeberList", (Serializable) memeberList);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    View.OnClickListener SaoSaoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ConstantNum.PERMISSION_CAMERA);
            } else {
                //调取二维码扫描
                Intent intent = new Intent(mContext, CaptureActivity.class);
                ((Activity) mContext).startActivityForResult(intent, ConstantNum.REQUEST_CAMERA);
            }
        }
    };
    View.OnClickListener AgnationCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupWindow.dismiss();
            if (dataBean == null) return;
            Intent intent = new Intent(mContext, TwoCodeActivity.class);
            intent.putExtra("city", dataBean.getArea_id_str());
            intent.putExtra("userName", dataBean.getName());
            intent.putExtra("avatar", dataBean.getImg_url());
            intent.putExtra("type", ConstantNum.AGNATION_TYPE);
            intent.putExtra("code", dataBean.getId());
            startActivity(intent);
        }
    };
    View.OnClickListener NewMemeberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, NewMemeberActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("associationId", associationId);
            bundle.putString("presidentId", presidentId);
            bundle.putString("from", "AgationActivity");
            bundle.putSerializable("dataBean", dataBean);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    PopupWindow.OnDismissListener poponDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 1f;
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    };
    ItemClickListener ItemClick = new ItemClickListener() {
        @Override
        public void onLongClick(View v, int position) {
            if ((XunGenApp.rid).equals(presidentId) && isEditor && "AgationActivity".equals(from)) {
                isEditor = false;
                mDelete.setVisibility(View.VISIBLE);
                mAdapter.isCheck(true);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onClick(View v, int position) {
            if ("AgationActivity".equals(from)) {
                //来自宗亲成员
                if (isEditor) {
                    //直接跳转。又上角这个时候是显示编辑
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    Bundle bundle = new Bundle();
                    FriendBean.DataBean dataBean = memeberList.get(position);
                    bundle.putString("rid", dataBean.getRid());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 202);
                } else {
                    ImageView imageView = (ImageView) v.findViewById(R.id.ischeck);
                    if (map.containsKey(position)) {
                        imageView.setImageResource(R.drawable.unselect);
                        map.remove(position);
                    } else {
                        imageView.setImageResource(R.drawable.select);
                        map.put(position, memeberList.get(position).getRid());
                    }
                }

            } else {
                //来自宗亲职务
                Intent intent = new Intent();
                intent.putExtra("nikeName", memeberList.get(position).getNikename());
                intent.putExtra("rid", memeberList.get(position).getRid());
                setResult(300, intent);
                finish();
            }
        }
    };
    SearchViewY.SearchViewListener SearchListener = new SearchViewY.SearchViewListener() {
        @Override
        public void onDelete() {

        }

        @Override
        public void onSearch(String text) {
            if (!TextUtils.isEmpty(text)) {
                memeberList.clear();
                for (int i = 0; i < saveList.size(); i++) {
                    FriendBean.DataBean dataBean = saveList.get(i);
                    String nikename = dataBean.getNikename();
                    String rid = dataBean.getRid();
                    if ((!TextUtils.isEmpty(nikename) && nikename.contains(text)) || (!TextUtils.isEmpty(rid) && rid.contains(text))) {
                        memeberList.add(dataBean);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancel() {
            memeberList.clear();
            memeberList.addAll(saveList);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChange(String text) {

        }
    };
    OnRefreshListener RefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            memeberList.clear();
            saveList.clear();
            addData();
        }
    };
    View.OnClickListener DeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (map.size() == 0) {
                ToastUtils.toast(mContext, "未选中删除项");
            } else {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                StringBuilder rid = new StringBuilder();
                int i = 0;
                //循环取出编号
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (i == 0) {
                        rid.append(entry.getValue());
                    } else rid.append("," + entry.getValue());
                    i++;
                }
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("id", associationId);
                params.putString("userRid", String.valueOf(rid));
                mHttp.post(Url.QUIT_AGNATION_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        //成功的时候更新数据源
                        mAdapter.isCheck(false);
                        mDelete.setVisibility(View.GONE);
                        mTitleBar.mTextRegister.setText("编辑");
                        isEditor = true;
                        memeberList.clear();
                        addData();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        }
    };

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
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
                mRefresh.finishRefresh();
                Gson gson = new Gson();
                FriendBean friendBean = gson.fromJson(result, FriendBean.class);
                memeberList.addAll(friendBean.getData());
                saveList.addAll(friendBean.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
